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
public class TabButtonListenerTest {

    public TabButtonListenerTest() {
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
     * Test of doAction method, of class TabButtonListener.
     */
    @Test
    public void testDoAction() {
        System.out.println("doAction");
        int buttonType = 0;
        String action = "";
        TabButtonListener instance = new TabButtonListenerImpl();
        instance.doAction(buttonType, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class TabButtonListenerImpl implements TabButtonListener {

        public void doAction(int buttonType, String action) {
        }
    }

}