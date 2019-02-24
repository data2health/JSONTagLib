package org.cd2h.JSONTagLib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;
import org.cd2h.JSONTagLib.GraphQL.CD2H_API;
import org.cd2h.JSONTagLib.GraphQL.GitHubAPI;
import org.cd2h.JSONTagLib.GraphQL.GraphQLAPI;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class ObjectTag extends BodyTagSupport {
    static Logger logger = Logger.getLogger(ObjectTag.class);
    
    String queryName = null;
    String targetName = null;
    String parameter = null;
    GraphQLAPI theAPI = null;
    JSONObject object = null;

    public ObjectTag() {
	super();
	init();
    }

    private void init() {
	queryName = null;
	targetName = null;
	object = null;
    }
    
    private Tag getJSONParent() {
	Tag parent = getParent();
	
	while (parent != null && !(parent instanceof SetAPITag || parent instanceof ArrayTag || parent instanceof ObjectTag)) {
	    parent = parent.getParent();
	}
	
	return parent;
    }

    public int doStartTag() throws JspException {
	SetAPITag theAPITag = null;
	ArrayTag theArrayParent = null;
	ObjectTag theObjectParent = null;
	
	if (getJSONParent() instanceof SetAPITag)
	    theAPITag = (SetAPITag) findAncestorWithClass(this, SetAPITag.class);
	if (getJSONParent() instanceof ArrayTag)
	    theArrayParent = (ArrayTag) findAncestorWithClass(this, ArrayTag.class);
	if (getJSONParent() instanceof ObjectTag)
	    theObjectParent = (ObjectTag) findAncestorWithClass(this, ObjectTag.class);

	if (theAPITag == null && theArrayParent == null && theObjectParent == null)
	    throw new JspTagException("No API, array or object for object specified");

	if (theAPITag != null) {
	    theAPI = null;
	    switch (theAPITag.getAPI()) {
	    case "GitHub":
		theAPI = new GitHubAPI();
		break;
	    case "CD2H":
		theAPI = new CD2H_API();
		break;
	    default:
		throw new JspException("unknown API requested: " + theAPITag.getAPI());
	    }
	    try {
		switch (theAPI.getStatementType(queryName)) {
		case "search":
		    object = theAPI.submitSearch(getStatement(queryName)).getJSONObject("data");
		    break;
		default:
		    object = theAPI.submitQuery(getStatement(queryName)).getJSONObject("data");
		    break;
		}
		if (targetName != null)
		    object = object.getJSONObject(targetName);
		logger.debug("object:\n" + object.toString(3));
	    } catch (IOException e) {
		throw new JspException(e);
	    }
	} else if (theArrayParent != null) {
	    object = theArrayParent.currentObject;
	    if (targetName != null)
		object = object.getJSONObject(targetName);
	    logger.debug("object:\n" + object.toString(3));
	} else {
	    object = theObjectParent.object.getJSONObject(targetName);
	}
	return EVAL_BODY_INCLUDE;
    }

    public void release() {
	init();
    }
    
    String getStatement(String queryName) {
	if (parameter == null)
	    return theAPI.getStatement(queryName);
	else {
	    String[] array = parameter.split(":");
	    return theAPI.getStatement(queryName).replace("$"+array[0], array[1]);
//	    return theAPI.getStatement(queryName) + " variables { \"proj\": " + parameter + "}";
	}
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

}
