/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common.net;

import java.io.File;
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
public class FileDataSourceTest {

    public FileDataSourceTest() {
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
     * Test of getInputStream method, of class FileDataSource.
     */
    @Test
    public void testGetInputStream() throws Exception {
        System.out.println("getInputStream");

        File dir1 = new File (".");        
        String url = "file://" + dir1.getCanonicalPath() + "/resources/datamodelTest.xml";

        ArrayList<NameValuePair> params = null;
        FileDataSource instance = new FileDataSource(url,"test");
        
        InputStream result = instance.getInputStream(params);
        assertNotNull(result);
    }

    

}