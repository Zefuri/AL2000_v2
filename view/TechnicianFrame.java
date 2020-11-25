package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import controller.SignalCtrl;
import errors.SubscriptionException;
import errors.WrongPasswordException;
import model.AL2000;
import model.Abonne;
import model.DVD;
import model.Location;
import model.Signalement;
import utils.DelBd;
import utils.UpdateBd;

/**
 * 
 * @author quentin frame used to create the frame which will help the technician
 *         do his job
 */
public class TechnicianFrame extends MainFrame {

	public TechnicianFrame(AL2000 al2000) {
		super(al2000);
		this.welcomingMessage = "Technicien";
	}

	@Override
	public void launch() {
		this.setPreferredSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel southP = new JPanel(new BorderLayout());
		southP.add(buttonAdd(al2000), BorderLayout.WEST);
		southP.add(buttonDel(al2000), BorderLayout.EAST);
		southP.add(new JButton(disconnectButtonAction(al2000, this)), BorderLayout.SOUTH);

		JPanel eastP = new JPanel(new BorderLayout());

		DefaultListModel<Signalement> model = new DefaultListModel<>();
		JList<Signalement> listeSign = new JList<>(model);
		JLabel label = new JLabel("Selectionnez un signalement");

		for (Signalement sign : this.al2000.getSignalements()) {
			model.addElement(sign);
		}

		listeSign.addListSelectionListener(new SignalCtrl(label, listeSign));

		eastP.add(label, BorderLayout.CENTER);

		JButton buttonmoins = new JButton(new AbstractAction("Effacer un signalement") {
			@Override
			public void actionPerformed(ActionEvent e) {
				label.setText("Selectionnez un signalement");
				Signalement aDel = listeSign.getSelectedValue();
				model.removeElement(aDel);
				DelBd.delSign(aDel, al2000);
			}
		});
		
		JButton buttonremb = new JButton(new AbstractAction("rembourser un signalement") {
			@Override
			public void actionPerformed(ActionEvent e) {
				label.setText("selectionnez un signalement");
				Signalement aDel = listeSign.getSelectedValue();
				Location loc = aDel.getLocation();
				if(loc.getClient().estAbonne()) {
					UpdateBd.updateCredit(null, aDel.getMontant());
				}
				JFrame remb = new JFrame("notif");
				remb.setSize(200, 100);
				remb.add(new JLabel("Le client a été remboursé, ou crédité si il était abonné"));
				remb.pack();
				remb.setVisible(true);
				model.removeElement(aDel);
				DelBd.delSign(aDel, al2000);
			}
		});
		
		JSplitPane splitb = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonmoins, buttonremb);
		eastP.add(splitb, BorderLayout.SOUTH);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listeSign, eastP);

		mainPanel.add(southP, BorderLayout.SOUTH);
		mainPanel.add(splitPane, BorderLayout.CENTER);

		this.add(mainPanel);
		this.pack();
		this.setVisible(true);
	}

	private JPanel buttonAdd(AL2000 al) {
		JPanel panel = new JPanel(new BorderLayout());
		JButton validate = new JButton(new AbstractAction("Ajouter un dvd") {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame addDvd = new JFrame("Ajouter un dvd");
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				addDvd.setSize(800, 600);
				DefaultListModel<DVD> model = new DefaultListModel<>();
				JList<DVD> listedvd = new JList<>(model);
				
				for(DVD dvd : al.getDvds()) {
					if(!dvd.isDispoLoc()) {
						model.addElement(dvd);
					}
				}
				JButton add = new JButton(new AbstractAction("Ajouter à l'al2000\"") {
					@Override
					public void actionPerformed(ActionEvent e) {
						DVD toChange = listedvd.getSelectedValue();
						if(toChange != null) {
							UpdateBd.updateDVD(toChange, al);
							model.removeElement(toChange);
						}
					}
				});
				JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listedvd, add);
				addDvd.add(splitPane);
				addDvd.pack();
				addDvd.setVisible(true);
			}
		});
		panel.add(validate);
		return panel;

	}

	private JPanel buttonDel(AL2000 al) {
		JPanel panel = new JPanel(new BorderLayout());
		JButton validate = new JButton(new AbstractAction("Retirer un dvd") {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame delDvd = new JFrame("Retirer un dvd");
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				delDvd.setSize(800, 600);
				DefaultListModel<DVD> model = new DefaultListModel<>();
				JList<DVD> listedvd = new JList<>(model);
				
				for(DVD dvd : al.getDvds()) {
					if(dvd.isDispoLoc()) {
						model.addElement(dvd);
					}
				}
				JButton del = new JButton(new AbstractAction("Retirer de l'al2000") {
					@Override
					public void actionPerformed(ActionEvent e) {
						DVD toChange = listedvd.getSelectedValue();
						if(toChange != null) {
							UpdateBd.updateDVD(toChange, al);
							model.removeElement(toChange);
						}
					}
				});
				JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listedvd, del);
				delDvd.add(splitPane);
				delDvd.pack();
				delDvd.setVisible(true);
			}
		});
		panel.add(validate);
		return panel;


	}
	
	private AbstractAction disconnectButtonAction(AL2000 al2000, TechnicianFrame me) {
		return new AbstractAction("Deconnexion") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame mainFrame = new MainFrame(al2000);
				mainFrame.launch();
				me.dispose();
			}
		};
	}
}
