package model;

import java.util.ArrayList;

import errors.SubscriptionException;
import utils.UpdateBd;

public class Abonne extends Client {
	protected int credit;
	protected String mdp;
	protected ArrayList<Location> historique;
	
	public Abonne(String numCB, String mail, int idc, String mdp) {
		super(numCB, mail, idc);
		this.credit = 15;
		this.historique = new ArrayList<>();
		this.mdp = mdp;
	}

	public Abonne(String numCB, String mail, int idc, int credit, String mdp) throws SubscriptionException {
		super(numCB, mail, idc);
		if(credit >= 15) {
			this.credit = credit;
		} else {
			throw new SubscriptionException("Crédit insuffisant, un minimum de 15€ est requis.");
		}
		this.historique = new ArrayList<>();
		this.mdp = mdp;
	}
	
	public ArrayList<Location> getHistorique() {
		return historique;
	}
	
	public String getMdp() {
		return this.mdp;
	}

	public void setHistorique(ArrayList<Location> historique) {
		this.historique = historique;
	}
	
	public int getCredit() {
		return this.credit;
	}

	public void rechargerCredit() {
		this.credit += 10;
	}
	
	public void setCredit(int cred) {
		this.credit = cred;
	}
	/**
	 * 
	 * @param montant the amount of credit added or subtracted
	 * @throws SubscriptionException if the montant is less than 10 but is positive it is too low
	 */
	public void ChangeCredit(int montant) throws SubscriptionException{
		if(montant >= 10 && montant > 0 && montant < this.credit) {
			UpdateBd.updateCredit(this, montant);
		} else {
			if(montant > 0) {
				throw new SubscriptionException("Le montant de recharge minimum est de 10€ !");
			}else {
				throw new SubscriptionException("La maison ne fait pas credit !");
			}
			
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
