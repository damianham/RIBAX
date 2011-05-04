/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common.net;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
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
public class StringPartTest {

    public StringPartTest() {
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
     * Test of getContentLength method, of class StringPart.
     */
    @Test
    public void testGetContentLength() {
        System.out.println("getContentLength");
        StringPart instance = new StringPart("test", "test data value");
        int expResult = 135;
        int result = instance.getContentLength();

        // expected result is 135 bytes
        assertEquals(expResult, result);
    }

    /**
     * Test of writeData method, of class StringPart.
     */
    @Test
    public void testWriteData() throws Exception {
        System.out.println("writeData");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringPart instance = new StringPart("test", "test data value");
        instance.writeData(out);

        //System.out.println("size == " + out.size());
        // byte array should be 15 bytes
        assertEquals(15,out.size());
    }

}