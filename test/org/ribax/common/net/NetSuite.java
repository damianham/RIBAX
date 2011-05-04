/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common.net;

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
@Suite.SuiteClasses({org.ribax.common.net.PartUtilsTest.class,org.ribax.common.net.NetUtilsTest.class,org.ribax.common.net.HTTPmultipartTest.class,org.ribax.common.net.FilePartTest.class,org.ribax.common.net.StringPartTest.class,org.ribax.common.net.DataSourceFactoryTest.class,org.ribax.common.net.PartTest.class,org.ribax.common.net.FileDataSourceTest.class})
public class NetSuite {

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