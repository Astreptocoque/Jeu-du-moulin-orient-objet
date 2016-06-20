import java.io.IOException;
import lejos.hardware.Sound;

public class ModeDeJeuMethodes {

	/// liste pour vérifier si le pion est glissé au bon endroit
	/// un groupe de parenthèse représente la case de départ
	/// les nbr à l'intérieur les possibilités de déplacement
	private final static int[][] listeCases = { { 2, 10 }, { 1, 3, 5 }, { 2, 15 }, { 5, 11 }, { 2, 4, 6, 8 },
			{ 5, 14 }, { 8, 12 }, { 5, 7, 9 }, { 8, 13 }, { 1, 11, 22 }, { 4, 10, 12, 19 }, { 7, 11, 16 },
			{ 9, 14, 18 }, { 6, 13, 15, 21 }, { 3, 14, 24 }, { 12, 17 }, { 16, 18, 20 }, { 13, 17 },
			{ 11, 20 }, { 17, 19, 21, 23 }, { 14, 20 }, { 10, 23 }, { 20, 22, 24 }, { 15, 23 } };

	public static void verifieMoulin(int IDCaseTestee, Deplacements pion) throws IOException {
		int couleurAdversaire = 0; /// couleur de l'adversaire
		boolean peutManger = false;

		if (Pion.getCouleurActuelle() == Pion.couleurDominante) {
			couleurAdversaire = Pion.noir;
		} else {
			couleurAdversaire = Pion.blanc;
		}

		/// vérifie si min. une pièce adverse est disponible
		for (int i = 1; i < 25; i++) {
			if (Pion.caseID.containsKey(i)) {
				if (moulinCompareCase(i, couleurAdversaire) == false
						&& Pion.caseID.get(i) == couleurAdversaire) {
					peutManger = true;
					break;
				} else
					peutManger = false;
			}
		}

		/// si aucune pièce adverse est diponible
		/// le programme saute l'étape
		if (peutManger == true) {
			/// vérifie ensuite si un moulin est créé
			if (moulinCompareCase(IDCaseTestee, Pion.getCouleurActuelle()) == true) {
				/// si oui, le joueur choisi un pion à manger
				/// création du pion à manger
				pion.setCouleurPion(couleurAdversaire);

				/// set la case départ, avec mangePion
				pion.setCaseDepart(mangePion(couleurAdversaire, pion));

				/// attribue la case d'arrivée sur la case de la
				/// ligne de départ suivante
				if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
					pion.setCaseArrivee(ModeDeJeu.robotElimine += 1);
					/// sort le pion
					pion.deplacementPionRobot();
					/// se replace à l'origine pour le
					/// joueur
					pion.deplacementOrigine();
				} else {
					pion.setCaseArrivee(ModeDeJeu.joueurElimine -= 1);
					/// sort le pion
					pion.deplacementPionJoueur();
				}

			} else {
				if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
					/// pas de moulin, le robot se met à
					/// l'origine pour le joueur
					pion.deplacementOrigine();
				}
			}

		} else {
			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// pas de moulin possible, le robot se met à
				/// l'origine pour le joueur
				pion.deplacementOrigine();
			}
		}

	}

	private static int mangePion(int couleurAManger, Deplacements pion) throws IOException {
		int caseChoisie;
		boolean ok = false;

		/// la boucle est effectuée tant qu'un pion valide n'est pas
		/// choisi
		do {
			/// choisi la case à manger
			if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
				pion.deplacementOrigine();
				Sound.beep();
				caseChoisie = Communication.PCInputStream();
			} else {
				caseChoisie = Robot.robotJoue();
			}

			/// condition 1 : vérifie que le pion choisi ne
			/// soit pas dans un moulin
			if (moulinCompareCase(caseChoisie, couleurAManger) == false
					/// c.2 : si la case est occupée, true.
					/// c'est ce qu'on veut
					&& caseLibre(caseChoisie) == true
					/// c.3 : vérifie la couleur du pion
					&& Pion.caseID.get(caseChoisie) == couleurAManger) {
				ok = true;
				/// pour le joueur
				if (Pion.getCouleurJoueur() == Pion.getCouleurActuelle()) {
					/// envoi un message d'approbation
					Communication.PCOutputStream(3);
				}
				/// si le pion n'est pas valide
			} else {
				ok = false;
				/// pour le joueur
				if (Pion.getCouleurJoueur() == Pion.getCouleurActuelle()) {
					/// envoi un message de refus
					Communication.PCOutputStream(4);
				}
			}
		} while (ok == false);

		if (Pion.getCouleurJoueur() == Pion.getCouleurActuelle()) {
			/// soustrait le pion mangé au nbr total de pion du
			/// joueur
			Pion.enlevePionRobot();
		} else {
			/// soustrait le pion au nbr total de pion du robot
			Pion.enlevePionJoueur();
		}

		return caseChoisie;
	}

	private static boolean moulinCompareCase(int caseTestee, int couleur) {
		/// vérifie si un moulin est créé
		/// la variable couleur désigne la couleur du joueur
		boolean oui_non = false;

		switch (caseTestee) {
		case 1:
			if (Pion.caseID.get(10) == couleur && Pion.caseID.get(22) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(2) == couleur && Pion.caseID.get(3) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 2:
			if (Pion.caseID.get(3) == couleur && Pion.caseID.get(1) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(5) == couleur && Pion.caseID.get(8) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 3:
			if (Pion.caseID.get(2) == couleur && Pion.caseID.get(1) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(15) == couleur && Pion.caseID.get(24) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 4:
			if (Pion.caseID.get(5) == couleur && Pion.caseID.get(6) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(11) == couleur && Pion.caseID.get(19) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 5:
			if (Pion.caseID.get(2) == couleur && Pion.caseID.get(8) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(6) == couleur & Pion.caseID.get(4) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 6:
			if (Pion.caseID.get(4) == couleur && Pion.caseID.get(5) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(14) == couleur && Pion.caseID.get(21) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 7:
			if (Pion.caseID.get(12) == couleur && Pion.caseID.get(16) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(8) == couleur && Pion.caseID.get(9) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 8:
			if (Pion.caseID.get(7) == couleur && Pion.caseID.get(9) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(5) == couleur && Pion.caseID.get(2) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 9:
			if (Pion.caseID.get(8) == couleur && Pion.caseID.get(7) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(13) == couleur && Pion.caseID.get(18) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 10:
			if (Pion.caseID.get(1) == couleur && Pion.caseID.get(22) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(11) == couleur && Pion.caseID.get(12) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 11:
			if (Pion.caseID.get(4) == couleur && Pion.caseID.get(19) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(12) == couleur && Pion.caseID.get(10) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 12:
			if (Pion.caseID.get(11) == couleur && Pion.caseID.get(10) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(7) == couleur && Pion.caseID.get(16) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 13:
			if (Pion.caseID.get(9) == couleur && Pion.caseID.get(18) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(14) == couleur && Pion.caseID.get(15) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 14:
			if (Pion.caseID.get(6) == couleur && Pion.caseID.get(21) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(13) == couleur && Pion.caseID.get(15) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 15:
			if (Pion.caseID.get(14) == couleur && Pion.caseID.get(13) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(3) == couleur && Pion.caseID.get(24) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 16:
			if (Pion.caseID.get(17) == couleur && Pion.caseID.get(18) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(12) == couleur && Pion.caseID.get(7) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 17:
			if (Pion.caseID.get(16) == couleur && Pion.caseID.get(18) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(20) == couleur && Pion.caseID.get(23) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 18:
			if (Pion.caseID.get(17) == couleur && Pion.caseID.get(16) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(13) == couleur && Pion.caseID.get(9) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 19:
			if (Pion.caseID.get(11) == couleur && Pion.caseID.get(4) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(20) == couleur && Pion.caseID.get(21) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 20:
			if (Pion.caseID.get(19) == couleur && Pion.caseID.get(21) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(17) == couleur && Pion.caseID.get(23) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 21:
			if (Pion.caseID.get(20) == couleur && Pion.caseID.get(19) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(14) == couleur && Pion.caseID.get(6) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 22:
			if (Pion.caseID.get(10) == couleur && Pion.caseID.get(1) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(23) == couleur && Pion.caseID.get(24) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 23:
			if (Pion.caseID.get(22) == couleur && Pion.caseID.get(24) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(20) == couleur && Pion.caseID.get(17) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;
		case 24:
			if (Pion.caseID.get(23) == couleur && Pion.caseID.get(22) == couleur)
				oui_non = true;
			else if (Pion.caseID.get(15) == couleur && Pion.caseID.get(3) == couleur)
				oui_non = true;
			else
				oui_non = false;
			break;

		}

		return oui_non;
	}

	public static boolean modeGlisseVerifiePose(Pion pion, int caseArrivee) {
		boolean ok = false;
		/// prend la série de cases possibles pour le pion choisi
		int casesPossibles[] = listeCases[pion.getCaseDepart() - 1];
		/// regarde si la case choisie par le joueur est comprise dans
		/// le tableau
		for (int casePossible : casesPossibles) {
			/// si la case d'arrivée est dans les cases disponibles
			/// et
			/// qu'elle est libre
			if (caseArrivee == casePossible && caseLibre(caseArrivee) == false) {
				ok = true;
				break;
			} else {
				ok = false;
			}
		}

		return ok;
	}

	public static boolean modeGlisseVerifiePrend(int caseDepart) {
		boolean ok = false;
		/// prend la série de cases possibles pour le pion choisi
		int casesPossibles[] = listeCases[caseDepart - 1];
		/// regarde si la case choisie par le joueur peut être déplacée
		for (int casePossible : casesPossibles) {
			/// si une case est inoccupée
			if (caseLibre(casePossible) == false) {
				ok = true;
				break;
			} else {
				ok = false;
			}
		}

		return ok;

	}

	public static boolean modeGlisseVerifieDeplacementPossible() {
		/// vérifie si le joueur peut déplacer un pion, où s'il est
		/// coincé
		boolean ok = false;
		/// numero de la case testee
		int caseTestee = 1;
		/// prend chaque case l'une après l'autre
		for (int[] casesAutours : listeCases) {
			/// si la case contient un pion qui appartient au joueur
			if (Pion.caseID.get(caseTestee) == Pion.getCouleurActuelle()) {
				/// regarde si le pion est bloqué, ou non
				for (int caseAutours : casesAutours) {
					/// si elle est libre...
					if (caseLibre(caseAutours) == false) {
						/// si oui, alors le joueur
						/// n'est pas bloqué, le jeu
						/// continu
						ok = true;
						break;
					}
				}
			}
			/// ajoute 1 à caseTestee car on passe à la suivante
			caseTestee++;
		}

		return ok;
	}

	public static boolean caseLibre(int caseTestee) {
		boolean ok;

		/// regarde si la case est occupée par un pion
		/// inoccupee = 0 , occupee = 1 ou 6 (noir ou blanc)
		if (Pion.caseID.get(caseTestee) == Pion.noir || Pion.caseID.get(caseTestee) == Pion.blanc)
			ok = true;
		else
			ok = false;

		return ok;
	}

}
