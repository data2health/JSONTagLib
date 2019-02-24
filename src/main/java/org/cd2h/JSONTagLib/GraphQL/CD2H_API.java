package org.cd2h.JSONTagLib.GraphQL;

import org.apache.log4j.Logger;
import org.cd2h.JSONTagLib.util.PropertyLoader;

public class CD2H_API extends GraphQLAPI{
    static Logger logger = Logger.getLogger(CD2H_API.class);

    String projects = "{"
    	+ "  query: allProjects (orderBy:TITLE_ASC) {"
    	+ "    nodes {"
    	+ "      id"
    	+ "      title"
    	+ "      leadSite"
    	+ "      description"
    	+ "      pitch"
    	+ "      github"
    	+ "      vertical"
    	+ "      thematicArea"
    	+ "      rolesById (condition:{role:\"Lead\"}) {"
    	+ "        nodes {"
    	+ "          personByEmailAddress {"
    	+ "            lastName"
    	+ "            preferredFirstName"
    	+ "          }"
    	+ "        }"
    	+ "      }"
    	+ "    }"
    	+ "  }"
    	+ "}"
    	;
    String project = "{"
    	+ "   projectById(id:$proj) {"
    	+ "      title"
    	+ "      leadSite"
    	+ "      description"
    	+ "      pitch"
    	+ "      github"
    	+ "      vertical"
    	+ "      thematicArea"
    	+ "      rolesById (condition:{role:\"Lead\"}) {"
    	+ "        nodes {"
    	+ "          personByEmailAddress {"
    	+ "            lastName"
    	+ "            preferredFirstName"
    	+ "          }"
    	+ "        }"
    	+ "      }"
    	+ "	}"
    	+ "}"
    	;
    String project_proper = "query($proj:Int!) {"
	    	+ "   projectById(id:$proj) {"
	    	+ "      title"
	    	+ "      leadSite"
	    	+ "      description"
	    	+ "      pitch"
	    	+ "      github"
	    	+ "      vertical"
	    	+ "      thematicArea"
	    	+ "      rolesById (condition:{role:\"Lead\"}) {"
	    	+ "        nodes {"
	    	+ "          personByEmailAddress {"
	    	+ "            lastName"
	    	+ "            preferredFirstName"
	    	+ "          }"
	    	+ "        }"
	    	+ "      }"
	    	+ "	}"
	    	+ "}"
	    	;

    public CD2H_API() {
	super("CD2H");
	props = PropertyLoader.loadProperties("cd2h_postgraphile");
	
	registerStatement("projects", projects);
	registerStatement("project", project);
    }

}
