/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common.validators;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author damian
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({org.ribax.common.validators.RegexpValidatorTest.class,org.ribax.common.validators.ValidatorAdaptorTest.class,org.ribax.common.validators.ValidatorFactoryTest.class})
public class ValidatorsSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}