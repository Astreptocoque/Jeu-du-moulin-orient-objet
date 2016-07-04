

public class Pion{

	/// variables g�n�rales � tout le programme
	private static int couleurRobot;
	private static int couleurJoueur;
	/// Donne la couleur du joueur qui joue
	private static int couleurActuelle;
	/// couleurs
	public final static int blanc = 6;
	public final static int noir = 1;
	public final static int vide = 0;
	/// couleur dominante, donc qui commence. Blanc = 6, noir = 1
	public final static int couleurDominante = blanc;

	/// pour l'optimisation des d�pla. du robot, en degr�s
//	public static int[] coordDernierPion = { 0, 0 };
	/// enregistre le dernier coup du joueur
	public static int derniereCaseJoueur;
	///
	public static int[] coordDernierPion = { 0, 0 };


	/// getter et setter pour les couleurs
	public static void setCouleurRobot(int couleur) {
		couleurRobot = couleur;
	}

	public static void setCouleurJoueur(int couleur) {
		couleurJoueur = couleur;
	}

	public static void setCouleurActuelle(int couleur) {
		couleurActuelle = couleur;
	}

	public static int getCouleurRobot() {
		return couleurRobot;
	}

	public static int getCouleurJoueur() {
		return couleurJoueur;
	}

	public static int getCouleurActuelle() {
		return couleurActuelle;
	}

	/// pour la derni�re case jou�e, pour l'intelligence
	public static int getDerniereCaseJoueur() {
		return derniereCaseJoueur;
	}

}