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

import java.awt.Color;

/**
 * A class that holds a set of attributes for a table row, column or individual table cell.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class TableRowColumnAttributes {

    /** The table column name for the case that this attribute set is for a column */
    public String name = "";
    /** The background colour of the row, column or cell */
    public Color colour = null;
    /** The tooltip for the table column for the case that this attribute set is for a column */
    public String tooltip = null;
    /** The preferred width of the table column for the case that this attribute set is for a column */
    public int width = 0;
    /** Indicates whether the table column or row is editable */
    public boolean editable = false;
    /** Indicates whether the table column is visible */
    public boolean visible = true;
    /** Holds embedded data for the case that this attribute set is for a cell */
    public Object data = null;
    /** Holds a cell editor for a column */
    public Object editor = null;

    /**
     * Create a TableRowColumnAttributes object for a row, column or cell.
     * 
     * @param name the column name for the case that this attribute set is for a column.
     * @param width the preferred width of the table column for the case that this attribute set is for a column.
     * @param colour the background colour of the row, column or cell.
     * @param editable indicates whether the table column or row is editable.
     * @param tooltip the tooltip for the table column for the case that this attribute set is for a column.
     * @param data embedded data for the case that this attribute set is for a cell.
     */
    public TableRowColumnAttributes(String name, int width, Color colour,
            boolean editable, String tooltip, Object data) {

        this.name = name;
        this.width = width;
        this.colour = colour;
        this.tooltip = tooltip;
        this.editable = editable;
        this.data = data;
    }

    /**
     * Set the cell editor for column.
     * 
     * @param editor the cell editor for this column.
     */
    public void setEditor(Object editor) {
        this.editor = editor;
    }
}
