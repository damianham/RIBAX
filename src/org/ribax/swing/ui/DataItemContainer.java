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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.ribax.common.Messages;


import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * A class that implements a common set of methods for DataItems that are containers
 * for other DataItems.
 * 
 * @author damian
 *
 */
public abstract class DataItemContainer extends DataItem {

    private static BasicLogger LOG = new BasicLogger(DataItemContainer.class.getName());
    /** The set of DataItems contained in this container */
    protected Vector<DataItem> dataItems = new Vector<DataItem>();
    /** A URL to a web service that returns the description of this DataItemContainer,
     * this is set if we have already read the description from the URL
     * */
    protected String url = null;
    /** A URL to a web service that returns the description of this DataItemContainer */
    protected String source = null;

    /**
     * No argument Constructor (required by DataItemFactory)
     * 
     * @see org.ribax.swing.ui.DataItemFactory
     */
    public DataItemContainer() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element cdi) {
        Element e;

        // read the common DataItem attributes and elements
        super.readDescription(cdi);

        // remove all the data items before reading the description 
        dataItems = new Vector<DataItem>();
        removeAll();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ColumnDataItem.0")); //$NON-NLS-1$
        }
        // if a source has been specified then read the description from the source
        source = XMLutils.getElementString("source", cdi); //$NON-NLS-1$

        // test the value of url, if it has been set then this could be the 2nd
        // time we are in this method, a source tag in a sourced description would
        // otherwise cause an infinite loop
        if (source != null && url == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "ColumnDataItem.3")); //$NON-NLS-1$
            }
            url = source;

            readDescriptionFromURL(source, null, "LoadDescription"); //$NON-NLS-1$
            return;
        }

        // read a set of DataItems
        if ((e = cdi.getChild("dataItems")) != null) { //$NON-NLS-1$
            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "GridDataItem.7")); //$NON-NLS-1$
            }
            // iterate through the list of data items
            List<Element> dilist = e.getChildren();
            Iterator<Element> it = dilist.iterator();
            while (it.hasNext()) {
                Element di = it.next();

                // use DataItemFactory.readComponent to create a new DataItem
                // from the Element tree
                DataItem component = DataItemFactory.readComponent(di, this);

                if (component == null) {
                    continue;
                }

                // add it to our internal list 
                dataItems.add(component);

            }
        }

    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#invalidateData()
     */
    public void invalidateData() {
        invalidateDataItems();
    }

    /**
     * Tell each DataItem in our internal list to invalidate it's data so he next time it
     * is displayed it wll reload the data from the data source.
     */
    private void invalidateDataItems() {

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ColumnDataItem.10")); //$NON-NLS-1$
        }
        // invalidate each DataItem in our internal list
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item == null) {
                LOG.error(Messages.getString(BUNDLE_NAME, "ColumnDataItem.11") + getTitle()); //$NON-NLS-1$
                continue;
            }
            item.invalidateData();
        }
    }


    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#close()
     */
    public void close() {

        // iterate through our list of DataItems calling their close() method
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item == null) {
                LOG.error(Messages.getString(BUNDLE_NAME, "ColumnDataItem.12") + getTitle()); //$NON-NLS-1$
                continue;
            }
            item.close();
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */
    public void setData(Element root) {
        Element e;

        // Set the data for all the DataItems in our internal list.
        if ((e = root.getChild(name)) != null) {
            for (int i = 0; i < dataItems.size(); i++) {
                DataItem item = (DataItem) dataItems.elementAt(i);
                item.setData(e);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        return null;
    }
    // values that determine the type of NameValuePair elements to collect
    private final int PARAM = 0;
    private final int ALL = 1;
    private final int SELECTED = 2;

    /**
     * Collect a set of NameValuePair parameters from the DataItems in our internal 
     * list according to the type of parameters that are required.
     * 
     * @param mode the type of parameters required 
     * @return the list of parameters from all the DataItems or null if there are no 
     * parameters matching the criteria
     * 
     * @see utils.types.NameValuePair
     */
    private ArrayList<NameValuePair> getElements(int mode) {
        ArrayList<NameValuePair> tlist, list = new ArrayList<NameValuePair>();

        // iterate through the DataItems in our internal list
        Iterator<DataItem> it = dataItems.iterator();
        while (it.hasNext()) {
            DataItem item = it.next();

            // call the appropriate method in the DataItem
            if (mode == PARAM && (tlist = item.getParameters()) != null) {
                list.addAll(tlist);
            } else if (mode == ALL && (tlist = item.getAllElements()) != null) {
                list.addAll(tlist);
            } else if (mode == SELECTED && (tlist = item.getSelectedElements()) != null) {
                list.addAll(tlist);
            }
        }

        if (list.size() == 0) {
            return null;
        }

        return list;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getSelectedElements()
     */
    public ArrayList<NameValuePair> getSelectedElements() {
        return getElements(SELECTED);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getParameters()
     */
    public ArrayList<NameValuePair> getParameters() {
        return getElements(PARAM);
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getAllElements()
     */

    public ArrayList<NameValuePair> getAllElements() {
        return getElements(ALL);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#refresh(java.util.ArrayList, java.lang.String)
     */
    public void refresh(ArrayList<NameValuePair> params, String action) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("refresh"); //$NON-NLS-1$
        }
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        // add the input params 
        if (params != null) {
            list.addAll(params);
        }

        // add the button type name
        list.add(new NameValuePair("ButtonType", TabButton.buttonTypeToName(TabButton.REFRESH))); //$NON-NLS-1$

        if (source != null) {
            // replace the contents of this DataItem with the description from the source
            readDescriptionFromURL(url, list, action);

            loaded = false;

            // now tell each new data item to load its data
            loadData(list, action);

        } else {
            // invalidate all the data items 
            invalidateDataItems();

            // tell each data Item to load its data
            for (int i = 0; i < dataItems.size(); i++) {
                DataItem item = (DataItem) dataItems.elementAt(i);

                if (item == null) {
                    LOG.error(Messages.getString(BUNDLE_NAME, "ColumnDataItem.15") + getTitle()); //$NON-NLS-1$
                    continue;
                }
                item.refresh(list, action);
            }
        }
    }


    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#loadData(java.util.ArrayList, java.lang.String)
     */
    public void loadData(ArrayList<NameValuePair> params, String action) {

        // for each DataItem in our internal list tell them to load their data
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item == null) {
                continue;
            }
            item.loadData(params, action);
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#updateData(java.util.ArrayList, java.lang.String)
     */
    public void updateData(ArrayList<NameValuePair> params, String action) {
        // for each DataItem in our internal list tell them to update their data
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item == null) {
                continue;
            }
            item.updateData(params, action);
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#validateContents()
     */
    public boolean validateContents() {
        // validate the contents from all the data items in our internal list
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            // if any DataItem doe not validate then return false immediately
            if (item.validateContents() == false) {
                return false;
            }
        }
        return true;
    }
}
