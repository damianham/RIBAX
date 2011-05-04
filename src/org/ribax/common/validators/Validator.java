/**
 * 
 */
package org.ribax.common.validators;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import utils.types.NameValuePair;

/**
 * Interface for classes that implement a method to validate a DataItem
 * contents
 * 
 * @author damian
 *
 */
public interface Validator {

    /**
     * Verify that a list of string values are valid.
     * 
     * @param params the list of text strings to validate
     * @return true if all the text strings are valid, false otherwise.
     */
    public boolean validate(List<String> params);

    /**
     * Verify that the text contents are valid
     * 
     * @param text the text contents to validate
     * @return true if the text string is valid, false otherwise.
     */
    public boolean validate(String text);

    /**
     * Verify that a list of NameValuePairs are valid.
     * 
     * @param params the list of NameValuePairs to validate
     * @return true if all the NameValuePairs are valid, false otherwise.
     */
    public boolean validate(ArrayList<NameValuePair> params);

    /**
     * get the error message associated with this validator
     * @return
     */
    public String getErrorMessage();

    /**
     * Read the description for the Validator. 
     * 
     * @param config  the root node for the Validator description
     */
    public void readDescription(Element config);
}
