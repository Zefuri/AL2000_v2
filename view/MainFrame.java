package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import controller.MovieListener;
import model.AL2000;
import model.Abonne;
import model.Client;
import model.DVD;

public class MainFrame extends JFrame {
	protected AL2000 al2000;
	protected String welcomingMessage;

	public MainFrame(AL2000 al2000) {
		super("AL2000");
		this.al2000 = al2000;
		this.welcomingMessage = "Bienvenue dans l'AL2000";
	}

	public void launch() {
		this.setPreferredSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JPanel northPanel = createNorthPanel();
		
		JPanel scrollableMoviePane = createScrollMovie();
		JScrollPane centerPanel = new JScrollPane(scrollableMoviePane);
		
		JToolBar southToolBar = createToolBar();
		
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southToolBar, BorderLayout.SOUTH);
		
		this.add(mainPanel);
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * Create the welcoming message at the top of the frame
	 * @return the JPanel with the JLabel corresponding added
	 */
	protected JPanel createNorthPanel() {
		JPanel res = new JPanel();
		
		res.add(new JLabel(this.welcomingMessage));
		
		return res;
	}
	
	/**
	 * Create the scroll movie thing at the center of the frame
	 * @return the JPanel with all the MoviePanel added
	 */
	protected JPanel createScrollMovie() {
		JPanel res = new JPanel();
		
		for(int i = 0; i < this.al2000.getDvds().size(); i++) {
			DVD curDVD = this.al2000.getDvds().get(i);
			
			MoviePanel curMoviePanel = new MoviePanel(curDVD);
			curMoviePanel.addMouseListener(new MovieListener(this, curDVD));
			
			res.add(curMoviePanel);
		}
		
		return res;
	}
	
	/**
	 * Create the ToolBar with all the needed buttons
	 * @return the JToolBar with all needs added
	 */
	protected JToolBar createToolBar() {
		JToolBar res = new JToolBar();
		
		res.add(userConnectionButtonAction(this));
		res.add(switchToTechModeButtonAction(this));
		
		return res;
	}
	
	private AbstractAction userConnectionButtonAction(MainFrame me) {
		
		return new AbstractAction("Connexion") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame connectionFrame = new JFrame("Connexion");
				connectionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				connectionFrame.setPreferredSize(new Dimension(300, 150));
				
				JPanel mainPanel = new JPanel(new BorderLayout());
				
				JPanel centerPanel = new JPanel();
				
				JPanel idPanel = new JPanel();
				JLabel idLabel = new JLabel("ID Abonné : ");
				JTextField idField = new JTextField();
				idField.setPreferredSize(new Dimension(150, 25));
				
				idPanel.add(idLabel);
				idPanel.add(idField);
				
				JPanel pwdPanel = new JPanel();
				JLabel pwdLabel = new JLabel("Mot de passe : ");
				JPasswordField pwdField = new JPasswordField();
				pwdField.setPreferredSize(new Dimension(150, 25));
				
				pwdPanel.add(pwdLabel);
				pwdPanel.add(pwdField);
				
				centerPanel.add(idPanel);
				centerPanel.add(pwdPanel);
				
				JPanel southPanel = new JPanel();
				
				JButton validate = new JButton(new AbstractAction("Valider") {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						int id = Integer.parseInt(idField.getText());
						String pwd = String.valueOf(pwdField.getPassword());
						
						Abonne abo = (Abonne) al2000.getAbonneId(id);
						
						if(abo != null) {
							if(abo.verifierMdp(pwd)) {
								SubscriberFrame subFrame = new SubscriberFrame(al2000, abo);
								subFrame.launch();
								connectionFrame.dispose();
								me.dispose();
							} else {
								System.err.println("Mot de passe incorrect : réessayez !");
							}
						} else {
							System.err.println("Abonné introuvable !");
							connectionFrame.dispose();
						}
					}
				});
				
				JButton cancel = new JButton(new AbstractAction("Annuler") {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						connectionFrame.dispose();
					}
				});
				
				southPanel.add(validate);
				southPanel.add(cancel);

				mainPanel.add(centerPanel, BorderLayout.CENTER);
				mainPanel.add(southPanel, BorderLayout.SOUTH);
				
				connectionFrame.add(mainPanel);
				connectionFrame.pack();
				connectionFrame.setVisible(true);
			}
		};
	}

	private AbstractAction switchToTechModeButtonAction(MainFrame me) {

		return new AbstractAction("Maintenance") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame connectionFrame = new JFrame("Connexion Technicien");
				connectionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				connectionFrame.setPreferredSize(new Dimension(300, 150));
				
				JPanel mainPanel = new JPanel(new BorderLayout());
				
				JPanel centerPanel = new JPanel();
				
				JPanel idPanel = new JPanel();
				JLabel idLabel = new JLabel("ID Technicien : ");
				JTextField idField = new JTextField();
				idField.setPreferredSize(new Dimension(150, 25));
				
				idPanel.add(idLabel);
				idPanel.add(idField);
				
				JPanel pwdPanel = new JPanel();
				JLabel pwdLabel = new JLabel("Mot de passe : ");
				JPasswordField pwdField = new JPasswordField();
				pwdField.setPreferredSize(new Dimension(150, 25));
				
				pwdPanel.add(pwdLabel);
				pwdPanel.add(pwdField);
				
				centerPanel.add(idPanel);
				centerPanel.add(pwdPanel);
				
				JPanel southPanel = new JPanel();
				
				JButton validate = new JButton(new AbstractAction("Valider") {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						int id = Integer.parseInt(idField.getText());
						String pwd = pwdField.getSelectedText();
						
						if(al2000.getTechniciens().containsKey(id)) {
							if(al2000.getTechniciens().get(id).connexion(pwd)) {
								TechnicianFrame techFrame = new TechnicianFrame(al2000);
								techFrame.launch();
								connectionFrame.dispose();
								me.dispose();
							} else {
								System.err.println("Mot de passe incorrect : réessayez !");
							}
						} else {
							System.err.println("Technicien inexistant.");
							connectionFrame.dispose();
						}
					}
				});
				
				JButton cancel = new JButton(new AbstractAction("Annuler") {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						connectionFrame.dispose();
					}
				});
				
				southPanel.add(validate);
				southPanel.add(cancel);

				mainPanel.add(centerPanel, BorderLayout.CENTER);
				mainPanel.add(southPanel, BorderLayout.SOUTH);
				
				connectionFrame.add(mainPanel);
				connectionFrame.pack();
				connectionFrame.setVisible(true);
			}
		};
	}

	public void switchToMovieFrame(DVD dvd) {
		MovieFrame movieFrame = new MovieFrame(this, dvd);
		movieFrame.launch();
		this.setVisible(false);
	}
}
