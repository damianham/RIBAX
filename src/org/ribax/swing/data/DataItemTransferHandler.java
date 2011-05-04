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

import javax.swing.TransferHandler;
import javax.swing.JComponent;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Vector;

import org.ribax.common.Messages;
import org.ribax.swing.ui.DataItem;

import utils.log.BasicLogger;

/**
 * An implementation of TransferHandler that adds support for dropping
 * DataItems. Dropping a DataItem on a component having this TransferHandler
 * adds the DataItem to the component.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class DataItemTransferHandler extends TransferHandler {

    public static final long serialVersionUID = 1;
    private static final String BUNDLE_NAME = "org.ribax.swing.data.messages"; //$NON-NLS-1$
    /** The set of DataFlavors handled by this TransferHandler */
    Vector<DataFlavor> flavours = new Vector<DataFlavor>();
    /** The DataItem that is associated with this TransferHandler that will handle
     * import/export data events */
    private DataItem dataItemHandler = null;
    /** The name of the DataItem that is associated with this TransferHandler */
    private String name;
    private static BasicLogger LOG = new BasicLogger(DataItemTransferHandler.class.getName());

    /**
     * Get the DataFlavors we can handle.
     * 
     * @return the DataFlavors handled by this TransferHandler.
     */
    public Vector<DataFlavor> getFlavours() {
        return flavours;
    }

    /**
     * Set the DataFlavors handled by this TransferHandler.
     * @param flavours the DataFlavors to set.
     */
    public void setFlavours(Vector<DataFlavor> flavours) {
        this.flavours = flavours;
    }

    /**
     * Constructor that takes the DataItem associated with this TransferHandler.
     * 
     * @param item the DataItem associated with this TransferHandler.
     */
    public DataItemTransferHandler(DataItem item) {
        this.name = item == null ? "n/a" : item.getDataItemName();

        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataItemTransferHandler.0") + item.hashCode()); //$NON-NLS-1$

        }
        this.dataItemHandler = item;

        // add the basic DataItem Flavour local version for transfer within the same application
        addFlavor("org.ribax.swing.ui.DataItem"); //$NON-NLS-1$

        // add the serial version for inter application data transfer
        addFlavor(new DataFlavor(DataItem.class, "DataItem")); //$NON-NLS-1$
    }

    /**
     * Add a DataFlavor to the front of the list of DataFlavors so it becomes the 
     * preferred DataFlavor.
     * 
     * @param classname the fully qualified classname of the class to add.
     */
    public synchronized void pushFlavor(String classname) {
        //add the flavor to the front of the list so it is the preferred flavour
        try {
            flavours.add(0, new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=" + classname)); //$NON-NLS-1$
        } catch (ClassNotFoundException e) {
            System.out.println(Messages.getString(BUNDLE_NAME, "DataItemTransferHandler.4")); //$NON-NLS-1$
        }
    }

    /**
     * Add a DataFlavor to the end of the list of DataFlavors.
     * 
     * @param classname the fully qualified classname of the class to add.
     */
    public synchronized void addFlavor(String classname) {
        try {
            flavours.add(new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=" + classname)); //$NON-NLS-1$
        } catch (ClassNotFoundException e) {
            System.out.println(Messages.getString(BUNDLE_NAME, "DataItemTransferHandler.6")); //$NON-NLS-1$
        }
    }

    /**
     * Add a DataFlavor to the front of the list of DataFlavors so it becomes the 
     * preferred DataFlavor.
     * 
     * @param flavor the DataFlavor to add.
     */
    public synchronized void pushFlavor(DataFlavor flavor) {
        //add the flavor to the front of the list so it is the preferred flavour
        flavours.add(0, flavor);
    }

    /**
     * Add a DataFlavor to the end of the list of DataFlavors.
     * 
     * @param flavor the DataFlavor to add.
     */
    public synchronized void addFlavor(DataFlavor flavor) {
        // add the flavor to the end of the list
        flavours.add(flavor);
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
     */
    public boolean importData(JComponent c, Transferable t) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataItemTransferHandler.7") + dataItemHandler); //$NON-NLS-1$
        }
        // if a handler is setup then pass the event to the handler
        if (dataItemHandler != null) {
            return dataItemHandler.importData(c, t);
        } else if (c instanceof DataItem) {
            // otherwise pass the event to the component associated with the event
            // i.e. the source of the drag or target of the drop operations
            return ((DataItem) c).importData(c, t);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#exportDone(javax.swing.JComponent, java.awt.datatransfer.Transferable, int)
     */
    protected void exportDone(JComponent c, Transferable data, int action) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataItemTransferHandler.8") + dataItemHandler); //$NON-NLS-1$
        }
        if (dataItemHandler != null) {
            dataItemHandler.exportDone(c, data, action);
        } else if (c instanceof DataItem) {
            // otherwise pass the event to the component associated with the event
            // i.e. the source of the drag or target of the drop operations
            ((DataItem) c).exportDone(c, data, action);
        }
    }

    /**
     * Check to see if the DataFlavor list has a DataFlavor we like the taste of ?
     * 
     * @param flavors the array of DataFlavors that are on offer.
     * @return the preferred DataFlavor matching an one of the DataFlavors on offer 
     * or null of no DataFlavors match.
     */
    protected DataFlavor getPreferredFlavour(DataFlavor[] flavors) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataItemTransferHandler.9") + dataItemHandler); //$NON-NLS-1$
        }
        if (flavours == null) {
            // we have no DataFlavors 
            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataItemTransferHandler.10")); //$NON-NLS-1$
            }
            return null;
        }

        // iterate through the list of DataFlavors
        for (int i = 0; i < flavors.length; i++) {
            for (int j = 0; j < flavours.size(); j++) {
                DataFlavor flavour = flavours.get(j);

                // check to see if we can accept this DataFlavor
                if (flavour.equals(flavors[i])) {
                    return flavour;
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataItemTransferHandler.11")); //$NON-NLS-1$
        }
        // nothing matched
        return null;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
     */
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        // check that we can accept the data in one of the provided DataFlavors
        return getPreferredFlavour(flavors) != null;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
     */
    protected Transferable createTransferable(JComponent c) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(name + Messages.getString(BUNDLE_NAME, "DataItemTransferHandler.12") + dataItemHandler); //$NON-NLS-1$
        }
        // a DataItem implements the Transferable interface
        if (c instanceof DataItem) {
            return (DataItem) c;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
     */
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }
}
