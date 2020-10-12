package csdbgui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class ImagePanel extends JPanel {
	// set panel size
	private int totalWidth = 900;
	private int height = 230;
	// images
	private Image frontView;
	private Image rearView;
	private Image sideView;
	private JLabel jlblFrontView;
	private JLabel jlblRearView;
	private JLabel jlblSideView;
	
	public ImagePanel() {
		Dimension d = new Dimension(totalWidth, height);
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

	public void normalizeImageSize() {
		frontView = frontView.getScaledInstance(totalWidth/3-20, height-25, Image.SCALE_SMOOTH);
		rearView = rearView.getScaledInstance(totalWidth/3-20, height-25, Image.SCALE_SMOOTH);
		sideView = sideView.getScaledInstance(totalWidth/3-20, height-25, Image.SCALE_SMOOTH);
		//frontView = fitSize(frontView, totalWidth/3, height);
		//rearView = fitSize(rearView, totalWidth/3, height);
		//sideView = fitSize(sideView, totalWidth/3, height);
	}
	
	private Image fitSize(Image image, int w, int h) {
		int oldW = image.getWidth(null);
		int oldH = image.getHeight(null);
		if (oldW < w && oldH < h)
			return image;
		
		BufferedImage bim = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics g = bim.createGraphics();
	    g.drawImage(image, 0, 0, null);
		System.out.println(oldW + " -o- " + oldH);
		JOptionPane.showConfirmDialog(null, "Original image", "Test", JOptionPane.OK_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(image));

		double wScale = 1. * w / oldW;
		double hScale = 1. * h / oldH;
		double scale;
		System.out.println(wScale + ", " + hScale);
		
		if (wScale < hScale)
			scale = wScale;
		else
			scale = hScale;
		System.out.println(scale);
		
        int scaledWidth = (int) (scale * oldW);
        int scaledHeight = (int) (scale * oldH);
		System.out.println(scaledWidth + " -s- " + scaledHeight);
        Image scaledImage = bim.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        // new ImageIcon(image); // load image
        // scaledWidth = scaledImage.getWidth(null);
        // scaledHeight = scaledImage.getHeight(null);
		JOptionPane.showConfirmDialog(null, "Scaled image", "Test", JOptionPane.OK_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(scaledImage));
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledBI.createGraphics();
             g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(scaledImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        g.dispose();
		
		JOptionPane.showConfirmDialog(null, "Returned image", "Test", JOptionPane.OK_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(scaledBI));
		return scaledBI;
	}

	public void addImageIcons() {
		Border blackline = BorderFactory.createLineBorder(Color.BLACK);		
		TitledBorder titleFront = BorderFactory.createTitledBorder(
                blackline, "Front View");
		titleFront.setTitleJustification(TitledBorder.LEFT);
		jlblFrontView = new JLabel(new ImageIcon(frontView));
		jlblFrontView.setBorder(titleFront);
		this.add(jlblFrontView);
		
		TitledBorder titleRear = BorderFactory.createTitledBorder(
                blackline, "Rear View");
		titleRear.setTitleJustification(TitledBorder.LEFT);
		jlblRearView = new JLabel(new ImageIcon(rearView));
		jlblRearView.setBorder(titleRear);
		this.add(jlblRearView);
		
		TitledBorder titleSide = BorderFactory.createTitledBorder(
                blackline, "Side View");
		titleSide.setTitleJustification(TitledBorder.LEFT);
		jlblSideView = new JLabel(new ImageIcon(sideView));
		jlblSideView.setBorder(titleSide);
		this.add(jlblSideView);
		
	}
	/*
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(frontView, 0, 0, totalWidth/3, height, null);
		g.drawImage(rearView, totalWidth/3, 0, totalWidth/3, height, null);
		g.drawImage(sideView, 2*totalWidth/3, 0, totalWidth/3, height, null);
	}
	*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame f = new JFrame("Image Panel Test");

		ImagePanel imgP = new ImagePanel();
		imgP.loadImages("Mm1759");
		imgP.normalizeImageSize();
		imgP.addImageIcons();
		
		f.add(imgP);
		f.pack();
		//f.setSize(imgP.totalWidth, imgP.height);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

}
