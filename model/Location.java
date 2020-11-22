package model;

import java.sql.Date;

public class Location {
	@Override
	public String toString() {
		return "Location [id=" + id + ", client=" + client + ", dvd=" + dvd + ", date=" + date + "]";
	}

	private int id;
	private Client client;
	private DVD dvd;
	private java.sql.Date date;
	
	public Location(int id, Client client, DVD dvd) {
		this.id = id;
		this.client = client;
		this.dvd = dvd;
		this.date = new java.sql.Date(new java.util.Date().getTime());
	}
	
	public Location(int id, Client client, DVD dvd, Date date) {
		this.id = id;
		this.client = client;
		this.dvd = dvd;
		this.date = date;
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return (new java.util.Date()).getTime() > (this.date.getTime() + (24*60*60*1000));
	}
}
