/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import javax.swing.JToolTip;
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
public class DataSetDataItemTest {

    public DataSetDataItemTest() {
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
     * Test of addDataElement method, of class DataSetDataItem.
     */
    @Test
    public void testAddDataElement() {
        System.out.println("addDataElement");
        String tag = "";
        String value = "";
        int visible = 0;
        DataSetDataItem instance = new DataSetDataItem();
        instance.addDataElement(tag, value, visible);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readDescription method, of class DataSetDataItem.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element node = null;
        DataSetDataItem instance = new DataSetDataItem();
        instance.readDescription(node);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getButtonPanel method, of class DataSetDataItem.
     */
    @Test
    public void testGetButtonPanel() {
        System.out.println("getButtonPanel");
        DataSetDataItem instance = new DataSetDataItem();
        Component expResult = null;
        Component result = instance.getButtonPanel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class DataSetDataItem.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        Element node = null;
        DataSetDataItem instance = new DataSetDataItem();
        instance.setData(node);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class DataSetDataItem.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        DataSetDataItem instance = new DataSetDataItem();
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNameValuePair method, of class DataSetDataItem.
     */
    @Test
    public void testGetNameValuePair() {
        System.out.println("getNameValuePair");
        DataSetDataItem instance = new DataSetDataItem();
        NameValuePair expResult = null;
        NameValuePair result = instance.getNameValuePair();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class DataSetDataItem.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        DataSetDataItem instance = new DataSetDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllElements method, of class DataSetDataItem.
     */
    @Test
    public void testGetAllElements() {
        System.out.println("getAllElements");
        DataSetDataItem instance = new DataSetDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getAllElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedElements method, of class DataSetDataItem.
     */
    @Test
    public void testGetSelectedElements() {
        System.out.println("getSelectedElements");
        DataSetDataItem instance = new DataSetDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getSelectedElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateData method, of class DataSetDataItem.
     */
    @Test
    public void testUpdateData() {
        System.out.println("updateData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataSetDataItem instance = new DataSetDataItem();
        instance.updateData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class DataSetDataItem.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataSetDataItem instance = new DataSetDataItem();
        instance.loadData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refresh method, of class DataSetDataItem.
     */
    @Test
    public void testRefresh() {
        System.out.println("refresh");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataSetDataItem instance = new DataSetDataItem();
        instance.refresh(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class DataSetDataItem.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        DataSetDataItem instance = new DataSetDataItem();
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class DataSetDataItem.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        DataSetDataItem instance = new DataSetDataItem();
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransferable method, of class DataSetDataItem.
     */
    @Test
    public void testGetTransferable() {
        System.out.println("getTransferable");
        DataSetDataItem instance = new DataSetDataItem();
        Transferable expResult = null;
        Transferable result = instance.getTransferable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransferDataFlavors method, of class DataSetDataItem.
     */
    @Test
    public void testGetTransferDataFlavors() {
        System.out.println("getTransferDataFlavors");
        DataSetDataItem instance = new DataSetDataItem();
        DataFlavor[] expResult = null;
        DataFlavor[] result = instance.getTransferDataFlavors();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDataFlavorSupported method, of class DataSetDataItem.
     */
    @Test
    public void testIsDataFlavorSupported() {
        System.out.println("isDataFlavorSupported");
        DataFlavor df = null;
        DataSetDataItem instance = new DataSetDataItem();
        boolean expResult = false;
        boolean result = instance.isDataFlavorSupported(df);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransferData method, of class DataSetDataItem.
     */
    @Test
    public void testGetTransferData() {
        System.out.println("getTransferData");
        DataFlavor flavor = null;
        DataSetDataItem instance = new DataSetDataItem();
        Object expResult = null;
        Object result = instance.getTransferData(flavor);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createToolTip method, of class DataSetDataItem.
     */
    @Test
    public void testCreateToolTip() {
        System.out.println("createToolTip");
        DataSetDataItem instance = new DataSetDataItem();
        JToolTip expResult = null;
        JToolTip result = instance.createToolTip();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}