package model;

public class Client {
	protected static int NB_LOCATION = 1;
	protected String numCB;
	protected String mail;
	protected int idc;
	
	public Client(String numCB, String mail, int idc) {
		this.numCB = numCB;
		this.mail = mail;
		this.idc = idc;
	}

	public String getNumCB() {
		return numCB;
	}

	public void setNumCB(String numCB) {
		this.numCB = numCB;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getIdc() {
		return idc;
	}

	public void setIdc(int idc) {
		this.idc = idc;
	}
	
	public boolean estAbonne() {
		return false;
	}
}
