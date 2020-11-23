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

import errors.BdIncoherenteException;
import errors.SubscriptionException;
import model.AL2000;
import model.Abonne;
import model.Client;
import model.DVD;
import model.Genre;
import model.Location;
import model.Signalement;
import model.Technicien;

public final class InitBd {

	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:im2ag";

	static final String USER = "demarquq";
	static final String PASSWD = "Cork1440safety";
	static Connection conn;

	/**
	 * reset the attribute of an object AL2000 with the database l'al2000 must be
	 * empty otherwise the behavior of this method is undefined
	 *
	 * @param al the AL2000 object that should be reset
	 * @throws BdIncoherenteException if the database is not in a coherent state,
	 *                                should not happened but we never know.
	 *                                Consider resetting the data base if it
	 *                                happens.
	 */
	public static void initAl2000(AL2000 al) throws BdIncoherenteException {
		initdvd(al);
		initAbonne(al);
		initClient(al);
		initSignalements(al);
		initLocation(al);
		initTech(al);
		System.out.println("Init al2000 done");

	}

	/**
	 * 
	 * Initialize the oracle database with the sql scripts in the package bd
	 */
	public static void initBD() {
		System.out.println("Begining BD init");
		ArrayList<String[]> listqueries = new ArrayList<>();
		listqueries.add(createqueries("/bd/data.sql"));
		listqueries.add(createqueries("/bd/initdvd.sql"));
		listqueries.add(createqueries("/bd/initClients.sql"));
		listqueries.add(createqueries("/bd/initLoc.sql"));
		listqueries.add(createqueries("/bd/initHisto.sql"));
		listqueries.add(createqueries("/bd/initSignal.sql"));

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
			for (String[] str : listqueries) {
				for (String query : str) {
					System.out.println(query + "à faire");
					PreparedStatement createbd = conn.prepareStatement(query);
					createbd.executeUpdate();

				}
			}
			conn.close();
			System.out.println("Bd init done");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void initClient(AL2000 al) {
		try {
			System.out.println("Begining Client init");
			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement affichegard = conn.prepareStatement("SELECT * FROM Client");

			resultats = affichegard.executeQuery();
			while (resultats.next()) {
				al.getClients().add(new Client(resultats.getString("numCB"), resultats.getString("email"),
						resultats.getInt("idC")));

			}

			conn.close();
			System.out.println("Client init done");

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

	private static String[] createqueries(String path) {
		DVD dvd = new DVD(0, null, null, 0, null, null, null, null);
		URL url = dvd.getClass().getResource(path);
		File data = new File(url.getPath());
		String initbd = "";
		String queries[] = null;
		try {
			Scanner reader = new Scanner(data);
			while (reader.hasNextLine()) {
				initbd += reader.nextLine();
			}

			queries = initbd.split(";");
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		return queries;
	}

	private static void initAbonne(AL2000 al) throws BdIncoherenteException {
		// Les DVD de l'al2000 doit être initialisé avant d'utiliser cette fonction
		System.out.println("Begining Abonne init");
		if (al.getDvds() == null) {
			System.out.println("initclients doit être utilisé avec un al200 ayant ses dvds initialisés");
			return;
		}
		try {

			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement getAbo = conn.prepareStatement("SELECT * FROM Abonne");
			ArrayList<Abonne> abos = new ArrayList<Abonne>();
			resultats = getAbo.executeQuery();
			while (resultats.next()) {
				ResultSet reqhisto = null;
				Abonne abo = new Abonne(resultats.getString("numCB"), resultats.getString("email"),
						resultats.getInt("idc"), resultats.getInt("credit"), resultats.getString("mdp"));

				ArrayList<Location> histo = new ArrayList<Location>();

				PreparedStatement getloc = conn.prepareStatement("Select * FROM Historique WHERE idAbonne = ?");

				getloc.setInt(1, resultats.getInt("idc"));

				reqhisto = getloc.executeQuery();
				while (reqhisto.next()) {
					int idD = reqhisto.getInt("idDvd");
					DVD dvdloc = null;
					for (DVD dvd : al.getDvds()) {
						if (dvd.getId() == idD) {
							dvdloc = dvd;
						}
					}
					if (dvdloc == null) {
						throw new BdIncoherenteException("Location incohérente");
					}

					histo.add(
							new Location(reqhisto.getInt("idLocation"), abo, dvdloc, reqhisto.getDate("dateLocation")));
				}

				abo.setHistorique(histo);
				abos.add(abo);
			}

			al.setAbonnes(abos);
			conn.close();

			System.out.println("Abonne init done");

			// traitement d'exception
		} catch (SQLException e) {
			System.err.println("failed");
			System.out.println("Affichage de la pile d'erreur");
			e.printStackTrace(System.err);
			System.out.println("Affichage du message d'erreur");
			System.out.println(e.getMessage());
			System.out.println("Affichage du code d'erreur");
			System.out.println(e.getErrorCode());

		} catch (SubscriptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void initSignalements(AL2000 al) throws BdIncoherenteException {
		try {
			System.out.println("Begining Signalements init");
			if (al.getDvds() == null || al.getClients() == null) {
				System.out.println(
						"Signalement doit être utilisé avec un al200 ayant ses dvds et ses clients initialisés");
				return;
			}

			ResultSet resultats = null;
			ArrayList<Signalement> signalements = new ArrayList<Signalement>();
			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement getsignal = conn.prepareStatement("SELECT * FROM Signalement");

			resultats = getsignal.executeQuery();
			while (resultats.next()) {
				int idloc = resultats.getInt("idLocation");
				Location loc = null;
				for (Abonne abo : al.getAbonnes()) {
					for (Location loca : abo.getHistorique()) {
						if (loca.getId() == idloc) {
							loc = loca;
						}
					}
				}
				if (loc == null) {
					throw new BdIncoherenteException("Signalement incohérent id = " + idloc);
				}
				signalements.add(new Signalement(loc, resultats.getString("signalement")));

			}

			al.setSignalements(signalements);

			conn.close();
			System.out.println("Signalement init done");

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

	private static void initTech(AL2000 al) {
		try {
			System.out.println("Begining Technicien init");
			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement affichegard = conn.prepareStatement("SELECT * FROM Techniciens");

			resultats = affichegard.executeQuery();
			while (resultats.next()) {
				al.getTechniciens().put(resultats.getInt("idt"), new Technicien(al, resultats.getString("mdp")));

			}

			conn.close();
			System.out.println("Technicien init done");

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

	private static void initdvd(AL2000 al) {
		try {
			System.out.println("Begining dvd init");
			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement affichegard = conn.prepareStatement("SELECT * FROM DVDS");

			resultats = affichegard.executeQuery();
			while (resultats.next()) {
				ArrayList<String> actors = new ArrayList<String>();
				String[] act = resultats.getString("actors").replace("[", "").replace("]", "").split(",");
				for (String str : act) {
					actors.add(str);
				}
				al.getDvds()
						.add(new DVD(resultats.getInt("idD"), resultats.getString("title"),
								Genre.valueOf(resultats.getString("genre").toUpperCase()),
								resultats.getInt("releaseYear"), resultats.getString("producer"), actors,
								resultats.getString("summary"), resultats.getString("urlImage")));
			}

			conn.close();

			System.out.println("dvd init done");

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

	private static void initLocation(AL2000 al) {
		try {
			System.out.println("Begining Location init");
			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement affichegard = conn.prepareStatement("SELECT * FROM Locations");

			resultats = affichegard.executeQuery();
			while (resultats.next()) {
				DVD locdvd = null;
				Client locCli = null;
				for (DVD dvd : al.getDvds()) {
					if (dvd.getId() == resultats.getInt("idDvd")) {
						locdvd = dvd;
					}
				}
				if (resultats.getInt("idAbonne") == 0) {
					for (Client cli : al.getClients()) {
						if (cli.getIdc() == resultats.getInt("idClient")) {
							locCli = cli;
						}
					}
				}else {
					for(Abonne abo : al.getAbonnes()) {
						if (abo.getIdc() == resultats.getInt("idAbonne")) {
							locCli = abo;
						}
					}
				}
				al.getCurrentLocation().add(new Location(resultats.getInt("idLocation"), locCli, locdvd, resultats.getDate("dateLocation")));

			}

			conn.close();
			System.out.println("Location init done");

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
