// Prints the info for all Macaca monkeys.
// Can be edited to print Rhesus monkeys. 

package analytics;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectTest {
	private Statement stmt;
	private analytics.SqlServerDbAccessor dba;
	
	public SelectTest() {
		dba = new analytics.SqlServerDbAccessor();
		dba.setDbName("CayoSantiagoRhesusDB"); // use for default database
		//dba.setDbName("KerryO"); // use this line for Kerry's database
		dba.connectToDb();
		//stmt = dba.getStmt();
	}

	public static void main(String[] args) {
		SelectTest test = new SelectTest();

		//String[] cols = {"OriginalSubjectId", "MotherId"};
		//String[] cols = {"SequenceId", "OriginalSubjectId", "Gender", "BirthYear", "DeathYear", "MotherId", "Generation", "FamilyId"};
		String[] cols = {"*"};
		test.selectColumnsFromTable("CSRhesusSubject", cols); // use if using default db
		//test.selectColumnsFromTable("MacacaSubject", cols); // use if using Kerry's db
			
	}

	private void selectColumnsFromTable(String table, String[] cols) {
		String query = "SELECT ";
		int index = 1;
		for (String col : cols) {
			query += col;
			if (index++ < cols.length)
				query += ", ";
		}
		
		query += " FROM " + table;
		
		ResultSet result = null;
		try {
			stmt = dba.getConnection().createStatement();
			result = stmt.executeQuery(query);
			
			// get meta data from result and print column labels
			ResultSetMetaData meta = result.getMetaData(); 
			int columns = meta.getColumnCount();
			System.out.println(columns);            
			int j;            
			for (j=1; j<columns; j++)                
				System.out.print(meta.getColumnName(j) + ", ");
			System.out.println(meta.getColumnName(j));            

			while (result.next()) {
				for (int i=1; i<=columns; i++)                        
					System.out.print(result.getString(i) +                                 
							((i==columns)?"":",\t"));                
				System.out.println();            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
