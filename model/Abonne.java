package model;

import java.util.ArrayList;

public class Abonne extends Client {
	protected int credit;
	protected ArrayList<Location> historique;
	
	public Abonne(String numCB, String mail, int idc) {
		super(numCB, mail, idc);
		this.credit = 15;
		this.historique = new ArrayList<>();
	}

	public Abonne(String numCB, String mail, int idc, int credit) throws SubscriptionException {
		super(numCB, mail, idc);
		if(credit >= 15) {
			this.credit = credit;
		} else {
			throw new SubscriptionException("Crédit insuffisant, un minimum de 15€ est requis.");
		}
		this.historique = new ArrayList<>();
	}
	
	public ArrayList<Location> getHistorique() {
		return historique;
	}

	public void setHistorique(ArrayList<Location> historique) {
		this.historique = historique;
	}

	public void rechargerCredit() {
		this.credit += 10;
	}
	
	public void rechargerCredit(int montant) throws SubscriptionException{
		if(montant >= 10) {
			this.credit += montant;
		} else {
			throw new SubscriptionException("Le montant de recharge minimum est de 10€ !");
		}
	}
}
