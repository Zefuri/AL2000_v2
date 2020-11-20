package model;

import java.util.ArrayList;

public class Abonne extends Client {
	protected int credit;
	protected String mdp;
	protected ArrayList<Location> historique;
	
	public Abonne(String numCB, String mail) {
		super(numCB, mail);
		this.credit = 15;
		this.historique = new ArrayList<>();
	}

	public Abonne(String numCB, String mail, int credit) throws SubscriptionException {
		super(numCB, mail);
		if(credit >= 15) {
			this.credit = credit;
		} else {
			throw new SubscriptionException("Crédit insuffisant, un minimum de 15€ est requis.");
		}
		this.historique = new ArrayList<>();
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
	
	public boolean verifierMdp(String mdp) {
		return (this.mdp.compareTo(mdp) == 0 ? true : false);
	}
	
	@Override
	public boolean estAbonne() {
		return true;
	}
}
