import java.util.HashMap;

public class Plateau {
	/// contient les informations du plateau comme le nombre de moulins
	/// ouverts/ferm�s de chaque joueur, etc...

	/// contient les cases du plateau
	HashMap<Integer, Cases> mapCases;
	/// nbr de pions restant pour chaque joueur
	public int nbrPionsJoueur;
	public int nbrPionsRobot;
	/// variable pour d�finir le mode de jeu. 1 = pose, 2 = glisse, 3 = saut
	public int mode;
	public boolean modeElimination;

	/// constructeur
	public Plateau() {
		/// ajoute les cases de d�part dans tabCases
		ajouteCases();

		/// ajoute les pions
		this.nbrPionsJoueur = 9;
		this.nbrPionsRobot = 9;

		/// r�gle le mode. Au d�but il est sur 1:
		this.mode = 1;
		this.modeElimination = false;

	}

	/// pour les moulins
	public void setNbrMoulinsFermesJoueur() {
	}

	public void setNbrMoulinsFermesRobot() {
	}

	/// pour le nbr total de pion
	/// g�re le total des pions
	public void enlevePionJoueur() {
		this.nbrPionsJoueur = this.nbrPionsJoueur - 1;
	}

	public void enlevePionRobot() {
		this.nbrPionsRobot = this.nbrPionsRobot - 1;
	}

	public int getNbrPionsSurLePlateau(int couleur) {
		int nbrPions = 0;
		for (int i = 1; i < 25; i++) {
			if (this.mapCases.get(i).pion == couleur) {
				nbrPions++;
			}
		}
		return nbrPions;
	}
	
	public boolean caseLibre(int couleur){
		/// si une case est libre, etat est true
		boolean etat = false;
		/// passe en revue toutes les cases
		for(int i = 1; i < 25; i ++){
			/// si une case est libre...
			if(this.mapCases.get(i).etatMoulin == false  && this.mapCases.get(i).pion == couleur){
				etat = true; 
				break;
			}
		}
		
		return etat;
	}

	/// pour ajouter les cases, fait partie du constructeur
	public void ajouteCases() {
		/// pour attribuer les cases dans le constructeur
		int numeroCase = 1;
		/// valeurs pour cr�er le couple de coord. x/y des cases
		int[][] valeurs = { { 7, 4, 1 }, { 6, 4, 2 }, { 5, 4, 3 }, { 7, 6, 5, 3, 2, 1 }, { 5, 4, 3 },
				{ 6, 4, 2 }, { 7, 4, 1 } };

		/// cr�e la map qui accueillera toutes les cases pour tout le
		/// jeu
		this.mapCases = new HashMap<Integer, Cases>();

		/// ajoute les 24 cases
		for (int i = 0; i < valeurs.length; i++) {
			for (int j = 0; j < valeurs[i].length; j++) {
				/// cr�e la case et lui attribue son num�ro
				Cases cases = new Cases(numeroCase);
				/// attribue les coordonn�es � la case
				cases.coordX = valeurs[i][j];
				cases.coordY = i + 1;
				/// ajoute la case au tableau des cases
				this.mapCases.put(numeroCase, cases);
				numeroCase++;
			}
		}
		/// ajoute les cases de depart du robot
		for (int i = 0; i < 9; i++) {
			Cases cases = new Cases(numeroCase);
			cases.coordX = i;
			cases.coordY = 0;
			this.mapCases.put(numeroCase, cases);
			numeroCase++;
		}
		/// ajoute les cases de depart du joueur
		for (int i = 0; i < 9; i++) {
			Cases cases = new Cases(numeroCase);
			cases.coordX = i;
			cases.coordY = 8;
			this.mapCases.put(numeroCase, cases);
			numeroCase++;

		}
	}

}
