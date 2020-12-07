// This program simply builds and prints a FamilyTree from the Rhesus monkey database

package com.nsfdb.application.analytics.FamilyTree;

import com.nsfdb.application.analytics.SqlServerDbAccessor;
import com.nsfdb.application.analytics.Monkey;
import com.nsfdb.application.analytics.FamilyTree.*;

public class FamilyTreeTest {
    public static void main(String[] args)
    {
        FamilyTree familytree = new FamilyTree("CayoSantiagoRhesusDB", "CSRhesusSubject", "1");
        familytree.printTree();
    }
}