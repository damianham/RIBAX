/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
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
public class ListDataItemTest {

    public ListDataItemTest() {
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
     * Test of readDescription method, of class ListDataItem.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element di = null;
        ListDataItem instance = new ListDataItem();
        instance.readDescription(di);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSelectionMode method, of class ListDataItem.
     */
    @Test
    public void testSetSelectionMode() {
        System.out.println("setSelectionMode");
        int mode = 0;
        ListDataItem instance = new ListDataItem();
        instance.setSelectionMode(mode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setModel method, of class ListDataItem.
     */
    @Test
    public void testSetModel() {
        System.out.println("setModel");
        DefaultListModel model = null;
        ListDataItem instance = new ListDataItem();
        instance.setModel(model);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class ListDataItem.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        ListDataItem instance = new ListDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedElements method, of class ListDataItem.
     */
    @Test
    public void testGetSelectedElements() {
        System.out.println("getSelectedElements");
        ListDataItem instance = new ListDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getSelectedElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllElements method, of class ListDataItem.
     */
    @Test
    public void testGetAllElements() {
        System.out.println("getAllElements");
        ListDataItem instance = new ListDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getAllElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refresh method, of class ListDataItem.
     */
    @Test
    public void testRefresh() {
        System.out.println("refresh");
        ArrayList<NameValuePair> params = null;
        String action = "";
        ListDataItem instance = new ListDataItem();
        instance.refresh(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class ListDataItem.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        ListDataItem instance = new ListDataItem();
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class ListDataItem.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        ArrayList extraParams = null;
        String action = "";
        ListDataItem instance = new ListDataItem();
        instance.loadData(extraParams, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class ListDataItem.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        ListDataItem instance = new ListDataItem();
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNameValuePair method, of class ListDataItem.
     */
    @Test
    public void testGetNameValuePair() {
        System.out.println("getNameValuePair");
        ListDataItem instance = new ListDataItem();
        NameValuePair expResult = null;
        NameValuePair result = instance.getNameValuePair();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class ListDataItem.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        Element node = null;
        ListDataItem instance = new ListDataItem();
        instance.setData(node);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateData method, of class ListDataItem.
     */
    @Test
    public void testUpdateData() {
        System.out.println("updateData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        ListDataItem instance = new ListDataItem();
        instance.updateData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPrintable method, of class ListDataItem.
     */
    @Test
    public void testIsPrintable() {
        System.out.println("isPrintable");
        ListDataItem instance = new ListDataItem();
        boolean expResult = false;
        boolean result = instance.isPrintable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class ListDataItem.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        ListDataItem instance = new ListDataItem();
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compare method, of class ListDataItem.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        Object term = null;
        Object obj = null;
        ListDataItem instance = new ListDataItem();
        int expResult = 0;
        int result = instance.compare(term, obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}