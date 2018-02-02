import java.io.IOException;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class ModeDeJeuMethodes {

	/// variable pour sortir les pions que le robot mange
	private static int robotElimine = 24;
	/// variable pour sortir les pions que le joueur mange
	private static int joueurElimine = 43;

	public static void verifieMoulin(int IDCaseTestee, Deplacements pion, Plateau plateau) throws IOException {
		int couleurAdversaire = 0; /// couleur de l'adversaire

		if (Pion.getCouleurActuelle() == Pion.blanc) {
			couleurAdversaire = Pion.noir;
		} else {
			couleurAdversaire = Pion.blanc;
		}

		/// v�rifie si un moulin est cr��
		/// v�rifie si min. une pi�ce adverse est disponible
		/// si aucune pi�ce adverse est diponible
		/// le programme saute l'�tape
		if (moulinVerifie(IDCaseTestee, Pion.getCouleurActuelle(), plateau)
				&& plateau.getNbrPionsSurLePlateau(couleurAdversaire) > 0
				&& plateau.caseLibre(couleurAdversaire)) {
			
			
			/// si oui, le joueur choisi un pion � manger
			/// cr�ation du pion � manger
			Deplacements pionAManger = new Deplacements();

			/// set la case d�part, avec mangePion
			pionAManger.setCaseDepart(mangePion(couleurAdversaire, pionAManger, plateau), plateau);

			/// attribue la case d'arriv�e sur la case de la
			/// ligne de d�part suivante
			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				pionAManger.setCaseArrivee(robotElimine += 1, plateau);
				/// sort le pion
				pionAManger.deplacementPionRobot();
				/// se replace � l'origine pour le
				/// joueur
				pionAManger.deplacementOrigine();
			} else {
				pionAManger.setCaseArrivee(joueurElimine -= 1, plateau);
				/// sort le pion
				pionAManger.deplacementPionJoueur();
			}
			/// si aucun moulin n'est cr��...
		} else {
			/// ... et si c'est le robot qui a jou�...
			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// ...il se replace � l'origine pour le
				/// joueur
				pion.deplacementOrigine();
			}
		}
	}

	private static int mangePion(int couleurAManger, Deplacements pion, Plateau plateau) throws IOException {
		int caseChoisie;
		boolean ok = false;

		/// met le modeElimination, pour manger
		plateau.modeElimination = true;
		
		LCD.clear();
		LCD.drawString("mangePion", 0, 0);
		

		/// la boucle est effectu�e tant qu'un pion valide n'est pas
		/// choisi
		do {
			/// choisi la case � manger
			if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
				/// si c'est au joueur de jouer
				pion.deplacementOrigine();
				Sound.beepSequenceUp();
				/// choisit une case
				caseChoisie = Communication.PCInputStream();
			} else {
				/// choisit une case
				Coups coup = Robot.robotJoue(plateau);
				caseChoisie = coup.getCoupCaseArrivee();
				LCD.drawString("coup : " + caseChoisie, 0, 1);
			}

			/// on v�rifie ensuite que le pion puisse �tre mang�

			/// condition 1 : v�rifie que le pion choisi ne
			/// soit pas dans un moulin
			if (moulinVerifie(caseChoisie, couleurAManger, plateau) == false
					/// c.2 : si la case est occup�e
					/// c'est ce que l'on souhaite
					&& plateau.mapCases.get(caseChoisie).pion != Pion.vide
					/// c.3 : v�rifie la couleur du pion
					/// on veut la couleur � manger
					&& plateau.mapCases.get(caseChoisie).pion == couleurAManger) {
				/// si toutes les conditions sont r�unis, alors
				/// c'est bon
				ok = true;
				/// pour le joueur
				if (Pion.getCouleurJoueur() == Pion.getCouleurActuelle()) {
					/// envoi un message d'approbation
					Communication.PCOutputStream(3);
				}
			} else {
				/// si le pion n'est pas valide
				ok = false;
				/// pour le joueur
				if (Pion.getCouleurJoueur() == Pion.getCouleurActuelle()) {
					/// envoi un message de refus
					Communication.PCOutputStream(4);
				}
			}
			/// donc tant que le pion n'est pas valide
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
		int[] moulin1 = plateau.mapCases.get(caseTestee).casesMoulins[0].clone();
		int[] moulin2 = plateau.mapCases.get(caseTestee).casesMoulins[1].clone();

		/// et on regarde s'ils font un moulins
		if (plateau.mapCases.get(moulin1[0]).pion == couleur
				&& plateau.mapCases.get(moulin1[1]).pion == couleur) {
			oui_non = true;
			/// change l'etat (dans un moulin ou pas) des cases
			plateau.mapCases.get(moulin1[0]).etatMoulin = true;
			plateau.mapCases.get(moulin1[1]).etatMoulin = true;
			plateau.mapCases.get(caseTestee).etatMoulin = true;

		} else if (plateau.mapCases.get(moulin2[0]).pion == couleur
				&& plateau.mapCases.get(moulin2[1]).pion == couleur) {
			oui_non = true;
			/// change l'etat (dans un moulin ou pas) des cases
			plateau.mapCases.get(moulin2[0]).etatMoulin = true;
			plateau.mapCases.get(moulin2[1]).etatMoulin = true;
			plateau.mapCases.get(caseTestee).etatMoulin = true;

		} else {
			oui_non = false;
		}

		if(couleur == Pion.getCouleurJoueur() && oui_non){
			plateau.setNbrMoulinsFermesJoueur();
		}else{
			plateau.setNbrMoulinsFermesRobot();
		}
		
		return oui_non;

	}

	public static boolean modeGlisseVerifiePose(Deplacements pion, int caseArrivee, Plateau plateau) {
		/// v�rifie si le joueur a le droit de poser son pion �
		/// l'endroit choisi

		boolean ok = false;
		/// prend la s�rie de cases possibles pour le pion choisi
		int casesPossibles[] = plateau.mapCases.get(pion.getCaseDepart()).getCasesAdjacentes();
		/// regarde si la case choisie par le joueur est comprise dans
		/// le tableau
		for (int casePossible : casesPossibles) {
			/// si la case d'arriv�e est dans les cases disponibles
			/// et qu'elle est libre
			if (caseArrivee == casePossible && plateau.mapCases.get(casePossible).pion == Pion.vide) {
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
			ok = plateau.mapCases.get(i).getBlocage(plateau);
			if (ok) {
				break;
			}
		}

		return ok;
	}
}
