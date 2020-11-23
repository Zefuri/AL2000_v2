package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Abonne;

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
}
