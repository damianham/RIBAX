/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.common.validators;

import java.util.ArrayList;
import org.ribax.common.data.DataUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jdom.Element;
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
public class RegexpValidatorTest {

    Element config;

    public RegexpValidatorTest() {
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
     * Test of readDescription method, of class RegexpValidator.
     */
    @Test
    public void testReadDescription() throws IOException {
        System.out.println("readDescription");

        RegexpValidator instance = new RegexpValidator();
        instance.readDescription(config);
    }

    /**
     * Test of validate method, of class RegexpValidator.
     */
    @Test
    public void testValidateString() {
        System.out.println("validate String");

        RegexpValidator instance = new RegexpValidator();
        instance.readDescription(config);

        // test an invalid email fails
        boolean expResult = false;
        boolean result = instance.validate("invalid email");
        assertEquals(expResult, result);

        // test a valid email passes
        expResult = true;
        result = instance.validate("test@test.com");
        assertEquals(expResult, result);
    }

    /**
     * Test of validate method, of class RegexpValidator.
     */
    @Test
    public void testValidateList() {
        System.out.println("validate List");
        List<String> params = new ArrayList<String>();
        params.add("invalid email");

        RegexpValidator instance = new RegexpValidator();
        instance.readDescription(config);

        // test an invalid email fails
        boolean expResult = false;
        boolean result = instance.validate(params);
        assertEquals(expResult, result);

        // test a valid email passes
        params.clear();
        params.add("test@test.com");
        expResult = true;
        result = instance.validate(params);
        assertEquals(expResult, result);
    }

    /**
     * Test of getErrorMessage method, of class RegexpValidator.
     */
    @Test
    public void testGetErrorMessage() {
        System.out.println("getErrorMessage");
        RegexpValidator instance = new RegexpValidator();
        instance.readDescription(config);
        
        String result = instance.getErrorMessage();

        // the expected result should be 124 bytes
        assertEquals(124, result.length());

    }
}
