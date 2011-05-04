/*
 * RIBAX, Making Web Applications Easy 
 * Copyright (C) 2006 Damian Hamill and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ribax.swing.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.TransferHandler;
import javax.swing.JComponent;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JToolTip;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import org.ribax.common.Messages;
import org.ribax.common.data.DataChangeListener;
import org.ribax.common.data.DataModel;
import org.ribax.common.data.DataModelManager;

import utils.log.BasicLogger;
import utils.table.JMultiLineToolTip;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * A data item that holds an arbitrary amount of data and can have an associated 
 * picture.  Each element of data can be made visible (default is invisible) and it will
 * be displayed along with the picture
 * 
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class DataSetDataItem extends DataItem {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(DataSetDataItem.class.getName());
    /** data flavours for drag and drop data transfer */
    public static DataFlavor flavors[] = new DataFlavor[2];

    static {
        try {
            flavors[0] = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=org.ribax.swing.ui.DataItem"); //$NON-NLS-1$
            flavors[1] = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=java.util.ArrayList"); //$NON-NLS-1$

        } catch (ClassNotFoundException e) {
            LOG.error(Messages.getString(BUNDLE_NAME, "DataSetDataItem.2") + e); //$NON-NLS-1$
        }
    }
    /** The data set associated with this DatasetDatItem */
    private ArrayList<DataElement> data;
    /** */
    private Element savedElement = null;
    /** An image associated with this DatasetDatItem */
    private ImageIcon image = null;
    /** The URL to an image associated with this DatasetDatItem */
    private String imageURL = null;
    /** a button that can be set to post the data to a web service */
    private JButton button = null;

    /**
     * No argument Constructor - required
     */
    public DataSetDataItem() {

        // setup dummy tooltip text so this component is registered with the
        // ToolTipManager instance
        setToolTipText(""); //$NON-NLS-1$

        // use a vertical box as the layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // recognize a drag gesture
        MouseListener ml = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                JComponent c = (JComponent) e.getSource();
                TransferHandler th = c.getTransferHandler();
                th.exportAsDrag(c, e, TransferHandler.COPY);
            }
        };
        addMouseListener(ml);
    }

    /**
     * 
     * Add a new element of data to the data set.
     * 
     * @param tag the data element name
     * @param value the data element value
     * @param visible whether the data is visible or not
     */
    public void addDataElement(String tag, String value, int visible) {
        data.add(new DataElement(tag, value, visible));

        // each time a new data element is added then layout the component again
        layoutComponents();
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element node) {

        // read the basic attributes
        super.readDescription(node);

        // read the description using the setData method()
        setData(node);

        // if a model has been specified then add a listener to the model 
        // so if any data changes in the model so we can update the internal data
        if (model != null) {
            if (modelPath == null) {
                modelPath = getPath();
            }

            // get the Element tree corresponding to our path in the model
            Object o = model.getElement(modelPath);

            // update the internal data
            if (o == null) {
                LOG.error(Messages.getString(BUNDLE_NAME, "DataSetDataItem.4") + modelPath); //$NON-NLS-1$
            } else if (o instanceof Element) {
                setData((Element) o);
            }

            // add a listener to the model
            model.addDataChangeListener(new DataChangeListener() {

                public void dataChanged(DataModel model, String path) {

                    // check that we are interested in this data
                    if (path == null || !modelPath.equals(path)) {
                        return;
                    }

                    Object o = model.getElement(modelPath);

                    if (o != null && o instanceof Element) {
                        setData((Element) o);
                    }
                }
            });
        }
    }

    /**
     * Create a JPanel to host the button.
     * 
     * @return
     */
    protected Component getButtonPanel() {
        JPanel panel = new JPanel();

        panel.add(button);
        return panel;
    }

    /**
     * Layout the components of this DataItem.  Layout a picture and any visible 
     * data elements in a vertical stack.
     */
    private void layoutComponents() {

        removeAll();

        // add any image at the top of the stack
        if (image != null) {
            JLabel label = new JLabel();
            label.setIcon(image);
            add(label);
        }

        // iterate through the data elements
        Iterator iter = data.iterator();
        while (iter.hasNext()) {
            DataElement de = (DataElement) iter.next();

            /* if this data element is visible then create a new JLabel with the data
             * content and add it to the stack.  The kind of visibility determines exactly
             * what gets displayed.  If the visibility value == 2 then both the name of
             * the data element and the value are displayed otherwise only the value is
             * displayed
             */
            if (de.visible > 0) {
                JLabel label = new JLabel();
                if (de.visible == 2) {
                    label.setText(de.name + ": " + de.value); //$NON-NLS-1$
                } else {
                    label.setText(de.value);
                }
                add(label);
            }
        }

        // if a button has been specified then add it
        if (button != null) {
            add(getButtonPanel());
        }

        revalidate();
        repaint();
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */
    public void setData(Element node) {
        Element e;

        // initialise the data to a clean state
        data = new ArrayList<DataElement>();
        button = null;
        image = null;

        setBorder(null);

        // get the specification of an image URL
        imageURL = XMLutils.getElementString("image", node); //$NON-NLS-1$

        if (imageURL != null) {
            // load the image
            try {
                URL u = new URL(imageURL);

                image = new ImageIcon(u);

            } catch (Exception ex) {
                LOG.error(name, ex);
            }
        }

        // get the specification of the border
        if ((e = node.getChild("border")) != null) { //$NON-NLS-1$
            String title = XMLutils.getElementString("title", e); //$NON-NLS-1$
            Integer intval = XMLutils.getElementInt("width", e); //$NON-NLS-1$

            Border border = null;

            if (intval != null) {
                border = BorderFactory.createLineBorder(Color.BLACK, intval.intValue());
            } else {
                // otherwise create a compound bevel border
                border = BorderFactory.createCompoundBorder(
                        BorderFactory.createBevelBorder(BevelBorder.RAISED),
                        BorderFactory.createBevelBorder(BevelBorder.LOWERED));

            }
            // if the title is not null add the title to the border
            // centered on the top line
            if (title != null) {
                border = BorderFactory.createTitledBorder(border, title,
                        TitledBorder.CENTER, TitledBorder.TOP);
            }

            setBorder(border);
        }

        // read a set of data elements
        if ((e = node.getChild("data")) != null) { //$NON-NLS-1$

            // iterate through the children of the data node
            // each child is a DataElement 
            List<Element> dilist = e.getChildren();
            Iterator<Element> it = dilist.iterator();
            while (it.hasNext()) {
                Element element = it.next();

                int visible = 0;

                // get the attributes of the DataElement
                String tag = XMLutils.getElementString("name", element); //$NON-NLS-1$
                String value = XMLutils.getElementString("value", element); //$NON-NLS-1$
                String vis = XMLutils.getElementString("visible", element); //$NON-NLS-1$

                if (vis != null) {
                    Boolean boolval;

                    if (vis.equals("both")) //$NON-NLS-1$
                    {
                        visible = 2;
                    } else if (vis.equals("hidden")) //$NON-NLS-1$
                    {
                        visible = -1;
                    }

                    if ((boolval = XMLutils.getElementBoolean("visible", element)) != null) //$NON-NLS-1$
                    {
                        if (boolval.booleanValue()) {
                            visible = 1;
                        }
                    }
                }
                // create a new DataElement and add it to the internal list
                data.add(new DataElement(tag, value, visible));
            }
        }

        // reset any saved element
        savedElement = null;

        // read the specification for a button
        if ((e = node.getChild("button")) != null) { //$NON-NLS-1$

            // save this element in case the button click needs to send the element
            // to a DataModel
            savedElement = node;

            // get some basic button attributes - icon, url, title, action
            String title = XMLutils.getElementString("title", e); //$NON-NLS-1$
            String icon = XMLutils.getElementString("icon", e); //$NON-NLS-1$
            final String url = XMLutils.getElementString("url", e); //$NON-NLS-1$
            final String action = XMLutils.getElementString("action", e); //$NON-NLS-1$

            if (url == null || (title == null && icon == null)) {
                LOG.error(name + Messages.getString(BUNDLE_NAME, "DataSetDataItem.22")); //$NON-NLS-1$
            } else {
                // create a new button and if an icon was specified set the button icon
                button = new JButton(title);
                if (icon != null) {
                    try {
                        URL u = new URL(icon);

                        ImageIcon im = new ImageIcon(u);
                        button.setIcon(im);

                    } catch (Exception ex) {
                        LOG.error(name, ex);
                    }
                }

                // add a listener which will take some action when the user clicks the button
                button.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        button.setEnabled(false);
                        buttonClicked(url, action);
                        button.setEnabled(true);
                    }
                });
            }
        }
        // layout the image, data elements and button
        layoutComponents();
    }

    /**
     * Respond to a user button click.  This could either submit the data elements to a web 
     * service identified by the URL or add the data elements to a data model identified by
     * the URL.
     * 
     * @param url  the URL to post the data elements to
     * @param action an associated action string
     */
    private void buttonClicked(String url, String action) {

        ArrayList<NameValuePair> params = getParameters();

        // if the URL is of the form model:<model_name>:<model_path>
        if (url != null && url.startsWith("model:")) { //$NON-NLS-1$
            // add this DataSet as an Element to the model
            String modelName = url.substring(6);

            if (modelName.startsWith("//")) //$NON-NLS-1$
            {
                modelName = url.substring(2);
            }

            // split the model name into model_name and model_path
            String els[] = modelName.split(":", 2); //$NON-NLS-1$

            if (els[0] != null && els[1] != null) {
                // get the model from the DataModel manager
                DataModelManager mgr = DataModelManager.getInstance();

                DataModel mod = mgr.getModel(els[0]);

                if (mod == null) {
                    LOG.error(Messages.getString(BUNDLE_NAME, "DataSetDataItem.26") + els[0]); //$NON-NLS-1$
                } else {
                    try {
                        // set the data element in the model
                        mod.setElement(els[1], savedElement, params);
                    } catch (Exception ex) {
                        errorMessage(Messages.getString(BUNDLE_NAME, "DataSetDataItem.27") + ex.getMessage()); //$NON-NLS-1$
                    }
                }
            }
        } else if (url != null) {
            // otherwise post the data elements to the web service
            try {

                // post the parameters to the URL
                Element node = getElementFromURL(url, params, action);

                if (node != null) {
                    if ("error".equals(node.getName())) //$NON-NLS-1$
                    {
                        errorMessage(node.getText());
                    } else if ("info".equals(node.getName())) //$NON-NLS-1$
                    {
                        infoMessage(node.getText());
                    }
                }
                // otherwise we don't do anything with the node

            } catch (Exception ex) {
                errorMessage(Messages.getString(BUNDLE_NAME, "DataSetDataItem.30") + url); //$NON-NLS-1$
                LOG.error(Messages.getString(BUNDLE_NAME, "DataSetDataItem.31") + url, ex); //$NON-NLS-1$
            }
        }
    }

    // methods that must be implemented
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public Component getComponent() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        if (value == null || value.length() == 0) {
            return null;
        }

        return new NameValuePair(name, value);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getParameters()
     */
    public ArrayList<NameValuePair> getParameters() {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        // iterate through the data elements in our internal list
        Iterator iter = data.iterator();
        while (iter.hasNext()) {
            DataElement de = (DataElement) iter.next();

            // add a new NameValuePair for each data element
            list.add(new NameValuePair(de.name, de.value));
        }
        // add a NameValuePair for the image URL
        if (imageURL != null) {
            list.add(new NameValuePair("image", imageURL)); //$NON-NLS-1$
        }
        return list;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getAllElements()
     */

    public ArrayList<NameValuePair> getAllElements() {
        return getParameters();
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getSelectedElements()
     */

    public ArrayList<NameValuePair> getSelectedElements() {
        return getParameters();
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#updateData(java.util.ArrayList, java.lang.String)
     */

    public void updateData(ArrayList<NameValuePair> params, String action) {
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#loadData(java.util.ArrayList, java.lang.String)
     */

    public void loadData(ArrayList<NameValuePair> params, String action) {
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#refresh(java.util.ArrayList, java.lang.String)
     */

    public void refresh(ArrayList<NameValuePair> params, String action) {
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#close()
     */

    public void close() {
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */

    public String getTypeName() {
        return DataItemFactory.DATASET;
    }

    /////////////////////////////////////////////////////
    //Drag and Drop methods
    /////////////////////////////////////////////////////
    /// Transferable methods
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTransferable()
     */
    public Transferable getTransferable() {
        return this;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTransferDataFlavors()
     */

    public DataFlavor[] getTransferDataFlavors() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataSetDataItem.33")); //$NON-NLS-1$
        }
        return flavors;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(DataFlavor df) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataSetDataItem.34")); //$NON-NLS-1$
        }
        return df.equals(flavors[0]) || df.equals(flavors[1]);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(DataFlavor flavor) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataSetDataItem.35")); //$NON-NLS-1$
        }
        return getParameters();
    }
    //////////////////

    /**
     * Create an array of Strings for a multi line tooltip.
     * 
     * @return the name and value of each data element
     */
    private String[] getToolTipArray() {
        ArrayList<String> list = new ArrayList<String>();

        Iterator iter = data.iterator();
        while (iter.hasNext()) {
            DataElement de = (DataElement) iter.next();

            if (de.visible >= 0) {
                list.add(new String(de.name + ": " + de.value)); //$NON-NLS-1$
            }
        }

        return list.toArray(new String[1]);
    }

    /**
     * Create a multi line tooltip.
     * 
     *@return The JToolTip returned is actually a JMultiLineToolTip
     */
    public JToolTip createToolTip() {
        JMultiLineToolTip ret = JMultiLineToolTip.getInstance();
        getToolTipText();
        ret.setToolTipArray(getToolTipArray());
        return ret;
    }

    // support class
    private class DataElement {

        /**
         * The class that holds the date element name and value and it's visibility
         */
        int visible = 0;
        String value = null;
        String name = null;

        DataElement(String name, String value, int visible) {
            this.name = name;
            this.value = value;
            this.visible = visible;
        }
    }
}
