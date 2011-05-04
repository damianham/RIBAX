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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.print.Printable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellEditor;

import org.jdom.Element;

import org.ribax.common.Messages;
import org.ribax.common.data.DataChangeListener;
import org.ribax.common.data.DataModel;
import org.ribax.swing.parameters.ParameterSet;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.table.XTable;
import utils.types.Memo;
import utils.table.TableTextAreaRenderer;
import utils.xml.XMLutils;

import org.ribax.swing.print.*;
import org.ribax.swing.data.*;

/**
 * A DataItem to display tabular data using an XTable.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 * 
 * @see {link utils.table.XTable}
 */
public class TableDataItem extends DataItem {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(TableDataItem.class.getName());
    /** A URL to a web service that provides the data for the Table */
    private String url = null;
    /** The main panel containing the GUI components */
    private Box box = Box.createVerticalBox();
    /** The table */
    protected XTable table;
    /** The Table Data Model */
    private TableDataModel data;
    /** A parameter set associated with this Table Data Item */
    private ParameterSet paramSet;
    /** A set of options for printing the table */
    private Hashtable<String, Object> printOptions = new Hashtable<String, Object>();

    /**
     * No argument Constructor - required
     */
    public TableDataItem() {
    }

    /**
     * Layout the GUI components.
     */
    private void layoutComponents() {

        // clear any previous GUI components
        removeAll();

        box.removeAll();

        // set the layout to BorderLayout
        setLayout(new BorderLayout());

        // add any parameters
        if (paramSet != null) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.0") + paramSet.getLayoutLocation()); //$NON-NLS-1$
            add(paramSet, paramSet.getLayoutLocation());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.3")); //$NON-NLS-1$
        }
        // embed the table in a scroll pane
        JScrollPane scrpane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        if (prefWidth > 0 || prefHeight > 0) {
            Dimension d = new Dimension(prefWidth == 0 ? 400 : prefWidth,
                    prefHeight == 0 ? 400 : prefHeight);
            scrpane.setPreferredSize(d);
        }

        // add the scrollpane to the box
        box.add(scrpane);

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.4")); //$NON-NLS-1$
        }
        // add the box to this panel
        add(box, BorderLayout.CENTER);

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.5")); //$NON-NLS-1$
        }
        validate();
    }

    /**
     * Set the options for printing the table.
     * 
     * @param options the Hashtable of print options to set.
     */
    public void setPrintOptions(Hashtable<String, Object> options) {
        printOptions = options;
    }

    /**
     * Read the print options from the Element tree.
     * 
     * @param optTree the Element containing the print options.
     */
    public void readPrintOptions(Element optTree) {
        Element opt, e;

        // get the <header> print option
        if ((opt = optTree.getChild("header")) != null) { //$NON-NLS-1$

            // get the text of the header
            if ((e = opt.getChild("text")) != null) { //$NON-NLS-1$
                String text = e.getText();
                text = text.replaceAll("<newline>", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                printOptions.put(TablePrinter.HEADER, text);
            }
            /**
             * Get the Font specification for the header.  The Font specification conforms
             * to the Java font specification.  This text is taken from teh javadoc for
             * java.awt.Font. 
             * 
             * public static Font decode(String str)

            Returns the Font that the str argument describes.
            To ensure that this method returns the desired Font,
            format the str parameter in one of these ways

             * fontname-style-pointsize
             * fontname-pointsize
             * fontname-style
             * fontname
             * fontname style pointsize
             * fontname pointsize
             * fontname style
             * fontname

            in which style is one of the four case-insensitive strings:
            "PLAIN", "BOLD", "BOLDITALIC", or "ITALIC", and pointsize is a
            positive decimal integer representation of the point size. For example,
            if you want a font that is Arial, bold, with a point size of 18, you
            would call this method with: "Arial-BOLD-18". This is equivalent to
            calling the Font constructor : new Font("Arial", Font.BOLD, 18); and
            the values are interpreted as specified by that constructor.

            @see java.awt.Font#decode(string)

             */
            if ((e = opt.getChild("font")) != null) { //$NON-NLS-1$
                Font f = Font.decode(e.getText());
                printOptions.put(TablePrinter.HEADER_FONT, f);
            }
            /** get the foreground colour for the header text.  The colour spec
             * conforms to the Java colour textual representation.
             * 
             * @see java.awt.Color#decode(String)
             * */
            if ((e = opt.getChild("colour")) != null) { //$NON-NLS-1$
                Color c = Color.decode(e.getText());
                printOptions.put(TablePrinter.HEADER_COLOUR, c);
            }
            // indicates whether the header is centered
            if ((e = opt.getChild("center")) != null) { //$NON-NLS-1$
                Boolean b = Boolean.valueOf(e.getText());
                printOptions.put(TablePrinter.CENTER_HEADER, b);
            }
        }

        // get the pageFooter which is printed on every page (as above for the header)
        if ((opt = optTree.getChild("pageFooter")) != null) {    	 //$NON-NLS-1$
            if ((e = opt.getChild("text")) != null) { //$NON-NLS-1$
                String text = e.getText();
                text = text.replaceAll("<newline>", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                printOptions.put(TablePrinter.PAGE_FOOTER, text);
            }
            if ((e = opt.getChild("font")) != null) { //$NON-NLS-1$
                Font f = Font.decode(e.getText());
                printOptions.put(TablePrinter.PAGEFOOTER_FONT, f);
            }
            if ((e = opt.getChild("colour")) != null) { //$NON-NLS-1$
                Color c = Color.decode(e.getText());
                printOptions.put(TablePrinter.PAGEFOOTER_COLOUR, c);
            }
            if ((e = opt.getChild("center")) != null) { //$NON-NLS-1$
                Boolean b = Boolean.valueOf(e.getText());
                printOptions.put(TablePrinter.CENTER_PAGEFOOTER, b);
            }
        }

        // get the tableFooter which is printed at the end of the table (as above for the header)
        if ((opt = optTree.getChild("tableFooter")) != null) {    	 //$NON-NLS-1$
            if ((e = opt.getChild("text")) != null) { //$NON-NLS-1$
                String text = e.getText();
                text = text.replaceAll("<newline>", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                printOptions.put(TablePrinter.TABLE_FOOTER, text);
            }
            if ((e = opt.getChild("font")) != null) { //$NON-NLS-1$
                Font f = Font.decode(e.getText());
                printOptions.put(TablePrinter.TABLEFOOTER_FONT, f);
            }
            if ((e = opt.getChild("colour")) != null) { //$NON-NLS-1$
                Color c = Color.decode(e.getText());
                printOptions.put(TablePrinter.TABLEFOOTER_COLOUR, c);
            }
            if ((e = opt.getChild("center")) != null) { //$NON-NLS-1$
                Boolean b = Boolean.valueOf(e.getText());
                printOptions.put(TablePrinter.CENTER_TABLEFOOTER, b);
            }
        }

        /** read the general options
         * 
         * @see java.swing.JTable
         * @see org.ribax.swing.print.TablePrinter
         */
        if ((e = optTree.getChild("drawHorizontalLines")) != null) { //$NON-NLS-1$
            Boolean b = Boolean.valueOf(e.getText());
            printOptions.put(TablePrinter.SHOW_HORIZONTAL_LINES, b);
        }
        if ((e = optTree.getChild("drawVerticalLines")) != null) { //$NON-NLS-1$
            Boolean b = Boolean.valueOf(e.getText());
            printOptions.put(TablePrinter.SHOW_VERTICAL_LINES, b);
        }
        if ((e = optTree.getChild("drawGrid")) != null) { //$NON-NLS-1$
            Boolean b = Boolean.valueOf(e.getText());
            printOptions.put(TablePrinter.SHOW_GRID, b);
        }
        if ((e = optTree.getChild("drawBorder")) != null) { //$NON-NLS-1$
            Boolean b = Boolean.valueOf(e.getText());
            printOptions.put(TablePrinter.DRAW_BORDER, b);
        }
        if ((e = optTree.getChild("drawColumnHeader")) != null) { //$NON-NLS-1$
            Boolean b = Boolean.valueOf(e.getText());
            printOptions.put(TablePrinter.DRAW_HEADER, b);
        }
        if ((e = optTree.getChild("drawSeparator")) != null) { //$NON-NLS-1$
            Boolean b = Boolean.valueOf(e.getText());
            printOptions.put(TablePrinter.DRAW_SEPARATOR, b);
        }
        if ((e = optTree.getChild("drawHeaderEveryPage")) != null) { //$NON-NLS-1$
            Boolean b = Boolean.valueOf(e.getText());
            printOptions.put(TablePrinter.HEADER_EVERY_PAGE, b);
        }

    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */

    public void readDescription(Element di) {
        Element e;

        super.readDescription(di);

        // the value attributes denotes the URL of a web service that provides the data
        // for the table
        this.url = value;

        // get the parameters
        if ((e = di.getChild("parameters")) != null) //$NON-NLS-1$
        {
            addParameters(ParameterSet.readParameters(e));
        }

        // if the table data is given here then create a TableDataModel
        // and set the model with the data
        if ((e = di.getChild("tableData")) != null) { //$NON-NLS-1$
            data = new TableDataModel(e);
        } else {
            data = new TableDataModel();
        }

        // if a data model was specified then add a listener to the model
        // to be informed of data updates
        if (model != null) {
            if (modelPath == null) {
                modelPath = getPath();
            }

            Object node = model.getElement(modelPath);

            if (node == null) {
                LOG.error(Messages.getString(BUNDLE_NAME, "TableDataItem.36") + modelPath); //$NON-NLS-1$
            }
            // set the initial table contents from the data model
            if (node != null && node instanceof Element) {
                setData((Element) node);
            }

            // add a listener to be informed of any data updates
            model.addDataChangeListener(new DataChangeListener() {

                public void dataChanged(DataModel model, String path) {

                    // check that we are interested in this data
                    if (path == null || !modelPath.equals(path)) {
                        return;
                    }

                    Object node = model.getElement(modelPath);

                    if (node == null) {
                        LOG.error(Messages.getString(BUNDLE_NAME, "TableDataItem.37") + modelPath); //$NON-NLS-1$
                    }
                    if (node != null && node instanceof Element) {
                        setData((Element) node);
                    }
                }
            });
        }

        // read the printing options
        if ((e = di.getChild("printOptions")) != null) //$NON-NLS-1$
        {
            readPrintOptions(e);
        }

        // create the table
        try {
            table = new XTable(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // get the table features, they ar not on by default and need to be
        // explicitly enabled
        String features = XMLutils.getElementString("features", di); //$NON-NLS-1$

        // check for enabled features
        // there are 4 features (striped rows, coloured rows and columns, sorting and
        // multi line tooltips)
        if (features != null) {
            features = features.toUpperCase();
            if (features.indexOf("STRIP") >= 0) { //$NON-NLS-1$
                table.enableFeature(XTable.STRIPED);
            }
            if (features.indexOf("COL") >= 0) { //$NON-NLS-1$
                table.enableFeature(XTable.COLOURED);
            }
            if (features.indexOf("MULTI") >= 0) { //$NON-NLS-1$
                table.enableFeature(XTable.MULTILINETOOLTIP);
            }
            if (features.indexOf("SORT") >= 0) { //$NON-NLS-1$
                table.enableFeature(XTable.SORTED);
            }
        }

        // set the table font
        if (font != null) {
            table.setFont(font);
        }

        // set a special renderer for a Memo class so we can display multiple line
        // text areas in cells
        table.setDefaultRenderer(Memo.class, new TableTextAreaRenderer());

        layoutComponents();
    }

    /**
     * Set the Table Data Model.
     * 
     * @param model the table data model to set.
     */
    public void setModel(TableDataModel model) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.44")); //$NON-NLS-1$
        }
        data = model;

        table.setModel(model);
    }

    /**
     * Add a set of parameters to the display and layout the GUI components again.
     * 
     * @param params the parameter set to add to the display panel.
     */
    public void addParameters(ParameterSet params) {
        this.paramSet = params;

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.45")); //$NON-NLS-1$
        }
        // clear the display
        removeAll();

        //  layout the GUI components
        layoutComponents();
    }

    /**
     * Sends the Table a signal to stop editing if a cell is currently active and being 
     * edited.  This might be the case if the user is editing a cell and then clicks a button
     * rather before pressing the Enter key or selecting another cell.
     */
    private void stopEditing() {
        int row = table.getEditingRow();
        int col = table.getEditingColumn();

        if (row > 0 && col > 0) {
            // a cell is being edited
            TableColumn column = table.getColumnModel().getColumn(col);
            TableCellEditor editor = column.getCellEditor();

            editor.stopCellEditing();
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getParameters()
     */
    public ArrayList<NameValuePair> getParameters() {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        TableModel data = table.getModel();

        // stop any editing in case the user didn't press Enter
        stopEditing();

        if (data instanceof TableDataModel) {
            // get any changed rows from the data model
            ArrayList<NameValuePair> plist = ((TableDataModel) data).getChangedRowsElements();
            if (plist != null) {
                list.addAll(plist);
            }
        }
        // add any parameters embedded in this Table DataItem
        if (paramSet != null) {
            ArrayList<NameValuePair> plist = paramSet.getNameValuePairs();
            if (plist != null) {
                list.addAll(plist);
            }
        }

        return list;
    }

    /**
     * Get an ArrayList of NameValuePairs for the data elements for a particular row.  
     * Each NameValuePair name corresponds to the name of the column for each cell and
     * the integer offset.  The value of the NameValuePair is the value of the cell.
     * 
     * @param row  the row number to retrieve.
     * @param offset a value that represents the row number in the parameters which may 
     * be different from the data row number if only selected rows are being retrieved.
     * @return the list of data elements for the indicated row.
     */
    private ArrayList<NameValuePair> getRowElements(int row, int offset) {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.46")); //$NON-NLS-1$
        }
        // get the table data model
        TableModel data = table.getModel();

        // cancel any editing in case the user didn't press Enter
        stopEditing();

        if (data instanceof TableDataModel) {
            // we can use a feature of TableDataModel
            ArrayList<NameValuePair> plist = ((TableDataModel) data).getSelectedRowElements(row, offset);
            if (plist != null) {
                list.addAll(plist);
            }
        } else {
            // a regular table model so we need to collect the data manually
            int numcols = data.getColumnCount();
            String name, value;

            // iterate through the columns
            for (int i = 0; i < numcols; i++) {
                // make the parameter name from the column name + the offset
                name = data.getColumnName(i) + "_" + offset; //$NON-NLS-1$

                value = data.getValueAt(row, i).toString();

                list.add(new NameValuePair(name, value));
            }
        }
        return list;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getSelectedElements()
     */
    public ArrayList<NameValuePair> getSelectedElements() {

        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.48")); //$NON-NLS-1$
        }
        // get the number of rows selected
        int numsel = table.getSelectedRowCount();

        if (numsel > 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.49")); //$NON-NLS-1$
            }
            // get an array of indeces of selected rows
            int[] selidx = table.getSelectedRows();

            // iterate through the indeces
            for (int i = 0; i < numsel; i++) {
                // get the data elements for the selected row
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.50")); //$NON-NLS-1$
                }
                list.addAll(getRowElements(selidx[i], i));
            }
        }
        // add any parameters embedded in this Table DataItem
        if (paramSet != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.51")); //$NON-NLS-1$
            }
            ArrayList<NameValuePair> plist = paramSet.getNameValuePairs();
            if (plist != null) {
                list.addAll(plist);
            }
        }

        return list;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getAllElements()
     */
    public ArrayList<NameValuePair> getAllElements() {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.52")); //$NON-NLS-1$
        }
        // get the data elements of all rows and columns
        for (int i = 0; i < table.getRowCount(); i++) {
            // get the data elements for the row
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.53")); //$NON-NLS-1$
            }
            list.addAll(getRowElements(i, i));
        }
        // add any parameters embedded in this Table DataItem
        if (paramSet != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.54")); //$NON-NLS-1$
            }
            ArrayList<NameValuePair> plist = paramSet.getNameValuePairs();
            if (plist != null) {
                list.addAll(plist);
            }
        }

        return list;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#close()
     */
    public void close() {

        // if we are streaming data to the table then stop the streaming
        if (loader != null) {
            loader.close();
        }
    }
    // a TableDataLoader for loading the table data and streaming data
    private TableDataLoader loader = null;

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#refresh(java.util.ArrayList, java.lang.String)
     */
    public void refresh(ArrayList<NameValuePair> params, String action) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TableDataItem.55")); //$NON-NLS-1$
        }
        // check we have a URL to load the data from
        if (streamSource != null) {
            url = streamSource;

            // if we already have a streaming TableDataLoader then tell it to stop
            if (loader != null) {
                loader.close();
            }

            // start a new streaming data loader
            loader = new TableDataLoader(this, url, params, true, this.name);

        } else if (url != null && url.length() > 0) // load the table data from the web service indicated by the URL
        {
            loader = new TableDataLoader(this, url, params, false, this.name);
        }

    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#loadData(java.util.ArrayList, java.lang.String)
     */
    public void loadData(ArrayList<NameValuePair> params, String action) {

        if (loaded) {
            return;
        }

        refresh(params, action);

        loaded = true;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public Component getComponent() {
        return table;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */
    public void setData(Element node) {
        // create a new TableDataModel with the Element tree
        data = new TableDataModel(node);

        // set the table data model
        setModel(data);
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#updateData(java.util.ArrayList, java.lang.String)
     */

    public void updateData(ArrayList<NameValuePair> params, String action) {
        refresh(params, action);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getPrintable()
     */
    public Printable getPrintable() {

        // create a new TablePrinter 
        TablePrinter tp = new TablePrinter(table, true);

        // set the print options on the TablePrinter
        for (Enumeration<String> e = printOptions.keys(); e.hasMoreElements();) {
            String name = e.nextElement();
            tp.setOption(name, printOptions.get(name));
        }

        return tp;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#isPrintable()
     */
    public boolean isPrintable() {
        return true;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */

    public String getTypeName() {
        return DataItemFactory.TABLE;
    }
}
