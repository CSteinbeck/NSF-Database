package csdbgui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CsDbBackgroundPanel extends JPanel {
	private Image backgroundPhoto;
	private Image iconOnTitleBar;
	
	public CsDbBackgroundPanel() {
		Dimension windowSize = new Dimension(900, 600);
		this.setPreferredSize(windowSize);
		this.setMaximumSize(windowSize);
		this.setMinimumSize(windowSize);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(backgroundPhoto, 0, 0, this.getPreferredSize().width, 
				this.getPreferredSize().height, null);
	}
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Cayo Santigo DB Portal");
		
		CsDbBackgroundPanel mp = new CsDbBackgroundPanel();
		mp.iconOnTitleBar = new ImageIcon("Images/NsfLog.png").getImage();
		mp.backgroundPhoto = new ImageIcon("Images/csMonkeys.jpg").getImage();
		window.setIconImage(mp.iconOnTitleBar);
		
		window.add(mp);
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
