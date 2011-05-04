/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common.net;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utils.types.NameValuePair;

/**
 *
 * @author damian
 */
public class NetUtilsTest {

    public NetUtilsTest() {
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
     * Test of getInputStream method, of class NetUtils.
     */
    @Test
    public void testGetInputStream() throws Exception {
        System.out.println("getInputStream");
        String url = "test://./resources/datamodelTest.xml";
        ArrayList<NameValuePair> params = null;
        String name = "";
        InputStream expResult = null;
        InputStream result = NetUtils.getInputStream(url, params, name);
        assertNotNull(result);
    }

    /**
     * Test of getCGIInputStream method, of class NetUtils.
     */
    @Test
    public void testGetByteArrayInputStream() throws Exception {
        System.out.println("getByteArrayInputStream");
        ArrayList<String> envp = new ArrayList<String>();
        ArrayList<NameValuePair> params = null;

        ByteArrayInputStream result = NetUtils.getByteArrayInputStream(envp, params);

        assertNotNull(result);        
    }

}