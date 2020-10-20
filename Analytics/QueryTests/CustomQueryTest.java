// Custom Query Test
// This program tests Kerry's custom query "GetMonkey"
// Type the SubjectId of the monkey, and their info will print to the screen.
// Type 'q' to end program.

package analytics;

import java.util.Scanner;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomQueryTest {
	private Statement stmt;
	private analytics.SqlServerDbAccessor dba;
	
	public CustomQueryTest() {
		dba = new analytics.SqlServerDbAccessor();
		dba.setDbName("KerryO");
		dba.connectToDb();
//		stmt = dba.getStmt();
	}
	
	public static void main(String[] args) {
		CustomQueryTest test = new CustomQueryTest();
		Scanner stdin = new Scanner(System.in);
		boolean runMenu = true;
		while (runMenu) {
			System.out.print("Enter Monkey SubjectId : ");
			String subjectId = stdin.nextLine();
			test.getMonkeyInfo(subjectId);
			if ( subjectId.compareTo("q") == 0 )
				runMenu = false;
		}
		System.out.println("               .-\"\"\"-.\r\n" + 
				" ________    _/-=-.   \\\r\n" + 
				"|Bye Bye!|  (_|a a/   |_\r\n" + 
				" ------------/ \"  \\   ,_)\r\n" + 
				"        _    \\`=' /__/\r\n" + 
				"       / \\_  .;--'  `-.\r\n" + 
				"       \\___)//      ,  \\\r\n" + 
				"        \\ \\/;        \\  \\\r\n" + 
				"         \\_.|         | |\r\n" + 
				"          .-\\ '     _/_/\r\n" + 
				"        .'  _;.    (_  \\\r\n" + 
				"       /  .'   `\\   \\\\_/\r\n" + 
				"      |_ /       |  |\\\\\r\n" + 
				"     /  _)       /  / ||\r\n" + 
				"    /  /       _/  /  //\r\n" + 
				"    \\_/       ( `-/  ||\r\n" + 
				"              /  /   \\\\ .-.\r\n" + 
				"              \\_/     \\'-'/\r\n" + 
				"                       `\"`");
	}
	
	private void getMonkeyInfo(String subjectId) {
		String query = "EXEC GetMonkey " + subjectId;
		
		ResultSet result = null;
		try {
			stmt = dba.getConnection().createStatement();
			result = stmt.executeQuery(query);
			
			// get meta data from result and print column labels
			ResultSetMetaData meta = result.getMetaData(); 
			int columns = meta.getColumnCount();
			
			
			int j;            
			for (j=1; j<columns; j++)                
				System.out.print(meta.getColumnName(j) + ", ");
			System.out.println(meta.getColumnName(j)); 
			
			
			while (result.next()) {
				for (int i=1; i<=columns; i++)                        
					System.out.print(result.getString(i) +                                 
							((i==columns)?"":",\t"));                
				System.out.println(); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
