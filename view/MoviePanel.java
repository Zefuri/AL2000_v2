package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.DVD;

public class MoviePanel extends JPanel {
	private ImagePanel dustJacket;
	private DVD dvd;
	
	public MoviePanel(DVD dvd) {
		super();
		this.dvd = dvd;
		this.dustJacket = new ImagePanel(new Dimension(300, 400), dvd.getUrlImage());
		
		this.setLayout(new BorderLayout());
		
		this.add(dustJacket, BorderLayout.CENTER);
		this.add(new JLabel(this.dvd.getTitle()), BorderLayout.SOUTH);
	}
}
