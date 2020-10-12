package csdbgui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import csdbdao.SqlServerDbAccessor;

public class SubjectDetailsDialog extends JDialog {
	
	private Object[] basicDetailContents;
	private String[] basicDetailColumnNames;
	private Object[][] scanContents;
	private String[] scanColumnNames;
	
	private SqlServerDbAccessor sqda;
	
	private JPanel contentPanel;
	private JPanel controlPanel;
	private JPanel textFieldPanel;
	private JPanel photoPanel;
	private JLabel jlblPhoto;
	private int currScan;
	private String subjectId;
		
	public SubjectDetailsDialog(JFrame parent, SqlServerDbAccessor sqda, String subjectId) {
	    super(parent, "Subject Details", true);
	    this.sqda = sqda;
	    this.subjectId = subjectId;
	    currScan = 0;
	    sqda.connectToDb();
	    updatePhotoDialog();
	
	}
	
	
	private void updatePhotoDialog() {
	    contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		
		textFieldPanel = prepareFieldPanel(this.subjectId);
		contentPanel.add(textFieldPanel, BorderLayout.NORTH);
		
		controlPanel = new JPanel();
		JButton btn = new JButton("Done");

		btn.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
			
		}); 
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
		
		pack();
		
		
	}
	
	
	protected void setValuesForRow(int currRow2) {
		// TODO Auto-generated method stub
		
	}
	
	private JPanel prepareFieldPanel(String subject) {
	    JPanel panel = new JPanel(new GridBagLayout());
	    GridBagConstraints cs = new GridBagConstraints();
	
	    cs.fill = GridBagConstraints.HORIZONTAL;
	    
	    String SQL = "SELECT FamilyId, Generation, MotherId, BirthYear, DeathYear, Gender, OriginalSubjectId, SequenceId FROM CSRhesusSubject WHERE OriginalSubjectId = '"+subject+"'";
	    String[] cols = {"FamilyId", "Generation", "MotherId", "Birth Year", "Death Year", "Gender", "OriginalSubjectId", "SequenceId" };
	    
	    basicDetailColumnNames = cols;
	    
		LinkedList<Object[]> contents = new LinkedList<Object[]>();

	    try {
			Statement stmt = sqda.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			Object[] row = new Object[basicDetailColumnNames.length];
			while(rs.next()) {
				for (int i=1; i<=basicDetailColumnNames.length; i++) {
						System.out.print(rs.getString(i) + ((i==basicDetailColumnNames.length)?"":", "));
						row[i-1] = ((rs.getString(i) == null) ? "" : rs.getString(i));
				}
			}
			System.out.println();
			contents.add(row);
			basicDetailContents = contents.get(0);
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		
		String SQL2 = "SELECT ScanID, MethodID, ScanDate, Site, Ontology, SOS, Notes FROM Scan WHERE SubjectID = "+basicDetailContents[6];
		String[] cols2 = {"ScanID", "MethodID", "ScanDate", "Site", "Ontology", "SOS", "Notes"};
		
		scanColumnNames = cols2;
		    
		LinkedList<Object[]> contents2 = new LinkedList<Object[]>();

	    try {
			Statement stmt = sqda.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(SQL2);
			Object[] row = new Object[scanColumnNames.length];
			while(rs.next()) {
				for (int i=1; i<=scanColumnNames.length; i++) {
					System.out.print(rs.getString(i) + ((i==scanColumnNames.length)?"":", "));
					row[i-1] = ((rs.getString(i) == null) ? "" : rs.getString(i));
				}
				contents2.add(row);
				row = new Object[scanColumnNames.length];
				System.out.println();
			}
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		   

		
	    JLabel jcolLabel;
	    JLabel jdataLabel;
	    
	    Object data;
	    
	    for (int fieldSeq = 0; fieldSeq < basicDetailColumnNames.length; fieldSeq++) {
	    	jcolLabel = new JLabel(basicDetailColumnNames[fieldSeq] + ": ");
	    	cs.gridx = 0;
	    	cs.gridy = fieldSeq;
	    	cs.gridwidth = 1;
	    	panel.add(jcolLabel, cs);
	    	
	    	data = basicDetailContents[fieldSeq];
	
	    	jdataLabel = new JLabel((data==null)?"":data.toString());
	 
	        cs.gridx = 1;
	        cs.gridy = fieldSeq;
	        cs.gridwidth = 2;
	        panel.add(jdataLabel, cs);
	    }

	    
	    if(contents2.size() > 0) {
	    	
		    scanContents = new Object[contents2.size()][contents2.get(0).length];
			for (int i=0; i<contents2.size(); i++) {
				scanContents[i] = contents2.get(i);
			}

	    	for (int fieldSeq = basicDetailColumnNames.length; fieldSeq < (basicDetailColumnNames.length+scanColumnNames.length); fieldSeq++) {
		    	jcolLabel = new JLabel(scanColumnNames[fieldSeq-basicDetailColumnNames.length] + ": ");
		    	cs.gridx = 0;
		    	cs.gridy = fieldSeq;
		    	cs.gridwidth = 1;
		    	panel.add(jcolLabel, cs);
		    	
		    	data = scanContents[currScan][fieldSeq-basicDetailColumnNames.length];
		
		    	jdataLabel = new JLabel((data==null)?"":data.toString());
		 
		        cs.gridx = 1;
		        cs.gridy = fieldSeq;
		        cs.gridwidth = 2;
		        panel.add(jdataLabel, cs);
		    }
	    	

	    	
	    	String SQL3 = "SELECT ImageBlob FROM ImageSeries WHERE ScanID = "+scanContents[currScan][0];

		    try {
				Statement stmt = sqda.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery(SQL3);

			    JLabel jimgLabel;
			    int count = 0;
				while(rs.next()) {
					try {
						Image img = ImageIO.read(rs.getBinaryStream(1));
						jimgLabel = new JLabel();
						ImageIcon photoIcon = new ImageIcon(img);
						jimgLabel.setIcon(photoIcon);
						cs.gridx = count;
				    	cs.gridy = basicDetailColumnNames.length+scanColumnNames.length;
				    	cs.gridwidth = 1;
						panel.add(jimgLabel, cs);
						count++;

					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}  catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    }
	    
	    
		return panel;
	}
	
	public static void main(String[] args) {
		SqlServerDbAccessor sqda = new SqlServerDbAccessor();
		sqda.setDbName("CayoSantiagoRhesusDB");
		sqda.connectToDb();
		SubjectDetailsDialog d = new SubjectDetailsDialog(null, sqda, "22");
		d.setVisible(true);
	
	}
	
}
