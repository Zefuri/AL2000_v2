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
 * this class put at disposal static functions to delete object both from the
 * data base and from the al2000 using any of these methods with an
 * uninitialized al2000 will result in uncertain behaviors
 */
public class DelBd {

	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:im2ag";

	static final String USER = "demarquq";
	static final String PASSWD = "Cork1440safety";
	static Connection conn;

	
	/**
	 * 
	 *delete a dvd from the database and from the al2000
	 *@throws WrongModeException this method must be used in the mode Technicien
	 *@throws AL2000Internal Error if the al2000 is full or if the dvd is not in the al2000
	 *@param dvd the dvd that should be deleted from the al 2000 and the database
	 *@param al the al2000 which will delete the dvd
	*/
	public static void delDVD(DVD dvd, AL2000 al) throws WrongModeException, AL2000InternalError {

		if (dvd == null) {
			return;
		}

		if (al.getMode() == Mode.UTILISATEUR) {
			throw new WrongModeException("addDvD must be used in thechnicien mode");
		}

		if (!al.dvdexist(dvd.getId())) {
			throw new AL2000InternalError("this dvd is not in the Al2000");
		}

		al.getDvds().remove(dvd);

		try {

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement remDvd = conn.prepareStatement("DELETE FROM DVDs WHERE idD = ?");
			PreparedStatement remLoc = conn.prepareStatement("DELETE FROM Locations WHERE idDVD = ?");

			remDvd.setInt(1, dvd.getId());
			remLoc.setInt(1, dvd.getId());

			remDvd.executeUpdate();

			conn.close();

		} catch (SQLException e) {
			// we delete the previously added dvd to keep the system coherent
			al.getDvds().add(dvd);
			System.err.println("could not remove the dvd please try again" + e);

		}

	}
	/**
	 * delete a Location from the database and from the al2000
	 * @param loc the Location that should be deleted from the al 2000 and the database
	 * @param al the al2000 from which the location will be deleted
	 */
	public static void delLocation(Location loc, AL2000 al) {
		if(loc == null) {
			return;
		}
		
		al.getCurrentLocation().add(loc);
		
		try {

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement delloc = conn.prepareStatement("DELETE FROM Locations WHERE idLocation = ?");
			
			delloc.setInt(1, loc.getId());
			
			delloc.executeUpdate();
			
			

			conn.close();

		} catch (SQLException e) {
			al.getCurrentLocation().add(loc);
			System.err.println("could not remove the Location please try again" + e);

		}
		
	}
	
	/**
	 * delete a Location from the database and from the al2000
	 * @param loc the Location that should be deleted from the al 2000 and the database
	 * @param abo the Abonne from which the location in his historique will be deleted
	 */
	public static void delHisto(Location loc, Abonne abo) {
		if(loc == null) {
			return;
		}
		
		abo.getHistorique().remove(loc);
		
		try {

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement delhisto = conn.prepareStatement("DELETE FROM Historique WHERE idLocation = ?");
			
			delhisto.setInt(1, loc.getId());
			
			delhisto.executeUpdate();
			
			

			conn.close();

		} catch (SQLException e) {
			abo.getHistorique().add(loc);
			System.err.println("could not remove the Location please try again" + e);

		}
		
	}
	
	/**
	 * delete a Location from the database and from the al2000
	 * @param sign the signalement that should be deleted from the al 2000 and the database
	 * @param al the AL2000 from which the signalement will be deleted
	 */
	public static void delSign(Signalement sign, AL2000 al) {
		if(sign == null) {
			return;
		}
		
		al.getSignalements().remove(sign);
		
		try {

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement delsign = conn.prepareStatement("DELETE FROM Signalement WHERE idLocation = ?");
			
			delsign.setInt(1, sign.getLocation().getId());
			
			delsign.executeUpdate();
			
			

			conn.close();

		} catch (SQLException e) {
			al.getSignalements().add(sign);
			System.err.println("could not remove the Location please try again" + e);

		}
		
	}
	
	
	

}
