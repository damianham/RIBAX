package org.ribax.swing;

import org.jdom.Element;

/**
 * The interface for classes that perform configuration.  A class that implements this 
 * interface is loaded from the classpath and passed the element tree which contains 
 * the configuration data.
 * 
 * @author <a href="mailto:damian@ribax.org">Damian Hamill</a>
 *
 */
public interface Configurator {

    /** read the configuration from the element tree and do the configuration
     * 
     * @param config the element tree containing the configuration data
     */
    public void readConfiguration(Element config);
}
