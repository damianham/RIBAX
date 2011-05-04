/**
 * 
 */
package org.ribax.common.validators;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;


import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * @author damian
 *
 */
public class ValidatorAdaptor implements Validator {

    String errorMessage = null;

    public void readDescription(Element config) {

        errorMessage = XMLutils.getElementString("errorMessage", config); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.ribax.common.validators.Validator#validate(java.util.List)
     */
    public boolean validate(List<String> params) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.ribax.common.validators.Validator#validate(java.lang.String)
     */
    public boolean validate(String text) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.ribax.common.validators.Validator#validate(java.util.ArrayList)
     */
    public boolean validate(ArrayList<NameValuePair> params) {
        return false;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
