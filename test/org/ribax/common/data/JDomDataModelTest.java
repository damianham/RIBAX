/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.common.data;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utils.types.NameValuePair;

import static org.mockito.Mockito.*;

/**
 *
 * @author damian
 */
public class JDomDataModelTest {

    static Element root;

    public JDomDataModelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        File f = new File("./resources/datamodelTest.xml");

        BufferedInputStream fin = new BufferedInputStream(new FileInputStream(f));

        // create node tree from XML
        SAXBuilder builder = new SAXBuilder();

        Document doc = builder.build(fin);
        root = doc.getRootElement();
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
     * Test of getDataAsInputStream method, of class JDomDataModel.
     */
    @Test
    public void testGetDataAsInputStream() {
        System.out.println("getDataAsInputStream");
        JDomDataModel instance = new JDomDataModel();

        instance.readDescription(root);

        InputStream result = instance.getDataAsInputStream();
        assertNotNull(result);
    }

    /**
     * Test of readDescription method, of class JDomDataModel.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");

        JDomDataModel instance = new JDomDataModel();
        instance.readDescription(root);

        String path = "details/name";
        String expResult = "Mr Smith";
        String result = instance.getValue(path);
        assertEquals(expResult, result);
    }

    /**
     * Test of setData method, of class JDomDataModel.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");

        JDomDataModel instance = new JDomDataModel();
        DataChangeListener mockedListener = mock(DataChangeListener.class);

        String path = "/";
        instance.addDataChangeListener(mockedListener);

        instance.setData(root);

        // verify the listener was called
        verify(mockedListener).dataChanged(instance, path);
    }


    /**
     * Test of getElement method, of class JDomDataModel.
     */
    @Test
    public void testGetElement() {
        System.out.println("getElement");

        JDomDataModel instance = new JDomDataModel();
        instance.readDescription(root);
        String path = "details/name";

        Object result = instance.getElement(path);
        assertNotNull(result);
    }

    /**
     * Test of getChildren method, of class JDomDataModel.
     */
    @Test
    public void testGetChildren() {
        System.out.println("getChildren");
      
        JDomDataModel instance = new JDomDataModel();
        instance.readDescription(root);

        String path = "details";

        List result = instance.getChildren(path);
        assertNotNull(result);
    }

    /**
     * Test of getValue method, of class JDomDataModel.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");

        JDomDataModel instance = new JDomDataModel();
        instance.readDescription(root);

        String path = "details/name";

        String expResult = "Mr Smith";
        String result = instance.getValue(path);
        assertEquals(expResult, result);
    }

    /**
     * Test of setElement method, of class JDomDataModel.
     */
    @Test
    public void testSetElement() throws Exception {
        System.out.println("setElement");
       
        ArrayList<NameValuePair> params = null;
        JDomDataModel instance = new JDomDataModel();
        instance.readDescription(root);

        String path = "details/name";
        Object value = "Mrs Jones";
        instance.setElement(path, value, params);

        String result = instance.getValue(path);
        assertEquals(value, result);
        
    }
}
