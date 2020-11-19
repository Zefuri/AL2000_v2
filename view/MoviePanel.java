package view;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.DVD;

public class MoviePanel extends JPanel {
	private ImagePanel dustJacket;
	private String movieTitle;
	
	public MoviePanel(String dustJacketPath, String movieTitle) {
		super();
		this.movieTitle = movieTitle;
		this.dustJacket = new ImagePanel("/resources/" + dustJacketPath);
		
		this.setLayout(new BorderLayout());
		
		this.add(dustJacket, BorderLayout.CENTER);
		this.add(new JLabel(this.movieTitle), BorderLayout.SOUTH);
	}
}
