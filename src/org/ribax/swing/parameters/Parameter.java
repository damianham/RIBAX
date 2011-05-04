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

import java.awt.Color;
import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom.Attribute;
import org.jdom.Element;

import utils.types.NameValuePair;

/**
 * A base class for different kinds of parameters
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class Parameter extends JPanel {

    public static final long serialVersionUID = 1;
    protected String title = null;
    protected String fieldname = null;
    protected String value = null;
    protected String description = null;
    protected String name = "";
    protected String colour = null;
    protected String tooltip = null;
    protected Font font = null;
    protected int width = 0;
    protected int height = 0;

    public Parameter() {
    }

    protected void readDescription(Element param) {
        Element e;
        Attribute attr;

        String wstr = null, hstr = null;

        if ((e = param.getChild("name")) != null) {
            name = e.getText();
        } else if ((attr = param.getAttribute("name")) != null) {
            name = attr.getValue();
        }

        if ((e = param.getChild("title")) != null) {
            title = e.getText();
        } else if ((attr = param.getAttribute("title")) != null) {
            title = attr.getValue();
        }

        if ((e = param.getChild("description")) != null) {
            description = e.getText();
        } else if ((attr = param.getAttribute("description")) != null) {
            description = attr.getValue();
        }

        if ((e = param.getChild("value")) != null) {
            value = e.getText();
        } else if ((attr = param.getAttribute("value")) != null) {
            value = attr.getValue();
        }

        if ((e = param.getChild("fieldname")) != null) {
            fieldname = e.getText();
        } else if ((attr = param.getAttribute("fieldname")) != null) {
            fieldname = attr.getValue();
        }

        if ((e = param.getChild("colour")) != null) {
            colour = e.getText();
        } else if ((attr = param.getAttribute("colour")) != null) {
            colour = attr.getValue();
        } else if ((e = param.getChild("bgcolour")) != null) {
            colour = e.getText();
        } else if ((attr = param.getAttribute("bgcolour")) != null) {
            colour = attr.getValue();
        }

        if ((e = param.getChild("tooltip")) != null) {
            tooltip = e.getText();
        } else if ((attr = param.getAttribute("tooltip")) != null) {
            tooltip = attr.getValue();
        }

        if ((e = param.getChild("width")) != null) {
            wstr = e.getText();
        } else if ((attr = param.getAttribute("width")) != null) {
            wstr = attr.getValue();
        }

        if ((e = param.getChild("height")) != null) {
            hstr = e.getText();
        } else if ((attr = param.getAttribute("height")) != null) {
            hstr = attr.getValue();
        }

        if ((e = param.getChild("font")) != null) {
            font = Font.decode(e.getText());
        } else if ((attr = param.getAttribute("font")) != null) {
            font = Font.decode(attr.getValue());
        }

        if (wstr != null) {
            try {
                width = Integer.parseInt(wstr);
            } catch (NumberFormatException ex) {
            }
        }
        if (hstr != null) {
            try {
                height = Integer.parseInt(hstr);
            } catch (NumberFormatException ex) {
            }
        }
        if (tooltip != null) {
            setToolTipText(tooltip);
        }

        Color col = null;

        if (colour != null) {
            try {
                col = Color.decode(colour);
                setBackground(col);
            } catch (Exception ex) {
            }
        }
    }

    public NameValuePair getNameValuePair() {
        if (fieldname == null || value == null) {
            return null;
        }

        return new NameValuePair(fieldname, value);
    }

    protected void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message,
                "RIBAX",
                JOptionPane.ERROR_MESSAGE);
    }
}
