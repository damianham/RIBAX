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
package org.ribax.common;

import java.io.*;
import java.util.Hashtable;
import java.util.Properties;

/**
 * A class that holds objects that we use to store configuration
 * information and various settings i.e. global parameters.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class RIBAXConfig implements Serializable {

    public static final long serialVersionUID = 1;
    /** This is a singleton, there is only 1 instance of this class in any application  */
    private static RIBAXConfig instance = null;
    /** The hashtable of settings managed by this class */
    private Hashtable<String, Object> settings = new Hashtable<String, Object>();

    /*
     * A private constructor called by getInstance() if the instance has not already
     * been created.
     */
    private RIBAXConfig() {
        // add some defaults;
        settings.put(ConfigStrings.DISPLAY_HELP_TEXT, new Boolean(true));
        settings.put(ConfigStrings.USE_PROXY, new Boolean(false));
        settings.put(ConfigStrings.LANGUAGE, new String("en"));
    }

    /**
     * Get an instance of this class.
     * 
     * @return the singleton instance of this class.
     */
    public synchronized static RIBAXConfig getInstance() {
        if (instance == null) {
            instance = new RIBAXConfig();
        }

        return instance;
    }

    /**
     * Get a value for a given key.
     * 
     * @param name the key that identifies the value.
     * @return the value for the given key or null if the key did not exist.
     */
    public Object getValue(String name) {
        return settings.get(name);
    }

    /**
     * Get a value for a given key and return a default value if the given key 
     * does not exist.
     * 
     * @param name the key that identifies the value.
     * @param defaultValue a value to return if the key does not exist.
     * @return the value for the given key or the default value.
     */
    public Object getValue(String name, Object defaultValue) {
        Object o = settings.get(name);

        if (o == null) {
            return defaultValue;
        }

        return o;
    }

    /**
     * Store a value for a given key.
     * 
     * @param name the key that identifies the value.
     * @param value the value to store.
     * @return the original value for the key or null if there was no original value.
     */
    public synchronized Object setValue(String name, Object value) {
        Object o = settings.put(name, value);
        return o;
    }

    /**
     * Map a short name to the full class name.  The map is stored in a properties
     * object which is set with the setValue(String name, Object value) method.
     * 
     * @param className the short name for the class.
     * @return the full class name.
     */
    public static String mapClassName(String className) {
        RIBAXConfig config = RIBAXConfig.getInstance();

        // get the properties from the global config
        Properties properties = (Properties) config.getValue(ConfigStrings.PROPERTIES);

        if (properties == null) {
            return className;
        }

        // get the full classname from properties
        String newName = properties.getProperty(className);

        if (newName != null) {
            return newName;
        }

        return className;
    }
}
