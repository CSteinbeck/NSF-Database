/* SqlServerDbAccessor Class
   This class connects you to the SQL Server so that you can qeury to your heart's content
   
   General Format for connecting to db in your client code:
   SqlServerDbAccessor dba = new SqlServerDbAccessor();
   dba.setDbName("name of database");
   dba.connectToDb();
   
   The default constructor uses the default connection URL which, at the moment, is the 
   login credentials for an admin. If you do not want to connect as an admin, use the other
   constructor. Login credentials can be found on Discord.

   After declaring a database accessor object, read the SelectTest or CustomQueryTest for 
   how to query the database.
*/

package com.nsfdb.application.analytics;

import java.sql.Connection;
import java.sql.DriverManager;
import com.nsfdb.application.analytics.FamilyTree.*;

public class SqlServerDbAccessor {
	private Connection con;
	private String connectionUrl;

	private String defaultConnUrl = "jdbc:sqlserver://;" +
            "servername=csdata.cd4sevot432y.us-east-1.rds.amazonaws.com;"
			+ "user=administrator;password=MercerBear$2020;";
	
	/*
	  In WSC
	  private String defaultConnUrl = "jdbc:sqlserver://;servername=cssql\\sqldata;"
				                    + "user=csc480dev;password=c4s*C0sWe;"
			                        + "databaseName=JLBookstore;";
	*/

	// Default constructor sets the login credentials to admin
	public SqlServerDbAccessor() {
		connectionUrl = defaultConnUrl;
	}
	
	// This constructor lets you choose username and password
	public SqlServerDbAccessor(String serverName, String user, String pwd, String dbName) {
		connectionUrl = "jdbc:sqlserver://;";
		connectionUrl += "servername=" + serverName + ";"; 
		connectionUrl += "user=" + user + ";"; 
		connectionUrl += "password=" + pwd + ";"; 
		connectionUrl += "databaseName=" + dbName + ";"; 
	}

	// Adds the database name to the connection url
	public void setDbName(String dbName) {
		connectionUrl += "databaseName=" + dbName;
	}

	// This method establishes the connection to the sql server so you can query
	public void connectToDb() {
    		try {
    			// Establish the connection.
    			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        		con = DriverManager.getConnection(connectionUrl);
    		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		// TODO Auto-generated method stub
		return con;
	}

	public String getUrl() {
		// TODO Auto-generated method stub
		return connectionUrl;
	}
}
