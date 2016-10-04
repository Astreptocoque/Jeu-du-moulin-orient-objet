import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class ModeDeJeu {

	public static void joue(Plateau plateau) throws IOException {
		/// variable pour sortir du jeu s'il un joueur fini
		boolean fin = false;
		/// blanc commence
		Pion.setCouleurActuelle(Pion.couleurDominante);
		/// première partie, les joueurs posent leurs pions.
		modePose(plateau);

		while (!fin) {

			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// si le nombre de poins est supérieur à 3,
				/// glissements
				if (plateau.nbrPionsRobot > 3)
					modeGlisse(plateau);
				/// s'il est égal à 3, sauts
				else if (plateau.nbrPionsRobot == 3)
					modeSaut(plateau);
				/// sinon, c'est la fin du jeu
				else
					fin = true;

			} else {
				if (plateau.nbrPionsJoueur > 3)
					modeGlisse(plateau);
				else if (plateau.nbrPionsJoueur == 3)
					modeSaut(plateau);
				else
					fin = true;
			}

			/// effectue le changement de joueur
			Pion.setCouleurActuelle((Pion.getCouleurActuelle() == Pion.blanc ? Pion.noir : Pion.blanc));
			/// buzz pour annoncer au joueur qu'il peut joueur (si
			/// c'est son tour)
			if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
				Sound.beep();
			}
		}
		/// pour tout ce qui concerne la fin de la partie
		Deplacements d = new Deplacements();
		modeFin(d);
	}

	public static void modePose(Plateau plateau) throws IOException {
		int iDepartRobot = 25; /// défini le n° de la case départ du
					/// robot. Doit être à 25
		int iDepartJoueur = 42; /// défini le n° de la case départ du
					/// joueur. Doit être à 42
		

		for (int i = 0; i < 2; i++) {
			/// réglage du mode en tant que pose (1)
			plateau.mode = 1;
			
			/// buzz pour annoncer au joueur qu'il peut joueur (si
			/// c'est son tour)
			if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
				Sound.beep();
			}
			/// création d'un déplacement
			Deplacements pion = new Deplacements();

			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// case départ, avance à chaque tour de 1 dans
				/// la ligne de départ
				pion.setCaseDepart(iDepartRobot, plateau);
				/// choisi une case libre et attribue case
				/// d'arrivée
				pion.setCaseArrivee(robotPose(pion, plateau), plateau);
				LCD.clear();
				LCD.drawString("Arr. " + pion.getCaseArrivee(), 0, 0);
				LCD.drawString("Dep." + pion.getCaseDepart(), 0, 1);
				pion.deplacementPionRobot();
				/// incrémente 1 dans la ligne de départ des
				/// pions
				iDepartRobot++;

			} else {
				/// case départ, avance à chaque tour de 1 dans
				/// la ligne de départ
				pion.setCaseDepart(iDepartJoueur, plateau);
				/// choisi une case libre, et attibue la case
				/// d'arrivée
				pion.setCaseArrivee(joueurPose(pion, plateau), plateau);

				// pion.deplacementOrigine();
				pion.deplacementPionJoueur();
				/// incrémente 1 dans la ligne de départ des
				/// pions
				iDepartJoueur--;
			}

			LCD.drawInt(pion.getCaseArrivee(), 0, 4);
			
			/// vérifie si un moulin est effectué, et joue en
			/// conséquence
			ModeDeJeuMethodes.verifieMoulin(pion.getCaseArrivee(), pion, plateau);
			/// change la couleur pour donner la main
			Pion.setCouleurActuelle((Pion.getCouleurActuelle() == Pion.blanc ? Pion.noir : Pion.blanc));
			
			String[] verification = new String[24];
			for(int test = 1; test < 25;test++){
				verification[test -1 ] = Integer.toString(test) + " : " +  Integer.toString(plateau.mapCases.get(test).pion);
			}
			int rien = verification.length;
		}

	}

	public static void modeGlisse(Plateau plateau) throws IOException {
		String[] verification = new String[24];
		for(int test = 1; test < 25;test++){
			verification[test -1 ] = Integer.toString(test) + " : " +  Integer.toString(plateau.mapCases.get(test).pion);
		}
		int rien = verification.length;
		/// réglage du mode en tant que glisse (2)
		plateau.mode = 2;

		/// création d'un déplacement
		Deplacements pion = new Deplacements();

		/// séléctionne le pion à bouger et la case d'arrivée
		if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
			/// vérifie que le robot puisse déplacer un pion
			if (ModeDeJeuMethodes.modeGlisseVerifieDeplacementPossible(plateau) == true) {
				/// si oui, fait le nécessaire pour en déplacer
				/// un
				pion.setCaseDepart(robotPrend(plateau), plateau);
				pion.setCaseArrivee(robotPose(pion, plateau), plateau);
				pion.deplacementPionRobot();
			} else {
				/// si non, la partie est finie
				modeFin(pion);
			}

		} else {
			/// vérifie que le robot puisse déplacer un pion
			if (ModeDeJeuMethodes.modeGlisseVerifieDeplacementPossible(plateau) == true) {
				/// si oui, fait le nécessaire pour en déplacer
				/// un
				pion.setCaseDepart(joueurPrend(plateau), plateau);
				pion.setCaseArrivee(joueurPose(pion, plateau), plateau);
				pion.deplacementOrigine();
				pion.deplacementPionJoueur();
			} else {
				/// si non, la partie est finie
				modeFin(pion);
			}
		}
		/// vérifie si un moulin est créé
		ModeDeJeuMethodes.verifieMoulin(pion.getCaseArrivee(), pion, plateau);
	}

	public static void modeSaut(Plateau plateau) throws IOException {
		/// réglage du mode en tant que saut (3)
		plateau.mode = 3;

		/// création d'un déplacement
		Deplacements pion = new Deplacements();

		/// séléctionne le pion à bouger et la case d'arrivée
		if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
			/// choisi un pion à déplacer
			pion.setCaseDepart(robotPrend(plateau), plateau);
			pion.setCaseArrivee(robotPose(pion, plateau), plateau);
			pion.deplacementPionRobot();

		} else {
			/// choisi un pion à déplacer
			pion.setCaseDepart(joueurPrend(plateau), plateau);
			pion.setCaseArrivee(joueurPose(pion, plateau), plateau);
			pion.deplacementOrigine();
			pion.deplacementPionJoueur();

		}
		/// vérifie si un moulin est créé
		ModeDeJeuMethodes.verifieMoulin(pion.getCaseArrivee(), pion, plateau);
	}

	public static void modeFin(Deplacements pion) throws IOException {
		/// ramène le robot à l'origine
		pion.deplacementOrigine();

		Sound.beep();
		Sound.beep();
		Sound.beep();
		Reglages.reglagesFin();
		System.exit(0);
	}

	/// ******* tour des joueurs ************
	public static int joueurPose(Deplacements pion, Plateau plateau) throws IOException {
		boolean ok = true;
		int caseChoisie;
		do {

			caseChoisie = Communication.PCInputStream();

			/// vérifie que la case choisie est possible
			if (plateau.mode == 1 || plateau.mode == 3) {
				if (plateau.mapCases.get(caseChoisie).pion == Pion.vide) {
					ok = false;
				}
			} else if (plateau.mode == 2) { /// glisse
				if (ModeDeJeuMethodes.modeGlisseVerifiePose(pion, caseChoisie, plateau) == true) {
					ok = false;
				}
			}

			/// renvoie un message d'acceptation ou de rejet
			if (!ok) {
				/// si un moulin est créé, le joueur est averti
				if (ModeDeJeuMethodes.moulinVerifie(caseChoisie, Pion.getCouleurActuelle(), plateau)) {
					Communication.PCOutputStream(8);
				} else {
					Communication.PCOutputStream(2);
				}
			} else {
				/// en fonction du mode
				if (plateau.mode == 1)
					Communication.PCOutputStream(1);
				if (plateau.mode == 2)
					Communication.PCOutputStream(7);
			}
		} while (ok);
		return caseChoisie;

	}

	public static int robotPose(Deplacements pion, Plateau plateau) {
		boolean ok = true;
		int caseChoisie;
		do {
			caseChoisie = Robot.robotJoue(plateau);
			/// vérifie que la case choisie est possible
			if (plateau.mode == 1 || plateau.mode == 3) {
				/// si la case est libre
				if (plateau.mapCases.get(caseChoisie).pion == Pion.vide) {
					ok = false;
				}
			} else if (plateau.mode == 2) { /// glisse
				if (ModeDeJeuMethodes.modeGlisseVerifiePose(pion, caseChoisie, plateau)) {
					ok = false;
				}
			}
		} while (ok);
		return caseChoisie;
	}

	public static int joueurPrend(Plateau plateau) throws IOException {
		boolean ok = false;
		int caseChoisie;
		do {

			caseChoisie = Communication.PCInputStream();

			if (plateau.mode == 2) { /// glisse
				/// vérifie que la case choisie est de la bonne
				/// couleur et qu'elle peut être déplacée
				if (plateau.mapCases.get(caseChoisie).pion == Pion.getCouleurJoueur()
						&& plateau.mapCases.get(caseChoisie).getBlocage(plateau) == false) {
					ok = true;
				} else {
					ok = false;
				}
			} else if (plateau.mode == 3) { /// saut
				if (plateau.mapCases.get(caseChoisie).pion == Pion.getCouleurJoueur()) {
					ok = true;
				} else {
					ok = false;
				}
			}
			/// renvoie un message de d'acceptation ou de rejet
			if (ok) {
				Communication.PCOutputStream(6);
			} else {
				Communication.PCOutputStream(5);
			}

		} while (!ok);
		return caseChoisie;
	}

	public static int robotPrend(Plateau plateau) {
		boolean ok = false;
		int caseChoisie;
		do {
			caseChoisie = Robot.robotJoue(plateau);

			
			if (plateau.mode == 2) { /// glisse
				/// vérifie que la case choisie est de la bonne couleur
				/// et qu'elle peut être déplacée
				if (plateau.mapCases.get(caseChoisie).pion == Pion.getCouleurRobot()
						&& plateau.mapCases.get(caseChoisie).getBlocage(plateau) == false) {
					ok = true;
				} else {
					ok = false;
				}
			} else if (plateau.mode == 3) { /// saut
				/// vérifie que la case peut être choisie
				if (plateau.mapCases.get(caseChoisie).pion == Pion.getCouleurRobot()) {
					ok = true;
				} else {
					ok = false;
				}
			}
		} while (!ok);

		return caseChoisie;
	}

}
