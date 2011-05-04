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
import java.net.URL;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.tree.*;
import javax.swing.border.EmptyBorder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.ribax.common.Messages;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * A class which is the container for the different elements of the
 * application.  Each folder is represented by an expandable node in
 * the application tree
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class Folder extends DataItem {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(Folder.class.getName());
    /** An Icon for the Folder which is displayed in the expandable Folder tree */
    private ImageIcon icon = null;
    /** A heading which is displayed above the Tabbed Pane */
    private String heading = null;
    /** The url of the delegated Folder description */
    private String url = null;
    /** The tabbed pane containing the Tabs in this Folder */
    protected JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    /** The list of Tabs in this Folder */
    private Vector<DataItem> dataItems = new Vector<DataItem>();
    /** The panel for this component  */
    private Box box = Box.createVerticalBox();

    /**
     * Constructor that takes a name, title, description, logo and heading
     * 
     * @param name the internal name of this folder
     * @param title the title of this folder
     * @param description a text description of this folder
     * @param logo a URL to a logo to display for this folder
     * @param heading a heading for this folder displayed above the Tabbed Pane
     */
    public Folder(String name, String title, String description, String logo, String heading) {

        super(name, title, description, null);

        // create the icon
        if (logo != null) {
            this.icon = new ImageIcon(logo);
        }
        this.heading = heading;

        // user Borderlayout for this component
        setLayout(new BorderLayout());
        add(box, BorderLayout.CENTER);

    }

    /**
     * Constructor that takes the parent DataItem (Folder).
     * 
     * @param parent the parent DataItem (Folder) containing this Folder
     */
    public Folder(DataItem parent) {

        setParent(parent);
        setLayout(new BorderLayout());
        add(box, BorderLayout.CENTER);
    }

    /**
     * Get the Icon for this Folder.
     * 
     * @return the icon of one has been specified otherwise null.
     */
    public ImageIcon getFolderIcon() {
        if (icon != null) {
            return icon;
        }

        return null;
    }

    /**
     * Layout the GUI components of this Folder.
     */
    private void layoutComponents() {

        // clear the box
        box.removeAll();

        // add the heading if one has been specified
        if (heading != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "Folder.0")); //$NON-NLS-1$
            }
            JLabel p1 = new JLabel(heading);
            box.add(p1);
        }

        // add the tabbed pane if there is more than 1 tab
        if (dataItems.size() > 1) {
            box.add(tabbedPane);

        } else if (dataItems.size() == 1) {
            // otherwise add the single tab as the main component
            DataItem item = (DataItem) dataItems.elementAt(0);
            box.add(item);
        }

        validate();

        repaint();
    }

    /**
     * Read the definition of this Folder from the XML element tree.
     * 
     * @param treenode the JTree node that represents this Folder
     * @param node the XML element tree
     */
    public void readFolder(DefaultMutableTreeNode treenode, Element node) {
        Element e;
        String source = null;

        // base class reads common values
        super.readDescription(node);

        // get the heading
        heading = XMLutils.getElementString("heading", node); //$NON-NLS-1$

        // if a background colour has been specified then set it also for the tabbed pane
        if (bgcolour != null) {
            tabbedPane.setBackground(bgcolour);
        }

        source = XMLutils.getElementString("source", node); //$NON-NLS-1$
        // if a source has been specified then read the description from the source
        // test the value of url, if it has been set then this could be the 2nd
        // time we are in this method, a source tag in a sourced description would
        // otherwise cause an infinite loop
        if (source != null && url == null) {
            url = source;
            readFolderFromURL(source, treenode);
            return;
        }

        // read the list of tabs in this folder
        if ((e = node.getChild("tabList")) != null) { //$NON-NLS-1$
            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "Folder.4")); //$NON-NLS-1$
            }
            // iterate through the list
            List<Element> children = e.getChildren();
            Iterator<Element> iterator = children.iterator();
            while (iterator.hasNext()) {
                Element tabnode = (Element) iterator.next();

                // create a new Tab and read the definition
                Tab t = new Tab(this);
                t.readDescription(tabnode);

                // add the tab to this folder
                addDataItem(t);
            }
        }

        Element tds;
        // get any tabbed data sets
        // Tabbed data sets are siblings of other tabs in the tabbed pane and are
        // themselves a tabbed pane with a set of sub tabs
        if ((tds = node.getChild("tabbedDataSet")) != null) { //$NON-NLS-1$
            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "Folder.6")); //$NON-NLS-1$
            }
            // create a new tabbed data set
            TabbedDataSet tabset = new TabbedDataSet(this);

            // read the description and add it to the tabbed pane
            tabset.bgcolour = bgcolour;
            tabset.readDescription(tds);

            addDataItem(tabset);
        }

        // read any sub folders recursively
        if ((e = node.getChild("folderList")) != null) { //$NON-NLS-1$
            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "Folder.8")); //$NON-NLS-1$
            }
            // iterate through the list of folders
            List<Element> children = e.getChildren();
            Iterator<Element> iterator = children.iterator();
            while (iterator.hasNext()) {
                Element folder = (Element) iterator.next();

                // create a new Folder
                Folder f = new Folder(this);

                DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(f);

                // read the description of the folder and add it to the JTree
                // as a child of the tree node that this Folder represents
                f.readFolder(newnode, folder);

                treenode.add(newnode);
            }
        }

        layoutComponents();

        // get any specified icon
        try {
            String logo = XMLutils.getElementString("icon", node); //$NON-NLS-1$
            if (logo != null) {
                URL u = new URL(logo);
                this.icon = new ImageIcon(u);  //createAppletImageIcon(logo,title);
            }
        } catch (Exception ex) {
            LOG.error(name, ex);
        }

    }

    /**
     * Read the description of the Folder from the specified URL.
     * 
     * @param url the URL of a web service providing the description of the Folder
     * @param treenode the JTree node that represents this Folder
     */
    private void readFolderFromURL(String url, DefaultMutableTreeNode treenode) {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        // add the LoadDescription action
        list.add(new NameValuePair("Action", "LoadDescription")); //$NON-NLS-1$ //$NON-NLS-2$

        try {
            // get an input stream from the URL
            InputStream fin = getInputStream(url, list);

            // build the XML document
            SAXBuilder builder = new SAXBuilder();

            Document doc = builder.build(fin);

            // get the root Element in the document
            Element root = doc.getRootElement();

            readFolder(treenode, root);

        } catch (MalformedURLException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "Folder.12") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "Folder.13") + url, ex); //$NON-NLS-1$
        } catch (JDOMException ex) {
            // indicates a well-formedness error
            errorMessage(Messages.getString(BUNDLE_NAME, "Folder.14") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "Folder.15") + url, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "Folder.16") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "Folder.17") + url, ex); //$NON-NLS-1$
        } catch (Exception ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "Folder.18") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "Folder.19") + url, ex); //$NON-NLS-1$
        }
    }

    /**
     * Add a component to the tabbed pane.
     * 
     * @param item the component to add.
     */
    protected void addDataItem(DataItem item) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Folder.20") + item.getDataItemName() + Messages.getString(BUNDLE_NAME, "Folder.21")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        item.setBorder(new EmptyBorder(5, 5, 5, 5));
        dataItems.add(item);
        String title = item.getTitle();
        if (title == null || title.length() == 0) {
            title = item.toString();
        }
        tabbedPane.addTab(title, item);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#toString()
     */
    public String toString() {
        if (title != null && title.length() > 0) {
            return title;
        }

        return ""; //return name; //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#refresh(java.util.ArrayList, java.lang.String)
     */
    public void refresh(ArrayList<NameValuePair> params, String action) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + " refresh");
        }

        // refresh each Tab in the tabbed pane
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);
            item.refresh(params, action);
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#close()
     */
    public void close() {

        // invalidate all the Tabs in this folder
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);
            item.close();
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#invalidateData()
     */
    public void invalidateData() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Folder.24")); //$NON-NLS-1$
        }
        loaded = false;

        // invalidate all the Tabs in this folder
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);
            item.invalidateData();
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#updateData(java.util.ArrayList, java.lang.String)
     */
    public void updateData(ArrayList<NameValuePair> params, String action) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Folder.25")); //$NON-NLS-1$
        }
        // update all the Tabs in this folder
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);
            item.updateData(params, action);
        }

    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#loadData(java.util.ArrayList, java.lang.String)
     */

    public void loadData(ArrayList<NameValuePair> params, String action) {

        if (loaded) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "Folder.26")); //$NON-NLS-1$
        }
        // tell the tabs in this folder to load their data
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item == null) {
                LOG.error(name + Messages.getString(BUNDLE_NAME, "Folder.27") + getTitle()); //$NON-NLS-1$
                continue;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "Folder.28") + item.getTitle()); //$NON-NLS-1$
            }
            item.loadData(params, action);

        }

        loaded = true;
    }

    /**  Returns an ImageIcon, or null if the path was invalid.
    When running an applet using Java Plug-in,
    getResourceAsStream is more efficient than getResource.
    
     * @param path the path to the image icon
     * @param description a text description of the image
     * @return a new ImageIcon or null if the image could not be loaded from the 
     * specified path
     */
    protected static ImageIcon createAppletImageIcon(String path,
            String description) {
        int MAX_IMAGE_SIZE = 575000; //Change this to the size of
        //your biggest image, in bytes.
        int count = 0;
        BufferedInputStream imgStream = new BufferedInputStream(
                Folder.class.getResourceAsStream(path));
        if (imgStream != null) {
            byte buf[] = new byte[MAX_IMAGE_SIZE];
            try {
                count = imgStream.read(buf);
            } catch (IOException ieo) {
                LOG.error(Messages.getString(BUNDLE_NAME, "Folder.29") + path); //$NON-NLS-1$
                LOG.error(ieo.getLocalizedMessage());
            }

            try {
                imgStream.close();
            } catch (IOException ieo) {
                LOG.error(Messages.getString(BUNDLE_NAME, "Folder.30") + path); //$NON-NLS-1$
            }

            if (count <= 0) {
                LOG.error(Messages.getString(BUNDLE_NAME, "Folder.31") + path); //$NON-NLS-1$
                return null;
            }
            return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf),
                    description);
        } else {
            LOG.error(Messages.getString(BUNDLE_NAME, "Folder.32") + path); //$NON-NLS-1$
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public Component getComponent() {
        return this;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */

    public NameValuePair getNameValuePair() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getParameters()
     */

    public ArrayList<NameValuePair> getParameters() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getAllElements()
     */

    public ArrayList<NameValuePair> getAllElements() {
        return getParameters();
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getSelectedElements()
     */

    public ArrayList<NameValuePair> getSelectedElements() {
        return getParameters();
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */
    public void setData(Element node) {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */
    public String getTypeName() {
        return DataItemFactory.FOLDER;
    }
}
