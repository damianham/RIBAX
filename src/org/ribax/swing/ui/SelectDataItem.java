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

import java.awt.Component;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;

import org.jdom.Element;
import utils.xml.XMLutils;

import utils.log.BasicLogger;
import utils.types.NameValuePair;

import org.ribax.common.Messages;
import org.ribax.common.data.DataChangeListener;
import org.ribax.common.data.DataModel;
import org.ribax.swing.datasources.OptionDataSource;

/**
 * A DataItem that provides a drop down menu
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class SelectDataItem extends DataItemAdaptor {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(SelectDataItem.class.getName());
    /** The drop down menu */
    private JComboBox menu;
    /** The main panel for the GUI components */
    private Box box = Box.createVerticalBox();
    /** A URL of a web service that provides the set of options in the drop down menu */
    private String sourceURL = null;

    /**
     * No argument Constructor - required
     */
    public SelectDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {
        Element e;

        super.readDescription(di);

        this.sourceURL = value;

        // if a data model was specified then add a listener to the model
        // to be informed of data updates and get the list of options from the Data Model
        if (model != null) {
            // create the combo box
            menu = new JComboBox();

            if (modelPath == null) {
                modelPath = getPath();
            }

            Object node = model.getElement(modelPath);

            if (node == null) {
                LOG.error(Messages.getString(BUNDLE_NAME, "SelectDataItem.0") + modelPath); //$NON-NLS-1$
            }

            // set the menu contents from the model node
            if (node != null && node instanceof Element) {
                setData((Element) node);
            }

            // add the data change listener
            model.addDataChangeListener(new DataChangeListener() {

                public void dataChanged(DataModel model, String path) {

                    // check that we are interested in this data
                    if (path == null || !modelPath.equals(path)) {
                        return;
                    }

                    Object node = model.getElement(modelPath);

                    if (node == null) {
                        LOG.error(Messages.getString(BUNDLE_NAME, "SelectDataItem.1") + modelPath); //$NON-NLS-1$
                    }
                    if (node != null && node instanceof Element) {
                        setData((Element) node);
                    }
                }
            });

        } else if ((e = di.getChild("options")) != null) { //$NON-NLS-1$

            // the menu contents are embedded in the XML document defining the Data Item

            // iterate through the child elements
            // adding each option to the menu
            Vector<String> v = new Vector<String>();
            List<Element> optlist = e.getChildren();
            Iterator<Element> optit = optlist.iterator();
            while (optit.hasNext()) {
                Element opt = (Element) optit.next();
                v.add(opt.getText());
            }
            String[] options = new String[v.size()];

            v.toArray(options);

            // create the combo box with the parsed options
            menu = new JComboBox(options);

        } else {
            // get the list of options from the sourceURL

            // create the combo box
            menu = new JComboBox();
            try {
                // set the combo box model to a new OptionDataSource (which reads the options
                // from the source URL)
                menu.setModel(new OptionDataSource(sourceURL, this.name));
            } catch (MalformedURLException ex) {
                errorMessage(Messages.getString(BUNDLE_NAME, "SelectDataItem.3") + sourceURL); //$NON-NLS-1$
                LOG.error(Messages.getString(BUNDLE_NAME, "SelectDataItem.4") + sourceURL, ex); //$NON-NLS-1$
            } catch (IOException ex) {
                errorMessage(Messages.getString(BUNDLE_NAME, "SelectDataItem.5") + sourceURL); //$NON-NLS-1$
                LOG.error(Messages.getString(BUNDLE_NAME, "SelectDataItem.6") + sourceURL, ex); //$NON-NLS-1$
            }
        }

        // indicates whether the combo box is editable which allows the user to add new values
        Boolean boolval = XMLutils.getElementBoolean("editable", di); //$NON-NLS-1$

        if (boolval != null) {
            menu.setEditable(boolval.booleanValue());
        }

        layoutComponents();
    }

    /**
     * Add the menu elements from an XML document element tree to the combo box model.
     * 
     * @param options the combo box model
     * @param node the Element tree containing the menu elements
     */
    private void addElements(DefaultComboBoxModel options, Element node) {

        // iterate through each child element
        List<Element> optlist = node.getChildren();
        Iterator<Element> optit = optlist.iterator();
        while (optit.hasNext()) {
            Element opt = optit.next();

            // if the element has a name attribute then add a NameValuePair object
            // this allows us to associate values with displayed names
            if (opt.getChild("name") != null) { //$NON-NLS-1$
                options.addElement(new NameValuePair(opt.getChild("name").getText(), //$NON-NLS-1$
                        opt.getChild("value").getText())); //$NON-NLS-1$
            } else // otherwise add the text as a String object
            {
                options.addElement(opt.getText());
            }
        }
    }

    /**
     * Layout the GUI components
     */
    private void layoutComponents() {

        // clear any previous GUI components
        removeAll();
        box.removeAll();

        if (font != null) {
            menu.setFont(font);
        }

        // add the components to a sub panel
        JPanel p1 = new JPanel();
        if (bgcolour != null) {
            p1.setBackground(bgcolour);
        }

        p1.add(new JLabel(title));
        p1.add(menu);

        box.add(p1);

        if (tooltip != null && tooltip.length() > 0) {
            menu.setToolTipText(tooltip);
        }

        add(box);
    }

    /**
     * Set the tooltip on the drop down menu.
     * 
     * @param tooltip The tooltip to set.
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
        menu.setToolTipText(tooltip);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */
    public void setData(Element node) {

        DefaultComboBoxModel options = new DefaultComboBoxModel();

        // just in case the model has list elements embedded in an <elements> tag
        // test for this case
        if (node.getChild("options") != null) //$NON-NLS-1$
        {
            addElements(options, node.getChild("options")); //$NON-NLS-1$
        } else {
            addElements(options, node);
        }

        menu.setModel(options);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public Component getComponent() {
        return menu;
    }

    /**
     * Add an element to the combo box.
     * 
     * @param item the element to add.
     */
    protected void addMenuItem(String item) {
        menu.addItem(item);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        if (menu.getSelectedIndex() < 0) {
            return null;
        }

        // return the field name and the selected element
        return new NameValuePair(fieldname, menu.getSelectedItem().toString());
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getParameters()
     */
    public ArrayList<NameValuePair> getParameters() {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(getNameValuePair());
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
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */
    public String getTypeName() {
        return DataItemFactory.SELECT;
    }
}
