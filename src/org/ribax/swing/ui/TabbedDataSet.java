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
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JTabbedPane;

import org.jdom.Element;

import utils.types.NameValuePair;

/**
 * A class for organising a large set of DataItems in a set of Tabs in a JTabbedPane.  
 * This class handles button clicks and collects the data from all the Tabs in the set 
 * and submits them to a defined web service depending on the kind of button clicked.
 * 
 * Usually a Tab will handle button clicks and submit parameters from DataItems on the
 * Tab to a web service, but if there are a lot of DataItems that represent
 * a logical data group i.e. name, address, age, email and other personal details etc.
 * it might be better to break these into a number of separate Tabs.  However you want to
 * submit the collection of data elements that represent the logical grouping in a single 
 * submission to a web service rather than individually for each Tab.  This is where this
 * class can be useful.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 * 
 * @see org.ribax.swing.ui.TabContainer
 */
public class TabbedDataSet extends TabContainer {

    public static final long serialVersionUID = 1;
    /** The tabbed pane hosting the set of Tabs in this Tabbed Data Set */
    protected JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

    /**
     * Constructor that takes the name, title, discription and web service URL.
     * 
     * @param name the inernal name of this Tabbed Data Set.
     * @param title the displayed title of this Tabbed Data Set.
     * @param description a text description of this Tabbed Data Set.
     * @param url a URL of a web service to submit the data elements to in reposnse to a
     * button click.
     */
    protected TabbedDataSet(String name, String title, String description, String url) {
        super(name, title, description);

        this.url = url;

        layoutComponents();
    }

    /**
     * Constructor that takes a DataItem parent (usually a Folder)
     */
    protected TabbedDataSet(DataItem parent) {
        super(parent);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#readDescription(org.jdom.Element)
     */
    public void readDescription(Element tds) {
        Element e;

        super.readDescription(tds);

        // read the tab list
        if ((e = tds.getChild("tabList")) != null) { //$NON-NLS-1$
            List children = e.getChildren();
            Iterator iterator = children.iterator();
            while (iterator.hasNext()) {
                Element tabnode = (Element) iterator.next();

                Tab t = new Tab(this);
                t.readDescription(tabnode);

                // add this tab to the current container
                addDataItem(t);
            }
        }

        layoutComponents();
    }

    /**
     * Add a Tab to our internal list and the tabbed pane.
     * @param item the DataItem to add.
     */
    protected void addDataItem(Tab tab) {
        dataItems.add(tab);
        tabbedPane.add(tab.toString(), tab);
        tab.setActionHandler(this);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#updateItemData(java.util.ArrayList, java.lang.String)
     */
    protected void updateItemData(ArrayList<NameValuePair> params, String action) {
        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }

        // collect the parameters from all the data items in this TabbedDataSet
        ArrayList<NameValuePair> tlist = getParameters();

        if (tlist != null) {
            params.addAll(tlist);
        }

        doRequest(params, action);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#selectItemData(java.util.ArrayList, java.lang.String)
     */
    protected void selectItemData(ArrayList<NameValuePair> params, String action) {

        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }

        // collect the selected elements from all the data items in this TabbedDataSet
        ArrayList<NameValuePair> tlist = getSelectedElements();

        if (tlist != null) {
            params.addAll(tlist);
        }


        doRequest(params, action);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#submitItemData(java.util.ArrayList, java.lang.String)
     */
    protected void submitItemData(ArrayList<NameValuePair> params, String action) {

        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }

        // collect all elements from all the tabs
        ArrayList<NameValuePair> tlist = getAllElements();

        if (tlist != null) {
            params.addAll(tlist);
        }

        doRequest(params, action);
    }

    /**
     * Load the data for all the Tabs in this TabbedDataSet.
     * 
     * @param params  the parameters to Post to the URL
     * @param action  the action String 
     */
    protected void loadItemData(ArrayList<NameValuePair> params, String action) {

        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }

        // add the parameters from a parameter set
        if (paramSet != null) {
            params.addAll(paramSet.getNameValuePairs());
        }

        doRequest(params, action);

    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#loadData(java.util.ArrayList, java.lang.String)
     */
    public void loadData(ArrayList<NameValuePair> params, String action) {

        if (loaded) {
            return;
        }

        loadItemData(params, action);

        loaded = true;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#refresh(java.util.ArrayList, java.lang.String)
     */
    public void refresh(ArrayList<NameValuePair> params, String action) {

        doRequest(params, action);
    }

    /**
     * Submit the parameters to a web service and set the data for all the DataItems on all
     * the Tabs in this TabbedDataset from the result returned by the web service.
     * 
     * @param params a set of base parameters to submit to web services.
     * @param action the action string associated with this event.
     */
    protected void doRequest(ArrayList<NameValuePair> params, String action) {

        Element root;
        if ((root = getElementFromURL(url, params, action)) != null) {
            setData(root);
        }
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#getComponent()
     */

    public Component getComponent() {
        return tabbedPane;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#updateData(java.util.ArrayList, java.lang.String)
     */
    public void updateData(ArrayList<NameValuePair> params, String action) {
        doRequest(params, action);
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabContainer#getTypeName()
     */

    public String getTypeName() {
        return DataItemFactory.TABDATASET;
    }
}
