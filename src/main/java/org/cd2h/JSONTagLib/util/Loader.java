package org.cd2h.JSONTagLib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Loader {
	static Connection theConnection = null;
	static LocalProperties prop_file = null;
	protected static final Log logger = LogFactory.getLog(Loader.class);

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		PropertyConfigurator.configure("/Users/eichmann/Documents/Components/log4j.info");
//		prop_file = PropertyLoader.loadProperties("medline_clustering");
//		theConnection = getConnection();
		logger.info("");
		logger.info("fetching ");
		logger.info("");

		URL theURL = new URL("https://api.github.com/repos/OHDSI/Vocabulary-v5.0/releases?per_page=100");
		BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

		JSONArray results = new JSONArray(new JSONTokener(reader));
		logger.trace("array: " + results.toString(3));

		for (int i = 0; i < results.length(); i++) {
			JSONObject theObject = results.getJSONObject(i);
			logger.info("object: " + theObject.toString(3));
		}
	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		Properties props = new Properties();
		props.setProperty("user", "eichmann");
		props.setProperty("password", "translational");
		Connection conn = DriverManager.getConnection("jdbc://hal.local/cd2h", props);
		return conn;
	}

}
