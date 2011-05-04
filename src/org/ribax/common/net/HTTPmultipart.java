/*
 * Ribax Ajax Framework.
 * Copyright (C) 2007 Damian Hamill all rights reserved.
 * 
 */
package org.ribax.common.net;

import java.io.OutputStream;
import java.io.IOException;

/**
 * A class to write a HTTP parameter in multi part format
 *
 * @version <tt>$Revision: 1.1 $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class HTTPmultipart {

    private byte[] boundary = null;

    public HTTPmultipart() {
        boundary = PartUtils.generateMultipartBoundary();
    }

    public byte[] getBoundary() {
        return boundary;
    }

    public int getContentLength(Part part) {
        int len = PartUtils.EXTRA_BYTES.length + boundary.length
                + PartUtils.CRLF_BYTES.length;

        len += part.getContentLength();

        len += (PartUtils.CRLF_BYTES.length * 3);

        return len;
    }

    public void writePart(OutputStream out, Part part) throws IOException {

        // write the boundary to the output stream
        out.write(PartUtils.EXTRA_BYTES);
        out.write(boundary);
        out.write(PartUtils.CRLF_BYTES);

        part.writeHeaders(out);

        // write the end of header to the output stream
        out.write(PartUtils.CRLF_BYTES);
        out.write(PartUtils.CRLF_BYTES);

        // write the part data to the output stream
        part.writeData(out);

        // write the end data to the output stream.
        out.write(PartUtils.CRLF_BYTES);
    }
}
