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
public class SelectDataItemTest {

    public SelectDataItemTest() {
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
     * Test of readDescription method, of class SelectDataItem.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element di = null;
        SelectDataItem instance = new SelectDataItem();
        instance.readDescription(di);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTooltip method, of class SelectDataItem.
     */
    @Test
    public void testSetTooltip() {
        System.out.println("setTooltip");
        String tooltip = "";
        SelectDataItem instance = new SelectDataItem();
        instance.setTooltip(tooltip);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class SelectDataItem.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        Element node = null;
        SelectDataItem instance = new SelectDataItem();
        instance.setData(node);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class SelectDataItem.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        SelectDataItem instance = new SelectDataItem();
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addMenuItem method, of class SelectDataItem.
     */
    @Test
    public void testAddMenuItem() {
        System.out.println("addMenuItem");
        String item = "";
        SelectDataItem instance = new SelectDataItem();
        instance.addMenuItem(item);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNameValuePair method, of class SelectDataItem.
     */
    @Test
    public void testGetNameValuePair() {
        System.out.println("getNameValuePair");
        SelectDataItem instance = new SelectDataItem();
        NameValuePair expResult = null;
        NameValuePair result = instance.getNameValuePair();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class SelectDataItem.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        SelectDataItem instance = new SelectDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllElements method, of class SelectDataItem.
     */
    @Test
    public void testGetAllElements() {
        System.out.println("getAllElements");
        SelectDataItem instance = new SelectDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getAllElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedElements method, of class SelectDataItem.
     */
    @Test
    public void testGetSelectedElements() {
        System.out.println("getSelectedElements");
        SelectDataItem instance = new SelectDataItem();
        ArrayList expResult = null;
        ArrayList result = instance.getSelectedElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class SelectDataItem.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        SelectDataItem instance = new SelectDataItem();
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}