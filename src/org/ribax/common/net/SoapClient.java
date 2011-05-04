package org.ribax.common.net;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


import utils.types.NameValuePair;

public class SoapClient {

    private static Logger LOG = Logger.getLogger(SoapClient.class.getName());
    // TODO eliminate
    private boolean debug = true;

    public SoapClient() {
        // We also have to set our logger to log finer-grained
        // messages
        LOG.setLevel(Level.FINE);
    }

    private void writeStringParam(PrintWriter pw, String paramName, String value) {
        pw.write("<ns1:" + paramName + " xsi:type=\"xsd:string\">");
        pw.write(value);
        pw.write("</ns1:" + paramName + ">\n");
    }

    private void writeIntParam(PrintWriter pw, String paramName, int value) {
        pw.write("<ns1:" + paramName + " xsi:type=\"xsd:int\">");
        pw.write(value);
        pw.write("</ns1:" + paramName + ">\n");
    }

    private void writeCalendarParam(PrintWriter pw, String paramName, Calendar value) {
        pw.write("<ns1:" + paramName + ">");
        pw.write(value.toString());
        pw.write("</ns1:" + paramName + ">\n");
    }

    public InputStream getInputStream(String serviceEndpoint,
            String methodName, String soapAction,
            ArrayList<NameValuePair> params)
            throws IOException {

        InputStream stream = null;
        URLConnection conn = null;

        URL hp = new URL(serviceEndpoint);

        conn = hp.openConnection();

        // we don't want the connection to stay alive
        conn.setRequestProperty("Connection", "close");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);

        if (soapAction != null) {
            conn.setRequestProperty("SOAP-ACTION", soapAction);
        }

        conn.setRequestProperty("Content-Type", "text/xml");

        // create a ByteArrayOutputStream and write the parameters to the stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PrintWriter pw = new PrintWriter(baos);

        // write the soap preamble
        //pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        //pw.write("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\">");

        pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        pw.write("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n");

        //write soap envelope - body
        pw.write("<soap:Body xmlns:ns1=\"http://ribax.org/\">\n");


        // write method parameters


        if (params != null && params.size() > 0) {
            pw.write("<ns1:" + methodName + ">");
            try {
                for (NameValuePair pair : params) {
                    Object obj = pair.getValue();

                    if (obj instanceof String) {
                        writeStringParam(pw, pair.getName(), (String) obj);
                    } else if (obj instanceof Calendar) {
                        writeCalendarParam(pw, pair.getName(), (Calendar) obj);
                    } else if ("file".equals(pair.getMimeType())) {
                        // TODO encode and add the file
                        // use Soap with attachements
                    }
                }
            } catch (Exception e) {
                throw new IOException("error writing SOAP parameters");
            }
            pw.write("</ns1:" + methodName + ">\n");
        } else {
            pw.write("<ns1:" + methodName + "/>\n");
        }

        pw.write("</soap:Body>\n</soap:Envelope>\n");

        pw.flush();
        pw.close();

        // set the content length header
        conn.setRequestProperty("Content-Length", "" + baos.size());

        // now we can open the output stream
        OutputStream ostream = conn.getOutputStream();

        byte[] pp = baos.toByteArray();

        String pps = baos.toString();
        System.out.println(pps);

        ostream.write(pp);

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
                throw new IOException("Post failed, response=" + hc.getResponseMessage());
            }
        } else if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection hc = (HttpsURLConnection) conn;

            int status = hc.getResponseCode();
            outlength = hc.getContentLength();

            if (status == HttpsURLConnection.HTTP_OK) {
                stream = conn.getInputStream();
            } else {
                throw new IOException("Post failed, response=" + hc.getResponseMessage());
            }

        } else {
            stream = conn.getInputStream();
        }

        if (debug) {
            if (outlength < 0) {
                outlength = 8192;
            }

            StringBuffer sb = new StringBuffer((int) outlength);
            String line;
            BufferedReader bin = new BufferedReader(new InputStreamReader(stream));

            while ((line = bin.readLine()) != null) {
                sb.append(line);
            }
            String res = sb.toString();
            LOG.fine(res);
            stream = new ByteArrayInputStream(res.getBytes());

        }
        return stream;
    }

    public Element getDocumentRootFromStream(String serviceEndpoint,
            String methodName, String soapAction,
            ArrayList<NameValuePair> params) throws IOException {
        try {
            // get an input stream from the web service
            InputStream in = getInputStream(serviceEndpoint, methodName, soapAction, params);

            SAXBuilder builder = new SAXBuilder();

            // build the Element tree from the XML input
            Document doc = builder.build(in);

            // return the root element
            return doc.getRootElement();

        } catch (JDOMException ex) {
            // indicates a well-formedness error
            LOG.warning("Error malformed XML data from " + serviceEndpoint + " " + ex);
        } catch (IOException ex) {
            LOG.warning("Error reading from " + serviceEndpoint + " " + ex);
        }
        return null;
    }

    public Element getDocumentRoot(String serviceEndpoint,
            String methodName, String soapAction,
            ArrayList<NameValuePair> params) {

        try {
            MessageFactory mfac = MessageFactory.newInstance();
            SOAPFactory soapFactory = SOAPFactory.newInstance();

            SOAPMessage message = mfac.createMessage();
            SOAPPart soapPart = message.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

            /// the header is not used to detach it
            header.detachNode();

            // add the method name
            Name bodyName = soapFactory.createName(methodName, "m", "http://ribax.org");

            SOAPBodyElement bodyElement = body.addBodyElement(bodyName);

            // add the parameters
            for (NameValuePair p : params) {
                Object o = p.getValue();

                if (o instanceof String) {
                    Name name = soapFactory.createName(p.getName());
                    SOAPElement symbol = bodyElement.addChildElement(name);
                    symbol.addTextNode((String) o);
                } else if (o instanceof Element) {
                    Element el = (Element) o;
                    XMLOutputter op = new XMLOutputter();

                    String xml = op.outputString(el);
                    @SuppressWarnings("unused")
                    AttachmentPart attachment = message.createAttachmentPart(xml, "text/xml");
                }
            }

            SOAPConnectionFactory factory =
                    SOAPConnectionFactory.newInstance();
            SOAPConnection connection = factory.createConnection();

            java.net.URL endpoint = new URL(serviceEndpoint);
            SOAPMessage response = connection.call(message, endpoint);

            SOAPBody soapBody = response.getSOAPBody();
            java.util.Iterator iterator =
                    soapBody.getChildElements(bodyName);
            bodyElement = (SOAPBodyElement) iterator.next();

            DOMBuilder db = new DOMBuilder();

            return db.build(bodyElement);
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SOAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public Element getSoapResponse(String serviceEndpoint,
            String methodName, String soapAction,
            ArrayList<NameValuePair> params) throws IOException {

        Element env = getDocumentRoot(serviceEndpoint, methodName, soapAction, params);

        if (env == null) {
            return null;
        }

        List<Element> content = env.getChildren();
        Element body = content.get(0);

        if (body == null) {
            return null;
        }

        List<Element> response = body.getChildren();

        Element res = response.get(0);

        if ("Fault".equals(res.getName())) {
            // a soap fault occured
			/*
             *       <SOAP-ENV:Fault>
            <faultcode>SOAP-ENV:Server</faultcode>
            <faultstring>Server Error</faultstring>
            <detail>
            <e:myfaultdetails xmlns:e="Some-URI">
            <message>
            My application didn't work
            </message>
            <errorcode>
            1001
            </errorcode>
            </e:myfaultdetails>
            </detail>
            </SOAP-ENV:Fault>
             */
            throw new IOException("Error making soap request " + res.toString());
        }
        return res;

    }
}
