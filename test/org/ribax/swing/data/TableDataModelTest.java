/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.swing.data;

import utils.types.NameValuePair;
import utils.table.TableLinkElement;
import org.ribax.swing.URLopener;
import javax.swing.JComboBox;
import java.io.StringReader;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import org.jdom.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 *
 * @author damian
 */
public class TableDataModelTest {

    static Element root;

    public TableDataModelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        File f = new File("./resources/tableDataModelTest.xml");

        BufferedInputStream fin = new BufferedInputStream(new FileInputStream(f));

        // create node tree from XML
        SAXBuilder builder = new SAXBuilder();

        Document doc = builder.build(fin);
        root = doc.getRootElement();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of clear method, of class TableDataModel.
     */
    @Test
    public void testClear() {
        System.out.println("clear");

        TableDataModel instance = new TableDataModel(root);

        instance.clear(null);
        assertEquals(0, instance.getRowCount());
        assertTrue(instance.getColumnCount() > 0);

        instance.clear("ALL");
        assertEquals(0, instance.getColumnCount());
    }

    /**
     * Test of getMetaText method, of class TableDataModel.
     */
    @Test
    public void testGetMetaText() {
        System.out.println("getMetaText");
        int row = 0;
        int col = 0;
        TableDataModel instance = new TableDataModel(root);

        String[] result = instance.getMetaText(row, col);

        assertNotNull(result);
        assertTrue(result.length == 2);

    }

    /**
     * Test of addRowData method, of class TableDataModel.
     *
     */
    @Test
    public void testAddRowData() throws JDOMException, IOException {
        System.out.println("addRowData");

        boolean replace = false;
        TableDataModel instance = new TableDataModel(root);

        int count = instance.getRowCount();

        SAXBuilder builder = new SAXBuilder();

        String xml = "<root><rowData><row><col type=\"string\" >Fred Bloggs</col><col type=\"float\">99.95</col></row></rowData></root>";

        Document doc = builder.build(new StringReader(xml));
        Element node = doc.getRootElement();

        instance.addRowData(node, replace);

        assertEquals(count + 1, instance.getRowCount());

        // replace the same row
        replace = true;
        instance.addRowData(node, replace);

        assertEquals(count + 1, instance.getRowCount());

        // add a duplicate row
        replace = false;
        instance.addRowData(node, replace);

        assertEquals(count + 2, instance.getRowCount());
    }

    /**
     * Test of addTableRow method, of class TableDataModel.
     */
    @Test
    public void testAddTableRow() {
        System.out.println("addTableRow");
        TableRow row = new TableRow();

        boolean replace = true;
        TableDataModel instance = new TableDataModel(root);
        int count = instance.getRowCount();

        row.add("John Doe");
        row.add(new Float("21.34"));

        instance.addTableRow(row, replace);
        assertEquals(count + 1, instance.getRowCount());

        // check the row is replaced
        instance.addTableRow(row, replace);
        assertEquals(count + 1, instance.getRowCount());
    }

    /**
     * Test of addColumn method, of class TableDataModel.
     */
    @Test
    public void testAddColumn() {
        System.out.println("addColumn");
        TableRowColumnAttributes column = new TableRowColumnAttributes(null, 0, Color.BLACK,
                false, null, null);
        TableDataModel instance = new TableDataModel(root);
        int count = instance.getColumnCount();

        instance.addColumn(column);
        assertEquals(count + 1, instance.getColumnCount());
    }

    /**
     * Test of getColumnName method, of class TableDataModel.
     */
    @Test
    public void testGetColumnName() {
        System.out.println("getColumnName");
        int col = 0;
        TableDataModel instance = new TableDataModel(root);
        String expResult = "Customer Name";
        String result = instance.getColumnName(col);
        assertEquals(expResult, result);

    }

    /**
     * Test of getPreferredColumnWidth method, of class TableDataModel.
     */
    @Test
    public void testGetPreferredColumnWidth() {
        System.out.println("getPreferredColumnWidth");
        int col = 0;
        TableDataModel instance = new TableDataModel(root);
        int expResult = 100;
        int result = instance.getPreferredColumnWidth(col);
        assertEquals(expResult, result);

    }

    /**
     * Test of getColumnEditor method, of class TableDataModel.
     */
    @Test
    public void testGetColumnEditor() {
        System.out.println("getColumnEditor");

        TableDataModel instance = new TableDataModel(root);

        TableRowColumnAttributes column = new TableRowColumnAttributes(null, 0, Color.BLACK,
                false, null, null);
        JComboBox comboBox = new JComboBox();

        // set the editor attribute for the column
        column.setEditor(new DefaultCellEditor(comboBox));

        int count = instance.getColumnCount();

        instance.addColumn(column);
        assertEquals(count + 1, instance.getColumnCount());

        DefaultCellEditor result = instance.getColumnEditor(count);
        assertEquals(comboBox, result.getComponent());

    }

    /**
     * Test of getColumnTooltip method, of class TableDataModel.
     */
    @Test
    public void testGetColumnTooltip() {
        System.out.println("getColumnTooltip");
        int col = 0;
        TableDataModel instance = new TableDataModel(root);
        String expResult = "The customers name";
        String result = instance.getColumnTooltip(col);
        assertEquals(expResult, result);

    }

    /**
     * Test of getRowCount method, of class TableDataModel.
     */
    @Test
    public void testGetRowCount() {
        System.out.println("getRowCount");
        TableDataModel instance = new TableDataModel(root);
        int expResult = 4;
        int result = instance.getRowCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getColumnCount method, of class TableDataModel.
     */
    @Test
    public void testGetColumnCount() {
        System.out.println("getColumnCount");
        TableDataModel instance = new TableDataModel(root);
        int expResult = 2;
        int result = instance.getColumnCount();
        assertEquals(expResult, result);

    }

    /**
     * Test of getValueAt method, of class TableDataModel.
     */
    @Test
    public void testGetValueAt() {
        System.out.println("getValueAt");
        int row = 0;
        int col = 0;
        TableDataModel instance = new TableDataModel(root);
        Object expResult = "Mrs Jones";
        Object result = instance.getValueAt(row, col);
        assertEquals(expResult, result);

    }

    /**
     * Test of fireMouseClicked method, of class TableDataModel.
     */
    @Test
    public void testFireMouseClicked() {
        System.out.println("fireMouseClicked");

        URLOpenerImpl opener = mock(URLOpenerImpl.class);
        URLopener.g_Opener = opener;

        TableDataModel instance = new TableDataModel(root);

        TableRow row = new TableRow();
        TableLinkElement tle = new TableLinkElement("http://localhost/test", "test", null);

        int count = instance.getRowCount();

        row.add(tle);
        row.add(new Float("21.34"));

        instance.addTableRow(row, false);
        assertEquals(count + 1, instance.getRowCount());

        instance.fireMouseClicked(count, 0);

        verify(opener).openURL(tle.url, tle.title);
    }

    /**
     * Test of getColumnClass method, of class TableDataModel.
     */
    @Test
    public void testGetColumnClass() {
        System.out.println("getColumnClass");
        int c = 1;
        TableDataModel instance = new TableDataModel(root);
        Class expResult = Float.class;
        Class result = instance.getColumnClass(c);
        assertEquals(expResult, result);

    }

    /**
     * Test of isCellEditable method, of class TableDataModel.
     */
    @Test
    public void testIsCellEditable() {
        System.out.println("isCellEditable");
        int row = 0;
        int col = 1;
        TableDataModel instance = new TableDataModel(root);
        boolean expResult = false;
        boolean result = instance.isCellEditable(row, col);
        assertEquals(expResult, result);

    }

    /**
     * Test of getBackground method, of class TableDataModel.
     */
    @Test
    public void testGetBackground() {
        System.out.println("getBackground");
        int row = 0;
        int col = 0;
        TableDataModel instance = new TableDataModel(root);
        Color expResult = Color.decode("0xcccc66");
        Color result = instance.getBackground(row, col);
        assertEquals(expResult, result);
    }

    /**
     * Test of setValueAt method, of class TableDataModel.
     */
    @Test
    public void testSetValueAt() {
        System.out.println("setValueAt");
        Object value = "Cassius Clay";
        int row = 0;
        int col = 0;
        TableDataModel instance = new TableDataModel(root);
        instance.setValueAt(value, row, col);

        assertEquals(value, instance.getValueAt(row, col));

    }

    /**
     * Test of getChangedRowsElements method, of class TableDataModel.
     */
    @Test
    public void testGetChangedRowsElements() {
        System.out.println("getChangedRowsElements");
        TableDataModel instance = new TableDataModel(root);
        Object value = "Cassius Clay";
        int row = 0;
        int col = 0;

        instance.setValueAt(value, row, col);

        ArrayList<NameValuePair> result = instance.getChangedRowsElements();
        assertNotNull(result);

        NameValuePair pair = result.get(0);
        assertEquals(value, pair.getValue());
    }

    /**
     * Test of getAllRowsElements method, of class TableDataModel.
     */
    @Test
    public void testGetAllRowsElements() {
        System.out.println("getAllRowsElements");
        TableDataModel instance = new TableDataModel(root);

        int rows = instance.getRowCount();
        int cols = instance.getColumnCount();

        ArrayList result = instance.getAllRowsElements();
        // the result contains an element for every cell
        assertEquals(rows * cols, result.size());
    }

    /**
     * Test of getSelectedRowElements method, of class TableDataModel.
     */
    @Test
    public void testGetSelectedRowElements() {
        System.out.println("getSelectedRowElements");
        int rownum = 0;
        int paramnum = 0;
        TableDataModel instance = new TableDataModel(root);
 
        ArrayList result = instance.getSelectedRowElements(rownum, paramnum);
        assertEquals(2, result.size());

    }

    public class URLOpenerImpl implements URLopener.Opener {

        public void openURL(String url, String title) {
            throw new UnsupportedOperationException("Not implemented - it should be mocked.");
        }
    }
}
