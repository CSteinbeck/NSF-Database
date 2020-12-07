package csdbgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import csdbdao.DbServerJTableModelAdaptor;
import csdbdao.SqlServerDbAccessor;

public class ViewImageSeriesContents extends JDialog {
	private ResultSet rows;
	private Object[][] tableContents;
	private String[] columnNames;
	private JFrame parent;
	private String table;
	private Map<String, JTextField> fieldsByName;
	private SqlServerDbAccessor sqda;
	
	private JPanel contentPanel;
	private JPanel controlPanel;
	private JPanel textFieldPanel;
	private JPanel photoPanel;
	private JTextArea jtfPhotoDesc;
	private JLabel jlblPhoto;
	
	private int currRow;

	public ViewImageSeriesContents(JFrame parent, SqlServerDbAccessor sqda, 
    		String table) {
	    super(parent, "Image Series Content Browser", true);
	    this.sqda = sqda;
	    sqda.connectToDb();
	    this.table = table;
	    fieldsByName = new HashMap<String, JTextField>();
		
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		
		jtfPhotoDesc = new JTextArea(3, 40);
		Border blackline = BorderFactory.createLineBorder(Color.BLACK);		
		TitledBorder title = BorderFactory.createTitledBorder(
                blackline, "Photo Desc");
		title.setTitleJustification(TitledBorder.CENTER);
		jtfPhotoDesc.setBorder(title);
		contentPanel.add(jtfPhotoDesc, BorderLayout.SOUTH);
		
		// get data for display
	    DbServerJTableModelAdaptor dao = new DbServerJTableModelAdaptor(sqda);
	    columnNames = dao.getColNamesForTable(table);	    
	    tableContents = dao.getData(table);
	    currRow = 0;

		
		textFieldPanel = prepareFieldPanel();
		contentPanel.add(textFieldPanel, BorderLayout.NORTH);
		
		photoPanel = // preparePhotoPanel();	/*
		new JPanel();
		Dimension photoDim = new Dimension(450, 350);
		photoPanel.setMinimumSize(photoDim);
		photoPanel.setMaximumSize(photoDim);
		photoPanel.setPreferredSize(photoDim);
		jlblPhoto = new JLabel();
		ImageIcon photoIcon = new ImageIcon("Images/Jaw.png");
		//try {
			//Image photo = (Image)tableContents[currRow][4];
			//ImageIcon photoIcon = new ImageIcon(photo);
			jlblPhoto.setIcon(photoIcon);
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		photoPanel.add(jlblPhoto);
		//*/
		contentPanel.add(photoPanel);
		
		controlPanel = new JPanel();
		String[] btnNames = {"Previous", "Edit", "Delete", "Add", "Next"}; 
		JButton[] btns = new JButton[btnNames.length];
		for (int i=0; i<btnNames.length; i++) {
			btns[i] = new JButton(btnNames[i]);
			controlPanel.add(btns[i]);
		}
		
		// event handling to be enhanced 
		if (currRow == 0) 
			btns[0].setEnabled(false);
		else if (currRow == tableContents.length - 1)
			btns[btns.length - 1].setEnabled(false);
		
		// handles next button
		btns[btns.length - 1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				currRow++;
				if (currRow == tableContents.length - 1)
					btns[btns.length - 1].setEnabled(false);
				if (currRow > 0) 
					btns[0].setEnabled(true);
				System.out.println("currRow = " + currRow);
				setValuesForRow();
			}
			
		}); 
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
		
		pack();

	}
	
	protected void setValuesForRow() {
		JTextField jtf;
		Object data;
		for (int i = 1; i < textFieldPanel.getComponentCount(); i += 2) {
			jtf = (JTextField) textFieldPanel.getComponent(i);
			data = tableContents[currRow][i/2];
			jtf.setText((data==null)?"":data.toString());
		}
		// reset image
		Image photo = (Image)tableContents[currRow][4];
		ImageIcon photoIcon = new ImageIcon(photo);
		//JOptionPane.showConfirmDialog(null, "Which Image ...", 
		//		"Scan Image Test", 0, 0, photoIcon);
		jlblPhoto.setIcon(photoIcon);
		// reset text area
	    data = tableContents[currRow][5];
	    jtfPhotoDesc.setEnabled(false);
	    jtfPhotoDesc.setText((data==null)?"":data.toString());
	    //jtfPhotoDesc.append("-->" + currRow); // for testing
		
		repaint();
	}

	private JPanel preparePhotoPanel() {
		JPanel pp = new JPanel();
		Dimension photoDim = new Dimension(320, 350);
		pp.setMinimumSize(photoDim);
		pp.setMaximumSize(photoDim);
		pp.setPreferredSize(photoDim);
		jlblPhoto = new JLabel();
		//ImageIcon photoIcon = new ImageIcon("image/user_cu_04.png");
		//try {
			Image photo = (Image)tableContents[currRow][4];
			ImageIcon photoIcon = new ImageIcon(photo);
			//JOptionPane.showConfirmDialog(null, "Which Image ...", 
			//		"Scan Image Test", 0, 0, photoIcon);
			jlblPhoto.setIcon(photoIcon);
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		pp.add(jlblPhoto);
		
		return pp;
	}

	private JPanel prepareFieldPanel() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    GridBagConstraints cs = new GridBagConstraints();
	
	    cs.fill = GridBagConstraints.HORIZONTAL;
	    
	    /*
	     * move to constructor
	    DbServerJTableModelAdaptor dao = new DbServerJTableModelAdaptor(sqda);
	    columnNames = dao.getColNamesForTable(table);
	    
	    tableContents = dao.getData(table);
	    //currRow = 0;
	    */
	    
	    JLabel jLabel;
	    JTextField jTextField;
	    
	    Object data;
	    
	    for (int fieldSeq = 0; fieldSeq < 4; fieldSeq++) {
	    	jLabel = new JLabel(columnNames[fieldSeq] + ": ");
	    	cs.gridx = 0;
	    	cs.gridy = fieldSeq;
	    	cs.gridwidth = 1;
	    	panel.add(jLabel, cs);
	
	    	jTextField = new JTextField(30);
	    	
	    	data = tableContents[currRow][fieldSeq];
	    	
	    	jTextField.setText((data==null)?"":data.toString());
	    	jTextField.setEnabled(false);
	        cs.gridx = 1;
	        cs.gridy = fieldSeq;
	        cs.gridwidth = 2;
	        panel.add(jTextField, cs);
	        fieldsByName.put(columnNames[fieldSeq], jTextField);
	    }
	    
	    data = tableContents[currRow][5];
	    jtfPhotoDesc.setEnabled(false);
	    jtfPhotoDesc.setText((data==null)?"":data.toString());
	    //jtfPhotoDesc.append("-->" + currRow); // for testing
		
	    for (Component c : panel.getComponents()) {
	    	System.out.println(c.getClass() + "@" + c.getLocation());
	    }
		return panel;
	}

	public static void main(String[] args) {
		SqlServerDbAccessor sqda = new SqlServerDbAccessor(
        		"csdata.cd4sevot432y.us-east-1.rds.amazonaws.com",
        		"NsfResearcher", "r2h0e2S0u5s", "CayoSantiagoRhesusDB");
		ViewImageSeriesContents d = new ViewImageSeriesContents(null,
    			sqda, "ImageSeries");
		d.setVisible(true);
	}

}
