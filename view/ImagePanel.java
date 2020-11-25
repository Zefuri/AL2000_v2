package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	private BufferedImage dustJacket;
	
	public ImagePanel(Dimension dimension, String dustJacketPath) {
		this.setPreferredSize(dimension);
		setImagePanel(dustJacketPath);

	}
	
	public ImagePanel(String dustJacketPath) {
		setImagePanel(dustJacketPath);

	}
	
	public void setImagePanel(String dustJacketPath) {
		if(dustJacketPath != null) {
			try {
				this.dustJacket = ImageIO.read(this.getClass().getResourceAsStream("/resources/" + dustJacketPath));
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(dustJacketPath + " fichier ou chemin introuvable.");
			}
		}else {
			dustJacket = null;
		}
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(dustJacket != null) {
            g.drawImage(dustJacket, 0, 0, this.getSize().width, this.getSize().height, this);           
        }
    }
}
