package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
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
import errors.LocationException;
import errors.SubscriptionException;
import errors.TechnicianException;
import errors.WrongPasswordException;
import model.AL2000;
import model.Abonne;
import model.Client;
import model.DVD;
import model.Location;
import model.Signalement;
import model.Technicien;
import utils.AddBd;
import utils.DelBd;
import utils.UpdateBd;

public class MainFrame extends JFrame {
	protected AL2000 al2000;
	protected String welcomingMessage;
	
	private JPanel mainPanel;
	private JPanel northPanel;
	private JScrollPane centerPanel;
	private JToolBar southToolBar;
	
	public MainFrame(AL2000 al2000) {
		super("AL2000");
		this.al2000 = al2000;
		this.welcomingMessage = "Bienvenue dans l'AL2000 !";
		
		this.setPreferredSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.mainPanel = new JPanel(new BorderLayout());
		this.northPanel = createNorthPanel();
		this.centerPanel = new JScrollPane(createScrollMovie());
		this.southToolBar = createToolBar();
		
		this.mainPanel.add(this.northPanel, BorderLayout.NORTH);
		this.mainPanel.add(this.centerPanel, BorderLayout.CENTER);
		this.mainPanel.add(this.southToolBar, BorderLayout.SOUTH);
		
		this.add(this.mainPanel);
	}

	public void launch() {
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
			
			if (curDVD.isDispoLoc()) {
				MoviePanel curMoviePanel = new MoviePanel(curDVD);
				curMoviePanel.addMouseListener(new MovieListener(this, curDVD));
				
				res.add(curMoviePanel);
			}
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
		res.add(depositButtonAction(this));
		res.add(signUpButtonAction());
		res.add(switchToTechModeButtonAction(this));
		
		return res;
	}
	
	protected AbstractAction depositButtonAction(MainFrame me) {
		return new AbstractAction("Déposer un dvd") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame depositFrame = new JFrame("Déposer un dvd");
				depositFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				depositFrame.setPreferredSize(new Dimension(300, 125));
				
				JPanel mainPanel = new JPanel(new BorderLayout());
				
				JPanel centerPanel = new JPanel();
				
				JPanel idPanel = new JPanel();
				JLabel idLabel = new JLabel("ID DVD : ");
				JTextField idField = new JTextField();
				idField.setPreferredSize(new Dimension(150, 25));
				
				idPanel.add(idLabel);
				idPanel.add(idField);
				
				centerPanel.add(idPanel);
				
				JPanel southPanel = new JPanel();
				
				JButton validate = new JButton(new AbstractAction("Valider") {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						int idDVD = Integer.parseInt(idField.getText());
						
						Location loc = null;
						try {
							loc = al2000.findDVDLoc(idDVD);
						} catch (LocationException e1) {
							e1.printStackTrace();
						}
						
						if(loc != null) {
							UpdateBd.updateDVD(loc.getDvd(), al2000);
							DelBd.delLocation(loc, al2000);
							
							if(loc.getClient().estAbonne()) {
								UpdateBd.updateCredit(((Abonne) loc.getClient()), al2000.montantLoc(loc));
							}
							
							depositFrame.dispose();
							
							JFrame reportFrame = new JFrame("Retour de location");
							reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
							reportFrame.setPreferredSize(new Dimension(500, 200));
							
							JPanel reportMainPanel = new JPanel(new BorderLayout());
							
							JPanel reportCenterPanel = new JPanel();
							
							JPanel motivPanel = new JPanel();
							JLabel motivLabel = new JLabel("Motif : ");
							JTextField motivField = new JTextField();
							motivField.setPreferredSize(new Dimension(400, 25));
							
							motivPanel.add(motivLabel);
							motivPanel.add(motivField);
							
							reportCenterPanel.add(new JLabel("Vous aller être débité de " + al2000.montantLoc(loc) + "€ supplémentaires."));
							reportCenterPanel.add(new JLabel("Voulez-vous signaler un problème avec le DVD loué ?"));
							reportCenterPanel.add(motivPanel);
							
							JPanel reportSouthPanel = new JPanel();
							
							JButton reportValidate = new JButton(createReportValidateButtonAction(reportFrame, loc, motivField));
							
							JButton reportCancel = new JButton(new AbstractAction("Tout s'est bien passé !") {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									reportFrame.dispose();
								}
							});
							
							reportSouthPanel.add(reportCancel);
							reportSouthPanel.add(reportValidate);

							reportMainPanel.add(reportCenterPanel, BorderLayout.CENTER);
							reportMainPanel.add(reportSouthPanel, BorderLayout.SOUTH);
							
							reportFrame.add(reportMainPanel);
							reportFrame.pack();
							reportFrame.setVisible(true);
						}
						
					}
				});
				
				JButton cancel = new JButton(new AbstractAction("Annuler") {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						depositFrame.dispose();
					}
				});
				
				southPanel.add(validate);
				southPanel.add(cancel);

				mainPanel.add(centerPanel, BorderLayout.CENTER);
				mainPanel.add(southPanel, BorderLayout.SOUTH);
				
				depositFrame.add(mainPanel);
				depositFrame.pack();
				depositFrame.setVisible(true);
			}
		};
	}
	
	private AbstractAction signUpButtonAction() {
		return new AbstractAction("S'abonner") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SubscriptionFrame subscribeFrame = new SubscriptionFrame(al2000);
				subscribeFrame.launch();
			}
		};
	}
	
	private AbstractAction createReportValidateButtonAction(JFrame reportFrame, Location loc, JTextField motivField) {
		return new AbstractAction("Signaler") {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				AddBd.addSignalement(new Signalement(loc, motivField.getText(), al2000.montantLoc(loc)), al2000);
				reportFrame.dispose();
			}
		};
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
						
						Abonne abo = null;
						try {
							abo = al2000.connectAbonne(id, pwd);
						} catch (SubscriptionException | WrongPasswordException e1) {
							e1.printStackTrace();
						}
						
						if(abo != null) {
							SubscriberFrame subFrame = new SubscriberFrame(al2000, abo);
							subFrame.launch();
							connectionFrame.dispose();
							me.dispose();
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
						char[] pwd = pwdField.getPassword();
						String strpwd = "";
						for(char c : pwd) {
							strpwd += c;
						}
						
						Technicien tech = null;
						try {
							tech = al2000.modeMaintenance(id, strpwd);
						} catch (TechnicianException | WrongPasswordException e1) {
							e1.printStackTrace();
						}
						
						if(tech != null) {
							TechnicianFrame techFrame = new TechnicianFrame(al2000);
							techFrame.launch();
							connectionFrame.dispose();
							me.dispose();
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
		MovieFrame movieFrame = new MovieFrame(this, dvd, this.al2000);
		movieFrame.launch();
		this.dispose();
	}
	
	public Abonne getConnectedSubscriber() {
		return null;
	}
}
