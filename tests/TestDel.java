package tests;

import errors.BdIncoherenteException;
import errors.SubscriptionException;
import model.AL2000;
import model.Abonne;
import model.Client;
import model.Location;
import model.Signalement;
import utils.AddBd;
import utils.DelBd;
import utils.InitBd;

public class TestDel {

	public static void main(String[] args) {
		InitBd.initBD();
		AL2000 al = new AL2000();
		try {
			InitBd.initAl2000(al);
		} catch (BdIncoherenteException e) {
			e.printStackTrace();
		}
		// everything should work and end with a WrongModeException

		Abonne Pepe;
		try {
			Pepe = new Abonne("479845685", "Pepemancel38@gmail.com", 8, 500, "gpudeponey");
			Location loc = new Location(012, Pepe, al.getDvds().get(2));
			Signalement sign = new Signalement(loc, "ceci est un test en vrai le film était plutot pas mal", 100);
			Client cli = new Client("456481356", "gpasdabo@free.fr", 504);
			
			
			AddBd.addAbo(Pepe, al);			
			AddBd.addLocation(loc, al);			
			AddBd.addHisto(loc);
			AddBd.addSignalement(sign, al);
			
			DelBd.delSign(sign, al);
			System.out.println("signalement delete");
			DelBd.delLocation(loc, al);
			System.out.println("Location delete");
			DelBd.delHisto(loc, Pepe);
			System.out.println("histo delete");

			
			
		} catch (SubscriptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
