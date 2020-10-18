package analytics;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqlServerDbAccessor {
	private Connection con;
	private String connectionUrl;
	
	private String defaultConnUrl = "jdbc:sqlserver://;" +
            "servername=csdata.cd4sevot432y.us-east-1.rds.amazonaws.com;"
//			+ "user=csc312cloud;password=c3s!c2Cld;";
//            + "user=csc480dev;password=c4s*C0sWe;"; 
//			+ "user=NsfResearcher;password=r2h0e2S0u5s;";
			+ "user=administrator;password=MercerBear$2020;";
			// + "databaseName=JLBookstore;";
	/*
	// in WSC
	private String defaultConnUrl = "jdbc:sqlserver://;servername=cssql\\sqldata;"
				+ "user=csc480dev;password=c4s*C0sWe;" +
			"databaseName=JLBookstore;";
	*/

	public SqlServerDbAccessor() {
		connectionUrl = defaultConnUrl;
	}
	
	public SqlServerDbAccessor(String serverName, String user, String pwd, String dbName) {
		connectionUrl = "jdbc:sqlserver://;";
		connectionUrl += "servername=" + serverName + ";"; 
		connectionUrl += "user=" + user + ";"; 
		connectionUrl += "password=" + pwd + ";"; 
		connectionUrl += "databaseName=" + dbName + ";"; 
	}
	
	public void setDbName(String dbName) {
		connectionUrl += "databaseName=" + dbName;
	}
	
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
