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
package org.ribax.swing.parameters;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.jdom.Attribute;
import org.jdom.Element;

import org.ribax.common.Messages;
import org.ribax.swing.datasources.OptionDataSource;

import utils.log.BasicLogger;
import utils.types.NameValuePair;

/**
 * A Parameter that provides a set of options in a drop down menu
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class SelectFieldParameter extends Parameter {

    public static final long serialVersionUID = 1;
    private static final String BUNDLE_NAME = "org.ribax.swing.parameters.messages"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(SelectFieldParameter.class.getName());
    private JComboBox menu;
    private Box box;
    private String sourceURL = null;
    @SuppressWarnings("unused")
    private int layoutType = BoxLayout.Y_AXIS;

    public SelectFieldParameter(int layoutType) {
        this.layoutType = layoutType;

        if (layoutType == BoxLayout.Y_AXIS) {
            box = Box.createVerticalBox();
        } else {
            box = Box.createHorizontalBox();
        }
    }

    protected void readDescription(Element di) {
        Element e;
        Attribute attr;
        String edit = ""; //$NON-NLS-1$

        super.readDescription(di);

        this.sourceURL = value;

        if ((e = di.getChild("options")) != null) { //$NON-NLS-1$
            Vector<String> v = new Vector<String>();
            List<Element> optlist = e.getChildren();
            Iterator<Element> optit = optlist.iterator();
            while (optit.hasNext()) {
                Element opt = optit.next();
                v.add(opt.getText());
            }
            String[] options = new String[v.size()];

            v.toArray(options);
            menu = new JComboBox(options);
        } else {
            // get the list of options from the sourceURL
            menu = new JComboBox();
            try {
                menu.setModel(new OptionDataSource(sourceURL, this.name));
            } catch (MalformedURLException ex) {
                errorMessage(Messages.getString(BUNDLE_NAME, "SelectFieldParameter.2") + sourceURL); //$NON-NLS-1$
                LOG.error(Messages.getString(BUNDLE_NAME, "SelectFieldParameter.3") + sourceURL, ex); //$NON-NLS-1$
            } catch (IOException ex) {
                errorMessage(Messages.getString(BUNDLE_NAME, "SelectFieldParameter.4") + sourceURL); //$NON-NLS-1$
                LOG.error(Messages.getString(BUNDLE_NAME, "SelectFieldParameter.5") + sourceURL, ex); //$NON-NLS-1$
            }
        }

        if ((e = di.getChild("editable")) != null) //$NON-NLS-1$
        {
            edit = e.getText();
        } else if ((attr = di.getAttribute("editable")) != null) //$NON-NLS-1$
        {
            edit = attr.getValue();
        }

        if ("TRUE".equals(edit.toUpperCase())) //$NON-NLS-1$
        {
            menu.setEditable(true);
        }

        if (font != null) {
            menu.setFont(font);
        }

        layoutComponents();
    }

    private void layoutComponents() {

        box.add(new JLabel(title));

        box.add(menu);

        if (tooltip != null && tooltip.length() > 0) {
            menu.setToolTipText(tooltip);
        }

        menu.setSelectedIndex(0);

        add(box);
    }

    public NameValuePair getNameValuePair() {
        Object o;
        NameValuePair p;

        if (menu.getSelectedIndex() >= 0) {
            o = menu.getSelectedItem();

            if (o instanceof NameValuePair) {
                p = (NameValuePair) o;
                value = p.getValue().toString();
            } else {
                String text = (String) o;

                if (text.length() > 0) {
                    value = text;
                }
            }
        }

        return super.getNameValuePair();
    }
}
