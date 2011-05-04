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
public class DataItemAdaptorTest {

    public DataItemAdaptorTest() {
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
     * Test of loadData method, of class DataItemAdaptor.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItemAdaptor instance = new DataItemAdaptor();
        instance.loadData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refresh method, of class DataItemAdaptor.
     */
    @Test
    public void testRefresh() {
        System.out.println("refresh");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItemAdaptor instance = new DataItemAdaptor();
        instance.refresh(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateData method, of class DataItemAdaptor.
     */
    @Test
    public void testUpdateData() {
        System.out.println("updateData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        DataItemAdaptor instance = new DataItemAdaptor();
        instance.updateData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class DataItemAdaptor.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        Element node = null;
        DataItemAdaptor instance = new DataItemAdaptor();
        instance.setData(node);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class DataItemAdaptor.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        DataItemAdaptor instance = new DataItemAdaptor();
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class DataItemAdaptor.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        DataItemAdaptor instance = new DataItemAdaptor();
        ArrayList expResult = null;
        ArrayList result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllElements method, of class DataItemAdaptor.
     */
    @Test
    public void testGetAllElements() {
        System.out.println("getAllElements");
        DataItemAdaptor instance = new DataItemAdaptor();
        ArrayList expResult = null;
        ArrayList result = instance.getAllElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedElements method, of class DataItemAdaptor.
     */
    @Test
    public void testGetSelectedElements() {
        System.out.println("getSelectedElements");
        DataItemAdaptor instance = new DataItemAdaptor();
        ArrayList expResult = null;
        ArrayList result = instance.getSelectedElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class DataItemAdaptor.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        DataItemAdaptor instance = new DataItemAdaptor();
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class DataItemAdaptor.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        DataItemAdaptor instance = new DataItemAdaptor();
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNameValuePair method, of class DataItemAdaptor.
     */
    @Test
    public void testGetNameValuePair() {
        System.out.println("getNameValuePair");
        DataItemAdaptor instance = new DataItemAdaptor();
        NameValuePair expResult = null;
        NameValuePair result = instance.getNameValuePair();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}