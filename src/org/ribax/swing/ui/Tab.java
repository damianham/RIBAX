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

import java.awt.Cursor;
import java.awt.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;

import org.jdom.Element;
import org.ribax.common.Messages;

import utils.log.BasicLogger;
import utils.types.NameValuePair;

/**
 * A Tab is the principle container of DataItems.  The DataItems are layed out in a 
 * vertical stack with space roughly allocated equally.  Tabs also handle button clicks to
 * submit data from the DataItems on the Tab to a defined web service.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 * 
 * @see org.ribax.swing.ui.TabContainer
 */
public class Tab extends TabContainer {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(Tab.class.getName());
    /** The main panel for this Tab */
    private Box box = Box.createVerticalBox();

    /**
     * Constructor that takes a name, title and description
     */
    public Tab(String name, String title, String description) {

        super(name, title, description);

        layoutComponents();
    }

    /**
     * Constructor that takes a DataItem parent (usually a Folder)
     */
    public Tab(DataItem parent) {
        super(parent);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#readDescription(org.jdom.Element)
     */
    public void readDescription(Element tab) {
        Element e;

        super.readDescription(tab);

        box.removeAll();

        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.2")); //$NON-NLS-1$
        }
        // get the set of DataItems on this Tab
        if ((e = tab.getChild("dataItems")) != null) { //$NON-NLS-1$
            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.7")); //$NON-NLS-1$
            }
            // iterate through the DataItems
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

                // if the DataItem is hidden then only add it to our internal list
                if (component instanceof HiddenDataItem) {
                    dataItems.add(component);
                } else // otherwise add it to our internal list and the display panel
                {
                    addDataItem(component);
                }
            }
        }

        // layout the GUI components
        layoutComponents();
    }

    /**
     * Add a DataItem to our internal list and the display panel.
     * @param item the DataItem to add.
     */
    protected void addDataItem(DataItem item) {
        dataItems.add(item);

        box.add(item);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#refresh(java.util.ArrayList, java.lang.String)
     */
    public void refresh(ArrayList<NameValuePair> params, String action) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.17")); //$NON-NLS-1$
        }
        // invalidate all the DataItems on this Tab
        invalidateDataItems();

        if (source != null) {
            // replace the contents of the tab with the data from the source
            readDescriptionFromURL(url, params, action);

            loaded = false;

            // now tell each new data item to load its data
            loadData(params, action);

        } else {

            // tell each DataItem in our internal list to refresh
            for (int i = 0; i < dataItems.size(); i++) {
                DataItem item = (DataItem) dataItems.elementAt(i);

                if (item == null) {
                    LOG.error(name + Messages.getString(BUNDLE_NAME, "Tab.18") + getTitle()); //$NON-NLS-1$
                    continue;
                }
                item.refresh(params, action);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#loadItemData(java.util.ArrayList, java.lang.String)
     */
    protected void loadItemData(ArrayList<NameValuePair> params, String action) {

        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }

        // if there are parameters on this Tab then add them to the parameters for
        // submission to any web service
        if (paramSet != null) {
            ArrayList<NameValuePair> tlist = paramSet.getNameValuePairs();

            if (tlist != null) {
                params.addAll(tlist);
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.19")); //$NON-NLS-1$
        }
        // tell each data Item to load its data using the parameters and action string
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item == null) {
                LOG.error(name + Messages.getString(BUNDLE_NAME, "Tab.20") + getTitle()); //$NON-NLS-1$
                continue;
            }
            item.loadData(params, action);
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#updateItemData(java.util.ArrayList, java.lang.String)
     */
    protected void updateItemData(ArrayList<NameValuePair> params, String action) {

        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }

        // collect the parameters from all the data items on this tab
        ArrayList<NameValuePair> tlist = getParameters();

        if (tlist != null) {
            params.addAll(tlist);
        }

        // invalidate all the DataItems on this Tab
        invalidateDataItems();

        if (source != null) {
            // replace the contents of the tab with the data from the source
            readDescriptionFromURL(url, params, action);

            loaded = false;

            // now tell each new data item to load its data
            loadData(params, action);

        } else {

            // tell each data Item to update its data with the collected parameters
            for (int i = 0; i < dataItems.size(); i++) {
                DataItem item = (DataItem) dataItems.elementAt(i);
                item.updateData(params, action);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#selectItemData(java.util.ArrayList, java.lang.String)
     */
    protected void selectItemData(ArrayList<NameValuePair> params, String action) {

        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }

        // collect the selected elements from all the data items on this tab
        ArrayList<NameValuePair> tlist = getSelectedElements();

        if (tlist != null) {
            params.addAll(tlist);
        }

        // invalidate all the DataItems on this Tab
        invalidateDataItems();

        if (source != null) {
            // replace the contents of the tab with the data from the source
            readDescriptionFromURL(url, params, action);

            loaded = false;

            // now tell each new data item to load its data
            loadData(params, action);

        } else {

            // tell each data Item to load its data with the collected parameters
            for (int i = 0; i < dataItems.size(); i++) {
                DataItem item = (DataItem) dataItems.elementAt(i);
                item.loadData(params, action);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#submitItemData(java.util.ArrayList, java.lang.String)
     */
    protected void submitItemData(ArrayList<NameValuePair> params, String action) {

        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }

        // collect the selected elements from all the data items on this tab
        ArrayList<NameValuePair> tlist = getAllElements();

        if (tlist != null) {
            params.addAll(tlist);
        }

        // invalidate all the DataItems on this Tab
        invalidateDataItems();

        if (source != null) {
            // replace the contents of the tab with the data from the source
            readDescriptionFromURL(url, params, action);

            loaded = false;

            // now tell each new data item to load its data
            loadData(params, action);

        } else {

            // tell each data Item to load its data with the collected parameters
            for (int i = 0; i < dataItems.size(); i++) {
                DataItem item = (DataItem) dataItems.elementAt(i);
                item.loadData(params, action);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#loadData(java.util.ArrayList, java.lang.String)
     */
    public void loadData(ArrayList<NameValuePair> params, String action) {

        // if we already have our data then do nothing
        if (loaded) {
            return;
        }

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // if we have deferred reading the Tab description until the Tab was displayed
            // then read the description now
            if (deferredSource != null) {
                readDescriptionFromURL(deferredSource, deferredParams, deferredAction);
                deferredSource = null;
                deferredParams = null;
                deferredAction = null;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.23")); //$NON-NLS-1$
            }
            // load the data for all the DataItems on this Tab
            loadItemData(params, action);
        } finally {
            // whatever happens we must set the cursor back to the default
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        // indicate we have loaded our data 
        loaded = true;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#getComponent()
     */
    public Component getComponent() {
        return box;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#getTypeName()
     */
    public String getTypeName() {
        return DataItemFactory.TAB;
    }
}
