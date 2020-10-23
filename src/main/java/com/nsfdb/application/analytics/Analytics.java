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
    private List<MonkeyNode> monkeyList;

    public Analytics( String dbName )
    {
        this.dba = new SqlServerDbAccessor();
        this.dba.setDbName(dbName);
        this.dba.getConnection();

        this.familytree = new FamilyTree(dbName, "CSRhesusSubject", "1");
        this.monkeyList = this.familytree.getMonkeyList();
    }

    public List<String> getMonkeySubjectIds() {
        List<String> monkeySubIds = new ArrayList<>();
        for (int i = 0; i < this.monkeyList.size(); i++) {
            if (this.monkeyList.get(i).getMonkey().getSubjectId() != null) {
                monkeySubIds.add(this.monkeyList.get(i).getMonkey().getSubjectId());
            }
        }
        return monkeySubIds;
    }

    public List<MonkeyNode> getMonkeys() {
        return this.monkeyList;
    }

    public List<MonkeyNode> getRootMonkies() {
        List<MonkeyNode> rootMonkeys = new ArrayList<>();
        for (int i = 0; i < this.monkeyList.size(); i++) {
            if (this.monkeyList.get(i).getMonkey().getMotherId() == null) {
                rootMonkeys.add(this.monkeyList.get(i));
            }
        }
        return rootMonkeys;
    }

    public List<MonkeyNode> getChildMonkies(MonkeyNode parent) {
        return parent.getChildren();
    }

}
