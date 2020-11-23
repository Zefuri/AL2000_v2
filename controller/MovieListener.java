package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.DVD;
import view.MainFrame;

public class MovieListener extends MouseAdapter {
	private MainFrame mainFrame;
	private DVD dvd;
	
	public MovieListener(MainFrame mainFrame, DVD dvd) {
		super();
		this.mainFrame = mainFrame;
		this.dvd = dvd;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.mainFrame.switchToMovieFrame(dvd);
		super.mouseClicked(e);
	}
}
