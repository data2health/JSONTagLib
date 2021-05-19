package org.cd2h.JSONTagLib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public class SetAPITag extends BodyTagSupport {
	static Logger logger = LogManager.getLogger(SetAPITag.class);

	String API = null;

	public SetAPITag() {
		super();
		init();
	}

	private void init() {
		API = null;
	}

	public int doStartTag() throws JspException {
		logger.info("setting API to " + API);
		return EVAL_BODY_INCLUDE;
	}

	public void release() {
		init();
	}

	public String getAPI() {
		return API;
	}

	public void setAPI(String aPI) {
		API = aPI;
	}

}
