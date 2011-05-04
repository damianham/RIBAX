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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.Component;
import java.awt.print.Printable;
import java.awt.Font;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

import org.ribax.common.Messages;
import org.ribax.common.data.DataModel;
import org.ribax.common.data.DataModelManager;
import org.ribax.common.net.NetUtils;
//import org.ribax.common.validators.RegexpValidator;
import org.ribax.common.validators.Validator;
import org.ribax.common.validators.ValidatorFactory;

/**
 * The abstract base class for all RIBAX <code>Folders</code>, <code>Tabs</code> and 
 * <code>DataItem</code> subclasses.  Classes which subclass <code>DataItem</code> 
 * can be placed on a <code>Tab</code>.  The <code>DataItemAdaptor</code> is a concrete
 * subclass of <code>DataItem</code> which provides null implementations of the abstract
 * methods of <code>DataItem</code> and can be used by subclasses that do not want to 
 * implement all of the <code>DataItem</code> abstract methods.
 * <p>
 * This Example illustrates how to create a subclass of <code>DataItem</code>.
 * 
 * <p>
<pre>

import org.ribax.swing.ui.DataItem;
import org.ribax.swing.data.DataModel;

import utils.types.NameValuePair;
import utils.log.LOG;

import org.jdom.Element;

import java.awt.Component;
import java.util.ArrayList;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JLabel;

public class MyDataItem extends DataItem {

JLabel component = new JLabel("this is my DataItem");

public MyDataItem() {}

// the description has been loaded into an org.jdom.Element tree
// please read the JDOM documentation for information about how to 
// retrieve values from elements in the tree
public void readDescription(Element di) {

// read the standard DataItem attributes
super.readDescription(di);

// read any extra data from the Element that is specific to this class
// using the JDOM API
setData(di);

// bind to a Data Model
if (model != null) {
if (modelPath == null)
modelPath = getPath();

Object node = model.getElement(modelPath);

if (node == null) {
LOG.error("no node for "+modelPath);
}
if (node != null && node instanceof Element)
setData((Element)node);

model.addDataChangeListener(new DataChangeListener() {
public void dataChanged(DataModel model) {

Object node = model.getElement(modelPath);

if (node != null && node instanceof Element)
setData((Element)node);
}
});
}

}

private void layoutComponents() {

removeAll();

// add the GUI elements
add(component);
}

protected void setData(Element node) {
Element e;

if ((e = node.getChild(fieldname)) != null) {
// set the internal data with the value of this node i.e.
component.setText(e.getText());
}

// layout the GUI components
layoutComponents();
}

// methods that must be implemented
protected Component getComponent() {
// return a Component that contains data or null
// used when laying out in a ColumnDataItem
return component;
}

public NameValuePair getNameValuePair() {
// classes which represent a single value can return a NameValuePair
return new NameValuePair(fieldname,value);
// otherwise return null;
}

protected ArrayList getParameters() {
ArrayList list = new ArrayList();

// add NameValuePair objects to the list for each element of data

return list;
}
protected ArrayList getAllElements() {
return getParameters();
}
protected ArrayList getSelectedElements() {
// classes that represent data that can have selected elements can
// return a list of elements that are selected, otherwise
return null;
}
protected void updateData(ArrayList params,String action) { 
refresh(params,action);
}
protected void loadData(ArrayList params,String action) {
if (loaded == false)
refresh(params,action);

loaded = true;
}

protected void refresh(ArrayList extraParams,String action) {
loadItemData(extraParams,action);   	
}

public void close() {
// called when the application closes
}

protected void loadItemData(ArrayList params,String action) { 

// post the parameters in the ArrayList to the url in the value attribute

try {

// post the parameters to the URL
Element node = getElementFromURL(value,params,action);

if (node != null) {
setData(node)
}

} catch (Exception e) {
LOG.error(name+" error loading data ",e);
errorMessage("error loading data " + e.getMessage());
}

}
protected String getTypeName() {
return "MyData";
}
}
</pre>
 * 
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>

 * @see {@link DataItemAdaptor}
 */
public abstract class DataItem extends JPanel implements Transferable {

    protected static final String BUNDLE_NAME = "org.ribax.swing.ui.messages"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(DataItem.class.getName());
    /** the parent DataItem that contains this DataItem */
    protected DataItem parentDataItem = null;
    /** the name of the data item  */
    protected String name = ""; //$NON-NLS-1$
    /** the displayable title  */
    protected String title = ""; //$NON-NLS-1$
    /** a short text description of what the data item is for  */
    protected String description = null;
    /**
     * The fieldname that is used when submitting data to Web Services.<br>
     * Data from this data item will be HTTP encoded using the fieldname as the parameter
     * identifer and the data as the parameter value 
     */
    protected String fieldname = ""; //$NON-NLS-1$
    /** used by sub classes to define elements of data, URLs etc.  Each sub class
     * interprets the value it's own way
     */
    protected String value = null;
    /** tooltip text for the DataItem panel */
    protected String tooltip = null;
    /** a background colour for the DataItem panel */
    protected Color bgcolour = null;
    /** indicates whether this DataItem has loaded it's data */
    protected boolean loaded = false;
    /** a URL to a source for streaming data */
    protected String streamSource = null;
    /** a font to use for text rendered within this DataItem */
    protected Font font = null;
    /** the preferred width of the DataItem */
    protected int prefWidth = 0;
    /** the preferred height of the DataItem */
    protected int prefHeight = 0;
    /** a byte with the value of 0x04 is used when streaming data in text 
     * format to signal the start of new text */
    public static final char EOT = 0x04;
    /** a set of regular expression data validators */
    protected Vector<Validator> validators = null;
    /** a data model that provides data for this data item */
    protected DataModel model = null;
    /** the path to the data in the data model */
    protected String modelPath = null;

    /**
     * Create a new DataItem and initialise with a name, title, description and fieldname
     * 
     * @param name  the internal name
     * @param title the displayable title
     * @param description a description of what the DataItem is for
     * @param fieldname the fieldname that is used as the parameter name when submitting
     * data to CGI Program data sources
     */
    public DataItem(String name, String title, String description, String fieldname) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.fieldname = fieldname;

        if (bgcolour != null) {
            setBackground(bgcolour);
        }

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    /**
     * default constructor with no arguments 
     * (the one which is actually used by sub classes created 
     * in DataItemFactory.readComponent())
     */
    public DataItem() {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    /**
     * Set the parent of this DataItem.  The parent reference is used to determine the path
     * to this DataItem.
     * 
     * @param parent
     */
    public void setParent(DataItem parent) {
        this.parentDataItem = parent;
    }

    /* (non-Javadoc)
     * @see java.awt.Component#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        // start with the default preferred size
        Dimension d = super.getPreferredSize();

        // modify it with any values specified in the description
        if (prefWidth > 0) {
            d.width = prefWidth;
        }

        if (prefHeight > 0) {
            d.height = prefHeight;
        }

        return d;
    }

    /**
     * Get a String value for a named element.
     * 
     * @param name the name of the element 
     * @param di the parent element containing the named element
     * @return the text value of the named element or null of no such element exists
     * 
     * <b>deprecated</b> use utils.XMLutils.getElementString(String name,Element el)
     * 
     * @see {@link utils.xml.XMLutils#getElementString(String name,Element el)}
     */
    public String getElementValue(String name, Element di) {
        Element e;
        Attribute attr;
        String element_value = null;

        if ((e = di.getChild(name)) != null) {
            element_value = e.getText();
        } else if ((attr = di.getAttribute(name)) != null) {
            element_value = attr.getValue();
        }

        return element_value;
    }

    /**
     * Get the background colour for this DataItem using a colour specification in 
     * an XML Element/Attribute.
     * 
     * @param node the element containing the colour specification
     */
    private void getBackgroundColour(Element node) {
        bgcolour = XMLutils.getColour(node);
    }

    /**
     * Read the description for the DataItem.  Sub classes should override this method to
     * read the description but first call super.readDescription(Element di) to setup the
     * basic DataItem elements (title, name etc.)
     * 
     * @param di  the root node for the DataItem description
     */
    public void readDescription(Element di) {
        Element e;
        Attribute attr;

        // get the basic attibutes   		
        name = XMLutils.getElementString("name", di); //$NON-NLS-1$
        title = XMLutils.getElementString("title", di); //$NON-NLS-1$
        description = XMLutils.getElementString("description", di); //$NON-NLS-1$
        tooltip = XMLutils.getElementString("tooltip", di); //$NON-NLS-1$
        value = XMLutils.getElementString("value", di); //$NON-NLS-1$
        fieldname = XMLutils.getElementString("fieldname", di); //$NON-NLS-1$

        // get the background colour
        getBackgroundColour(di);

        // get the URL of a streaming data source
        streamSource = XMLutils.getElementString("stream", di); //$NON-NLS-1$

        // get a Font specification
        // see java.awt.Font
        if ((e = di.getChild("font")) != null) //$NON-NLS-1$
        {
            font = Font.decode(e.getText());
        } else if ((attr = di.getAttribute("font")) != null) //$NON-NLS-1$
        {
            font = Font.decode(attr.getValue());
        }

        // set the background colour
        if (bgcolour != null) {
            setBackground(bgcolour);
        }

        Integer intval;

        // get width and height specifications
        if ((intval = XMLutils.getElementInt("width", di)) != null) //$NON-NLS-1$
        {
            prefWidth = intval.intValue();
        }

        if ((intval = XMLutils.getElementInt("height", di)) != null) //$NON-NLS-1$
        {
            prefHeight = intval.intValue();
        }

        // get any data models
        List<Element> dms = di.getChildren("dataModel"); //$NON-NLS-1$

        if (dms != null && dms.size() > 0) {
            Iterator<Element> iterator = dms.iterator();

            // get a handle in the DataModelManager singleton instance
            DataModelManager mgr = DataModelManager.getInstance();

            String path = getPath();
            // iterate through each data model specification adding them to the
            // data model manager instance
            while (iterator.hasNext()) {
                e = iterator.next();

                mgr.readModel(e, path);
            }
        }

        // get the name of a data model for this DataItem
        String modelName;
        if ((modelName = getElementValue("modelName", di)) != null) {  //$NON-NLS-1$
            DataModelManager mgr = DataModelManager.getInstance();

            model = mgr.getModel(modelName);

            if (model == null) {
                LOG.error(Messages.getString(BUNDLE_NAME, "DataItem.16") + modelName); //$NON-NLS-1$
            }
        }
        // get the path within the named data model to this DataItem's data
        modelPath = XMLutils.getElementString("modelPath", di); //$NON-NLS-1$

        // read a set of validators
        validators = getValidators(di);

    }

    /**
     * Get an InputStream from a Web Service.
     *
     * @param url  the URL of the Web Service
     * @param params an ArrayList of NameValuePairs to send as parameters
     * @return an InputStream to read the output of the Web Service or null if an error occurs
     * @throws IOException
     * 
     * @see {@link org.ribax.common.data.DataUtils#getInputStream(String, ArrayList, String)}
     */
    public InputStream getInputStream(String url, ArrayList<NameValuePair> params) throws IOException {
        return NetUtils.getInputStream(url, params, name);
    }

    /**
     * Access a Web Service and parse the ouput into an Element tree.  All Exceptions
     * are caught by this method and a message dialog is displayed with the details of
     * the Exception.
     *
     * @param url the URL of the Web Service
     * @param params an ArrayList of NameValuePairs to send as parameters
     * @param action HTTP parameter encoded as 'Action=action'
     * @return  the root node of the Element tree or null if an error occurs
     */
    public Element getElementFromURL(String url, ArrayList<NameValuePair> params, String action) {

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            InputStream fin = getInputStream(url, params);

            // create Element tree from XML 
            SAXBuilder builder = new SAXBuilder();

            Document doc = builder.build(fin);

            // get the root node for the document
            Element node = doc.getRootElement();

            return node;
        } catch (MalformedURLException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "DataItem.21") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "DataItem.22") + url, ex); //$NON-NLS-1$
        } catch (JDOMException ex) {
            // indicates a well-formedness error
            errorMessage(Messages.getString(BUNDLE_NAME, "DataItem.23") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "DataItem.24") + url, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "DataItem.25") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "DataItem.26") + url, ex); //$NON-NLS-1$
        } catch (Exception ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "DataItem.27") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "DataItem.28") + url, ex); //$NON-NLS-1$
        } finally {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        return null;
    }

    /**
     * Read the description of this Data Item from a URL.
     * 
     * @param url the URL of the XML description file
     * @param params an ArrayList of NameValuePairs to send as parameters
     * @param action HTTP parameter encoded as 'Action=action'
     */
    public void readDescriptionFromURL(String url, ArrayList<NameValuePair> params, String action) {

        Element node = getElementFromURL(url, params, action);

        if (node != null) {
            readDescription(node);
        }
    }

    /** Extract a list of validators from an Element Tree
     * 
     * @param node the Element containing 0 or more validators
     * @return the Vector of extracted validators
     */
    public Vector<Validator> getValidators(Element node) {
        Vector<Validator> vec = new Vector<Validator>();

        if (XMLutils.getElementString("regexp", node) != null) {
            // add a single validator to the validators list
            Validator v = ValidatorFactory.readValidator(node);

            if (v == null) {
                return null;
            }

            vec.add(v);

            return vec;
        }

        /* iterate through the children of the given node, 
         * each child is a specification of a validator
         */
        List<Element> vlist = node.getChildren("validator");

        if (vlist.size() == 0) {
            return null;
        }

        Iterator<Element> vit = vlist.iterator();
        while (vit.hasNext()) {
            Element el = (Element) vit.next();

            Validator v = ValidatorFactory.readValidator(el);

            if (v != null) {
                vec.add(v);
            }
        }
        return vec;
    }

    /**
     * Signal that the data is invalid and the DataItem subclass should reload the data
     * on the next invocation of loadData()
     * 
     */
    public void invalidateData() {
        loaded = false;
    }

    /**
     * Get the short text description of this Data Item.
     * 
     * @return  the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description.
     * 
     * @param  description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the fieldname.
     * 
     * @return  the fieldname.
     */
    public String getFieldname() {
        return fieldname;
    }

    /**
     * Set the fieldname.
     * 
     * @param fieldname The fieldname to set.
     */
    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    /**
     * Get the displayable title.
     * 
     * @return  the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the displayable title.
     * 
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the tooltip for the DataItem panel.
     * 
     * @return  the tooltip.
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Set the tooltip for the DataItem panel.
     * 
     * @param tooltip The tooltip to set.
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * Get the data streaming source URL
     *
     * @return  the streamSource URL.
     */
    public String getStreamSource() {
        return streamSource;
    }

    /**
     * Set the source URL to use for data streaming.
     *
     * @param streamSource The streamSource URL.
     */
    public void setStreamSource(String streamSource) {
        this.streamSource = streamSource;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (name != null) {
            return name;
        }

        return title;
    }

    /**
     * Indicate whether the DataItem subclass can be printed
     * 
     * @return true if this class support printing, false otherwise
     */
    public boolean isPrintable() {
        return false;
    }

    /**
     * Print the contents of the DataItem subclass
     */
    public void print() {
    }

    /** Display an error message dialog to the user.
     * 
     * @param message the error message to display
     */
    public void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message,
                "RIBAX", //$NON-NLS-1$
                JOptionPane.ERROR_MESSAGE);
    }

    /** Display an information message dialog to the user.
     * 
     * @param message the information message to display
     */
    public void infoMessage(String message) {
        JOptionPane.showMessageDialog(this, message,
                "RIBAX", //$NON-NLS-1$
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Get the printable component for the DataItem subclass
     * @return a component that can be printed
     */
    public Object getPrintableComponent() {
        return null;
    }

    /**
     * Get the Printable for the DataItem subclass
     * @return the Printable
     * 
     * @see {@link java.awt.print.Printable}
     */
    public Printable getPrintable() {
        return null;
    }

    /**
     * Get the name of this DataItem.  
     * 
     * @return A String that identifies this particular DataItem
     */
    public String getDataItemName() {
        return name == null ? "" : name; //$NON-NLS-1$
    }

    /////////// Drag and Drop methods 
    /**
     * Get a Transferable object for Drag and Drop data transfer.  
     * 
     * Override this method and provide a Transferable object to effect data transfer
     */
    public Transferable getTransferable() {
        return null;
    }

    /// Transferable methods
   	/* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors() {
        return null;
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return false;
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(DataFlavor flavor) {
        return null;
    }

    /**
     * Override this method to import data from a drag and drop operation.
     * 
     * @param c  The component that was under the drop point
     * @param t  The Transferable containing the data being transferred
     * @return true if the data was imported false otherwise
     * 
     * @see {@link java.awt.datatransfer.Transferable }
     */
    public boolean importData(JComponent c, Transferable t) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataItem.34")); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * Override this method to handle the export done event.
     * 
     * @param c  the component that exported the data
     * @param data the Transferable containing the exported data
     * @param action
     * 
     * @see {@link java.awt.datatransfer.Transferable }
     */
    public void exportDone(JComponent c, Transferable data, int action) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataItem.35")); //$NON-NLS-1$
        }
    }

    /**
     * Get the pathname that identifies this DataItem in the
     * applications DataItem tree.  A DataItem pathname corresponds to the names
     * of the ancestors of this DataItem seperated by '/' e.g.
     * <code>rootfolderName/subfolderName/tabName/DataItemName</code>
     *
     * @return Return the pathname for this DataItem.
     */
    public String getPath() {
        if (parentDataItem != null) {
            String path = parentDataItem.getPath();

            if (path == null || path.equals("")) //$NON-NLS-1$
            {
                return getDataItemName();
            }

            return path + "/" + name; //$NON-NLS-1$
        } else {
            return getDataItemName();
        }
    }

    /** Validate the contents of this Data Item
     *
     * @return true or false whether the data was valid (true) or not (false)
     */
    public boolean validateContents() {
        return true;
    }

    // these methods must be overridden by sub classes
    /**
     * Load the data for this DataItem from a defined data source.
     * 
     * @param params a list of parameters to add to the request
     * @param action an action string identifying the kind of button press (if any) 
     * which initiated the process of loading data
     */
    public abstract void loadData(ArrayList<NameValuePair> params, String action);

    /**
     * Reload the data for this DataItem from the defined data source.
     * 
     * @param params a list of parameters to add to the request
     * @param action an action string identifying the kind of button press (if any) 
     * which initiated the process of reloading data
     */
    public abstract void refresh(ArrayList<NameValuePair> params, String action);

    /**
     * Update the data for this DataItem to the defined data source.
     *
     * @param params a list of parameters to add to the request
     * @param action an action string identifying the kind of button press (if any)
     * which initiated the process of loading data
     */
    public abstract void updateData(ArrayList<NameValuePair> params, String action);

    /**
     * Set the data for this DataItem from the data contained in the element hierarchy 
     * 
     * @param node the root element in the data hierarchy
     */
    public abstract void setData(Element node);

    /**
     * Get the displayable swing component for this DataItem
     * 
     * @return the swing component that can be displayed without titles
     */
    public abstract Component getComponent();

    /**
     * Get the list of parameters for this DataItem.  Each DataItem subclass
     * interprets what it considers to be the list of parameters.
     * 
     * @return the list of parameters which is an ArrayList of utils.types.NameValuePair objects
     * 
     * @see {@link utils.types.NameValuePair}
     */
    public abstract ArrayList<NameValuePair> getParameters();

    /**
     * Get the list of all data elements for this DataItem.
     * 
     * @return the list of elements which is an ArrayList of utils.types.NameValuePair objects
     * 
     * @see {@link utils.types.NameValuePair}
     */
    public abstract ArrayList<NameValuePair> getAllElements();

    /**
     * Get the list of selected elements for this DataItem.  Each DataItem subclass
     * interprets what it considers to be the list of selected elements.
     * 
     * @return the list of selected elements which is an ArrayList of utils.types.NameValuePair objects
     * 
     * @see {@link utils.types.NameValuePair}
     */
    public abstract ArrayList<NameValuePair> getSelectedElements();

    /**
     * The name for the subclass of DataItem.
     * 
     * @return the string representation of the type of DataItem subclass
     */
    public abstract String getTypeName();

    /**
     * Perform any operations on close/shutdown, called before the Application exits
     * 
     */
    public abstract void close();

    /**
     * Get the data for the DataItem subclass as a utils.NameValuePair object
     * 
     * @return the data for the DataItem subclass
     * 
     * @see {@link utils.types.NameValuePair}
     */
    public abstract NameValuePair getNameValuePair();
}
