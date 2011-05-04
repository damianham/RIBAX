/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common.net;

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
public class PartUtilsTest {

    public PartUtilsTest() {
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
     * Test of getAsciiBytes method, of class PartUtils.
     */
    @Test
    public void testGetAsciiBytes() {
        System.out.println("getAsciiBytes");
        String data = "testdata";
        
        byte[] result = PartUtils.getAsciiBytes(data);

        //System.out.println("result == " + result.length);
        // expected length is 8
        assertEquals(8, result.length);
        
    }

    /**
     * Test of getBytes method, of class PartUtils.
     */
    @Test
    public void testGetBytes() {
        System.out.println("getBytes");
        String data = "testdata";
        String charset = "UTF-8";

        byte[] result = PartUtils.getBytes(data, charset);

        //System.out.println("result == " + result.length);
        // expected length is 8
        assertEquals(8, result.length);
    }

    /**
     * Test of generateMultipartBoundary method, of class PartUtils.
     */
    @Test
    public void testGenerateMultipartBoundary() {
        System.out.println("generateMultipartBoundary");
       
        byte[] result = PartUtils.generateMultipartBoundary();

        String s = new String(result);

        // verify the byte array contains a 32 byte string
        assertEquals(32, s.length());
        
    }

}