import java.io.IOException;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class ModeDeJeuMethodes {
	
	public static void verifieMoulin(int IDCaseTestee) throws IOException {
		int couleurAdversaire = 0; /// couleur de l'adversaire
		boolean peutManger = false;

		if (Pion.getCouleurActuelle() == Pion.couleurDominante) {
			Pion.setCouleurActuelle(Pion.blanc);
			couleurAdversaire = Pion.noir;
		} else {
			Pion.setCouleurActuelle(Pion.noir);
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
		/// le programme saut l'étape
		if (peutManger == true) {
			/// si un moulin est créé
			if (moulinCompareCase(IDCaseTestee, Pion.getCouleurActuelle()) == true) {

				/// si oui, le joueur choisi un pion à manger
				int caseChoisie;

				// do {
				caseChoisie = mangePion(couleurAdversaire);
				
				/// création du pion à manger
				Deplacements pion = new Deplacements(couleurAdversaire);
				
				
				/// vérifie si le pion peut être mangé
				// } while (moulinCompareCase(caseChoisie,
				// couleurAdversaire) == true &&
				// caseLibre(caseChoisie) == true &&
				// Outils.caseID.get(caseChoisie) ==
				// couleurAdversaire);

				/// prend la pièce choisie et la sort sur la
				/// ligne de
				/// départ

				pion.setCaseDepart(caseChoisie);
				/// sort le pion sur la case de départ suivante
				if (Pion.getCouleurActuelle() == Pion.getCouleurRobot())
					pion.setCaseArrivee(ModeDeJeu.robotElimine += 1);
				else
					pion.setCaseArrivee(ModeDeJeu.joueurElimine -= 1);

				pion.deplacementPion();
				
			} else {
				LCD.clear(6);
				LCD.drawString("aucune pieces", 0, 6);
			}
		}

	}

	private static int mangePion(int couleurAManger) throws IOException {
		int caseChoisie;
		boolean ok = false;
		/// défini si c'est le robot ou le joueur qui joue
		if (couleurAManger == Pion.getCouleurJoueur()) {
			do {
				LCD.drawString("robot mange", 0, 5);
				caseChoisie = ModeDeJeu.mange();
				/// vérifie que la case choisie est occupée par
				/// un pion adverse
				if (moulinCompareCase(caseChoisie, couleurAManger) == false && ModeDeJeu.caseLibre(caseChoisie) == true && Pion.caseID.get(caseChoisie) == couleurAManger)
					ok = true;
				else{
					ok = false;
					Communication.PCOutputStream(4);
				}
			} while (ok == false);
			Pion.enlevePionJoueur();

		} else {
			/// en attendant, c'est au hasard
			do {
				Sound.beep();

				LCD.drawString("joueur mange", 0, 5);
				caseChoisie =ModeDeJeu.mange();
				/// vérifie que la case choisie est occupée par
				/// un pion adverse
				if (moulinCompareCase(caseChoisie, couleurAManger) == false
						&& ModeDeJeu.caseLibre(caseChoisie) == true
						&& Pion.caseID.get(caseChoisie) == couleurAManger) {
					ok = true;
					Communication.PCOutputStream(3);
				} else {
					ok = false;
					Communication.PCOutputStream(4);
				}

			} while (ok == false);
			Pion.enlevePionRobot();;
		}

		return caseChoisie;
	}

	private static boolean moulinCompareCase(int caseDeposee, int couleur) {
		/// vérifie si un moulin est créé
		/// la variable couleur désigne la couleur du joueur
		boolean oui_non = false;

		switch (caseDeposee) {
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
}
