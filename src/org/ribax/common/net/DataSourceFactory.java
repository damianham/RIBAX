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
 * 
 * MODIFICTION RECORD
 * 
 * October 15/06 - Michael Hollis
 * 	Modified this file to enable the use of file:// for local description files.
 * 
 * 
 */
package org.ribax.common.net;

/**
 * Factory to provide a data source based on the url
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 * @author Michael Hollis
 */
public class DataSourceFactory {

    public static DataSource getDataSource(String url, String name) {
        if (url == null) {
            return null;
        }

        String burl = url.trim().toLowerCase();

        /*
         * we are using ApacheHTTPdataSource rather than the Sun version because
         * the Sun version has problems if the content-length header is not given
         */
        if (burl.startsWith("http:") || burl.startsWith("https:")) {
            return new WebDataSource(url, name);
        } else if (burl.startsWith("telnet:")) {
            return new SocketDataSource(url, name);
        } else if (burl.startsWith("file:")) {
            return new FileDataSource(url, name);
        } else if (burl.startsWith("test:")) {
            return new TestDataSource(name);
        }

        return null;
    }
}
