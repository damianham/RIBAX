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
package org.ribax.common.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import utils.types.NameValuePair;

import org.ribax.common.ConfigStrings;
import org.ribax.common.Messages;
import org.ribax.common.RIBAXConfig;
import org.ribax.common.log.RequestLog;
import org.ribax.common.log.ResponseLog;
import org.ribax.common.net.HTTPmultipart;
import org.ribax.common.net.Part;

/**
 * A class that implements the DataSource interface using the standard
 * URLConnection class
 *
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class WebDataSource implements DataSource {

    private String url;
    private Hashtable<String, String> headers = new Hashtable<String, String>();
    private static final String BUNDLE_NAME = "org.ribax.common.net.messages"; //$NON-NLS-1$
    private RIBAXConfig config = RIBAXConfig.getInstance();
    RequestLog requestLog;
    ResponseLog responseLog;
    String name = "WebDataSource"; //$NON-NLS-1$

    public WebDataSource(String url, String name) {
        this.url = url;
        if (name != null) {
            this.name = name;
        }

        String useragent = (String) config.getValue(ConfigStrings.USERAGENT);
        if (useragent != null) {
            setHeader("User-Agent", useragent); //$NON-NLS-1$
        } else {
            setHeader("User-Agent", "RIBAX framework see http://www.ribax.org"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        requestLog = RequestLog.getInstance(WebDataSource.class.getName());
        responseLog = ResponseLog.getInstance(WebDataSource.class.getName());
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    private void nullParameterWarning(NameValuePair pair) {
        if (pair == null) {
            requestLog.warn(Messages.getString(BUNDLE_NAME, "WebDataSource.11")); //$NON-NLS-1$
        } else {
            requestLog.warn(Messages.getString(BUNDLE_NAME, "WebDataSource.12") + pair.getName() + " value:" + pair.getValue());		 //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public InputStream getInputStream(ArrayList<NameValuePair> params)
            throws IOException {

        InputStream stream = null;
        URLConnection conn = null;

        // send the form data
        if (requestLog.isDebugEnabled(name)) {
            requestLog.debug(name, Messages.getString(BUNDLE_NAME, "WebDataSource.14") + url); //$NON-NLS-1$
        }
        URL hp = new URL(url);

        conn = hp.openConnection();

        // we don't want the connection to stay alive
        conn.setRequestProperty("Connection", "close"); //$NON-NLS-1$	

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);

        // add  headers
        for (Enumeration<String> e = headers.keys(); e.hasMoreElements();) {
            String name = e.nextElement();
            conn.setRequestProperty(name, headers.get(name));
        }

        HTTPmultipart mp = null;

        if (params != null && params.size() > 0) {
            mp = new HTTPmultipart();
            String boundary = new String(mp.getBoundary());
            conn.setRequestProperty("Boundary", boundary); //$NON-NLS-1$
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=\"" + boundary + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        }

        ArrayList<Part> content = new ArrayList<Part>();

        int length = 0;

        // determine the content length and save the parts to the content list

        if (params != null && params.size() > 0) {

            try {
                for (NameValuePair pair : params) {

                    if (pair.getName() == null || pair.getValue() == null) {
                        nullParameterWarning(pair);
                        continue;
                    }

                    if ("file".equals(pair.getMimeType())) {
                        File f = new File(pair.getValue().toString());

                        if (f == null) {
                            nullParameterWarning(pair);
                            continue;
                        }
                        FilePart part = new FilePart(pair.getName(), f);
                        content.add(part);
                        length += mp.getContentLength(part);
                    } else {
                        StringPart part = new StringPart(pair.getName(), pair.getValue().toString());
                        content.add(part);
                        length += mp.getContentLength(part);
                    }

                }
            } catch (Exception e) {
                throw new IOException("error reading CGI parameters");
            }

            // add the length of the final boundary
            length += PartUtils.EXTRA_BYTES.length + mp.getBoundary().length
                    + PartUtils.EXTRA_BYTES.length + PartUtils.CRLF_BYTES.length;
        }

        // set the content length header
        conn.setRequestProperty("Content-Length", "" + length);

        // now we can open the output stream
        OutputStream ostream = conn.getOutputStream();

        if (params != null && params.size() > 0) {

            // write the parts to the output stream
            for (Part part : content) {
                mp.writePart(ostream, part);
            }

            // write the final boundary
            ostream.write(PartUtils.EXTRA_BYTES);
            ostream.write(mp.getBoundary());
            ostream.write(PartUtils.EXTRA_BYTES);
            ostream.write(PartUtils.CRLF_BYTES);
        }

        ostream.close();

        // if response logging is enabled then read the data into a 
        // string buffer and return a ByteArrayInputStream maximum size
        // is 128KB
        long outlength = 0;

        if (conn instanceof HttpURLConnection) {
            HttpURLConnection hc = (HttpURLConnection) conn;

            int status = hc.getResponseCode();
            outlength = hc.getContentLength();

            if (status == HttpURLConnection.HTTP_OK) {
                stream = conn.getInputStream();
            } else {
                throw new IOException(Messages.getString(BUNDLE_NAME, "WebDataSource.24") + hc.getResponseMessage()); //$NON-NLS-1$
            }
        } else if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection hc = (HttpsURLConnection) conn;

            int status = hc.getResponseCode();
            outlength = hc.getContentLength();

            if (status == HttpsURLConnection.HTTP_OK) {
                stream = conn.getInputStream();
            } else {
                throw new IOException(Messages.getString(BUNDLE_NAME, "WebDataSource.25") + hc.getResponseMessage()); //$NON-NLS-1$
            }

        } else {
            stream = conn.getInputStream();
        }


        if (outlength < (128 * 1024)) {
            if (outlength < 0) {
                outlength = 8192;
            }

            if (responseLog.isDebugEnabled(name)) {
                StringBuffer sb = new StringBuffer((int) length);
                String line;
                BufferedReader bin = new BufferedReader(new InputStreamReader(stream));

                responseLog.debug(name, Messages.getString(BUNDLE_NAME, "WebDataSource.26") + url); //$NON-NLS-1$

                while ((line = bin.readLine()) != null) {
                    sb.append(line);
                }
                String res = sb.toString();
                responseLog.debug(name, res);
                stream = new ByteArrayInputStream(res.getBytes());
            }
        }

        return stream;
    }
}
