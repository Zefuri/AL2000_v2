package model;

import java.util.Date;

public class Location {
	private Client client;
	private DVD dvd;
	private Date date;
	
	public Location(Client client, DVD dvd) {
		this.client = client;
		this.dvd = dvd;
		this.date = new Date();
	}

	public Client getClient() {
		return client;
	}

	public DVD getDvd() {
		return dvd;
	}
	
	public Date getDate() {
		return date;
	}
	
	boolean isLate() {
		return (new Date()).getTime() > (this.date.getTime() + (24*60*60*1000));
	}
}
