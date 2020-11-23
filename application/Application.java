package application;

import java.util.ArrayList;

import errors.BdIncoherenteException;
import model.AL2000;
import model.DVD;
import model.Genre;
import utils.InitBd;
import view.MainFrame;

public class Application {
	private static final String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque a dui egestas, facilisis nunc a, tristique nulla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Phasellus in justo sed mi dignissim viverra in in nisl. Nam est eros, dapibus at metus sit amet, laoreet convallis turpis. Proin lectus ante, dignissim non euismod eget, pharetra et nisl. Vestibulum hendrerit libero lectus, eget finibus erat rhoncus quis. Vivamus vestibulum ultrices turpis. Nunc bibendum vehicula rutrum.";
	private static final String producer = "Lambda Productor";
	
	private AL2000 al2000;

	public static void main(String[] args) {
		//GestionBd.initBD();
		AL2000 al2000 = new AL2000();
		/*try {
			InitBd.initAl2000(al2000);
		} catch (BdIncoherenteException e) {
			e.printStackTrace();
		}
		
		System.out.println(al2000);*/

		ArrayList<DVD> dvds = new ArrayList<DVD>();
		dvds.add(new DVD(0, "The Godfather", Genre.DRAME, 2000, producer, new ArrayList<String>(), lorem, "godfather.jpg"));
		dvds.add(new DVD(1, "Joker", Genre.ACTION, 2000, producer, new ArrayList<String>(), lorem, "joker.jpg"));
		dvds.add(new DVD(2, "Pulp Fiction", Genre.ACTION, 2000, producer, new ArrayList<String>(), lorem, "pulpfiction.jpg"));
		dvds.add(new DVD(3, "The Dictator", Genre.COMEDIE, 2000, producer, new ArrayList<String>(), lorem, "thedictator.jpg"));
		dvds.add(new DVD(4, "Titanic", Genre.DRAME, 2000, producer, new ArrayList<String>(), lorem, "titanic.jpg"));

		al2000.setDvds(dvds);

		MainFrame mainFrame = new MainFrame(al2000);

		mainFrame.launch();
	}
}
