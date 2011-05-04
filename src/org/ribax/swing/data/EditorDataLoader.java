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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import utils.log.BasicLogger;
import utils.types.NameValuePair;

import org.ribax.common.Messages;
import org.ribax.common.net.NetUtils;
import org.ribax.swing.parameters.ParameterSet;
import org.ribax.swing.ui.DataItem;
import org.ribax.swing.ui.EditorDataItem;

/**
 * Loads the data for an editor pane from a web service in a background thread.  The data
 * can either be loaded once or can be streamed from the web service to the editor pane.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class EditorDataLoader extends Thread {

    /** Indicates whether the data is being streamed or loaded once */
    private boolean stream = false;
    /** A set of parameters to post to the web service */
    private ArrayList<NameValuePair> params;
    /** The URL of the web service providing the data */
    private String url;
    /** The editor pane to recieve the data */
    private JEditorPane editor;
    /** The DataItem that has created this data laoder */
    private DataItem parentDataItem;
    /** A flag to indicate the loader should stop streaming and terminate the thread */
    private boolean stopped = false;
    /** The name of the loader (for debugging) */
    private String name = null;
    private static final String BUNDLE_NAME = "org.ribax.swing.data.messages"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(EditorDataLoader.class.getName());

    /**
     * Construct a new data loader.
     * 
     * @param parent  the DataItem that created this data loader.
     * @param editor  The editor pane that will receive the data.
     * @param url the URL of the web service providing the data.
     * @param params a set of parameters to post to the web service.
     * @param stream indicates whether to stream the data or not.
     * @param name a name for this loader (for debugging).
     */
    public EditorDataLoader(DataItem parent, JEditorPane editor, String url,
            ArrayList<NameValuePair> params, boolean stream, String name) {

        this.parentDataItem = parent;
        this.url = url;
        this.params = params;
        this.stream = stream;
        this.editor = editor;
        this.name = name;

        // start the ball rolling (this calls the run() method )
        start();
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() {
        if (stream) // stream the data
        {
            streamEditorData();
        } else // load the data once
        {
            loadEditorData();
        }
    }

    /**
     * Tell this data loader to stop streaming and exit the thread.
     */
    public void stopStreaming() {
        stopped = true;
    }

    /**
     * Load the data once from the web service and terminate.
     */
    private void loadEditorData() {

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "EditorDataLoader.0"));	 //$NON-NLS-1$
        }
        try {

            // get the editor document
            HTMLDocument doc = (HTMLDocument) editor.getDocument();

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "EditorDataLoader.1")); //$NON-NLS-1$
            }
            //set the codebase so images relative to the codebase are properly loaded
            doc.setBase(new URL(url));
            doc.putProperty(Document.StreamDescriptionProperty, url);

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

            // open an inpustream from the web service/page
            InputStream in = NetUtils.getInputStream(url, params, name);
            editor.setText(""); //$NON-NLS-1$

            // set the contents of the editorpane with the contents of the input stream 
            editor.read(in, doc);

        } catch (MalformedURLException ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "EditorDataLoader.3") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "EditorDataLoader.4") + url, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "EditorDataLoader.7") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "EditorDataLoader.8") + url, ex); //$NON-NLS-1$
        } catch (Exception ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "EditorDataLoader.9") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "EditorDataLoader.10") + url, ex); //$NON-NLS-1$
        }

    }

    /**
     * Continously stream data from a web service to an editor pane.
     */
    private void streamEditorData() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("load page");	 //$NON-NLS-1$
        }
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
            String line;
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "EditorDataLoader.12")); //$NON-NLS-1$
            }
            // open an inpustream from the web service/page
            InputStream in = NetUtils.getInputStream(url, params, name);
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));

            // a string buffer for storing the input text
            StringBuffer buf = new StringBuffer();

            try {
                // while there are more lines available to read on the input stream
                while ((line = bin.readLine()) != null) {
                    if (stopped) {
                        break;
                    }

                    if (line.length() == 0) {
                        continue;
                    }

                    if (LOG.isDebugEnabled()) {
                        LOG.debug(Messages.getString(BUNDLE_NAME, "EditorDataLoader.13") + line); //$NON-NLS-1$
                    }
                    // check for the EOT character 0x04, if the line starts with EOT
                    // we replace the document contents otherwise we append to the document
                    if (line.charAt(0) == DataItem.EOT) {
                        buf = new StringBuffer();
                        line = line.substring(1);

                        // append this new text to the string buffer
                        buf.append(line);

                        // for EditorDataItems only update the text pane contents for each
                        // line that arrives from the input stream, for UrlDataItems we
                        // wait for the </html> end tag
                        if (parentDataItem instanceof EditorDataItem) {
                            editor.setText(line);
                        }
                    } else {
                        // append this new text to the string buffer
                        buf.append(line);

                        // for EditorDataItems only update the text pane contents for each
                        // line that arrives from the input stream, for UrlDataItems we
                        // wait for the </html> end tag
                        if (parentDataItem instanceof EditorDataItem) {
                            editor.setText(buf.toString());
                        }
                    }
                    // if the html end tag exists in the input line then reset the contents
                    // of the editor pane to the contents of the string buffer
                    // i.e. update the contents of a UrlDataItem
                    if (line.toLowerCase().indexOf("</html>") >= 0) { //$NON-NLS-1$
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(Messages.getString(BUNDLE_NAME, "EditorDataLoader.15") + buf.toString()); //$NON-NLS-1$
                        }
                        editor.setText(buf.toString());

                        // clear the string buffer
                        buf = new StringBuffer();
                    }
                }
            } catch (Exception e) {
                LOG.info(Messages.getString(BUNDLE_NAME, "EditorDataLoader.16"), e); //$NON-NLS-1$

            }
        } catch (MalformedURLException ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "EditorDataLoader.17") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "EditorDataLoader.18") + url, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "EditorDataLoader.21") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "EditorDataLoader.22") + url, ex); //$NON-NLS-1$
        } catch (Exception ex) {
            parentDataItem.errorMessage(Messages.getString(BUNDLE_NAME, "EditorDataLoader.23") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "EditorDataLoader.24") + url, ex); //$NON-NLS-1$
        }
    }
}
