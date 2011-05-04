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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import utils.log.BasicLogger;

import java.net.URL;

/**
 * A class that defines various button types and passes button clicks to a handler. 
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class TabButton extends JButton {

    public static final long serialVersionUID = 1;
    public static final int REFRESH = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    public static final int ADD = 4;
    public static final int NEXT = 5;
    public static final int BACK = 6;
    public static final int FINISH = 7;
    public static final int HELP = 8;
    public static final int ACTION = 9;
    public static final int RELOAD = 10;
    public static final int SELECT = 11;
    public static final int CANCEL = 12;
    public static final int SUBMIT = 13;
    public static final int PRINT = 14;
    public static final String REFRESH_TITLE = "REFRESH";
    public static final String UPDATE_TITLE = "UPDATE";
    public static final String DELETE_TITLE = "DELETE";
    public static final String ADD_TITLE = "ADD";
    public static final String NEXT_TITLE = "NEXT";
    public static final String BACK_TITLE = "BACK";
    public static final String FINISH_TITLE = "FINISH";
    public static final String HELP_TITLE = "HELP";
    public static final String ACTION_TITLE = "ACTION";
    public static final String RELOAD_TITLE = "RELOAD";
    public static final String SELECT_TITLE = "SELECT";
    public static final String CANCEL_TITLE = "CANCEL";
    public static final String SUBMIT_TITLE = "SUBMIT";
    public static final String PRINT_TITLE = "PRINT";
    /** The text on the button */
    private String title;
    /** An action string associated with the button */
    private String action = null;
    /** The button type */
    protected int buttonType;
    /** The event handler that will perform some action when the button is clicked */
    private TabButtonListener listener;
    private static BasicLogger LOG = new BasicLogger(TabButton.class.getName());

    /**
     * Constructor which takes the click handler and the button type.  The text on the
     * button defaults to the button type name.  The action defaults to a string 
     * representation of the button type.
     * 
     * @param parent the object which will handle the button clicks.
     * @param typeName the type of button.
     */
    public TabButton(TabButtonListener parent, String typeName) {
        this.listener = parent;
        this.buttonType = nametoButtonType(typeName);
        this.title = typeName;
        this.action = buttonTypeToName(buttonType);
        setValues();
    }

    /**
     * Constructor which takes the click handler, button type and text for the button.
     *  The action defaults to a string representation of the button type.
     *  
     * @param parent the object which will handle the button clicks.
     * @param typeName the type of button.
     * @param title text string that is the button label.
     */
    public TabButton(TabButtonListener parent, String typeName, String title) {
        this.listener = parent;
        this.buttonType = nametoButtonType(typeName);
        this.title = title;
        this.action = buttonTypeToName(buttonType);
        setValues();
    }

    /**
     * Set the action string associated with the button.
     * 
     * @param action the action string to set.
     */
    protected void setAction(String action) {
        this.action = action;
    }

    /**
     * Set the icon image on the button.
     * 
     * @param iconurl a URL that refers to an image for the button.
     */
    protected void setIconURL(String iconurl) {

        try {
            URL u = new URL(iconurl);

            ImageIcon im = new ImageIcon(u);
            setIcon(im);

        } catch (Exception ex) {
            LOG.error(title, ex);
        }
    }

    /**
     * Convert a button type to a text name.
     * 
     * @param buttonType the button type.
     * @return a string representing the button type or "Unknown" if the 
     * button type is not recognised.
     */
    public static String buttonTypeToName(int buttonType) {
        switch (buttonType) {
            case REFRESH:
                return REFRESH_TITLE;
            case UPDATE:
                return UPDATE_TITLE;
            case DELETE:
                return DELETE_TITLE;
            case ADD:
                return ADD_TITLE;
            case NEXT:
                return NEXT_TITLE;
            case BACK:
                return BACK_TITLE;
            case FINISH:
                return FINISH_TITLE;
            case CANCEL:
                return CANCEL_TITLE;
            case ACTION:
                return ACTION_TITLE;
            case RELOAD:
                return RELOAD_TITLE;
            case SELECT:
                return SELECT_TITLE;
            case SUBMIT:
                return SUBMIT_TITLE;
            case PRINT:
                return PRINT_TITLE;
        }
        return "Unknown";
    }

    /**
     * Convert a button type name to a button type.
     * 
     * @param name the button type name.
     * @return the button type coresponding to the name.
     */
    public static int nametoButtonType(String name) {

        if (REFRESH_TITLE.equals(name.toUpperCase())) {
            return REFRESH;
        } else if (UPDATE_TITLE.equals(name.toUpperCase())) {
            return UPDATE;
        } else if (DELETE_TITLE.equals(name.toUpperCase())) {
            return DELETE;
        } else if (ADD_TITLE.equals(name.toUpperCase())) {
            return ADD;
        } else if (NEXT_TITLE.equals(name.toUpperCase())) {
            return NEXT;
        } else if (BACK_TITLE.equals(name.toUpperCase())) {
            return BACK;
        } else if (FINISH_TITLE.equals(name.toUpperCase())) {
            return FINISH;
        } else if (HELP_TITLE.equals(name.toUpperCase())) {
            return HELP;
        } else if (ACTION_TITLE.equals(name.toUpperCase())) {
            return ACTION;
        } else if (RELOAD_TITLE.equals(name.toUpperCase())) {
            return RELOAD;
        } else if (SELECT_TITLE.equals(name.toUpperCase())) {
            return SELECT;
        } else if (CANCEL_TITLE.equals(name.toUpperCase())) {
            return CANCEL;
        } else if (SUBMIT_TITLE.equals(name.toUpperCase())) {
            return SUBMIT;
        } else if (PRINT_TITLE.equals(name.toUpperCase())) {
            return PRINT;
        } else {
            return REFRESH;
        }
    }

    /**
     * Set the text label of the button and add an action listener to respond to
     * button clicks.
     */
    private void setValues() {

        setText(title);

        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                setEnabled(false);
                // pass the event to the click handler
                listener.doAction(buttonType, action);
                setEnabled(true);
            }
        });
    }
}
