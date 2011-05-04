/*
 * Ribax Ajax Framework.
 * Copyright (C) 2007 Damian Hamill all rights reserved.
 * 
 */
package org.ribax.common.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilePart extends Part {

    /* Attachment's file name */
    protected static final String FILE_NAME = "; filename=";
    private File file;

    public FilePart(String name, File f) {
        this.name = name;
        this.file = f;

        /* Default content encoding of file attachments. */
        this.contentType = "application/octet-stream";
        /* Default charset of file attachments. */
        this.charSet = "ISO-8859-1";
        /* Default transfer encoding of file attachments. */
        this.transferEncoding = "binary";
    }

    /* (non-Javadoc)
     * @see org.ribax.datasources.Part#getContentLength()
     */
    protected int getContentLength() {
        return getHeaderLength() + (int) file.length() + dispositionLength();
    }

    /**
     * Determine the length of the disposition header.
     *
     * @return the length of the disposition header
     */
    private int dispositionLength() {
        int len = 0;

        if (file != null) {
            len += PartUtils.getAsciiBytes(FILE_NAME).length
                    + PartUtils.QUOTE_BYTES.length + PartUtils.getAsciiBytes(file.getName()).length
                    + PartUtils.QUOTE_BYTES.length;
        }

        return len;
    }

    /**
     * Write the disposition header to the output stream.
     *
     * @param out The output stream.
     * @throws IOException if an IO problem occurs.
     */
    protected void writeDisposition(OutputStream out) throws IOException {

        if (file != null) {
            out.write(PartUtils.getAsciiBytes(FILE_NAME));
            out.write(PartUtils.QUOTE_BYTES);
            out.write(PartUtils.getAsciiBytes(file.getName()));
            out.write(PartUtils.QUOTE_BYTES);
        }

    }

    /**
     * Write the data in the file to the specified stream.
     *
     * @param out The output stream.
     * @throws IOException if an IO problem occurs.
     */
    protected void writeData(OutputStream out) throws IOException {

        if (file.length() == 0) {

            // this file contains no data, so there is nothing to send.
           
            return;
        }

        byte[] tmp = new byte[4096];
        InputStream instream = new FileInputStream(file);
        try {
            int len;
            while ((len = instream.read(tmp)) >= 0) {
                out.write(tmp, 0, len);
            }
        } finally {
            // we're done with the stream, close it
            instream.close();
        }

    }
}
