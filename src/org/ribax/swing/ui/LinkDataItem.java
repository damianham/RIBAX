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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import org.jdom.Element;

import utils.types.NameValuePair;

import org.ribax.swing.*;

/**
 * A class that displays a button that when clicked opens a web page
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class LinkDataItem extends DataItemAdaptor {

    public static final long serialVersionUID = 1;

    /**
     * No argument Constructor - required
     */
    public LinkDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {

        // clear any previous GUI components
        removeAll();

        super.readDescription(di);

        // create the button with the DataItem title
        JButton button = new JButton(title);

        // set the button tooltip
        if (tooltip != null && tooltip.length() > 1) {
            button.setToolTipText(tooltip);
        }

        if (font != null) {
            button.setFont(font);
        }

        // add a listener to the button to rspond to button clicks
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                // open the URL
                URLopener.openURL(value, description);
            }
        });

        add(button);
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
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */

    public Component getComponent() {
        return this;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */

    public String getTypeName() {
        return DataItemFactory.LINK;
    }
}
