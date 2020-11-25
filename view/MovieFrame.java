package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import model.AL2000;
import model.Abonne;
import model.Client;
import model.DVD;
import model.Mode;
import utils.AddBd;
import utils.UpdateBd;

public class MovieFrame extends JFrame {
	private MainFrame parentFrame;
	private DVD dvd;
	private AL2000 al2000;
	
	public MovieFrame(MainFrame parentFrame, DVD dvd, AL2000 al2000) {
		super("AL2000 location du film \"" + dvd.getTitle() + "\"");
		this.parentFrame = parentFrame;
		this.dvd = dvd;
		this.al2000 = al2000;
	}

	public void launch() {
		this.setPreferredSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
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
		labelPanel.add(new JLabel("Réalisé par " + dvd.getProducer()));
		labelPanel.add(new JLabel("Avec " + dvd.getActorsAsString()));
		labelPanel.add(new JLabel(dvd.getGenre().toDisplayString()));
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.PAGE_START;
		
		toScrollPanel.add(labelPanel, gbc);
		
		JTextPane summaryTextPane = new JTextPane();
		summaryTextPane.setText(dvd.getSummary());
		summaryTextPane.setEditable(false);
		summaryTextPane.setPreferredSize(new Dimension(750, 150));
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		
		toScrollPanel.add(summaryTextPane, gbc);
		
		JScrollPane centerPanel = new JScrollPane(toScrollPanel);
		
		JPanel southPanel = new JPanel(new GridLayout(1, 3));
		
		MovieFrame me = this;
		
		JButton returnButton = new JButton(new AbstractAction("Retour") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Abonne abonne = parentFrame.getConnectedSubscriber();
				if(abonne == null) {
					MainFrame mainFrame = new MainFrame(al2000);
					mainFrame.launch();
				} else {
					SubscriberFrame subFrame = new SubscriberFrame(al2000, abonne);
					subFrame.launch();
				}
				me.dispose();
			}
		});
		
		JButton locationButton = new JButton(new AbstractAction("Louer !") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame locationFrame = new JFrame();
				locationFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				locationFrame.setPreferredSize(new Dimension(500, 150));
				
				JPanel mainPanel = new JPanel(new BorderLayout());
				JPanel centerPanel = new JPanel();
				JPanel southPanel = new JPanel();
				
				if(me.getAL2000().isConnected() && me.getAL2000().getMode() == Mode.UTILISATEUR) {
					Abonne abo = ((SubscriberFrame) parentFrame).getConnectedSubscriber();
					
					if(abo.getCredit() > 5) {
						UpdateBd.updateCredit(abo, -5);
						// TODO UpdateBd.updateDVDDispoLoc
						me.rentDVD();
						
						locationFrame.setTitle("Location réussie !");
						centerPanel.add(new JLabel("Location réussie, vous avez été débité de 5€ sur votre solde!"));
					} else {
						locationFrame.setTitle("Echec de la location ...");
						centerPanel.add(new JLabel("Il semblerait que votre solde ne sois pas suffisant ..."));
						centerPanel.add(new JLabel("Mais vous pouvez toujours créditer votre compte !"));
					}
					
					southPanel.add(new JButton(new AbstractAction("Ok !") {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							locationFrame.dispose();
							
							Abonne abonne = parentFrame.getConnectedSubscriber();
							if(abonne == null) {
								MainFrame mainFrame = new MainFrame(al2000);
								mainFrame.launch();
							} else {
								SubscriberFrame subFrame = new SubscriberFrame(al2000, abonne);
								subFrame.launch();
							}
							me.dispose();
						}
					}));
				} else {
					locationFrame.setPreferredSize(new Dimension(300, 150));
					locationFrame.setTitle("Coordonnées");
					
					JPanel mailPanel = new JPanel();
					JLabel mailLabel = new JLabel("Adresse mail : ");
					JTextField mailField = new JTextField();
					mailField.setPreferredSize(new Dimension(150, 25));
					
					mailPanel.add(mailLabel);
					mailPanel.add(mailField);
					
					JPanel cardPanel = new JPanel();
					JLabel cardLabel = new JLabel("Numéro de carte : ");
					JPasswordField cardField = new JPasswordField();
					cardField.setPreferredSize(new Dimension(150, 25));
					
					cardPanel.add(cardLabel);
					cardPanel.add(cardField);
					
					centerPanel.add(mailPanel);
					centerPanel.add(cardPanel);
					
					southPanel.add(new JButton(new AbstractAction("Suivant") {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							AddBd.addClient(me.getAL2000().createNewClient(mailField.getText(), String.valueOf(cardField.getPassword())), me.getAL2000());
							locationFrame.dispose();
							
							// TODO UpdateBd.updateDVD()
							me.rentDVD();
							
							JFrame doneFrame = new JFrame("Location réussie !");
							doneFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
							doneFrame.setPreferredSize(new Dimension(500, 150));
							
							JPanel mainPanelDone = new JPanel(new BorderLayout());
							
							JPanel centerPanelDone = new JPanel();
							centerPanelDone.add(new JLabel("Location réussie, vous allez être débité de 5€ sur votre carte!"));
							
							JPanel southPanelDone = new JPanel();
							southPanelDone.add(new JButton(new AbstractAction("Ok !") {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									doneFrame.dispose();

									Abonne abonne = parentFrame.getConnectedSubscriber();
									if(abonne == null) {
										MainFrame mainFrame = new MainFrame(al2000);
										mainFrame.launch();
									} else {
										SubscriberFrame subFrame = new SubscriberFrame(al2000, abonne);
										subFrame.launch();
									}
									me.dispose();
								}
							}));
							
							mainPanelDone.add(centerPanelDone, BorderLayout.CENTER);
							mainPanelDone.add(southPanelDone, BorderLayout.SOUTH);
							
							doneFrame.add(mainPanelDone);
							doneFrame.pack();
							doneFrame.setVisible(true);
						}
					}));
				}
				
				mainPanel.add(centerPanel, BorderLayout.CENTER);
				mainPanel.add(southPanel, BorderLayout.SOUTH);
				
				locationFrame.add(mainPanel);
				locationFrame.pack();
				locationFrame.setVisible(true);
			}
		});
		
		southPanel.add(returnButton);
		southPanel.add(new JPanel());
		southPanel.add(locationButton);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		
		this.add(mainPanel);
		this.pack();
		this.setVisible(true);
	}
	
	private AL2000 getAL2000() {
		return al2000;
	}
	
	private void rentDVD() {
		this.dvd.changeDispoLoc();
	}
}
