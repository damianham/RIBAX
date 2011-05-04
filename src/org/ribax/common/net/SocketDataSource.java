package org.ribax.common.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.Socket;


import utils.types.NameValuePair;

/**
 * A class that implements a DataSource using TCP/IP sockets
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class SocketDataSource implements DataSource {

    private String url;
    private String host;
    private int port;
    @SuppressWarnings("unused")
    private String name = null;
    private Hashtable<String, String> headers = new Hashtable<String, String>();
    private static Logger LOG = Logger.getLogger(SocketDataSource.class.getName());

    public SocketDataSource(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    private void nullParameterWarning(NameValuePair pair) {
        if (pair == null) {
            LOG.warning("SocketDataSource.0");
        } else {
            LOG.warning("SocketDataSource.1" + pair.getName() + " value:" + pair.getValue());
        }
    }

    public InputStream getInputStream(ArrayList<NameValuePair> params)
            throws IOException {
        InputStream stream = null;
        OutputStream out;

        if (url.startsWith("telnet:")) {
            url = url.replaceFirst("telnet:", "http:");
        }

        try {
            URI u = new URI(url);

            host = u.getHost();
            port = u.getPort();
        } catch (URISyntaxException ex) {
            LOG.warning("SocketDataSource.6" + url);
            return null;
        }

        Socket sock = new Socket(host, port);
        stream = sock.getInputStream();
        out = sock.getOutputStream();

        if (params != null && params.size() > 0) {
            HTTPmultipart mp = new HTTPmultipart();


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
                    mp.writePart(out, part);
                } else {
                    StringPart part = new StringPart(pair.getName(), pair.getValue().toString());
                    mp.writePart(out, part);
                }

            }
        }
        return stream;
    }
}
