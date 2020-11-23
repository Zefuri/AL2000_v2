package application;

import java.util.ArrayList;

import errors.BdIncoherenteException;
import model.AL2000;
import model.DVD;
import model.Genre;
import utils.InitBd;
import view.MainFrame;

public class Application {

	private AL2000 al2000;

	

	public static void main(String[] args) {
		//GestionBd.initBD();
		AL2000 al2000 = new AL2000();
		try {
			InitBd.initAl2000(al2000);
		} catch (BdIncoherenteException e) {
			e.printStackTrace();
		}
		
		System.out.println(al2000);

		/*ArrayList<DVD> dvds = new ArrayList<DVD>();
		dvds.add(new DVD("The Godfather", Genre.DRAME, 2000, "", new ArrayList<String>(), "", "godfather.jpg"));
		dvds.add(new DVD("Joker", Genre.ACTION, 2000, "", new ArrayList<String>(), "", "joker.jpg"));
		dvds.add(new DVD("Pulp Fiction", Genre.ACTION, 2000, "", new ArrayList<String>(), "", "pulpfiction.jpg"));
		dvds.add(new DVD("The Dictator", Genre.COMEDIE, 2000, "", new ArrayList<String>(), "", "thedictator.jpg"));
		dvds.add(new DVD("Titanic", Genre.DRAME, 2000, "", new ArrayList<String>(), "", "titanic.jpg"));

		al2000.setDvds(dvds);*/

		MainFrame mainFrame = new MainFrame(al2000);

		mainFrame.launch();
	}
}
