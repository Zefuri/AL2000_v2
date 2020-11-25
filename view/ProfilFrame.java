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

public class ProfilFrame extends MainFrame {
	private SubscriberFrame parentFrame;
	private Abonne abonne;

	public ProfilFrame(AL2000 al2000, Abonne abonne, SubscriberFrame parentFrame) {
		super(al2000);
		this.abonne = abonne;
		this.parentFrame = parentFrame;
		// TODO perhaps personalize the welcoming message
	}

	public void launch() {
		this.setTitle("Espace Client");
		this.setPreferredSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel northPanel = createNorthPanel();

		JPanel labelPanelCenter = new JPanel(new GridLayout(3, 1));
		// Email
		labelPanelCenter.add(new JLabel("Email :" + abonne.getMail()));
		// Credit
		JLabel credit = new JLabel("Credit en Euros :" + abonne.getCredit());
		labelPanelCenter.add(credit);
		// Recharger
		JTextField textField = new JTextField("15");
		// Ne fonctionne PAS!
		textField.setPreferredSize(new Dimension(400, 100));

		JSplitPane sFooter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		sFooter.add(textField);
		labelPanelCenter.add(sFooter);

		JButton boutonRecharge = new JButton("Recharger");
		boutonRecharge.addActionListener(actionEvent -> {
			abonne.setCredit(abonne.getCredit() + Integer.parseInt(textField.getText()));
			credit.setText("Credit en Euros :" + abonne.getCredit());
			UpdateBd.updateCredit(abonne, abonne.getCredit());
			this.pack();
			System.out.println(abonne.getCredit());
		});
		sFooter.add(boutonRecharge);

		JToolBar southToolBar = createToolBar();

		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(labelPanelCenter, BorderLayout.CENTER);
		mainPanel.add(southToolBar, BorderLayout.SOUTH);

		this.add(mainPanel);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Create the ToolBar with all the needed buttons
	 * 
	 * @return the JToolBar with all needs added
	 */
	@Override
	protected JToolBar createToolBar() {
		JToolBar res = new JToolBar();

		res.add(homeButtonAction(this));
		res.add(disconnectButtonAction(this));

		return res;
	}

	private AbstractAction homeButtonAction(ProfilFrame me) {
		return new AbstractAction("Accueil") {

			@Override
			public void actionPerformed(ActionEvent e) {
				parentFrame.repaint();
				me.dispose();
			}
		};
	}

	private AbstractAction disconnectButtonAction(ProfilFrame me) {
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
