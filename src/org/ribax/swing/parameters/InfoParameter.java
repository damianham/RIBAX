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
package org.ribax.swing.parameters;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JSeparator;
import javax.swing.JLabel;

import javax.swing.JTextArea;

import org.jdom.Attribute;
import org.jdom.Element;

import utils.types.NameValuePair;

/**
 * A class that displays a description text area with a title and a separator
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class InfoParameter extends Parameter {

    public static final long serialVersionUID = 1;
    private int rows = 3;
    private int cols = 30;
    private Box box;
    @SuppressWarnings("unused")
    private int layoutType = BoxLayout.Y_AXIS;
    private boolean editable = false;
    private JTextArea field;

    public InfoParameter(int layoutType) {
        this.layoutType = layoutType;

        if (layoutType == BoxLayout.Y_AXIS) {
            box = Box.createVerticalBox();
        } else {
            box = Box.createHorizontalBox();
        }
    }

    public InfoParameter(int layoutType, boolean editable) {
        this.layoutType = layoutType;
        this.editable = editable;

        if (layoutType == BoxLayout.Y_AXIS) {
            box = Box.createVerticalBox();
        } else {
            box = Box.createHorizontalBox();
        }
    }

    protected void readDescription(Element param) {
        super.readDescription(param);

        value = value.replaceAll("<newline>", "\n");

        Element e;
        Attribute attr;
        String rowString, colString;
        rowString = colString = null;

        if ((e = param.getChild("rows")) != null) {
            rowString = e.getText();
        } else if ((attr = param.getAttribute("rows")) != null) {
            rowString = attr.getValue();
        }

        if ((e = param.getChild("columns")) != null) {
            colString = e.getText();
        } else if ((attr = param.getAttribute("columns")) != null) {
            colString = attr.getValue();
        }

        if (rowString != null) {
            try {
                rows = Integer.parseInt(rowString);
            } catch (NumberFormatException ex) {
            }
        }
        if (colString != null) {
            try {
                cols = Integer.parseInt(colString);
            } catch (NumberFormatException ex) {
            }
        }

        layoutComponents();
    }

    public NameValuePair getNameValuePair() {
        if (editable == false || fieldname == null || fieldname.length() == 0) {
            return null;
        } else {
            return new NameValuePair(fieldname, field.getText());
        }
    }

    private void layoutComponents() {

        field = new JTextArea(value, rows, cols);

        //setBorder(compound);
        field.setLineWrap(true);
        field.setWrapStyleWord(true);
        field.setEditable(editable);

        if (font != null) {
            field.setFont(font);
        }

        if (tooltip != null && tooltip.length() > 0) {
            field.setToolTipText(tooltip);
        }

        box.add(new JLabel(title));
        box.add(new JSeparator());
        box.add(field);
        add(box);
    }
}
