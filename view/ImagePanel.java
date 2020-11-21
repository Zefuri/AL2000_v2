package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	private BufferedImage dustJacket;
	
	public ImagePanel(String dustJacketPath) {
		this.setPreferredSize(new Dimension(300, 400));
		try {
			this.dustJacket = ImageIO.read(this.getClass().getResourceAsStream(dustJacketPath));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(dustJacketPath + " fichier ou chemin introuvable.");
		}
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(dustJacket, 0, 0, 300, 400, this);           
    }
}
