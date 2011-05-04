/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common;

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
@Suite.SuiteClasses({org.ribax.common.data.DataSuite.class,org.ribax.common.validators.ValidatorsSuite.class,org.ribax.common.log.LogSuite.class,org.ribax.common.RIBAXConfigTest.class,org.ribax.common.MessagesTest.class,org.ribax.common.net.NetSuite.class})
public class CommonSuite {

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