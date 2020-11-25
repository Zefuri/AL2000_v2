package model;

public class Signalement {
	@Override
	public String toString() {
		return "id Location : " + getLocation().getId() + ", id Abonne : " + getLocation().getClient().getIdc();
	}

	private Location location;
	private String motif;
	private int montant;
	
	public Signalement(Location location, String motif, int montant) {
		this.location = location;
		this.motif = motif;
		this.montant = montant;
	}

	public Location getLocation() {
		return location;
	}
	
	public String getMotif() {
		return motif;
	}
	
	public int getMontant() {
		return montant;
	}
}
