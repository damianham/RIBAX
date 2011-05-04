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
package org.ribax.swing.ui;

import java.awt.Component;
import java.util.ArrayList;

import org.jdom.Element;
import org.ribax.common.data.DataChangeListener;
import org.ribax.common.data.DataModel;


import utils.types.NameValuePair;

/**
 * A data item which is not displayed in the GUI, it is used to hold
 * data that is passed back to the data source in network requests
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class HiddenDataItem extends DataItemAdaptor {

    public static final long serialVersionUID = 1;

    /**
     * No argument Constructor - required
     */
    public HiddenDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {

        super.readDescription(di);

        // if a data model was specified then add a listener to the model
        // to be informed of data updates
        if (model != null) {
            if (modelPath == null) {
                modelPath = getPath();
            }

            value = model.getValue(modelPath);
            model.addDataChangeListener(new DataChangeListener() {

                public void dataChanged(DataModel model, String path) {

                    // check that we are interested in this data
                    if (path == null || !modelPath.equals(path)) {
                        return;
                    }

                    Object o = model.getElement(modelPath);

                    if (o != null && o instanceof Element) {
                        value = ((Element) o).getText();
                    }
                }
            });
        }

    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */
    public void setData(Element node) {
        Element e;

        if ((e = node.getChild(fieldname)) != null) {
            value = e.getText();
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public Component getComponent() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        if (value == null || value.length() == 0) {
            return null;
        }

        return new NameValuePair(fieldname, value);
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getParameters()
     */

    public ArrayList<NameValuePair> getParameters() {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        NameValuePair pair = getNameValuePair();

        if (pair == null) {
            return null;
        }

        list.add(pair);

        return list;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getAllElements()
     */

    public ArrayList<NameValuePair> getAllElements() {
        return getParameters();
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getSelectedElements()
     */

    public ArrayList<NameValuePair> getSelectedElements() {
        return getParameters();
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */
    public String getTypeName() {
        return DataItemFactory.HIDDEN;
    }
}
