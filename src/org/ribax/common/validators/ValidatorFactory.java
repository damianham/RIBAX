/**
 * 
 */
package org.ribax.common.validators;

import java.lang.reflect.Method;

import org.jdom.Element;
import org.ribax.common.Messages;

import utils.log.BasicLogger;
import utils.xml.XMLutils;

/**
 * @author damian
 *
 */
public class ValidatorFactory {

    private static final String BUNDLE_NAME = "org.ribax.common.validators.messages"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(ValidatorFactory.class.getName());

    public static Validator readValidator(Element config) {

        String regexp = XMLutils.getElementString("regexp", config); //$NON-NLS-1$

        if (regexp != null) {
            // a regexp is a single validator
            return new RegexpValidator(regexp,
                    XMLutils.getElementString("errorMessage", config)); //$NON-NLS-1$
        } else {
            // maybe a user defined class
            String className = XMLutils.getElementString("classname", config); //$NON-NLS-1$

            // fall back on a short type name which is mapped to a fully qualified classname
            if (className == null) {
                className = XMLutils.getElementString("type", config); //$NON-NLS-1$
            }
            try {
                Class provider = Class.forName(className);

                Class[] args = {org.jdom.Element.class};

                // check this class is a DataItem
                Method m = provider.getMethod("readDescription", args); //$NON-NLS-1$

                if (m != null) {
                    // create a new instance
                    Validator v = (Validator) provider.newInstance();

                    // tell the new DataItem instance to read it's description from
                    // the Element tree
                    v.readDescription(config);

                    return v;
                }
            } catch (NoSuchMethodException ex) {
                LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.3"), ex); //$NON-NLS-1$

            } catch (NullPointerException ex) {
                LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.4"), ex); //$NON-NLS-1$

            } catch (SecurityException ex) {
                LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.5"), ex); //$NON-NLS-1$

            } catch (ClassNotFoundException ex) {
                LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.6"), ex); //$NON-NLS-1$

            } catch (ClassCastException ex) {
                LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.7"), ex); //$NON-NLS-1$

            } catch (Exception ex) {
                LOG.error(Messages.getString(BUNDLE_NAME, "DataItemFactory.8"), ex); //$NON-NLS-1$

            }
        }
        return null;
    }
}
