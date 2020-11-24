package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import errors.BdIncoherenteException;
import errors.WrongPasswordException;
import utils.AddBd;
import utils.DelBd;
import utils.InitBd;

public class AL2000 {



	private Mode mode;
	private ArrayList<Client> clients;
	private ArrayList<Abonne> abonnes;
	private HashMap<Integer, Technicien> techniciens;
	private ArrayList<DVD> dvds;
	private ArrayList<Signalement> signalements;
	private ArrayList<Location> currentLocation;
 	
	public AL2000() {
		this.mode = Mode.UTILISATEUR;
		this.clients = new ArrayList<>();
		this.techniciens = new HashMap<>();
		this.dvds = new ArrayList<>();
		this.abonnes = new ArrayList<>();
		this.signalements = new ArrayList<>();
		this.currentLocation = new ArrayList<>();
	}
	/**
	 * @param idc the id of the client we want to find
	 * @return the client with the id idc or null if the client is not in the list
	 */
	public Client getClientId(int idc) {
		for(Client cli : this.clients) {
			if(cli.getIdc() == idc) {
				return cli;
			}
		}
		return null;
	}
	/**
	 * @param ida the id of the client we want to find
	 * @return the subscriber with the id ida or null if the subscriber is not in the list
	 */
	public Abonne getAbonneId(int ida) {
		for(Abonne abo : this.abonnes) {
			if(abo.getIdc() == ida) {
				return abo;
			}
		}
		return null;
	}
	/**
	 * give us the amount of money the customer owe us, being 4/day for Abonne and 5/day otherwise
	 * every day began is a day which must be paid
	 * @param loc the location
	 * @return the amount of money the customer owe us
	 */
	public int montantLoc(Location loc) {
		Date now = new Date();
		Date old = loc.getDate();
		int i = 1;
		int prix = 5;
		if(loc.getClient().estAbonne()) {
			prix = 4;
		}
		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		while(now.compareTo(old) < 0) {
			i++;
			c.setTime(old); 
			c.add(Calendar.DATE, 1);
			old = c.getTime();
		}
		return i * prix;
	}
	/**
	 * end the location and add it to the corresponding Historique if needed
	 * @param loc the location being ended
	 */
	public void rendreLocation(Location loc) {
		DelBd.delLocation(loc, this);
		if(loc.getClient().estAbonne()) {
			AddBd.addHisto(loc);
		}
		return;
	}
	
	public ArrayList<Location> getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(ArrayList<Location> currentLocation) {
		this.currentLocation = currentLocation;
	}

	public ArrayList<Abonne> getAbonnes() {
		return abonnes;
	}

	public void setAbonnes(ArrayList<Abonne> abonnes) {
		this.abonnes = abonnes;
	}
	
	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public ArrayList<Client> getClients() {
		return clients;
	}

	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}

	public HashMap<Integer, Technicien> getTechniciens() {
		return techniciens;
	}

	public void setTechniciens(HashMap<Integer, Technicien> techniciens) {
		this.techniciens = techniciens;
	}

	public ArrayList<DVD> getDvds() {
		return dvds;
	}

	public void setDvds(ArrayList<DVD> dvds) {
		this.dvds = dvds;
	}

	public ArrayList<Signalement> getSignalements() {
		return signalements;
	}

	public void setSignalements(ArrayList<Signalement> signalements) {
		this.signalements = signalements;
	}
	
	@Override
	public String toString() {
		return "AL2000 [mode=" + mode + ", clients=" + clients + "\n" + ", Abonnes=" + abonnes + "\n" + ", techniciens=" + (techniciens==null)+ "\n"
				+ ", dvds=" + (dvds == null)+ "\n" + ", signalements=" + (signalements== null)+ "\n" + "]";
	}
	
	public boolean dvdexist(int idDvd) {
		for (DVD dvd : this.dvds) {
			if(dvd.getId() == idDvd) {
				return true;
			}
		}
		return false;
		
	}

	public void modeMaintenance(int idTech, String mdp) throws WrongPasswordException {
		switch(mode) {
			case MAINTENANCE:
				this.mode = Mode.UTILISATEUR;
				break;
			case UTILISATEUR:
				if(techniciens.containsKey(idTech)) {
					if(techniciens.get(idTech).connexion(mdp)) {
						this.mode = Mode.MAINTENANCE;
					} else {
						throw new WrongPasswordException("Mauvais mot de passe, connexion impossible.");
					}
				}
		}
		
	}
	
	
}
