/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common.validators;

import org.ribax.common.data.DataUtils;
import java.io.File;
import java.io.IOException;
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
public class ValidatorFactoryTest {

    public ValidatorFactoryTest() {
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
     * Test of readValidator method, of class ValidatorFactory.
     */
    @Test
    public void testReadValidator() throws IOException {
        System.out.println("readValidator");

        File dir1 = new File (".");
        String url = "file://" + dir1.getCanonicalPath() + "/resources/validatorTest.xml";

        String name = "";

        Element config = DataUtils.getDocumentRoot(url, null, name);

        
        Validator result = ValidatorFactory.readValidator(config);
        assertNotNull( result);

    }

}