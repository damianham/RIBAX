/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ribax.swing.ui;

import java.awt.Component;
import java.awt.print.Printable;
import java.util.ArrayList;
import javax.swing.JComponent;
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
public class TabContainerTest {

    public TabContainerTest() {
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
     * Test of getNorthPanel method, of class TabContainer.
     */
    @Test
    public void testGetNorthPanel() {
        System.out.println("getNorthPanel");
        TabContainer instance = null;
        JComponent expResult = null;
        JComponent result = instance.getNorthPanel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSouthPanel method, of class TabContainer.
     */
    @Test
    public void testGetSouthPanel() {
        System.out.println("getSouthPanel");
        TabContainer instance = null;
        JComponent expResult = null;
        JComponent result = instance.getSouthPanel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEastPanel method, of class TabContainer.
     */
    @Test
    public void testGetEastPanel() {
        System.out.println("getEastPanel");
        TabContainer instance = null;
        JComponent expResult = null;
        JComponent result = instance.getEastPanel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWestPanel method, of class TabContainer.
     */
    @Test
    public void testGetWestPanel() {
        System.out.println("getWestPanel");
        TabContainer instance = null;
        JComponent expResult = null;
        JComponent result = instance.getWestPanel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of layoutComponents method, of class TabContainer.
     */
    @Test
    public void testLayoutComponents() {
        System.out.println("layoutComponents");
        TabContainer instance = null;
        instance.layoutComponents();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readDescription method, of class TabContainer.
     */
    @Test
    public void testReadDescription() {
        System.out.println("readDescription");
        Element root = null;
        TabContainer instance = null;
        instance.readDescription(root);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setActionHandler method, of class TabContainer.
     */
    @Test
    public void testSetActionHandler() {
        System.out.println("setActionHandler");
        DataItem parent = null;
        TabContainer instance = null;
        instance.setActionHandler(parent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of invalidateData method, of class TabContainer.
     */
    @Test
    public void testInvalidateData() {
        System.out.println("invalidateData");
        TabContainer instance = null;
        instance.invalidateData();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of invalidateDataItems method, of class TabContainer.
     */
    @Test
    public void testInvalidateDataItems() {
        System.out.println("invalidateDataItems");
        TabContainer instance = null;
        instance.invalidateDataItems();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class TabContainer.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        TabContainer instance = null;
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refresh method, of class TabContainer.
     */
    @Test
    public void testRefresh() {
        System.out.println("refresh");
        ArrayList<NameValuePair> params = null;
        String action = "";
        TabContainer instance = null;
        instance.refresh(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadItemData method, of class TabContainer.
     */
    @Test
    public void testLoadItemData() {
        System.out.println("loadItemData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        TabContainer instance = null;
        instance.loadItemData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateItemData method, of class TabContainer.
     */
    @Test
    public void testUpdateItemData() {
        System.out.println("updateItemData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        TabContainer instance = null;
        instance.updateItemData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of selectItemData method, of class TabContainer.
     */
    @Test
    public void testSelectItemData() {
        System.out.println("selectItemData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        TabContainer instance = null;
        instance.selectItemData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of submitItemData method, of class TabContainer.
     */
    @Test
    public void testSubmitItemData() {
        System.out.println("submitItemData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        TabContainer instance = null;
        instance.submitItemData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validateContents method, of class TabContainer.
     */
    @Test
    public void testValidateContents() {
        System.out.println("validateContents");
        TabContainer instance = null;
        boolean expResult = false;
        boolean result = instance.validateContents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doAction method, of class TabContainer.
     */
    @Test
    public void testDoAction() {
        System.out.println("doAction");
        int buttonType = 0;
        String action = "";
        TabContainer instance = null;
        instance.doAction(buttonType, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setData method, of class TabContainer.
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        Element root = null;
        TabContainer instance = null;
        instance.setData(root);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deferReadDescriptionFromURL method, of class TabContainer.
     */
    @Test
    public void testDeferReadDescriptionFromURL() {
        System.out.println("deferReadDescriptionFromURL");
        String source = "";
        ArrayList<NameValuePair> params = null;
        String action = "";
        TabContainer instance = null;
        instance.deferReadDescriptionFromURL(source, params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class TabContainer.
     */
    @Test
    public void testLoadData() {
        System.out.println("loadData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        TabContainer instance = null;
        instance.loadData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getElements method, of class TabContainer.
     */
    @Test
    public void testGetElements() {
        System.out.println("getElements");
        int mode = 0;
        TabContainer instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getElements(mode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class TabContainer.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        TabContainer instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllElements method, of class TabContainer.
     */
    @Test
    public void testGetAllElements() {
        System.out.println("getAllElements");
        TabContainer instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getAllElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSelectedElements method, of class TabContainer.
     */
    @Test
    public void testGetSelectedElements() {
        System.out.println("getSelectedElements");
        TabContainer instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getSelectedElements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponent method, of class TabContainer.
     */
    @Test
    public void testGetComponent() {
        System.out.println("getComponent");
        TabContainer instance = null;
        Component expResult = null;
        Component result = instance.getComponent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNameValuePair method, of class TabContainer.
     */
    @Test
    public void testGetNameValuePair() {
        System.out.println("getNameValuePair");
        TabContainer instance = null;
        NameValuePair expResult = null;
        NameValuePair result = instance.getNameValuePair();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateData method, of class TabContainer.
     */
    @Test
    public void testUpdateData() {
        System.out.println("updateData");
        ArrayList<NameValuePair> params = null;
        String action = "";
        TabContainer instance = null;
        instance.updateData(params, action);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printElements method, of class TabContainer.
     */
    @Test
    public void testPrintElements() {
        System.out.println("printElements");
        TabContainer instance = null;
        instance.printElements();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printPrintable method, of class TabContainer.
     */
    @Test
    public void testPrintPrintable() {
        System.out.println("printPrintable");
        Printable printable = null;
        TabContainer instance = null;
        instance.printPrintable(printable);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTypeName method, of class TabContainer.
     */
    @Test
    public void testGetTypeName() {
        System.out.println("getTypeName");
        TabContainer instance = null;
        String expResult = "";
        String result = instance.getTypeName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class TabContainerImpl extends TabContainer {

        public TabContainerImpl() {
            super(null);
        }

        public void refresh(ArrayList<NameValuePair> params, String action) {
        }

        public void loadItemData(ArrayList<NameValuePair> params, String action) {
        }

        public void updateItemData(ArrayList<NameValuePair> params, String action) {
        }

        public void selectItemData(ArrayList<NameValuePair> params, String action) {
        }

        public void submitItemData(ArrayList<NameValuePair> params, String action) {
        }

        public void loadData(ArrayList<NameValuePair> params, String action) {
        }

        public Component getComponent() {
            return null;
        }

        public String getTypeName() {
            return "";
        }
    }

}