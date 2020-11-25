package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import errors.AL2000InternalError;
import errors.WrongModeException;
import model.AL2000;
import model.Abonne;
import model.DVD;
import model.Mode;

/**
 * This class contain methods allowing the update of something in the data base
 *
 */
public class UpdateBd {
	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:im2ag";

	static final String USER = "demarquq";
	static final String PASSWD = "Cork1440safety";
	static Connection conn;
	
	
	/**
	 * this method will update the credit of an abonne in the database
	 * @param abo the Abonne 
	 * @param montant the amount of money added or substracted form his credit
	 */
	public static void updateCredit(Abonne abo, int montant) {
		if(abo == null) {	
			return;
		}
		int oldCred = abo.getCredit();
		int newCred = oldCred + montant;
		abo.setCredit(newCred);
		try {

			

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement delhisto = conn.prepareStatement("UPDATE Abonne SET credit = ? WHERE idC = ?");
			
			delhisto.setInt(1, abo.getCredit());
			delhisto.setInt(2, abo.getIdc());
			
			delhisto.executeUpdate();
			
			

			conn.close();

		} catch (SQLException e) {
			abo.setCredit(oldCred);
			System.err.println("could not update the Abonne please try again" + e);

		}
	}
	/**
	 * change the availableness of the al2000
	 * @param dvd change the dispo of this dvd
	 * @param al
	 * @throws AL2000InternalError if the dvd is not in the AL2000
	 */
	public static void updateDVD(DVD dvd, AL2000 al) throws AL2000InternalError {

		if (dvd == null) {
			return;
		}

		if (!al.dvdexist(dvd.getId())) {
			throw new AL2000InternalError("this dvd is not in the Al2000");
		}
		
		dvd.changeDispoLoc();

		try {

			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);

			PreparedStatement remDvd = conn.prepareStatement("UPDATE DVDs set dispoloc = ? WHERE idD = ?");

			remDvd.setInt(2, dvd.getId());
			remDvd.setBoolean(1, dvd.isDispoLoc());

			remDvd.executeUpdate();

			conn.close();

		} catch (SQLException e) {
			dvd.changeDispoLoc();
			System.err.println("could not remove the dvd please try again" + e);

		}

	}
}
