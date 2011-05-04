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
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import javax.swing.table.AbstractTableModel;

import org.jdom.*;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.table.TableLinkElement;
import utils.table.XTableModel;

import utils.xml.XMLutils;
import utils.types.Memo;

import org.ribax.common.Messages;
import org.ribax.swing.*;

/**
 * TableDataModel reads table data from an XML source (it has already
 * been parsed by the parser) and implements the AbstractTableModel
 * interface so the model can be used by a JTable.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class TableDataModel extends AbstractTableModel implements XTableModel {

    public static final long serialVersionUID = 1;
    private static final String BUNDLE_NAME = "org.ribax.swing.data.messages"; //$NON-NLS-1$
    /** a collection of TableRows that comprise the data in the model */
    private Vector<TableRow> data = new Vector<TableRow>();
    /** a collection of rows that have been changed locally */
    private Vector<TableRow> changedRows = new Vector<TableRow>();
    /** a collection of attributes for table columns */
    private Vector<TableRowColumnAttributes> columnAttributes = new Vector<TableRowColumnAttributes>();
    /** the URL of a web service that has provided the table data */
    private String url = null;
    /**
     * The preferred height of table rows -1 == no preferred height
     */
    private int maxRowHeight = -1;
    public static final String ALL = "ALL"; //$NON-NLS-1$
    public static final String ROWDATA = "ROWDATA"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(TableDataModel.class.getName());

    /**
     * Constructor to create a new TableDataModel that takes an Element tree and a url 
     * string as arguments.
     * 
     * @param root the Element tree containing the table data.
     * @param url the URL of a web service that has provided the table data.
     */
    public TableDataModel(Element root, String url) {
        if (url != null && url.length() > 0) {
            this.url = url;
        }

        // read the table data from the Element tree
        readData(root);
    }

    /**
     * Constructor to create a new TableDataModel that takes an Element tree argument.
     * 
     * @param root the Element tree containing the table data.
     */
    public TableDataModel(Element root) {
        // read the table data from the Element tree
        readData(root);
    }

    /**
     * No argument Constructor.
     */
    public TableDataModel() {
    }

    /**
     * Clear some or all of the table data depending on the value of the argument passed in.
     * 
     * @param cleartype a string indicating the type of data to clear, one of (All,ROWDATA).
     * ROWDATA clears row data and changed row data, ALL clears column name definitions as 
     * well.
     */
    public void clear(String cleartype) {
        try {
            if (ALL.equals(cleartype)) {
                // clear everything
                data.clear();
                changedRows.clear();
                columnAttributes.clear();
            } else {  // defaults to ROWDATA
                // leave the column name definitions intact
                data.clear();
                changedRows.clear();
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Log a warning message but log the url only once.
     * 
     * @param msg the warning message.
     */
    private void warn(String msg) {
        if (url != null) {
            LOG.warn(Messages.getString(BUNDLE_NAME, "TableDataModel.2") + url); //$NON-NLS-1$
            // set the url to null so it won't be logged again
            url = null;
        }
        LOG.warn(msg);
    }

    /**
     * Read the table data from an Element tree.
     * 
     * @param root the Element tree containing the table data.
     */
    private void readData(Element root) {
        Element e;

        // read the set of column names
        if ((e = root.getChild("columnNames")) != null) { //$NON-NLS-1$

            // iterate through the list of column definitions
            List<Element> children = e.getChildren();
            Iterator<Element> iterator = children.iterator();

            while (iterator.hasNext()) {
                Element col = iterator.next();
                String name, tooltip = null;

                Color colour = null;
                int width = 0;
                boolean editable = false, visible = true;
                Integer intval;
                Boolean boolval;

                // get the column name
                name = XMLutils.getElementString("name", col);	 //$NON-NLS-1$

                // get the preferred column width
                if ((intval = XMLutils.getElementInt("width", col)) != null) //$NON-NLS-1$
                {
                    width = intval.intValue();
                }

                // get the column background colour
                colour = XMLutils.getColour(col);

                // get a value which indicates whether the column is editable
                if ((boolval = XMLutils.getElementBoolean("editable", col)) != null) //$NON-NLS-1$
                {
                    editable = boolval.booleanValue();
                }

                // get a tooltip for the column
                tooltip = XMLutils.getElementString("tooltip", col); //$NON-NLS-1$

                if ((boolval = XMLutils.getElementBoolean("visible", col)) != null) //$NON-NLS-1$
                {
                    visible = boolval.booleanValue();
                }

                // create a new attributes object for the column
                TableRowColumnAttributes tca = new TableRowColumnAttributes(name, width, colour,
                        editable, tooltip, null);

                tca.visible = visible;

                // if the column has a range of values, convert it into a combo box
                // and set is as the cell editor for this column
                Element values = col.getChild("values"); //$NON-NLS-1$
                if (values != null) {

                    // iterate through the set of column values
                    List<Element> vkids = values.getChildren();
                    Iterator<Element> viter = vkids.iterator();
                    JComboBox comboBox = new JComboBox();

                    while (viter.hasNext()) {
                        Element vcol = viter.next();
                        // add the value to the combo box
                        comboBox.addItem(vcol.getText());
                    }
                    // set the editor attribute for the column
                    tca.setEditor(new DefaultCellEditor(comboBox));
                }
                // add the column attributes object to the column list
                columnAttributes.add(tca);

            }
        }

        // add the table row data
        addRowData(root, false);
    }

    /* (non-Javadoc)
     * @see utils.table.XTableModel#getMetaText(int, int)
     */
    public String[] getMetaText(int row, int col) {
        String[] tips = new String[2];
        tips[0] = getColumnTooltip(col);

        if (tips[0] == null) {
            return null;
        }

        tips[1] = getValueAt(row, col).toString();

        return tips;
    }

    /**
     * read rows of table data and add them to the data vector.  If replace is true
     * then replace rows which have a matching element in the first String column 
     * NB. the first column may be another type like a checkbox.
     * 
     * @param root the Element tree containing the table row data
     * @param replace indicates whether to replace existing rows or not
     */
    public void addRowData(Element root, boolean replace) {
        Element e;

        /*  XML data format is
         * <rowData>
         *   <row>
         *     <col>data</col><col>data</col>
         *   </row>
         * </rowData>
         */

        if ((e = root.getChild("rowData")) != null) { //$NON-NLS-1$

            // iterate through the rows
            List<Element> children = e.getChildren();
            Iterator<Element> iterator = children.iterator();
            Color colour = null;
            int line = 0;

            while (iterator.hasNext()) {
                Element row = iterator.next();
                line++;
                TableRow v = new TableRow();

                // get the row background colour
                if ((colour = XMLutils.getColour(row)) != null) {
                    // add a colour as the first element for the row
                    v.setRowAttributes(new TableRowColumnAttributes(null, 0, colour,
                            false, null, null));
                }

                // get a key which uniquely identifies the row
                String key = XMLutils.getElementString("key", row); //$NON-NLS-1$

                if (key != null) {
                    v.setKey(key);
                }

                // iterate through the columns in this row
                List<Element> cols = row.getChildren();
                Iterator<Element> colit = cols.iterator();
                int colnum = 0;

                while (colit.hasNext()) {
                    Element col = colit.next();
                    Object value = null;
                    String colData = "", colType = null; //$NON-NLS-1$

                    colour = null;

                    // get the cell data type
                    colType = XMLutils.getElementString("type", col); //$NON-NLS-1$

                    // get the cell background colour
                    colour = XMLutils.getColour(col);

                    // get the cell data value
                    colData = XMLutils.getElementString("value", col); //$NON-NLS-1$

                    // if no 'value' attribute was given then use the text of the Element
                    // as the cell data value
                    if (colData == null) {
                        colData = col.getText();
                    }

                    // replace newlines in the cell data value
                    colData = colData.replaceAll("<newline>", "\n"); //$NON-NLS-1$ //$NON-NLS-2$

                    value = colData;

                    // see if we need to convert the cell data value to another type
                    if (colType != null) {
                        if (colType.equals("boolean")) { //$NON-NLS-1$
                            // convert it to a Boolean
                            try {
                                value = new Boolean(colData);
                            } catch (Exception ex) {
                                warn(Messages.getString(BUNDLE_NAME, "TableDataModel.18") + colData + Messages.getString(BUNDLE_NAME, "TableDataModel.19") + colnum + Messages.getString(BUNDLE_NAME, "TableDataModel.20") + line + ") "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                                value = new Boolean(false);
                            }
                        } else if (colType.equals("float")) { //$NON-NLS-1$
                            // convert it to a Float
                            try {
                                value = new Float(colData);
                            } catch (Exception ex) {
                                warn(Messages.getString(BUNDLE_NAME, "TableDataModel.23") + colData + Messages.getString(BUNDLE_NAME, "TableDataModel.24") + colnum + Messages.getString(BUNDLE_NAME, "TableDataModel.25") + line + ") "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                                value = new Float(0.0);
                            }
                        } else if (colType.equals("number")) { //$NON-NLS-1$
                            // convert it to a Number
                            try {
                                value = new Integer(colData);
                            } catch (Exception ex) {
                                warn(Messages.getString(BUNDLE_NAME, "TableDataModel.28") + colData + Messages.getString(BUNDLE_NAME, "TableDataModel.29") + colnum + Messages.getString(BUNDLE_NAME, "TableDataModel.30") + line + ") "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                                value = new Integer(0);
                            }
                        } else if (colType.equals("string")) { //$NON-NLS-1$
                            // no conversion needed
                            value = colData;

                        } else if (colType.equals("memo")) { //$NON-NLS-1$
                            // create a new Memo object
                            value = new Memo(colData);
                        } else if (colType.equals("image")) { //$NON-NLS-1$

                            // create a new ImageIcon using the cell data value as the
                            // URL of the image
                            ImageIcon icon = null;

                            if (colData != null) {
                                try {
                                    URL u = new URL(colData);

                                    icon = new ImageIcon(u);

                                    // while we are here get the maximum row height from
                                    // this image
                                    maxRowHeight = Math.max(maxRowHeight, icon.getIconHeight());

                                    value = icon;
                                } catch (Exception ex) {
                                    LOG.error(Messages.getString(BUNDLE_NAME, "TableDataModel.35"), ex); //$NON-NLS-1$
                                }
                            }
                        } else if (colType.equals("link")) { //$NON-NLS-1$
                            // the cell is a hyperlink so the column definition
                            // contains embedded items that define the link url etc.
                            ImageIcon icon = null;

                            // get the Element containing the link definition
                            if ((e = col.getChild("value")) != null) { //$NON-NLS-1$
                                String linkurl = null, title = null, iconURL = null;

                                // get the URL of the link
                                linkurl = XMLutils.getElementString("linkurl", e); //$NON-NLS-1$

                                // get an optional link title to display
                                title = XMLutils.getElementString("title", e); //$NON-NLS-1$

                                // get an optional icon to display
                                iconURL = XMLutils.getElementString("iconurl", e); //$NON-NLS-1$

                                if (iconURL != null) {
                                    try {
                                        URL u = new URL(iconURL);

                                        icon = new ImageIcon(u);

                                        // while we are here get the maximum row height from
                                        // this image
                                        maxRowHeight = Math.max(maxRowHeight, icon.getIconHeight());

                                    } catch (Exception ex) {
                                        LOG.error(Messages.getString(BUNDLE_NAME, "TableDataModel.41"), ex); //$NON-NLS-1$
                                    }
                                }
                                // create a new TableLinkElement
                                value = new TableLinkElement(linkurl, title, icon);
                            } else {
                                value = null;
                            }
                        }
                    }

                    // if a background colour was specified then create a new TableRowColumnAttributes
                    // which holds the real value along with the background colour
                    if (colour != null) {
                        value = new TableRowColumnAttributes(null, 0, colour,
                                false, null, value);
                    }

                    // add this column to the row
                    if (value != null) {
                        v.add(value);
                    }

                    colnum++;
                }
                // if 'replace' is true then replace an existing row which has the same key 
                // or column 0 value with this new row otherwise just add the row
                if (replace) {
                    replaceVector(v, data);
                } else {
                    data.add(v);
                }
            }
            // tell any listeners the table data has changed
            fireTableDataChanged();
        }
    }

    /**
     * Add a TableRow to the table data.
     * 
     * @param row the TableRow to add.
     * @param replace indicates whether to replace any existing row with the same 
     * key or column 0 value with this new row
     */
    public void addTableRow(TableRow row, boolean replace) {
        if (replace) // replace any existing row
        {
            replaceVector(row, data);
        } else // just add this row to the row data
        {
            data.add(row);
        }

        // tell any listeners the table data has changed
        fireTableDataChanged();
    }

    /**
     * Add a column definition to the list of columns.
     * @param column the column definition to add.
     */
    public void addColumn(TableRowColumnAttributes column) {
        columnAttributes.add(column);
    }

    /**
     * Find the index of a given row in the row data using the key data or a specific column.
     * 
     * @param keyValue the data to search for.
     * @param offset the column offset.  If this vaue is >= 0 it specifies a column index to 
     * match with the key data.  The key data is compared against the values in the specified 
     * column rather than the row key or first column
     * @param v the collection of rows to search
     * @return the index of the matching row or -1 if not found
     */
    private int findIndex(String keyValue, int offset, Vector<TableRow> v) {

        // iterate through the collection of rows
        for (int i = 0; i < v.size(); i++) {
            TableRow old = v.elementAt(i);

            // if the offset < 0 then match on the row key, otherwise match on the cell data
            // from the column specified by the offset
            Object o = offset < 0 ? old.getKey() : old.elementAt(offset);

            String oldColumnData = ""; //$NON-NLS-1$

            // if the value to match is not a string then go to the next row
            if (o instanceof String) {
                oldColumnData = (String) o;
            } else {
                continue;
            }

            // compare the key data with the row's key or cell value
            if (keyValue.equals(oldColumnData)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Replace a TableRow in the row data with a new TableRow value.
     * 
     * @param newrow the new TableRow value.
     * @param v the collection of TableRows to modify.
     */
    private synchronized void replaceVector(TableRow newrow, Vector<TableRow> v) {

        int index = -1;
        String keyValue = ""; //$NON-NLS-1$
        int offset = 0;

        // if the row does not have a key value then use the first column that has a String value
        // as the the column for matching
        if ((keyValue = newrow.getKey()) == null) {
            // find the first element in the row which contains String data that can be matched
            for (; offset < newrow.size(); offset++) {
                Object o = newrow.elementAt(offset);

                if (o instanceof String) {
                    keyValue = (String) o;
                    break;
                } else {
                    continue;
                }
            }
        }

        // find the row which matches this new row
        index = findIndex(keyValue, offset, v);
        if (index < 0) // no such row so just add this new row
        {
            v.add(newrow);
        } else {
            // remove the old row and add the new row at the same index
            v.remove(index);
            v.insertElementAt(newrow, index);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int col) {

        // get the name of the specified column from the collection of column attributes
        if (col >= columnAttributes.size()) {
            return null;
        }

        TableRowColumnAttributes attr =
                (TableRowColumnAttributes) columnAttributes.elementAt(col);

        return attr.name;
    }

    /* (non-Javadoc)
     * @see utils.table.XTableModel#getPreferredColumnWidth(int)
     */
    public int getPreferredColumnWidth(int col) {

        // get the preferred width of the specified column from the collection of column attributes
        if (col >= columnAttributes.size()) {
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataModel.44") + col); //$NON-NLS-1$
            // return some default value
            return 50;
        }

        TableRowColumnAttributes attr =
                (TableRowColumnAttributes) columnAttributes.elementAt(col);

        return attr.width;
    }

    /* (non-Javadoc)
     * @see utils.table.XTableModel#getPreferredRowHeight()
     */
    public int getPreferredRowHeight() {
        return maxRowHeight;
    }

    /* (non-Javadoc)
     * @see utils.table.XTableModel#getColumnEditor(int)
     */
    public DefaultCellEditor getColumnEditor(int col) {

        // get the cell editor of the specified column from the collection of column attributes
        if (col >= columnAttributes.size()) {
            return null;
        }

        TableRowColumnAttributes attr =
                (TableRowColumnAttributes) columnAttributes.elementAt(col);

        if (attr.editor instanceof DefaultCellEditor) {
            return (DefaultCellEditor) attr.editor;
        }

        return null;
    }

    /**
     * Get a tooltip for the specified column.
     * @param col the column index.
     * @return the tooltip for the indicated column or null if no tooltip was specified in 
     * the column definition.
     */
    public String getColumnTooltip(int col) {

        // get the tooltip of the specified column from the collection of column attributes
        if (col >= columnAttributes.size()) {
            return null;
        }

        TableRowColumnAttributes attr =
                (TableRowColumnAttributes) columnAttributes.elementAt(col);
        return attr.tooltip;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return data.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnAttributes.size(); // visibleColumns;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {

        if (row >= data.size()) {
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataModel.45") + row + " col:" + col); //$NON-NLS-1$ //$NON-NLS-2$
            return null;
        }

        // get the indicated TableRow
        TableRow v = data.get(row);

        Object o;

        // return null if the row does not have enough columns
        if (v.size() <= col) {
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataModel.47") + row + " col:" + col); //$NON-NLS-1$ //$NON-NLS-2$
            return null;
        }

        // get the cell data for the specified column
        o = v.get(col);

        // if the data is an instance of TableRowColumnAttributes then this holds the real
        // value in the 'data' property
        if (o instanceof TableRowColumnAttributes) {
            TableRowColumnAttributes attr = (TableRowColumnAttributes) o;
            o = attr.data;
        }

        return o;
    }

    /**
     * Get the attributes for a given row.
     * 
     * @param row the table row to find the attributes for.
     * @return attributes for the row or null if no attribues were specified in the table data
     */
    private TableRowColumnAttributes getAttributes(int row) {

        if (row >= data.size()) {
            return null;
        }

        // get the specified TableRow
        TableRow v = data.get(row);

        return v.getRowAttributes();
    }

    /* (non-Javadoc)
     * @see utils.table.XTableModel#fireMouseClicked(int, int)
     */
    public void fireMouseClicked(int row, int col) {

        Object o = getValueAt(row, col);

        // if the cell data is a link then open the url
        if (o instanceof TableLinkElement) {
            TableLinkElement tle = (TableLinkElement) o;

            URLopener.openURL(tle.url, tle.title);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int c) {
        Object o = getValueAt(0, c);

        // dummy up a String object in the case where getValueAt returned null
        if (o == null) {
            o = new String();
        }

        return o.getClass();
    }
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */

    public boolean isCellEditable(int row, int col) {
        TableRowColumnAttributes attr;

        // check the column attributes first
        if (col < columnAttributes.size()) {
            attr = (TableRowColumnAttributes) columnAttributes.elementAt(col);

            if (attr != null && attr.editable == true) {
                return true;
            }
        }

        // check the attributes for the row
        if ((attr = getAttributes(row)) != null) {
            if (attr.editable == true) {
                return true;
            }
        }

        Object o = getValueAt(row, col);

        // Booleans are editable since they are rendered as a checkbox
        if (o instanceof Boolean) {
            return true;
        }

        // otherwise the cell is not editable
        return false;
    }

    /* (non-Javadoc)
     * @see utils.table.XTableModel#getBackground(int, int)
     */
    public Color getBackground(int row, int col) {

        TableRow v = data.get(row);

        // check for a cell specific colour
        if (v.size() <= col) {
            // invalid column index
            LOG.error(Messages.getString(BUNDLE_NAME, "TableDataModel.49") + row + " col:" + col); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            // get the cell data for the specified column
            Object o = v.get(col);

            // if the cell data is a TableRowColumnAttributes it may contain a colour 
            if (o instanceof TableRowColumnAttributes) {
                TableRowColumnAttributes attr = (TableRowColumnAttributes) o;

                // only return the cell attributes colour if it is not null
                if (attr != null && attr.colour != null) {
                    return attr.colour;
                }
            }
        }

        // try row colour
        TableRowColumnAttributes attr = getAttributes(row);

        // only return the row attributes colour if it is not null
        if (attr != null && attr.colour != null) {
            return attr.colour;
        }

        // finally try a colour for the column
        attr = (TableRowColumnAttributes) columnAttributes.elementAt(col);

        // only return the column attributes colour if it is not null
        if (attr != null && attr.colour != null) {
            return attr.colour;
        }

        // no background colour specified
        return null;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object value, int row, int col) {

        // get the specified row
        TableRow v = data.get(row);

        // set the new cell value in the row
        v.set(col, value);

        // tell any listeners the data has changed
        fireTableCellUpdated(row, col);

        // save the changed row in the collection of changed rows, 
        // replacing any previous version of the same row
        replaceVector(v, changedRows);
    }

    /**
     * Get the values of a TableRow as a collection of NameValuePairs.
     * 
     * @param row the row to collect the data.
     * @param paramnum an index to add to the parameter name for each value.
     * @return the collection of NameValuePairs.
     */
    private ArrayList<NameValuePair> getRowDataElements(TableRow row, int paramnum) {

        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        NameValuePair pair = null;
        String name;

        // add a parameter for the row key if it is not null
        String key = row.getKey();

        if (key != null) {
            pair = new NameValuePair("key_" + paramnum, key); //$NON-NLS-1$
            list.add(pair);
        }

        // for each column in the row
        for (int j = 0; j < columnAttributes.size(); j++) {
            TableRowColumnAttributes attr = columnAttributes.elementAt(j);

            // get the column name
            name = attr.name;

            // get the cell data
            Object o = row.elementAt(j);

            // add parameters for certain types, other types are ignored
            if (o instanceof String) {
                // make a name of columName_paramnum
                pair = new NameValuePair(name + "_" + paramnum, (String) o); //$NON-NLS-1$
                list.add(pair);
            } else if (o instanceof Boolean) {
                pair = new NameValuePair(name + "_" + paramnum, ((Boolean) o).toString()); //$NON-NLS-1$
                list.add(pair);
            } else if (o instanceof Integer) {
                pair = new NameValuePair(name + "_" + paramnum, ((Integer) o).toString()); //$NON-NLS-1$
                list.add(pair);
            } else if (o instanceof Float) {
                pair = new NameValuePair(name + "_" + paramnum, ((Float) o).toString()); //$NON-NLS-1$
                list.add(pair);
            } else if (o instanceof Memo) {
                pair = new NameValuePair(name + "_" + paramnum, ((Memo) o).toString()); //$NON-NLS-1$
                list.add(pair);
            }
        }
        return list;
    }

    /**
     * Get all the cells of all the rows in a collection as a collection of NameValuePairs.
     * 
     * @param v the collection of TableRows.
     * @return a collection of NameValuePairs.
     */
    private ArrayList<NameValuePair> getVectorRows(Vector<TableRow> v) {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        // for each row in the collection
        for (int i = 0; i < v.size(); i++) {
            // get the next row
            TableRow row = v.elementAt(i);

            // add all the elements in the row
            list.addAll(getRowDataElements(row, i));

        }
        return list;
    }

    /**
     * Get a collection of NameValuePairs for all rows that have changed.
     * 
     * @return a collection of NameValuePairs for all rows that have changed.
     */
    public ArrayList<NameValuePair> getChangedRowsElements() {

        // get the NameValuePairs for all changed rows 
        ArrayList<NameValuePair> list = getVectorRows(changedRows);

        // reset the changed row list
        changedRows = new Vector<TableRow>();

        return list;
    }

    /**
     * Get a collection of NameValuePairs for all data in the data model.
     * 
     * @return a collection of NameValuePairs for all data in the data model.
     */
    public ArrayList<NameValuePair> getAllRowsElements() {
        ArrayList<NameValuePair> list = getVectorRows(data);

        return list;
    }

    /**
     * Get a collection of NameValuePairs for a specific row.
     * 
     * @param rownum the row to collect as NameValuePairs.
     * @param paramnum the index to add to the column name to create the parameter name.
     * @return a collection of NameValuePairs for the specified row.
     */
    public ArrayList<NameValuePair> getSelectedRowElements(int rownum, int paramnum) {
        TableRow row = data.get(rownum);

        return getRowDataElements(row, paramnum);
    }
}
