/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.common;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
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
public class RIBAXConfigTest {

    public RIBAXConfigTest() {
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
     * Test of getInstance method, of class RIBAXConfig.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        RIBAXConfig result = RIBAXConfig.getInstance();
        assertNotNull(result);
        assertSame(RIBAXConfig.class, result.getClass());
    }

    /**
     * Test of getValue method, of class RIBAXConfig.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        String name = ConfigStrings.LANGUAGE;
        RIBAXConfig instance = RIBAXConfig.getInstance();
        Object expResult = "en";
        Object result = instance.getValue(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of getValue method, with a default value of class RIBAXConfig.
     */
    @Test
    public void testGetValueWithDefault() {
        System.out.println("getValue with default");
        String name = "nonexistent";
        Object defaultValue = "defaultValue";
        RIBAXConfig instance = RIBAXConfig.getInstance();
        Object result = instance.getValue(name, defaultValue);
        assertEquals(defaultValue, result);
    }

    /**
     * Test of setValue method, of class RIBAXConfig.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        String name = ConfigStrings.LANGUAGE;
        Object value = "de";
        RIBAXConfig instance = RIBAXConfig.getInstance();
        Object expResult = "en";
        Object result = instance.setValue(name, value);
        assertEquals(expResult, result);
    }

    /**
     * Test of mapClassName method, of class RIBAXConfig.
     */
    @Test
    public void testMapClassName() throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        File f = new File("./resources/ribax.properties");

        System.out.println("mapClassName " + f.getAbsolutePath());
        
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));

        properties.load(in);
        
        RIBAXConfig instance = RIBAXConfig.getInstance();
        instance.setValue(ConfigStrings.PROPERTIES, properties);

        String className = "COLUMN";
        String expResult = "org.ribax.swing.ui.ColumnDataItem";
        String result = RIBAXConfig.mapClassName(className);
        assertEquals(expResult, result);
     }
}
