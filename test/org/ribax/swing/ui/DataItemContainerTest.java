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
public class DataItemContainerTest {

    public DataItemContainerTest() {
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
     * Test of readDescription method, of class DataItemContainer.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element cdi = null;
        DataItemContainer instance = new DataItemContainerImpl();
        instance.readDescription(cdi);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of invalidateData method, of class DataItemContainer.
     */
    @Test
    public void testInvalidateData() {
        System.out.println("invalidateData");
        DataItemContainer instance = new DataItemContainerImpl();
        instance.invalidateData();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class DataItemContainer.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        DataItemContainer instance = new DataItemContainerImpl();
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class DataItemContainer.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        Element root = null;
        DataItemContainer instance = new DataItemContainerImpl();
        instance.setData(root);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNameValuePair method, of class DataItemContainer.
     */
    @Test
    public void testGetNameValuePair() {
        System.out.println("getNameValuePair");
        DataItemContainer instance = new DataItemContainerImpl();
        NameValuePair expResult = null;
        NameValuePair result = instance.getNameValuePair();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedElements method, of class DataItemContainer.
     */
    @Test
    public void testGetSelectedElements() {
        System.out.println("getSelectedElements");
        DataItemContainer instance = new DataItemContainerImpl();
        ArrayList expResult = null;
        ArrayList result = instance.getSelectedElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class DataItemContainer.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        DataItemContainer instance = new DataItemContainerImpl();
        ArrayList expResult = null;
        ArrayList result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllElements method, of class DataItemContainer.
     */
    @Test
    public void testGetAllElements() {
        System.out.println("getAllElements");
        DataItemContainer instance = new DataItemContainerImpl();
        ArrayList expResult = null;
        ArrayList result = instance.getAllElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refresh method, of class DataItemContainer.
     */
    @Test
    public void testRefresh() {
        System.out.println("refresh");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItemContainer instance = new DataItemContainerImpl();
        instance.refresh(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class DataItemContainer.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItemContainer instance = new DataItemContainerImpl();
        instance.loadData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateData method, of class DataItemContainer.
     */
    @Test
    public void testUpdateData() {
        System.out.println("updateData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItemContainer instance = new DataItemContainerImpl();
        instance.updateData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validateContents method, of class DataItemContainer.
     */
    @Test
    public void testValidateContents() {
        System.out.println("validateContents");
        DataItemContainer instance = new DataItemContainerImpl();
        boolean expResult = false;
        boolean result = instance.validateContents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class DataItemContainerImpl extends DataItemContainer {

        @Override
        public Component getComponent() {
            return this;
        }

        @Override
        public String getTypeName() {
            return DataItemFactory.PANEL;
        }
    }

}