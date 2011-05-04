/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.data;

import java.awt.Color;
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
public class TableRowTest {

    public TableRowTest() {
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
     * Test of getRowAttributes/setRowAttributes method, of class TableRow.
     */
    @Test
    public void testGetRowAttributes() {
        System.out.println("getRowAttributes");
        TableRow instance = new TableRow();
        TableRowColumnAttributes trca = new TableRowColumnAttributes(null, 0, Color.BLACK,
                false, null, null);

        instance.setRowAttributes(trca);
        TableRowColumnAttributes result = instance.getRowAttributes();
        assertEquals(trca, result);

    }


    /**
     * Test of getKey/setKey method, of class TableRow.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        TableRow instance = new TableRow();
        String key = "testkey";

        instance.setKey(key);

        String result = instance.getKey();
        assertEquals(key, result);

    }

    

}