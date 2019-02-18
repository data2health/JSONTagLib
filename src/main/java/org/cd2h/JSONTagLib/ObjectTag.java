package org.cd2h.JSONTagLib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;
import org.cd2h.drive.util.GitHubAPI;
import org.cd2h.drive.util.GitHubPush;
import org.json.JSONObject;

import edu.uiowa.extraction.LocalProperties;
import edu.uiowa.extraction.PropertyLoader;

@SuppressWarnings("serial")
public class ObjectTag extends BodyTagSupport {
    static Logger logger = Logger.getLogger(ObjectTag.class);
    static LocalProperties github_props = null;
    
    String queryName = null;
    String targetName = null;
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
	    switch (theAPITag.getAPI()) {
	    case "GitHub":
		if (github_props == null)
		    github_props = PropertyLoader.loadProperties("github");
		GitHubAPI theAPI = new GitHubAPI(github_props);
		try {
		    switch (GitHubPush.getQueryType(queryName)) {
		    case "search":
			object = theAPI.submitSearch(GitHubPush.getQuery(queryName)).getJSONObject("data");
			break;
		    default:
			object = theAPI.submitQuery(GitHubPush.getQuery(queryName)).getJSONObject("data");
			break;
		    }
		    if (targetName != null)
			object = object.getJSONObject(targetName);
		    logger.debug("object:\n" + object.toString(3));
		} catch (IOException e) {
		    throw new JspException(e);
		}
		break;
	    default:
		throw new JspException("unknown API requested: " + theAPITag.getAPI());
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

}
