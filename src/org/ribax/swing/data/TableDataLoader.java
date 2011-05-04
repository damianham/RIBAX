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
package org.ribax.swing.data;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.jdom.Element;
import org.jdom.JDOMException;

import utils.log.BasicLogger;
import utils.types.NameValuePair;

import org.ribax.common.Messages;
import org.ribax.common.data.DataUtils;
import org.ribax.common.net.NetUtils;
import org.ribax.swing.parameters.ParameterSet;
import org.ribax.swing.ui.TableDataItem;

/**
 * Load the data for a table into a TableDataModel and set the model
 * of the table to the model.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class TableDataLoader extends Thread {

    private static final String BUNDLE_NAME = "org.ribax.swing.data.messages"; //$NON-NLS-1$
    /** indicates whether to stream data from a web service */
    private boolean stream = false;
    /** parameters to submit to the web service */
    private ArrayList<NameValuePair> params;
    /** the URL of a web service providing the table data */
    private String url;
    /** the TABLE DataItem that created this TableDataLoader */
    private TableDataItem parentDataItem;
    /** tells the TableDataLoader to stop streaming data from a web service */
    private boolean stopped = false;
    /** a TableDataModel to store the table data */
    private TableDataModel data;
    /** the name of this object or the parent (for debugging) */
    private String name = null;
    private static BasicLogger LOG = new BasicLogger(TableDataLoader.class.getName());

    /**
     * Constructor to create a new TableDataLoader.
     * 
     * @param parent the TABLE DataItem that created this TableDataLoader.
     * @param url the URL of a web service providing the table data.
     * @param params parameters to submit to the web service.
     * @param stream indicates whether to stream data from the web service
     * @param name the name of this object or the parent (for debugging)
     */
    public TableDataLoader(TableDataItem parent, String url, ArrayList<NameValuePair> params,
            boolean stream, String name) {

        this.parentDataItem = parent;
        this.url = url;
        this.params = params;
        this.stream = stream;
        this.name = name;

        // get the ball rolling
        start();
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() {
        if (stream) // we are in stream mode this returns when the streaming is stopped
        // by calling the close() method
        {
            streamTableData();
        } else {
            // otherwise load the data once
            try {
                parentDataItem.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                loadTableData();

            } finally {
                parentDataItem.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

        }
    }

    /**
     * The table data included print options, set them in the parent.
     * @param options the print options to set.
     */
    public void setPrintOptions(Hashtable<String, Object> options) {
        parentDataItem.setPrintOptions(options);
    }

    /**
     * The table data included parameters, set them in the parent.
     * @param params the parameters to set.
     */
    public void setParameters(ParameterSet params) {
        parentDataItem.addParameters(params);
    }

    /**
     * Signal that we should stop streaming.
     */
    public void close() {
        stopped = true;
    }

    /**
     * Open an input stream from a web service given in the URL property.
     * @return an open InputStream or null if an error occured.
     * @throws Exception in an error occurs.
     */
    private InputStream getInputStream() throws Exception {
        // add any global parameters
        if (ParameterSet.globalParameterSet != null) {
            ArrayList<NameValuePair> tlist = ParameterSet.globalParameterSet.getNameValuePairs();
            if (tlist != null) {
                if (params == null) {
                    params = new ArrayList<NameValuePair>();
                }
                params.addAll(tlist);
            }
        }

        return NetUtils.getInputStream(url, params, name);
    }

    /**
     * Get the table data once from a web service.
     */
    private void loadTableData() {

        // try to force garbage collection to free up some memory
        System.gc();

        // add any global parameters
        if (ParameterSet.globalParameterSet != null) {
            ArrayList<NameValuePair> tlist = ParameterSet.globalParameterSet.getNameValuePairs();
            if (tlist != null) {
                if (params == null) {
                    params = new ArrayList<NameValuePair>();
                }
                params.addAll(tlist);
            }
        }

        try {
            // get the root element
            Element root = DataUtils.getDocumentRoot(url, params, name);

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataLoader.2")); //$NON-NLS-1$
            }
            // create a new TableDataModel with the Element tree
            data = new TableDataModel(root, url);

            // set the data model in the parent
            parentDataItem.setModel(data);

            Element e;

            // check for print options and parameters
            if ((e = root.getChild("printOptions")) != null) //$NON-NLS-1$
            {
                parentDataItem.readPrintOptions(e);
            }

            if ((e = root.getChild("parameters")) != null) //$NON-NLS-1$
            {
                parentDataItem.addParameters(ParameterSet.readParameters(e));
            }

        } catch (MalformedURLException ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "TableDataLoader.5") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataLoader.6") + url, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "TableDataLoader.9") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataLoader.10") + url, ex); //$NON-NLS-1$
        } catch (Exception ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "TableDataLoader.11") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataLoader.12") + url, ex); //$NON-NLS-1$
        }
    }

    /**
     * Stream the table data continously from a web service.  Read a continuous
     * stream of data and replace or add rows in the data model.
     */
    private void streamTableData() {
        // the set of end tags we are looking for that deliniate a block of XML data
        String[] triggers = {
            "tableData", "error" //$NON-NLS-1$ //$NON-NLS-2$
        };

        try {

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataLoader.13")); //$NON-NLS-1$
            }
            // get an input stream from the web service
            InputStream fin = getInputStream();

            if (fin == null) {
                return;
            }

            // get a buffered reader on the stream
            BufferedReader bin = new BufferedReader(new InputStreamReader(fin));

            // read the initial data

            // get the root Element from the stream
            Element root = DataUtils.readElementBlock(url, bin, triggers);

            // create a new TableDataModel with the Element tree
            data = new TableDataModel(root, url);

            // set the data model in the parent
            parentDataItem.setModel(data);

            // read the stream of data adding/replacing rows
            while (stopped == false) {

                // get the new Element from the stream
                root = DataUtils.readElementBlock(url, bin, triggers);

                // a null Element means there was no more data to read from the stream so 
                // pack up and go home
                if (root == null) {
                    break;
                }

                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataLoader.24")); //$NON-NLS-1$
                }
                // if we are given the clear element then clear the table data.
                // Element text must be one of ALL,ROWDATA, defaults to ROWDATA
                Element e = root.getChild("clearData"); //$NON-NLS-1$

                if (e != null) // clear the indicated data
                {
                    data.clear(e.getText());
                }

                // add the new data to the model
                data.addRowData(root, true);
            }

        } catch (MalformedURLException ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "TableDataLoader.27") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataLoader.28") + url, ex); //$NON-NLS-1$
        } catch (JDOMException ex) {
            // indicates a well-formedness error
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "TableDataLoader.29") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataLoader.30") + url, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "TableDataLoader.31") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataLoader.32") + url, ex); //$NON-NLS-1$
        } catch (Exception ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "TableDataLoader.33") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataLoader.34") + url, ex); //$NON-NLS-1$
        }
    }
}
