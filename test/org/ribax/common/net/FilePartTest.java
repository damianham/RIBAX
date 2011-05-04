/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.common.net;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
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
public class FilePartTest {

    public FilePartTest() {
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
     * Test of getContentLength method, of class FilePart.
     */
    @Test
    public void testGetContentLength() {
        System.out.println("getContentLength");
        File f = new File("./resources/ribax.properties");
        FilePart instance = new FilePart("test", f);
        int expResult = 944;
        int result = instance.getContentLength();

        //System.out.println("length = " + result);
        assertEquals(expResult, result);        
    }

    /**
     * Test of writeDisposition method, of class FilePart.
     */
    @Test
    public void testWriteDisposition() throws Exception {
        System.out.println("writeDisposition");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        File f = new File("./resources/ribax.properties");
        FilePart instance = new FilePart("test", f);
        instance.writeDisposition(out);

        // byte array should be 29 bytes
        assertEquals(29,out.size());
    }

    /**
     * Test of writeData method, of class FilePart.
     */
    @Test
    public void testWriteData() throws Exception {
        System.out.println("writeData");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        File f = new File("./resources/ribax.properties");
        FilePart instance = new FilePart("test", f);
        instance.writeData(out);

        //System.out.println("size == " + out.size());
        // byte array should be 777 bytes
        assertEquals(777,out.size());
    }
}
