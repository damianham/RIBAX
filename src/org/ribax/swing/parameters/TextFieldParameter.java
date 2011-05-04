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
import javax.swing.JTextField;
import javax.swing.JLabel;

import org.jdom.Element;

import utils.types.NameValuePair;

/**
 * A Parameter that provides a text entry field
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class TextFieldParameter extends Parameter {

    public static final long serialVersionUID = 1;
    private JTextField field;
    private int fieldSize = 20;
    private Box box;
    @SuppressWarnings("unused")
    private int layoutType = BoxLayout.Y_AXIS;

    public TextFieldParameter(int layoutType) {
        this.layoutType = layoutType;

        if (layoutType == BoxLayout.Y_AXIS) {
            box = Box.createVerticalBox();
        } else {
            box = Box.createHorizontalBox();
        }
    }

    protected void readDescription(Element param) {
        super.readDescription(param);

        if (width > 0) {
            fieldSize = width;
        }

        layoutComponents();
    }

    private void layoutComponents() {
        field = new JTextField(fieldSize);
        box.add(new JLabel(title));
        box.add(field);
        if (tooltip != null && tooltip.length() > 0) {
            field.setToolTipText(tooltip);
        }

        if (value != null && value.length() > 0) {
            field.setText(value);
        }

        if (font != null) {
            field.setFont(font);
        }

        add(box);
    }

    public NameValuePair getNameValuePair() {
        String text = field.getText();

        if (text.length() > 0) {
            value = text;
        }

        return super.getNameValuePair();
    }
}
