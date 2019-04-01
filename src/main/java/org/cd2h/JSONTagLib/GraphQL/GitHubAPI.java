package org.cd2h.JSONTagLib.GraphQL;

import org.apache.log4j.Logger;
import org.cd2h.JSONTagLib.util.PropertyLoader;

public class GitHubAPI extends GraphQLAPI{
    static Logger logger = Logger.getLogger(GitHubAPI.class);

    static String me = "viewer { login,name,id }";
    
    public GitHubAPI() {
	super("GitHub");
	props = PropertyLoader.loadProperties("github");
    }

}
