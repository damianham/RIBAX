/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.common.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.jdom.Element;
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
public class DataUtilsTest {

    public DataUtilsTest() {
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
     * Test of getDocumentRoot method, of class DataUtils.
     */
    @Test
    public void testGetDocumentRoot() throws Exception {
        System.out.println("getDocumentRoot");
        File dir1 = new File (".");
        String url = "file://" + dir1.getCanonicalPath() + "/resources/datamodelTest.xml";

        ArrayList<NameValuePair> params = null;
        String name = "";

        Element result = DataUtils.getDocumentRoot(url, params, name);
        assertNotNull(result);
    }

    /**
     * Test of readElementBlock method, of class DataUtils.
     */
    @Test
    public void testReadElementBlock() throws FileNotFoundException {
        System.out.println("readElementBlock");

        String[] triggers = {
            "modelData" //$NON-NLS-1$
        };

        String url = "";
        File f = new File("./resources/blockTest.xml");
        BufferedReader bin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

        Element result = DataUtils.readElementBlock(url, bin, triggers);
        assertNotNull(result);
    }

    /**
     * Test of getDataAsString method, of class DataUtils.
     */
    @Test
    public void testGetDataAsString() throws IOException {
        System.out.println("getDataAsString");

        String url = "test://./resources/datamodelTest.xml";
        ArrayList<NameValuePair> params = null;
        String name = "";

        Element root = DataUtils.getDocumentRoot(url, params, name);
        assertNotNull(root);

        String result = DataUtils.getDataAsString(root);
        assertNotNull(result);
    }

    /**
     * Test of getDataAsByteArray method, of class DataUtils.
     */
    @Test
    public void testGetDataAsByteArray() throws IOException {
        System.out.println("getDataAsByteArray");

        String url = "test://./resources/datamodelTest.xml";
        ArrayList<NameValuePair> params = null;
        String name = "";

        Element root = DataUtils.getDocumentRoot(url, params, name);
        assertNotNull(root);

        byte[] result = DataUtils.getDataAsByteArray(root);
        assertNotNull(result);
    }

    /**
     * Test of getDataAsInputStream method, of class DataUtils.
     */
    @Test
    public void testGetDataAsInputStream() throws IOException {
        System.out.println("getDataAsInputStream");

        String url = "test://./resources/datamodelTest.xml";
        ArrayList<NameValuePair> params = null;
        String name = "";

        Element root = DataUtils.getDocumentRoot(url, params, name);
        assertNotNull(root);

        InputStream result = DataUtils.getDataAsInputStream(root);
        assertNotNull(result);
    }
}
