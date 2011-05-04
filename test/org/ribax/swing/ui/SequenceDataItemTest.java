/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

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
public class SequenceDataItemTest {

    public SequenceDataItemTest() {
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
     * Test of readDescription method, of class SequenceDataItem.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element node = null;
        SequenceDataItem instance = new SequenceDataItem();
        instance.readDescription(node);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class SequenceDataItem.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        SequenceDataItem instance = new SequenceDataItem();
        instance.loadData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadTab method, of class SequenceDataItem.
     */
    @Test
    public void testLoadTab() {
        System.out.println("loadTab");
        Tab tab = null;
        SequenceDataItem instance = new SequenceDataItem();
        instance.loadTab(tab);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadCurrentTab method, of class SequenceDataItem.
     */
    @Test
    public void testLoadCurrentTab() {
        System.out.println("loadCurrentTab");
        SequenceDataItem instance = new SequenceDataItem();
        instance.loadCurrentTab();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doAction method, of class SequenceDataItem.
     */
    @Test
    public void testDoAction() {
        System.out.println("doAction");
        int buttonType = 0;
        String action = "";
        SequenceDataItem instance = new SequenceDataItem();
        instance.doAction(buttonType, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of submitDataAndReadDescription method, of class SequenceDataItem.
     */
    @Test
    public void testSubmitDataAndReadDescription() {
        System.out.println("submitDataAndReadDescription");
        SequenceDataItem instance = new SequenceDataItem();
        Element expResult = null;
        Element result = instance.submitDataAndReadDescription();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}