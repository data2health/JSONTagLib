package org.cd2h.JSONTagLib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class DataTag extends BodyTagSupport {
    static Logger logger = Logger.getLogger(DataTag.class);
    
    String label = null;

    public int doStartTag() throws JspTagException {
	ObjectTag theObject = (ObjectTag) findAncestorWithClass(this, ObjectTag.class);

	if (theObject == null) {
	    throw new JspTagException("Data tag not nesting in Data instance");
	}

	try {
	    if (theObject == null || theObject.object == null)
		pageContext.getOut().print("");
	    else
		pageContext.getOut().print(theObject.object.get(label).toString());
	} catch (IOException e) {
	    logger.error("IO Exception", e);

	}

	return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
	return super.doEndTag();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
