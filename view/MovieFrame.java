package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
		
		JPanel toScrollPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		ImagePanel imagePanel = new ImagePanel(new Dimension(300, 400), dvd.getUrlImage());
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		
		toScrollPanel.add(imagePanel, gbc);
		
		JPanel labelPanel = new JPanel(new GridLayout(5, 1));
		labelPanel.add(new JLabel(dvd.getTitle()));
		labelPanel.add(new JLabel("Sorti en " + dvd.getReleaseDate()));
		labelPanel.add(new JLabel("R�alis� par " + dvd.getProducer()));
		labelPanel.add(new JLabel("Avec " + dvd.getActorsAsString()));
		labelPanel.add(new JLabel(dvd.getGenre().toDisplayString()));
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.PAGE_START;
		
		toScrollPanel.add(labelPanel, gbc);
		
		JLabel summaryLabel = new JLabel(dvd.getSummary());
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		
		toScrollPanel.add(summaryLabel, gbc);
		
		JScrollPane mainPanel = new JScrollPane(toScrollPanel);
		
		this.add(mainPanel);
		this.pack();
		this.setVisible(true);
	}
}
