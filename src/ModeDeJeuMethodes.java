import java.io.IOException;
import lejos.hardware.Sound;

public class ModeDeJeuMethodes {

	public static void verifieMoulin(int IDCaseTestee, Deplacements pion, Plateau plateau) throws IOException {
		int couleurAdversaire = 0; /// couleur de l'adversaire

		if (Pion.getCouleurActuelle() == Pion.couleurDominante) {
			couleurAdversaire = Pion.noir;
		} else {
			couleurAdversaire = Pion.blanc;
		}

		/// v�rifie si min. une pi�ce adverse est disponible
		/// si aucune pi�ce adverse est diponible
		/// le programme saute l'�tape
		if (plateau.getNbrPionsSurLePlateau(couleurAdversaire) > 0) {
			/// v�rifie ensuite si un moulin est cr��
			if (moulinVerifie(IDCaseTestee, Pion.getCouleurActuelle(), plateau) == true) {

				/// si oui, le joueur choisi un pion � manger
				/// cr�ation du pion � manger
				Deplacements pionAManger = new Deplacements();

				/// set la case d�part, avec mangePion
				pionAManger.setCaseDepart(mangePion(couleurAdversaire, pionAManger, plateau), plateau);

				/// attribue la case d'arriv�e sur la case de la
				/// ligne de d�part suivante
				if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
					pionAManger.setCaseArrivee(ModeDeJeu.robotElimine += 1, plateau);
					/// sort le pion
					pionAManger.deplacementPionRobot();
					/// se replace � l'origine pour le
					/// joueur
					pionAManger.deplacementOrigine();
				} else {
					pionAManger.setCaseArrivee(ModeDeJeu.joueurElimine -= 1, plateau);
					/// sort le pion
					pionAManger.deplacementPionJoueur();
				}

			} else

			{
				if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
					/// pas de moulin, le robot se met �
					/// l'origine pour le joueur
					pion.deplacementOrigine();
				}
			}

		} else {
			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// pas de moulin possible, le robot se met �
				/// l'origine pour le joueur
				pion.deplacementOrigine();
			}
		}

	}

	private static int mangePion(int couleurAManger, Deplacements pion, Plateau plateau) throws IOException {
		int caseChoisie;
		boolean ok = false;

		plateau.mode = 4;

		/// la boucle est effectu�e tant qu'un pion valide n'est pas
		/// choisi
		do {
			/// choisi la case � manger
			if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
				pion.deplacementOrigine();
				Sound.beep();
				caseChoisie = Communication.PCInputStream();
			} else {
				caseChoisie = Robot.robotJoue(plateau);
			}

			/// condition 1 : v�rifie que le pion choisi ne
			/// soit pas dans un moulin
			if (moulinVerifie(caseChoisie, couleurAManger, plateau) == false
					/// c.2 : si la case est occup�e, true.
					/// c'est ce qu'on veut
					&&plateau.tabCases[caseChoisie].pion != Pion.vide
					/// c.3 : v�rifie la couleur du pion
					&& plateau.tabCases[caseChoisie].pion == couleurAManger) {
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
			/// soustrait le pion mang� au nbr total de pion du
			/// joueur
			plateau.enlevePionRobot();
		} else {
			/// soustrait le pion au nbr total de pion du robot
			plateau.enlevePionJoueur();
		}

		return caseChoisie;
	}

	public static boolean moulinVerifie(int caseTestee, int couleur, Plateau plateau) {
		/// v�rifie si un moulin est cr��
		/// la variable couleur d�signe la couleur du joueur
		boolean oui_non = false;

		/// on r�cup�re les moulins possibles avec la case test�e
		int[] moulin1 = plateau.tabCases[caseTestee].casesMoulins[0].clone();
		int[] moulin2 = plateau.tabCases[caseTestee].casesMoulins[1].clone();

		/// et on regarde s'ils font un moulins
		if (plateau.tabCases[moulin1[0]].pion == couleur && plateau.tabCases[moulin1[1]].pion == couleur) {
			oui_non = true;
		} else if (plateau.tabCases[moulin2[0]].pion == couleur
				&& plateau.tabCases[moulin2[1]].pion == couleur) {
			oui_non = true;
		} else {
			oui_non = false;
		}

		return oui_non;

	}

	public static boolean modeGlisseVerifiePose(Deplacements pion, int caseArrivee, Plateau plateau) {
		/// v�rifie si le joueur a le droit de poser son pion �
		/// l'endroit choisi

		boolean ok = false;
		/// prend la s�rie de cases possibles pour le pion choisi
		int casesPossibles[] = plateau.tabCases[pion.getCaseDepart()].getCasesAdjacentes();
		/// regarde si la case choisie par le joueur est comprise dans
		/// le tableau
		for (int casePossible : casesPossibles) {
			/// si la case d'arriv�e est dans les cases disponibles
			/// et qu'elle est libre
			if (caseArrivee == casePossible && plateau.tabCases[casePossible].pion != Pion.vide) {
				ok = true;
				break;
			} else {
				ok = false;
			}
		}

		return ok;
	}

	public static boolean modeGlisseVerifieDeplacementPossible(Plateau plateau) {
		/// v�rifie si le joueur peut d�placer un pion, o� s'il est
		/// coinc�
		boolean ok = false;

		/// test chaque case, les unes apr�s les autres
		for (int i = 1; i < 25; i++) {
			ok = plateau.tabCases[i].getBlocage(plateau);
			if (ok) {
				break;
			}
		}

		return ok;
	}
}
