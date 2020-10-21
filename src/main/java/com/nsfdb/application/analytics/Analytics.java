/*  This class is a work in progress.

    Essentially, the Analytics class will serve as a high-level, simple class
    which the GUI team can use to access all of our programs. The benefit of a
    unified analytics class is that we only need to declare one database-accessor
    per table.

    As you finish analytics programs, you can add them as private variables and
    put them in the constructor.
 */
package com.nsfdb.application.analytics;

import com.nsfdb.application.analytics.FamilyTree.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Analytics {
    private SqlServerDbAccessor dba;
    private FamilyTree familytree;

    public Analytics( String dbName )
    {
        this.dba = new SqlServerDbAccessor();
        this.dba.setDbName(dbName);
        this.dba.getConnection();

        this.familytree = new FamilyTree(dbName, "CSRhesusSubject", "1");
    }

    public List<MonkeyNode> getMonkeyList() {
        return familytree.getMonkeyList();
    }

    public List<Monkey> getMonkeys() {
        List<Monkey> allMonkeys = new ArrayList<>();
        for (MonkeyNode mN : familytree.getMonkeyList()) {
            allMonkeys.add(mN.getMonkey());
        }
        return allMonkeys;
    }

    public List<Monkey> getRootMonkey() {
        List<Monkey> rootMonkeys = new ArrayList<>();
        rootMonkeys.add(familytree.getMonkeyList().get(0).getMonkey());
        return rootMonkeys;
    }

    public List<Monkey> getChildMonkies(String motherID) {
        List<Monkey> allMonkeys = new ArrayList<>();
        for (MonkeyNode mN : familytree.getMonkeyList()) {
            allMonkeys.add(mN.getMonkey());
        }
        return allMonkeys.stream().filter( monkey -> monkey.getMotherId() == motherID).collect(Collectors.toList());
    }

}
