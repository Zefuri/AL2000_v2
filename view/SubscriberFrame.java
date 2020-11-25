package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import javax.swing.JButton;

import model.AL2000;
import model.Abonne;
import utils.UpdateBd;

public class SubscriberFrame extends MainFrame {
	private Abonne abonne;
	
	public SubscriberFrame(AL2000 al2000, Abonne abonne) {
		super(al2000);
		this.abonne = abonne;
		// TODO perhaps personalize the welcoming message
	}

	/**
	 * Create the ToolBar with all the needed buttons
	 * @return the JToolBar with all needs added
	 */
	@Override
	protected JToolBar createToolBar() {
		JToolBar res = new JToolBar();
		
		res.add(profilButtonAction(this));
		res.add(depositButtonAction(this));
		res.add(disconnectButtonAction(this));
		
		return res;
	}
	
	private AbstractAction profilButtonAction(SubscriberFrame me) {
		return new AbstractAction("Profil") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ProfilFrame profilFrame = new ProfilFrame(al2000, abonne, me);
				profilFrame.launch();
				me.setVisible(false);
			}
		};
	}

	private AbstractAction disconnectButtonAction(SubscriberFrame me) {
		return new AbstractAction("Deconnexion") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame mainFrame = new MainFrame(al2000);
				mainFrame.launch();
				me.dispose();
			}
		};
	}
	
	@Override
	public Abonne getConnectedSubscriber() {
		return this.abonne;
	}
}
