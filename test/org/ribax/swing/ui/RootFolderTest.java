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
public class RootFolderTest {

    public RootFolderTest() {
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
     * Test of loadRootFolder method, of class RootFolder.
     */
    @Test
    public void testLoadRootFolder() {
        System.out.println("loadRootFolder");
        RootFolder instance = null;
        instance.loadRootFolder();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stop method, of class RootFolder.
     */
    @Test
    public void testStop() {
        System.out.println("stop");
        RootFolder instance = null;
        instance.stop();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doAction method, of class RootFolder.
     */
    @Test
    public void testDoAction() {
        System.out.println("doAction");
        int buttonType = 0;
        String action = "";
        RootFolder instance = null;
        instance.doAction(buttonType, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFolderForNode method, of class RootFolder.
     */
    @Test
    public void testGetFolderForNode() {
        System.out.println("getFolderForNode");
        Object value = null;
        Folder expResult = null;
        Folder result = RootFolder.getFolderForNode(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}