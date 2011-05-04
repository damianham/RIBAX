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
package org.ribax.common.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.Attribute;

import org.ribax.common.Messages;
import org.ribax.common.net.SoapClient;

import utils.log.BasicLogger;
import utils.xml.XMLutils;
import utils.types.NameValuePair;
import utils.ui.StatusReporter;

/**
 * An implemenation of the DataModel interface that stores XML data in a JDOM Element 
 * tree and can synchronise the data with a back end web service.  The data for the model
 * can either be provided statically in the Data Model definition or it can be provided
 * by a back end web service.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 * 
 * @see org.ribax.common.data.DataModel
 */
public class JDomDataModel extends DataModel {

    private static final String BUNDLE_NAME = "org.ribax.common.data.messages"; //$NON-NLS-1$
    /* The root node for this Data Model*/
    private Element root = null;
    /* The URL of a web service that provides the data for this data model.  We also use
     * this URL to post changes in the data back to the web service (optional) */
    private String url = ""; //$NON-NLS-1$
    /* The URL of a web service that provides the streaming data (optional) */
    private String stream = null;
    /* A flag to tell this data model to stop streaming */
    private boolean stopped = false;
    
    private InputStream inStream = null;
    /* A flag indicating whether we should submit local changes in the data back to 
     * the web service (optional) defaults to false - do not submit changes 
     * in the data to the web service */
    private boolean syncData = false;
    /* A delay in seconds between polling the web service for the data (optional) */
    private int pollDelay = 0;
    private boolean notify = false;
    private static BasicLogger LOG = new BasicLogger(JDomDataModel.class.getName());

    /**
     * No argument constructor - required.
     */
    public JDomDataModel() {
    }

    /**
     * Invoke a new thread to download the data for the DataModel from a web service.
     */
    public synchronized void queryData() {
        // the data was not provided in the DataModel definition so start a process
        // to download or stream the data from a web service
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                // if the streaming URL is not null then we are streaming data from 
                // a web service
                if (stream != null) {
                    streamData(stream);
                } else if (serviceEndpoint != null && methodName != null) {
                    // get the data using a soap client
                    SoapClient sc = new SoapClient();

                    try {
                        root = sc.getSoapResponse(serviceEndpoint, methodName, methodName, params);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (url != null) {
                    // otherwise we are just requesting the data from a web service
                    // at least once

                    try {
                        // if the poll delay > 0 then we need to poll the web service
                        // every pollDelay seconds for the data
                        if (pollDelay > 0) {
                            while (true) {
                                // get the root document
                                root = DataUtils.getDocumentRoot(url, params, name);

                                // tell all the handlers that the data has changed
                                fireDataChanged(null);
                                try {
                                    Thread.sleep(pollDelay * 1000);
                                } catch (InterruptedException ex) {
                                }
                            }
                        } else {
                            // get the data once and be done with it
                            root = DataUtils.getDocumentRoot(url, null, name);

                            // tell all the handlers that the data has changed
                            fireDataChanged(null);
                        }
                    } catch (Exception ex) {
                        LOG.warning(name + " Error polling data " + ex.getMessage());
                    }
                } else {
                    LOG.warning(name + " No modelData, stream or url defined");
                }
            }
        });
    }
    HashSet<String> updatedPaths = new HashSet<String>();

    private void doDataUpdate() {

        // get the updated elements as a set of changes and         
        // send the changed elements to the back end

        if (serviceEndpoint != null && methodName != null) {
            // using SOAP
            Document doc = new Document();
            Element root = new Element("changes");
            doc.addContent(root);

            for (String s : updatedPaths) {
                Element e = (Element) getElement(s);

                params.add(new NameValuePair(s, DataUtils.getDataAsString(e)));
            }

            // get a soap client
            SoapClient sc = new SoapClient();

            try {
                root = sc.getSoapResponse(serviceEndpoint, methodName, methodName, params);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    @SuppressWarnings("unused")
    private boolean dirty = false;

    /**
     * Indicate that the DataModel has been updated at the given path.
     * If the data for the DataModel came from a web service then updates are
     * propagated back to the web service at regular intervals.
     *
     * @param path the path in the DataModel where the data was changed.
     */
    public void pathUpdated(String path) {
        dirty = true;
        updatedPaths.add(path);
        startTimer();
    }

    /*
     * The Timer and TimerTask objects used to schedule the recurring event
     */
    private Timer timer = null;
    private TimerTask timerTask = null;

    /**
     * Start a timer to update data.
     */
    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {

            public void run() {
                timerTick();
            }
        };
        timer.schedule(timerTask, 10000);
    }

    private synchronized void timerTick() {
        // perform any recurring tasks
        doDataUpdate();
    }

    /**
     * Get the contents of the DataModel as an InputStream.
     *
     * @return an InputStream that will supply the contents of the DataModel when read.
     */
    public InputStream getDataAsInputStream() {
        return DataUtils.getDataAsInputStream(root);
    }

    /* (non-Javadoc)
     * @see org.ribax.common.data.DataModel#readDescription(org.jdom.Element)
     */
    public void readDescription(Element node) {

        super.readDescription(node);

        // get the URL of the web service
        url = XMLutils.getElementString("url", node); //$NON-NLS-1$

        // get the URL of a streaming data web service
        stream = XMLutils.getElementString("stream", node); //$NON-NLS-1$

        // get the delay in seconds between polling the web service for the data
        Integer val = XMLutils.getElementInt("pollDelay", node); //$NON-NLS-1$
        if (val != null) {
            pollDelay = val.intValue();
        }

        // get the flag to indicate whether we synchronize local changes to the back end
        Boolean bval = XMLutils.getElementBoolean("synchronize", node); //$NON-NLS-1$

        if (bval != null) {
            syncData = bval.booleanValue();
        }

        bval = XMLutils.getElementBoolean("notify", node); //$NON-NLS-1$

        if (bval != null) {
            notify = bval.booleanValue();
        }

        // get the root node for the data element.  If it is not defined in the
        // description then get it from a web service
        if ((root = node.getChild("modelData")) == null) { //$NON-NLS-1$
            queryData();
        }
    }

    /**
     * Set the Element tree of this DataModel.
     *
     * @param root the JDOM Element tree.
     */
    public void setData(Element root) {
        this.root = root;
        fireDataChanged("/");
    }

    /**
     * Stop streaming the data if the the data model is currently streaming
     * data from a web service.
     */
    public void stopStreaming() {
        stopped = true;
    }

    /**
     * Read a continuous stream of data from a web service and replace or add data in 
     * the data model.
     * 
     * @param url the URL of the web service that will provide the data stream.
     */
    private void streamData(String url) {
        // the set of end tags we are looking for that deliniate a block of XML data
        String[] triggers = {
            "modelData", "error" //$NON-NLS-1$ //$NON-NLS-2$
        };

        try {

            // get an input stream from the web service
            if ((inStream = getInputStream(url, null)) == null) {
                return;
            }

            // get a buffered reader on the stream
            BufferedReader bin = new BufferedReader(new InputStreamReader(inStream));

            // get the root Element from the stream
            root = DataUtils.readElementBlock(url, bin, triggers);

            // tell all the listeners that the data has changed
            fireDataChanged(null);

            // now read the continuous stream of data adding/replacing data
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "JDomDataModel.10")); //$NON-NLS-1$
            }
            while (stopped == false) {

                // get the new Element from the stream
                Element newnode = DataUtils.readElementBlock(url, bin, triggers);

                // a null Element means there was no more data to read from the stream so 
                // pack up and go home
                if (newnode == null) {
                    break;
                }

                // if we are given the clearData element then clear the data
                // this has the effect of removing all data from the model
                Element e = newnode.getChild("clearData"); //$NON-NLS-1$

                if (e != null) {
                    root = newnode;
                } else // merge the new data
                {
                    mergeData(root, newnode);
                }

                // tell all the listeners that the data has changed
                fireDataChanged(null);

            }

        } catch (MalformedURLException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "JDomDataModel.12") + url + " " + ex); //$NON-NLS-1$ //$NON-NLS-2$
        } catch (IOException e) {
            LOG.warn(Messages.getString(BUNDLE_NAME, "JDomDataModel.14") + e); //$NON-NLS-1$
        }
    }

    /**
     * Merge new data in an XML Element into the existing Element tree.
     * 
     * @param orig  the existing Element tree.
     * @param newNode the new XML Element.
     */
    private void mergeData(Element orig, Element newNode) {

        // for each child of newNode merge it into the corresponding child of orig
        List<Element> children = newNode.getChildren();

        if (children.size() == 0) {
            // no children so set the value
            orig.setText(newNode.getText());

            // set the node attributes
            java.util.List<Attribute> l = newNode.getAttributes();

            for (Attribute t : l) {
                orig.setAttribute(t.getName(), t.getValue());
            }
            return;
        }

        // iterate through the children
        Iterator<Element> iterator = children.iterator();

        while (iterator.hasNext()) {
            Element node = iterator.next();

            // get the child matching this node
            Element origNode = orig.getChild(node.getName());

            if (origNode == null) {
                // there is no matching child so add a new child node to orig
                orig.addContent((Element) node.clone());
            } else {
                // otherwise recursively merge the new child with the old child
                mergeData(origNode, node);
            }
        }
    }

    /**
     * Set the value of a node within an element tree to a new value.
     * 
     * @param node the Element tree.
     * @param path the path to the node within the Element tree.
     * @param value the value to set.
     */
    private void mergeData(Element node, String path, Object value) {

        // resolve the path to the final node
        Object o = getNode(node, path);

        // set the content of the node if it is an Element node
        if (o instanceof Element) {
            node = (Element) o;

            // and the value is a String
            if (value instanceof String) {
                node.setText(value.toString());
            }
        }
    }

    /**
     * Find a node in an element tree corresponding to a given path.
     * 
     * @param node the element tree containing the node.
     * @param path the path within the Element tree.
     * @return the node corresponding to the path argument or null if no such node exists in
     * the Element tree.
     */
    private Object getNode(Element node, String path) {
        if (node == null) {
            return null;
        }

        if (path.indexOf('/') < 0) // the path has no more child elements so it now names a node at this
        // level in the tree so return the child for the final node name
        {
            return node.getChild(path);
        } else {
            // there are more elements in the path
            // seperate the path into the first element and all remaining elements
            String[] els = path.split("/", 2); //$NON-NLS-1$

            // get the child element corresponding to the first child name
            Element e = node.getChild(els[0]);

            if (e != null) // find the node recursively using the remainder of the path
            {
                return getNode(e, els[1]);
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.data.DataModel#getElement(java.lang.String)
     */
    public Object getElement(String path) {
        return getNode(root, path);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.data.DataModel#getChildren(java.lang.String)
     */
    public List getChildren(String path) {
        Object node = getNode(root, path);

        if (node instanceof Element) {
            Element e = (Element) node;

            return e.getChildren();
        }
        return new ArrayList();
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.data.DataModel#getValue(java.lang.String)
     */

    public String getValue(String path) {

        // find the Element for the path
        Object o = getElement(path);

        if (o != null) {
            // check for other data types but it should be an Element
            if (o instanceof String) {
                return (String) o;
            }

            if (o instanceof Element) {
                return ((Element) o).getText();
            }

            if (o instanceof Text) {
                return ((Text) o).getText();
            }

            if (o instanceof Attribute) {
                return ((Attribute) o).getValue();
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.data.DataModel#setElement(java.lang.String, java.lang.Object, java.util.ArrayList)
     */
    public void setElement(String path, Object value, ArrayList<NameValuePair> params) throws Exception {

        // update the incore element tree
        mergeData(root, path, value);

        // check to see if we need to update the back end
        if (syncData == false || url == null) {
            // tell all the listeners that the data has changed
            fireDataChanged(path);

            return;
        }

        // sync the data with the back end
        // create a new list of parameters and add the model name and path
        // so the back end web service knows which value has been updated
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new NameValuePair("modelName", name)); //$NON-NLS-1$
        list.add(new NameValuePair("modelPath", path)); //$NON-NLS-1$

        // if the value is a string object then add a NameValuePair with the value
        if (value instanceof String) {
            list.add(new NameValuePair("value", value.toString())); //$NON-NLS-1$
        } else if (value instanceof ArrayList) {
            // otherwise we assume it is an arraylist of NameValuePairs
            list.addAll((ArrayList) value);
        }
        // add the passed in parameters
        if (params != null) {
            list.addAll(params);
        }

        // get the document root from the web service
        Element node = DataUtils.getDocumentRoot(url, list, name);

        if (node == null) {
            // tell all the listeners that the data has changed
            fireDataChanged(path);

            return;
        }

        // replace the root node if this is a <modelData> element
        if ("modelData".equals(node.getName())) { //$NON-NLS-1$

            // if we are given the clearData element then clear the data
            Element e = node.getChild("clearData"); //$NON-NLS-1$

            if (e != null) {
                root = node;
            } else // merge the new data
            {
                mergeData(root, node);
            }
        } else if ("error".equals(node.getName())) { //$NON-NLS-1$
            throw new Exception(node.getText());
        }

        // tell all the listeners that the data has changed
        fireDataChanged(path);

        if (notify) {
            // tell the user the data has been updated sucessfully
            StatusReporter.reportStatus(path + " updated to " + value);
        }
    }
}
