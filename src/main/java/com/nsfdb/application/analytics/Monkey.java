/*  Monkey Class
    Each object of this class holds all of the data for a monkey.
 */

package com.nsfdb.application.analytics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.nsfdb.application.analytics.FamilyTree.*;

public class Monkey
{
    // These are all of the columns in the Rhesus and Macaca monkey tables
    private String SequenceId;
    private String SubjectId;
    private String Gender;
    private String BirthYear;
    private String DeathYear;
    private String MotherId;
    private String Generation;
    private String FamilyId;

    // Use this constructor if you have not instantiated a database accessor
    // DbName = name of database ie. "CayoSantiagoRhesusDb"
    // table = name of table ie. CSRhesusSubject
    // id = sequence id of the monkey
    public Monkey( String DbName, String table, String id )
    {
        // declare a dba and get connection to server
        SqlServerDbAccessor dba = new SqlServerDbAccessor();
        dba.setDbName(DbName);
        dba.connectToDb();

        String query = "SELECT * FROM " + table + " WHERE SequenceId='" + id + "'";

        try {
            Statement stmt = dba.getConnection().createStatement();
            ResultSet result = stmt.executeQuery(query);

            // Initialize each of this monkeys properties from the query result
            result.next();
            this.SequenceId = result.getString(1);
            this.SubjectId = result.getString(2);
            this.Gender = result.getString(3);
            this.BirthYear = result.getString(4);
            this.DeathYear = result.getString(5);
            this.MotherId = result.getString(6);
            this.Generation = result.getString(7);
            this.FamilyId = result.getString(8);
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    // Use this constructor if you've externally declared your database accessor object
    // table = name of table ie. "CSRhesusSubject"
    // id = sequence id of monkey you want to make
    public Monkey(SqlServerDbAccessor dba, String table, String id )
    {
        String query = "SELECT * FROM " + table + " WHERE SequenceId='" + id + "'";

        try {
            Statement stmt = dba.getConnection().createStatement();
            ResultSet result = stmt.executeQuery(query);

            // Initialize this monkey's values from the query result
            result.next();
            this.SequenceId = result.getString(1);
            this.SubjectId = result.getString(2);
            this.Gender = result.getString(3);
            this.BirthYear = result.getString(4);
            this.DeathYear = result.getString(5);
            this.MotherId = result.getString(6);
            this.Generation = result.getString(7);
            this.FamilyId = result.getString(8);
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    // Use this constructor if you have already queried the database and have a result set
    // that you want to turn into a monkey
    public Monkey( ResultSet result )
    {
        try {
            this.SequenceId = result.getString(1);
            this.SubjectId = result.getString(2);
            this.Gender = result.getString(3);
            this.BirthYear = result.getString(4);
            this.DeathYear = result.getString(5);
            this.MotherId = result.getString(6);
            this.Generation = result.getString(7);
            this.FamilyId = result.getString(8);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters for all monkey variables
    public String getSequenceId() {return this.SequenceId;}
    public String getSubjectId() {return this.SubjectId;}
    public String getGender() {return this.Gender;}
    public String getBirthYear() {return this.BirthYear;}
    public String getDeathYear() {return this.DeathYear;}
    public String getMotherId() {return this.MotherId;}
    public String getGeneration() {return this.Generation;}
    public String getFamilyId() {return this.FamilyId;}

    @Override
    public String toString() { return (this.SubjectId + "-" + this.Gender + "(" +
            this.BirthYear + "-" + ((this.DeathYear == "R") ? "0" : this.DeathYear) + ")" +
            ((this.MotherId == null) ? "" : this.MotherId) + "-" + this.Generation + "#" + this.FamilyId); }
}
