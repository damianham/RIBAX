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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;

import org.jdom.Element;
import org.ribax.common.Messages;
import org.ribax.common.validators.Validator;

import utils.log.BasicLogger;
import utils.xml.XMLutils;

/**
 * A DataItem that restricts data entry to numbers only
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class NumberDataItem extends TextDataItem {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(NumberDataItem.class.getName());
    /** A minimum value for the entered number */
    private float minimum = 0;
    /** A maximum value for the entered number */
    private float maximum = 0;
    /** An error message to display to the user during validation if the entered number 
     * is out of range
     */
    private String rangeError = null;

    /**
     * No argument Constructor - required
     */
    public NumberDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TextDataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {

        super.readDescription(di);

        // create a number formatted text field
        NumberFormat nf = NumberFormat.getInstance();
        field = new JFormattedTextField(nf);

        field.setColumns(fieldSize);

        Element e;

        // get the range specification
        if ((e = di.getChild("range")) != null) { //$NON-NLS-1$
            String min, max;

            // get the minimum and maximum
            if ((min = XMLutils.getElementString("minimum", e)) != null //$NON-NLS-1$
                    && (max = XMLutils.getElementString("maximum", e)) != null) { //$NON-NLS-1$
                try {
                    minimum = Float.parseFloat(min);
                    maximum = Float.parseFloat(max);
                } catch (NumberFormatException ex) {
                    LOG.error(Messages.getString(BUNDLE_NAME, "NumberDataItem.3") + ex); //$NON-NLS-1$
                }
            }
            // if no errorMessage is defined then use the error message from the first validator
            if ((rangeError = XMLutils.getElementString("errorMessage", e)) == null) { //$NON-NLS-1$
                // use the first error message if defined
                if (validators != null && validators.size() > 0) {
                    Validator v = validators.firstElement();
                    rangeError = v.getErrorMessage();
                }
            }
        }

        layoutComponents();

        // add an action to the field to catch the keypressed event
        field.addKeyListener(new KeyAdapter() {

            /** Handle the key-pressed event from the text field. */
            public void keyPressed(KeyEvent e) {

                //You should only rely on the key char if the event
                //is a key typed event.
                if (e.getID() != KeyEvent.KEY_TYPED) {
                    return;
                }

                if (validateContents() == false) {
                }
            }
        });
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TextDataItem#getTypeName()
     */
    public String getTypeName() {
        return DataItemFactory.NUMBER;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.TextDataItem#validateContents()
     */
    public boolean validateContents() {

        if (!super.validateContents()) {
            return false;
        }

        // check that a range has been specified
        if (maximum != minimum) {
            try {
                Float f = Float.parseFloat(field.getText());

                // test the entered value is within the range
                if ((f < minimum && minimum != 0) || (f > maximum && maximum != 0)) {
                    if (rangeError != null) {
                        errorMessage(rangeError);
                    }
                    return false;
                }
            } catch (NumberFormatException ex) {
                // try removing non digits from the field but allow signs and exponent etc.
                String s = field.getText();
                s = s.replaceAll("[^0-9\\.\\-\\+dDeEfF]", ""); //$NON-NLS-1$ //$NON-NLS-2$
                try {
                    Float f = Float.parseFloat(field.getText());
                    if ((f < minimum && minimum != 0) || (f > maximum && maximum != 0)) {
                        if (rangeError != null) {
                            errorMessage(rangeError);
                        }
                        return false;
                    }
                } catch (NumberFormatException ex2) {
                    // I think we are screwed, let the back end handle it
                }
            }
        }

        return true;
    }
}
