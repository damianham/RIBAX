/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
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
public class GridDataItemTest {

    public GridDataItemTest() {
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
     * Test of readDescription method, of class GridDataItem.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element grid = null;
        GridDataItem instance = new GridDataItem();
        instance.readDescription(grid);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class GridDataItem.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        GridDataItem instance = new GridDataItem();
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class GridDataItem.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        GridDataItem instance = new GridDataItem();
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of exportDone method, of class GridDataItem.
     */
    @Test
    public void testExportDone() {
        System.out.println("exportDone");
        JComponent c = null;
        Transferable data = null;
        int action = 0;
        GridDataItem instance = new GridDataItem();
        instance.exportDone(c, data, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of importData method, of class GridDataItem.
     */
    @Test
    public void testImportData() {
        System.out.println("importData");
        JComponent c = null;
        Transferable t = null;
        GridDataItem instance = new GridDataItem();
        boolean expResult = false;
        boolean result = instance.importData(c, t);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doImport method, of class GridDataItem.
     */
    @Test
    public void testDoImport() {
        System.out.println("doImport");
        Object data = null;
        GridDataItem instance = new GridDataItem();
        boolean expResult = false;
        boolean result = instance.doImport(data);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransferable method, of class GridDataItem.
     */
    @Test
    public void testGetTransferable() {
        System.out.println("getTransferable");
        Component component = null;
        GridDataItem instance = new GridDataItem();
        Transferable expResult = null;
        Transferable result = instance.getTransferable(component);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}