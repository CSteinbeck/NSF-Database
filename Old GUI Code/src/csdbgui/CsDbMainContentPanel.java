package csdbgui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class CsDbMainContentPanel extends JPanel {
	private FamilyTreeViewPanel mp;
	private Vertical3ViewImagePanel imgP;
	private JPanel measurePanel;
	
	public CsDbMainContentPanel() {
		Dimension windowSize = new Dimension(900, 600);
		this.setPreferredSize(windowSize);
		this.setMaximumSize(windowSize);
		this.setMinimumSize(windowSize);

		this.setLayout(new BorderLayout());
		mp = new FamilyTreeViewPanel();
		add(mp, BorderLayout.CENTER);
		imgP = new Vertical3ViewImagePanel();
		imgP.loadImages("225");
		add(imgP, BorderLayout.WEST);
		measurePanel = new JPanel();
		measurePanel.add(new JLabel("Pathological Property Sheet"));
		add(measurePanel, BorderLayout.EAST);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame f = new JFrame("Main Panel Test");
		f.setIconImage(new ImageIcon("Images/NsfLogo.jfif").getImage());

		CsDbMainContentPanel mp = new CsDbMainContentPanel();
		
		f.add(mp.measurePanel, BorderLayout.EAST);
		ImagePanel ip = new ImagePanel();
		ip.loadImages("225");
		ip.normalizeImageSize();
		ip.addImageIcons();
		f.add(ip, BorderLayout.SOUTH);
		f.add(mp.mp, BorderLayout.CENTER);
		f.pack();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
