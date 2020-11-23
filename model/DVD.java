package model;

import java.util.ArrayList;

public class DVD {
	@Override
	public String toString() {
		return "DVD [id=" + id + ", title=" + title + ", genre=" + genre + ", releaseDate=" + releaseDate
				+ ", producer=" + producer + ", actors=" + actors + ", summary=" + summary + ", dispoLoc=" + dispoLoc
				+ ", urlImage=" + urlImage + "]";
	}

	private int id;
	private String title;
	private Genre genre;
	private int releaseDate;
	private String producer;
	private ArrayList<String> actors;
	private String summary;
	private boolean dispoLoc;
	private String urlImage;

	public DVD(int id, String title, Genre genre, int releaseDate, String producer, ArrayList<String> actors,
			String summary, String urlImage) {
		this.id = id;
		this.title = title;
		this.genre = genre;
		this.releaseDate = releaseDate;
		this.producer = producer;
		this.actors = actors;
		this.summary = summary;
		this.urlImage = urlImage;
		this.dispoLoc = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public int getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(int releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public ArrayList<String> getActors() {
		return actors;
	}

	public void setActors(ArrayList<String> actors) {
		this.actors = actors;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public boolean isDispoLoc() {
		return dispoLoc;
	}

	public void changeDispoLoc() {
		this.dispoLoc = !dispoLoc;
	}

	public String getActorsAsString() {
		String res = "";
		
		if (this.actors.size() != 0) {
			res = this.actors.get(0);

			int i;
			for (i = 1; i < this.actors.size() - 1; i++) {
				res += ", " + this.actors.get(i);
			}

			res += "et " + this.actors.get(i);
		}

		return res;
	}
}
