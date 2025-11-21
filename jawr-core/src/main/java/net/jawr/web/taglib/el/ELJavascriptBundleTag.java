/**
 * Copyright 2008-2016 Ibrahim Chaehoi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.jawr.web.taglib.el;

import jakarta.servlet.jsp.JspException;

import net.jawr.web.taglib.JavascriptBundleTag;

/**
 * This class defines the EL version of Javascript Bundle tag
 * 
 * @author Ibrahim Chaehoi
 */
public class ELJavascriptBundleTag extends JavascriptBundleTag {

	/** The serial version UID */
	private static final long serialVersionUID = -7205914220367498483L;

	/** The source expression */
	private String srcExpr;

	/** The type expression */
	private String typeExpr;

	/** The async expression */
	private String asyncExpr;

	/** The defer expression */
	private String deferExpr;

	/** The use random parameter expression */
	private String useRandomParamExpr;

	/**
	 * Returns the useRandomParamExpr
	 * 
	 * @return the useRandomParamExpr
	 */
	public String getUseRandomParamExpr() {
		return useRandomParamExpr;
	}

	/**
	 * Sets the useRandomParamExpr
	 * 
	 * @param useRandomParamExpr
	 *            the useRandomParamExpr to set
	 */
	public void setUseRandomParamExpr(String useRandomParamExpr) {
		this.useRandomParamExpr = useRandomParamExpr;
	}

	/**
	 * Returns the typeExpr
	 * 
	 * @return the typeExpr
	 */
	public String getTypeExpr() {
		return typeExpr;
	}

	/**
	 * Sets the typeExpr
	 * 
	 * @param typeExpr
	 *            the typeExpr to set
	 */
	public void setTypeExpr(String typeExpr) {
		this.typeExpr = typeExpr;
	}

	/**
	 * Returns the srcExpr
	 * 
	 * @return the srcExpr
	 */
	public String getSrcExpr() {
		return srcExpr;
	}

	/**
	 * Sets the srcExpr
	 * 
	 * @param srcExpr
	 *            the srcExpr to set
	 */
	public void setSrcExpr(String srcExpr) {
		this.srcExpr = srcExpr;
	}

	/**
	 * Returns the async expression
	 * 
	 * @return the async expression
	 */
	public String getAsyncExpr() {
		return asyncExpr;
	}

	/**
	 * Sets the asyncExpr
	 * 
	 * @param asyncExpr
	 *            the asyncExpr to set
	 */
	public void setAsyncExpr(String asyncExpr) {
		this.asyncExpr = asyncExpr;
	}

	/**
	 * Returns the defer expression
	 * 
	 * @return the defer expression
	 */
	public String getDeferExpr() {
		return deferExpr;
	}

	/**
	 * Sets the deferExpr
	 * 
	 * @param deferExpr
	 *            the deferExpr to set
	 */
	public void setDeferExpr(String deferExpr) {
		this.deferExpr = deferExpr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jawr.web.taglib.AbstractResourceBundleTag#doStartTag()
	 */
	public int doStartTag() throws JspException {

		String string = null;

		if (srcExpr != null) {
			string = EvalHelper.evalString("srcExpr", srcExpr, this, pageContext);
			setSrc(string);
		}

		if (typeExpr != null) {
			string = EvalHelper.evalString("typeExpr", typeExpr, this, pageContext);
			setType(string);
		}

		if (asyncExpr != null) {
			string = EvalHelper.evalString("asyncExpr", asyncExpr, this, pageContext);
			setAsync(string);
		}

		if (deferExpr != null) {
			string = EvalHelper.evalString("deferExpr", deferExpr, this, pageContext);
			setDefer(string);
		}

		if (useRandomParamExpr != null) {
			string = EvalHelper.evalString("useRandomParamExpr", useRandomParamExpr, this, pageContext);
			setUseRandomParam(string);
		}

		return super.doStartTag();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#release()
	 */
	@Override
	public void release() {
		super.release();
		setTypeExpr(null);
		setSrcExpr(null);
		setUseRandomParamExpr(null);
		setAsyncExpr(null);
		setDeferExpr(null);
	}
}