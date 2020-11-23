package view;

import java.awt.Dimension;

import javax.swing.JFrame;

import model.DVD;

public class MovieFrame extends JFrame {
	private MainFrame parentFrame;
	private DVD dvd;
	
	public MovieFrame(MainFrame parentFrame, DVD dvd) {
		super("AL2000 location du film \"" + dvd.getTitle() + "\"");
		this.parentFrame = parentFrame;
		this.dvd = dvd;
	}
	
	public void launch() {
		this.setPreferredSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// TODO implements the movie display, the location button and the return button
		
		this.pack();
		this.setVisible(true);
	}
}
