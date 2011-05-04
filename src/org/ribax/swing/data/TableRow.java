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

import java.util.Vector;

/**
 * A class that represents a row in a table.  The table row can have a
 * set of attributes.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class TableRow extends Vector<Object> {

    public static final long serialVersionUID = 1;
    /** A key that uniquely identifies the table row */
    private String key = null;
    /** A set of attributes for the table row */
    private TableRowColumnAttributes rowAttributes = null;

    /**
     * No argument constructor
     */
    public TableRow() {
    }

    /**
     * Get the attributes for this TableRow.
     * 
     * @return the row attributes.
     */
    public TableRowColumnAttributes getRowAttributes() {
        return rowAttributes;
    }

    /**
     * Set the attributes for this TableRow.
     * 
     * @param rowAttributes the attributes to set.
     */
    public void setRowAttributes(TableRowColumnAttributes rowAttributes) {
        this.rowAttributes = rowAttributes;
    }

    /**
     * Get the key that uniquely identifies this table row.
     * 
     * @return the key for the table row or null if no key has been defined.
     */
    public String getKey() {
        return key;
    }

    /**
     * Set the key that uniquely identifies this table row.
     * 
     * @param key the key to set.
     */
    public void setKey(String key) {
        this.key = key;
    }
}
