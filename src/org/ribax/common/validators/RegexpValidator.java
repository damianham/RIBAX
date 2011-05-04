/**
 * 
 */
package org.ribax.common.validators;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.jdom.Element;
import org.ribax.common.Messages;

import utils.log.BasicLogger;
import utils.xml.XMLutils;

/**
 * @author damian
 *
 */
public class RegexpValidator extends ValidatorAdaptor implements Validator {

    public static final long serialVersionUID = 1;
    protected static final String BUNDLE_NAME = "org.ribax.common.validators.messages"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(RegexpValidator.class.getName());
    String regexp = null;

    public RegexpValidator() {
    }

    public RegexpValidator(String regexp, String msg) {
        this.regexp = regexp;
        this.errorMessage = msg;
    }

    public void readDescription(Element config) {
        super.readDescription(config);

        regexp = XMLutils.getElementString("regexp", config); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.Validators.Validator#validate(java.String)
     */
    public boolean validate(String text) {
        // an empty regular expression matches 
        if (regexp != null) {
            try {
                if (!text.matches(regexp)) {
                    return false;
                }
            } catch (PatternSyntaxException ex) {
                LOG.error(Messages.getString(BUNDLE_NAME, "RegexpValidator.17") + regexp + Messages.getString(BUNDLE_NAME, "TextDataItem.18"), ex); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return true;
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.Validators.Validator#validate(java.util.List)
     */

    public boolean validate(List<String> params) {

        for (String s : params) {
            if (validate(s) == false) {
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
