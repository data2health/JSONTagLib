package org.cd2h.JSONTagLib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public class SetAPITag extends BodyTagSupport {

    private static final Logger logger = LogManager.getLogger( "SetAPITag" );
    // static Logger logger = Logger.getLogger(SetAPITag.class);

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
