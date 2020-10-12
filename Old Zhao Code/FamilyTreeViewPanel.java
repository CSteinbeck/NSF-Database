package csdbgui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import csdbdao.SqlServerDbAccessor;
import csdbvo.SubjectEntry;


public class FamilyTreeViewPanel extends JPanel 
		implements TreeSelectionListener {
    private JTree tree;
    private Map<Integer, List<SubjectEntry>> subjectsByGen;
    private SubjectEntry founder;
    
    int count = 0;
	
    public FamilyTreeViewPanel() {
        super(new GridLayout(1,0));
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        
		FamilyDataMgr mgr = new FamilyDataMgr("CSRhesusSubject");
		subjectsByGen = mgr.getSubjectsByGen();
		founder = subjectsByGen.get(0).get(0);
		
		Dimension d = new Dimension(500, 370);
		this.setMaximumSize(d);
		this.setMinimumSize(d);
		this.setPreferredSize(d);

		System.out.println(founder);

    	DefaultMutableTreeNode top =
            new DefaultMutableTreeNode(founder);
    	count++;
        createNodes(top);
 
        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                   tree.getLastSelectedPathComponent();

            /* if nothing is selected */ 
                if (node == null) return;

            /* retrieve the node that was selected */ 
                Object nodeInfo = node.getUserObject();
            /* React to the node selection. */
                
                System.out.println(nodeInfo);
                
                SqlServerDbAccessor sqda = new SqlServerDbAccessor();
        		sqda.setDbName("CayoSantiagoRhesusDB");
        		sqda.connectToDb();
        		
        		String subjectId = nodeInfo.toString().substring(0, nodeInfo.toString().indexOf('-'));
                
                SubjectDetailsDialog detDlg = new SubjectDetailsDialog(topFrame, sqda, subjectId);
				detDlg.setVisible(true);
                
                
                
            }
        });
        
        //Enable tool tips.
        ToolTipManager.sharedInstance().registerComponent(tree);
 
        //Set the icon for leaf nodes.
        //ImageIcon tutorialIcon = createImageIcon("images/middle.gif");
        ImageIcon momIcon = new ImageIcon("Images/MomRhesus.png");
        ImageIcon boyIcon = new ImageIcon("Images/MaleSymbol.png");
        ImageIcon girlIcon = new ImageIcon("Images/FemaleSymbol.png");
        //ImageIcon momIcon = new ImageIcon("Images/MomMonkey.png");
        //ImageIcon boyIcon = new ImageIcon("Images/BabyMonkey.png");
        //ImageIcon girlIcon = new ImageIcon("Images/BabyMonkeyF.png");
        ImageIcon unknownIcon = new ImageIcon("Images/UnknownGen.png");
        tree.setCellRenderer(new MyRenderer(momIcon, boyIcon, girlIcon, unknownIcon));
        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);        
        
        //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);
        add(treeView);

        System.out.println("count=" + count);
    }

	private void createNodes(DefaultMutableTreeNode top) {
		int gen = 1;
		List<SubjectEntry> firstGen = subjectsByGen.get(gen);
		List<DefaultMutableTreeNode> nodesInCurGen =
				new LinkedList<DefaultMutableTreeNode>();
		List<DefaultMutableTreeNode> nodesInChildGen =
				new LinkedList<DefaultMutableTreeNode>();
		
		DefaultMutableTreeNode childNode, momNode;
		
		// deals with 1st gen subjects
		for (SubjectEntry se : firstGen) {
			childNode = new DefaultMutableTreeNode(se);
			count++;
			//childNode.setParent(top);
			top.add(childNode);
			nodesInCurGen.add(childNode);
			
			System.out.println(se.getOriginalId() + "->" + 
					childNode.getUserObject());
		}
		
		List<SubjectEntry> allInMomsGen = firstGen;
		
		List<SubjectEntry> allInCurGen;
		SubjectEntry mom;
		gen++;
		while (subjectsByGen.containsKey(gen)) {
		//while (gen < 12) {
			allInCurGen = subjectsByGen.get(gen);
			for (SubjectEntry se : allInCurGen) {

				childNode = new DefaultMutableTreeNode(se);
				count++;
				//mom = lookupMomNodeinGen(se.getMotherID(), allInMomsGen);
				momNode = lookupMomNodeinGen(se.getMotherID(), nodesInCurGen);
				//childNode.setParent(momNode);
				momNode.add(childNode);
				nodesInChildGen.add(childNode);
				//System.out.println(gen + "::" + se.getOriginalId() + "-->" 
						//+ momNode.getUserObject()
						//);
			}

			nodesInCurGen = nodesInChildGen;
			nodesInChildGen = new LinkedList<DefaultMutableTreeNode>();
			System.out.println(gen);
			gen++;
		}
	}

	/*
	private SubjectEntry lookupMomNodeinGen(String motherID, 
			List<SubjectEntry> allInMomsGen) {
		// TODO Auto-generated method stub
		return null;
	}
	*/

	private DefaultMutableTreeNode lookupMomNodeinGen(String motherID, 
			List<DefaultMutableTreeNode> nodes) {
		DefaultMutableTreeNode momNode = null;
		SubjectEntry se;
		for (DefaultMutableTreeNode node : nodes) {
			se = (SubjectEntry)node.getUserObject();
			//System.out.println(se.getOriginalId() + "<=>" + motherID);
			if (se.getOriginalId().equals(motherID)) {
				momNode = node;
				break;
			}
		}
		return momNode;
	}
	
    private class MyRenderer extends DefaultTreeCellRenderer {
        Icon momIcon;
        Icon boyIcon;
        Icon girlIcon;
        Icon unknownIcon;
 
        public MyRenderer(ImageIcon icon0, Icon icon, Icon icon2, Icon icon3) {
        	momIcon = icon0;
        	boyIcon = icon;
        	girlIcon = icon2;
        	unknownIcon = icon3;
        }
 
        @Override
		public Component getTreeCellRendererComponent(
                            JTree tree,
                            Object value,
                            boolean sel,
                            boolean expanded,
                            boolean leaf,
                            int row,
                            boolean hasFocus) {
 
            super.getTreeCellRendererComponent(
                            tree, value, sel,
                            expanded, leaf, row,
                            hasFocus);
            if (!leaf)
            	setIcon(momIcon);
            else if (genderCode(value) == 'm') {
                setIcon(boyIcon);
                setToolTipText("It's a boy.");
            }
            else if (genderCode(value) == 'f') {
                    setIcon(girlIcon);
                    setToolTipText("It's a girl.");
            } else {
                setIcon(unknownIcon);
                setToolTipText(null); //no tool tip
            }
 
            return this;
        }
 
        protected char genderCode(Object value) {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)value;
            SubjectEntry nodeInfo = 
                    (SubjectEntry)(node.getUserObject());
 
            return nodeInfo.getGender();
        }
    }


	public static void main(String[] args) {
		JFrame f = new JFrame("Family Tree Panel Test");

		FamilyTreeViewPanel treeView = new FamilyTreeViewPanel();
		
		f.add(treeView);
		f.setSize(800, 500);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
