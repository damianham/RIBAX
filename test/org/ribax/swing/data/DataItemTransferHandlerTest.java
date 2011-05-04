/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.swing.data;

import org.ribax.swing.ui.TextDataItem;
import org.ribax.common.data.DataUtils;
import java.io.File;
import java.io.IOException;
import org.jdom.Element;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Vector;
import javax.swing.JComponent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 *
 * @author damian
 */
public class DataItemTransferHandlerTest {

    Element config;
    TextDataItem tdi;

    public DataItemTransferHandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {
        File dir1 = new File(".");

        // read the validator XML that validates email addresses
        String url = "file://" + dir1.getCanonicalPath() + "/resources/dataitemTest.xml";

        String name = "test";

        config = DataUtils.getDocumentRoot(url, null, name);
        tdi = new TextDataItem();
        tdi.readDescription(config);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getFlavours method, of class DataItemTransferHandler.
     */
    @Test
    public void testGetFlavours() {
        System.out.println("getFlavours");
        DataItemTransferHandler instance = new DataItemTransferHandler(tdi);

        Vector<DataFlavor> result = instance.getFlavours();
        assertNotNull(result);

        assertEquals(2, result.size());
    }

    /**
     * Test of setFlavours method, of class DataItemTransferHandler.
     */
    @Test
    public void testSetFlavours() {
        System.out.println("setFlavours");
        Vector<DataFlavor> flavours = new Vector<DataFlavor>();
        DataItemTransferHandler instance = new DataItemTransferHandler(tdi);

        flavours.add(DataFlavor.imageFlavor);
        instance.setFlavours(flavours);

        Vector<DataFlavor> result = instance.getFlavours();
        assertNotNull(result);

        assertEquals(1, result.size());
    }

    /**
     * Test of pushFlavor method, of class DataItemTransferHandler.
     */
    @Test
    public void testPushFlavor() {
        System.out.println("pushFlavor");
        String classname = "org.ribax.swing.ui.TextDataItem";
        DataItemTransferHandler instance = new DataItemTransferHandler(tdi);
        instance.pushFlavor(classname);

        Vector<DataFlavor> result = instance.getFlavours();
        assertNotNull(result);

        /*
        for (DataFlavor f : result) {
        System.out.println("flavor = " + f.toString());
        System.out.println("flavor = " + f.getRepresentationClass().getName());
        }
         */
        assertEquals(3, result.size());
        DataFlavor df = result.get(0);

        assertEquals(classname, df.getRepresentationClass().getName());
    }

    /**
     * Test of addFlavor method, of class DataItemTransferHandler.
     */
    @Test
    public void testAddFlavor() {
        System.out.println("addFlavor");
        String classname = "org.ribax.swing.ui.TextDataItem";
        DataItemTransferHandler instance = new DataItemTransferHandler(tdi);
        instance.addFlavor(classname);

        Vector<DataFlavor> result = instance.getFlavours();
        assertNotNull(result);
        assertEquals(3, result.size());
        DataFlavor df = result.get(2);

        assertEquals(classname, df.getRepresentationClass().getName());

    }

    /**
     * Test of pushFlavor method, of class DataItemTransferHandler.
     */
    @Test
    public void testPushDataFlavor() throws ClassNotFoundException {
        System.out.println("pushDataFlavor");

        String classname = "org.ribax.swing.ui.TextDataItem";
        DataItemTransferHandler instance = new DataItemTransferHandler(tdi);
        DataFlavor flavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=" + classname);
        
        instance.pushFlavor(flavor);

        Vector<DataFlavor> result = instance.getFlavours();
        assertNotNull(result);

        /*
        for (DataFlavor f : result) {
        System.out.println("flavor = " + f.toString());
        System.out.println("flavor = " + f.getRepresentationClass().getName());
        }
         */
        assertEquals(3, result.size());
        DataFlavor df = result.get(0);

        assertEquals(flavor,df);
    }

    /**
     * Test of addFlavor method, of class DataItemTransferHandler.
     */
    @Test
    public void testAddDataFlavor() throws ClassNotFoundException {
        System.out.println("addDataFlavor");
        String classname = "org.ribax.swing.ui.TextDataItem";
        DataItemTransferHandler instance = new DataItemTransferHandler(tdi);
        DataFlavor flavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=" + classname);
        instance.addFlavor(flavor);

        Vector<DataFlavor> result = instance.getFlavours();
        assertNotNull(result);
        assertEquals(3, result.size());
        DataFlavor df = result.get(2);

        assertEquals(flavor, df);
    }

    /**
     * Test of importData method, of class DataItemTransferHandler.
     */
    @Test
    public void testImportData() {
        System.out.println("importData");

        TextDataItem di = mock(TextDataItem.class);
        TextDataItem c = mock(TextDataItem.class);
        Transferable t = mock(Transferable.class);

        when(di.getDataItemName()).thenReturn("testName");
        
        DataItemTransferHandler instance = new DataItemTransferHandler(di);

        instance.importData(c, t);

        verify(di).importData(c, t);

        // verify the case when the data item is null
        instance = new DataItemTransferHandler(null);

        instance.importData(di, t);

        verify(di).importData(di, t);

    }

    /**
     * Test of exportDone method, of class DataItemTransferHandler.
     */
    @Test
    public void testExportDone() {
        System.out.println("exportDone");

        int action = 0;
        TextDataItem di = mock(TextDataItem.class);
        TextDataItem c = mock(TextDataItem.class);
        Transferable t = mock(Transferable.class);
        DataItemTransferHandler instance = new DataItemTransferHandler(di);

        instance.exportDone(c, t,action);

        verify(di).exportDone(c, t,action);

        // verify the case when the data item is null
        instance = new DataItemTransferHandler(null);

        instance.exportDone(di, t,action);

        verify(di).exportDone(di, t,action);
    }

    /**
     * Test of getPreferredFlavour method, of class DataItemTransferHandler.
     */
    @Test
    public void testGetPreferredFlavour() throws ClassNotFoundException {
        System.out.println("getPreferredFlavour");
        String classname = "org.ribax.swing.ui.DataItem";
        
        DataItemTransferHandler instance = new DataItemTransferHandler(tdi);

        DataFlavor[] flavors = {new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=org.ribax.swing.ui.DataItem" ),
                    new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=org.ribax.swing.ui.TextDataItem" )

        };

        DataFlavor expResult = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=" + classname);

        DataFlavor result = instance.getPreferredFlavour(flavors);

        assertNotNull(result);
        
        assertEquals(expResult, result);

        // check we can get an added flavor
        classname = "org.ribax.swing.ui.TextDataItem";
        instance.addFlavor(classname);

        expResult = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=" + classname);

        result = instance.getPreferredFlavour(new DataFlavor[] {expResult});

        assertNotNull(result);

        assertEquals(expResult, result);
    }

    /**
     * Test of canImport method, of class DataItemTransferHandler.
     */
    @Test
    public void testCanImport() throws ClassNotFoundException {
        System.out.println("canImport");
        JComponent c = null;
        DataFlavor[] flavors = {new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=org.ribax.swing.ui.DataItem" ),
                    new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=org.ribax.swing.ui.TextDataItem" )

        };
        DataItemTransferHandler instance = new DataItemTransferHandler(tdi);

        boolean expResult = true;
        boolean result = instance.canImport(c, flavors);
        assertEquals(expResult, result);
    }

    /**
     * Test of createTransferable method, of class DataItemTransferHandler.
     */
    @Test
    public void testCreateTransferable() {
        System.out.println("createTransferable");
        JComponent c = null;
        DataItemTransferHandler instance = new DataItemTransferHandler(tdi);
        Transferable expResult = tdi;
        Transferable result = instance.createTransferable(tdi);
        assertEquals(expResult, result);
        
    }

   
}
