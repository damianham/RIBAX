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

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;

import org.jdom.Element;

/**
 * A Data Item that groups other data items onto a panel in a horizontal layout
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class PanelDataItem extends DataItemContainer {

    public static final long serialVersionUID = 1;

    /**
     * No argument Constructor - required
     */
    public PanelDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {

        super.readDescription(di);

        // layout the GUI components
        layoutComponents();
    }

    /**
     * Layout the components of this DataItem.
     */
    private void layoutComponents() {

        removeAll();

        // add each DataItem to the panel
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);
            if (item instanceof HiddenDataItem) {
                continue;
            } else {
                add(item);
            }
        }

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
     * @see org.ribax.swing.ui.DataItem#isPrintable()
     */
    public boolean isPrintable() {
        // this is printable if any data item on the panel is printable
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item == null) {
                continue;
            }
            if (item.isPrintable()) {
                return true;
            }
        }
        return false;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#print()
     */

    public void print() {
        // for each DataItem in our internal list tell them to print themselves
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            if (item == null) {
                continue;
            }
            item.print();
        }
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */

    public String getTypeName() {
        return DataItemFactory.PANEL;
    }

    /**
     * Import data from a drag and drop or cut and paste operation.
     *
     * @param flavour  the kind of data that is being imported
     * @param data  the data object to import
     * @return true or false whether the data was imported
     */
    public boolean importData(DataFlavor flavour, Object data) {

        // we can only accept a DataItem
        if (data instanceof DataItem) {
            dataItems.add((DataItem) data);
            layoutComponents();
            return true;
        }
        return false;
    }
}
