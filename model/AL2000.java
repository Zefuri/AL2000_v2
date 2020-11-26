package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import errors.BdIncoherenteException;
import errors.LocationException;
import errors.SubscriptionException;
import errors.TechnicianException;
import errors.WrongPasswordException;
import utils.AddBd;
import utils.DelBd;
import utils.InitBd;
import utils.UpdateBd;

public class AL2000 {
	
	private Mode mode;
	private boolean connected;
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
		this.connected = false;
	}
	
	public Location findDVDLoc(int idDVD) throws LocationException {
		Location res = null;
		
		int i = 0;
		while(i < this.currentLocation.size() && idDVD != this.currentLocation.get(i).getDvd().getId()) {
			i++;
		}
		
		if(i < this.currentLocation.size()) {
			res = this.currentLocation.get(i);
		} else {
			throw new LocationException("Aucune locations ne correspond à ce DVD !");
		}
		
		return res;
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
	 * @param ida the id of the subscriber we want to find
	 * @param pwd the password of the subscriber
	 * @return the subscriber with the id ida
	 */
	public Abonne connectAbonne(int ida, String pwd) throws SubscriptionException, WrongPasswordException {
		Abonne res = null;
		
		int i = 0;
		while(i < this.abonnes.size() && ida != this.abonnes.get(i).getIdc()) {
			i++;
		}
		
		if(i < this.abonnes.size()) {
			res = this.abonnes.get(i);
			
			if (res.verifierMdp(pwd)) {
				this.connected = true;
			} else {
				throw new WrongPasswordException("Mot de passe erronné : réessayez !");
			}
		} else {
			throw new SubscriptionException("Abonné introuvable !");
		}
		
		return res;
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
		int i = 0;
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
		for(DVD dvd : this.dvds) {
			if(dvd.getId() == loc.getDvd().getId()) {
				 UpdateBd.updateDVD(dvd, this);
				 break;
			}
		}
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
	
	public boolean isConnected() {
		return this.connected;
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
	
	public void addTech(int idt, String mdp) {
		this.getTechniciens().put(idt, new Technicien(this, mdp));
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
		return "AL2000 [mode=" + mode + ", clients=" + clients + "\n" + ", Abonnes=" + abonnes + "\n" + ", techniciens=" + (techniciens.get(1) == null)+ "\n"
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

	public Technicien modeMaintenance(int idTech, String mdp) throws TechnicianException, WrongPasswordException {
		Technicien res = null;
		
		switch(mode) {
			case MAINTENANCE:
				this.mode = Mode.UTILISATEUR;
				this.connected = false;
				break;
			case UTILISATEUR:
				if(techniciens.containsKey(idTech)) {
					if(techniciens.get(idTech).connexion(mdp)) {
						res= techniciens.get(idTech);
						this.mode = Mode.MAINTENANCE;
						this.connected = true;
					} else {
						throw new WrongPasswordException("Mot de passe erroné : réessayez !");
					}
				} else {
					throw new TechnicianException("Technicien introuvable §");
				}
		}
		
		return res;
	}
	
	public Client createNewClient(String mail, String numCB) {
		int max = 0;
		
		for(Client c : this.clients) {
			if(c.getIdc() > max) {
				max = c.getIdc();
			}
		}
		
		return new Client(numCB, mail, max+1);
	}
	
	public Abonne createNewAbonne(String mail, String pwd, String numCB, int credit) {
		int max = 0;
		
		for(Abonne a : this.abonnes) {
			if(a.getIdc() > max) {
				max = a.getIdc();
			}
		}
		
		Abonne res = null;
		if(credit > 0 ) {
			try {
				res = new Abonne(numCB, mail, max+1, credit, pwd);
			} catch (SubscriptionException e) {
				e.printStackTrace();
			}
		} else {
			res = new Abonne(numCB, mail, max+1, pwd);
		}
		
		return res;
	}
}
