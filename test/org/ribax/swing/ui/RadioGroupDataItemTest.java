/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

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
public class RadioGroupDataItemTest {

    public RadioGroupDataItemTest() {
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
     * Test of readDescription method, of class RadioGroupDataItem.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element grid = null;
        RadioGroupDataItem instance = new RadioGroupDataItem();
        instance.readDescription(grid);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}