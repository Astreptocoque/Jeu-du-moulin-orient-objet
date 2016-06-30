import java.util.HashMap;
import java.util.Map;

import lejos.hardware.lcd.LCD;

public class Pion extends Cases {

	/// variables générales à tout le programme
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

	/// pour l'optimisation des dépla. du robot, en degrés
	public static int[] coordDernierPion = { 0, 0 };
	/// enregistre le dernier coup du joueur
	private static int derniereCaseJoueur;
	///

	/// variables pour les objets pions
	int couleur; /// couleur du pion
	int numeroCaseDepart; /// N° de la case départ du pion
	int numeroCaseArrivee; /// N° de la case arrivée du pion

	/// constructeurs
	public Pion(int couleur) {
		super(couleur);
		this.couleur = couleur;
	}

	/// getter et setter pour les déplacements

	public void setCaseDepart(int numeroCase) {
		this.numeroCaseDepart = numeroCase;
		/// modifie l'emplacement du nouveau pion en tant
		/// que vide
		/// le if est là pour empecher une erreur lors de la pose
		/// initiale des pions
		if (numeroCase > 0 && numeroCase < 25) {
			super.tabCases[numeroCase - 1].pion = vide;
		}
	}

	public void setCaseArrivee(int numeroCase) {
		this.numeroCaseArrivee = numeroCase;
		/// modifie l'emplacement du nouveau pion
		/// le if est là pour empecher une erreur lors de la pose
		/// initiale des pions
		if (numeroCase > 0 && numeroCase < 25) {
			super.tabCases[numeroCase].pion = Pion.getCouleurActuelle();
		}
		/// enregistre la dernière case. La condition est là pour
		/// empêcher de changer la case si le joueur mange un pion
		if (super.getMode() != 4) {
			derniereCaseJoueur = numeroCase;
		}

	}

	public int getCouleurPion() {
		return this.couleur;
	}

	public int getCaseDepart() {
		return this.numeroCaseDepart;
	}

	public int getCaseArrivee() {
		return this.numeroCaseArrivee;
	}

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

	/// pour la dernière case jouée, pour l'intelligence
	public static int getDerniereCaseJoueur() {
		return derniereCaseJoueur;
	}

}