/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.common.log;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.ribax.common.net.WebDataSource;
import utils.log.LogComponent;

/**
 *
 * @author damian
 */
public class ResponseLogTest {

    public ResponseLogTest() {
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
     * Test of getInstance method, of class ResponseLog.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        
        ResponseLog result = ResponseLog.getInstance(WebDataSource.class.getName());
        assertNotNull(result);
    }

    /**
     * Test of getInstance method, of class ResponseLog.
     */
    @Test
    public void testGetInstanceWithComponent() {
        System.out.println("getInstance with component");
       
        LogComponent panel = null;
        
        ResponseLog result = ResponseLog.getInstance(WebDataSource.class.getName(),panel);
        assertNotNull(result);
    }

}