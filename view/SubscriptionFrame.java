package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.AL2000;
import model.Abonne;
import utils.AddBd;

public class SubscriptionFrame extends JFrame {
	private AL2000 al2000;
	
	private JPanel mainPanel;
	private JPanel centerPanel;
	private JPanel soutPanel;
	
	private JTextField mailField;
	private JPasswordField pwdField;
	private JPasswordField cardField;
	private JTextField creditField;
	
	public SubscriptionFrame(AL2000 al2000) {
		super("AL2000 Subscription");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(400, 250));
		
		this.al2000 = al2000;
		
		this.mainPanel = new JPanel(new BorderLayout());
		this.centerPanel = createCenterPanel();
		this.soutPanel = createSouthPanel();
		
		this.mainPanel.add(this.centerPanel, BorderLayout.CENTER);
		this.mainPanel.add(this.soutPanel, BorderLayout.SOUTH);
		
		this.add(this.mainPanel);
	}
	
	public void launch() {
		this.pack();
		this.setVisible(true);
	}
	
	private JPanel createCenterPanel() {
		JPanel res = new JPanel();
		
		JPanel mailPanel = new JPanel();
		JLabel mailLabel = new JLabel("Adresse mail* : ");
		this.mailField = new JTextField();
		this.mailField.setPreferredSize(new Dimension(200, 25));
		
		mailPanel.add(mailLabel);
		mailPanel.add(this.mailField);
		
		JPanel pwdPanel = new JPanel();
		JLabel pwdLabel = new JLabel("Mot de passe* : ");
		this.pwdField = new JPasswordField();
		this.pwdField.setPreferredSize(new Dimension(200, 25));
		
		pwdPanel.add(pwdLabel);
		pwdPanel.add(this.pwdField);
		
		JPanel cardPanel = new JPanel();
		JLabel cardLabel = new JLabel("Numéro de CB* : ");
		this.cardField = new JPasswordField();
		this.cardField.setPreferredSize(new Dimension(200, 25));
		
		cardPanel.add(cardLabel);
		cardPanel.add(this.cardField);
		
		JPanel creditPanel = new JPanel();
		JLabel creditLabel = new JLabel("Credit initial : ");
		this.creditField = new JTextField();
		this.creditField.setPreferredSize(new Dimension(200, 25));
		
		creditPanel.add(creditLabel);
		creditPanel.add(this.creditField);
		
		res.add(mailPanel);
		res.add(pwdPanel);
		res.add(cardPanel);
		res.add(creditPanel);
		
		return res;
	}
	
	private JPanel createSouthPanel() {
		JPanel res = new JPanel();
		
		res.add(new JButton(backButtonAction()));
		res.add(new JButton(createSubscriberButtonAction()));
		
		return res;
	}
	
	private AbstractAction createSubscriberButtonAction() {
		return new AbstractAction("S'abonner !") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String mail = mailField.getText();
				String pwd = String.copyValueOf(pwdField.getPassword());
				String card = String.copyValueOf(cardField.getPassword());
				int credit = Integer.parseInt(creditField.getText());
				
				Abonne newAbo;
				if(mail != null && pwd != null && card != null) {
					newAbo = al2000.createNewAbonne(mail, pwd, card, credit);
					AddBd.addAbo(newAbo, al2000);
					
					dispose();
					
					JFrame validationFrame = new JFrame("Abonnement réussi !");
					validationFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					validationFrame.setPreferredSize(new Dimension(400, 200));
					
					JPanel validationMainPanel = new JPanel();
					validationMainPanel.add(new JLabel("Vous êtes l'abonné n°" + newAbo.getIdc()));
					validationMainPanel.add(new JLabel("Vous avez été crédité de " + newAbo.getCredit() + "€"));
					
					validationFrame.add(validationMainPanel);
					validationFrame.pack();
					validationFrame.setVisible(true);
				} else {
					JFrame exceptionFrame = new JFrame("Erreur lors de l'abonnement ...");
					exceptionFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					exceptionFrame.setPreferredSize(new Dimension(400, 100));
					
					JPanel exceptionMainPanel = new JPanel();
					exceptionMainPanel.add(new JLabel("Veuillez compléter tous les champs obligatoires !"));
					
					exceptionFrame.add(exceptionMainPanel);
					exceptionFrame.pack();
					exceptionFrame.setVisible(true);
				}
			}
		};
	}
	
	private AbstractAction backButtonAction() {
		return new AbstractAction("Retour") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
	}
}
