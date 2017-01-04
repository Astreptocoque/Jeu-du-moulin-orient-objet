import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class TactiqueGlisse {

	public static Coups tactiqueGlisse(Plateau plateau) {
		ArrayList<Integer> pionsRobot = pionSurPlateau(plateau, Pion.getCouleurRobot());
		ArrayList<Integer> pionsJoueur = pionSurPlateau(plateau, Pion.getCouleurJoueur());
		ArrayList<Coups> listeCoupsAttenteValeur2 = verifiePionsRobotBloques(plateau, pionsRobot);
		ArrayList<Coups> listeCoupsAttenteValeur1 = verifiePionsAdversaireBloques(plateau, pionsJoueur);
		ArrayList<Coups> listeCoupsAttenteValeur3 = verifiePionBloqueMoulinAdversaire(plateau);
		ArrayList<Coups> listeCoups = new ArrayList<Coups>();

		LCD.clear();
		LCD.drawString("tactiqueGlisse", 0, 0);

		/// la liste qu'on passe en param�tre
		ArrayList<Coups> liste = listeCoups;

		/// analyse des coups
		deuxPionsRobotCaseLibre(plateau, liste);
		deuxPionsAdversaireCaseLibre(plateau, liste);
		robotMoulin(plateau, liste);
		chaquePion(plateau, liste);

		/// on m�lange la liste
		Collections.shuffle(listeCoups);
		/// et on prend le premier coup le plus bas
		Boolean affirmatif = true;
		int valeur = 0;
		int nombreTests = 0;
		Coups coupChoisi = null;
		while (affirmatif) {
			for (int i = 0; i < listeCoups.size(); i++) {
				/// s'il y a un coup de la valeur voulue
				if (listeCoups.get(i).getValeur() == valeur) {
					/// pour sortir de la boucle
					affirmatif = false;
					/// on prend le coup
					coupChoisi = listeCoups.get(i);
					///
					nombreTests += 1;
				}
			}
			/// si affirmatif = true, c'est qu'aucun coup n'a �t�
			/// trouv�
			if (affirmatif == true) {
				/// on essaye avec la valeur du dessus
				valeur += 1;
			}
			/// si le nbr Test = listeCoup, c'est qu'on a tout essay�.
			if (nombreTests == listeCoups.size()) {
				affirmatif = false;
				Random random = new Random();

			}
		}

		return coupChoisi;
	}

	public static void deuxPionsRobotCaseLibre(Plateau plateau, ArrayList<Coups> liste) {

	}

	public static void deuxPionsAdversaireCaseLibre(Plateau plateau, ArrayList<Coups> liste) {

	}

	public static void robotMoulin(Plateau plateau, ArrayList<Coups> liste) {

	}

	public static void chaquePion(Plateau plateau, ArrayList<Coups> liste) {

	}

	public static void creationCoupValeurAttente(Plateau plateau) {
		/// non en fait il faut cr�er les coup directement dans la cr�atin des listes. On
		/// regarde toute les directions que peut prendre chaque pion et on l'ajoute comme
		/// coup
	}

	/// --------------------------------------------------------------------------

	public static ArrayList<Coups> verifiePionsRobotBloques(Plateau plateau, ArrayList<Integer> pionRobot) {
		/// si le pion est bloqu�, alors ValeurDifferentsChemins doit �tre vide
		ArrayList<Integer> valeursDifferentsChemins = new ArrayList<Integer>();
		/// la liste des coups en attente
		ArrayList<Coups> listeCoupsAttenteValeur2 = new ArrayList<Coups>();

		/// on passe en revue tout les pions de l'adversaire
		for (int pion : pionRobot) {
			valeursDifferentsChemins = remonteCase(plateau, pion, Pion.getCouleurRobot(), 0, -1,
					valeursDifferentsChemins);
			/// si c'est vide, alors le pion est "bloqu�"
			if (valeursDifferentsChemins.isEmpty()) {
				/// on ajoute le coup
				Coups coup = new Coups(2, pion);
				listeCoupsAttenteValeur2.add(coup);

			}
		}

		return listeCoupsAttenteValeur2;

	}

	public static ArrayList<Coups> verifiePionsAdversaireBloques(Plateau plateau, ArrayList<Integer> pionsJoueur) {
		/// si le pion est bloqu�, alors ValeurDifferentsChemins doit �tre vide
		ArrayList<Integer> valeursDifferentsChemins = new ArrayList<Integer>();
		/// la liste des coups en attente
		ArrayList<Coups> listeCoupsAttenteValeur1 = new ArrayList<Coups>();

		/// on passe en revue tout les pions de l'adversaire
		for (int pion : pionsJoueur) {
			valeursDifferentsChemins = remonteCase(plateau, pion, Pion.getCouleurJoueur(), 0, -1,
					valeursDifferentsChemins);
			/// si c'est vide, alors le pion est "bloqu�"
			if (valeursDifferentsChemins.isEmpty()) {
				/// on ajoute le coup
				Coups coup = new Coups(1, pion);
				listeCoupsAttenteValeur1.add(coup);

			}
		}

		return listeCoupsAttenteValeur1;

	}

	public static ArrayList<Coups> verifiePionBloqueMoulinAdversaire(Plateau plateau) {
		// si deux pions adverses sont align�s avec un troisi�me robot qui bloque
		/// la liste des coups en attente
		ArrayList<Coups> listeCoupsAttenteValeur3 = new ArrayList<Coups>();

		/// parcours tous les possibilit�s de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3�me case est occup�e par l'adversaire
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {
				/// on ajoute le pion adverse
				Coups coup = new Coups(3, moulin[2]);
				listeCoupsAttenteValeur3.add(coup);
			}
			/// si la 2�me case est occup�e par l'adversaire
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// on ajoute le pion adverse
				Coups coup = new Coups(3, moulin[1]);
				listeCoupsAttenteValeur3.add(coup);
			}
			/// si la 1�re case est occup�e par l'adversaire
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// on ajoute le pion adverse
				Coups coup = new Coups(3, moulin[0]);
				listeCoupsAttenteValeur3.add(coup);
			}
		}

		return listeCoupsAttenteValeur3;

	}

	public static ArrayList<Integer> remonteCase(Plateau plateau, int caseDepart, int couleur, int valeur,
			int casePrecedente, ArrayList<Integer> valeursDifferentsChemins) {
		/// !!! r�cursivit�
		/// la couleur est celle que l'on veut faire venir sur caseDepart

		/// r�cup�re les cases adjacente � caseDepart
		ArrayList<Integer> casesAdjacentes = new ArrayList<Integer>();
		for (int element : plateau.mapCases.get(caseDepart).casesAdjacentes)
			casesAdjacentes.add(element);
		/// retire la case pr�c�demment test�e
		if (casePrecedente != -1) {
			for (int i = 0; i < casesAdjacentes.size(); i++) {
				if (casesAdjacentes.get(i) == casePrecedente)
					casesAdjacentes.remove(i);
			}
		}

		casePrecedente = caseDepart;

		/// parcours toutes les cases adjacente � la case
		for (int caseAdjacente : casesAdjacentes) {
			/// si la case est vide, recommence l'op�ration
			if (plateau.mapCases.get(caseAdjacente).pion == Pion.vide) {
				valeur += 1;
				/// caseDepart devient caseAdjacente,
				/// casePrecedente a d�j� �t� chang�e
				valeursDifferentsChemins = remonteCase(plateau, caseAdjacente, couleur, valeur,
						casePrecedente, valeursDifferentsChemins);
			}
			/// si la case est occup�e par un pion de l'adversaire
			else if (plateau.mapCases.get(caseAdjacente).pion == couleur) {
				valeur += 1;
				/// si la case est occup�e par un pion du robot, rien
				/// on ajoute la valeur du chemin effectu�
				valeursDifferentsChemins.add(valeur);

			}
			/// si la case est occup�e par un pion "ami"
			else {
				valeur = 0;
			}

		}

		/// envoi les diff�rentes valeurs
		return valeursDifferentsChemins;
	}

	public static int hasard() {
		/// prend une case au hasard sur le plateau
		Random random = new Random();
		return random.nextInt(24) + 1;
	}

	public static ArrayList<Integer> pionSurPlateau(Plateau plateau, int couleur) {
		/// r�cup�re tous les pions d'une couleur pr�sents sur le
		/// plateau
		/// liste des pions
		ArrayList<Integer> pionPlateau = new ArrayList<Integer>();

		/// parcours toutes les cases
		for (int i = 1; i < 25; i++) {
			if (plateau.mapCases.get(i).pion == couleur)
				pionPlateau.add(i);
		}

		return pionPlateau;
	}
}
