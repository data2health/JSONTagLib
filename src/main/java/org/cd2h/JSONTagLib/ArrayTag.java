package org.cd2h.JSONTagLib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class ArrayTag extends BodyTagSupport {

	private static final Logger logger = LogManager.getLogger( "ArrayTag" );
    // static Logger logger = Logger.getLogger(ArrayTag.class);

    static Vector<ArrayTag> stack = new Vector<ArrayTag>();
    //TODO need to shift the stack from a static to a session variable
    
    public static Boolean isFirst() {
	return stack.elementAt(0).fence == 1;
    }
    
    public static Boolean isLast() {
	return stack.elementAt(0).fence >= stack.elementAt(0).theArray.length();
    }
    
    String label = null;
    JSONArray theArray = null;
    JSONObject currentObject = null;
    int fence = 0;

    private void init() {
	label = null;
	theArray = null;
	currentObject = null;
	fence = 0;
    }

    public int doStartTag() throws JspTagException {
	stack.add(0, this);
	ObjectTag theObject = (ObjectTag) findAncestorWithClass(this, ObjectTag.class);

	if (theObject == null) {
	    throw new JspTagException("Array tag not nesting in Data instance");
	}

	theArray = theObject.object.getJSONArray(label);
	logger.debug("json array " + label + ": " + theArray.toString(3));
	
	fence = 0;
	if (fence < theArray.length()) {
	    currentObject = theArray.getJSONObject(fence++);
	    return EVAL_BODY_INCLUDE;
	} else
	    return SKIP_BODY;
    }

    public int doAfterBody() throws JspTagException {
	if (fence < theArray.length()) {
	    currentObject = theArray.getJSONObject(fence++);
	    return EVAL_BODY_AGAIN;
	} else
	    return SKIP_BODY;
    }
    
    public void release() {
	init();
    }

    public int doEndTag() throws JspException {
	stack.remove(0);
	return super.doEndTag();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @SuppressWarnings("unused")
    private void futureSortOption() {
	// I assume that we need to create a JSONArray object from the following string
	String jsonArrStr = "[ { \"ID\": \"135\", \"Name\": \"Fargo Chan\" },{ \"ID\": \"432\", \"Name\": \"Aaron Luke\" },{ \"ID\": \"252\", \"Name\": \"Dilip Singh\" }]";

	JSONArray jsonArr = new JSONArray(jsonArrStr);
	JSONArray sortedJsonArray = new JSONArray();

	List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	for (int i = 0; i < jsonArr.length(); i++) {
	    jsonValues.add(jsonArr.getJSONObject(i));
	}
	Collections.sort(jsonValues, new Comparator<JSONObject>() {
	    // You can change "Name" with "ID" if you want to sort by ID
	    private static final String KEY_NAME = "Name";

	    @Override
	    public int compare(JSONObject a, JSONObject b) {
		String valA = new String();
		String valB = new String();

		try {
		    valA = (String) a.get(KEY_NAME);
		    valB = (String) b.get(KEY_NAME);
		} catch (JSONException e) {
		    // do something
		}

		return valA.compareTo(valB);
		// if you want to change the sort order, simply use the
		// following:
		// return -valA.compareTo(valB);
	    }
	});

	for (int i = 0; i < jsonArr.length(); i++) {
	    sortedJsonArray.put(jsonValues.get(i));
	}
    }
}
