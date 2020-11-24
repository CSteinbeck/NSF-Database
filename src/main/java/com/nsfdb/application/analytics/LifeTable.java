package com.nsfdb.application.analytics;//This is mostly re-purposed code from SelectTest.java
//TODO: create a LifeTable object to store birth year, death year, and age
//TODO: choose a data structure to make a table with. Considering a 2d array at the moment

import com.nsfdb.application.analytics.SqlServerDbAccessor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.lang.Math;

import static java.lang.Math.exp;

public class LifeTable {
    private Statement stmt;
    private SqlServerDbAccessor dba;

    public LifeTable() {
        dba = new SqlServerDbAccessor();
        dba.setDbName("CayoSantiagoRhesusDB"); // use for default database
        //dba.setDbName("KerryO"); // use this line for Kerry's database
        dba.connectToDb();
        //stmt = dba.getStmt();
    }

    public static void main(String[] args) {
        LifeTable lt = new LifeTable();
        String[] cols = {"*"};
        lt.createLifeTable("CSRhesusSubject", cols); // use if using default db

        ArrayList<ArrayList<Double>> lifeTable = lt.createLifeTable("CSRhesusSubject", cols);
        //print life table
        printLifeTable(lifeTable);
    }


    private ArrayList<ArrayList<Double>> createLifeTable(String table, String[] cols) {
        ArrayList<ArrayList<Double>> lifeTable = new ArrayList<>();

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

            int count = 0;
            double ltAge = 0;
            double ltTot = 0;
            double ltDeath = 0;

            while (result.next()) {
                lifeTable.add(new ArrayList());

                ltAge = Double.parseDouble(result.getString(1));
                ltTot = Double.parseDouble(result.getString(2));
                ltDeath = Double.parseDouble(result.getString(3));

                lifeTable.get(count).add(ltAge);
                lifeTable.get(count).add(ltTot);
                lifeTable.get(count).add(ltDeath);

                count++;
            }

            /*
            LifeTable Columns
            Age = 0
            Total = 1
            Deaths = 2
            Nx = 3
            mx = 4
            qx = 5
            px = 6
            Ix = 7
            dx = 8
            Lx = 9
            Tx = 10
            Ex = 11
            */

            //getting NX is getting the all of the subjects who lived through that age
            double NX;
            for (int i = 0; i < lifeTable.size(); i++) {
                NX = 0;

                for (int x = i; x < lifeTable.size(); x++)
                    NX += lifeTable.get(x).get(1);

                lifeTable.get(i).add(NX);
            }

            //mx = Death/NX
            double mx;
            double d;
            double nx;
            for (int i = 0; i < lifeTable.size(); i++) {
                d = lifeTable.get(i).get(2);
                nx = lifeTable.get(i).get(3);
                mx = d / nx;
                lifeTable.get(i).add(mx);
            }

            //qx = 1 - exp(-mx)
            double qx;
            for (int i = 0; i < lifeTable.size(); i++) {
                qx = 1 - exp(-lifeTable.get(i).get(4));
                lifeTable.get(i).add(qx);
            }

            //px = 1 - qx
            double px;
            for (int i = 0; i < lifeTable.size(); i++) {
                px = 1 - lifeTable.get(i).get(5);
                lifeTable.get(i).add(px);
            }

            //lx starts at 1
            //lx = (previous rows) lx * px
            double Ix = 1;
            for (int i = 0; i < lifeTable.size(); i++) {
                if (i == 0)
                    Ix = 1;
                else
                    Ix = lifeTable.get(i - 1).get(7) * lifeTable.get(i - 1).get(6);
                lifeTable.get(i).add(Ix);
            }

            //dx = qx * Ix
            double dx;
            for (int i = 0; i < lifeTable.size(); i++) {
                dx = lifeTable.get(i).get(5) * lifeTable.get(i).get(7);
                lifeTable.get(i).add(dx);
            }

            //Lx = dx/mx
            double Lx;
            for (int i = 0; i < lifeTable.size(); i++) {
                if (lifeTable.get(i).get(4) != 0.0)
                    Lx = lifeTable.get(i).get(8) / lifeTable.get(i).get(4);
                else
                    Lx = 0.8;
                lifeTable.get(i).add(Lx);
            }

            //getting Tx is getting the sum of the previous column starting from that row
            double Tx;
            for (int i = 0; i < lifeTable.size(); i++) {
                Tx = 0;

                for (int x = i; x < lifeTable.size(); x++)
                    Tx += lifeTable.get(x).get(9);

                lifeTable.get(i).add(Tx);
            }

            //Ex = Tx / Ix
            double Ex;
            for (int i = 0; i < lifeTable.size(); i++) {
                if (lifeTable.get(i).get(4) != 0.0)
                    Ex = lifeTable.get(i).get(10) / lifeTable.get(i).get(7);
                else
                    Ex = 0;
                lifeTable.get(i).add(Ex);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lifeTable;
    }

    private static void printLifeTable(ArrayList<ArrayList<Double>> lifeTable)
    {
        String columns [] = {"Age", "Total", "Deaths", "NX", "mx", "qx", "px", "Ix", "dx", "Lx", "Tx", "Ex"};
        for(int i = 0; i < columns.length; i++)
            System.out.print(columns[i] + "\t\t");

        System.out.println("");

        for(int i = 0; i < lifeTable.size(); i++) {
            for (int x = 0; x < lifeTable.get(i).size(); x++) {
                System.out.print(lifeTable.get(i).get(x) + "\t\t");
            }
            System.out.println("");
        }
    }
}
