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
package org.ribax.common.data;

import java.util.Hashtable;

import org.jdom.Element;
import org.ribax.common.Messages;

import utils.log.BasicLogger;
import utils.xml.XMLutils;

/**
 * A singleton class that manages data models.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class DataModelManager {

    private static final String BUNDLE_NAME = "org.ribax.common.data.messages"; //$NON-NLS-1$
    /* The singleton instance of the Data Model manager */
    private static DataModelManager instance = null;
    /* The set of models managed by the Data Model manager */
    private Hashtable<String, DataModel> models = new Hashtable<String, DataModel>();
    private static BasicLogger LOG = new BasicLogger(DataModelManager.class.getName());

    /**
     * private constructor
     */
    private DataModelManager() {
    }

    /**
     * Get the single instance of the Data Model manager.
     * 
     * @return the Data Model manager instance.
     */
    public synchronized static DataModelManager getInstance() {
        if (instance == null) // first time in this method - create a new manager
        {
            instance = new DataModelManager();
        }

        return instance;
    }

    /**
     * Add a new Data Model to the set of models.
     * 
     * @param name the mode name.
     * @param model the data model.
     */
    public synchronized void addModel(String name, DataModel model) {
        models.put(name, model);
    }

    /**
     * Find a data model for a given name.
     * 
     * @param name the name of the data model to find.
     * @return the named data model or null if a model by the given name didn't exist.
     */
    public DataModel getModel(String name) {
        return models.get(name);
    }

    /**
     * Get the specification of a data model from an XML Element tree.
     * @param node the Element tree containing the data model.
     * @param path the full pathname of the data model.
     */
    public void readModel(Element node, String path) {

        DataModel model;

        // get the classname of the class that handles this data model
        String classname = XMLutils.getElementString("classname", node); //$NON-NLS-1$

        try {
            // create a new instance of the data model class
            if (classname != null) {
                Class t = Class.forName(classname);
                model = (DataModel) t.newInstance();
            } else {
                // default to a JDomDataModel
                model = new JDomDataModel();
            }
        } catch (ClassCastException ex) {
            LOG.error(classname + Messages.getString(BUNDLE_NAME, "DataModelManager.1"), ex); //$NON-NLS-1$
            model = new JDomDataModel();
        } catch (IllegalAccessException ex) {
            LOG.error(classname + Messages.getString(BUNDLE_NAME, "DataModelManager.2")); //$NON-NLS-1$
            model = new JDomDataModel();
        } catch (InstantiationException ex) {
            LOG.error(classname + Messages.getString(BUNDLE_NAME, "DataModelManager.3")); //$NON-NLS-1$
            model = new JDomDataModel();
        } catch (ClassNotFoundException ex) {
            LOG.error(classname + Messages.getString(BUNDLE_NAME, "DataModelManager.4")); //$NON-NLS-1$
            model = new JDomDataModel();
        }

        // read the data model specification
        model.readDescription(node);

        // set the path and name of this data model instance
        if (path.length() > 0) {
            path += "/" + model.name; //$NON-NLS-1$
        } else {
            path += model.name;
        }

        // add the new data model to the set of data models
        if (getModel(path) != null) {
            LOG.error(Messages.getString(BUNDLE_NAME, "DataModelManager.6") + path + Messages.getString(BUNDLE_NAME, "DataModelManager.7")); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            addModel(path, model);
        }

    }
}
