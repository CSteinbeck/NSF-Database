/*  MonkeyNode Class
    Private Variables: Monkey monkey                  - the actual subject entry
                       ArrayList<MonkeyNode> children - the list of children

    Private Methods  : buildChildrenList - called by constructor, queries the sql server for all
                                         - children of this monkey and adds them to the children list

    Public Methods   : getters         - for the monkey and children list
                       printMonkeyNode - used for testing, prints the monkey subject id with tabs indicating gen number
                       childrenCount   - returns the size of children list
                       hasChildren     - returns (this.childrenCount > 0)
                       getIcon         - determines which icon to give the node in the GUI (mother, female (no kids), male)

    This class is the engine of the family tree. Each node has a monkey and a list of children nodes.
 */

package com.nsfdb.application.analytics.FamilyTree;

import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.nsfdb.application.analytics.SqlServerDbAccessor;
import com.nsfdb.application.analytics.Monkey;
import com.nsfdb.application.analytics.FamilyTree.*;

public class MonkeyNode {
    // Each monkey node has a monkey and a list of children nodes
    private Monkey monkey;
    private ArrayList<MonkeyNode> children;

    // Use this constructor if you do not have the Monkey object instantiated yet
    // Takes a dba, name of the database table, and monkey SequenceId
    public MonkeyNode(SqlServerDbAccessor dba, String table, String id)
    {
        this.monkey = new Monkey(dba, table, id); // initialize the monkey
        this.children = new ArrayList<MonkeyNode> ();
        buildChildrenList(dba, table); // build the children list
    }

    // Use this constructor if you have a Monkey object instantiated externally
    public MonkeyNode(SqlServerDbAccessor dba, String table, Monkey monkey )
    {
        this.monkey = monkey; // initialize the monkey
        this.children = new ArrayList<MonkeyNode>();
        buildChildrenList(dba, table); // build the children list
    }

    // This method queries the database for all children of the given monkey node.
    // Then, it builds a monkey node for each child and appends them to the list.
    // Note that the monkey node constructor calls this method, thus one constructor
    // call for the root monkey node creates the entire tree.
    private void buildChildrenList( SqlServerDbAccessor dba, String table )
    {
        // The query selects all children of the current monkey
        String query = "SELECT * FROM " + table + " WHERE MotherId='" + this.monkey.getSubjectId() + "'";

        try {
            Statement stmt = dba.getConnection().createStatement();
            ResultSet result = stmt.executeQuery(query);

            // after the query executes, we have a list of results. Loop through them and make nodes of each
            while (result.next()) {
                MonkeyNode child = new MonkeyNode(dba, table, new Monkey(result));
                this.children.add(child);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Used for testing, called by the FamilyTree print method.
    // Recursively prints each node in the tree
    public void printMonkeyNode( int gen )
    {
        for (int i = 0; i < gen; i++)
            System.out.print("-\t");

        System.out.println(this.monkey.getSubjectId());

        for (int i = 0; i < this.children.size(); i++)
            this.children.get(i).printMonkeyNode(gen+1);
    }

    public boolean hasChildren()
    {
        return this.children.size() > 0;
    }

    public int childrenCount()
    {
        return this.children.size();
    }

    public ArrayList<MonkeyNode> getChildren()
    {
        return this.children;
    }

    public Monkey getMonkey()
    {
        return this.monkey;
    }

    public boolean hasChild( String id ) {
        boolean child = false;

        for (int i = 0; i < this.children.size(); i++) {
            if (this.children.get(i).monkey.getSubjectId().compareTo(id) == 0)
                child = true;
        }

        return child;
    }

    public String getBirthDay() { return this.monkey.getBirthYear(); }

    public String getIcon() {
        switch (this.monkey.getGender()) {
            case "f":
                if (this.hasChildren()) {
                    return "MotherMonkey.png";
                } else {
                    return "FemaleMonkey.png";
                }
            case "m":
                return "MaleMonkey.png";
            default:
                return "UnknownGen.png";
        }
    }
}