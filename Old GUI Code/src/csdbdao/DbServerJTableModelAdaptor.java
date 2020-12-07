package csdbdao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class DbServerJTableModelAdaptor implements JTableModelAdaptor {
	private SqlServerDbAccessor sqda;
	private String[] columnNamesToReturn;
	private Object[][] dataToReturn;

	public DbServerJTableModelAdaptor(SqlServerDbAccessor sqda) {
		this.sqda = sqda;
	}
	
	public String[] getColNamesForTable(String tableName) {
		try {
			DatabaseMetaData databaseMetaData = sqda.getConnection().getMetaData();
	        ResultSet columns = databaseMetaData.getColumns(null,null, tableName, null);
	        LinkedList<String> columnNames = new LinkedList<String>();
	        while (columns.next()) {
	        	columnNames.add(columns.getString("COLUMN_NAME"));
	        }
	        columnNamesToReturn = columnNames.toArray(new String[columnNames.size()]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return columnNamesToReturn;
	}

	public Object[][] getData(String tableName) {
		String SQL = "SELECT * FROM ";
		Object[] row; 
		LinkedList<Object[]> contents = new LinkedList<Object[]>();
		try {
			Statement stmt = sqda.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(SQL + tableName);
	        ResultSetMetaData meta = rs.getMetaData();
	        int columns = meta.getColumnCount();
	        System.out.println(columns);
	        row = new Object[columns];
	        
	        InputStream is;
    		while (rs.next()) {
                for (int i=1; i<=columns; i++) {
                    if (meta.getColumnType(i) == Types.VARBINARY) {
	                    System.out.print("[Image]");
	                    is = rs.getBinaryStream(i);
	                    if (is == null) {
	                    	row[i-1] = null;
	                    	System.out.print("*");
	                    }
	                    else 
	                    	row[i-1] = ImageIO.read(is);;
	                	// can do it in UpdatePhoto with one row one field
	                	// not here in a whole table
	                	// running out of memory?
	                    	System.out.print(", ");
                    }
                    else {
	                    System.out.print(rs.getString(i) + 
	                            ((i==columns)?"":", "));
	                	row[i-1] = rs.getString(i);
                    }
                }
                System.out.println();
                contents.add(row);
                row = new Object[columns];
    		}
	        System.out.println();
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(contents.size() + "," + contents.get(0).length);
		
		dataToReturn = new Object[contents.size()][contents.get(0).length];
		for (int i=0; i<contents.size(); i++)
			dataToReturn[i] = contents.get(i);
		return dataToReturn;
	}

}
