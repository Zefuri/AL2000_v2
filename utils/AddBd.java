package utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import errors.AL2000InternalError;
import errors.WrongModeException;
import model.AL2000;
import model.Abonne;
import model.Client;
import model.DVD;
import model.Location;
import model.Mode;
import model.Signalement;


/**
 * 
 *this class put at disposal static functions to add object both to the data base and to the al2000
 *using any of these methods with an uninitialized al2000 will result in uncertain behaviors
 *Adding object with null attributes will probably not work
*/
public class AddBd {

	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:im2ag";

	static final String USER = "demarquq";
	static final String PASSWD = "Cork1440safety";
	static Connection conn;
	
	
	/**
	 * 
	 *add a dvd to the database and to the al2000
	 *@throws WrongModeException this method must be used in the mode Technicien
	 *@throws AL2000Internal Error if the al2000 is full or if the dvd is already in the al2000
	 *@param dvd the dvd that should be added to the al 2000 and the database
	 *@param al the al2000 wich will recieve the dvd
	*/
	public static void addDVD(DVD dvd, AL2000 al) throws WrongModeException, AL2000InternalError{
		
		if(dvd == null) {
			return;
		}
		
		if(al.getMode() == Mode.UTILISATEUR) {
			throw new WrongModeException("addDvD must be used in thechnicien mode");
		}
		
		
		if(al.getDvds().size() >= 100) {
			throw new AL2000InternalError("the al2000 is full cannot add another dvd");
		}
		
		if(al.dvdexist(dvd.getId())) {
			throw new AL2000InternalError("this dvd is already in the Al2000");
		}
		
		al.getDvds().add(dvd);
		
		try {
			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement addDvd = conn.prepareStatement("INSERT INTO DVDs Values (?, ?, ?, ?, ?, ?, ?, ?)");
			
			addDvd.setInt(1, dvd.getId());
			addDvd.setString(2, dvd.getTitle());
			addDvd.setString(3, dvd.getGenre().toString());
			addDvd.setInt(4, dvd.getReleaseDate());
			addDvd.setString(5, dvd.getProducer());
			addDvd.setString(6, dvd.getActors().toString());
			addDvd.setString(7, dvd.getSummary());
			addDvd.setString(8, dvd.getUrlImage());
			

			addDvd.executeUpdate();

			conn.close();

		} catch (SQLException e) {
			//we delete the previously added dvd to keep the system coherent
			al.getDvds().remove(dvd);
			System.err.println("could not add the dvd please try again" + e);

		}
		
		
		
	}
	/**
	 * add an abonne to the database and to the al2000
	 *@param abo the Abonne that should be added to the al 2000 and the database
	 *@param al the al2000 wich will recieve the dvd
	*/
	public static void addAbo(Abonne abo, AL2000 al) {
		if(abo == null) {
			return;
		}
		al.getAbonnes().add(abo);
		try {
			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement addabo = conn.prepareStatement("INSERT INTO Abonne Values (?, ?, ?, ?, ?)");
			
			addabo.setInt(1, abo.getIdc());
			addabo.setString(2, abo.getMail());
			addabo.setString(3, abo.getNumCB());
			addabo.setString(4, abo.getMdp());
			addabo.setInt(5, abo.getCredit());

			addabo.executeUpdate();

			conn.close();

		} catch (SQLException e) {
			//we delete the previously added Abonne to keep the system coherent
			al.getAbonnes().remove(abo);
			System.err.println("could not add the Abonne please try again " + e);

		}
	}
	/**
	 * add a client to the database and to the al2000
	 *@param cli the client that should be added to the al 2000 and the database
	 *@param al the al2000 wich will recieve the dvd
	*/
	public static void addClient(Client cli, AL2000 al) {
		if(cli == null) {
			return;
		}
		al.getClients().add(cli);
		try {
			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement addcli = conn.prepareStatement("INSERT INTO Client Values (?, ?, ?)");
			
			addcli.setInt(1, cli.getIdc());
			addcli.setString(2, cli.getMail());
			addcli.setString(3, cli.getNumCB());

			addcli.executeUpdate();

			conn.close();

		} catch (SQLException e) {
			//we delete the previously added client to keep the system coherent
			al.getClients().remove(cli);
			System.err.println("could not add the Client please try again" + e);

		}
	}
	
	/**
	 * add a Signalement to the database and to the al2000
	 *@param sign the Signalement that should be added to the al 2000 and the database
	 *@param al the al2000 wich will recieve the Signalement
	*/
	public static void addSignalement(Signalement sign, AL2000 al) {
		if(sign == null) {
			return;
		}
		al.getSignalements().add(sign);
		try {
			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement addsign = conn.prepareStatement("INSERT INTO Signalement Values (?, ?)");
			
			addsign.setInt(1, sign.getLocation().getId());
			addsign.setString(2, sign.getMotif());

			addsign.executeUpdate();

			conn.close();

		} catch (SQLException e) {
			//we delete the previously added Signalement to keep the system coherent
			al.getSignalements().remove(sign);
			System.err.println("could not add the Signalement please try again" + e);

		}
	}
	
	/**
	 * add a Location to the database and add it to the corresponding historique
	 *@param loc the Location that should be added to the database
	*/
	public static void addLocation(Location loc, AL2000 al) {
		if(loc == null) {
			return;
		}
		
		al.getCurrentLocation().add(loc);
		Client cli = loc.getClient();
		int idc = cli.getIdc();
		int ida = 000;
		if(cli.estAbonne()) {
			ida = idc;
			idc = 000;
		}
		
		try {
			ResultSet resultats = null;

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement addloc = conn.prepareStatement("INSERT INTO LOCATIONS Values (?, ?,?,?,?)");
			
			addloc.setInt(1, loc.getId());
			addloc.setInt(2, loc.getDvd().getId());
			addloc.setInt(3, idc);
			addloc.setInt(4, ida);
			addloc.setDate(5, (Date) loc.getDate());
			
			addloc.executeUpdate();
			
			

			conn.close();

		} catch (SQLException e) {
			al.getCurrentLocation().remove(loc);
			System.err.println("could not add the Location please try again" + e);

		}
	}

	
	/**
	 * add location to the corresponding historique of the corresponding abonne and into the database
	 *@param loc the Location that should be put into an historique
	*/
	public static void addHisto(Location loc) {
		
		if(loc == null) {
			return;
		}
		
		Abonne abo = (Abonne) loc.getClient();
		
		if(!abo.estAbonne()) {
			return;
		}
		
		abo.getHistorique().add(loc);
		try {
			



			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);


			PreparedStatement addHisto = conn.prepareStatement("INSERT INTO Historique Values (?,?,?,?,SYSDATE)");
			
			addHisto.setInt(1, loc.getId());
			addHisto.setInt(2, loc.getDvd().getId());
			addHisto.setInt(3, loc.getClient().getIdc());
			addHisto.setDate(4, (Date) loc.getDate());
			
			addHisto.executeUpdate();
				

			conn.close();

		} catch (SQLException e) {
			abo.getHistorique().remove(loc);
			System.err.println("could not add the historique please try again" + e);

		}
	}

}
