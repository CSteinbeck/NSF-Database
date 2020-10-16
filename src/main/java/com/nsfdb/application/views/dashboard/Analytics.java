package com.nsfdb.application.views.dashboard;

import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Analytics {
    private Statement stmt;
    private SqlServerDbAccessor dba;

    public Analytics() {
        dba = new SqlServerDbAccessor();
        dba.setDbName("KerryO");
        dba.connectToDb();
//		stmt = dba.getStmt();
    }

    protected HashMap<String, String> getMonkeyInfo(String subjectId) {
        String query = "EXEC GetMonkey " + subjectId;
        HashMap<String, String> data = new HashMap<String, String>();
        ResultSet result = null;
        try {
            stmt = dba.getConnection().createStatement();
            result = stmt.executeQuery(query);

            // get meta data from result and print column labels
            ResultSetMetaData meta = result.getMetaData();
            int columns = meta.getColumnCount();

            //int j;
//			for (j=1; j<=columns; j++)
//				data.put(meta.getColumnName(j), "");
//			System.out.println(meta.getColumnName(j));


            while (result.next()) {
                for (int i = 1; i <= columns; i++)
                    data.put(meta.getColumnName(i), result.getString(i));
//					System.out.print(result.getString(i) +
//							((i==columns)?"":",\t"));
//				System.out.println();
            }
        } catch (SQLException e) {
//			e.printStackTrace();
        }

        return data;
    }
}
