package view;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.DVD;

public class MoviePanel extends JPanel {
	private ImagePanel dustJacket;
	private DVD dvd;
	
	public MoviePanel(String dustJacketPath, DVD dvd) {
		super();
		this.dvd = dvd;
		this.dustJacket = new ImagePanel("/resources/" + dustJacketPath);
		
		this.setLayout(new BorderLayout());
		
		this.add(dustJacket, BorderLayout.CENTER);
		this.add(new JLabel(this.dvd.getTitle()), BorderLayout.SOUTH);
	}
}
