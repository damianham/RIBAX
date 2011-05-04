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
 * MODIFCATION RECORD
 * 
 */
package org.ribax.common.net;

/**
 * FileDataSource enables the use of a local file to be used as a description
 * source of the program
 * 
 * @author Michael Hollis
 * @version 1.0
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.net.URI;

import utils.types.NameValuePair;

public class FileDataSource implements DataSource {

    private String uri;
    
    @SuppressWarnings("unused")
    private String name = "FileDataSource";

    public FileDataSource(String url, String name) {
        this.name = name;
        this.uri = url;
    }

    public InputStream getInputStream(ArrayList<NameValuePair> params) throws IOException {

        try {
            URI uri = new URI(this.uri);

            File f = new File(uri);

            FileInputStream in = new FileInputStream(f);
            return (InputStream) in;
        } catch (Exception e) {
            throw new IOException(e.getLocalizedMessage());
        }

    }

    public void setHeader(String name, String value) {
        // nop
    }
}
