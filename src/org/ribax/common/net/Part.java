/*
 * Ribax Ajax Framework.
 * Copyright (C) 2007 Damian Hamill all rights reserved.
 * 
 */
package org.ribax.common.net;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Part {

    /** Name of the part. */
    protected String name;
    /** Content type of the part. */
    protected String contentType = null;
    /** Content encoding of the part. */
    protected String charSet = null;
    /** The transfer encoding. */
    protected String transferEncoding = null;

    /**
     * Return the name of this part.
     * @return The name.
     */
    protected String getName() {
        return this.name;
    }

    /**
     * Returns the content type of this part.
     * @return the content type, or <code>null</code> to exclude the content type header
     */
    protected String getContentType() {
        return this.contentType;
    }

    /**
     * Returns the length of this part including the headers
     * @return the length of this part including the headers
     */
    protected abstract int getContentLength();

    /**
     * Returns the length of the headers
     * @return
     */
    protected int getHeaderLength() {

        int len = PartUtils.CONTENT_DISPOSITION_BYTES.length + PartUtils.QUOTE_BYTES.length
                + PartUtils.getAsciiBytes(getName()).length + PartUtils.QUOTE_BYTES.length;

        String contentType = getContentType();
        if (contentType != null) {
            len += PartUtils.CRLF_BYTES.length + PartUtils.CONTENT_TYPE_BYTES.length
                    + PartUtils.getAsciiBytes(contentType).length;

            String charSet = getCharSet();
            if (charSet != null) {
                len += PartUtils.CHARSET_BYTES.length + PartUtils.getAsciiBytes(charSet).length;
            }
        }
        String transferEncoding = getTransferEncoding();
        if (transferEncoding != null) {
            len += PartUtils.CRLF_BYTES.length + PartUtils.CONTENT_TRANSFER_ENCODING_BYTES.length
                    + PartUtils.getAsciiBytes(transferEncoding).length;
        }
        return len;
    }

    /**
     * Return the character encoding of this part.
     * @return the character encoding, or <code>null</code> to exclude the character 
     * encoding header
     */
    protected String getCharSet() {
        return this.charSet;
    }

    /**
     * Return the transfer encoding of this part.
     * @return the transfer encoding, or <code>null</code> to exclude the transfer encoding header
     */
    protected String getTransferEncoding() {
        return transferEncoding;
    }

    /**
     * Write the content type header to the specified output stream
     * @param out The output stream
     * @throws IOException If an IO problem occurs.
     */
    protected void writeContentTypeHeader(OutputStream out) throws IOException {

        String contentType = getContentType();
        if (contentType != null) {
            out.write(PartUtils.CRLF_BYTES);
            out.write(PartUtils.CONTENT_TYPE_BYTES);
            out.write(PartUtils.getAsciiBytes(contentType));
            String charSet = getCharSet();
            if (charSet != null) {
                out.write(PartUtils.CHARSET_BYTES);
                out.write(PartUtils.getAsciiBytes(charSet));
            }
        }
    }

    /**
     * @param out
     * @throws IOException
     */
    protected void writeDisposition(OutputStream out) throws IOException {
        // override to write part specific disposition header
    }

    /**
     * Write the part headers to the specified output stream
     * @param out The output stream
     * @throws IOException If an IO problem occurs.
     */
    protected void writeHeaders(OutputStream out) throws IOException {

        // write the content disposition header to the output stream
        out.write(PartUtils.CONTENT_DISPOSITION_BYTES);
        out.write(PartUtils.QUOTE_BYTES);
        out.write(PartUtils.getAsciiBytes(getName()));
        out.write(PartUtils.QUOTE_BYTES);

        // write any additional disposition for the part to the output stream
        writeDisposition(out);

        // write the content type header to the output stream
        writeContentTypeHeader(out);

        // write the content transfer encoding header to the output stream
        String transferEncoding = getTransferEncoding();
        if (transferEncoding != null) {
            out.write(PartUtils.CRLF_BYTES);
            out.write(PartUtils.CONTENT_TRANSFER_ENCODING_BYTES);
            out.write(PartUtils.getAsciiBytes(transferEncoding));
        }

    }

    /**
     * Write the data to the specified output stream
     * @param out The output stream
     * @throws IOException If an IO problem occurs.
     */
    protected abstract void writeData(OutputStream out) throws IOException;
}
