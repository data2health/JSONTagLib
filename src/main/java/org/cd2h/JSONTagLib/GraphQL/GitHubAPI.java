package org.cd2h.JSONTagLib.GraphQL;

import org.apache.log4j.Logger;
import org.cd2h.JSONTagLib.util.PropertyLoader;

public class GitHubAPI extends GraphQLAPI{
    static Logger logger = Logger.getLogger(GitHubAPI.class);

    static String me = "viewer { login,name,id }";
    static String repo_list = "viewer {"
	    	+ "	repositories(first:100) {"
	    	+ "		nodes {"
	    	+ "			name,"
	    	+ "			url,"
	    	+ "			id,"
	    	+ "			milestones(first:100) {"
	    	+ "				nodes {"
	    	+ "					id,"
	    	+ "					title,"
	    	+ "					description,"
	    	+ "					dueOn,"
	    	+ "					creator{login}"
	    	+ "				}"
	    	+ "			},"
	    	+ "			repositoryTopics(first:100) {"
	    	+ "				nodes {"
	    	+ "					topic{name}"
	    	+ "				}"
	    	+ "			}"
//	    	+ "			collaborators(first:100) {"
//	    	+ "				nodes {"
//	    	+ "					login,name"
//	    	+ "				}"
//	    	+ "			}"
	    	+ "		}"
	    	+ "	}"
	    	+ "}";
    static String repo = "repository(owner:eichmann, name:SPARQLTagLib) {"
    	+ "	id,"
    	+ "	url"
    	+ "			repositoryTopics(first:100) {"
    	+ "				nodes {"
    	+ "					topic{name}"
    	+ "				}"
    	+ "			}"
    	+ "}";
    static String repo_mutate = " updateTopics ("
    				+ "	input: {"
    				+ "		repositoryId: \"MDEwOlJlcG9zaXRvcnkxMzQzMTU4NDk=\","
    				+ "		topicNames:[\"data2health\", \"pea\"]"
    				+ "	}"
    				+ ") {"
    				+ "	clientMutationId"
    				+ "}";
    static String data2health_org = "organization(login:data2health) {"
    	+ "	id,"
    	+ "	login,"
    	+ "	repositories(first:100, orderBy:{field:NAME, direction: ASC}) {"
    	+ "		nodes {"
    	+ "			name,"
    	+ "			description,"
    	+ "			url,"
    	+ "			milestones(first:100) {"
    	+ "				nodes {"
    	+ "					id,"
    	+ "					title,"
    	+ "					description,"
    	+ "					dueOn,"
    	+ "					creator{login}"
    	+ "				}"
    	+ "			},"
    	+ "			repositoryTopics(first:100) {"
    	+ "				nodes {"
    	+ "					topic{name}"
    	+ "				}"
    	+ "			}"
//    	+ "			collaborators(first:100) {"
//    	+ "				nodes {"
//    	+ "					login,name"
//    	+ "				}"
//    	+ "			}"
    	+ "		}"
    	+ "	}"
    	+ "	members(first:100) {"
    	+ "		nodes {"
    	+ "			id,name,bio,email,login,avatarUrl"
    	+ "		}"
    	+ "	}"
    	+ "}";
    static String repoByTopic = "search(query: \"topic:data2health\", type: REPOSITORY, first:100) {"
    				+ "	repositoryCount"
    				+ "	edges {"
    				+ "		node {"
    				+ "		... on Repository {"
    				+ "			owner {"
    				+ "				id"
    				+ "				login"
    				+ "			}"
    				+ "			name"
    				+ "			description"
    				+ "			stargazers {"
    				+ "				totalCount"
    				+ "			}"
    				+ "			forks {"
    				+ "				totalCount"
    				+ "			}"
    				+ "			updatedAt"
    				+ "			}"
    				+ "		}"
    				+ "	}"
    				+ "}";

    public GitHubAPI() {
	super();
	props = PropertyLoader.loadProperties("github");
	
	registerStatement("me", me);
	registerStatement("repo_list", repo_list);
	registerStatement("repo", repo);
	registerStatement("repo_mutate", repo_mutate);
	registerStatement("data2health_org", data2health_org);
	registerStatement("data2health_tagged_repos", repoByTopic);
    }

}
