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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JLabel;

import org.jdom.Element;
import org.ribax.common.Messages;

import utils.types.NameValuePair;

/**
 * A parameter for uploading a file.  Provides a path entry box and a
 * browse button which opens a file chooser.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class FileParameter extends Parameter {

    public static final long serialVersionUID = 1;
    private static final String BUNDLE_NAME = "org.ribax.swing.parameters.messages"; //$NON-NLS-1$
    private JTextField field;
    private Box box;
    private JButton button = new JButton(Messages.getString(BUNDLE_NAME, "FileParameter.0")); //$NON-NLS-1$
    private JFileChooser chooser;
    private int fieldSize = 20;
    static String sep = System.getProperty("file.separator"); //$NON-NLS-1$
    @SuppressWarnings("unused")
    private int layoutType = BoxLayout.Y_AXIS;

    public FileParameter(int layoutType) {
        this.layoutType = layoutType;

        if (layoutType == BoxLayout.Y_AXIS) {
            box = Box.createVerticalBox();
        } else {
            box = Box.createHorizontalBox();
        }
    }

    protected void readDescription(Element param) {
        super.readDescription(param);

        if (width > 0) {
            fieldSize = width;
        }

        layoutComponents();
    }

    private void layoutComponents() {

        field = new JTextField(fieldSize);
        box.add(new JLabel(title));
        box.add(field);

        if (tooltip != null && tooltip.length() > 0) {
            field.setToolTipText(tooltip);

            if (description != null) {
                button.setToolTipText(description);
            }
        }

        if (value != null) {
            field.setText(value);
        }

        if (font != null) {
            field.setFont(font);
            button.setFont(font);
        }

        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                button.setEnabled(false);

                String fpath = field.getText();

                try {
                    if (fpath.length() > 0) {
                        int pos;

                        // set the starting directory to the path value
                        if ((pos = fpath.lastIndexOf(sep)) > 0) {
                            fpath = fpath.substring(0, pos);
                        }
                        File f = new File(fpath);
                        chooser = new JFileChooser(f);
                    } else {
                        chooser = new JFileChooser();
                    }

                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                    int returnVal = chooser.showOpenDialog(box);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File f = chooser.getSelectedFile();
                        field.setText(f.getAbsolutePath());
                    }
                } catch (SecurityException ex) {
                    errorMessage(Messages.getString(BUNDLE_NAME, "FileParameter.2")); //$NON-NLS-1$
                }
                button.setEnabled(true);
            }
        });

        box.add(button);
        add(box);
    }

    public NameValuePair getNameValuePair() {
        String text = field.getText();

        if (text.length() > 0) {
            value = text;
        }

        return super.getNameValuePair();
    }
}
