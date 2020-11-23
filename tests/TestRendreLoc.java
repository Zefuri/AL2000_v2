package tests;

import errors.BdIncoherenteException;
import model.AL2000;
import utils.InitBd;

public class TestRendreLoc {
	
	public static void main(String[] args) {
		AL2000 al = new AL2000();
		try {
			InitBd.initAl2000(al);
		} catch (BdIncoherenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int test = al.montantLoc(al.getCurrentLocation().get(0));
		System.out.println(test);
	}

}
