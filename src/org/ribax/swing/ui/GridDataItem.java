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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdom.Element;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

import org.ribax.common.Messages;
import org.ribax.swing.data.DataItemTransferHandler;

/**
 * A class that lays out a set of Data Items in a grid with a specified 
 * number of columns.  The default number of columns is 3.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class GridDataItem extends DataItemContainer {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(GridDataItem.class.getName());
    /** The main panel for this component */
    private JPanel panel = new JPanel();
    /** A Gridlayout for this component */
    private GridLayout layout = null;
    /////////////  Grid suports drag and drop
    /** A TransferHandler for drag and drop operations */
    private DataItemTransferHandler dragNdropHandler = null;

    /**
     * No argument Constructor - required
     */
    public GridDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element grid) {

        super.readDescription(grid);

        int columns = 3;

        Integer intval;

        // get the number of columns
        if ((intval = XMLutils.getElementInt("columns", grid)) != null) //$NON-NLS-1$
        {
            columns = intval.intValue();
        }

        layout = new GridLayout(0, columns);

        // get the cell spacing between each element
        if ((intval = XMLutils.getElementInt("cellspacing", grid)) != null) { //$NON-NLS-1$
            layout.setHgap(intval.intValue());
            layout.setVgap(intval.intValue());
        }

        // setup Drag and Drop capabilities
        setupDND();

        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "GridDataItem.8")); //$NON-NLS-1$
        }
        // layout the GUI components
        layoutComponents(columns);

    }

    /**
     * layout the GUI components of this DataItem 
     * @param columns
     */
    private void layoutComponents(int columns) {

        // clear any previous GUI components
        removeAll();

        // use BorderLayout for this enclosing panel
        setLayout(new BorderLayout());

        // set the background colour of the panel
        if (bgcolour != null) {
            panel.setBackground(bgcolour);
        }

        // set the GridLayout of the panel containing the DataItems
        panel.setLayout(layout);

        // add each DataItem to the grid
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            // we don't add hidden data items
            if (item instanceof HiddenDataItem) {
                continue;
            }

            panel.add(item);

            // set the drag and drop handler for the DataItem to this component
            // so if a drag or drop occurs over the DataItem it is handled by this 
            // GridDataItem
            if (dragNdropHandler != null) {
                item.setTransferHandler(dragNdropHandler);
            }

        }

        // create a scroll pane with the panel as the viewport
        JScrollPane scrpane = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        if (prefWidth > 0 || prefHeight > 0) {
            Dimension d = new Dimension(prefWidth == 0 ? 400 : prefWidth,
                    prefHeight == 0 ? 400 : prefHeight);
            scrpane.setPreferredSize(d);
        }

        // add the scrollpane to this component
        add(scrpane, BorderLayout.CENTER);

        validate();
        repaint();
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public Component getComponent() {
        return this;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */
    public String getTypeName() {
        return DataItemFactory.GRID;
    }

    // Drag and Drop methods
    /**
     * Setup Drag and Drop capabilities for this component.
     */
    private void setupDND() {

        dragNdropHandler = new DataItemTransferHandler(this);
        dragNdropHandler.addFlavor("java.util.ArrayList"); //$NON-NLS-1$

        // add the handler to this component so it will accept a drop
        this.setTransferHandler(dragNdropHandler);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#exportDone(javax.swing.JComponent, java.awt.datatransfer.Transferable, int)
     */
    public void exportDone(JComponent c, Transferable data, int action) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "GridDataItem.16") + action); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
     */
    public boolean importData(JComponent c, Transferable t) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "GridDataItem.17")); //$NON-NLS-1$
        }
        // get the flavours I support
        Vector<DataFlavor> flavors = dragNdropHandler.getFlavours();

        // foreach supported flavour try to get a transfer object of that flavour
        for (DataFlavor flavor : flavors) {

            try {
                Object o = t.getTransferData(flavor);

                // no exception so we have a transfer object
                // check that we support it and import the data
                if (o instanceof DataSetDataItem || o instanceof ArrayList) {
                    return doImport(o);
                }
            } catch (UnsupportedFlavorException ufe) {
            } catch (IOException ex) {
                errorMessage(Messages.getString(BUNDLE_NAME, "GridDataItem.18") + ex); //$NON-NLS-1$
                return false;
            }
        }
        errorMessage(Messages.getString(BUNDLE_NAME, "GridDataItem.19")); //$NON-NLS-1$
        return false;
    }

    /**
     * Import data from a Drop operation.
     * 
     * @param data the data to import.
     * @return true if we imported the data, false otherwise.
     */
    public boolean doImport(Object data) {
        // we accept a DataItem or an ArrayList of NameValuePairs
        try {

            // if it is a data item then just add it to the grid
            if (data instanceof DataItem) {
                panel.add((DataItem) data);
                dataItems.add((DataItem) data);

            } else if (data instanceof ArrayList) {
                // otherwise create a new instance of a DataSetDataItem
                // with the data elements in the list
                DataSetDataItem dhi = new DataSetDataItem();

                dhi.setParent(this);

                ArrayList<NameValuePair> list = (ArrayList<NameValuePair>) data;

                int visible = 1;
                for (NameValuePair pair : list) {
                    dhi.addDataElement(pair.getName(), pair.getValue().toString(), visible);
                    // make all but the first element invisible
                    visible = 0;
                }
                // and add it to the grid
                panel.add(dhi);
                dataItems.add(dhi);
            }
            return true;
        } catch (ClassCastException cce) {
            errorMessage(Messages.getString(BUNDLE_NAME, "GridDataItem.20")); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * Get the Transferable for the indicated DataItem.  This is as a result of a drag gesture
     * over the component.
     * 
     * @param component that the drag gestured occured over and which should supply the
     * transferable data.
     * 
     * @return a Transferable data object or null if the component was not a DataItem
     */
    public Transferable getTransferable(Component component) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + " getTransferable()"); //$NON-NLS-1$
        }
        if (component instanceof DataItem) {
            return ((DataItem) component).getTransferable();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "GridDataItem.22")); //$NON-NLS-1$
        }
        return null;
    }
}
