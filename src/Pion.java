import java.util.HashMap;
import java.util.Map;

import lejos.hardware.lcd.LCD;

public class Pion {

	/// variables g�n�rales � tout le programme
	private static int couleurRobot;
	private static int couleurJoueur;
	/// Donne la couleur du joueur qui joue
	private static int couleurActuelle;
	/// couleurs
	public final static int blanc = 6;
	public final static int noir = 1;
	/// couleur dominante, donc qui commence. Blanc = 6, noir = 1
	public final static int couleurDominante = blanc;
	/// nbr de pions restant pour chaque joueur
	private static int nbrPionsJoueur = 9;
	private static int nbrPionsRobot = 9;
	/// pour l'optimisation des d�pla. du robot, en degr�s
	public static int[] coordDernierPion = { 0, 0 };

	/// variables pour les objets pions
	int couleur; /// couleur du pion
	int numeroCaseDepart; /// N� de la case d�part du pion
	int numeroCaseArrivee; /// N� de la case arriv�e du pion
	int[] coordCaseDepart; /// Case d�part en coordonn�es
	int[] coordCaseArrivee; /// Case arriv�e en coordonn�es

	/// map contenant les coordonn�es de chaques cases. Origine en haut �
	/// droite.
	/// cl� : num�ro de la case. Valeur : coordon�es
	public static Map<Integer, int[]> caseIDCoord = new HashMap<Integer, int[]>();
	/// map contenant les cases en cl� et la couleur des pions en valeur
	public static Map<Integer, Integer> caseID = new HashMap<Integer, Integer>();

	public Pion(int couleur, int numAncienneCase, int numNouvelleCase) {
		this.couleur = couleur;
		setCaseDepart(numAncienneCase);
		setCaseArrivee(numNouvelleCase);
	}

	public Pion(int couleur) {
		this.couleur = couleur;
	}

	public Pion() {

	}

	/// getter et setter
	public void setCouleurPion(int pCouleur) {
		this.couleur = pCouleur;
	}

	public void setCaseDepart(int numeroCase) {
		this.numeroCaseDepart = numeroCase;
		this.coordCaseDepart = caseIDCoord.get(numeroCase).clone();
		/// modifie l'emplacement du nouveau pion dans CaseID en tant
		/// que vide
		/// le if est l� pour empecher une erreur lors de la pose
		/// initiale des pions
		if (numeroCase > 0 && numeroCase < 25) {
			caseID.remove(numeroCase);
			caseID.put(numeroCase, 0);
		}
	}

	public void setCaseArrivee(int numeroCase) {
		this.numeroCaseArrivee = numeroCase;
		this.coordCaseArrivee = caseIDCoord.get(numeroCase).clone();
		/// modifie l'emplacement du nouveau pion dans CaseID
		/// le if est l� pour empecher une erreur lors de la pose
		/// initiale des pions
		if (numeroCase > 0 && numeroCase < 25) {
			caseID.remove(numeroCase);
			caseID.put(numeroCase, this.couleur);
		}
		/// pour optimiser les d�placements du robot
		// coordDernierPion = caseIDCoord.get(numeroCase).clone();
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

	/// getter et setter couleur robot
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

	/// g�re le total des pions
	public static void enlevePionJoueur() {
		nbrPionsJoueur = nbrPionsJoueur - 1;
		LCD.clear(0);
		LCD.drawString("nbr J. : " + getNbrPionsJoueur(),  0, 0);
	}

	public static void enlevePionRobot() {
		nbrPionsRobot = nbrPionsRobot - 1;
		LCD.clear(1);
		LCD.drawString("nbr R. : " + getNbrPionsRobot(),  0, 1);
	}

	public static int getNbrPionsJoueur() {
		return nbrPionsJoueur;
	}

	public static int getNbrPionsRobot() {
		return nbrPionsRobot;
	}

	/// maps
	public static void creationMapCaseIDCoord() {
		/// cr�� une map de cl�/valeurs
		/// les cl�s sont le NUMERO de la case
		/// les valeurs sont COORDONNEES de chaque case
		int numeroCase = 1;
		int max = 3;
		int[] tableauMomentane = new int[2];

		/// valeurs pour cr�er le couple de coord. x/y des cases
		int[][] valeurs = { { 1, 4, 7 }, { 2, 4, 6 }, { 3, 4, 5 }, { 1, 2, 3, 5, 6, 7 }, { 3, 4, 5 },
				{ 2, 4, 6 }, { 1, 4, 7 } };

		/// ajoute les cases du plateau
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < max; j++) {
				if (i == 3)
					max = 6;
				else
					max = 3;
				tableauMomentane[1] = i + 1;
				tableauMomentane[0] = valeurs[i][j];
				caseIDCoord.put(numeroCase, tableauMomentane.clone());
				numeroCase++;
			}
		}

		/// ajoute les cases de depart du robot
		for (int i = 0; i < 9; i++) {
			tableauMomentane[1] = 0;
			tableauMomentane[0] = i;
			caseIDCoord.put(i + 25, tableauMomentane.clone());
		}

		/// ajoute les cases de depart du joueur
		for (int i = 0; i < 9; i++) {
			tableauMomentane[1] = 8;
			tableauMomentane[0] = i;
			caseIDCoord.put(i + 34, tableauMomentane.clone());
		}
	}

	public static void creationMapCaseID() {
		/// creer une map cl�/valeur
		/// la cl� est le NUMERO de la case
		/// la valeur est la COULEUR de la case
		/// 0 = rien, 1 = noir, 6 = blanc
		for (int i = 0; i < 24; i++) {
			caseID.put(i + 1, 0);
		}
	}

	/// pour entrer des cases d�s le d�part.
	public static void sautille() {
		/// partie pour tester la glisse
//		caseID.remove(1);
//		caseID.put(1, 6);
		caseID.remove(2);
		caseID.put(2, 6);
//		caseID.remove(3);
//		caseID.put(3, 6);
		caseID.remove(4);
		caseID.put(4, 6);
		caseID.remove(5);
		caseID.put(5, 6);
		caseID.remove(6);
		caseID.put(6, 6);
//		caseID.remove(7);
//		caseID.put(7, 1);
		caseID.remove(8);
		caseID.put(8, 6);
//		caseID.remove(9);
//		caseID.put(9, 1);
		caseID.remove(10);
		caseID.put(10, 6);
//		caseID.remove(11);
//		caseID.put(11, 1);
		caseID.remove(12);
		caseID.put(12, 6);
		caseID.remove(13);
		caseID.put(13, 6);
		caseID.remove(14);
		caseID.put(14, 1);
//		caseID.remove(15);
//		caseID.put(15, 6);
//		caseID.remove(16);
//		caseID.put(16, 6);
		caseID.remove(17);
		caseID.put(17, 1);
//		caseID.remove(18);
//		caseID.put(18, 1);
		caseID.remove(19);
		caseID.put(19, 1);
//		caseID.remove(20);
//		caseID.put(20,6);
		caseID.remove(21);
		caseID.put(21, 6);
//		caseID.remove(22);
//		caseID.put(22, 6);
//		caseID.remove(23);
//		caseID.put(23, 1);
//		caseID.remove(24);
//		caseID.put(24, 6);
	}
	public static void partieGlisse() {
		/// partie pour tester la glisse
		caseID.remove(1);
		caseID.put(1, 6);
		caseID.remove(2);
		caseID.put(2, 1);
		caseID.remove(3);
		caseID.put(3, 6);
		caseID.remove(4);
		caseID.put(4, 6);
		caseID.remove(5);
		caseID.put(5, 6);
		caseID.remove(6);
		caseID.put(6, 1);
		caseID.remove(7);
		caseID.put(7, 1);
//		caseID.remove(8);
//		caseID.put(8, 1);
//		caseID.remove(9);
//		caseID.put(9, 1);
		caseID.remove(10);
		caseID.put(10, 1);
		caseID.remove(11);
		caseID.put(11, 1);
//		caseID.remove(12);
//		caseID.put(12, 6);
		caseID.remove(13);
		caseID.put(13, 1);
		caseID.remove(14);
		caseID.put(14, 1);
		caseID.remove(15);
		caseID.put(15, 6);
//		caseID.remove(16);
//		caseID.put(16, 1);
		caseID.remove(17);
		caseID.put(17, 1);
//		caseID.remove(18);
//		caseID.put(18, 1);
		caseID.remove(19);
		caseID.put(19, 6);
		caseID.remove(20);
		caseID.put(20,6);
		caseID.remove(21);
		caseID.put(21, 1);
		caseID.remove(22);
		caseID.put(22, 6);
//		caseID.remove(23);
//		caseID.put(23, 1);
		caseID.remove(24);
		caseID.put(24, 6);
	}
}