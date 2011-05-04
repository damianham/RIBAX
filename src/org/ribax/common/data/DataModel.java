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

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;

import org.jdom.Element;
import org.ribax.common.net.NetUtils;

import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * An abstract base class for classes that manage a data set and
 * provide methods to get/set data elements.  Implementors can
 * subclass this class to provide a custom DataModel e.g.
 * 
 * <p>
 *
<code>
import org.ribax.swing.data.DataModel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.Attribute;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class MyDataModel extends DataModel {

    public MyDataModel() {}

    public void readDescription(Element node) {

        super.readDescription(node);

        // extract the custom configuration from the Element tree
    }

    public Object getElement(String path) {
        // the returned value should be a JDOM Element
    }
    public String getValue(String path) {
        // return the String value for path
    }

    public void setElement(String path, Object value,ArrayList<NameValuePair> params) {
        // set the value in the local DataModel and update the back end if required
        // value.toString() should return a text representation of the value

        // notify listeners the data has changed
        fireDataChanged();
    }
}

 </code>
 * 
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public abstract class DataModel {

    /** the name of this data model */
    public String name = "";
    /** the URL of a back end service that provides the data for the model */
    public String serviceEndpoint = null;
    /** a methodName in the case that we are using SOAP to access the back end */
    public String methodName = null;
    /** a list of parameters to supply to the back end web service when getting or
     * setting data */
    public ArrayList<NameValuePair> params = null;

    /** Get the element that corresponds to the named path.
     * This should be a JDOM Element.
     * @param path  The path within the data model to the Element.
     * @return the Object value for the given path or null if not found.
     */
    public abstract Object getElement(String path);

    /**
     * Get the children of the element that corresponds to the named path.
     * 
     * @param path The path within the data model to the Element.
     * @return the list of child elements of the named element.
     */
    public abstract List getChildren(String path);

    /** Get the text String value that corresponds to the named path.
     * <p>
     * equal to
     * <code>
     * (Element)getElement(path).getText();
     * </code>
     * @param path The path within the data model to the String value.
     * @return the String value for the given path or null if not found.
     * */
    public abstract String getValue(String path);

    /** Set the value of the element that corresponds to the named path.
     *
     * @param path The path within the data model to the Element.
     * @param value Object value for the given path.
     * @param params an ArrayList of utils.types.NameValuePairs that are sent as parameters.
     * in the network request to the Web Service if the Data Model is synchronized.
     * */
    public abstract void setElement(String path, Object value, ArrayList<NameValuePair> params) throws Exception;

    /**
     * Get the name of the DataModel.
     *
     * @return the name of the DataModel.
     */
    public synchronized String getName() {
        return name;
    }

    /**
     * Set the DataModel name.
     * @param name
     */
    public synchronized void setName(String name) {
        this.name = name;
    }

    /**
     * Get the service endpoint URL of a back end SOAP service that provides the
     * data for the model.
     *
     * @return the service endpoint URL or null if it has not been set.
     */
    public synchronized String getServiceEndpoint() {
        return serviceEndpoint;
    }

    /**
     * Set the service endpoint URL of a back end SOAP service that provides the
     * data for the model.
     *
     * @param url the URL of the SOAP service.
     */
    public synchronized void setServiceEndpoint(String url) {
        this.serviceEndpoint = url;
    }

    /**
     * Get the SOAP service method name that provides the
     * data for the model.
     *
     * @return the SOAP service method name or null if not defined.
     */
    public synchronized String getMethodName() {
        return methodName;
    }

    /**
     * Set the SOAP service method name that provides the
     * data for the model.
     *
     * @param methodName the SOAP service method name.
     */
    public synchronized void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Override to get the data from the back end if the data is not provided
     * in the data model definition.
     *
     */
    public synchronized void queryData() {
    }

    /**
     * A client informs the model handler that it has changed the data contained in the model at
     * the specified path and the model handler should take whatever action necessary to sync
     * with the back end.
     * 
     * @param path  the path within the model to the data that has been changed.
     */
    public void pathUpdated(String path) {
    }
    //////////////////////

    /**
     * Read the definition of this Data Model.  Sub classes should override this method
     * and call
     *
     * <code>
     * super.readDescription(Element node);
     * </code>
     * 
     * @param node  the Element that contains the Data Model definition.
     */
    public void readDescription(Element node) {

        name = XMLutils.getElementString("modelName", node);

        serviceEndpoint = XMLutils.getElementString("serviceEndpoint", node);

        methodName = XMLutils.getElementString("methodName", node);

    }

    /**
     * Subclasses should override this method to return the contents of the
     * DataModel as an InputStream.
     *
     * @return an InputStream that will supply the contents of the DataModel when read.
     */
    public InputStream getDataAsInputStream() {
        return null;
    }

    /**
     * Post a set of parameters to a Web Service and return an InputStream
     * to read the result.
     *
     * @param url the URL of the Web Service to connect to.
     * @param params an ArrayList of NameValuePair objects to send as parameters.
     * @return an InputStream to read the output of the Web Service.
     * @throws IOException
     */
    public InputStream getInputStream(String url, ArrayList<NameValuePair> params) throws IOException {
        return NetUtils.getInputStream(url, params, name);
    }
    /** The set of listeners that are notified of changes to the data */
    private Vector<DataChangeListener> listeners = new Vector<DataChangeListener>();

    /** Add an object for notification of data changed events.  Components that
     * bind to a Data Model should add a listener to the model to receive 
     * notification of data changes.
     *
     * <p>
     * <code>
     
     model.addDataChangeListener(new DataChangeListener() {
        public void dataChanged(DataModel model) {

        Object o = model.getElement(modelPath);

        if (o!= null && o instanceof Element)
            setData((Element)o);
        }
     });
     </code>
     *
     * @param l the component that wants to listen for data changed events.
     */
    public void addDataChangeListener(DataChangeListener l) {
        listeners.addElement(l);
    }

    /**
     * Remove an object for notification of data changed events.
     *
     * @param l the component to remove.
     */
    public void removeDataChangeListener(DataChangeListener l) {
        listeners.removeElement(l);
    }

   /**
    * Notify all listeners of a data changed event.
    *
    * @param path the path in the data model of the data that has changed.
    */
    public void fireDataChanged(String path) {
        for (int i = 0; i < listeners.size(); i++) {
            DataChangeListener l =
                    (DataChangeListener) listeners.elementAt(i);
            l.dataChanged(this, path);
        }
    }
}
