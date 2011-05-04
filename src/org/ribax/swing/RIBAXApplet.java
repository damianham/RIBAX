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
package org.ribax.swing;

import java.applet.AppletContext;
import java.awt.Component;
import java.awt.BorderLayout;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

import org.ribax.common.ConfigStrings;
import org.ribax.common.Messages;
import org.ribax.common.RIBAXConfig;
import org.ribax.common.data.DataModelManager;
import org.ribax.common.log.RequestLog;
import org.ribax.common.log.ResponseLog;
import org.ribax.common.net.DataSource;
import org.ribax.common.net.DataSourceFactory;
import org.ribax.swing.ui.*;

import utils.ui.StatusReporter;
import utils.log.BasicLogger;
import utils.log.ILogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * The main file for RIBAX.  The RIBAXApplet class extends JApplet and does all the work
 * of creating the user interface.  This class can be embedded in a web browser or created by
 * the RIBAXApplication class when running the framework in stand alone application mode.
 *
 * <p>RIBAX is a client side presentation layer tool for websites,
 * online services and software systems.  Instead of a succession of
 * http requests and responses this utility submits small amounts of
 * data to the server thus eliminating a large amount of network
 * traffic and improving the user experience, especially over slow
 * networks.
 * 
 * <p>Upon initialisation we download a description of the GUI interface from the given URL
 * and create the user interface.  The entry point is the init() method.
 * @see {@link org.ribax.swing.RIBAXApplet#init()}
 * <p>
 * created 17:50 January 29th 2005
 * <p>
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class RIBAXApplet extends JApplet implements StatusReporter.Reporter, URLopener.Opener {

    public static final long serialVersionUID = 1;
    private static final String BUNDLE_NAME = "org.ribax.swing.messages"; //$NON-NLS-1$
    /** A reference to a parent application if the applet is run in an application */
    protected RIBAXApplication parentApplication = null;
    /** The main panel where the gui is loaded */
    protected JPanel mainPanel = new JPanel();
    /** The set of parameters from the environment - base paraemters */
    protected ArrayList<NameValuePair> baseParams = new ArrayList<NameValuePair>();
    /** A set of properties read from the ribax.properties resource */
    private Properties properties = new Properties();
    /** The URL of the file that contains the top level of the RIBAX application */
    private String descriptionURL = null;
    /** The set of RootFolders, each of which is an application */
    private Vector<RootFolder> folders = new Vector<RootFolder>();
    /** A set of messages to display in the status bar */
    private Vector<String> messages = new Vector<String>();
    /** A global configuration set accessible by all parts of the application */
    private RIBAXConfig config = RIBAXConfig.getInstance();
    private static BasicLogger LOG = new BasicLogger(RIBAXApplet.class.getName());

    /**
     * Constructor called when the applet is hosted in an application.
     * 
     * @param parent the parent application
     */
    public RIBAXApplet(RIBAXApplication parent) {
        super();
        this.parentApplication = parent;
    }

    /**
     * Constructor called when the applet is hosted in a web browser.
     */
    public RIBAXApplet() {
        super();
    }

    /**
     * Read the properties from the ribax.properties resource.
     */
    private void readProperties() {
        String path = "/resources/ribax.properties"; //$NON-NLS-1$

        try {
            // try to read the file from the local file system
            File f = new File(path);

            BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));

            properties.load(in);

            in.close();
        } catch (Exception ex) {
            // that didn't work so try to read it from the jar the applet was loaded from
            try {
                BufferedInputStream in = new BufferedInputStream(
                        RIBAXApplet.class.getResourceAsStream(path));

                properties.load(in);

                in.close();

            } catch (IOException ex2) {
                LOG.warn(Messages.getString(BUNDLE_NAME, "RIBAXApplet.3"), ex2); //$NON-NLS-1$
            }
        }

        /*
         * add the properties to the global configuration settings so other parts of 
         * the application can reach them
         */
        config.setValue(ConfigStrings.PROPERTIES, properties);

        // set the log level if it is defined in the properties
        String str;
        if ((str = properties.getProperty(ConfigStrings.LOG_LEVEL)) != null) {
            config.setValue(ConfigStrings.LOG_LEVEL, str);
        }
    }

    private void readConfigProperties(Element props) {
        /*
         * iterate through the children of this node, all children are 
         * elements whose name is the config property name and text is the value
         */
        List<Element> children = props.getChildren();
        Iterator<Element> iterator = children.iterator();
        while (iterator.hasNext()) {
            Element el = iterator.next();

            config.setValue(el.getName(), el.getText());

        }
    }
    /** The logger instance which writes to the Logging panel */
    @SuppressWarnings("unused")
    private ILogger reqlog, resplog;

    private void readLogging(Element logging, String typename) {

        ILogger log;
        Element e;

        if ("REQUEST".equals(typename)) {
            log = reqlog = RequestLog.getInstance(null);
        } else {
            log = resplog = ResponseLog.getInstance(null);
        }

        // read logging levels after creating the GUI components 
        if ((e = logging.getChild("levels")) != null) {
            log.readLevels(e);
        }
    }

    /**
     * Setup the platform look and feel with the class specified in the 
     * lookAndFeel parameter.
     * 
     * @param lookAndFeel
     */
    private void setupLookAndFeel(String lookAndFeel) {

        if (lookAndFeel == null) // use the default look and feel for this system
        {
            lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        }

        try {
            UIManager.setLookAndFeel(lookAndFeel);

        } catch (Exception ex) {
            // some problem so revert to the system default laf
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex2) {
            }
        }
    }

    /**
     * Load and run a configurator class that implements the Configurator interface.
     * 
     * @param className the class name of the configurator
     * @param config the Element tree containing the configuration
     * 
     * @see {@link org.ribax.swing.Configurator}
     * 
     */
    private void runConfigurator(String className, Element config) {

        try {
            Class provider = Class.forName(className);

            /*
             * the class must contain a readConfiguration method which takes an 
             * Element tree as a parameter
             */
            Class[] args = {org.jdom.Element.class};

            // check this class is a Configurator
            Method m = provider.getMethod("readConfiguration", args); //$NON-NLS-1$

            if (m != null) {
                // create an instance of the class
                Configurator cfg = (Configurator) provider.newInstance();

                // call the readConfiguration() method passing the Element tree
                cfg.readConfiguration(config);
            }
        } catch (NoSuchMethodException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "RIBAXApplet.5"), ex); //$NON-NLS-1$

        } catch (NullPointerException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "RIBAXApplet.6"), ex); //$NON-NLS-1$

        } catch (SecurityException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "RIBAXApplet.7"), ex); //$NON-NLS-1$

        } catch (ClassNotFoundException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "RIBAXApplet.8"), ex); //$NON-NLS-1$

        } catch (ClassCastException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "RIBAXApplet.9"), ex); //$NON-NLS-1$

        } catch (Exception ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "RIBAXApplet.10"), ex); //$NON-NLS-1$

        }
    }

    /**
     * Read a configuration Element tree and run one or more Configurators.
     * 
     * @param cfg the Element tree contain the definition of one or more Configurators
     * and their associated configuration data
     */
    private void readConfiguration(Element cfg) {

        /*
         * iterate through the children of this node, all children are either configurator
         * elements or a lookandFeelClassName element
         */
        List<Element> children = cfg.getChildren();
        Iterator<Element> iterator = children.iterator();
        while (iterator.hasNext()) {
            Element el = iterator.next();

            // if the element specifies a look and feel then set it up
            if ("lookandFeelClassName".equals(el.getName())) { //$NON-NLS-1$
                setupLookAndFeel(el.getText());
            } // check for an instance of a configurator
            else if ("configurator".equals(el.getName())) { //$NON-NLS-1$

                String configClass = XMLutils.getElementString("className", el); //$NON-NLS-1$

                runConfigurator(configClass, el);
            } // read application properties
            else if ("properties".equals(el.getName())) { //$NON-NLS-1$
                readConfigProperties(el);
            } else if ("logging".equals(el.getName())) { //$NON-NLS-1$
                readLogging(el, el.getAttributeValue("type"));
            }
        }
    }

    /**
     * The entry point to this class and the method that is called either by a web browser
     * or an application hosting this applet.  This method gets the arguments from the
     * environment, configures the look and feel and calls loadInterface() to download and
     * create the GUI.
     * 
     * @see {@link org.ribax.swing.RIBAXApplet#loadInterface()}
     * @see {@link java.applet.Applet#init()}
     */
    public void init() {

        // this object is the status reporter for the application
        StatusReporter.g_Reporter = this;

        // it is also the web page opener
        URLopener.g_Opener = this;

        // read the properties resource
        readProperties();

        // get the basic parameters
        String paramNames = getParameter("parameters", ""); //$NON-NLS-1$ //$NON-NLS-2$

        String pname, pvalue;

        // iterate through the parameter names
        StringTokenizer st = new StringTokenizer(paramNames, ","); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            pname = st.nextToken();

            // get the parameter from the environment
            pvalue = getParameter(pname, null);

            // if it has a value then add it to the set of base params
            if (pvalue != null) {
                baseParams.add(new NameValuePair(pname, pvalue));
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.getString(BUNDLE_NAME, "RIBAXApplet.17") + pname + "=" + pvalue); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }

        // set the base params in the global config settings
        config.setValue(ConfigStrings.BASE_PARAMS, baseParams);

        // get the all important url parameter which refers to the application 
        descriptionURL = getParameter("url", ""); //$NON-NLS-1$ //$NON-NLS-2$

        // do some browser name fiddling
        String browser = getParameter("browser", "explorer"); //$NON-NLS-1$ //$NON-NLS-2$

        if (browser != null) {
            if ("firefox".equals(browser)) { //$NON-NLS-1$
                // check the full path (on windows)
                File f = new File("c:/Program Files/Mozilla Firefox/firefox.exe"); //$NON-NLS-1$

                if (f.exists()) {
                    browser = f.getAbsolutePath();
                }
            }

            config.setValue(ConfigStrings.BROWSER, browser);
        }

        ////Layout the panel
        mainPanel.setLayout(new BorderLayout());

        /*
         * set the look and feel to a user defined class defined in the ribax.properties
         * resource file or the platform default
         */
        String lookAndFeel = properties.getProperty(ConfigStrings.LOOKANDFEEL_CLASSNAME);

        setupLookAndFeel(lookAndFeel);

        // load the interface from the description url
        loadInterface();

        // promote the framework a little bit :-)
        String available = Messages.getString(BUNDLE_NAME, "RIBAXApplet.25"); //$NON-NLS-1$

        messages.add(Messages.getString(BUNDLE_NAME, "RIBAXApplet.26")); //$NON-NLS-1$
        messages.add(Messages.getString(BUNDLE_NAME, "RIBAXApplet.27") + available); //$NON-NLS-1$
        messages.add(Messages.getString(BUNDLE_NAME, "RIBAXApplet.28")); //$NON-NLS-1$
        messages.add(Messages.getString(BUNDLE_NAME, "RIBAXApplet.29") + available); //$NON-NLS-1$

        // start a timer to cycle the messages
        startTimer();
    }

    /**
     * Load the application interface from the defined URL.  The work of downloading and
     * creating the GUI is done by the getDescription() method which creates one or more
     * RootFolder objects.  After the RootFolders have been created we use a background
     * thread to display each RootFolder's GUI component and load their data.
     * 
     * @see {@link org.ribax.swing.RIBAXApplet#getDescription(String, ArrayList)}
     * @see {@link org.ribax.swing.ui.RootFolder}
     * @see {@link org.ribax.swing.ui.RootFolder#loadRootFolder()}
     */
    private void loadInterface() {

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "RIBAXApplet.32") + descriptionURL); //$NON-NLS-1$
        }
        getDescription(descriptionURL, baseParams);

        // load the data for each root folder in a new thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                for (RootFolder f : folders) {
                    f.loadRootFolder();
                }
            }
        });

    }

    /**
     * Read the definition of a splitpane from an Element tree.  A splitpane is a container
     * for 2 RootFolders or nested splitpanes.
     * 
     * @param root the Element tree containing the elements in the splitpane
     * @return the newly created splitpane
     * @see {@link org.ribax.swing.ui.RootFolder#RootFolder(RIBAXApplet, Element)}
     */
    private Component readSplitPane(Element root) {
        // check for a split orientation
        String splitType = root.getAttributeValue("orientation"); //$NON-NLS-1$
        int splitOrientation = JSplitPane.HORIZONTAL_SPLIT;  // default to horizontal split

        // the splitType defines the orientation of the split - the default is horizontal
        if (splitType != null) {
            if ("VERTICAL".equals(splitType)) //$NON-NLS-1$
            {
                splitOrientation = JSplitPane.VERTICAL_SPLIT;
            } else {
                splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
            }
        }

        int divLocation = 0;
        String s = ""; //$NON-NLS-1$

        /*
         * the splitAt attribute defines the location of the split divider in the splitpane
         * this value is expressed in pixels
         */
        try {
            s = root.getAttributeValue("splitAt"); //$NON-NLS-1$
            if (s != null) {
                divLocation = Integer.parseInt(s);
            }
        } catch (NumberFormatException ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "RIBAXApplet.37") + s); //$NON-NLS-1$
        }
        /*
         * in a split pane there are 2 components each component can be either 
         * a split pane or a folder
         */
        List<Element> list = root.getChildren();

        // get the 2 element trees from the list
        Element e1 = list.get(0), e2 = list.get(1);

        Component c1, c2;

        // read the first component
        if ("splitpane".equals(e1.getName())) { //$NON-NLS-1$
            c1 = readSplitPane(e1);
        } else {
            // it has to be a folder then
            RootFolder rf = new RootFolder(this, e1);
            folders.add(rf);
            c1 = rf;
        }

        // read the second component
        if ("splitpane".equals(e2.getName())) { //$NON-NLS-1$
            c2 = readSplitPane(e2);
        } else {
            RootFolder rf = new RootFolder(this, e2);
            folders.add(rf);
            c2 = rf;
        }

        // create the splitpane with the 2 components
        JSplitPane splitPane = new JSplitPane(splitOrientation,
                c1, c2);

        splitPane.setOneTouchExpandable(true);

        // set the location of the divider
        if (divLocation != 0) {
            splitPane.setDividerLocation(divLocation);
        }
        return splitPane;
    }

    /**
     * Display an error message dialog to the user.
     * 
     * @param message the message to display in the dialog
     */
    private void errorMessage(String message) {
        if (parentApplication == null) {
            JOptionPane.showMessageDialog(this, message,
                    "RIBAX", //$NON-NLS-1$
                    JOptionPane.ERROR_MESSAGE);
        } else {
            LOG.error(message);
        }

    }

    /**
     * The method which downloads the application description file from the given URL 
     * and creates the application elements.  The root &lt;application&gt; tag can 
     * contain 4 kinds of sub elements
     * 
     * <ul>
     * <li> splitpane
     * <li> folder
     * <li> configuration
     * <li> dataModel
     * </ul>
     * 
     * @param url the URL that identifies the location of the top level application 
     * description file
     * @param params a set of parameters to submit to the process which serves the 
     * description file
     * 
     * @see {@link org.ribax.swing.RIBAXApplet#readSplitPane(Element)}
     * @see {@link org.ribax.swing.RIBAXApplet#readConfiguration(Element)}
     * @see {@link org.ribax.swing.ui.RootFolder#RootFolder(RIBAXApplet, Element)}
     * @see {@link org.ribax.common.data.DataModelManager#readModel(Element, String)}
     */
    private void getDescription(String url, ArrayList<NameValuePair> params) {
        // open the url from the parameter

        DataSource hac = DataSourceFactory.getDataSource(url, "init"); //$NON-NLS-1$

        if (hac == null) {
            errorMessage(Messages.getString(BUNDLE_NAME, "RIBAXApplet.42") + url + Messages.getString(BUNDLE_NAME, "RIBAXApplet.43")); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }
        try {
            InputStream fin = hac.getInputStream(params);

            // create node tree from XML 
            SAXBuilder builder = new SAXBuilder();

            Document doc = builder.build(fin);
            Element root = doc.getRootElement();

            // iterate through the children of the <application> tag
            // looking for <folder>,<splitpane>,<dataModel> & <configuration> tags

            List<Element> children = root.getChildren();
            Iterator<Element> iterator = children.iterator();
            while (iterator.hasNext()) {
                Element el = iterator.next();

                if ("splitpane".equals(el.getName())) { //$NON-NLS-1$

                    getContentPane().add(readSplitPane(el), BorderLayout.CENTER);
                } else if ("folder".equals(el.getName())) { //$NON-NLS-1$
                    folders.add(new RootFolder(this, el));

                    getContentPane().add(folders.elementAt(0), BorderLayout.CENTER);
                } else if ("configuration".equals(el.getName())) { //$NON-NLS-1$
                    readConfiguration(el);

                } else if ("dataModel".equals(el.getName())) { //$NON-NLS-1$
                    DataModelManager mgr = DataModelManager.getInstance();
                    mgr.readModel(el, ""); //$NON-NLS-1$
                }
            }

            String windowTitle = XMLutils.getElementString("title", root); //$NON-NLS-1$

            if (windowTitle != null && parentApplication != null) {
                parentApplication.setTitle(windowTitle);
            }

        } catch (MalformedURLException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "RIBAXApplet.50") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "RIBAXApplet.51") + url, ex); //$NON-NLS-1$
        } catch (IOException ex) {
            errorMessage(Messages.getString(BUNDLE_NAME, "RIBAXApplet.52") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "RIBAXApplet.53") + url, ex); //$NON-NLS-1$
        } catch (JDOMException ex) {
            // indicates a well-formedness error
            errorMessage(Messages.getString(BUNDLE_NAME, "RIBAXApplet.54") + url); //$NON-NLS-1$
            LOG.error(Messages.getString(BUNDLE_NAME, "RIBAXApplet.55") + url, ex); //$NON-NLS-1$
        }
    }

    /**
     * Find the value of a parameter - from the HTML page if an applet, 
     * and from the application if within an application.
     * 
     * @param name the name of the parameter
     * @param defValue a default value if the parameter is not specified
     */
    private String getParameter(String name, String defValue) {
        String param = ""; //$NON-NLS-1$
        if (parentApplication != null) {

            try {
                // try JNLP parameters
                if ((param = System.getProperty("jnlp." + name)) == null) //$NON-NLS-1$
                {
                    param = parentApplication.getParameter(name);
                }
            } catch (Exception ex) {
            }

        } else {
            param = getParameter(name);
        }

        if (param == null) {
            param = defValue;
        }

        return param;
    }
    /** 
     * If we recently printed a status message from the application we don't want to 
     * overwrite it straight away with a promo message so flag that we should wait for
     * the next timer tick.
     */
    private boolean waitForNexTick = false;
    /**
     * The Timer and TimerTask objects used to schedule the recurring event
     */
    private Timer timer = null;
    private TimerTask timerTask = null;

    /**
     * Sends info to the Status Line.  When embedded in a web page we use the web browser's
     * status line.  When hosted by an application we send the message to the parent 
     * application which contains a status line.
     */
    public synchronized void reportStatus(String status) {

        // if we are running in an application then send the message to the parent application
        if (parentApplication != null) {
            parentApplication.reportStatus(status);
        } else {
            // otherwise get the applet context and send the message to the browser
            AppletContext ctx = this.getAppletContext();
            ctx.showStatus(status);
        }
        waitForNexTick = true;
    }

    /**
     * Start a timer to rotate the promo messages.
     */
    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {

            public void run() {
                timerTick();
            }
        };
        timer.schedule(timerTask, 0, 10000);
    }
    int pos = 0;

    private synchronized void timerTick() {
        if (waitForNexTick == true) {
            waitForNexTick = false;
            return;
        }
        reportStatus((String) messages.get(pos++));

        if (pos >= messages.size()) {
            pos = 0;
        }
    }

    /**
     * opens the given URL in a web browser
     * 
     * @param page the URL to open
     * @param title the window title 
     */
    public void openURL(String page, String title) {

        if (page == null) {
            reportStatus(Messages.getString(BUNDLE_NAME, "RIBAXApplet.58")); //$NON-NLS-1$
            return;
        }
        if (title == null) {
            title = ""; //$NON-NLS-1$
        }
        try {
            URL url = new URL(page);

            /*
             *  if this is running as an applet then get the applet context (a handle on
             *  the web browser hosting the applet)
             */
            if (parentApplication == null) {
                AppletContext ctx = getAppletContext();
                ctx.showDocument(url, title);
            } else {
                /*
                 * otherwise the applet is hosted by an application so create a new 
                 * web browser process and pass it the URL we want displayed
                 */
                Runtime rt = Runtime.getRuntime();
                String[] cmd = new String[2];
                cmd[0] = (String) config.getValue(ConfigStrings.BROWSER);
                cmd[1] = page;
                @SuppressWarnings("unused")
                Process p = rt.exec(cmd);
            }
        } catch (java.net.MalformedURLException e) {
            LOG.warn(Messages.getString(BUNDLE_NAME, "RIBAXApplet.2"), e);  //$NON-NLS-1$
        } catch (IOException ex) {
            LOG.warn(Messages.getString(BUNDLE_NAME, "RIBAXApplet.1"), ex);  //$NON-NLS-1$
        }
    }
}
