/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ribax.swing.datasources;

import javax.swing.event.ListDataEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.event.ListDataListener;
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
public class OptionDataSourceTest {

    String url;

    public OptionDataSourceTest() {
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
        url = "file://" + dir1.getCanonicalPath() + "/resources/optionTest.xml";
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getSize method, of class OptionDataSource.
     */
    @Test
    public void testGetSize() throws IOException {
        System.out.println("getSize");

        OptionDataSource instance = new OptionDataSource(url, "test");
        int expResult = 5;
        int result = instance.getSize();
        assertEquals(expResult, result);

    }

    /**
     * Test of getElementAt method, of class OptionDataSource.
     */
    @Test
    public void testGetElementAt() throws IOException {
        System.out.println("getElementAt");
        int index = 0;
        OptionDataSource instance = new OptionDataSource(url, "test");
        Object expResult = "option 1";
        Object result = instance.getElementAt(index);
        assertEquals(expResult, result);

    }

    /**
     * Test of getSelectedItem/setSelectedItem method, of class OptionDataSource.
     */
    @Test
    public void testGetSelectedItem() throws IOException {
        System.out.println("getSelectedItem");
        OptionDataSource instance = new OptionDataSource(url, "test");
        Object expResult = instance.getElementAt(0);
        instance.setSelectedItem(expResult);
        Object result = instance.getSelectedItem();
        assertEquals(expResult, result);

    }

    /**
     * Test of addListDataListener, removeListDataListener and
     * fireListDataChanged method, of class OptionDataSource.
     */
    @Test
    public void testFireListDataChanged() throws IOException {
        System.out.println("fireListDataChanged");

        ListDataListener l = mock(ListDataListener.class);
        OptionDataSource instance = new OptionDataSource(url, "test");
        instance.addListDataListener(l);

        ListDataEvent event = new ListDataEvent(instance, ListDataEvent.CONTENTS_CHANGED, 0, instance.getSize() -1);

        instance.fireListDataChanged(event);

        verify(l).contentsChanged(event);

        instance.removeListDataListener(l);

        instance.fireListDataChanged();

        verify(l,times(1)).contentsChanged(event);
    }
}
