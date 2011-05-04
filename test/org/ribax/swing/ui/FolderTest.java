/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jdom.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utils.types.NameValuePair;

/**
 *
 * @author damian
 */
public class FolderTest {

    public FolderTest() {
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
     * Test of getFolderIcon method, of class Folder.
     */
    @Test
    public void testGetFolderIcon() {
        System.out.println("getFolderIcon");
        Folder instance = null;
        ImageIcon expResult = null;
        ImageIcon result = instance.getFolderIcon();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readFolder method, of class Folder.
     */
    @Test
    public void testReadFolder() {
        System.out.println("readFolder");
        DefaultMutableTreeNode treenode = null;
        Element node = null;
        Folder instance = null;
        instance.readFolder(treenode, node);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addDataItem method, of class Folder.
     */
    @Test
    public void testAddDataItem() {
        System.out.println("addDataItem");
        DataItem item = null;
        Folder instance = null;
        instance.addDataItem(item);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Folder.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Folder instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refresh method, of class Folder.
     */
    @Test
    public void testRefresh() {
        System.out.println("refresh");
        ArrayList<NameValuePair> params = null;
        String action = "";
        Folder instance = null;
        instance.refresh(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class Folder.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        Folder instance = null;
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of invalidateData method, of class Folder.
     */
    @Test
    public void testInvalidateData() {
        System.out.println("invalidateData");
        Folder instance = null;
        instance.invalidateData();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateData method, of class Folder.
     */
    @Test
    public void testUpdateData() {
        System.out.println("updateData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        Folder instance = null;
        instance.updateData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class Folder.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        Folder instance = null;
        instance.loadData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createAppletImageIcon method, of class Folder.
     */
    @Test
    public void testCreateAppletImageIcon() {
        System.out.println("createAppletImageIcon");
        String path = "";
        String description = "";
        ImageIcon expResult = null;
        ImageIcon result = Folder.createAppletImageIcon(path, description);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class Folder.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        Folder instance = null;
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNameValuePair method, of class Folder.
     */
    @Test
    public void testGetNameValuePair() {
        System.out.println("getNameValuePair");
        Folder instance = null;
        NameValuePair expResult = null;
        NameValuePair result = instance.getNameValuePair();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class Folder.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        Folder instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllElements method, of class Folder.
     */
    @Test
    public void testGetAllElements() {
        System.out.println("getAllElements");
        Folder instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getAllElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedElements method, of class Folder.
     */
    @Test
    public void testGetSelectedElements() {
        System.out.println("getSelectedElements");
        Folder instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getSelectedElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class Folder.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        Element node = null;
        Folder instance = null;
        instance.setData(node);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class Folder.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        Folder instance = null;
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}