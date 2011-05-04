/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.common.validators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.ribax.common.data.DataUtils;
import utils.types.NameValuePair;

/**
 *
 * @author damian
 */
public class ValidatorAdaptorTest {

    Element config;
    
    public ValidatorAdaptorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {
        File dir1 = new File(".");

        // read the validator XML that validates email addresses
        String url = "file://" + dir1.getCanonicalPath() + "/resources/validatorTest.xml";

        String name = "test";

        config = DataUtils.getDocumentRoot(url, null, name);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of readDescription method, of class ValidatorAdaptor.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");

        ValidatorAdaptor instance = new ValidatorAdaptor();
        instance.readDescription(config);

    }

    /**
     * Test of getErrorMessage method, of class ValidatorAdaptor.
     */
    @Test
    public void testGetErrorMessage() {
        System.out.println("getErrorMessage");
        ValidatorAdaptor instance = new ValidatorAdaptor();
        instance.readDescription(config);

        String expResult = "";
        String result = instance.getErrorMessage();

        // the expected result should be 124 bytes
        assertEquals(124, result.length());
        
    }
}
