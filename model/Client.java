package model;

public class Client {
	protected static int NB_LOCATION = 1;
	protected String numCB;
	protected String mail;
	
	public Client(String numCB, String mail) {
		this.numCB = numCB;
		this.mail = mail;
	}
	
	public boolean estAbonne() {
		return false;
	}
}
