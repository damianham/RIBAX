/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.common.data;

import java.util.List;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
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
public class DataModelTest {

    static Element root;
    DataModel instance;

    public DataModelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        File f = new File("./resources/dataModelTest.xml");

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
        instance = new JDomDataModel();

        instance.readDescription(root);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class DataModel.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");

        String expResult = "testModel";
        String result = instance.getName();
        assertEquals(expResult, result);

    }

    /**
     * Test of setName method, of class DataModel.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "newName";

        instance.setName(name);

        String result = instance.getName();
        assertEquals(name, result);
    }

    /**
     * Test of getServiceEndpoint method, of class DataModel.
     */
    @Test
    public void testGetServiceEndpoint() {
        System.out.println("getServiceEndpoint");

        String expResult = "http://localhost/test/endpoint.pl";
        String result = instance.getServiceEndpoint();
        assertEquals(expResult, result);

    }

    /**
     * Test of setServiceEndpoint method, of class DataModel.
     */
    @Test
    public void testSetServiceEndpoint() {
        System.out.println("setServiceEndpoint");
        String url = "http://localhost/test/newEndpoint.pl";

        instance.setServiceEndpoint(url);
        String result = instance.getServiceEndpoint();
        assertEquals(url, result);

    }

    /**
     * Test of getMethodName method, of class DataModel.
     */
    @Test
    public void testGetMethodName() {
        System.out.println("getMethodName");

        String expResult = "soapMethod";
        String result = instance.getMethodName();
        assertEquals(expResult, result);

    }

    /**
     * Test of setMethodName method, of class DataModel.
     */
    @Test
    public void testSetMethodName() {
        System.out.println("setMethodName");
        String methodName = "soapMethod2";

        instance.setMethodName(methodName);
        String result = instance.getMethodName();
        assertEquals(methodName, result);
    }

    /**
     * Test of readDescription method, of class DataModel.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");

        instance.readDescription(root);
        String path = "details/name";
        String expResult = "Mr Smith";
        String result = instance.getValue(path);
        assertEquals(expResult, result);
    }

    /**
     * Test of addDataChangeListener and fireDataChanged methods, of class DataModel.
     */
    @Test
    public void testAddDataChangeListener() {
        System.out.println("addDataChangeListener");
        DataChangeListener mockedListener = mock(DataChangeListener.class);

        String path = "details/name";
        instance.addDataChangeListener(mockedListener);

        instance.fireDataChanged(path);

        // verify the listener was called
        verify(mockedListener).dataChanged(instance, path);
    }

    /**
     * Test of removeDataChangeListener method, of class DataModel.
     */
    @Test
    public void testRemoveDataChangeListener() {
        System.out.println("removeDataChangeListener");
        DataChangeListener mockedListener = mock(DataChangeListener.class);

        String path = "details/name";
        instance.addDataChangeListener(mockedListener);

        instance.fireDataChanged(path);

        // verify the listener was called
        verify(mockedListener).dataChanged(instance, path);

        instance.removeDataChangeListener(mockedListener);

        // fire again
        instance.fireDataChanged(path);

        // verify the listener was not called again
        verify(mockedListener, times(1)).dataChanged(instance, path);
    }

    /**
     * Test of getElement method, of class DataModel.
     */
    @Test
    public void testGetElement() {
        System.out.println("getElement");
        String path = "details/name";

        Object result = instance.getElement(path);
        assertNotNull(result);
    }

    /**
     * Test of getChildren method, of class DataModel.
     */
    @Test
    public void testGetChildren() {
        System.out.println("getChildren");
        String path = "details";

        List result = instance.getChildren(path);
        assertNotNull(result);
    }

    /**
     * Test of getValue method, of class DataModel.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        String path = "details/name";

        String expResult = "Mr Smith";
        String result = instance.getValue(path);
        assertEquals(expResult, result);
    }

    /**
     * Test of setElement method, of class DataModel.
     */
    @Test
    public void testSetElement() throws Exception {
        System.out.println("setElement");
        String path = "details/name";
        Object value = "Mrs Jones";
        ArrayList<NameValuePair> params = null;

        instance.setElement(path, value, params);

        String result = instance.getValue(path);
        assertEquals(value, result);

    }

    /**
     * Test of getInputStream method, of class DataModel.
     */
    @Test
    public void testGetInputStream() throws Exception {
        System.out.println("getInputStream");
        File dir1 = new File (".");
        String url = "file://" + dir1.getCanonicalPath() + "/resources/datamodelTest.xml";

        ArrayList<NameValuePair> params = null;

        InputStream result = instance.getInputStream(url, params);
        assertNotNull(result);
    }
    
    /**
     * Test of getDataAsInputStream method, of class DataModel.
     */
    @Test
    public void testGetDataAsInputStream() {
        System.out.println("getDataAsInputStream");

        InputStream result = instance.getDataAsInputStream();
        assertNotNull(result);
    }

   
}
