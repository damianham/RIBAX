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
package org.ribax.swing.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.Shape;
import java.awt.FontMetrics;
import java.awt.geom.Line2D;
import java.awt.Component;

import java.util.Date;
import java.util.Vector;

import javax.swing.table.TableColumnModel;

import java.util.Hashtable;
import java.text.MessageFormat;

import javax.swing.JTable;
import javax.swing.RepaintManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.ribax.common.Messages;

import utils.log.BasicLogger;

/**
 * some of this code originally copied from this post
 * http://forum.java.sun.com/thread.jspa?threadID=625107&messageID=3572960
 * 
 * <p>
 * 
 * The <code>TablePrinter</code> is used to print a <code>JTable</code>
 * with a header, table footer and page footer.  Headers and footers are set as 
 * options after creating the TablePrinter instance.  The header is 1 or more lines
 * of text that are printed at the top of the first page (and optionally at the top of 
 * every page).  The table footer is 1 or more lines of text that are printed after all
 * the table data has been printed.  The page footer is 1 or more lines of text that are
 * printed at the foot of every page.
 * 
 * 
<p>
The following code illustrates how to use this class.
<pre>
PrinterJob job = PrinterJob.getPrinterJob();

TablePrinter tablePrinter = new TablePrinter(table,true);

// set the header using a String[]
String[] header = new String[3];
header[0] = "This is line 1";
header[1] = "This is line 2 printed on {1}";
header[2] = "This is line 3";
tablePrinter.setOption(TablePrinter.HEADER,header);

// set the table footer using a String
String tableFooter = "This is the table footer which is printed below the table\n" +
"lines will be seperated by the newline character.\n" +
"So this will be printed as 3 lines - printed on page {0}";
tablePrinter.setOption(TablePrinter.TABLE_FOOTER,tableFooter);

// set the page footer using a MessageFormat Object
MessageFormat mform = new MessageFormat("Page {0} printed on {1}.");
tablePrinter.setOption(TablePrinter.PAGE_FOOTER,mform);


// set drawing options on tablePrinter (these are the defaults)
tablePrinter.setOption(TablePrinter.SHOW_HORIZONTAL_LINES,true);
tablePrinter.setOption(TablePrinter.SHOW_VERTICAL_LINES,true);
tablePrinter.setOption(TablePrinter.SHOW_GRID,true);

job.setPrintable( tablePrinter);

PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

// add attributes
//aset.add(OrientationRequested.LANDSCAPE);
//aset.add(new Copies(2));
//aset.add(new MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4));
//aset.add(MediaName.ISO_A4_WHITE);  //aset.add(MediaName.ISO_A4_TRANSPARENT);
//aset.add(MediaSizeName.ISO_A4);
//aset.add(Sides.ONE_SIDED);



if(job.printDialog(aset)){
try{
setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
printJob.print(aset);
}catch (Exception PrintException) {
// output error message
} finally {
setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
}
}


</pre>
<p>

The header and footers can be <code>MessageFormat</code> objects,
<code>String</code> objects or <code>String[]</code> objects.  Whichever type of object they are
they are formatted using 
<p>
Object[] objs = new Object[]{new Integer(pageIndex + 1), new Date()};<br>
MessageFormat.format(line,objs); 
<p>
where line is either the <code>MessageFormat</code> object, the <code>String</code> 
object or each element of the <code>String[]</code> array.

 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class TablePrinter implements Printable {

    private static final String BUNDLE_NAME = "org.ribax.swing.print.messages"; //$NON-NLS-1$
    /** The table to print. */
    private JTable table;
    /** For quick reference to the table's header. */
    private JTableHeader columnHeader;
    /** For quick reference to the table's column model. */
    private TableColumnModel colModel;
    /** Used to store an area of the table to be printed. */
    private final Rectangle tableClip = new Rectangle(0, 0, 0, 0);
    /** Used to store an area of the table's header to be printed. */
    private final Rectangle columnHeaderClip = new Rectangle(0, 0, 0, 0);
    /** Saves the creation of multiple rectangles. */
    private final Rectangle tempRect = new Rectangle(0, 0, 0, 0);
    /** To save multiple calculations of total column width. */
    private int totalColWidth;
    /** The printing mode of this printable. */
    private JTable.PrintMode printMode;
    /** The most recent page index asked to print. */
    private int last = -1;
    /** The next row to print. */
    private int row = 0;
    /** The next column to print. */
    private int col = 0;
    /** Vertical space to leave between table and header/footer text. */
    private static final int H_F_SPACE = 8;
    /** default Font size for the header text. */
    private static final float HEADER_FONT_SIZE = 16.0f;
    /** default Font size for the footer text. */
    private static final float FOOTER_FONT_SIZE = 12.0f;
    /** used to store printing options */
    private Hashtable<String, Object> options = new Hashtable<String, Object>();
    /** whether to make a copy of the JTable */
    private boolean copyTable = false;
    // option names
    /**  identifies a <code>Boolean</code> that indicates whether the table draws horizontal lines between cells.*/
    public static final String SHOW_HORIZONTAL_LINES = "SHOW_HORIZONTAL_LINES"; //$NON-NLS-1$
    /**  identifies  a <code>Boolean</code> that indicates whether the table draws vertical lines between cells.*/
    public static final String SHOW_VERTICAL_LINES = "SHOW_VERTICAL_LINES"; //$NON-NLS-1$
    /**   identifies a <code>Boolean</code> that indicates whether the table draws grid lines around cells. */
    public static final String SHOW_GRID = "SHOW_GRID_LINES"; //$NON-NLS-1$
    /**  identifies a <code>Boolean</code> that indicates whether to draw a box around the table */
    public static final String DRAW_BORDER = "DRAW_BORDER"; //$NON-NLS-1$
    /**   identifies a <code>Boolean</code> that indicates whether to draw the table header*/
    public static final String DRAW_HEADER = "DRAW_HEADER"; //$NON-NLS-1$
    /**  identifies  a <code>Boolean</code> that indicates whether to draw a separator between the header text and the table */
    public static final String DRAW_SEPARATOR = "DRAW_SEPARATOR"; //$NON-NLS-1$
    // headers and footers are text that appear above and below the table
    // the HEADER here does not refer to the table JTableHeader
    /**  identifies a <code>Boolean</code> that indicates whether to print the header text on every page */
    public static final String HEADER_EVERY_PAGE = "HEADER_EVERY_PAGE"; //$NON-NLS-1$
    /**  identifies the header <code>String</code>, <code>String[]</code> or <code>MessageFormat</code>*/
    public static final String HEADER = "HEADER"; //$NON-NLS-1$
    /**  identifies the page footer <code>String</code>, <code>String[]</code> or <code>MessageFormat</code>
     *  that goes on every page */
    public static final String PAGE_FOOTER = "PAGEFOOTER"; //$NON-NLS-1$
    /**  identifies the table footer <code>String</code>, 
     * <code>String[]</code> or <code>MessageFormat</code> that is printed 
     * below the table data */
    public static final String TABLE_FOOTER = "TABLEFOOTER"; //$NON-NLS-1$
    /**  identifies the <code>Color</code> to use for header text */
    public static final String HEADER_COLOUR = "HEADER_COLOUR"; //$NON-NLS-1$
    /**  identifies a <code>Boolean</code> that indicates whether to center the header text */
    public static final String CENTER_HEADER = "CENTER_HEADER"; //$NON-NLS-1$
    /**  identifies the <code>Font</code> to use for header text */
    public static final String HEADER_FONT = "HEADER_FONT"; //$NON-NLS-1$
    /**  identifies the <code>Font</code> to use for the table text */
    public static final String TABLE_FONT = "TABLE_FONT"; //$NON-NLS-1$
    /**  identifies the <code>Color</code> to use for the page footer text */
    public static final String PAGEFOOTER_COLOUR = "PAGEFOOTER_COLOUR";     //$NON-NLS-1$
    /**  identifies a <code>Boolean</code> that indicates whether to center the page footer text */
    public static final String CENTER_PAGEFOOTER = "CENTER_PAGEFOOTER"; //$NON-NLS-1$
    /**  identifies the <code>Font</code> to use for the page footer text */
    public static final String PAGEFOOTER_FONT = "PAGEFOOTER_FONT"; //$NON-NLS-1$
    /**  identifies the <code>Color</code> to use for the table footer text */
    public static final String TABLEFOOTER_COLOUR = "TABLEFOOTER_COLOUR";     //$NON-NLS-1$
    /**  identifies a <code>Boolean</code> that indicates whether to center the table footer text */
    public static final String CENTER_TABLEFOOTER = "CENTER_TABLEFOOTER"; //$NON-NLS-1$
    /**  identifies the <code>Font</code> to use for the table footer text */
    public static final String TABLEFOOTER_FONT = "TABLEFOOTER_FONT"; //$NON-NLS-1$
    private final String HEADER_TEXTBOX = "HEADER_TEXTBOX"; //$NON-NLS-1$
    private final String TABLEFOOTER_TEXTBOX = "TABLEFOOTER_TEXTBOX"; //$NON-NLS-1$
    private final String PAGEFOOTER_TEXTBOX = "PAGEFOOTER_TEXTBOX"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(TablePrinter.class.getName());

    /**
     * Constructs a  <code>TablePrinter<code> initialised with table as the
     * <code>JTable</code> that will be printed
     * 
     * @param  table         the table to print
     */
    public TablePrinter(JTable table) {
        this(table, false);
    }

    /**
     * Constructs a  <code>TablePrinter<code> initialised with old_table as the
     * <code>JTable</code> that will be printed and makes a copy of the table to 
     * the originals display properties are not modified
     * 
     * @param  old_table         the table to print
     * @param  copyTable     make a copy of the table to preserve the original
     */
    public TablePrinter(JTable old_table, boolean copyTable) {

        // create a new JTable to work with so the original is not modified
        // when we modify the display properties and column widths

        this.copyTable = copyTable;

        if (copyTable) {
            this.table = new JTable(old_table.getModel());

            // set the column widths, editor and renderer to match the old table
            TableColumn oldcol, newcol;

            for (int i = 0; i < table.getModel().getColumnCount(); i++) {
                oldcol = old_table.getColumnModel().getColumn(i);
                newcol = table.getColumnModel().getColumn(i);

                newcol.setPreferredWidth(oldcol.getPreferredWidth());
                newcol.setCellEditor(oldcol.getCellEditor());
                newcol.setCellRenderer(oldcol.getCellRenderer());
            }

            // set the preferred row height
            int height = old_table.getRowHeight();
            if (height > 0) {
                table.setRowHeight(height);
            }

            JFrame frm = new JFrame();
            frm.setContentPane(new JScrollPane(table));
            frm.pack();
        } else {
            this.table = old_table;
        }

        columnHeader = table.getTableHeader();
        colModel = table.getColumnModel();
        totalColWidth = colModel.getTotalColumnWidth();

        if (columnHeader != null) {
            // the header clip height can be set once since it's unchanging
            columnHeaderClip.height = columnHeader.getHeight();
        }

        Font tableFont = table.getFont();

        // derive the header and footer font from the table's font
        Font headerFont = tableFont.deriveFont(Font.BOLD, HEADER_FONT_SIZE);
        Font footerFont = tableFont.deriveFont(Font.PLAIN, FOOTER_FONT_SIZE);

        // set the default options
        setOption(TABLE_FONT, tableFont);
        setOption(HEADER_FONT, headerFont);
        setOption(PAGEFOOTER_FONT, footerFont);
        setOption(TABLEFOOTER_FONT, footerFont);

        setOption(SHOW_HORIZONTAL_LINES, new Boolean(true));
        setOption(SHOW_VERTICAL_LINES, new Boolean(true));
        setOption(SHOW_GRID, new Boolean(true));

        setOption(HEADER_EVERY_PAGE, new Boolean(false));

        setOption(CENTER_HEADER, new Boolean(false));
        setOption(CENTER_PAGEFOOTER, new Boolean(true));
        setOption(CENTER_TABLEFOOTER, new Boolean(false));

        setOption(DRAW_BORDER, new Boolean(true));
        setOption(DRAW_HEADER, new Boolean(true));
        setOption(DRAW_SEPARATOR, new Boolean(false));

        setOption(HEADER_COLOUR, Color.BLACK);
        setOption(PAGEFOOTER_COLOUR, Color.BLACK);
        setOption(TABLEFOOTER_COLOUR, Color.BLACK);
    }

    /**
     * Set a table printing option.  The option should be one of the 
     * public static String fields that identify the option to set.  
     * The setting object type must be appropriate for the option name.
     * 
     * <code>
     * 
     * @param option  the option name to set
     * @param setting the object to set for the option
     */
    public void setOption(String option, Object setting) {
        options.put(option, setting);
    }
    private static final String SHL = "SHL"; //$NON-NLS-1$
    private static final String SVL = "SVL"; //$NON-NLS-1$
    private static final String SG = "SG"; //$NON-NLS-1$
    private static final String WIDTHS = "WIDTHS"; //$NON-NLS-1$

    // save the current lines and grid and column widths
    private void saveOptions() {

        // check to see if the options have already been saved
        if (options.get(SHL) != null) {
            return;
        }

        options.put(SHL, new Boolean(table.getShowHorizontalLines()));
        options.put(SVL, new Boolean(table.getShowVerticalLines()));
        if (table.getShowHorizontalLines() && table.getShowVerticalLines()) {
            options.put(SG, new Boolean(true));
        } else {
            options.put(SG, new Boolean(false));
        }

        // save the original column widths

        int colCount = colModel.getColumnCount();

        int[] colWidths = new int[colCount];

        for (int i = 0; i < colCount; i++) {
            TableColumn col = colModel.getColumn(i);
            colWidths[i] = col.getWidth();
        }

        options.put(WIDTHS, colWidths);
    }

    // reset the tables lines and grid and column widths
    private void resetOptions() {
        table.setShowHorizontalLines(((Boolean) options.get(SHL)).booleanValue());
        table.setShowVerticalLines(((Boolean) options.get(SVL)).booleanValue());
        table.setShowGrid(((Boolean) options.get(SG)).booleanValue());

        // restore the original column widths
        int colCount = colModel.getColumnCount();
        int[] colWidths = (int[]) options.get(WIDTHS);

        for (int i = 0; i < colCount; i++) {
            TableColumn col = colModel.getColumn(i);
            col.setWidth(colWidths[i]);
        }
    }

    // set the lines and grid on the table
    private void setPrintOptions() {
        table.setShowHorizontalLines(((Boolean) options.get(SHOW_HORIZONTAL_LINES)).booleanValue());
        table.setShowVerticalLines(((Boolean) options.get(SHOW_VERTICAL_LINES)).booleanValue());
        table.setShowGrid(((Boolean) options.get(SHOW_GRID)).booleanValue());
    }

    // get the rectangle bounds of the given text with the given font
    private Rectangle2D getTextRectangle(Graphics graphics, String[] text, Font font) {

        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics();

        // get the bounds for each line and add them together
        Rectangle2D rect = metrics.getStringBounds(text[0], graphics);

        for (int i = 1; i < text.length; i++) {
            Rectangle2D tRect = metrics.getStringBounds(text[i], graphics);
            rect.setRect(rect.getX(), rect.getY(),
                    Math.max(rect.getWidth(), tRect.getWidth()),
                    rect.getHeight() + tRect.getHeight());
        }

        return rect;
    }
    private int count = 0;
    private boolean resized = false;

    /**
     * Prints the specified page of the table into the given {@link Graphics}
     * context, in the specified format.
     *
     * @param   graphics    the context into which the page is drawn
     * @param   pageFormat  the size and orientation of the page being drawn
     * @param   pageIndex   the zero based index of the page to be drawn
     * @return  PAGE_EXISTS if the page is rendered successfully, or
     *          NO_SUCH_PAGE if a non-existent page index is specified
     * @throws  PrinterException if an error causes printing to be aborted
     */
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {

        if (copyTable == false) {
            saveOptions();
        }

        setPrintOptions();

        calculateText(graphics, pageIndex);

        // Turn off double-buffering to speed up printing.
        RepaintManager.currentManager(table).setDoubleBufferingEnabled(false);

        // keep track of how many times this method is called
        count++;

        // for easy access to these values
        final int imgWidth = (int) pageFormat.getImageableWidth();
        final int imgHeight = (int) pageFormat.getImageableHeight();
        if (imgWidth <= 0) {
            throw new PrinterException(Messages.getString(BUNDLE_NAME, "TablePrinter.27")); //$NON-NLS-1$
        }

        boolean drawSeparator = ((Boolean) options.get(DRAW_SEPARATOR)).booleanValue();

        // if we are not drawing the table column header then reset the header clip height
        if (((Boolean) options.get(DRAW_HEADER)).booleanValue() == false) {
            columnHeader = null;
            columnHeaderClip.height = 0;
        }

        // the amount of vertical space available for printing the table
        int availableSpace = imgHeight;

        TextBox t;
        // get the header TextBox dimensions
        if ((t = (TextBox) options.get(HEADER_TEXTBOX)) != null) {
            availableSpace -= t.getSpace() + H_F_SPACE;
        }

        // get the page footer TextBox dimensions
        if ((t = (TextBox) options.get(PAGEFOOTER_TEXTBOX)) != null) {
            availableSpace -= t.getSpace() + H_F_SPACE;
        }

        if (drawSeparator) {
            availableSpace -= H_F_SPACE * 2;
        }

        if (availableSpace <= 0) {
            throw new PrinterException(Messages.getString(BUNDLE_NAME, "TablePrinter.28")); //$NON-NLS-1$
        }

        // depending on the print mode, we may need a scale factor to
        // fit the table's entire width on the page      
        double sf = 1.0D;
        if (totalColWidth > imgWidth) {
            // only try resizing the columns once
            if (resized == false) {
                sf = resizeColumns(imgWidth);
            } else {
                sf = (double) imgWidth / (double) totalColWidth;
            }
        }

        Graphics2D g2d = (Graphics2D) graphics;

        // to save and store the transform
        AffineTransform oldTrans;

        // This is in a loop for two reasons:
        // First, it allows us to catch up in case we're called starting
        // with a non-zero pageIndex. Second, we know that we can be called
        // for the same page multiple times. The condition of this while
        // loop acts as a check, ensuring that we don't attempt to do the
        // calculations again when we are called subsequent times for the
        // same page.

        while (last < pageIndex) {
            if (row >= table.getRowCount() && col == 0) {
                // Turn double-buffering back on to ensure display doesn't flicker.
                RepaintManager.currentManager(table).setDoubleBufferingEnabled(true);

                // something causes this method to be called many times per page ~1,000 - must be a bug
                if (sf < 1.0D && count > (pageIndex + 1)) {
                    // print some info
                    LOG.info(Messages.getString(BUNDLE_NAME, "TablePrinter.29") + count + Messages.getString(BUNDLE_NAME, "TablePrinter.30") + pageIndex + Messages.getString(BUNDLE_NAME, "TablePrinter.31")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    LOG.info(Messages.getString(BUNDLE_NAME, "TablePrinter.32") + sf + Messages.getString(BUNDLE_NAME, "TablePrinter.33")); //$NON-NLS-1$ //$NON-NLS-2$
                }

                if (copyTable == false) {
                    resetOptions();
                }

                return NO_SUCH_PAGE;
            }
            // rather than multiplying every row and column by the scale factor
            // in findNextClip, just pass a width and height that have already
            // been divided by it

            int scaledWidth = (int) (imgWidth / sf);
            int scaledHeight = (int) ((availableSpace - columnHeaderClip.height) / sf);
            findNextClip(scaledWidth, scaledHeight);
            last++;
        }

        // translate into the co-ordinate system of the pageFormat
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // if there's footer text, print it at the bottom of the imageable area
        if ((t = (TextBox) options.get(PAGEFOOTER_TEXTBOX)) != null) {
            // save the old co-ordinates
            oldTrans = g2d.getTransform();
            // go to the bottom of the page
            g2d.translate(0, imgHeight - t.getSpace());
            // print the text
            printText(g2d, t.getText(), t.getRect(), imgWidth, t.getAttributes());
            // restore old co-ordinates
            g2d.setTransform(oldTrans);
        }

        // if there's header text, print it at the top of the imageable area
        // and then translate downwards
        if ((t = (TextBox) options.get(HEADER_TEXTBOX)) != null) {
            // print the text
            printText(g2d, t.getText(), t.getRect(), imgWidth, t.getAttributes());
            // translate y point downwards
            g2d.translate(0, t.getSpace() + H_F_SPACE);
        }

        // if we need to print a table seperator draw a line at the
        // current y point and translate downwards
        if (drawSeparator) {
            Line2D.Double ld = new Line2D.Double(0.0, 0.0, 0.0 + imgWidth, 0.0);
            g2d.setColor(Color.BLACK);
            g2d.draw(ld);
            g2d.translate(0, H_F_SPACE);
        }

        // print the table footer text before scaling

        // if this is the last page and there is a table footer then print it now
        t = (TextBox) options.get(TABLEFOOTER_TEXTBOX);
        if (row >= table.getRowCount() && t != null) {

            // determine the available space after painting the table and header
            int tableSpace = columnHeaderClip.height + tableClip.height + H_F_SPACE;

            // add the space for the seperator
            if (drawSeparator) {
                tableSpace += H_F_SPACE;
            }

            int tspace = availableSpace - tableSpace;

            if (tspace > t.getSpace()) {
                // save the old co-ordinates
                oldTrans = g2d.getTransform();
                // go to the bottom of the table
                g2d.translate(0, tableSpace);
                // print the text
                printText(g2d, t.getText(), t.getRect(), imgWidth, t.getAttributes());
                // restore old co-ordinates
                g2d.setTransform(oldTrans);

                availableSpace -= t.getSpace();
            }
        }

        // if we need to print a table seperator draw a line underneath the table
        if (drawSeparator) {
            Line2D.Double ld = new Line2D.Double(0.0, columnHeaderClip.height + tableClip.height + H_F_SPACE, 0.0 + imgWidth,
                    columnHeaderClip.height + tableClip.height + H_F_SPACE);
            g2d.setColor(Color.BLACK);
            g2d.draw(ld);
        }

        // constrain the table output to the available space
        tempRect.x = 0;
        tempRect.y = 0;
        tempRect.width = imgWidth;
        tempRect.height = availableSpace;
        g2d.clip(tempRect);

        // if we have a scale factor, scale the graphics object to fit
        // the entire width
        if (sf != 1.0D) {
            g2d.scale(sf, sf);

            // otherwise, ensure that the current portion of the table is
            // centered horizontally
        } else {
            int diff = (imgWidth - tableClip.width) / 2;
            g2d.translate(diff, 0);
        }

        // store the old transform and clip for later restoration
        oldTrans = g2d.getTransform();
        Shape oldClip = g2d.getClip();

        // if there's a table header, print the current section and
        // then translate downwards
        if (columnHeader != null) {
            columnHeaderClip.x = tableClip.x;
            columnHeaderClip.width = tableClip.width;
            g2d.translate(-columnHeaderClip.x, 0);
            g2d.clip(columnHeaderClip);
            columnHeader.print(g2d);
            // restore the original transform and clip
            g2d.setTransform(oldTrans);
            g2d.setClip(oldClip);
            // translate downwards
            g2d.translate(0, columnHeaderClip.height);
        }

        // print the current section of the table
        g2d.translate(-tableClip.x, -tableClip.y);
        g2d.clip(tableClip);
        table.print(g2d);

        // restore the original transform and clip
        g2d.setTransform(oldTrans);
        g2d.setClip(oldClip);

        // draw a box around the table
        if (((Boolean) options.get(DRAW_BORDER)).booleanValue()) {
            g2d.setColor(Color.BLACK);
            g2d.drawRect(0, 0, tableClip.width, columnHeaderClip.height + tableClip.height);
        }

        // Turn double-buffering back on to ensure display doesn't flicker.
        RepaintManager.currentManager(table).setDoubleBufferingEnabled(true);

        return PAGE_EXISTS;

    }

    /**
     * A helper method that encapsulates common code for rendering the
     * header and footer text.  Prints multiple lines of text with a defined
     * colour and font and may center the text.
     *
     * @param  g2d       the graphics to draw into
     * @param  text      the text to draw, non null
     * @param  rect      the bounding rectangle for this text,
     *                   as calculated at the given font, non null
     * @param  imgWidth  the width of the area to draw into
     * @param  attr      the printing attribute set
     */
    private void printText(Graphics2D g2d,
            String[] text,
            Rectangle2D rect,
            int imgWidth, Hashtable<String, Object> attr) {

        Color colour;
        if ((colour = (Color) attr.get("Colour")) == null) //$NON-NLS-1$
        {
            colour = Color.BLACK;
        }

        boolean centerText = false;
        Boolean b = (Boolean) attr.get("Center"); //$NON-NLS-1$
        if (b != null) {
            centerText = b.booleanValue();
        }

        Font font = (Font) attr.get("Font"); //$NON-NLS-1$

        int ty = (int) Math.ceil(Math.abs(rect.getY()));

        Rectangle2D fr;

        g2d.setColor(colour);

        if (font != null) {
            g2d.setFont(font);
        }

        for (int i = 0; i < text.length; i++) {
            int tx;

            // if the text is small enough to fit, center it
            if (centerText && (rect.getWidth() < imgWidth)) {
                tx = (int) ((imgWidth - rect.getWidth()) / 2);

                // otherwise, if the table is LTR, ensure the left side of
                // the text shows; the right can be clipped
            } else if (table.getComponentOrientation().isLeftToRight()) {
                tx = 0;

                // otherwise, ensure the right side of the text shows
            } else {
                tx = -(int) (Math.ceil(rect.getWidth()) - imgWidth);
            }

            g2d.drawString(text[i], tx, ty);
            fr = g2d.getFontMetrics().getStringBounds(text[i], g2d);
            ty += fr.getHeight();
        }
    }

    /**
     * Calculate the area of the table to be printed for
     * the next page. This should only be called if there
     * are rows and columns left to print.
     *
     * To avoid an infinite loop in printing, this will
     * always put at least one cell on each page.
     *
     * @param  pw  the width of the area to print in
     * @param  ph  the height of the area to print in
     */
    private void findNextClip(int pw, int ph) {
        final boolean ltr = table.getComponentOrientation().isLeftToRight();

        // if we're ready to start a new set of rows
        if (col == 0) {
            if (ltr) {
                // adjust clip to the left of the first column
                tableClip.x = 0;
            } else {
                // adjust clip to the right of the first column
                tableClip.x = totalColWidth;
            }

            // adjust clip to the top of the next set of rows
            tableClip.y += tableClip.height;

            // adjust clip width and height to be zero
            tableClip.width = 0;
            tableClip.height = 0;

            // fit as many rows as possible, and at least one
            int rowCount = table.getRowCount();
            int rowHeight = table.getRowHeight(row);
            do {
                tableClip.height += rowHeight;

                if (++row >= rowCount) {
                    break;
                }

                rowHeight = table.getRowHeight(row);
            } while (tableClip.height + rowHeight <= ph);
        }

        // we can short-circuit for JTable.PrintMode.FIT_WIDTH since
        // we'll always fit all columns on the page
        if (printMode == JTable.PrintMode.FIT_WIDTH) {
            tableClip.x = 0;
            tableClip.width = totalColWidth;
            return;
        }

        if (ltr) {
            // adjust clip to the left of the next set of columns
            tableClip.x += tableClip.width;
        }

        // adjust clip width to be zero
        tableClip.width = 0;

        // fit as many columns as possible, and at least one
        int colCount = table.getColumnCount();
        int colWidth = colModel.getColumn(col).getWidth();
        do {
            tableClip.width += colWidth;
            if (!ltr) {
                tableClip.x -= colWidth;
            }

            if (++col >= colCount) {
                // reset col to 0 to indicate we're finished all columns
                col = 0;

                break;
            }

            colWidth = colModel.getColumn(col).getWidth();
        } while (tableClip.width + colWidth <= pw);

    }
    private int lastPageIndex = -1;

    /**
     * Calculate the stuff we need to know for the header and footers.  The print() method
     * can be called over 1,000 times for the same page so we only do the calculations once
     * for each page index.
     * 
     * @param graphics the graphics context
     * @param pageIndex the current page number
     * @throws PrinterException
     */
    private void calculateText(Graphics graphics, int pageIndex) throws PrinterException {

        boolean printHeader = pageIndex == 0 || ((Boolean) options.get(HEADER_EVERY_PAGE)).booleanValue();

        // have we already calculated the text objects for this page
        if (pageIndex == lastPageIndex) {
            return;
        }

        lastPageIndex = pageIndex;

        // to pass the page number when formatting the header and footer text
        Object[] objs = new Object[]{new Integer(pageIndex + 1), new Date()};

        Object text;

        // start by removing any previous values
        options.remove(HEADER_TEXTBOX);
        options.remove(PAGEFOOTER_TEXTBOX);
        options.remove(TABLEFOOTER_TEXTBOX);

        // do the header
        // fetch the formatted header text, if any
        if ((text = options.get(HEADER)) != null && printHeader) {

            // get any specified font
            Font font = (Font) options.get(HEADER_FONT);

            String[] lines = new String[1];
            if (text instanceof MessageFormat) {
                lines[0] = ((MessageFormat) text).format(objs);
            } else {

                if (text instanceof String) {
                    lines = ((String) text).split("\n"); //$NON-NLS-1$
                } else if (text instanceof String[]) {
                    lines = (String[]) text;
                } else {
                    throw new PrinterException(Messages.getString(BUNDLE_NAME, "TablePrinter.38")); //$NON-NLS-1$
                }
                for (int i = 0; i < lines.length; i++) {
                    lines[i] = MessageFormat.format(lines[i], objs);
                }
            }

            TextBox tb = new TextBox(lines,
                    getTextRectangle(graphics, lines, font),
                    font,
                    (Color) options.get(HEADER_COLOUR),
                    ((Boolean) options.get(CENTER_HEADER)).booleanValue());

            setOption(HEADER_TEXTBOX, tb);
        }

        // do the page footer
        // fetch the formatted footer text, if any    
        if ((text = options.get(PAGE_FOOTER)) != null) {

            // get any specified font
            Font font = (Font) options.get(PAGEFOOTER_FONT);

            String[] lines = new String[1];
            if (text instanceof MessageFormat) {
                lines[0] = ((MessageFormat) text).format(objs);
            } else {
                if (text instanceof String) {
                    lines = ((String) text).split("\n"); //$NON-NLS-1$
                } else if (text instanceof String[]) {
                    lines = (String[]) text;
                } else {
                    throw new PrinterException(Messages.getString(BUNDLE_NAME, "TablePrinter.40")); //$NON-NLS-1$
                }
                for (int i = 0; i < lines.length; i++) {
                    lines[i] = MessageFormat.format(lines[i], objs);
                }
            }

            TextBox tb = new TextBox(lines,
                    getTextRectangle(graphics, lines, font),
                    font,
                    (Color) options.get(PAGEFOOTER_COLOUR),
                    ((Boolean) options.get(CENTER_PAGEFOOTER)).booleanValue());

            setOption(PAGEFOOTER_TEXTBOX, tb);
        }

        // do the table footer

        if ((text = options.get(TABLE_FOOTER)) != null) {

            // get any specified font
            Font font = (Font) options.get(TABLEFOOTER_FONT);

            String[] lines = new String[1];
            if (text instanceof String) {
                lines = ((String) text).split("\n"); //$NON-NLS-1$
            } else if (text instanceof String[]) {
                lines = (String[]) text;
            } else if (text instanceof MessageFormat) {
                lines[0] = ((MessageFormat) text).format(objs);
            } else {
                throw new PrinterException(Messages.getString(BUNDLE_NAME, "TablePrinter.42")); //$NON-NLS-1$
            }
            for (int i = 0; i < lines.length; i++) {
                lines[i] = MessageFormat.format(lines[i], objs);
            }

            TextBox tb = new TextBox(lines,
                    getTextRectangle(graphics, lines, font),
                    font,
                    (Color) options.get(TABLEFOOTER_COLOUR),
                    ((Boolean) options.get(CENTER_TABLEFOOTER)).booleanValue());

            setOption(TABLEFOOTER_TEXTBOX, tb);
        }
    }

    /**
     * Resize the table columns to eliminate scaling to improve performance. 
     * 
     * Resize the smallest columns first on the basis that larger columns may need to
     * be larger
     * 
     * @param imgWidth the width of the printable page
     */
    private double resizeColumns(int imgWidth) {
        resized = true;

        int colCount = colModel.getColumnCount();
        TableModel model = table.getModel();

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        Vector<Integer> indexes = new Vector<Integer>();

        // add each column index to the vector in column width order
        for (int i = 0; i < colCount; i++) {
            TableColumn col = colModel.getColumn(i);
            int width = col.getWidth();
            int j = 0;
            for (; j < indexes.size(); j++) {
                Integer colNum = indexes.get(j);
                TableColumn tcol = colModel.getColumn(colNum.intValue());
                if (tcol.getWidth() > width) {
                    indexes.insertElementAt(new Integer(i), j);
                    break;
                }
            }
            // no columns were smaller than this column so add it to the end
            if (j == indexes.size()) {
                indexes.add(new Integer(i));
            }
        }

        for (int i = 0; i < indexes.size(); i++) {
            Integer colNum = indexes.get(i);
            TableColumn tcol = colModel.getColumn(colNum.intValue());

            resizeColumn(tcol, colNum.intValue(), model);

            // check the total width to see if we can break out of this loop
            totalColWidth = colModel.getTotalColumnWidth();

            if (totalColWidth <= imgWidth) {
                // we have resized so scaling is no longer needed
                return 1.0D;
            }
        }

        return (double) imgWidth / (double) totalColWidth;
    }

    /**
     * Set the width the given column to the max of the header and each cell
     * 
     * @param col the column to resize
     */
    private void resizeColumn(TableColumn col, int colNum, TableModel model) {

        // get the header renderer 
        TableCellRenderer renderer;

        if ((renderer = col.getHeaderRenderer()) == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }

        // Initialize the width to the width of the header.
        Component comp = renderer.getTableCellRendererComponent(null, col.getHeaderValue(),
                false, false, 0, 0);

        int newWidth = comp.getPreferredSize().width;

        // get the width of each cell in this column
        for (int row = 0; row < table.getRowCount(); row++) {
            Object data = model.getValueAt(row, colNum);

            if ((renderer = col.getCellRenderer()) == null) {
                renderer = table.getDefaultRenderer(data.getClass());
            }

            comp = renderer.getTableCellRendererComponent(table, data, false, false, row, colNum);

            newWidth = Math.max(newWidth, comp.getPreferredSize().width);
        }

        // set the new width and allow for a bit of spacing
        int currWidth = col.getWidth();

        // JTable javadoc says use setPreferredWidth but that has no effect
        col.setWidth(Math.min(currWidth, newWidth + 5));
    }

    /**
     * used to store calculated information about headers and footers
     * 
     * @author damian
     *
     */
    private class TextBox {

        String[] text;
        Rectangle2D rect;
        int space;
        Hashtable<String, Object> attr = new Hashtable<String, Object>();

        TextBox(String[] text, Rectangle2D rect, Font font, Color colour, boolean center) {
            this.text = text;
            this.rect = rect;
            this.space = (int) Math.ceil(rect.getHeight());

            // set up the print attributes
            attr.put("Font", font); //$NON-NLS-1$
            attr.put("Center", center); //$NON-NLS-1$
            attr.put("Colour", colour); //$NON-NLS-1$
        }

        String[] getText() {
            return text;
        }

        Rectangle2D getRect() {
            return rect;
        }

        int getSpace() {
            return space;
        }

        Hashtable<String, Object> getAttributes() {
            return attr;
        }
    }
}
