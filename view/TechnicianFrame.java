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

		JPanel eastP = new JPanel(new BorderLayout());

		DefaultListModel<Signalement> model = new DefaultListModel<>();
		JList<Signalement> listeSign = new JList<>(model);
		JLabel label = new JLabel("selectionnez un signalement");

		for (Signalement sign : this.al2000.getSignalements()) {
			model.addElement(sign);
		}

		listeSign.addListSelectionListener(new SignalCtrl(label, listeSign));

		eastP.add(label, BorderLayout.CENTER);

		JButton buttonmoins = new JButton(new AbstractAction("effacer un signalement") {
			@Override
			public void actionPerformed(ActionEvent e) {
				label.setText("selectionnez un signalement");
				Signalement aDel = listeSign.getSelectedValue();
				model.removeElement(aDel);
				DelBd.delSign(aDel, al2000);
			}
		});

		eastP.add(buttonmoins, BorderLayout.SOUTH);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listeSign, eastP);

		mainPanel.add(southP, BorderLayout.SOUTH);
		mainPanel.add(splitPane, BorderLayout.CENTER);

		this.add(mainPanel);
		this.pack();
		this.setVisible(true);
	}

	private JPanel buttonAdd(AL2000 al) {
		JPanel Panel = new JPanel(new BorderLayout());
		JButton validate = new JButton(new AbstractAction("Ajouter un dvd") {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame addDvd = new JFrame("remove dvd");
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				addDvd.setSize(800, 600);
				DefaultListModel<DVD> model = new DefaultListModel<>();
				JList<DVD> listedvd = new JList<>(model);
				
				for(DVD dvd : al.getDvds()) {
					if(!dvd.isDispoLoc()) {
						model.addElement(dvd);
					}
				}
				JButton add = new JButton(new AbstractAction("ajouter à l'al2000\"") {
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
		Panel.add(validate);
		return Panel;

	}

	private JPanel buttonDel(AL2000 al) {
		JPanel Panel = new JPanel(new BorderLayout());
		JButton validate = new JButton(new AbstractAction("retirer un dvd") {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame delDvd = new JFrame("remove dvd");
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				delDvd.setSize(800, 600);
				DefaultListModel<DVD> model = new DefaultListModel<>();
				JList<DVD> listedvd = new JList<>(model);
				
				for(DVD dvd : al.getDvds()) {
					if(dvd.isDispoLoc()) {
						model.addElement(dvd);
					}
				}
				JButton del = new JButton(new AbstractAction("retirer de l'al2000") {
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
		Panel.add(validate);
		return Panel;


	}
}
