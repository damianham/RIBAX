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

import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.border.*;

/**
 * A class that displays a text string in a bordered text area
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class Description extends JTextArea {

    public static final long serialVersionUID = 1;
    /** the background colour */
    protected Color bgcolour = null;
    /** the maximum width of this Description object */
    @SuppressWarnings("unused")
    private int maxwidth;
    // A set of borders for Description objects
    static Border lowered = BorderFactory.createLoweredBevelBorder();
    static Border raised = BorderFactory.createRaisedBevelBorder();
    static Border compound = BorderFactory.createCompoundBorder(raised, lowered);

    /**
     * Constructor that takes the string to display in the box
     * 
     * @param description the string to display in the box
     */
    public Description(String description) {
        this(description, null, 0);
    }

    /**
     * Constructor that takes the string to display in the box and the maximum width
     * 
     * @param description the string to display in the box
     * @param maxwidth the maximum width
     */
    public Description(String description, int maxwidth) {
        this(description, null, maxwidth);
    }

    /**
     *  Constructor that takes the string to display in the box and a background colour
     *  
     * @param description the string to display in the box
     * @param bgcolour the background colour
     */
    public Description(String description, Color bgcolour) {
        this(description, bgcolour, 0);
    }

    /**
     * Constructor that takes the string to display in the box, a background colour and
     * a maximum width 
     * @param description the string to display in the box
     * @param bgcolour the background colour
     * @param maxwidth the maximum width
     */
    public Description(String description, Color bgcolour, int maxwidth) {

        // set the text in the base class
        super(description);

        if (bgcolour != null) {
            this.bgcolour = bgcolour;
        }

        this.maxwidth = maxwidth;

        init();
    }

    /**
     *  Initialise the display.  Create the border and set the wrapping and margins.
     */
    private void init() {
        if (bgcolour != null) {
            setBackground(bgcolour);
        }

        Border empty = new EmptyBorder(5, 5, 5, 5);
        setBorder(BorderFactory.createCompoundBorder(empty, compound));
        setLineWrap(true);
        setWrapStyleWord(true);
        setMargin(new Insets(5, 5, 5, 5));

    }
}
