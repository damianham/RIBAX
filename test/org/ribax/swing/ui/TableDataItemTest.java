/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

import java.awt.Component;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Hashtable;
import org.jdom.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.ribax.swing.data.TableDataModel;
import org.ribax.swing.parameters.ParameterSet;
import utils.types.NameValuePair;

/**
 *
 * @author damian
 */
public class TableDataItemTest {

    public TableDataItemTest() {
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
     * Test of setPrintOptions method, of class TableDataItem.
     */
    @Test
    public void testSetPrintOptions() {
        System.out.println("setPrintOptions");
        Hashtable<String, Object> options = null;
        TableDataItem instance = new TableDataItem();
        instance.setPrintOptions(options);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readPrintOptions method, of class TableDataItem.
     */
    @Test
    public void testReadPrintOptions() {
        System.out.println("readPrintOptions");
        Element optTree = null;
        TableDataItem instance = new TableDataItem();
        instance.readPrintOptions(optTree);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readDescription method, of class TableDataItem.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element di = null;
        TableDataItem instance = new TableDataItem();
        instance.readDescription(di);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setModel method, of class TableDataItem.
     */
    @Test
    public void testSetModel() {
        System.out.println("setModel");
        TableDataModel model = null;
        TableDataItem instance = new TableDataItem();
        instance.setModel(model);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addParameters method, of class TableDataItem.
     */
    @Test
    public void testAddParameters() {
        System.out.println("addParameters");
        ParameterSet params = null;
        TableDataItem instance = new TableDataItem();
        instance.addParameters(params);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class TableDataItem.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        TableDataItem instance = new TableDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedElements method, of class TableDataItem.
     */
    @Test
    public void testGetSelectedElements() {
        System.out.println("getSelectedElements");
        TableDataItem instance = new TableDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getSelectedElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllElements method, of class TableDataItem.
     */
    @Test
    public void testGetAllElements() {
        System.out.println("getAllElements");
        TableDataItem instance = new TableDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getAllElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class TableDataItem.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        TableDataItem instance = new TableDataItem();
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refresh method, of class TableDataItem.
     */
    @Test
    public void testRefresh() {
        System.out.println("refresh");
        ArrayList<NameValuePair> params = null;
        String action = "";
        TableDataItem instance = new TableDataItem();
        instance.refresh(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class TableDataItem.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        TableDataItem instance = new TableDataItem();
        instance.loadData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class TableDataItem.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        TableDataItem instance = new TableDataItem();
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNameValuePair method, of class TableDataItem.
     */
    @Test
    public void testGetNameValuePair() {
        System.out.println("getNameValuePair");
        TableDataItem instance = new TableDataItem();
        NameValuePair expResult = null;
        NameValuePair result = instance.getNameValuePair();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class TableDataItem.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        Element node = null;
        TableDataItem instance = new TableDataItem();
        instance.setData(node);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateData method, of class TableDataItem.
     */
    @Test
    public void testUpdateData() {
        System.out.println("updateData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        TableDataItem instance = new TableDataItem();
        instance.updateData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPrintable method, of class TableDataItem.
     */
    @Test
    public void testGetPrintable() {
        System.out.println("getPrintable");
        TableDataItem instance = new TableDataItem();
        Printable expResult = null;
        Printable result = instance.getPrintable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPrintable method, of class TableDataItem.
     */
    @Test
    public void testIsPrintable() {
        System.out.println("isPrintable");
        TableDataItem instance = new TableDataItem();
        boolean expResult = false;
        boolean result = instance.isPrintable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class TableDataItem.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        TableDataItem instance = new TableDataItem();
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}