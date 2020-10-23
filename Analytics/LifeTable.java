//This is mostly re-purposed code from SelectTest.java
//TODO: create a LifeTable object to store birth year, death year, and age
//TODO: choose a data structure to make a table with. Considering a 2d array at the moment

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

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
            result = stmt.executeQuery(query);

            // get meta data from result and print column labels
            ResultSetMetaData meta = result.getMetaData();
            int columns = meta.getColumnCount();
            System.out.println(columns);
            int j;
            for (j=1; j<columns; j++)
                System.out.print(meta.getColumnName(j) + ", ");
            System.out.println(meta.getColumnName(j));

            //Column 4 Birth
            //Column 5 Death

            //TODO: fix the null pointer exception in the for loop
            while (result.next()) {
                for (int i=1; i<=columns; i++) {

                    int birthYr;
                    int deathYr;

                    //If the death year is R, the entry has been removed
                    //This conditional is causing a null pointer exception
                    if(!result.getString(5).contains("R")) {
                        birthYr = Integer.parseInt(result.getString(4));

                        //Dr. Zhao wants us to make all null values 2013 for death year
                        if(result.getString(5)!= null)
                            deathYr = Integer.parseInt(result.getString(5));

                        else
                            deathYr = 2013;

                        System.out.println(result.getString(4) + "\t" + result.getString(5) + "\t");
                        age = deathYr - birthYr;
                        System.out.println(age);
                    }
                } }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
