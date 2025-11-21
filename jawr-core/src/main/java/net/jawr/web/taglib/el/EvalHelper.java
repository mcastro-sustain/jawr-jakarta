/**
 * Copyright 2009 Ibrahim Chaehoi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package net.jawr.web.taglib.el;

import jakarta.el.ELContext;
import jakarta.el.ExpressionFactory;
import jakarta.el.ValueExpression;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.Tag;

/**
 * Evaluation helper utility class for Jakarta EL 5.0.
 *
 * @author Ibrahim Chaehoi
 *
 */
public class EvalHelper {

	/**
     * Evaluate the string EL expression passed as parameter
     * @param propertyName the property name
     * @param propertyValue the property value
     * @param tag the tag
     * @param pageContext the page context
     * @return the value corresponding to the EL expression passed as parameter
     * @throws JspException if an exception occurs
     */
    public static String evalString(String propertyName, String propertyValue, Tag tag, PageContext pageContext) throws JspException{
    	try {
    		return (String) evaluate(propertyValue, String.class, pageContext);
    	} catch (Exception e) {
    		throw new JspException("Error evaluating expression '" + propertyName + "': " + propertyValue, e);
    	}
    }

    /**
     * Evaluate the boolean EL expression passed as parameter
     * @param propertyName the property name
     * @param propertyValue the property value
     * @param tag the tag
     * @param pageContext the page context
     * @return the value corresponding to the EL expression passed as parameter
     * @throws JspException if an exception occurs
     */
    public static Boolean evalBoolean(String propertyName, String propertyValue, Tag tag, PageContext pageContext) throws JspException{
    	try {
    		return (Boolean) evaluate(propertyValue, Boolean.class, pageContext);
    	} catch (Exception e) {
    		throw new JspException("Error evaluating expression '" + propertyName + "': " + propertyValue, e);
    	}
    }

    /**
     * Evaluate an EL expression using Jakarta EL 5.0 API
     * @param expression the EL expression
     * @param expectedType the expected return type
     * @param pageContext the page context
     * @return the evaluated value
     */
    private static Object evaluate(String expression, Class<?> expectedType, PageContext pageContext) {
    	if (expression == null || expression.trim().isEmpty()) {
    		return null;
    	}

    	// Get the EL context from the page context
    	ELContext elContext = pageContext.getELContext();

    	// Get the expression factory
    	ExpressionFactory expressionFactory = ExpressionFactory.newInstance();

    	// Create and evaluate the value expression
    	ValueExpression valueExpression = expressionFactory.createValueExpression(
    			elContext, expression, expectedType);

    	return valueExpression.getValue(elContext);
    }
}
