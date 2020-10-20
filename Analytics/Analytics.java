/*  This class is a work in progress.

    Essentially, the Analytics class will serve as a high-level, simple class
    which the GUI team can use to access all of our programs. The benefit of a
    unified analytics class is that we only need to declare one database-accessor
    per table.

    As you finish analytics programs, you can add them as private variables and
    put them in the constructor.
 */

package analytics;

public class Analytics {
    private analytics.SqlServerDbAccessor dba;
    private analytics.FamilyTree familytree;

    public Analytics( String dbName )
    {
        this.dba = new analytics.SqlServerDbAccessor();
        this.dba.setDbName(dbName);
        this.dba.getConnection();

        this.familytree = new analytics.FamilyTree(dba, "CayoSantiagoRhesusDB", "1");
    }
}
