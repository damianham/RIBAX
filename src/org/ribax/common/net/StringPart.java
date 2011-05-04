/*
 * Ribax Ajax Framework.
 * Copyright (C) 2007 Damian Hamill all rights reserved.
 * 
 */
package org.ribax.common.net;

import java.io.IOException;
import java.io.OutputStream;

public class StringPart extends Part {

    /** Contents of this StringPart. */
    private byte[] content;

    /**
     * Constructor.
     *
     * @param name The name of the part
     * @param value the string to post
     */
    public StringPart(String name, String value) {
        this.name = name;
        this.content = value.getBytes();

        /** Default content encoding of string parameters. */
        this.contentType = "text/plain";
        /** Default charset of string parameters*/
        this.charSet = "US-ASCII";
        /** Default transfer encoding of string parameters*/
        this.transferEncoding = "8bit";

        content = PartUtils.getBytes(value, getCharSet());
    }

    /* (non-Javadoc)
     * @see org.ribax.datasources.Part#getContentLength()
     */
    protected int getContentLength() {
        return getHeaderLength() + content.length;
    }

    /**
     * Writes the data to the given OutputStream.
     * @param out the OutputStream to write to
     * @throws IOException if there is a write error
     */
    protected void writeData(OutputStream out) throws IOException {
        out.write(content);
    }
}
