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
import java.awt.Cursor;
import java.awt.Component;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.Box;
import javax.swing.JComponent;
import org.jdom.Element;

import org.ribax.common.Messages;
import org.ribax.common.RIBAXConfig;
import org.ribax.swing.parameters.ParameterSet;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * An abstract class that provides a common base for the Tab and TabbedDataSet classes.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 * 
 * @see org.ribax.swing.ui.Tab
 * @see org.ribax.swing.ui.TabbedDataSet
 * @see org.ribax.swing.ui.TabButtonListener
 */
public abstract class TabContainer extends DataItem implements TabButtonListener {

    private static BasicLogger LOG = new BasicLogger(TabContainer.class.getName());
    /** The set of DataItems on this TabContainer */
    protected Vector<DataItem> dataItems = new Vector<DataItem>();
    /** A URL to a web service that returns the description of this TabContainer,
     * this is set if we have already read the description from the URL
     * */
    protected String url = null;
    /** A URL of a web service that provides the definition of this TabContainer */
    protected String source = null;
    /** Another DataItem that should handle button clicks */
    protected DataItem actionHandler = null;
    /** A global configuration set accessible by all parts of the application */
    protected RIBAXConfig config = RIBAXConfig.getInstance();
    /** A button panel that hosts buttons on this TabContainer */
    protected ButtonPanel buttonPanel = null;
    /** A set of parameters for this TabContainer */
    protected ParameterSet paramSet = null;

    /**
     * Constructor that takes a name, title and description
     */
    public TabContainer(String name, String title, String description) {

        super(name, title, description, null);

        layoutComponents();
    }

    /**
     * Constructor that takes a DataItem parent (usually a Folder)
     */
    public TabContainer(DataItem parent) {
        setParent(parent);
    }

    /**
     * If there is a description and/or any parameters anchored North then make a panel
     * that contains these.
     * 
     * @return a panel containing the description and/or parameters or null if there is no 
     * description and no parameters anchored North.
     */
    protected JComponent getNorthPanel() {

        Box panel = null;

        if (description != null) {
            // there is a description so add it to the panel
            Description desc;

            if (prefWidth > 0) {
                desc = new Description(description, getBackground(), prefWidth);
            } else {
                desc = new Description(description, getBackground());
            }

            // if there are no parameters then we don't need to create a panel, we
            // can simply return the description component as the North panel
            if (paramSet == null) {
                return desc;
            }

            // create the panel and add the description
            panel = Box.createVerticalBox();

            panel.add(desc);
        }

        // if there are no parameters then return the panel whatever the value is now
        // this will in effect return null if the test is true
        if (paramSet == null) {
            return panel;
        }

        // there may be parameters but if they are not anchored North then we won't add them
        // here so return the panel as it is
        if (!BorderLayout.NORTH.equals(paramSet.getLayoutLocation())) {
            return panel;
        }

        LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.0") + paramSet.getLayoutLocation()); //$NON-NLS-1$

        // if we didn't add a description then we can just return the parameter panel
        if (panel == null) {
            return paramSet;
        }

        // add the parameters to the panel
        panel.add(paramSet);

        return panel;
    }

    /**
     * If there is a set of button and/or parameters anchored South then make a panel
     * that contains these.
     * 
     * @return a panel containing the buttons and/or parameters or null if there is no 
     * buttons and no parameters anchored South.
     */
    protected JComponent getSouthPanel() {

        Box panel = null;

        if (paramSet != null && BorderLayout.SOUTH.equals(paramSet.getLayoutLocation())) {
            // there is a parameters set anchored South
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.1") + paramSet.getLayoutLocation()); //$NON-NLS-1$

            // if there are no buttons we can just return the parameter panel
            if (buttonPanel == null) {
                return paramSet;
            }

            // create the panel and add the parameters
            panel = Box.createVerticalBox();

            panel.add(paramSet);
        }

        // if there are no buttons then return whatever the value of panel is now
        // this will in effect return null if the test is true
        if (buttonPanel == null) {
            return panel;
        }

        // if the panel has not already been created (no parameters anchored South) then
        // just return the button panel
        if (panel == null) {
            return buttonPanel;
        }

        // otherwise add the button panel
        panel.add(buttonPanel);

        return panel;
    }

    /**
     * If there is a set of parameters anchored East then return the parameter panel.
     * 
     * @return a parameter panel that is anchored on the East border.
     */
    protected JComponent getEastPanel() {
        if (paramSet != null && BorderLayout.EAST.equals(paramSet.getLayoutLocation())) {
            return paramSet;
        }

        return null;
    }

    /**
     * If there is a set of parameters anchored West then return the parameter panel.
     * 
     * @return a parameter panel that is anchored on the West border.
     */
    protected JComponent getWestPanel() {
        if (paramSet != null && BorderLayout.WEST.equals(paramSet.getLayoutLocation())) {
            return paramSet;
        }

        return null;
    }

    /**
     * Layout the GUI components.  DataItems occupy the majority of space with button and
     * parameters panels anchored around the border.
     */
    protected void layoutComponents() {

        // remove any previous contents
        removeAll();

        // use BorderLayout
        setLayout(new BorderLayout());

        // if there is more than 1 DataItem then embed the box (which contains
        // the DataItems) in a scroll pane
        if (dataItems.size() > 1) {

            /*
            JScrollPane scrpane = new JScrollPane(getComponent(),
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
            if (prefWidth > 0 || prefHeight > 0) {
            Dimension d = new Dimension (prefWidth == 0 ? 400 : prefWidth,
            prefHeight == 0 ? 400 : prefHeight);
            scrpane.setPreferredSize(d);
            }
             */

            add(getComponent(), BorderLayout.CENTER);
        } else if (dataItems.size() == 1) {
            // there is only 1 DataItem so it occupies the whole of the center of the TabContainer
            DataItem item = (DataItem) dataItems.elementAt(0);
            add(item, BorderLayout.CENTER);
        }

        // add any panels that are anchored around the TabContainer border

        // get the North panel and add it to the North Border
        JComponent panel = getNorthPanel();

        if (panel != null) {
            add(panel, BorderLayout.NORTH);
        }

        // ditto south
        panel = getSouthPanel();
        if (panel != null) {
            add(panel, BorderLayout.SOUTH);
        }

        // ditto West
        panel = getWestPanel();
        if (panel != null) {
            add(panel, BorderLayout.WEST);
        }

        // ditto East
        panel = getEastPanel();
        if (panel != null) {
            add(panel, BorderLayout.EAST);
        }

        revalidate();
        repaint();

    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element root) {
        Element e;

        super.readDescription(root);

        // clear out any previous DataItems and GUI components
        dataItems = new Vector<DataItem>();
        buttonPanel = null;
        paramSet = null;

        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.2")); //$NON-NLS-1$
        }
        source = XMLutils.getElementString("source", root); //$NON-NLS-1$
        // if a source has been specified then read the description from the source
        // test the value of url, if it has been set then this could be the 2nd
        // time we are in this method, a source tag in a sourced description would
        // otherwise cause an infinite loop
        if (source != null && url == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.4")); //$NON-NLS-1$
            }
            url = source;

            ArrayList params = null;

            if (paramSet != null) {
                params = paramSet.getNameValuePairs();
            }

            // read the description later (when it is needed, i.e. when the TabContainer is displayed)
            deferReadDescriptionFromURL(source, params, "LoadDescription"); //$NON-NLS-1$
            return;
        }

        // get the parameters
        if ((e = root.getChild("parameters")) != null) //$NON-NLS-1$
        {
            paramSet = ParameterSet.readParameters(e);
        }

        // get a set of buttons
        if ((e = root.getChild("buttons")) != null) { //$NON-NLS-1$
            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.10")); //$NON-NLS-1$
            }
            buttonPanel = new ButtonPanel(this);
            buttonPanel.readButtons(e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.11")); //$NON-NLS-1$
        }
    }

    /**
     * Setup an action handler which will handle button clicks from buttons on this TabContainer.
     * 
     * @param parent the DataItem that will handle any button clicks.
     */
    protected void setActionHandler(DataItem parent) {
        this.actionHandler = parent;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#invalidateData()
     */
    public void invalidateData() {
        invalidateDataItems();
    }

    /**
     * Invalidate the data of all the DataItems on this TabContainer.
     */
    protected void invalidateDataItems() {

        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Tab.12")); //$NON-NLS-1$
        }
        // tell each DataItem in our internal list to invalidate its data
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item == null) {
                LOG.error(name + Messages.getString(BUNDLE_NAME, "Tab.13") + getTitle()); //$NON-NLS-1$
                continue;
            }
            item.invalidateData();
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#close()
     */
    public void close() {
        // tell each DataItem in our internal list to close
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item == null) {
                LOG.error(name + Messages.getString(BUNDLE_NAME, "Tab.14") + getTitle()); //$NON-NLS-1$
                continue;
            }
            item.close();
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#refresh(java.util.ArrayList, java.lang.String)
     */
    public abstract void refresh(ArrayList<NameValuePair> params, String action);

    /**
     * Tell each DataItem in our internal list to load its data.
     * 
     * @param params a set of parameters for DataItems to submit to web services.
     * @param action the action string associated with this event.
     */
    protected abstract void loadItemData(ArrayList<NameValuePair> params, String action);

    /**
     * Collect all the data elements from all the DataItems in this TabContainer and then 
     * either replace the TabContainer contents from a web service or tell all the DataItems
     * in the TabContainer to replace their data using the collected data elements as 
     * parameters for submission to web services.
     * 
     * @param params a set of base parameters to submit to web services.
     * @param action the action string associated with this event.
     */
    protected abstract void updateItemData(ArrayList<NameValuePair> params, String action);

    /**
     * Collect SELECTED data elements from all the DataItems in this TabContainer and then 
     * either replace the TabContainer contents from a web service or tell all the DataItems 
     * in the TabContainer to replace their data using the collected data elements as 
     * parameters for submission to web services.
     * 
     * @param params a set of base parameters to submit to web services.
     * @param action the action string associated with this event.
     */
    protected abstract void selectItemData(ArrayList<NameValuePair> params, String action);

    /**
     * Collect all the data elements from all the DataItems in this TabContainer and then 
     * either replace the TabContainer contents from a web service or tell all the DataItems
     * in the TabContainer to replace their data using the collected data elements as 
     * parameters for submission to web services.
     * 
     * @param params a set of base parameters to submit to web services.
     * @param action the action string associated with this event.
     */
    protected abstract void submitItemData(ArrayList<NameValuePair> params, String action);

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#validateContents()
     */
    public boolean validateContents() {
        // iterate through the DataItems on this tab and validate their data contents
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item.validateContents() == false) {
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabButtonListener#doAction(int, java.lang.String)
     * 
     * The user has clicked a button.  Take some action corresponding to the kind of button
     * that was clicked.
     */
    public void doAction(int buttonType, String action) {

        // validate the contents and do nothing if there is an error
        // unless we are going Back in which case we allow invalid content
        if (buttonType != TabButton.BACK
                && buttonType != TabButton.CANCEL
                && validateContents() == false) {
            return;
        }

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // there are 4 kinds of buttons
            // submit all data items (default)
            // submit parameters & changed data items Update, Back, Finish,
            // submit selected data items  Select, Delete, Help, Action
            // submit nothing Refresh,  Reload, Cancel

            // if the actionHandler is non null and it can handle the action 
            // then let the actionHandler handle the action
            if (actionHandler != null
                    && (actionHandler instanceof TabbedDataSet
                    || actionHandler instanceof SequenceDataItem)) {
                //LOG.info("sending button type "+buttonType+" action to parent action == "+action);

                // an action handler has been setup, pass the event to the action handler
                if (actionHandler instanceof TabbedDataSet) {
                    ((TabbedDataSet) actionHandler).doAction(buttonType, action);
                } else if (actionHandler instanceof SequenceDataItem) {
                    ((SequenceDataItem) actionHandler).doAction(buttonType, action);
                }

            } else {
                // no actionHandler setup so handle the event here

                //LOG.info("button type "+buttonType+" fired with action "+action);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

                // add the button type name
                params.add(new NameValuePair("ButtonType", TabButton.buttonTypeToName(buttonType))); //$NON-NLS-1$

                // add the button action
                if (action != null) {
                    params.add(new NameValuePair("Action", action)); //$NON-NLS-1$
                }
                // add any parameters from a parameter set
                if (paramSet != null) {
                    ArrayList<NameValuePair> tlist = paramSet.getNameValuePairs();

                    if (tlist != null) {
                        params.addAll(tlist);
                    }
                }

                // take an appropriate action based on the button type
                if (buttonType == TabButton.REFRESH
                        || buttonType == TabButton.RELOAD
                        || buttonType == TabButton.CANCEL) {
                    refresh(params, action);
                } else if (buttonType == TabButton.PRINT) {
                    printElements();
                } else if (buttonType == TabButton.UPDATE
                        || buttonType == TabButton.BACK
                        || buttonType == TabButton.FINISH) {
                    updateItemData(params, action);
                } else if (buttonType == TabButton.SELECT
                        || buttonType == TabButton.DELETE
                        || buttonType == TabButton.ACTION) {
                    selectItemData(params, action);
                } else {
                    submitItemData(params, action);
                }
            }
        } finally {
            // whatever happens we must set the cursor back to the default
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */
    public void setData(Element root) {
        Element tab;

        // see if the Element has a child Element that matches our internal name
        if ((tab = root.getChild(name)) != null) {

            // it does so iterate through our internal DataItem list and 
            // set their data using a child Element of our Element matching the DataItem name
            for (int i = 0; i < dataItems.size(); i++) {
                DataItem item = (DataItem) dataItems.elementAt(i);
                Element e = tab.getChild(item.name);
                if (e != null) {
                    item.setData(e);
                }
            }
        }
    }
    protected ArrayList<NameValuePair> deferredParams = null;
    protected String deferredAction = null;
    protected String deferredSource = null;

    /**
     * Defer reading the TabContainer description until the TabContainer is loaded.  
     * This allows us to lazy load the TabContainer description when it is first displayed, 
     * reducing network traffic during startup but causing a slight delay when the user 
     * clicks on a Folder for the first time.
     * 
     * @param source the URL to a web service that provides the TabContainer description.
     * @param params a set of parameters to submit to the web service.
     * @param action the action string associated with the event that caused the TabContainer
     * to be loaded from the web service.
     */
    protected void deferReadDescriptionFromURL(String source, ArrayList<NameValuePair> params, String action) {
        deferredSource = source;
        deferredParams = params;
        deferredAction = action;
    }

    /* (non-Javadoc)
     * @see dbaccessor.DataItem#loadData(java.util.ArrayList)
     * 
     */
    public abstract void loadData(ArrayList<NameValuePair> params, String action);
    private final int PARAM = 0;
    private final int ALL = 1;
    private final int SELECTED = 2;

    /**
     * Get the data elements for all the DataItems on this TabContainer.  The data elements 
     * that are returned depend on the kind of elements requested.
     * 
     * @param mode indicates the kind of elements requested.
     * @return a list of data elements or null if the list is empty.
     */
    protected ArrayList<NameValuePair> getElements(int mode) {
        ArrayList<NameValuePair> tlist, list = new ArrayList<NameValuePair>();

        // iterate through the DataItems in our internal list
        Iterator<DataItem> it = dataItems.iterator();
        while (it.hasNext()) {
            DataItem item = it.next();

            // depending on the mode get the required data elements from the 
            // DataItem
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
     * @see org.ribax.swing.ui.DataItem#getSelectedElements()
     */

    public ArrayList<NameValuePair> getSelectedElements() {
        return getElements(SELECTED);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public abstract Component getComponent();

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#updateData(java.util.ArrayList, java.lang.String)
     */
    public void updateData(ArrayList<NameValuePair> params, String action) {
        updateItemData(params, action);
    }

    /**
     * The user has clicked a print button.  Print all of the DataItems on this TabContainer.
     * This prints each DataItem as a separate print job and needs fixing to print all the
     * DataItems in a single print job so they appear on the same page(s) as the other
     * DataItems on the TabContainer.
     */
    public void printElements() {

        // iterate through the DataItems in our internal list and print them
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            // we don't print hidden DataItems
            if (item instanceof HiddenDataItem) {
                continue;
            }

            printPrintable(item.getPrintable());
        }

        /*
         * 
         * This is the code that we should use to print the DataItems on this list
         * 
        if (dataItems.size() == 1) {
        DataItem item = (DataItem) dataItems.elementAt(0);
        printPrintable(item.getPrintable());
        return;
        }
        
        ComponentListPrinter list = new ComponentListPrinter();
        
        // copy the printable components 
        for(int i = 0; i < dataItems.size(); i++) {
        DataItem item = (DataItem) dataItems.elementAt(i);
        Object o = item.getPrintableComponent();

        if (o != null) {
        if (o instanceof ArrayList)
        list.addComponents((ArrayList)o);
        else
        list.addComponent((Component)o);
        }
        }
        printPrintable(list);
         */
    }

    /**
     * Create a print job with the printable component and send it to the printer.
     * 
     * @param printable the component Printable to print.
     * 
     * @see {link java.awt.print.PrinterJob}
     */
    protected void printPrintable(Printable printable) {

        if (printable == null) {
            return;
        }

        // wrap in a try/finally so state can be restored even if something fails
        try {

            // fetch a PrinterJob
            PrinterJob job = PrinterJob.getPrinterJob();

            // set the Printable on the PrinterJob
            job.setPrintable(printable);

            // create an attribute set to store attributes from the print dialog
            PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();

            // set any attributes here


            // display a print dialog and record whether or not the user cancels it
            boolean printAccepted = job.printDialog(attr);

            // if the user didn't cancel the dialog do the print job
            if (printAccepted) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                // do the printing (may need to handle PrinterException)
                job.print(attr);
            }
        } catch (PrinterException ex) {
            // something bad happened so let the user know
            errorMessage(Messages.getString(BUNDLE_NAME, "Tab.24")); //$NON-NLS-1$

        } finally {
            // restore the original state here (for example, restore selection)
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */

    public abstract String getTypeName();
}
