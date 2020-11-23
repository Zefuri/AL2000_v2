package view;

import model.AL2000;
import model.Abonne;

public class SubscriberFrame extends MainFrame {
	private Abonne abonne;
	
	public SubscriberFrame(AL2000 al2000, Abonne abonne) {
		super(al2000);
		this.abonne = abonne;
	}

}
