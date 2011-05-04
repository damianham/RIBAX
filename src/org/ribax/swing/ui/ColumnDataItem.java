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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.SwingConstants;
import javax.swing.JPanel;

import org.jdom.Element;
import org.ribax.common.Messages;


import utils.log.BasicLogger;

/**
 * A class that lays out a set of Data Items in a column so that labels 
 * and fields are aligned vertically.  We use a GridBagLayout to display the
 * title for each DataItem in the left column and the component field in the right
 * column.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 * 
 * @see org.ribax.swing.ui.DataItem
 * @see org.ribax.swing.ui.GridDataItem
 */
public class ColumnDataItem extends DataItemContainer {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(ColumnDataItem.class.getName());
    /** The panel which contains the components */
    private JPanel panel = new JPanel();
    // a set of GridBagConstraints for laying out the items
    private GridBagConstraints gbc = new GridBagConstraints();

    /**
     * No argument Constructor (required by DataItemFactory)
     * 
     * @see org.ribax.swing.ui.DataItemFactory
     */
    public ColumnDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element cdi) {

        super.readDescription(cdi);

        // layout the components of this DataItem
        layoutComponents();
    }

    /**
     * Add the given DataItem to the panel.  We use GridBagLayout to add the DataItem's
     * title in the left column and the DataItem's GUI component in the right column
     * 
     * @param item the DataItem to add
     * @param num the DataItem's ordinal index
     */
    protected void addDataItem(DataItem item, int num) {

        // get the GUI component from the DataItem
        Component field = item.getComponent();

        // check it isn't hidden and not null
        if (item instanceof HiddenDataItem || field == null) {
            return;
        }

        gbc.gridy = num;
        gbc.gridwidth = 1;

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.weightx = 0;

        // add the title in the left column
        panel.add(new JLabel(item.title, SwingConstants.RIGHT), gbc);

        if (item instanceof EditorDataItem
                || item instanceof InfoDataItem
                || item instanceof ListDataItem
                || item instanceof TableDataItem
                || item instanceof UrlDataItem
                || item instanceof PanelDataItem) {
            gbc.weighty = 1;
        } else {
            gbc.weighty = 0;
        }

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.weightx = 1;

        // use a Box with horizontal glue to absorb extra space in the horizontal axis
        Box box = Box.createHorizontalBox();

        // add the component in the right column
        box.add(field);
        box.add(Box.createHorizontalGlue());

        panel.add(box, gbc);

    }

    /**
     * Layout the components of this DataItem.
     */
    private void layoutComponents() {

        // clear the panel
        removeAll();
        panel.removeAll();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "ColumnDataItem.8") + name + ": " + description); //$NON-NLS-1$ //$NON-NLS-2$
        }
        // set the background colour
        if (bgcolour != null) {
            panel.setBackground(bgcolour);
        }

        // set the layout manager on the panel with a margin of 2 pixels
        panel.setLayout(new GridBagLayout());
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.fill = GridBagConstraints.BOTH;

        // add each DataItem to the panel
        for (int i = 0; i < dataItems.size(); i++) {
            DataItem item = (DataItem) dataItems.elementAt(i);

            addDataItem(item, i);
        }
        add(panel);

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
        return DataItemFactory.COLUMN;
    }
}
