/*  FamilyTree Class
    Private variables: root
    Private methods  : n/a
    Public methods   : printTree()

    This class is a client-side class for the FamilyTree. It has the root MonkeyNode
    and all methods which the GUI team will use to access the tree.
 */

package analytics;

import java.sql.Statement;

public class FamilyTree {
    private analytics.MonkeyNode root;

    public FamilyTree( String dbName, String table, String rootId )
    {
        analytics.SqlServerDbAccessor dba = new analytics.SqlServerDbAccessor("csdata.cd4sevot432y.us-east-1.rds.amazonaws.com", "administrator", "MercerBear$2020", "CayoSantiagoRhesusDB");
        dba.setDbName(dbName);
        dba.connectToDb();

        this.root = new analytics.MonkeyNode(dba, table, rootId);
    }

    public FamilyTree(analytics.SqlServerDbAccessor dba, String table, String rootId )
    {
        this.root = new analytics.MonkeyNode(dba, table, rootId);
    }

    public void printTree()
    {
        root.printMonkeyNode(0);
    }
}
