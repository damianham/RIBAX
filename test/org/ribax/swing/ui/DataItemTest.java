/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.print.Printable;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JComponent;
import org.jdom.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utils.types.NameValuePair;

/**
 *
 * @author damian
 */
public class DataItemTest {

    public DataItemTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
     * Test of setParent method, of class DataItem.
     */
    @Test
    public void testSetParent() {
        System.out.println("setParent");
        DataItem parent = null;
        DataItem instance = new DataItemImpl();
        instance.setParent(parent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPreferredSize method, of class DataItem.
     */
    @Test
    public void testGetPreferredSize() {
        System.out.println("getPreferredSize");
        DataItem instance = new DataItemImpl();
        Dimension expResult = null;
        Dimension result = instance.getPreferredSize();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getElementValue method, of class DataItem.
     */
    @Test
    public void testGetElementValue() {
        System.out.println("getElementValue");
        String name = "";
        Element di = null;
        DataItem instance = new DataItemImpl();
        String expResult = "";
        String result = instance.getElementValue(name, di);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readDescription method, of class DataItem.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element di = null;
        DataItem instance = new DataItemImpl();
        instance.readDescription(di);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInputStream method, of class DataItem.
     */
    @Test
    public void testGetInputStream() throws Exception {
        System.out.println("getInputStream");
        String url = "";
        ArrayList<NameValuePair> params = null;
        DataItem instance = new DataItemImpl();
        InputStream expResult = null;
        InputStream result = instance.getInputStream(url, params);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getElementFromURL method, of class DataItem.
     */
    @Test
    public void testGetElementFromURL() {
        System.out.println("getElementFromURL");
        String url = "";
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItem instance = new DataItemImpl();
        Element expResult = null;
        Element result = instance.getElementFromURL(url, params, action);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readDescriptionFromURL method, of class DataItem.
     */
    @Test
    public void testReadDescriptionFromURL() {
        System.out.println("readDescriptionFromURL");
        String url = "";
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItem instance = new DataItemImpl();
        instance.readDescriptionFromURL(url, params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValidators method, of class DataItem.
     */
    @Test
    public void testGetValidators() {
        System.out.println("getValidators");
        Element node = null;
        DataItem instance = new DataItemImpl();
        Vector expResult = null;
        Vector result = instance.getValidators(node);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of invalidateData method, of class DataItem.
     */
    @Test
    public void testInvalidateData() {
        System.out.println("invalidateData");
        DataItem instance = new DataItemImpl();
        instance.invalidateData();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDescription method, of class DataItem.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        DataItem instance = new DataItemImpl();
        String expResult = "";
        String result = instance.getDescription();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDescription method, of class DataItem.
     */
    @Test
    public void testSetDescription() {
        System.out.println("setDescription");
        String description = "";
        DataItem instance = new DataItemImpl();
        instance.setDescription(description);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFieldname method, of class DataItem.
     */
    @Test
    public void testGetFieldname() {
        System.out.println("getFieldname");
        DataItem instance = new DataItemImpl();
        String expResult = "";
        String result = instance.getFieldname();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFieldname method, of class DataItem.
     */
    @Test
    public void testSetFieldname() {
        System.out.println("setFieldname");
        String fieldname = "";
        DataItem instance = new DataItemImpl();
        instance.setFieldname(fieldname);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTitle method, of class DataItem.
     */
    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
        DataItem instance = new DataItemImpl();
        String expResult = "";
        String result = instance.getTitle();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTitle method, of class DataItem.
     */
    @Test
    public void testSetTitle() {
        System.out.println("setTitle");
        String title = "";
        DataItem instance = new DataItemImpl();
        instance.setTitle(title);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTooltip method, of class DataItem.
     */
    @Test
    public void testGetTooltip() {
        System.out.println("getTooltip");
        DataItem instance = new DataItemImpl();
        String expResult = "";
        String result = instance.getTooltip();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTooltip method, of class DataItem.
     */
    @Test
    public void testSetTooltip() {
        System.out.println("setTooltip");
        String tooltip = "";
        DataItem instance = new DataItemImpl();
        instance.setTooltip(tooltip);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStreamSource method, of class DataItem.
     */
    @Test
    public void testGetStreamSource() {
        System.out.println("getStreamSource");
        DataItem instance = new DataItemImpl();
        String expResult = "";
        String result = instance.getStreamSource();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStreamSource method, of class DataItem.
     */
    @Test
    public void testSetStreamSource() {
        System.out.println("setStreamSource");
        String streamSource = "";
        DataItem instance = new DataItemImpl();
        instance.setStreamSource(streamSource);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class DataItem.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        DataItem instance = new DataItemImpl();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPrintable method, of class DataItem.
     */
    @Test
    public void testIsPrintable() {
        System.out.println("isPrintable");
        DataItem instance = new DataItemImpl();
        boolean expResult = false;
        boolean result = instance.isPrintable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of print method, of class DataItem.
     */
    @Test
    public void testPrint() {
        System.out.println("print");
        DataItem instance = new DataItemImpl();
        instance.print();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of errorMessage method, of class DataItem.
     */
    @Test
    public void testErrorMessage() {
        System.out.println("errorMessage");
        String message = "";
        DataItem instance = new DataItemImpl();
        instance.errorMessage(message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of infoMessage method, of class DataItem.
     */
    @Test
    public void testInfoMessage() {
        System.out.println("infoMessage");
        String message = "";
        DataItem instance = new DataItemImpl();
        instance.infoMessage(message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPrintableComponent method, of class DataItem.
     */
    @Test
    public void testGetPrintableComponent() {
        System.out.println("getPrintableComponent");
        DataItem instance = new DataItemImpl();
        Object expResult = null;
        Object result = instance.getPrintableComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPrintable method, of class DataItem.
     */
    @Test
    public void testGetPrintable() {
        System.out.println("getPrintable");
        DataItem instance = new DataItemImpl();
        Printable expResult = null;
        Printable result = instance.getPrintable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDataItemName method, of class DataItem.
     */
    @Test
    public void testGetDataItemName() {
        System.out.println("getDataItemName");
        DataItem instance = new DataItemImpl();
        String expResult = "";
        String result = instance.getDataItemName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransferable method, of class DataItem.
     */
    @Test
    public void testGetTransferable() {
        System.out.println("getTransferable");
        DataItem instance = new DataItemImpl();
        Transferable expResult = null;
        Transferable result = instance.getTransferable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransferDataFlavors method, of class DataItem.
     */
    @Test
    public void testGetTransferDataFlavors() {
        System.out.println("getTransferDataFlavors");
        DataItem instance = new DataItemImpl();
        DataFlavor[] expResult = null;
        DataFlavor[] result = instance.getTransferDataFlavors();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDataFlavorSupported method, of class DataItem.
     */
    @Test
    public void testIsDataFlavorSupported() {
        System.out.println("isDataFlavorSupported");
        DataFlavor flavor = null;
        DataItem instance = new DataItemImpl();
        boolean expResult = false;
        boolean result = instance.isDataFlavorSupported(flavor);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransferData method, of class DataItem.
     */
    @Test
    public void testGetTransferData() {
        System.out.println("getTransferData");
        DataFlavor flavor = null;
        DataItem instance = new DataItemImpl();
        Object expResult = null;
        Object result = instance.getTransferData(flavor);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of importData method, of class DataItem.
     */
    @Test
    public void testImportData() {
        System.out.println("importData");
        JComponent c = null;
        Transferable t = null;
        DataItem instance = new DataItemImpl();
        boolean expResult = false;
        boolean result = instance.importData(c, t);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of exportDone method, of class DataItem.
     */
    @Test
    public void testExportDone() {
        System.out.println("exportDone");
        JComponent c = null;
        Transferable data = null;
        int action = 0;
        DataItem instance = new DataItemImpl();
        instance.exportDone(c, data, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPath method, of class DataItem.
     */
    @Test
    public void testGetPath() {
        System.out.println("getPath");
        DataItem instance = new DataItemImpl();
        String expResult = "";
        String result = instance.getPath();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validateContents method, of class DataItem.
     */
    @Test
    public void testValidateContents() {
        System.out.println("validateContents");
        DataItem instance = new DataItemImpl();
        boolean expResult = false;
        boolean result = instance.validateContents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class DataItem.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItem instance = new DataItemImpl();
        instance.loadData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refresh method, of class DataItem.
     */
    @Test
    public void testRefresh() {
        System.out.println("refresh");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItem instance = new DataItemImpl();
        instance.refresh(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateData method, of class DataItem.
     */
    @Test
    public void testUpdateData() {
        System.out.println("updateData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItem instance = new DataItemImpl();
        instance.updateData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class DataItem.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        Element node = null;
        DataItem instance = new DataItemImpl();
        instance.setData(node);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class DataItem.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        DataItem instance = new DataItemImpl();
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class DataItem.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        DataItem instance = new DataItemImpl();
        ArrayList expResult = null;
        ArrayList result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllElements method, of class DataItem.
     */
    @Test
    public void testGetAllElements() {
        System.out.println("getAllElements");
        DataItem instance = new DataItemImpl();
        ArrayList expResult = null;
        ArrayList result = instance.getAllElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedElements method, of class DataItem.
     */
    @Test
    public void testGetSelectedElements() {
        System.out.println("getSelectedElements");
        DataItem instance = new DataItemImpl();
        ArrayList expResult = null;
        ArrayList result = instance.getSelectedElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class DataItem.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        DataItem instance = new DataItemImpl();
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class DataItem.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        DataItem instance = new DataItemImpl();
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNameValuePair method, of class DataItem.
     */
    @Test
    public void testGetNameValuePair() {
        System.out.println("getNameValuePair");
        DataItem instance = new DataItemImpl();
        NameValuePair expResult = null;
        NameValuePair result = instance.getNameValuePair();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class DataItemImpl extends DataItem {

        public void loadData(ArrayList<NameValuePair> params, String action) {
        }

        public void refresh(ArrayList<NameValuePair> params, String action) {
        }

        public void updateData(ArrayList<NameValuePair> params, String action) {
        }

        public void setData(Element node) {
        }

        public Component getComponent() {
            return null;
        }

        public ArrayList<NameValuePair> getParameters() {
            return null;
        }

        public ArrayList<NameValuePair> getAllElements() {
            return null;
        }

        public ArrayList<NameValuePair> getSelectedElements() {
            return null;
        }

        public String getTypeName() {
            return "";
        }

        public void close() {
        }

        public NameValuePair getNameValuePair() {
            return null;
        }
    }

}