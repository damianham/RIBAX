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

import javax.swing.Box;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JPanel;

import javax.swing.JSeparator;
import javax.swing.JLabel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.JTextArea;
import org.jdom.Element;
import org.ribax.common.Messages;
import org.ribax.common.data.DataChangeListener;
import org.ribax.common.data.DataModel;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * A class that displays a text area with a title and a separator
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class InfoDataItem extends DataItem {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(InfoDataItem.class.getName());
    /** The text area containing the text */
    private JTextArea field;
    /** Default number of rows */
    private int rows = 3;
    /** Default number of columns */
    private int cols = 30;
    /** Info DataItems are not editable */
    protected boolean editable = false;
    /** The main panel for this component */
    private Box box = Box.createVerticalBox();

    /**
     * No argument Constructor - required
     */
    public InfoDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {
        super.readDescription(di);

        // XML parsers do not preserve newlines in text elements (why ???) so we use
        // <newline> in the CDATA element to indicate that we want a new line
        if (value != null) {
            value = value.replaceAll("<newline>", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        Integer intval;

        // get the number of rows
        if ((intval = XMLutils.getElementInt("rows", di)) != null) //$NON-NLS-1$
        {
            rows = intval.intValue();
        }

        // get the number of columns
        if ((intval = XMLutils.getElementInt("columns", di)) != null) //$NON-NLS-1$
        {
            cols = intval.intValue();
        }

        Boolean boolval;

        // this indicates whether this is editable or not
        if ((boolval = XMLutils.getElementBoolean("editable", di)) != null) //$NON-NLS-1$
        {
            this.editable = boolval.booleanValue();
        }

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

                    value = model.getValue(modelPath);

                    if (value != null && field.isFocusOwner() == false) {
                        field.setText(value);
                    }
                }
            });
        }

        layoutComponents();
    }

    /**
     * Layout the GUI components of this Data Item.
     * 
     */
    private void layoutComponents() {

        // clear any previous GUI components
        removeAll();
        box.removeAll();

        // create the text area with the specified text, rows and columns
        field = new JTextArea(value, rows, cols);

        // set the line wrap and margin properties
        field.setLineWrap(true);
        field.setWrapStyleWord(true);
        field.setEditable(editable);
        field.setMargin(new Insets(5, 5, 5, 5));

        if (font != null) {
            field.setFont(font);
        }

        // use BorderLayout
        setLayout(new BorderLayout());

        // if we have a title then display this at the top above the text field
        // seperated by a horizontal line
        if (title != null && title.length() > 0) {
            box.add(new JLabel(title));
            box.add(new JSeparator());
        }
        // embed the text field in a sub panel
        JPanel p1 = new JPanel();
        p1.add(field);

        // set the field tooltip
        if (tooltip != null && tooltip.length() > 0) {
            field.setToolTipText(tooltip);
        }

        // add the field to the vertical box
        box.add(p1);

        if (prefWidth > 0 || prefHeight > 0) {
            Dimension d = new Dimension(prefWidth == 0 ? 400 : prefWidth,
                    prefHeight == 0 ? 400 : prefHeight);
            box.setPreferredSize(d);
        }

        add(box, BorderLayout.CENTER);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public Component getComponent() {
        return field;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        if (editable == false || fieldname == null || fieldname.length() == 0) {
            return null;
        } else {
            return new NameValuePair(fieldname, field.getText());
        }
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getParameters()
     */

    public ArrayList<NameValuePair> getParameters() {
        NameValuePair pair;

        if ((pair = getNameValuePair()) == null) {
            return null;
        }

        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
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
        return null;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */

    public void setData(Element node) {
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#updateData(java.util.ArrayList, java.lang.String)
     */

    public void updateData(ArrayList<NameValuePair> params, String action) {
        refresh(params, action);
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#loadData(java.util.ArrayList, java.lang.String)
     */

    public void loadData(ArrayList<NameValuePair> params, String action) {
        refresh(params, action);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#refresh(java.util.ArrayList, java.lang.String)
     */
    public void refresh(final ArrayList<NameValuePair> params, final String action) {

        // if there is no stream source then this DataItem cannot refresh
        if (streamSource == null) {
            return;
        }

        // do the refresh in a background task
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                loadItemData(params, action);

            }
        });

    }
    private boolean stopped = false;

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#close()
     */
    public void close() {
        stopped = true;
    }

    /**
     * Connect to a streaming web service to stream data into the text field.
     * 
     * @param params a set of parameters to submit to the web service
     * @param action a text string describing the action
     */
    protected void loadItemData(ArrayList<NameValuePair> params, String action) {

        stopped = false;



        if (action != null) {
            params.add(new NameValuePair("Action", action)); //$NON-NLS-1$
        }
        try {
            // post the parameters to the web service url and
            // get an input stream
            InputStream fin = getInputStream(streamSource, params);
            BufferedReader bin = new BufferedReader(new InputStreamReader(fin));

            String line;

            // read lines from the stream
            while ((line = bin.readLine()) != null) {

                if (stopped == true) {
                    break;
                }

                // a blank line means clear the text field
                if (line.length() == 0) {
                    field.setText(""); //$NON-NLS-1$
                    continue;
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.getString(BUNDLE_NAME, "InfoDataItem.7") + line); //$NON-NLS-1$
                }
                // if we get the EOT character 0x04 then reset the text to
                // this line
                if (line.charAt(0) == DataItem.EOT) {
                    line = line.substring(1);
                    // replace the text with this new text
                    field.setText(line);
                } else {
                    // otherwise append this new text to the existing contents
                    field.append("\n" + line); //$NON-NLS-1$
                }
            }

        } catch (MalformedURLException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "InfoDataItem.9") + streamSource); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "InfoDataItem.10") + streamSource, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "InfoDataItem.11") + streamSource); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "InfoDataItem.12") + streamSource, ex); //$NON-NLS-1$
        } catch (Exception ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "InfoDataItem.13") + streamSource); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "InfoDataItem.14") + streamSource, ex); //$NON-NLS-1$
        }

    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */
    public String getTypeName() {
        return DataItemFactory.INFO;
    }
}
