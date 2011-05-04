/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

import java.awt.Component;
import java.util.ArrayList;
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
public class TabTest {

    public TabTest() {
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
     * Test of readDescription method, of class Tab.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element tab = null;
        Tab instance = null;
        instance.readDescription(tab);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addDataItem method, of class Tab.
     */
    @Test
    public void testAddDataItem() {
        System.out.println("addDataItem");
        DataItem item = null;
        Tab instance = null;
        instance.addDataItem(item);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refresh method, of class Tab.
     */
    @Test
    public void testRefresh() {
        System.out.println("refresh");
        ArrayList<NameValuePair> params = null;
        String action = "";
        Tab instance = null;
        instance.refresh(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadItemData method, of class Tab.
     */
    @Test
    public void testLoadItemData() {
        System.out.println("loadItemData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        Tab instance = null;
        instance.loadItemData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateItemData method, of class Tab.
     */
    @Test
    public void testUpdateItemData() {
        System.out.println("updateItemData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        Tab instance = null;
        instance.updateItemData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selectItemData method, of class Tab.
     */
    @Test
    public void testSelectItemData() {
        System.out.println("selectItemData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        Tab instance = null;
        instance.selectItemData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of submitItemData method, of class Tab.
     */
    @Test
    public void testSubmitItemData() {
        System.out.println("submitItemData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        Tab instance = null;
        instance.submitItemData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class Tab.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        Tab instance = null;
        instance.loadData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class Tab.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        Tab instance = null;
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class Tab.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        Tab instance = null;
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}