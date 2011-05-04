/*
 * RIBAX, Making Web Applications Easy 
 * Copyright (C) 2006 Damian Hamill and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ribax.swing.datasources;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.ribax.common.Messages;
import org.ribax.common.net.NetUtils;
import org.ribax.swing.parameters.ParameterSet;

import utils.log.BasicLogger;
import utils.types.NameValuePair;

/**
 * Implements a DataSource for a set of drop down menu options
 * using ComboBoxModel
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class OptionDataSource implements ComboBoxModel {

    private Object mSelectedItem = null;
    private ArrayList<Object> table = new ArrayList<Object>();
    private static final String BUNDLE_NAME = "org.ribax.swing.datasources.messages"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(OptionDataSource.class.getName());

    public OptionDataSource(String url, String name) throws IOException {
        getOptions(url, name);
    }

    //////// data methods
    private boolean readOptions(InputStream in, String url) {

        SAXBuilder builder = new SAXBuilder();

        try {
            Document doc = builder.build(in);
            Element root = doc.getRootElement();

            List<Element> optlist = root.getChildren();
            Iterator<Element> optit = optlist.iterator();
            while (optit.hasNext()) {
                Element opt = optit.next();
                if (opt.getChild("name") != null) { //$NON-NLS-1$
                    table.add(new NameValuePair(opt.getChild("name").getText(), //$NON-NLS-1$
                            opt.getChild("value").getText())); //$NON-NLS-1$
                } else {
                    table.add(opt.getText());
                }
            }

        } catch (JDOMException ex) {
            // indicates a well-formedness error
            LOG.error(Messages.getString(BUNDLE_NAME, "OptionDataSource.3") + url, ex); //$NON-NLS-1$

            return false;
        } catch (IOException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "OptionDataSource.4") + url, ex); //$NON-NLS-1$

            return false;
        }
        return true;
    }

    private boolean getOptions(String url, String name) throws IOException {

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        // add any global parameters
        if (ParameterSet.globalParameterSet != null) {
            ArrayList<NameValuePair> tlist = ParameterSet.globalParameterSet.getNameValuePairs();
            if (tlist != null) {
                params.addAll(tlist);
            }
        }


        try {
            InputStream in = NetUtils.getInputStream(url, params, name);

            if (readOptions(in, url) == false) {
                return false;
            }
            fireListDataChanged();
            return true;
        } catch (IOException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "OptionDataSource.5") + url, ex); //$NON-NLS-1$
            throw (ex);
        }

    }

    //// ============= model methods
    public int getSize() {
        return table.size();
    }

    public Object getElementAt(int index) {
        Object o = table.get(index);

        if (o instanceof NameValuePair) {
            NameValuePair p = (NameValuePair) o;
            return p.getName();
        } else {
            return o;
        }
    }

    public Object getSelectedItem() {
        return mSelectedItem;
    }

    public void setSelectedItem(Object obj) {
        mSelectedItem = obj;
    }
    private Vector<ListDataListener> mListDataListeners = new Vector<ListDataListener>();

    public void addListDataListener(ListDataListener l) {
        mListDataListeners.addElement(l);
    }

    public void removeListDataListener(ListDataListener l) {
        mListDataListeners.removeElement(l);
    }

    public void fireListDataChanged(ListDataEvent event) {
        int last = getSize() - 1;

        if (last < 0) {
            return;
        }

        for (int i = 0; i < mListDataListeners.size(); i++) {
            ListDataListener l =
                    (ListDataListener) mListDataListeners.elementAt(i);
            l.contentsChanged(event);
        }
    }

    public void fireListDataChanged() {
        int last = getSize() - 1;

        if (last < 0) {
            return;
        }

        ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, last);

        fireListDataChanged(event);
    }
}
