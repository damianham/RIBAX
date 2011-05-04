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
import java.awt.BorderLayout;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.ribax.common.Messages;

import utils.log.BasicLogger;
import utils.types.NameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.swing.JPanel;

/**
 * A class that displays a sequence of Tabs with navigation backwards
 * and forwards in the sequence with 'BACK' and 'NEXT' type buttons.
 * A button click of a 'NEXT' type button is required to move to the
 * next Tab in the sequence.  After all Tabs have been displayed the
 * next click of a 'NEXT' type button causes the data to be collected
 * from all the Tabs in the sequence and submitted to a Web service.  
 * A 'FINISH' type button has the same effect anywhere in the
 * sequence.

 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class SequenceDataItem extends DataItemAdaptor {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(SequenceDataItem.class.getName());
    /** The set of Tabs in the sequence */
    protected Vector<Tab> tabs = new Vector<Tab>();
    /** The current ordinal position in the sequence */
    protected int position = 0;
    /** The panel containing the displayed Tab */
    private JPanel panel = new JPanel();
    /** The special Cancelled Tab - loaded when the user clicks a 'CANCEL' button */
    protected Tab cancelledTab = null;
    /** The special Finished Tab - loaded at the end of the sequence or when the user clicks
     * a 'FINISH' button */
    protected Tab finishedTab = null;

    /**
     * No argument Constructor - required
     */
    public SequenceDataItem() {
    }

    /**
     * Layout the GUI components
     */
    private void layoutComponents() {

        // clear out any previous GUI components
        removeAll();

        // set the layout to BorderLayout
        setLayout(new BorderLayout());

        // set the layout to BorderLayout on the panel also
        panel.setLayout(new BorderLayout());

        // set the background colour of the panel to match the background colour of this DataItem
        if (bgcolour != null) {
            panel.setBackground(bgcolour);
        }

        // set the tooltip on the panel
        if (tooltip != null && tooltip.length() > 1) {
            panel.setToolTipText(tooltip);
        }

        add(panel, BorderLayout.CENTER);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element node) {
        Element e;

        super.readDescription(node);

        // read the list of tabs
        if ((e = node.getChild("tabList")) != null) { //$NON-NLS-1$
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "SequenceDataItem.1")); //$NON-NLS-1$
            }
            // iterate through the list of tabs
            List<Element> children = e.getChildren();
            Iterator<Element> iterator = children.iterator();
            while (iterator.hasNext()) {
                Element tabnode = (Element) iterator.next();

                // create the new Tab and read the description
                Tab t = new Tab(this);
                t.readDescription(tabnode);

                // check if the Tab name is one of the special Tabs
                if ("cancelled".equals(t.getDataItemName())) //$NON-NLS-1$
                {
                    cancelledTab = t;
                } else if ("finished".equals(t.getDataItemName())) //$NON-NLS-1$
                {
                    finishedTab = t;
                } else // otherwise add the Tab to the Tab Vector
                {
                    tabs.add(t);
                }

                // tell the Tab that this DataItem is it's parent
                t.setParent(this);
                t.setActionHandler(this);
            }
        }

        layoutComponents();
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItemAdaptor#loadData(java.util.ArrayList, java.lang.String)
     */
    public void loadData(ArrayList<NameValuePair> params, String action) {
        loadCurrentTab();
    }

    /**
     * Load the indicated Tab into the display panel, removing any previous Tab.
     * 
     * @param tab the Tab to display.
     */
    protected void loadTab(Tab tab) {
        panel.removeAll();

        panel.add(tab, BorderLayout.CENTER);

        panel.validate();
        panel.repaint();

        // tell the Tab to load it's data if it hasn't already done so
        tab.loadData(null, "LoadData"); //$NON-NLS-1$
    }

    /**
     * Either we have reached the end of the sequence or the user has clicked a 'FINISH'
     * button.  Collect all the data elements from all the tabs in the sequence and submit 
     * them to the specified URL.  The result is an XML document that could be a <tab>
     * Element.  If this is the case then create a new Tab with this Element otherwise
     * load the 'Finished' special Tab or the last Tab in the sequence if there is no
     * special finished Tab. 
     */
    private void doFinish() {
        // collect the data and submit to the url if one is specified
        // the result is a Tab that is displayed in the display panel
        Element node = submitDataAndReadDescription();
        Tab tab;

        // check to see if the result is a Tab definition
        if (node != null && "tab".equals(node.getName())) { //$NON-NLS-1$

            // create a new Tab and read the description
            tab = new Tab(this);
            tab.readDescription(node);

            // load the Tab into the display panel
            loadTab(tab);
        } else if (finishedTab != null) // display the finished Tab
        {
            loadTab(finishedTab);
        } else {
            // advance to the end and display the last Tab in the sequence
            // we might already be doing that - so this will have no effect
            position = tabs.size() - 1;
            loadTab(tabs.elementAt(position));
        }
    }

    /**
     * Load the Tab at the current position.  If there are no more Tabs then display the 
     * finished special Tab.
     */
    protected void loadCurrentTab() {

        Tab tab;

        if (tabs.size() <= position) {
            // we have gone past the end, display the finished Tab
            doFinish();
        } else {
            // display the Tab at the current position
            tab = tabs.elementAt(position);

            loadTab(tab);
        }
    }

    /**
     * A user has clicked a button.  Depending on the type of button clicked take an 
     * approriate action.  A 'BACK' button causes the previous Tab to be displayed (if the
     * current position is > 0), a 'NEXT' button causes the next Tab to be displayed if 
     * there are more Tabs otherwise the effect is the same as a 'FINISH' button.  
     * See doFinish() for an explanation of what happens when a 'FINISH' button is clicked.
     * 
     * @param buttonType
     * @param action
     * 
     * @see org.ribax.swing.ui.SelectDataItem#doFinish()
     */
    public void doAction(int buttonType, String action) {

        if (buttonType == TabButton.BACK) {

            // display the previous Tab if the current Tab position > 0
            if (position > 0) {
                position--;
                loadCurrentTab();
            }
        } else if (buttonType == TabButton.CANCEL) {

            // load the cancelled tab if one was specified
            if (cancelledTab != null) {
                loadTab(cancelledTab);
            }

        } else if (buttonType == TabButton.FINISH) {

            doFinish();

        } else if (buttonType == TabButton.NEXT) {

            // display the next tab or do the finished procedure if this is the last Tab
            position++;
            loadCurrentTab();

        }
    }

    /**
     * Collect all the parameter data from all the Tabs in the sequence and submit them to
     * the web service.
     * 
     * @return an XML Element that may define a new Tab to display or null if no URL 
     * has been defined
     */
    protected Element submitDataAndReadDescription() {

        String url = this.value;

        // check we have a URL to submit to
        if (url == null) {
            return null;
        }

        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        // iterate through the tabs collecting the data
        Iterator<Tab> it = tabs.iterator();
        while (it.hasNext()) {
            Tab tab = it.next();

            ArrayList<NameValuePair> tlist;

            // get this Tabs data and add it to the list of parameters
            if ((tlist = tab.getAllElements()) != null) {
                list.addAll(tlist);
            }
        }

        try {
            // get an input stream to read from the web service
            InputStream fin = getInputStream(url, list);

            // create the XML document and get the root node
            SAXBuilder builder = new SAXBuilder();

            Document doc = builder.build(fin);

            Element root = doc.getRootElement();

            return root;

        } catch (MalformedURLException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "SequenceDataItem.6") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "SequenceDataItem.7") + url, ex); //$NON-NLS-1$
        } catch (JDOMException ex) {
            // indicates a well-formedness error
            errorMessage(Messages.getString(BUNDLE_NAME, "SequenceDataItem.8") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "SequenceDataItem.9") + url, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "SequenceDataItem.10") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "SequenceDataItem.11") + url, ex); //$NON-NLS-1$
        } catch (Exception ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "SequenceDataItem.12") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "SequenceDataItem.13") + url, ex); //$NON-NLS-1$
        }

        return null;
    }
}
