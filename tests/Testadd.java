package tests;

import errors.BdIncoherenteException;
import errors.SubscriptionException;
import errors.WrongModeException;
import model.AL2000;
import model.Abonne;
import model.Client;
import model.DVD;
import model.Location;
import model.Signalement;
import utils.AddBd;
import utils.InitBd;

public class Testadd {
	
	public static void main(String[] args) {
		InitBd.initBD();
		AL2000 al = new AL2000();
		try {
			InitBd.initAl2000(al);
		} catch (BdIncoherenteException e) {
			e.printStackTrace();
		}
		
		try {
			//everything should work and end with a WrongModeException
			
			Abonne Pepe = new Abonne("479845685", "Pepemancel38@gmail.com", 8, 500, "gpudeponey");
			Location loc = new Location(012, Pepe, al.getDvds().get(2));
			Signalement sign = new Signalement(loc, "ceci est un test en vrai le film était plutot pas mal");
			Client cli = new Client("456481356", "gpasdabo@free.fr", 504);
			
			
			AddBd.addAbo(Pepe, al);
			System.out.println("addabo good");
			
			AddBd.addLocation(loc, al);
			System.out.println("addLocation good");
			
			AddBd.addHisto(loc);
			System.out.println("addhisto good");
			
			AddBd.addSignalement(sign, al);
			System.out.println("addSignalement good");
			
			
			AddBd.addClient(cli, al);
			System.out.println("addclient good");
			
			AddBd.addDVD(new DVD(0, null, null, 0, null, null, null, null, false), al);
			
			
			
		} catch (SubscriptionException e) {
			e.printStackTrace();
		} catch (WrongModeException e) {
			System.out.println("et oui c'est pas le bon mode");
		}

		
		
	}

}
