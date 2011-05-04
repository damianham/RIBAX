/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

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
public class TabButtonTest {

    public TabButtonTest() {
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
     * Test of setAction method, of class TabButton.
     */
    @Test
    public void testSetAction() {
        System.out.println("setAction");
        String action = "";
        TabButton instance = null;
        instance.setAction(action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setIconURL method, of class TabButton.
     */
    @Test
    public void testSetIconURL() {
        System.out.println("setIconURL");
        String iconurl = "";
        TabButton instance = null;
        instance.setIconURL(iconurl);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of buttonTypeToName method, of class TabButton.
     */
    @Test
    public void testButtonTypeToName() {
        System.out.println("buttonTypeToName");
        int buttonType = 0;
        String expResult = "";
        String result = TabButton.buttonTypeToName(buttonType);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of nametoButtonType method, of class TabButton.
     */
    @Test
    public void testNametoButtonType() {
        System.out.println("nametoButtonType");
        String name = "";
        int expResult = 0;
        int result = TabButton.nametoButtonType(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}