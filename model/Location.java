package model;

import java.util.Date;

public class Location {
	private int id;
	private Client client;
	private DVD dvd;
	private Date date;
	
	public Location(int id, Client client, DVD dvd) {
		this.id = id;
		this.client = client;
		this.dvd = dvd;
		this.date = new Date();
	}
	
	public Location(int id, Client client, DVD dvd, Date date) {
		this.id = id;
		this.client = client;
		this.dvd = dvd;
		this.date = date;
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
