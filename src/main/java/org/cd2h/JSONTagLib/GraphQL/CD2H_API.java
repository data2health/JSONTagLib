package org.cd2h.JSONTagLib.GraphQL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cd2h.JSONTagLib.util.PropertyLoader;

public class CD2H_API extends GraphQLAPI{

    private static final Logger logger = LogManager.getLogger("CD2H_API");
    // static Logger logger = Logger.getLogger(CD2H_API.class);
    
     public CD2H_API() {
	super("CD2H");
	props = PropertyLoader.loadProperties("cd2h_postgraphile");
   }

}
