/*  MonkeyNode Class
    Private Variables: Monkey monkey,
                       ArrayList<MonkeyNode> children
    Private Methods  : buildChildrenList
    Public Methods   : many lmao

    This class is the engine of the family tree. Each node has a monkey and a list of children nodes.
 */

package analytics;

import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MonkeyNode {
    // Each monkey node has a monkey and a list of children nodes
    private analytics.Monkey monkey;
    private ArrayList<MonkeyNode> children;

    // Use this constructor if you do not have the Monkey object instantiated yet
    // Takes a dba, name of the database table, and monkey SequenceId
    public MonkeyNode(analytics.SqlServerDbAccessor dba, String table, String id)
    {
        this.monkey = new analytics.Monkey(dba, table, id);
        this.children = new ArrayList<MonkeyNode> ();
        buildChildrenList(dba, table);
    }

    // Use this constructor if you have a Monkey object instantiated externally
    public MonkeyNode(analytics.SqlServerDbAccessor dba, String table, analytics.Monkey monkey )
    {
        this.monkey = monkey;
        this.children = new ArrayList<MonkeyNode>();
        buildChildrenList(dba, table);
    }

    //
    private void buildChildrenList(analytics.SqlServerDbAccessor dba, String table )
    {
        String[] DbCols = {"*"};
        String query = "SELECT ";
        int index = 1;
        for (String col : DbCols) {
            query += col;
            if (index++ < DbCols.length)
                query += ", ";
        }
        query += " FROM " + table + " WHERE MotherId='" + this.monkey.getSubjectId() + "'";

        try {
            Statement stmt = dba.getConnection().createStatement();
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                MonkeyNode child = new MonkeyNode(dba, table, new analytics.Monkey(result));
                this.children.add(child);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public analytics.Monkey getMonkey()
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
}