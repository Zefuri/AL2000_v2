package model;

public class Signalement {
	private Location location;
	private String motif;
	
	public Signalement(Location location, String motif) {
		this.location = location;
		this.motif = motif;
	}

	public Location getLocation() {
		return location;
	}
	
	public String getMotif() {
		return motif;
	}
}
