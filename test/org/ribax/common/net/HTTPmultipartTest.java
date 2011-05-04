/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
public class HTTPmultipartTest {

    public HTTPmultipartTest() {
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
     * Test of getBoundary method, of class HTTPmultipart.
     */
    @Test
    public void testGetBoundary() {
        System.out.println("getBoundary");
        HTTPmultipart instance = new HTTPmultipart();
        
        byte[] result = instance.getBoundary();
        assertNotNull(result);
    }

    /**
     * Test of getContentLength method, of class HTTPmultipart.
     */
    @Test
    public void testGetContentLength() {
        System.out.println("getContentLength");
        File f = new File("./resources/ribax.properties");
        FilePart fpart = new FilePart("test", f);
        HTTPmultipart instance = new HTTPmultipart();
        int expResult = 986;
        int result = instance.getContentLength(fpart);

        //System.out.println("size == " + result);
        // length should be 986 bytes
        assertEquals(expResult, result);        
    }

    /**
     * Test of writePart method, of class HTTPmultipart.
     */
    @Test
    public void testWritePart() throws Exception {
        System.out.println("writePart");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        File f = new File("./resources/ribax.properties");
        FilePart fpart = new FilePart("test", f);

        HTTPmultipart instance = new HTTPmultipart();
        instance.writePart(out, fpart);

        //System.out.println("size == " + out.size());
        // byte array should be 986 bytes
        assertEquals(986,out.size());
    }

}