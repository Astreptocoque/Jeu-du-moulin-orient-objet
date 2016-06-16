import java.util.HashMap;
import java.util.Map;

public class Pion {

	/// variables générales à tout le programme
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

	/// variables pour les objets pions
	int couleur; /// couleur du pion
	int numeroCaseDepart; /// N° de la case départ du pion
	int numeroCaseArrivee; /// N° de la case arrivée du pion
	int[] coordCaseDepart; /// Case départ en coordonnées
	int[] coordCaseArrivee; /// Case arrivée en coordonnées

	/// map contenant les coordonnées de chaques cases. Origine en haut à
	/// droite.
	/// clé : numéro de la case. Valeur : coordonées
	public static Map<Integer, int[]> caseIDCoord = new HashMap<Integer, int[]>();
	/// map contenant les cases en clé et la couleur des pions en valeur
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
		/// le if est là pour empecher une erreur lors de la pose
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
		/// le if est là pour empecher une erreur lors de la pose
		/// initiale des pions
		if (numeroCase > 0 && numeroCase < 25) {
			caseID.remove(numeroCase);
			caseID.put(numeroCase, this.couleur);
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

	/// gère le total des pions
	public static void enlevePionJoueur() {
		nbrPionsJoueur = nbrPionsJoueur - 1;
	}

	public static void enlevePionRobot() {
		nbrPionsRobot = nbrPionsRobot - 1;
	}

	public static int getNbrPionsJoueur() {
		return nbrPionsJoueur;
	}

	public static int getNbrPionsRobot() {
		return nbrPionsRobot;
	}

	/// maps
	public static void creationMapCaseIDCoord() {
		/// créé une map de clé/valeurs
		/// les clés sont le NUMERO de la case
		/// les valeurs sont COORDONNEES de chaque case
		int numeroCase = 1;
		int max = 3;
		int[] tableauMomentane = new int[2];

		/// valeurs pour créer le couple de coord. x/y des cases
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
		/// creer une map clé/valeur
		/// la clé est le NUMERO de la case
		/// la valeur est la COULEUR de la case
		/// 0 = rien, 1 = noir, 6 = blanc
		for (int i = 0; i < 24; i++) {
			caseID.put(i + 1, 0);
		}
	}

	/// pour entrer des cases dès le départ.
	public static void tests() {
		caseID.remove(9);
		caseID.put(9, 6);
		caseID.remove(8);
		caseID.put(8, 6);
		caseID.remove(14);
		caseID.put(14, 1);
		// caseID.remove(2);
		// caseID.put(2, 1);
		// caseID.remove(3);
		// caseID.put(3, 1);
		// caseID.remove(8);
		// caseID.put(8, 1);
		// caseID.replace(7, 1);
		// caseID.replace(8, 1);
		// caseID.replace(14, 6);
	}
}