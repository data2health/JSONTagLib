# JSONTagLib
A JSP tag library supporting access to various JSON constructs as tags. This is currently targeted at the V4 GitHub API.
## Example V4 GraphQL request
```
    static String members = "organization(login:data2health) {"
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
    	+ "		}"
    	+ "	}"
    	+ "	members(first:100) {"
    	+ "		nodes {"
    	+ "			id,name,bio,email,login,avatarUrl"
    	+ "		}"
    	+ "	}"
    	+ "}";
```
## Example JSP rendering of the returned JSON
```
            <h1>CD2H GitHub Organization</h1>
            <p>These are repositories owned by "data2health". (<a href="repos.jsp">switch to repository view</a>)</p>
            <json:setAPI API="GitHub">
                <json:object queryName="data2health_org" targetName="organization">
                <div style="width: 60%; padding: 0px 0px 0px 0px; float: left">
                <h3>Repositories Owned by data2health</h3>
                <hr>
                    <json:object targetName="repositories">
	                    <json:array label="nodes">
	                        <json:object>
	                            <h4><a href="<json:data label="url"/>"><json:data label="name"/></a></h4>
	                            <p><json:data label="description"/>
	                            <json:object targetName="repositoryTopics">
	                               <ol class="bulletedList">
	                               <json:array label="nodes">
	                                   <json:object>
	                                       <json:object targetName="topic">
	                                           <li><json:data label="name"/>
	                                       </json:object>
	                                   </json:object>
	                               </json:array>
	                               </ol>
	                            </json:object>
                                <json:object targetName="milestones">
                                    <json:array label="nodes">
                                    <c:if test="${json:isFirstArrayElement() == true}">
                                        <table>
                                        <tr><th>Title</th><th>Due On</th><th>Description</th><th>Creator</th></tr>
                                    </c:if>
                                       <json:object>
                                               <tr>
                                                <td><json:data label="title"/></td>
                                                <td><json:data label="dueOn"/></td>
                                                <td><json:data label="description"/></td>
                                                <td><json:object targetName="creator"><a href="https://github.com/<json:data label="login"/>"><json:data label="login"/></a></json:object></td>
                                               </tr>
                                        </json:object>
                                    <c:if test="${json:isLastArrayElement() == true}">
                                     </table>
                                   </c:if>
                                   </json:array>
                               </json:object>
	                        </json:object>
	                    </json:array>
                    </json:object>
                </div>
                <div style="width: 35%; padding: 0px 0px 0px 50px; float: left">
                <h3>Members</h3>
                <hr>
                    <json:object targetName="members">
                    <ol class="bulletedList">
                        <json:array label="nodes">
                            <json:object>
                                <li><img align="top" width="40px" src="<json:data label="avatarUrl"/>"> <b><json:data label="name"/></b>
                                    (<a href="https://github.com/<json:data label="login"/>"><json:data label="login"/></a>)
                                    <json:data label="bio"/>
                            </json:object>
                        </json:array>
                    </ol>
                    </json:object>
                </div>
                </json:object>
            </json:setAPI>
```
