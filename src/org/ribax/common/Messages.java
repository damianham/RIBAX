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

import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A class for Internationalisation with message bundles.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class Messages {

    private static String locale = "en";
    private static Hashtable<String, ResourceBundle> bundles = new Hashtable<String, ResourceBundle>();

    private Messages() {
    }

    /**
     * Get the string for a given key.
     *
     * @param bundleName the name of the message bundle.
     * @param key the key to find.
     * @return the message string for the given key.  If the key is not found the
     * key is returned surrounded by '!'.
     */
    public static String getString(String bundleName, String key) {
        ResourceBundle bundle = bundles.get(bundleName);

        if (bundle == null) {
            bundle = ResourceBundle.getBundle(bundleName, new Locale(locale));
            bundles.put(bundleName, bundle);
        }
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
     * Get the current locale.
     *
     * @return the current locale
     */
    public static String getLocale() {
        return locale;
    }

    /**
     * Set the current locale.  See the Javadoc for See ResourceBundle for
     * details of how the locale is used.
     * 
     * @param language the locale to set
     *
     * @see ResourceBundle
     */
    public static void setLocale(String locale) {
        Messages.locale = locale;

        // dump the old language properties
        Messages.bundles = new Hashtable<String, ResourceBundle>();
    }
}
