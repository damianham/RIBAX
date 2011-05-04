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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import org.jdom.Element;
import org.ribax.common.Messages;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * A Data Item to upload a file, displays a text field for the path
 * and a browse button which opens a file chooser.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class FileDataItem extends DataItem {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(FileDataItem.class.getName());
    /** The size of the path field */
    protected static int fieldSize = 20;
    /** The path field */
    private JTextField field;
    /** The main panel for this component */
    private Box mainPanel = Box.createVerticalBox();
    /** A button to launch a file chooser */
    private JButton button = new JButton(Messages.getString(BUNDLE_NAME, "FileDataItem.0")); //$NON-NLS-1$
    /** A File chooser to select a file on the local file system */
    private JFileChooser chooser;
    /** The URL of a web service that this component will post the selected file to
     * when told to update the data
     */
    private String url = null;
    /** The file path separator character for this platform */
    static String sep = System.getProperty("file.separator"); //$NON-NLS-1$

    /**
     * No argument Constructor - required
     */
    public FileDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {

        super.readDescription(di);
        this.url = value;

        String path = null;

        /** default values for the path and text field size */
        path = XMLutils.getElementString("path", di); //$NON-NLS-1$
        Integer intval = XMLutils.getElementInt("size", di); //$NON-NLS-1$

        if (intval != null) {
            fieldSize = intval.intValue();
        }

        field = new JTextField(fieldSize);

        if (path != null) {
            field.setText(path);
        }

        if (font != null) {
            field.setFont(font);
            button.setFont(font);
        }
        layoutComponents();
    }

    /**
     * Layout the GUI coponents of this DataItem.
     */
    private void layoutComponents() {

        // remove any previous elements
        removeAll();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "FileDataItem.4") + name + ": " + description); //$NON-NLS-1$ //$NON-NLS-2$
        }
        // use a border layout for this Component
        setLayout(new BorderLayout());

        // create a sub panel to host the label, field and button
        JPanel p1 = new JPanel();
        if (bgcolour != null) {
            p1.setBackground(bgcolour);
        }

        // add the label and field
        p1.add(new JLabel(title));
        p1.add(field);

        if (tooltip != null && tooltip.length() > 0) {
            field.setToolTipText(tooltip);
        }

        // setup the button to open the file chooser
        button.setToolTipText(description);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                button.setEnabled(false);

                String fpath = field.getText();

                try {
                    if (fpath.length() > 0) {
                        // start with the specified path
                        File f = new File(fpath);
                        chooser = new JFileChooser(f);
                    } else // start with no path
                    {
                        chooser = new JFileChooser();
                    }

                    // we can only accept files since this is about sending files to 
                    // the web service
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                    // open the dialog and if the user pressed OK then update the text field
                    int returnVal = chooser.showOpenDialog(mainPanel);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File f = chooser.getSelectedFile();
                        field.setText(f.getAbsolutePath());
                    }
                } catch (SecurityException ex) {
                    LOG.warn("", ex); //$NON-NLS-1$
                    errorMessage(Messages.getString(BUNDLE_NAME, "FileDataItem.7")); //$NON-NLS-1$
                }
                button.setEnabled(true);
            }
        });

        // add the GUI components
        p1.add(button);

        mainPanel.add(p1);

        add(mainPanel, BorderLayout.CENTER);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public Component getComponent() {
        return mainPanel;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setTooltip(java.lang.String)
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
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        if (field.getText().length() == 0) {
            return null;
        }

        // return the fieldname and the file path
        return new NameValuePair(fieldname, field.getText(), "file");
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getParameters()
     */
    public ArrayList<NameValuePair> getParameters() {
        ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(getNameValuePair());

        // add this object so the file is added to the parameters
        list.add(getNameValuePair());

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
     * @see org.ribax.swing.ui.DataItem#updateData(java.util.ArrayList, java.lang.String)
     */
    public void updateData(ArrayList<NameValuePair> params, String action) {

        // post the selected file to the web service specified by the url

        if (url == null || url.length() == 0) {
            LOG.info(Messages.getString(BUNDLE_NAME, "FileDataItem.8")); //$NON-NLS-1$
            return;
        }

        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }

        // add this object so the file is added to the parameters
        params.addAll(getParameters());
        try {
            getInputStream(url, params);

        } catch (MalformedURLException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "FileDataItem.9") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "FileDataItem.10") + url, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "FileDataItem.11") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "FileDataItem.12") + url, ex); //$NON-NLS-1$
        } catch (Exception ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "FileDataItem.13") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "FileDataItem.14") + url, ex); //$NON-NLS-1$
        }
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#close()
     */

    public void close() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#loadData(java.util.ArrayList, java.lang.String)
     */
    public void loadData(ArrayList<NameValuePair> params, String action) {
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#refresh(java.util.ArrayList, java.lang.String)
     */
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#refresh(java.util.ArrayList, java.lang.String)
     */

    public void refresh(ArrayList<NameValuePair> params, String action) {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */
    public String getTypeName() {
        return DataItemFactory.FILE;
    }
}
