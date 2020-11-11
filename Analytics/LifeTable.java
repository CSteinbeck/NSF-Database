//This is mostly re-purposed code from SelectTest.java
//TODO: create a LifeTable object to store birth year, death year, and age
//TODO: choose a data structure to make a table with. Considering a 2d array at the moment

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LifeTable {
    private Statement stmt;
    private analytics.SqlServerDbAccessor dba;

    public LifeTable() {
        dba = new analytics.SqlServerDbAccessor();
        dba.setDbName("CayoSantiagoRhesusDB"); // use for default database
        //dba.setDbName("KerryO"); // use this line for Kerry's database
        dba.connectToDb();
        //stmt = dba.getStmt();
    }

    public static void main(String[] args) {
        LifeTable lt = new LifeTable();
        String[] cols = {"*"};
        lt.createLifeTable("CSRhesusSubject", cols); // use if using default db

    }


    private void createLifeTable(String table, String[] cols) {
        System.out.print("");
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
            int age;
            System.out.print("");
            stmt = dba.getConnection().createStatement();
            result = stmt.executeQuery("EXECUTE dbo.LifeTable");

            // get meta data from result and print column labels
            ResultSetMetaData meta = result.getMetaData();

            int columns = meta.getColumnCount();

            System.out.println(columns);

            int j;
            for (j=1; j<columns; j++)
                System.out.print(meta.getColumnName(j) + ", ");
            System.out.println(meta.getColumnName(j));

            ArrayList<ArrayList<Integer>> lifeTable = new ArrayList<>();

            System.out.println(columns);

            int count = 0;
            int ltAge = 0;
            int ltTot = 0;

            while (result.next()) {
                lifeTable.add(new ArrayList());

                for (int i=1; i<columns; i++) {
                    ltAge = Integer.parseInt(result.getString(i));
                    ltTot = Integer.parseInt(result.getString(i + 1));
                }

                lifeTable.get(count).add(ltAge);
                lifeTable.get(count).add(ltTot);

                count++;
            }
            //Total value may be useful later. I'll delete if this isn't the case.
            int totMonkeys = 0;

            for(int i = 0; i < lifeTable.size(); i++)
                totMonkeys += lifeTable.get(i).get(1);

            //getting NX is getting the all of the subjects who lived through that age
            int NX ;
            for(int i = 0; i < lifeTable.size(); i++)
            {
                NX = 0;

                for(int x = i; x < lifeTable.size(); x++)
                    NX += lifeTable.get(x).get(1);

                lifeTable.get(i).add(NX);
            }

            //print out the whole thing
            for(int i = 0; i < lifeTable.size(); i++)
                System.out.println(lifeTable.get(i));

            System.out.println(totMonkeys);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
