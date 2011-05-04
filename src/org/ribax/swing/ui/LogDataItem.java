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
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JScrollPane;

import org.jdom.Element;

import utils.log.BasicLogger;
import utils.log.ILogger;
import utils.types.NameValuePair;
import utils.ui.LoggerPanel;
import utils.xml.XMLutils;

import org.ribax.common.log.RequestLog;
import org.ribax.common.log.ResponseLog;

/**
 * A DataItem for request and response logging
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class LogDataItem extends DataItemAdaptor {

    public static final long serialVersionUID = 1;
    /** The Logging panel which displays the log messages */
    private LoggerPanel lp;
    /** The logger instance which writes to the Logging panel */
    private BasicLogger log;
    /** the type of log required */
    private int type = -1;
    /** filename of a persistant log */
    private String filename = null;

    /**
     * Constructor that takes the type of log required and the parent Data Item.
     * 
     * @param type the type of log required
     * @param parent the parent Data Item
     */
    public LogDataItem(int type, DataItem parent) {
        setParent(parent);
        this.type = type;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {
        Element e;

        super.readDescription(di);

        // get a filename for persistent storage 
        filename = XMLutils.getElementString("logFile", di);

        layoutComponents();

        // read logging levels after creating the GUI components
        if ((e = di.getChild("levels")) != null) {
            readLevels(e);
        }
    }

    /**
     * Layout the GUI components
     */
    private void layoutComponents() {
        removeAll();
        setLayout(new BorderLayout());

        // create a new Logger panel
        lp = new LoggerPanel();

        if (font != null) {
            lp.setFont(font);
        }

        Dimension size = new Dimension(800, 600);

        lp.setPreferredSize(size);

        // depending on the type of log required tie this panel to an instance of either
        // request or response log
        if (type == ILogger.REQUEST) {
            log = RequestLog.getInstance(name, lp);
        } else {
            log = ResponseLog.getInstance(name, lp);
        }

        // open a log file if a filename was given
        if (filename != null) {
            log.openLog(filename);
        }

        // add the logger panel in a scroll pane
        add(new JScrollPane(lp), BorderLayout.CENTER);
    }

    /**
     * Read the component logging levels.
     * 
     * @param root the XML Element node containing the logging level definitions.
     */
    protected void readLevels(Element root) {
        log.readLevels(root);
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItemAdaptor#getComponent()
     */
    public Component getComponent() {
        return lp;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItemAdaptor#refresh(java.util.ArrayList, java.lang.String)
     */
    public void refresh(ArrayList<NameValuePair> params, String action) {
        // clear the log entries
        lp.clear();
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItemAdaptor#getTypeName()
     */
    public String getTypeName() {
        if (type == ILogger.REQUEST) {
            return DataItemFactory.REQUESTLOG;
        } else {
            return DataItemFactory.RESPONSELOG;
        }
    }
}
