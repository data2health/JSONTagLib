package org.cd2h.JSONTagLib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Loader {
	static Connection theConnection = null;
	static LocalProperties prop_file = null;
	static Logger logger = LogManager.getLogger(Loader.class);

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		prop_file = PropertyLoader.loadProperties(args[1]);
		theConnection = getConnection();
		logger.info("");
		logger.info("fetching "+ prop_file.getProperty("source.url"));
		logger.info("");

		URL theURL = new URL(prop_file.getProperty("source.url"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

		JSONArray results = new JSONArray(new JSONTokener(reader));
		logger.trace("array: " + results.toString(3));

		for (int i = 0; i < results.length(); i++) {
			JSONObject theObject = results.getJSONObject(i);
			logger.info("object: " + theObject.toString(3));
			
			PreparedStatement stmt = theConnection.prepareStatement(prop_file.getProperty("jdbc.statement"));
			stmt.setString(1, theObject.toString(3));
			stmt.execute();
			stmt.close();
		}
	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		Properties props = new Properties();
		props.setProperty("user", prop_file.getProperty("jdbc.user"));
		props.setProperty("password", prop_file.getProperty("jdbc.password"));
		Connection conn = DriverManager.getConnection(prop_file.getProperty("jdbc.url"), props);
		return conn;
	}

}
