package model;

public enum Genre {
	ACTION,
	HORREUR,
	SCI_FI,
	AVENTURE,
	DOCUMENTAIRE,
	DRAME,
	COMEDIE,
	ANIMATION;

	public String toDisplayString() {
		String res;
		
		switch (this) {
		case ACTION:
			res = "Dans un film d'action";
			break;
		case ANIMATION:
			res = "Dans un film d'animation";
			break;
		case AVENTURE:
			res = "Dans un film pleins d'aventures";
			break;
		case COMEDIE:
			res = "Dans une comédie hilarante";
			break;
		case DOCUMENTAIRE:
			res = "Dans un documentaire poignant";
			break;
		case DRAME:
			res = "Dans un un film dramatique";
			break;
		case HORREUR:
			res = "Dans un film qui vous donnera des frissons";
			break;
		case SCI_FI:
			res = "Dans un film où la réalité laisse place à la science-fiction";
			break;
		default:
			res = "Dans un film ...";
			break;
		}
		
		return res;
	}
}
