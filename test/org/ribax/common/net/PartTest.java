/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.common.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
public class PartTest {

    public PartTest() {
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
     * Test of getName method, of class Part.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Part instance = new PartImpl("test");
        String expResult = "test";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getContentType method, of class Part.
     */
    @Test
    public void testGetContentType() {
        System.out.println("getContentType");
        Part instance = new PartImpl("test");
        String expResult = "application/octet-stream";
        String result = instance.getContentType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHeaderLength method, of class Part.
     */
    @Test
    public void testGetHeaderLength() {
        System.out.println("getHeaderLength");
        Part instance = new PartImpl("test");
        // expected size is 138 bytes
        int expResult = 138;
        int result = instance.getHeaderLength();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getCharSet method, of class Part.
     */
    @Test
    public void testGetCharSet() {
        System.out.println("getCharSet");
        Part instance = new PartImpl("test");
        String expResult = "ISO-8859-1";
        String result = instance.getCharSet();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransferEncoding method, of class Part.
     */
    @Test
    public void testGetTransferEncoding() {
        System.out.println("getTransferEncoding");
        Part instance = new PartImpl("test");
        String expResult = "binary";
        String result = instance.getTransferEncoding();
        assertEquals(expResult, result);
    }

    /**
     * Test of writeContentTypeHeader method, of class Part.
     */
    @Test
    public void testWriteContentTypeHeader() throws Exception {
        System.out.println("writeContentTypeHeader");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Part instance = new PartImpl("test");
        instance.writeContentTypeHeader(out);

        //System.out.println("size == " + out.size());
        // byte array should be 60 bytes
        assertEquals(60,out.size());
    }

    /**
     * Test of writeHeaders method, of class Part.
     */
    @Test
    public void testWriteHeaders() throws Exception {
        System.out.println("writeHeaders");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Part instance = new PartImpl("test");
        instance.writeHeaders(out);

        //System.out.println("size == " + out.size());
        // byte array should be 138 bytes
        assertEquals(138,out.size());
    }


    public class PartImpl extends Part {

        public PartImpl(String name) {
            this.name = name;
            this.contentType = "application/octet-stream";
            this.charSet = "ISO-8859-1";
            this.transferEncoding = "binary";
        }

        public int getContentLength() {
            return 0;
        }

        public void writeData(OutputStream out) throws IOException {
        }
    }
}
