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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;

import utils.xml.XMLutils;

import javax.swing.JPanel;

/**
 * A class that lays out a set of buttons.  When a button is clicked the click 
 * handler in TabButton calls this class's doAction() method which in turn passes the event
 * to the listener's doAction method().
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 * 
 * @see org.ribax.swing.ui.TabButton
 * @see org.ribax.swing.ui.TabButtonListener
 */
public class ButtonPanel extends JPanel implements TabButtonListener {

    public static final long serialVersionUID = 1;
    // default is to layout on the south border
    private String layoutLocation = BorderLayout.SOUTH;
    /** The set of buttons in this button panel */
    private Vector<TabButton> buttons = new Vector<TabButton>();
    /** The listener that we send button click events to */
    private TabButtonListener listener = null;

    /**
     * Constructor for the button panel.
     * 
     * @param parent the component hosting this button panel that should receive button click 
     * events
     */
    public ButtonPanel(TabButtonListener parent) {
        this.listener = parent;
    }

    /**
     * Get the layout location which specifies where the button panel should be layed out
     * 
     * @return the layout location (default to BorderLayout.SOUTH)
     */
    public String getLayoutLocation() {
        return layoutLocation;
    }

    /**
     * Read the description of this button panel from the Element tree.
     *  
     * @param root the Element tree containing the button panel description
     */
    public void readButtons(Element root) {

        String location = root.getAttributeValue("location");
        if (location != null) {
            if (location.equals(BorderLayout.EAST)) {
                this.layoutLocation = BorderLayout.EAST;
            } else if (location.equals(BorderLayout.WEST)) {
                this.layoutLocation = BorderLayout.WEST;
            } else if (location.equals(BorderLayout.NORTH)) {
                this.layoutLocation = BorderLayout.NORTH;
            } else if (location.equals(BorderLayout.SOUTH)) {
                this.layoutLocation = BorderLayout.SOUTH;
            }
        }

        // iterate through the node's children, each element is a button
        List<Element> blist = root.getChildren();
        Iterator<Element> it = blist.iterator();

        while (it.hasNext()) {
            Element di = (Element) it.next();
            String di_title, di_type, di_action, di_icon;
            di_title = "";
            di_action = di_icon = null;
            di_type = "REFRESH";

            // get the basic atributes
            di_type = XMLutils.getElementString("type", di);
            di_title = XMLutils.getElementString("title", di);
            di_icon = XMLutils.getElementString("icon", di);
            di_action = XMLutils.getElementString("action", di);

            // create a new TabButton
            TabButton button = new TabButton(this, di_type, di_title);
            if (di_action != null) {
                button.setAction(di_action);
            }

            // if an image has been specified set the URL to the image
            if (di_icon != null) {
                button.setIconURL(di_icon);
            }

            // add the button to the list of buttons
            buttons.add(button);

            // add the button to the panel
            add(button);

        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TabButtonListener#doAction(int, java.lang.String)
     */
    public void doAction(int buttonType, String action) {
        // pass the event to the parent listener
        listener.doAction(buttonType, action);
    }
}
