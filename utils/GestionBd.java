package utils;

import java.io.File;
import java.io.FileNotFoundException;

import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import model.AL2000;
import model.DVD;
import model.Genre;
import model.Location;
import model.Technicien;

public final class GestionBd {
	
	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:im2ag";

	static final String USER = "demarquq";
	static final String PASSWD = "Cork1440safety";
	static Connection conn;
	
	public static String[] createqueries(String path) {
		DVD dvd = new DVD(null, null, 0, null, null, null, null);
		URL url = dvd.getClass().getResource(path);
		File data = new File(url.getPath());
		String initbd = "";
		String queries[] = null;
		try {
			Scanner reader = new Scanner(data);
			while(reader.hasNextLine()) {
				initbd += reader.nextLine();
			}

			queries = initbd.split(";");
			reader.close();
		}catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }

		return queries;
	}
	
	public static void initBD() {
		String[] queriestable = createqueries("/bd/data.sql");
		String[] queriesdvd = createqueries("/bd/initdvd.sql");


		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
			for(String query : queriestable) {
				System.out.println(query + "à faire");
				PreparedStatement createbd = conn.prepareStatement(query);
				createbd.executeUpdate();
				System.out.println(query + "done");
			}
			
			for(String query : queriesdvd) {
				System.out.println(query + "à faire");
				PreparedStatement createbd = conn.prepareStatement(query);
				createbd.executeUpdate();
				System.out.println(query + "done");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			}
	}
	
	public static void initclients(AL2000 al) {
		try {
			
			ResultSet resultats = null;

			System.out.print("Loading Oracle driver... ");
			System.out.println("loaded");
			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
			System.out.println("connected");

			
			PreparedStatement affichegard = conn
					.prepareStatement("SELECT * FROM CLIENTS");

			resultats = affichegard.executeQuery();
			while (resultats.next()) {
				ResultSet reqhistorique = null;
				ArrayList<Location> histo = null;
				
			}

			conn.close();

			System.out.println("bye.");

			// traitement d'exception
		} catch (SQLException e) {
			System.err.println("failed");
			System.out.println("Affichage de la pile d'erreur");
			e.printStackTrace(System.err);
			System.out.println("Affichage du message d'erreur");
			System.out.println(e.getMessage());
			System.out.println("Affichage du code d'erreur");
			System.out.println(e.getErrorCode());

		}
	}
	
	public static void initTech(AL2000 al) {
try {
			
			ResultSet resultats = null;

			System.out.print("Loading Oracle driver... ");
			System.out.println("loaded");
			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
			System.out.println("connected");

			
			PreparedStatement affichegard = conn
					.prepareStatement("SELECT * FROM Technicien");

			resultats = affichegard.executeQuery();
			while (resultats.next()) {
				al.getTechniciens().put(resultats.getInt("idt"), new Technicien(al, resultats.getString("mdp")));
				
			}

			conn.close();

			System.out.println("bye.");

			// traitement d'exception
		} catch (SQLException e) {
			System.err.println("failed");
			System.out.println("Affichage de la pile d'erreur");
			e.printStackTrace(System.err);
			System.out.println("Affichage du message d'erreur");
			System.out.println(e.getMessage());
			System.out.println("Affichage du code d'erreur");
			System.out.println(e.getErrorCode());

		}
	}

	public static void initdvd(AL2000 al) {
		try {
			
			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			
			PreparedStatement affichegard = conn
					.prepareStatement("SELECT * FROM DVDS");

			resultats = affichegard.executeQuery();
			while (resultats.next()) {
				ArrayList<String> actors = new ArrayList<String>();
				String[] act = resultats.getString("actors").replace("[", "").replace("]", "").split(",");
				for(String str : act) {
					actors.add(str);
				}
				al.getDvds().add(new DVD(resultats.getString("title"), Genre.valueOf(resultats.getString("genre").toUpperCase()), resultats.getInt("releaseYear"), 
						resultats.getString("producer"), actors, resultats.getString("summary"), resultats.getString("urlImage")));
			}

			conn.close();

			System.out.println("bye.");

			// traitement d'exception
		} catch (SQLException e) {
			System.err.println("failed");
			System.out.println("Affichage de la pile d'erreur");
			e.printStackTrace(System.err);
			System.out.println("Affichage du message d'erreur");
			System.out.println(e.getMessage());
			System.out.println("Affichage du code d'erreur");
			System.out.println(e.getErrorCode());

		}
	}

}
