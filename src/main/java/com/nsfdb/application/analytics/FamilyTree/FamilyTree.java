/*  FamilyTree Class
    Private variables: root
    Private methods  : n/a
    Public methods   : printTree()

    This class is a client-side class for the FamilyTree. It has the root MonkeyNode
    and all methods which the GUI team will use to access the tree.
 */

package com.nsfdb.application.analytics.FamilyTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.sql.Statement;
import com.nsfdb.application.analytics.SqlServerDbAccessor;
import com.nsfdb.application.analytics.FamilyTree.*;

public class FamilyTree {
    private MonkeyNode root;
    private List<MonkeyNode> MonkeyList;

    public FamilyTree( String dbName, String table, String rootId )
    {
        SqlServerDbAccessor dba = new SqlServerDbAccessor("csdata.cd4sevot432y.us-east-1.rds.amazonaws.com", "administrator", "MercerBear$2020", "CayoSantiagoRhesusDB");
        dba.setDbName(dbName);
        dba.connectToDb();

        this.root = new MonkeyNode(dba, table, rootId);
        this.MonkeyList = getAllMonkeys(this.root);
    }

    public FamilyTree(SqlServerDbAccessor dba, String table, String rootId )
    {
        this.root = new MonkeyNode(dba, table, rootId);
        this.MonkeyList = getAllMonkeys(this.root);
    }

    public void printTree()
    {
        root.printMonkeyNode(0);
    }

    private List<MonkeyNode> getAllMonkeys(MonkeyNode root) {
        List<MonkeyNode> monkeys = new ArrayList<>();
        getAllMonkeys(monkeys, root);
        return monkeys;
    }

    private void getAllMonkeys(List<MonkeyNode> mL, MonkeyNode root) {
        if (root==null)
            return;
        mL.add(root);
        if (root.hasChildren()) {
            for (MonkeyNode child : root.getChildren()) {
                getAllMonkeys(mL, child);
            }
        }
    }

    public List<MonkeyNode> getMonkeyList() {
        return MonkeyList;
    }
}
