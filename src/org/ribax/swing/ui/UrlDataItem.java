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
package org.ribax.swing.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.html.HTMLDocument;

import javax.swing.text.SimpleAttributeSet;

import org.jdom.Element;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

import org.ribax.common.Messages;
import org.ribax.common.data.DataChangeListener;
import org.ribax.common.data.DataModel;
import org.ribax.swing.data.EditorDataLoader;
import org.ribax.swing.*;

/**
 * A DataItem that embeds the contents of a web page.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 * 
 * @see {link java.swing.JEditorPane}
 */
public class UrlDataItem extends DataItem {

    public static final long serialVersionUID = 1;
    private static BasicLogger LOG = new BasicLogger(UrlDataItem.class.getName());
    protected String url = null;
    protected JEditorPane editorPane = new JEditorPane();
    private JScrollPane editorScrollPane;

    /**
     * No argument Constructor - required
     */
    public UrlDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element di) {

        super.readDescription(di);

        // the value attribute specifies the URL of the HTML web page
        this.url = value;

        // if a data model was specified then add a listener to the model
        // to be informed of changes to the web page URL
        if (model != null) {
            if (modelPath == null) {
                modelPath = getPath();
            }

            Object node = model.getElement(modelPath);

            if (node == null) {
                LOG.error(Messages.getString(BUNDLE_NAME, "UrlDataItem.0") + modelPath); //$NON-NLS-1$
            }
            // initialise the web page URL from the data model
            if (node != null && node instanceof Element) {
                setData((Element) node);
            }

            // add a listener to the data model
            model.addDataChangeListener(new DataChangeListener() {

                public void dataChanged(DataModel model, String path) {

                    // check that we are interested in this data
                    if (path == null || !modelPath.equals(path)) {
                        return;
                    }

                    Object node = model.getElement(modelPath);

                    // update the web page URL
                    if (node != null && node instanceof Element) {
                        setData((Element) node);
                    }
                }
            });
        }

        // layout the GUI components
        layoutComponents();
    }

    /**
     * Layout the GUI components.
     */
    private void layoutComponents() {

        // clear any previous contents
        removeAll();

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "UrlDataItem.1") + title); //$NON-NLS-1$
        }
        // use BorderLayout
        setLayout(new BorderLayout());

        // set the tooltip on the c
        if (tooltip != null && tooltip.length() > 0) {
            editorPane.setToolTipText(tooltip);
        }

        // set the default font on the editor pane
        if (font != null) {
            editorPane.setFont(font);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "UrlDataItem.2") + title); //$NON-NLS-1$
        }
        // rendered HTML web pages are not editable
        editorPane.setEditable(false);

        // initialise the editor pane with a default string
        if (value != null && value.toLowerCase().startsWith("http")) //$NON-NLS-1$
        {
            editorPane.setText(Messages.getString(BUNDLE_NAME, "UrlDataItem.4")); //$NON-NLS-1$
        }
        // embed the editor pane in a scrollpane
        editorScrollPane = new JScrollPane(editorPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        if (prefWidth > 0 || prefHeight > 0) {
            Dimension d = new Dimension(prefWidth == 0 ? 400 : prefWidth,
                    prefHeight == 0 ? 400 : prefHeight);
            editorScrollPane.setPreferredSize(d);
        }

        // add the scrollpane to the center of the window
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "UrlDataItem.5") + title); //$NON-NLS-1$
        }
        add(editorScrollPane, BorderLayout.CENTER);

        // add a hyperlink listener to the editorpane to respond to
        // whenthe user clicks on a hyperlink on a web page in the editor pane
        editorPane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent event) {
                // check that a hyperlink was clicked
                if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    JEditorPane pane = (JEditorPane) event.getSource();

                    // check if the event was an internal frame link event
                    if (event instanceof HTMLFrameHyperlinkEvent) {
                        HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) event;
                        HTMLDocument doc = (HTMLDocument) pane.getDocument();
                        doc.processHTMLFrameHyperlinkEvent(evt);
                    } else {
                        // otherwise get the URL that was clicked
                        url = event.getURL().toString();

                        javax.swing.text.Element el = event.getSourceElement();
                        AttributeSet set = el.getAttributes();

                        // get the attributes for the link and if a target was specified
                        // of '_blank' then open th URL in a new window
                        // othewise replace the web page in this DataItem
                        Object anchor = set.getAttribute(javax.swing.text.html.HTML.Tag.A);
                        Object target = null;

                        // get the target attribute
                        if (anchor instanceof SimpleAttributeSet) {
                            target = ((SimpleAttributeSet) anchor).getAttribute(javax.swing.text.html.HTML.Attribute.TARGET);
                        }

                        if (target == null) // no target so load the new page in the editor pane
                        {
                            refresh(null, null);
                        } else if (target instanceof String) {
                            String t = (String) target;

                            // check to see if the target was '_blank'
                            if (t.toLowerCase().equals("_blank") || t.toLowerCase().equals("_top")) //$NON-NLS-1$ //$NON-NLS-2$
                            // load the new web page in an external browser
                            {
                                URLopener.openURL(url, url);
                            } else // otherwise load the new page in the editor pane
                            {
                                refresh(null, null);
                            }
                        } else // anything else load the new page in the editor pane
                        {
                            refresh(null, null);
                        }

                    }
                }
            }
        });

    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#loadData(java.util.ArrayList, java.lang.String)
     */
    public void loadData(ArrayList<NameValuePair> params, String action) {

        // do nothing if we have already loaded or the url or stream source are null
        if (loaded || (url == null && streamSource == null)) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "UrlDataItem.8") + title); //$NON-NLS-1$
        }
        refresh(params, action);

        loaded = true;
    }
    // a helper class to load and stream data in the background
    private EditorDataLoader loader = null;

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#refresh(java.util.ArrayList, java.lang.String)
     */
    public void refresh(ArrayList<NameValuePair> params, String action) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "UrlDataItem.9") + title); //$NON-NLS-1$
        }
        // make sure the editor pane is setup for HTML
        HTMLEditorKit kit = new HTMLEditorKit();
        editorPane.setEditorKit(kit);

        if (streamSource != null) {
            // stream HTML to the editor pane in blocks of <html>.... </html>
            url = streamSource;

            // stop any previous loader
            if (loader != null) {
                loader.stopStreaming();
            }

            // create a new streaming loader
            loader = new EditorDataLoader(this, editorPane, url, params, true, this.name);

        } else // create a web page loader to load the web page once into the editor pane
        {
            loader = new EditorDataLoader(this, editorPane, url, params, false, this.name);
        }

    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#close()
     */
    public void close() {

        // stop any streaming loader
        if (loader != null) {
            loader.stopStreaming();
        }

    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getComponent()
     */
    public Component getComponent() {
        return editorScrollPane;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getNameValuePair()
     */
    public NameValuePair getNameValuePair() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getParameters()
     */

    public ArrayList<NameValuePair> getParameters() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getAllElements()
     */

    public ArrayList<NameValuePair> getAllElements() {
        return getParameters();
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getSelectedElements()
     */

    public ArrayList<NameValuePair> getSelectedElements() {
        return null;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#setData(org.jdom.Element)
     */

    public void setData(Element node) {

        // get the URL from the Elements 'value' attribute
        this.url = XMLutils.getElementString("value", node); //$NON-NLS-1$

        // if this DataItem is currently visible then refresh the contents
        // which will have the effect of loading the new page into the editor pane
        if (isShowing()) {
            refresh(null, "LoadData"); //$NON-NLS-1$
        }
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#updateData(java.util.ArrayList, java.lang.String)
     */

    public void updateData(ArrayList<NameValuePair> params, String action) {
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.DataItem#getTypeName()
     */

    public String getTypeName() {
        return DataItemFactory.URL;
    }
}
