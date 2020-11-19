package application;

import java.util.ArrayList;

import model.AL2000;
import model.DVD;
import model.Genre;
import view.MainFrame;

public class Application {
	public static void main(String[] args) {
		AL2000 al2000 = new AL2000();
		
		ArrayList<DVD> dvds = new ArrayList<DVD>();
		dvds.add(new DVD("The Godfather", Genre.DRAME, "", "", new ArrayList<String>(), "", "godfather.jpg"));
		dvds.add(new DVD("Joker", Genre.ACTION, "", "", new ArrayList<String>(), "", "joker.jpg"));
		dvds.add(new DVD("Pulp Fiction", Genre.ACTION, "", "", new ArrayList<String>(), "", "pulpfiction.jpg"));
		dvds.add(new DVD("The Dictator", Genre.COMEDIE, "", "", new ArrayList<String>(), "", "thedictator.jpg"));
		dvds.add(new DVD("Titanic", Genre.DRAME, "", "", new ArrayList<String>(), "", "titanic.jpg"));
		
		al2000.setDvds(dvds);
		
		System.out.println(al2000.getDvds());
		
		MainFrame mainFrame = new MainFrame(al2000);
		
		mainFrame.launch();
	}
}
