/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.common.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.File;
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
public class DataModelManagerTest {

    public DataModelManagerTest() {
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
     * Test of getInstance method, of class DataModelManager.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");

        DataModelManager result = DataModelManager.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of addModel and getModel methods, of class DataModelManager.
     */
    @Test
    public void testAddModel() {
        System.out.println("addModel");
        System.out.println("getModel");
        String name = "testModel";
        DataModel model = new JDomDataModel();
        DataModelManager instance = DataModelManager.getInstance();
        instance.addModel(name, model);
        DataModel result = instance.getModel(name);
        assertEquals(model, result);
    }

    /**
     * Test of readModel method, of class DataModelManager.
     */
    @Test
    public void testReadModel() throws FileNotFoundException, JDOMException, IOException {
        System.out.println("readModel");

        File f = new File("./resources/datamodelTest.xml");

        BufferedInputStream fin = new BufferedInputStream(new FileInputStream(f));

        // create node tree from XML
        SAXBuilder builder = new SAXBuilder();

        Document doc = builder.build(fin);
        Element root = doc.getRootElement();

        String path = "testRead";
        DataModelManager instance = DataModelManager.getInstance();
        instance.readModel(root, path);
        DataModel result = instance.getModel(path + "/testModel");
        assertNotNull(result);
    }
}
