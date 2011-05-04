/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

import java.awt.Component;
import org.jdom.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author damian
 */
public class ColumnDataItemTest {

    public ColumnDataItemTest() {
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
     * Test of readDescription method, of class ColumnDataItem.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element cdi = null;
        ColumnDataItem instance = new ColumnDataItem();
        instance.readDescription(cdi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addDataItem method, of class ColumnDataItem.
     */
    @Test
    public void testAddDataItem() {
        System.out.println("addDataItem");
        DataItem item = null;
        int num = 0;
        ColumnDataItem instance = new ColumnDataItem();
        instance.addDataItem(item, num);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class ColumnDataItem.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        ColumnDataItem instance = new ColumnDataItem();
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class ColumnDataItem.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        ColumnDataItem instance = new ColumnDataItem();
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}