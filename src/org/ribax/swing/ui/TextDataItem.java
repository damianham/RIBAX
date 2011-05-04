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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.regex.PatternSyntaxException;

import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;

import org.jdom.Element;
import org.ribax.common.Messages;
import org.ribax.common.data.DataChangeListener;
import org.ribax.common.data.DataModel;
import org.ribax.common.validators.Validator;


import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * A DataItem that displays a single line text field.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class TextDataItem extends DataItem {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(TextDataItem.class.getName());
    /** The size of the text field (defaults to 20 characters) */
    protected int fieldSize = 20;
    /** The text field */
    protected JTextField field;

    /**
     * No argument Constructor - required
     */
    public TextDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {

        super.readDescription(di);

        Integer intval;

        // get the size of the text field
        if ((intval = XMLutils.getElementInt("width", di)) != null) //$NON-NLS-1$
        {
            fieldSize = intval.intValue();
        }

        // create the text field
        field = new JTextField(fieldSize);

        // determine whether the text field is editable
        Boolean boolval;
        if ((boolval = XMLutils.getElementBoolean("editable", di)) != null) //$NON-NLS-1$
        {
            field.setEditable(boolval.booleanValue());
        }

        if (font != null) {
            field.setFont(font);
        }

        // if no field name was specified, use a default name
        if (this.fieldname == null || this.fieldname.length() == 0) {
            this.fieldname = "TextField"; //$NON-NLS-1$
        }
        // if a data model was specified then add a listener to the model
        // to be informed of data updates
        if (model != null) {
            if (modelPath == null) {
                modelPath = getPath();
            }

            value = model.getValue(modelPath);
            model.addDataChangeListener(new DataChangeListener() {

                public void dataChanged(DataModel changedModel, String path) {

                    // check that we are interested in this data
                    if (path == null || !modelPath.equals(path)) {
                        return;
                    }

                    value = changedModel.getValue(modelPath);

                    if (value != null && field.isFocusOwner() == false) {
                        field.setText(value);
                    }
                }
            });

            // also add a key listener to the text field to update the data model if the user
            // enters a new value and presses the Enter key
            field.addKeyListener(new KeyAdapter() {

                public void keyTyped(KeyEvent e) {
                    // update the model if the user pressed Enter to finish editing
                    if (e.getKeyChar() == '\n') {
                        updateModel();
                    }
                }
            });
        }

        // layout the GUI components
        layoutComponents();
    }

    /**
     * If the user has modified the data contents then update the model
     */
    private void updateModel() {
        if (model == null) {
            return;
        }

        String newValue = field.getText();

        // check the data has changed
        if (value != null && value.equals(newValue)) {
            return;
        }

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("fieldname", fieldname)); //$NON-NLS-1$
        try {
            model.setElement(modelPath, newValue, params);
        } catch (Exception ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "TextDataItem.2") + ex.getMessage()); //$NON-NLS-1$
        }
    }

    /**
     * Layout the GUI components.
     */
    protected void layoutComponents() {

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "TextDataItem.5") + name + ": " + description); //$NON-NLS-1$ //$NON-NLS-2$
        }
        // clear out any previous GUI components
        removeAll();

        // set the layout to BorderLayout
        setLayout(new BorderLayout());

        // add the label and text field in a sub panel
        JPanel p1 = new JPanel();
        if (bgcolour != null) {
            p1.setBackground(bgcolour);
        }
        p1.add(new JLabel(title));
        p1.add(field);

        // set the field tooltip
        if (tooltip != null && tooltip.length() > 1) {
            field.setToolTipText(tooltip);
        }

        // if a value was specified for the field then initialise the field with the value
        if (value != null) {
            field.setText(value);
        }

        add(p1, BorderLayout.CENTER);
    }

    /**
     * Set the tooltip on the text field.
     * 
     * @param tooltip The tooltip to set.
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
        field.setToolTipText(tooltip);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */
    public void setData(Element node) {
        Element e;

        if ((e = node.getChild(fieldname)) != null) {
            field.setText(e.getText());
        }
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
        if (field.getText().length() == 0) {
            return null;
        }

        return new NameValuePair(fieldname, field.getText());
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
        return null;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#updateData(java.util.ArrayList, java.lang.String)
     */

    public void updateData(ArrayList<NameValuePair> params, String action) {
        if (model != null) {
            updateModel();
        } else {
            refresh(params, action);
        }
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

        // if there is no stream source specified then do nothing
        if (streamSource == null) {
            return;
        }

        // create a background process to stream the data
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
     * Stream data from a web service and update the text field.
     * 
     * @param params a set of parameters for DataItems to submit to web services.
     * @param action the action string associated with this event.
     */
    protected void loadItemData(ArrayList<NameValuePair> params, String action) {

        stopped = false;

        if (action != null) {
            params.add(new NameValuePair("Action", action)); //$NON-NLS-1$
        }
        try {
            // get an inputstream from the streaming data source
            InputStream fin = getInputStream(streamSource, params);
            BufferedReader bin = new BufferedReader(new InputStreamReader(fin));

            String line;

            // read lines from the source continously until stopped by an event
            while ((line = bin.readLine()) != null) {

                // an event has occurred that means we have to stop streaming
                if (stopped == true) {
                    break;
                }

                // clear the text field if the line is empty
                if (line.length() == 0) {
                    field.setText(""); //$NON-NLS-1$
                    continue;
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.getString(BUNDLE_NAME, "TextDataItem.9") + line); //$NON-NLS-1$
                }
                // check for the EOT character 0x04, if the line starts with EOT
                // we replace the field contents otherwise we append to the field
                if (line.charAt(0) == DataItem.EOT) {
                    line = line.substring(1);
                    // replace the text with this new text
                    field.setText(line);
                } else {
                    // append this new text
                    field.setText(field.getText() + " " + line); //$NON-NLS-1$
                }
            }

        } catch (MalformedURLException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "TextDataItem.11") + streamSource); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "TextDataItem.12") + streamSource, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "TextDataItem.13") + streamSource); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "TextDataItem.14") + streamSource, ex); //$NON-NLS-1$
        } catch (Exception ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "TextDataItem.15") + streamSource); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "TextDataItem.16") + streamSource, ex); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */
    public String getTypeName() {
        return DataItemFactory.TEXT;
    }


    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#validateContents()
     */
    public boolean validateContents() {
        if (validators == null) {
            return true;
        }

        String text = field.getText();

        // iterate through the set of validators
        for (Validator v : validators) {

            // check this validator regular expresssion
            if (v.validate(text) == false) {
                String errMsg = v.getErrorMessage();

                if (errMsg != null) {
                    errorMessage(errMsg);
                }
                return false;
            }

        }

        return true;
    }
}
