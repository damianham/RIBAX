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
package org.ribax.swing;

import javax.swing.*;

import org.ribax.common.Messages;

import utils.ui.StatusReporter;

import java.awt.*;
import java.awt.event.*;

/**
 * Application wrapper for the RIBAX Applet.  This class is used when the framework is
 * used in Application mode.  This class simply embeds the applet which does all the work.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class RIBAXApplication extends JFrame implements StatusReporter.Reporter {

    public static final long serialVersionUID = 1;
    private static final String BUNDLE_NAME = "org.ribax.swing.messages"; //$NON-NLS-1$
    // command line arguments
    String[] m_Arguments;
    /** The embedded RIBAXApplet */
    RIBAXApplet papp;
    /** A status line for messages */
    protected JTextField m_statusLine = new JTextField();

    /**
     * The class constructor creates the applet and initialises the window
     * 
     * @param args the set of command line arguments
     */
    public RIBAXApplication(String[] args) {

        super(Messages.getString(BUNDLE_NAME, "RIBAXApplication.0")); //$NON-NLS-1$

        m_Arguments = args;

        // don't allow editing of the status line - its for information only
        m_statusLine.setEnabled(false);

        // create the interface 
        papp = new RIBAXApplet(this);

        // add it to the window
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(papp, BorderLayout.CENTER);
        getContentPane().add(m_statusLine, BorderLayout.SOUTH);

        // if the user clicks on the status line take them to the ribax.org website
        m_statusLine.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                openRibaxWebsite();
            }
        });

        // run the init method of the applet, when the applet is embedded in a browser 
        // this method is called by the browser automatically
        papp.init();

        // display the window and set its size to something reasonable
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dim.height = dim.height - 250;
        dim.width = dim.width - 250;
        setSize(dim);
        setVisible(true);
    }

    /**
     *  open a web browser with the ribax website page displayed.
     */
    private void openRibaxWebsite() {
        papp.openURL("http://www.ribax.org/", "RIBAX"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Displays a message in the Status Line.
     * 
     * @see {@link utils.ui.StatusReporter.Reporter#reportStatus(java.lang.String)}
     */
    public synchronized void reportStatus(String status) {
        Graphics g = m_statusLine.getGraphics();

        if (g != null) {
            m_statusLine.setText(status);
            m_statusLine.update(g);
        }
    }

    /**
     * Returns the named parameter from the command line. It is assumed that the parameter
     * appears on the command line preceded by a "-" and that the value follows
     * as the next argument.
     *
     * @param name the name of the parameter to retrieve
     * @return the parameter if found or null if not found
     */
    public String getParameter(String name) {
        String param = null;
        for (int i = 0; i < m_Arguments.length; i++) {
            if (m_Arguments[i].equals("-" + name)) { //$NON-NLS-1$
                param = m_Arguments[i + 1];
                break;
            }
        }
        return param;
    }

    /**
     * Entry point - create an application window.
     */
    public static void main(String args[]) {

        new RIBAXApplication(args);

    }

    /**
     * Closes the application.
     */
    public void close() {
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public void windowOpened(WindowEvent e) {
    }

    /*
     * maybe we should tell all parts of the application about these events ?
     */
    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
