import java.io.IOException;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class ModeDeJeuMethodes {

	/// liste pour v�rifier si le pion est gliss� au bon endroit
	/// un groupe de parenth�se repr�sente la case de d�part
	/// les nbr � l'int�rieur les possibilit�s de d�placement
	private final static int[][] listeCases = { { 2, 10 }, { 1, 3, 5 }, { 2, 15 }, { 5, 11 }, { 2, 4, 6, 8 },
			{ 5, 14 }, { 8, 12 }, { 5, 7, 9 }, { 8, 13 }, { 1, 11, 22 }, { 4, 10, 12, 19 }, { 7, 11, 16 },
			{ 9, 14, 18 }, { 6, 13, 15, 21 }, { 3, 14, 24 }, { 12, 17 }, { 16, 18, 20 }, { 13, 17 },
			{ 11, 20 }, { 17, 19, 21, 23 }, { 14, 20 }, { 10, 23 }, { 20, 22, 24 }, { 15, 23 } };

	public static void verifieMoulin(int IDCaseTestee) throws IOException {
		int couleurAdversaire = 0; /// couleur de l'adversaire
		boolean peutManger = false;

		if (Pion.getCouleurActuelle() == Pion.couleurDominante) {
			couleurAdversaire = Pion.noir;
		} else {
			couleurAdversaire = Pion.blanc;
		}

		/// v�rifie si min. une pi�ce adverse est disponible
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

		/// si aucune pi�ce adverse est diponible
		/// le programme saute l'�tape
		if (peutManger == true) {
			/// v�rifie ensuite si un moulin est cr��
			if (moulinCompareCase(IDCaseTestee, Pion.getCouleurActuelle()) == true) {
				/// si oui, le joueur choisi un pion � manger
				/// cr�ation du pion � manger
				Deplacements pion = new Deplacements(couleurAdversaire);
				/// set la case d�part, avec mangePion
				pion.setCaseDepart(mangePion(couleurAdversaire));

				/// attribue la case d'arriv�e sur la case de la
				/// ligne de d�part suivante
				if (Pion.getCouleurActuelle() == Pion.getCouleurRobot())
					pion.setCaseArrivee(ModeDeJeu.robotElimine += 1);
				else
					pion.setCaseArrivee(ModeDeJeu.joueurElimine -= 1);
				/// sort le pion
				pion.deplacementPion();

			}

		}

	}

	private static int mangePion(int couleurAManger) throws IOException {
		int caseChoisie;
		boolean ok = false;
		/// la boucle est effectu�e tant qu'un pion valide n'est pas
		/// choisi
		do {
			/// pour le joueur
			if (Pion.getCouleurJoueur() == Pion.getCouleurActuelle())
				Sound.beep();

			/// choisi la case � manger
			if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur())
				caseChoisie = Communication.PCInputStream();
			else
				caseChoisie = Robot.robotJoue();

			/// condition 1 : v�rifie que le pion choisi ne
			/// soit pas dans un moulin
			if (moulinCompareCase(caseChoisie, couleurAManger) == false
					/// c.2 : si la case est occup�e, true.
					/// c'est ce qu'on veut
					&& caseLibre(caseChoisie) == true
					/// c.3 : v�rifie la couleur du pion
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
			/// soustrait le pion mang� au nbr total de pion du
			/// joueur
			Pion.enlevePionJoueur();
		} else {
			/// soustrait le pion au nbr total de pion du robot
			Pion.enlevePionRobot();
		}

		return caseChoisie;
	}

	private static boolean moulinCompareCase(int caseTestee, int couleur) {
		/// v�rifie si un moulin est cr��
		/// la variable couleur d�signe la couleur du joueur
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

	public static boolean modeGlisseVerifie(Pion pion, int caseArrivee) {
		boolean ok = false;
		/// prend la s�rie de cases possibles pour le pion choisi
		int casesPossibles[] = listeCases[pion.getCaseDepart() - 1];
		/// regarde si la case choisie par le joueur est comprise dans
		/// le tableau
		for (int casePossible : casesPossibles) {
			/// si la case d'arriv�e est dans celle disponible et
			/// qu'elle est libre
			if (casePossible == pion.getCaseArrivee() && caseLibre(casePossible) == true)
				ok = true;
			else
				ok = false;
		}

		return ok;

	}

	public static boolean modeGlisseVerifiePosePossible(){
		return false;
		
	}
	
	public static boolean caseLibre(int caseTestee) {
		boolean ok = true;

		/// regarde si la case est occup�e par un pion
		/// inoccupee = 0 , occupee = 1 ou 6 (noir ou blanc)
		if (Pion.caseID.get(caseTestee) == Pion.noir || Pion.caseID.get(caseTestee) == Pion.blanc)
			ok = true;
		else
			ok = false;

		return ok;
	}
}
