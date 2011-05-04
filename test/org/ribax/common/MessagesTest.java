/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.common;

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
public class MessagesTest {

    private static final String BUNDLE_NAME = "org.ribax.common.messages"; //$NON-NLS-1$

    public MessagesTest() {
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
     * Test of getString method, of class Messages.
     */
    @Test
    public void testGetString() {
        System.out.println("getString");
        String bundleName = BUNDLE_NAME;
        String key = "TestMessage.1";
        String expResult = "test message 1";
        String result = Messages.getString(bundleName, key);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocale method, of class Messages.
     */
    @Test
    public void testGetLocale() {
        System.out.println("getLocale");
        String expResult = "en";
        String result = Messages.getLocale();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLocale method, of class Messages.
     */
    @Test
    public void testSetLocale() {
        System.out.println("setLocale");
        String locale = "de";
        Messages.setLocale(locale);
        String result = Messages.getLocale();
        assertEquals(locale, result);
    }
}
