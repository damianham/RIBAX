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
package org.ribax.swing.extra;

import org.jdom.Element;
import org.ribax.common.Messages;
import org.ribax.swing.Configurator;

import utils.log.BasicLogger;
import utils.xml.XMLutils;

/// substance imports
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.theme.SubstanceTheme;
import org.jvnet.substance.watermark.SubstanceImageWatermark;
import org.jvnet.substance.utils.SubstanceConstants.ImageWatermarkKind;

/**
 * 
 * Configure the Substance Look and Feel library
 * 
 * @author <a href="mailto:damian@ribax.org">Damian Hamill</a>
 *
 */
public class SubstanceConfigurator implements Configurator {

    private static final String BUNDLE_NAME = "org.ribax.swing.extra.messages"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(SubstanceConfigurator.class.getName());

    /**
     * Default no arguments constructor
     */
    public SubstanceConfigurator() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.Configurator#readConfiguration(org.jdom.Element)
     */
    public void readConfiguration(Element config) {

        // set up a skin
        setupSkin(config.getChild("skin")); //$NON-NLS-1$

        // set up a theme, look in substance.jar for the names of available
        // themes
        setupTheme(config.getChild("theme")); //$NON-NLS-1$

        try {
            // set up a button shaper, look in substance.jar for the names of available
            // button shapers
            String arg = XMLutils.getElementString("buttonShaper", config); //$NON-NLS-1$
            if (arg != null) {
                SubstanceLookAndFeel.setCurrentButtonShaper(arg);
            }
        } catch (Exception ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "SubstanceConfigurator.3") + ex.getLocalizedMessage()); //$NON-NLS-1$
        }

        try {
            // set up a gradient painter, look in substance.jar for the names of available
            // gradient painters 
            String arg = XMLutils.getElementString("gradientPainter", config); //$NON-NLS-1$
            if (arg != null) {
                SubstanceLookAndFeel.setCurrentGradientPainter(arg);
            }
        } catch (Exception ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "SubstanceConfigurator.5") + ex.getLocalizedMessage()); //$NON-NLS-1$
        }

        // set up a watermark, look in substance.jar for the names of available
        // watermarks
        setupWatermark(config.getChild("watermark")); //$NON-NLS-1$

    }

    private void setupTheme(Element config) {

        if (config == null) {
            return;
        }

        try {
            String arg = XMLutils.getElementString("classname", config); //$NON-NLS-1$
            if (arg != null) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "SubstanceConfigurator.8") + arg); //$NON-NLS-1$
                SubstanceLookAndFeel.setCurrentTheme(arg);
            }

            Boolean bool = XMLutils.getElementBoolean("invert", config); //$NON-NLS-1$
            if (bool != null && bool.booleanValue()) {
                SubstanceTheme theme = SubstanceLookAndFeel.getTheme();

                SubstanceLookAndFeel.setCurrentTheme(theme.invert());
            }
            bool = XMLutils.getElementBoolean("negate", config); //$NON-NLS-1$
            if (bool != null && bool.booleanValue()) {
                SubstanceTheme theme = SubstanceLookAndFeel.getTheme();

                SubstanceLookAndFeel.setCurrentTheme(theme.negate());
            }

            Double factor = XMLutils.getElementDouble("saturate", config); //$NON-NLS-1$
            if (factor != null) {
                bool = XMLutils.getElementBoolean("saturateEverything", config); //$NON-NLS-1$
                if (bool == null) {
                    bool = new Boolean(false);
                }

                SubstanceTheme theme = SubstanceLookAndFeel.getTheme();

                SubstanceLookAndFeel.setCurrentTheme(theme.saturate(factor,
                        bool.booleanValue()));
            }

            factor = XMLutils.getElementDouble("tint", config); //$NON-NLS-1$
            if (factor != null) {
                SubstanceTheme theme = SubstanceLookAndFeel.getTheme();
                SubstanceLookAndFeel.setCurrentTheme(theme.tint(factor.doubleValue()));
            }
            factor = XMLutils.getElementDouble("tone", config); //$NON-NLS-1$
            if (factor != null) {
                SubstanceTheme theme = SubstanceLookAndFeel.getTheme();
                SubstanceLookAndFeel.setCurrentTheme(theme.tone(factor.doubleValue()));
            }
            factor = XMLutils.getElementDouble("shade", config); //$NON-NLS-1$
            if (factor != null) {
                SubstanceTheme theme = SubstanceLookAndFeel.getTheme();
                SubstanceLookAndFeel.setCurrentTheme(theme.shade(factor.doubleValue()));
            }
            factor = XMLutils.getElementDouble("shift", config); //$NON-NLS-1$
            if (factor != null) {
                SubstanceTheme theme = SubstanceLookAndFeel.getTheme();
                SubstanceLookAndFeel.setCurrentTheme(theme.hueShift(factor.doubleValue()));
            }

        } catch (Exception ex) {
            LOG.error(Messages.getString(BUNDLE_NAME, "SubstanceConfigurator.17") + ex.getLocalizedMessage()); //$NON-NLS-1$
        }

    }

    private void setupWatermark(Element config) {

        if (config == null) {
            return;
        }
        try {
            String arg = XMLutils.getElementString("image", config); //$NON-NLS-1$
            if (arg != null) {
                // create a substance image water mark with the specified URL
                // this is mutually exclusive with the watermark attribute
                LOG.debug(Messages.getString(BUNDLE_NAME, "SubstanceConfigurator.19") + arg); //$NON-NLS-1$

                SubstanceImageWatermark obj = new SubstanceImageWatermark(arg);
                SubstanceLookAndFeel.setCurrentWatermark(obj);

                arg = XMLutils.getElementString("kind", config); //$NON-NLS-1$
                if (arg != null) {
                    SubstanceLookAndFeel.setImageWatermarkKind(ImageWatermarkKind.valueOf(arg));
                }
                arg = XMLutils.getElementString("opacity", config); //$NON-NLS-1$
                if (arg != null) {
                    SubstanceLookAndFeel.setImageWatermarkOpacity(Float.parseFloat(arg));
                }

            } else {

                arg = XMLutils.getElementString("classname", config); //$NON-NLS-1$
                if (arg != null) {
                    LOG.debug(Messages.getString(BUNDLE_NAME, "SubstanceConfigurator.23") + arg); //$NON-NLS-1$
                    org.jvnet.substance.SubstanceLookAndFeel.setCurrentWatermark(arg);
                }
            }
        } catch (Exception exc) {
        }
    }

    private void setupSkin(Element config) {

        if (config == null) {
            return;
        }

        String arg = XMLutils.getElementString(Messages.getString(BUNDLE_NAME, "SubstanceConfigurator.2"), config); //$NON-NLS-1$
        if (arg != null) {
            LOG.debug(Messages.getString(BUNDLE_NAME, "SubstanceConfigurator.25") + arg); //$NON-NLS-1$
            SubstanceLookAndFeel.setSkin(arg);

        }
    }
}
