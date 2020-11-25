package controller;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Signalement;

public class SignalCtrl implements ListSelectionListener{
	
	private JLabel label;
	private JList<Signalement> list; 
	
	


	public SignalCtrl(JLabel label, JList<Signalement> list) {
		super();
		this.label = label;
		this.list = list;
	}




	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if(list.getSelectedValue() != null) {
			label.setText(list.getSelectedValue().getMotif());
			label.repaint();
		}
		
	}

}
