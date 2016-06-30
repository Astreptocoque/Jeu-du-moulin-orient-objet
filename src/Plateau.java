import java.util.HashMap;
import java.util.Map;

public class Plateau {
	/// contient les informations du plateau comme le nombre de moulins
	/// ouverts/ferm�s de chaque joueur, etc...

	/// pour attribuer les cases dans le constructeur
	private static int numeroCase = 1;

	/// pour l'objet plateau
	int nbrMoulinsOuvertsJoueur;
	int nbrMoulinsFermesJoueur;
	int nbrMoulinsOuvertsRobot;
	int nbrMoulinsFermesRobot;
	Cases[] tabCases;
	/// nbr de pions restant pour chaque joueur
	int nbrPionsJoueur;
	int nbrPionsRobot;
	/// variable pour d�finir le mode de jeu. 1 = pose, 2 = glisse, 3 = saut
	int mode;

	/// constructeur
	public Plateau() {
		/// g�re les moulins
		this.nbrMoulinsOuvertsJoueur = 0;
		this.nbrMoulinsOuvertsRobot = 0;
		this.nbrMoulinsFermesJoueur = 0;
		this.nbrMoulinsFermesRobot = 0;

		/// ajoute les cases de d�part dans tabCases
		ajouteCases();
		
		/// ajoute les pions
		this.nbrPionsJoueur = 9;
		this.nbrPionsRobot = 9;

		/// r�gle le mode. Au d�but il est sur 1:
		this.mode = 1;

	}

	/// pour les moulins
	public void setNbrMoulinsOuvertsJoueur(int nombre) {
		this.nbrMoulinsOuvertsJoueur = this.nbrMoulinsOuvertsJoueur + nombre;
	}

	public void setNbrMoulinsFermesJoueur(int nombre) {
		this.nbrMoulinsFermesJoueur = this.nbrMoulinsFermesJoueur + nombre;
	}

	public void setNbrMoulinsOuvertsRobot(int nombre) {
		this.nbrMoulinsOuvertsRobot = this.nbrMoulinsOuvertsRobot + nombre;
	}

	public void setNbrMoulinsFermesRobot(int nombre) {
		this.nbrMoulinsFermesRobot = this.nbrMoulinsFermesRobot + nombre;
	}

	/// pour le nbr total de pion
	/// g�re le total des pions
	public void enlevePionJoueur() {
		this.nbrPionsJoueur = this.nbrPionsJoueur - 1;

	}

	public void enlevePionRobot() {
		this.nbrPionsRobot = this.nbrPionsRobot - 1;

	}

	public int getNbrPionsJoueur() {
		return this.nbrPionsJoueur;
	}

	public int getNbrPionsRobot() {
		return this.nbrPionsRobot;
	}

	/// g�re le mode
	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return this.mode;
	}

	/// pour ajouter les cases, fait partie du constructeur
	public void ajouteCases() {
		/// valeurs pour cr�er le couple de coord. x/y des cases
		int[][] valeurs = { { 7, 4, 1 }, { 6, 4, 2 }, { 5, 4, 3 }, { 7, 6, 5, 3, 2, 1 }, { 5, 4, 3 },
				{ 6, 4, 2 }, { 7, 4, 1 } };
		
		/// cr�e le tableau qui accueillera toutes les cases pour tout le jeu
		this.tabCases = new Cases[42];

		/// ajoute les 24 cases
		for (int i = 0; i < valeurs.length; i++) {
			for (int j = 0; j < valeurs[i].length; j++) {
				/// cr�e la case et lui attribue son num�ro
				Cases cases = new Cases(numeroCase);
				numeroCase++;
				/// attribue les coordonn�es � la case
				cases.coordX = valeurs[i][j];
				cases.coordY = i + 1;
				/// ajoute la case au tableau des cases
				this.tabCases[i] = cases;
			}
		}
		/// ajoute les cases de depart du robot
		for (int i = 25; i < 34; i++) {
			Cases cases = new Cases(i);
			cases.coordX = i;
			cases.coordY = 0;
			this.tabCases[i] = cases;
		}
		/// ajoute les cases de depart du joueur
		for (int i = 34; i < 43; i++) {
			Cases cases = new Cases(i);
			cases.coordX = i;
			cases.coordY = 8;
			this.tabCases[i] = cases;
		}
	}

}
