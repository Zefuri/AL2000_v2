package view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JToolBar;

import model.AL2000;
import model.Abonne;

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
		
		res.add(historyButtonAction());
		res.add(locationsButtonAction());
		res.add(disconnectButtonAction(this.al2000, this));
		
		return res;
	}
	
	private AbstractAction historyButtonAction() {
		return new AbstractAction("Historique") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO implements the HistoryFrame
			}
		};
	}
	
	private AbstractAction locationsButtonAction() {
		return new AbstractAction("Locations") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO implements LocationsFrame
			}
		};
	}
	
	private AbstractAction disconnectButtonAction(AL2000 al2000, SubscriberFrame me) {
		return new AbstractAction("Déconnexion") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame mainFrame = new MainFrame(al2000);
				me.dispose();
			}
		};
	}
	
	@Override
	public Abonne getConnectedSubscriber() {
		return this.abonne;
	}
}
