/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
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
public class PanelDataItemTest {

    public PanelDataItemTest() {
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
     * Test of readDescription method, of class PanelDataItem.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element di = null;
        PanelDataItem instance = new PanelDataItem();
        instance.readDescription(di);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class PanelDataItem.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        PanelDataItem instance = new PanelDataItem();
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPrintable method, of class PanelDataItem.
     */
    @Test
    public void testIsPrintable() {
        System.out.println("isPrintable");
        PanelDataItem instance = new PanelDataItem();
        boolean expResult = false;
        boolean result = instance.isPrintable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of print method, of class PanelDataItem.
     */
    @Test
    public void testPrint() {
        System.out.println("print");
        PanelDataItem instance = new PanelDataItem();
        instance.print();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class PanelDataItem.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        PanelDataItem instance = new PanelDataItem();
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of importData method, of class PanelDataItem.
     */
    @Test
    public void testImportData() {
        System.out.println("importData");
        DataFlavor flavour = null;
        Object data = null;
        PanelDataItem instance = new PanelDataItem();
        boolean expResult = false;
        boolean result = instance.importData(flavour, data);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}