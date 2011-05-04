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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import utils.log.BasicLogger;
import utils.types.NameValuePair;

import org.ribax.common.Messages;
import org.ribax.common.net.NetUtils;

/**
 * Utility methods for working with XML documents and Element trees.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class DataUtils {

    private static final String BUNDLE_NAME = "org.ribax.common.data.messages"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(DataUtils.class.getName());

    /**
     * Get an Element that is an XML document from a web service.
     * 
     * @param url the URL of the web service.
     * @param params a set of parameters to post to the web service.
     * @param name the name of the calling Object (for debugging).
     * @return the XML document root Element.
     * @throws IOException if an IO error occurs.
     */
    public static Element getDocumentRoot(String url, ArrayList<NameValuePair> params, String name) throws IOException {

        try {
            // get an input stream from the web service
            InputStream in = NetUtils.getInputStream(url, params, name);

            SAXBuilder builder = new SAXBuilder();

            // build the Element tree from the XML input
            Document doc = builder.build(in);

            // return the root element
            return doc.getRootElement();

        } catch (JDOMException ex) {
            // indicates a well-formedness error
            LOG.error(Messages.getString(BUNDLE_NAME, "DataUtils.1") + url, ex);			 //$NON-NLS-1$
        } catch (IOException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "DataUtils.2") + url, ex); //$NON-NLS-1$
        }
        return null;
    }

    /*
     * Check a block of text to see if one of a set of end tags occur in the text.  The 
     * array of end tag names are the simple names without the '</>' as in 'html' rather
     * than '</html>'.
     * 
     * @param block the text to examine
     * @param triggers an array of end tag names
     * @return
     */
    private static boolean triggersInBlock(String block, String[] triggers) {

        for (String s : triggers) {
            String endTrigger = "</" + s + ">"; //$NON-NLS-1$ //$NON-NLS-2$
            if (block.indexOf(endTrigger) >= 0) {
                return true;
            }
        }
        return false;
    }

    /** Read lines of text from an input stream looking for any 1 of a set of end tags.  The
     * end tags are in simple name format, i.e. 'html' rather than '</html>'.
     * 
     * @param url the URL of a web service that we are reading from
     * (only used when printing exceptions to the log).
     * @param inStream an open input stream from the web service.
     * @param triggers an array of tag names
     * @return
     */
    public static Element readElementBlock(String url, BufferedReader bin,
            String[] triggers) {

        try {

            SAXBuilder builder = new SAXBuilder();

            // a string buffer that holds the input until an end tag is found
            StringBuffer buf = new StringBuffer();

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "DataUtils.5")); //$NON-NLS-1$
            }
            String line;

            // read lines from the input stream looking for the end tags
            do {
                line = bin.readLine();
                buf.append(line);
            } while (line != null && triggersInBlock(line, triggers) == false);

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "DataUtils.6")); //$NON-NLS-1$
            }
            // create an XML document from the input text
            Document doc = builder.build(new StringReader(buf.toString()));

            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "DataUtils.7")); //$NON-NLS-1$
            }
            // return the document root Element
            return doc.getRootElement();

        } catch (IOException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "DataUtils.8") + url, ex); //$NON-NLS-1$
        } catch (JDOMException ex) {
            // indicates a well-formedness error
            LOG.error(Messages.getString(BUNDLE_NAME, "DataUtils.9") + url, ex); //$NON-NLS-1$
        }

        return null;
    }

    /**
     * Get the Element tree as a XML String.
     *
     * @param root the Element to convert into a String.
     * @return the XML representation of the Element tree.
     */
    public static String getDataAsString(Element root) {
        byte[] ba = getDataAsByteArray(root);
        if (ba == null) {
            return null;
        }

        return new String(ba);
    }

    /**
     * Get the XML representation of an Element tree as a byte array.
     *
     * @param root the Element to convert into a byte array.
     * @return the XML representation of the Element tree.
     */
    public static byte[] getDataAsByteArray(Element root) {
        XMLOutputter op = new XMLOutputter();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            op.output(root, baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return null;
        }
        return baos.toByteArray();
    }

    /**
     * Get the XML representation of an Element tree as an InputStream.
     *
     * @param root the Element to convert into an InputStream.
     * @return the InputStream from which an XML representation of the
     * Element tree can be read.
     */
    public static InputStream getDataAsInputStream(Element root) {
        return new ByteArrayInputStream(getDataAsByteArray(root));
    }
}
