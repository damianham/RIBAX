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

import java.awt.Insets;
import java.util.ArrayList;

import org.jdom.Element;

import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * A class for editing documents
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class EditorDataItem extends UrlDataItem {

    public static final long serialVersionUID = 1;

    /**
     * No argument Constructor - required
     */
    public EditorDataItem() {
        // set the margin of the text pane
        editorPane.setMargin(new Insets(15, 15, 15, 15));
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.UrlDataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {

        super.readDescription(di);

        // an editor pane is editable 
        editorPane.setEditable(true);

        setData(di);
    }

    /**
     * Set the text in the editor pane.
     * 
     * @param text the text to set.
     */
    public void setText(String text) {
        editorPane.setText(text);
    }

    // override certain methods from UrlDataItem
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.UrlDataItem#setData(org.jdom.Element)
     */
    public void setData(Element node) {

        // this class can only set the text of the text pane
        String text = XMLutils.getElementString("text", node);

        if (text != null) {
            setText(text);
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.UrlDataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        if (editorPane.getText().length() == 0) {
            return null;
        }

        // return the fieldname and text value 
        return new NameValuePair(fieldname, editorPane.getText());
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.UrlDataItem#getParameters()
     */
    public ArrayList<NameValuePair> getParameters() {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(getNameValuePair());
        return list;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.UrlDataItem#getTypeName()
     */
    public String getTypeName() {
        return DataItemFactory.EDITOR;
    }
}
