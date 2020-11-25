package controller;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Location;
import view.ImagePanel;

public class HistoCtrl implements ListSelectionListener{
	
	private ImagePanel img;
	private JList<Location> list;

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if(list.getSelectedValue() != null) {
			img.setImagePanel(list.getSelectedValue().getDvd().getUrlImage());
			img.repaint();
		}
		
	}

	public HistoCtrl(ImagePanel img, JList<Location> list) {
		this.img = img;
		this.list = list;
	}

}
