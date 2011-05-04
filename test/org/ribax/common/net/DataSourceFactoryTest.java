/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common.net;

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
public class DataSourceFactoryTest {

    public DataSourceFactoryTest() {
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
     * Test of getDataSource method, of class DataSourceFactory.
     */
    @Test
    public void testGetDataSource() {
        System.out.println("getDataSource");        
        String name = "test";
        
        DataSource result = DataSourceFactory.getDataSource("http://localhost/test", name);
        assertTrue(result instanceof WebDataSource);

        result = DataSourceFactory.getDataSource("telnet://localhost/test", name);
        assertTrue(result instanceof SocketDataSource);

        result = DataSourceFactory.getDataSource("file://localhost/test", name);
        assertTrue(result instanceof FileDataSource);

        result = DataSourceFactory.getDataSource("test://localhost/test", name);
        assertTrue(result instanceof TestDataSource);
    }

}