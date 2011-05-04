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

import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.ribax.common.Messages;

import utils.log.BasicLogger;
import utils.types.NameValuePair;

/**
 * A set of parameters that are layed out in the GUI
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class ParameterSet extends JPanel {

    public static final long serialVersionUID = 1;
    private static final String BUNDLE_NAME = "org.ribax.swing.parameters.messages"; //$NON-NLS-1$
    // defaults to a vertical layout on the left
    private String layoutLocation = BorderLayout.WEST;
    private int layoutType = BoxLayout.Y_AXIS;
    public static ParameterSet globalParameterSet = null;
    private ArrayList<Parameter> params = new ArrayList<Parameter>();
    private Box box;
    private static BasicLogger LOG = new BasicLogger(ParameterSet.class.getName());

    public ParameterSet(String location, String axis) {
        if (location.equals(BorderLayout.EAST)) {
            this.layoutLocation = BorderLayout.EAST;
        } else if (location.equals(BorderLayout.WEST)) {
            this.layoutLocation = BorderLayout.WEST;
        } else if (location.equals(BorderLayout.NORTH)) {
            this.layoutLocation = BorderLayout.NORTH;
        } else if (location.equals(BorderLayout.SOUTH)) {
            this.layoutLocation = BorderLayout.SOUTH;
        } else {
            LOG.warn(Messages.getString(BUNDLE_NAME, "ParameterSet.0") + location + Messages.getString(BUNDLE_NAME, "ParameterSet.1") + //$NON-NLS-1$ //$NON-NLS-2$
                    BorderLayout.WEST + ", " + //$NON-NLS-1$
                    BorderLayout.EAST + ", " + //$NON-NLS-1$
                    BorderLayout.NORTH + ", " + //$NON-NLS-1$
                    BorderLayout.SOUTH);
        }

        if ("VERTICAL".equals(axis)) {  //$NON-NLS-1$
            box = Box.createVerticalBox();
            layoutType = BoxLayout.Y_AXIS;
        } else if ("HORIZONTAL".equals(axis)) { //$NON-NLS-1$
            box = Box.createHorizontalBox();
            layoutType = BoxLayout.X_AXIS;
        } else {
            LOG.warn(Messages.getString(BUNDLE_NAME, "ParameterSet.7") + axis + Messages.getString(BUNDLE_NAME, "ParameterSet.8")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        setLayout(new BorderLayout());
        add(box, BorderLayout.CENTER);
    }

    public String getLayoutLocation() {
        return layoutLocation;
    }

    public int getLayoutType() {
        return layoutType;
    }

    private void addToBox(JComponent p) {
        box.add(p);
        if (layoutType == BoxLayout.Y_AXIS) {
            box.add(Box.createVerticalGlue());
        } else {
            box.add(Box.createHorizontalGlue());
        }
    }

    public void addHidden(Parameter p) {
        params.add(p);
    }

    public void addShown(Parameter p) {
        params.add(p);
        addToBox(p);
    }

    public void addGUIobject(JComponent object) {
        addToBox(object);
    }

    public ArrayList<NameValuePair> getNameValuePairs() {

        if (params.size() == 0) {
            return null;
        }

        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        NameValuePair nvp;

        Iterator<Parameter> it = params.iterator();
        while (it.hasNext()) {
            Parameter p = it.next();

            if ((nvp = p.getNameValuePair()) != null) {
                list.add(nvp);
            }
        }

        return list;
    }

    public static ParameterSet readParameters(Element paramList) {
        ParameterSet params;

        String location = null, axis = null;
        Element e;
        Attribute attr;

        if ((e = paramList.getChild("location")) != null) //$NON-NLS-1$
        {
            location = e.getText();
        } else if ((attr = paramList.getAttribute("location")) != null) //$NON-NLS-1$
        {
            location = attr.getValue();
        }

        if ((e = paramList.getChild("axis")) != null) //$NON-NLS-1$
        {
            axis = e.getText();
        } else if ((attr = paramList.getAttribute("axis")) != null) //$NON-NLS-1$
        {
            axis = attr.getValue();
        }

        if (location == null || axis == null) {
            LOG.warn(Messages.getString(BUNDLE_NAME, "ParameterSet.13") + location + Messages.getString(BUNDLE_NAME, "ParameterSet.14") + axis + Messages.getString(BUNDLE_NAME, "ParameterSet.15")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return null;
        }

        if (location.length() == 0 || axis.length() == 0) {
            LOG.warn(Messages.getString(BUNDLE_NAME, "ParameterSet.16") + location + Messages.getString(BUNDLE_NAME, "ParameterSet.17") + axis + Messages.getString(BUNDLE_NAME, "ParameterSet.18")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return null;
        }

        params = new ParameterSet(location, axis);

        List<Element> list = paramList.getChildren("parameter"); //$NON-NLS-1$
        Iterator<Element> it = list.iterator();
        while (it.hasNext()) {
            Element param = it.next();

            String type = "TEXT"; //$NON-NLS-1$

            if ((e = param.getChild("type")) != null) //$NON-NLS-1$
            {
                type = e.getText();
            } else if ((attr = param.getAttribute("type")) != null) //$NON-NLS-1$
            {
                type = attr.getValue();
            }

            if ("SELECT".equals(type.toUpperCase())) { //$NON-NLS-1$
                SelectFieldParameter p = new SelectFieldParameter(params.getLayoutType());

                p.readDescription(param);

                params.addShown(p);
            } else if ("TEXT".equals(type.toUpperCase())) { //$NON-NLS-1$
                TextFieldParameter p = new TextFieldParameter(params.getLayoutType());

                p.readDescription(param);

                params.addShown(p);
            } else if ("FILE".equals(type.toUpperCase())) { //$NON-NLS-1$
                FileParameter f = new FileParameter(params.getLayoutType());

                f.readDescription(param);

                params.addShown(f);
            } else if ("HIDDEN".equals(type.toUpperCase())) { //$NON-NLS-1$
                HiddenParameter h = new HiddenParameter();

                h.readDescription(param);

                params.addHidden(h);

            } else if ("TEXTAREA".equals(type.toUpperCase())) { //$NON-NLS-1$

                InfoParameter idi = new InfoParameter(params.getLayoutType(), true);

                idi.readDescription(param);

                params.addShown(idi);
            } else if ("INFO".equals(type.toUpperCase())) { //$NON-NLS-1$

                InfoParameter idi = new InfoParameter(params.getLayoutType());

                idi.readDescription(param);

                params.addGUIobject(idi);
            }
            LOG.debug(Messages.getString(BUNDLE_NAME, "ParameterSet.29") + type); //$NON-NLS-1$
        }
        return params;
    }
}
