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
		/// premi�re partie, les joueurs posent leurs pions.
		modePose(plateau);

		while (!fin) {

			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// si le nombre de poins est sup�rieur � 3,
				/// glissements
				if (plateau.nbrPionsRobot > 3)
					modeGlisse(plateau);
				/// s'il est �gal � 3, sauts
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
		int iDepartRobot = 25; /// d�fini le n� de la case d�part du
					/// robot. Doit �tre � 25
		int iDepartJoueur = 42; /// d�fini le n� de la case d�part du
					/// joueur. Doit �tre � 42
		

		for (int i = 0; i < 2; i++) {
			/// r�glage du mode en tant que pose (1)
			plateau.mode = 1;
			
			/// buzz pour annoncer au joueur qu'il peut joueur (si
			/// c'est son tour)
			if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
				Sound.beep();
			}
			/// cr�ation d'un d�placement
			Deplacements pion = new Deplacements();

			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// case d�part, avance � chaque tour de 1 dans
				/// la ligne de d�part
				pion.setCaseDepart(iDepartRobot, plateau);
				/// choisi une case libre et attribue case
				/// d'arriv�e
				pion.setCaseArrivee(robotPose(pion, plateau), plateau);
				LCD.clear();
				LCD.drawString("Arr. " + pion.getCaseArrivee(), 0, 0);
				LCD.drawString("Dep." + pion.getCaseDepart(), 0, 1);
				pion.deplacementPionRobot();
				/// incr�mente 1 dans la ligne de d�part des
				/// pions
				iDepartRobot++;

			} else {
				/// case d�part, avance � chaque tour de 1 dans
				/// la ligne de d�part
				pion.setCaseDepart(iDepartJoueur, plateau);
				/// choisi une case libre, et attibue la case
				/// d'arriv�e
				pion.setCaseArrivee(joueurPose(pion, plateau), plateau);

				// pion.deplacementOrigine();
				pion.deplacementPionJoueur();
				/// incr�mente 1 dans la ligne de d�part des
				/// pions
				iDepartJoueur--;
			}

			LCD.drawInt(pion.getCaseArrivee(), 0, 4);
			
			/// v�rifie si un moulin est effectu�, et joue en
			/// cons�quence
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
		/// r�glage du mode en tant que glisse (2)
		plateau.mode = 2;

		/// cr�ation d'un d�placement
		Deplacements pion = new Deplacements();

		/// s�l�ctionne le pion � bouger et la case d'arriv�e
		if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
			/// v�rifie que le robot puisse d�placer un pion
			if (ModeDeJeuMethodes.modeGlisseVerifieDeplacementPossible(plateau) == true) {
				/// si oui, fait le n�cessaire pour en d�placer
				/// un
				pion.setCaseDepart(robotPrend(plateau), plateau);
				pion.setCaseArrivee(robotPose(pion, plateau), plateau);
				pion.deplacementPionRobot();
			} else {
				/// si non, la partie est finie
				modeFin(pion);
			}

		} else {
			/// v�rifie que le robot puisse d�placer un pion
			if (ModeDeJeuMethodes.modeGlisseVerifieDeplacementPossible(plateau) == true) {
				/// si oui, fait le n�cessaire pour en d�placer
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
		/// v�rifie si un moulin est cr��
		ModeDeJeuMethodes.verifieMoulin(pion.getCaseArrivee(), pion, plateau);
	}

	public static void modeSaut(Plateau plateau) throws IOException {
		/// r�glage du mode en tant que saut (3)
		plateau.mode = 3;

		/// cr�ation d'un d�placement
		Deplacements pion = new Deplacements();

		/// s�l�ctionne le pion � bouger et la case d'arriv�e
		if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
			/// choisi un pion � d�placer
			pion.setCaseDepart(robotPrend(plateau), plateau);
			pion.setCaseArrivee(robotPose(pion, plateau), plateau);
			pion.deplacementPionRobot();

		} else {
			/// choisi un pion � d�placer
			pion.setCaseDepart(joueurPrend(plateau), plateau);
			pion.setCaseArrivee(joueurPose(pion, plateau), plateau);
			pion.deplacementOrigine();
			pion.deplacementPionJoueur();

		}
		/// v�rifie si un moulin est cr��
		ModeDeJeuMethodes.verifieMoulin(pion.getCaseArrivee(), pion, plateau);
	}

	public static void modeFin(Deplacements pion) throws IOException {
		/// ram�ne le robot � l'origine
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

			/// v�rifie que la case choisie est possible
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
				/// si un moulin est cr��, le joueur est averti
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
			/// v�rifie que la case choisie est possible
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
				/// v�rifie que la case choisie est de la bonne
				/// couleur et qu'elle peut �tre d�plac�e
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
				/// v�rifie que la case choisie est de la bonne couleur
				/// et qu'elle peut �tre d�plac�e
				if (plateau.mapCases.get(caseChoisie).pion == Pion.getCouleurRobot()
						&& plateau.mapCases.get(caseChoisie).getBlocage(plateau) == false) {
					ok = true;
				} else {
					ok = false;
				}
			} else if (plateau.mode == 3) { /// saut
				/// v�rifie que la case peut �tre choisie
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
