package csdbgui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Vertical3ViewImagePanel extends JPanel {
	private int totalHeight = 600;
	private int width = 250;
	// images
	private Image frontView;
	private Image rearView;
	private Image sideView;
	
	public Vertical3ViewImagePanel() {
		Dimension d = new Dimension(width, totalHeight);
		this.setMaximumSize(d);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
	}
	
	public void loadImages(String id) {
		String pathStem = "Images/CT Scans/";
		frontView = new ImageIcon(pathStem + "Front/" + id + "Front.png").getImage();
		rearView = new ImageIcon(pathStem + "Rear/" + id + "Rear.png").getImage();
		sideView = new ImageIcon(pathStem + "Side/" + id + "Side.png").getImage();
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(frontView, 0, 0, width, totalHeight/3, null);
		g.drawImage(rearView, 0, totalHeight/3, width, totalHeight/3, null);
		g.drawImage(sideView, 0, 2*totalHeight/3, width, totalHeight/3, null);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame f = new JFrame("Vertical Image Panel Test");
		f.setIconImage(new ImageIcon("Images/NsfLogo.jfif").getImage());

		Vertical3ViewImagePanel imgP = new Vertical3ViewImagePanel();
		imgP.loadImages("225");
		
		f.add(imgP);
		f.pack();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
