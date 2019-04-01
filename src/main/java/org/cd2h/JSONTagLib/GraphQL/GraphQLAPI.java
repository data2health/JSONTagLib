package org.cd2h.JSONTagLib.GraphQL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.cd2h.JSONTagLib.util.LocalProperties;
import org.cd2h.JSONTagLib.util.PropertyLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GraphQLAPI {
    static Logger logger = Logger.getLogger(GraphQLAPI.class);

    protected static Connection conn = null;
    
    LocalProperties props = null;
    String targetServer = null;
    boolean libraryLoaded = false;
    Hashtable<String, String> statementHash = new Hashtable<String, String>();
    Hashtable<String, String> typeHash = new Hashtable<String, String>();
    
    public GraphQLAPI(String targetServer) {
	this.targetServer = targetServer;
	loadStatements();
    }
    
    private void loadStatements() {
	if (libraryLoaded)
	    return;
	logger.info("loading " + targetServer + " graphql library");
	try {
	    conn = getConnection();
	    PreparedStatement stmt = conn.prepareStatement("select name,query from graphql.query where service = ?");
	    stmt.setString(1, targetServer);
	    ResultSet rs = stmt.executeQuery();
	    while (rs.next()) {
		String name = rs.getString(1);
		String query = rs.getString(2);
		registerStatement(name, query);
		logger.debug("query: " + name + "\n" + query);
	    }
	    conn.close();
	} catch (SQLException | ClassNotFoundException e) {
	    logger.error("error loading " + targetServer + "graphql library: " + e);;
	}
    }
    
    public void registerStatement(String name, String statement) {
	statementHash.put(name, statement);
	
	if (statement.matches(" *search.*"))
	    typeHash.put(name, "search");
	else if (statement.matches(" *update.*"))
	    typeHash.put(name, "mutation");
	else
	    typeHash.put(name, "query");
    }
    
    public String getStatement(String name) {
	return statementHash.get(name);
    }

    public String getStatementType(String name) {
	return typeHash.get(name);
    }

    public JSONObject submitSearch(String search) throws IOException {
	if (!search.matches(" *search.*"))
	    throw new IOException("search request doesn't start with search");
	// normalize search string to remove tabs, as GraphQL doesn't like them...
	return submit(("{ \"query\": \"{" + quotify(search) + "}\" } ").replaceAll("[\n\t ]+", " "));
    }

    public JSONObject submitQuery(String query) throws IOException {
	if (query.matches(" *search.*") || query.matches(" *update.*"))
	    throw new IOException("query request doesn't start with valid object");
	// normalize query string to remove tabs, as GraphQL doesn't like them...
	switch (targetServer) {
	case "CD2H":
	    return submit(query.replaceAll("[\n\t ]+", " "));
	default:
	    return submit(("{ \"query\": \"query {" + quotify(query) + "}\" } ").replaceAll("[\n\t ]+", " "));
	}
    }

    public JSONObject submitMutation(String mutation) throws IOException {
	if (!mutation.matches(" *update.*"))
	    throw new IOException("mutation request doesn't start with update...");
	// normalize mutation string to remove tabs, as GraphQL doesn't like them...
	return submit(("{ \"query\": \"mutation {" + quotify(mutation) + "}\" } ").replaceAll("[\n\t ]+", " "));
    }

    private JSONObject submit(String request) throws IOException {
	// configure the connection
	logger.debug("url: " + props.getProperty("url"));
	URL uri = new URL(props.getProperty("url"));
	HttpURLConnection con = (HttpURLConnection) uri.openConnection();
	con.setRequestMethod("POST"); // type: POST, PUT, DELETE, GET
	if (props.getProperty("token") != null)
	    con.setRequestProperty("Authorization", "token " + props.getProperty("token"));
	con.setRequestProperty("Accept","application/json");
	if (targetServer.equals("CD2H"))
	    con.setRequestProperty("Content-Type", "application/graphql");
	con.setDoOutput(true);
	con.setDoInput(true);
	
	// submit the GraphQL construct
	logger.debug("request: " + request);
	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
	out.write(request);
	out.flush();
	out.close();

	// pull down the response JSON
	con.connect();
	logger.debug("response:" + con.getResponseCode());
	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	JSONObject results = new JSONObject(new JSONTokener(in));
	logger.debug("results:\n" + results.toString(3));
	in.close();
	return results;
    }

    static String quotify(String theString) {
	return theString.replace("\"", "\\\"");
    }

    protected static void store(String name, String server, String query) throws SQLException {
        System.out.println(name+"\n"+query);
        PreparedStatement stmt = conn.prepareStatement("insert into graphql.query(name,service,query) values(?,?,?)");
        stmt.setString(1, name);
        stmt.setString(2, server);
        stmt.setString(3, query);
        stmt.execute();
        stmt.close();
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
	LocalProperties props = PropertyLoader.loadProperties("graphql");
	Class.forName("org.postgresql.Driver");
    	Properties pprops = new Properties();
    	pprops.setProperty("user", props.getProperty("jdbc.user"));
    	pprops.setProperty("password", props.getProperty("jdbc.password"));
    //	if (use_ssl.equals("true")) {
    //	    props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
    //	    props.setProperty("ssl", "true");
    //	}
    	Connection conn = DriverManager.getConnection(props.getProperty("jdbc.url"), pprops);
    	conn.setAutoCommit(true);
    	return conn;
        }
}
