// This program simply builds and prints a FamilyTree from the Rhesus monkey database

package analytics;

public class FamilyTreeTest {
    public static void main(String[] args)
    {
        analytics.FamilyTree familytree = new analytics.FamilyTree("CayoSantiagoRhesusDB", "CSRhesusSubject", "1");
        familytree.printTree();
    }
}