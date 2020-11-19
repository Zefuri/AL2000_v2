package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.AL2000;
import model.DVD;

public class MainFrame extends JFrame {
	private AL2000 al2000;

	public MainFrame(AL2000 al2000) {
		super("AL2000");
		this.al2000 = al2000;
	}

	public void launch() {
		this.setPreferredSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JPanel northPanel = new JPanel();
		northPanel.add(new JLabel("Bienvenue dans l'AL2000"));
		
		JPanel scrollableMoviePane = new JPanel();
		
		for(int i = 0; i < this.al2000.getDvds().size(); i++) {
			DVD curDVD = this.al2000.getDvds().get(i);
			MoviePanel curMoviePanel = new MoviePanel(curDVD.getUrlImage(), curDVD.getTitle());
			scrollableMoviePane.add(curMoviePanel);
		}
		JScrollPane centerPanel = new JScrollPane(scrollableMoviePane);
		
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		this.add(mainPanel);
		this.pack();
		this.setVisible(true);
	}

}
