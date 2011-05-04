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

import java.lang.reflect.Method;
import java.util.Properties;

import org.jdom.Element;

import org.ribax.common.ConfigStrings;
import org.ribax.common.Messages;
import org.ribax.common.RIBAXConfig;

import utils.xml.XMLutils;

import utils.log.BasicLogger;
import utils.log.ILogger;

/**
 * A Class that provides a method to read a DataItem description and
 * create an instance of the DataItem.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class DataItemFactory implements DataItemNames {

    private static final String BUNDLE_NAME = "org.ribax.swing.ui.messages"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(DataItemFactory.class.getName());

    /**
     * Determine the classname to load for the DataItem, create a new instance of the class
     * and call the instance.readDescription() method.
     * 
     * @param di the Element tree containing the description of the DataItem
     * @param parent the DataItem container that contains this DataItem
     * @return the newly created DataItem or null if an error occurred
     */
    public static DataItem readComponent(Element di, DataItem parent) {

        /* try a classname attribute first, this allows the user to specify any fully
         * qualified classname as the DataItem class to load.
         */
        String className = XMLutils.getElementString("classname", di); //$NON-NLS-1$

        // fall back on a short type name which is mapped to a fully qualified classname
        if (className == null) {
            className = XMLutils.getElementString("type", di); //$NON-NLS-1$
        }
        try {

            // special processing for TEXTAREA and REQUEST|RESPONSE LOG
            if (TEXTAREA.equals(className)) {

                // a TEXTAREA is an InfoDataItem with editing enabled
                InfoDataItem idi = new InfoDataItem();

                idi.setParent(parent);
                idi.editable = true;
                idi.readDescription(di);

                return idi;

            } else if (REQUESTLOG.equals(className)) {
                // a REQUESTLOG is an instance of LogDataItem
                LogDataItem ldi = new LogDataItem(ILogger.REQUEST, parent);

                ldi.readDescription(di);
                return ldi;

            } else if (RESPONSELOG.equals(className)) {
                // a RESPONSELOG is an instance of LogDataItem
                LogDataItem ldi = new LogDataItem(ILogger.RESPONSE, parent);

                ldi.readDescription(di);
                return ldi;

            } else {

                // map the short name to the full classname
                className = RIBAXConfig.mapClassName(className);

                try {
                    Class provider = Class.forName(className);

                    Class[] args = {org.jdom.Element.class};

                    // check this class is a DataItem
                    Method m = provider.getMethod("readDescription", args); //$NON-NLS-1$

                    if (m != null) {
                        // create a new instance
                        DataItem jc = (DataItem) provider.newInstance();

                        jc.setParent(parent);

                        // tell the new DataItem instance to read it's description from
                        // the Element tree
                        jc.readDescription(di);

                        return jc;
                    }
                } catch (NoSuchMethodException ex) {
                    LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.3"), ex); //$NON-NLS-1$

                } catch (NullPointerException ex) {
                    LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.4"), ex); //$NON-NLS-1$

                } catch (SecurityException ex) {
                    LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.5"), ex); //$NON-NLS-1$

                } catch (ClassNotFoundException ex) {
                    LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.6"), ex); //$NON-NLS-1$

                } catch (ClassCastException ex) {
                    LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.7"), ex); //$NON-NLS-1$

                } catch (Exception ex) {
                    LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.8"), ex); //$NON-NLS-1$

                }
                // if an exception occurred
                return null;
            }
        } catch (Exception ex) {
            // catch any exception so the application will still work
            // in particular we want to catch ClassNotFoundException
            LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.9"), ex); //$NON-NLS-1$
        }
        return null;
    }
}
