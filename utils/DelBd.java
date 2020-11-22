package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Errors.AL2000InternalError;
import Errors.WrongModeException;
import model.AL2000;
import model.DVD;
import model.Mode;

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

	public static void addDVD(DVD dvd, AL2000 al) throws WrongModeException, AL2000InternalError {

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

			remDvd.setInt(1, dvd.getId());

			remDvd.executeUpdate();

			conn.close();

		} catch (SQLException e) {
			// we delete the previously added dvd to keep the system coherent
			al.getDvds().add(dvd);
			System.err.println("could not remove the dvd please try again" + e);

		}

	}

}
