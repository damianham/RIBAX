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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.jdom.Element;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;
import utils.model.SearchableListModel;

import org.ribax.common.Messages;
import org.ribax.common.data.DataChangeListener;
import org.ribax.common.data.DataModel;
import org.ribax.swing.parameters.ParameterSet;

/**
 * A class that implements a list with facilities to filter the list
 * elements.  If the DataItem description specifies that the list is searchable 
 * a text entry field is displayed where the user can enter characters.  The list elements
 * are filtered to those elements that begin with the data in the entry field.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 * 
 * @see {link utils.model.SearchableListModel }
 */
public class ListDataItem extends DataItem implements Comparator {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(ListDataItem.class.getName());
    /** The main panel containg the GUI components */
    private Box box = Box.createVerticalBox();
    /** The list that is the viewport of the list data model */
    protected JList table = new JList();
    /** A set of parameters for this Data Item */
    private ParameterSet paramSet;
    /** Indicates whether the list is searchable */
    private boolean searchable = false;
    /** A text entry field to enter a search term */
    private JTextField searchfield;
    /** The list data model that holds the list data elements */
    private DefaultListModel listModel;
    /** */
    private int visibleRows = -1;

    /**
     * No argument Constructor - required
     */
    public ListDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {
        Element e;

        super.readDescription(di);

        if (this.fieldname == null || this.fieldname.length() == 0) {
            this.fieldname = "ListElement"; //$NON-NLS-1$
        }
        // get the list selection mode one of (SINGLE,RANGE,MULTI)
        String mode = XMLutils.getElementString("selectionMode", di); //$NON-NLS-1$

        if (mode != null) {
            if ("SINGLE".equals(mode.toUpperCase()) == true) //$NON-NLS-1$
            {
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            } else if ("RANGE".equals(mode.toUpperCase()) == true) //$NON-NLS-1$
            {
                setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            } else if ("MULTI".equals(mode.toUpperCase()) == true) //$NON-NLS-1$
            {
                setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            }
        }

        // get the value that determiens whether the list is searchable
        Boolean boolval;
        if ((boolval = XMLutils.getElementBoolean("searchable", di)) != null) //$NON-NLS-1$
        {
            searchable = boolval.booleanValue();
        }

        // create a searchable or regular list data model
        if (searchable) {
            listModel = new SearchableListModel(this);
        } else {
            // create a DefaultListModel
            listModel = new DefaultListModel();
        }

        Integer intval;

        if ((intval = XMLutils.getElementInt("visibleRows", di)) != null) //$NON-NLS-1$
        {
            visibleRows = intval.intValue();
        }

        if (visibleRows > 0) {
            table.setVisibleRowCount(visibleRows);
        }

        // get the list layout style, one of (LIST,NEWSPAPER,GRID) default LIST
        String layout = XMLutils.getElementString("layout", di); //$NON-NLS-1$

        if (layout != null) {
            if ("NEWSPAPER".equals(layout.toUpperCase())) { //$NON-NLS-1$
                table.setLayoutOrientation(JList.VERTICAL_WRAP);
            } else if ("GRID".equals(layout.toUpperCase())) { //$NON-NLS-1$
                table.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            }
        }

        if ((e = di.getChild("elements")) != null) {		     //$NON-NLS-1$
            // the list data elements are embedded in the XML document
            addElements(e);
        } else if (value != null) {
            // read the list data elements from a web service
            readElements(value);
        }

        // set the model to the list data model
        setModel(listModel);

        if (prefWidth > 0 || prefHeight > 0) {
            Dimension d = new Dimension(prefWidth == 0 ? 400 : prefWidth,
                    prefHeight == 0 ? 400 : prefHeight);
            setPreferredSize(d);
        }

        // if a data model was specified then add a listener to the model
        // to be informed of data updates
        if (model != null) {
            if (modelPath == null) {
                modelPath = getPath();
            }

            Object node = model.getElement(modelPath);

            if (node == null) {
                LOG.error(Messages.getString(BUNDLE_NAME, "ListDataItem.11") + modelPath); //$NON-NLS-1$
            }
            // set the initial list contents from the data model
            if (node != null && node instanceof Element) {
                setData((Element) node);
            }

            // add a listener to be informed of any data updates
            model.addDataChangeListener(new DataChangeListener() {

                public void dataChanged(DataModel model, String path) {

                    // check that we are interested in this data
                    if (path == null || !modelPath.equals(path)) {
                        return;
                    }

                    Object node = model.getElement(modelPath);

                    if (node == null) {
                        LOG.error(Messages.getString(BUNDLE_NAME, "ListDataItem.12") + modelPath); //$NON-NLS-1$
                    }
                    if (node != null && node instanceof Element) {
                        setData((Element) node);
                    }
                }
            });
        }

        layoutComponents();
    }

    /**
     * Add list data elements from and XML Element tree.
     * 
     * @param node
     */
    private void addElements(Element node) {

        // iterate through the Element's child nodes
        List<Element> optlist = node.getChildren();
        Iterator<Element> optit = optlist.iterator();
        while (optit.hasNext()) {
            Element opt = (Element) optit.next();

            // add the list item
            listModel.addElement(opt.getText());
        }
    }

    /**
     * Read the list data contents from a web service.
     * 
     * @param url The URL to the web service that will provide the list contents.
     */
    private void readElements(String url) {

        ArrayList<NameValuePair> params = null;

        // add any data item parameters
        if (paramSet != null) {
            params = paramSet.getNameValuePairs();
        }

        // use a support method from the base class to get the Element root node
        // from the web service
        Element root = getElementFromURL(url, params, "LoadData"); //$NON-NLS-1$

        // add the list contents from the XML Element tree
        if (root != null) {
            addElements(root);
        }

    }

    /**
     * Set the selection mode of the JList.
     * 
     * @param mode the selection mode to set.
     */
    protected void setSelectionMode(int mode) {
        table.setSelectionMode(mode);
    }

    /**
     * Set the viewport data model of the JList.
     * 
     * @param model the list data model containing the list contents.
     */
    protected void setModel(DefaultListModel model) {
        table.setModel(model);
    }

    /**
     * Layout the GUI components.
     */
    private void layoutComponents() {

        // clear out any previous GUI components
        removeAll();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ListDataItem.14")); //$NON-NLS-1$
        }
        box.removeAll();

        // use Borderlayout
        setLayout(new BorderLayout());

        // if a parameter set has been specified than add it at the specified border location
        if (paramSet != null) {
            add(paramSet, paramSet.getLayoutLocation());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ListDataItem.15")); //$NON-NLS-1$
        }
        // set the tooltip of the JList
        if (tooltip != null && tooltip.length() > 0) {
            table.setToolTipText(tooltip);
        }

        if (font != null) {
            table.setFont(font);
        }

        // if the list is searchable then add the text entry field above the list 
        if (searchable) {
            JPanel p1 = new JPanel();
            searchfield = new JTextField(20);
            p1.add(new JLabel(Messages.getString(BUNDLE_NAME, "ListDataItem.16"))); //$NON-NLS-1$
            p1.add(searchfield);
            box.add(p1);

            // on each key press modify the viewed list contents
            searchfield.addKeyListener(new KeyAdapter() {

                public void keyReleased(KeyEvent e) {
                    setSearchTerm();
                }
            });
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ListDataItem.17")); //$NON-NLS-1$
        }
        JScrollPane scrpane;

        // add the list in a scrollpane
        scrpane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        /* with a regular vertical layout set the scrollpane inside a panel
         * so the preferred size will be honoured, the box would otherwise expand
         * to the full width of this component
         */
        if (table.getLayoutOrientation() == JList.VERTICAL) {
            JPanel p1 = new JPanel();
            p1.add(scrpane);

            box.add(p1);
        } else {
            box.add(scrpane);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ListDataItem.18")); //$NON-NLS-1$
        }
        // add the main box panel in the centre
        add(box, BorderLayout.CENTER);

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ListDataItem.19")); //$NON-NLS-1$
        }
        validate();
    }

    /**
     * Modify the search term so the visible elements change according to the key pressed.
     * 
     * @see {link utils.model.SearchableListModel }
     */
    private void setSearchTerm() {
        if (listModel instanceof SearchableListModel) {
            SearchableListModel m = (SearchableListModel) listModel;
            m.setSearchTerm(searchfield.getText());
        }
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getParameters()
     */

    public ArrayList<NameValuePair> getParameters() {

        return getSelectedElements();
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getSelectedElements()
     */
    public ArrayList<NameValuePair> getSelectedElements() {

        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ListDataItem.20")); //$NON-NLS-1$
        }
        Object[] array = table.getSelectedValues();

        // add a parameter to the parameters for each selected element of the list
        for (int i = 0; i < array.length; i++) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "ListDataItem.21") + array[i].toString()); //$NON-NLS-1$
            }
            // the parameter name is the fieldname + an index
            list.add(new NameValuePair(fieldname + "_" + i, array[i].toString())); //$NON-NLS-1$
        }

        // if this ListDataItem is searchable then add the contents of the search field
        // if the list is empty, this allows the user to add new values
        if (list.size() == 0 && searchable) {
            list.add(new NameValuePair(fieldname + "_0", searchfield.getText())); //$NON-NLS-1$
        }
        return list;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getAllElements()
     */
    public ArrayList<NameValuePair> getAllElements() {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ListDataItem.24")); //$NON-NLS-1$
        }
        ListModel data = table.getModel();

        // add a parameter to the parameters for every element in the list
        for (int i = 0; i < data.getSize(); i++) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "ListDataItem.25")); //$NON-NLS-1$
            }
            // the parameter name is the fieldname + an index unless the list element
            // is selected in which case it is "Selected_" + fieldname + an index      
            if (table.isSelectedIndex(i)) {
                list.add(new NameValuePair("Selected_" + fieldname + "_" + i, data.getElementAt(i).toString())); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                list.add(new NameValuePair(fieldname + "_" + i, data.getElementAt(i).toString()));		 //$NON-NLS-1$
            }
        }
        return list;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#refresh(java.util.ArrayList, java.lang.String)
     */
    public void refresh(ArrayList<NameValuePair> params, String action) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ListDataItem.29")); //$NON-NLS-1$
        }
        // no action to take for refresh
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#close()
     */

    public void close() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#loadData(java.util.ArrayList, java.lang.String)
     */
    public void loadData(ArrayList extraParams, String action) {

        if (loaded) {
            return;
        }

        refresh(extraParams, action);

        loaded = true;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public Component getComponent() {
        return table;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */

    public void setData(Element node) {
        listModel.clear();

        // just in case the model has list elements embedded in an <elements> tag
        // test for this case
        if (node.getChild("elements") != null) //$NON-NLS-1$
        {
            addElements(node.getChild("elements")); //$NON-NLS-1$
        } else {
            addElements(node);
        }
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#updateData(java.util.ArrayList, java.lang.String)
     */

    public void updateData(ArrayList<NameValuePair> params, String action) {
        refresh(params, action);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#isPrintable()
     */
    public boolean isPrintable() {
        return true;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */

    public String getTypeName() {
        return DataItemFactory.LIST;
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object term, Object obj) {
        if (obj instanceof String && term instanceof String) {
            String s1 = (String) obj, s2 = (String) term;
            // return 0 if object starts with the search term 1 otherwise
            return s1.startsWith(s2) ? 0 : 1;
        }
        return 1;
    }
}
