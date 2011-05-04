/*
 * Ribax Ajax Framework.
 * Copyright (C) 2007 Damian Hamill all rights reserved.
 * 
 */
package org.ribax.common.net;

import java.util.Random;
import java.io.UnsupportedEncodingException;

public class PartUtils {

    public static final byte[] CRLF_BYTES = getAsciiBytes("\r\n");
    /** Extra characters as a byte array */
    public static final byte[] EXTRA_BYTES = getAsciiBytes("--");
    /** Quote character as a byte array */
    public static final byte[] QUOTE_BYTES = getAsciiBytes("\"");
    /** Default content encoding chatset */
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    /** Content dispostion as a byte array */
    public static final byte[] CONTENT_DISPOSITION_BYTES = getAsciiBytes("Content-Disposition: form-data; name=");
    /** Content type header as a byte array */
    public static final byte[] CONTENT_TYPE_BYTES = getAsciiBytes("Content-Type: ");
    /** Content charset as a byte array */
    public static final byte[] CHARSET_BYTES = getAsciiBytes("; charset=");
    /** Content type header as a byte array */
    public static final byte[] CONTENT_TRANSFER_ENCODING_BYTES = getAsciiBytes("Content-Transfer-Encoding: ");
    /**
     * The pool of ASCII chars to be used for generating a multipart boundary.
     */
    private static byte[] MULTIPART_CHARS = getAsciiBytes(
            "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

    /**
     * Convert the string to a byte array.
     *
     * @param data the string to convert.
     * @return
     */
    public static byte[] getAsciiBytes(final String data) {

        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }

        try {
            return data.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new Error("ASCII support required");
        }
    }

    /**
     * Converts the specified string to a byte array.  If the charset is not 
     * supported the default system charset is used.
     *
     * @param data the string to be encoded
     * @param charset the desired character encoding
     * @return The resulting byte array.
     */
    public static byte[] getBytes(final String data, String charset) {

        if (data == null) {
            throw new IllegalArgumentException("data may not be null");
        }

        if (charset == null || charset.length() == 0) {
            throw new IllegalArgumentException("charset may not be null or empty");
        }

        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return data.getBytes();
        }
    }

    /**
     * Generates a random multipart boundary string.
     * @return
     */
    public static byte[] generateMultipartBoundary() {
        Random rand = new Random();
        byte[] bytes = new byte[32]; // a random size from 30 to 40
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)];
        }
        return bytes;
    }
}
