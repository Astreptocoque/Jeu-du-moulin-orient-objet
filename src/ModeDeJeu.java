import java.io.IOException;

import lejos.hardware.Sound;

public class ModeDeJeu {
	/// variable pour sortir les pions que le robot mange
	public static int robotElimine = 24;
	/// variable pour sortir les pions que le joueur mange
	public static int joueurElimine = 42;

	/// ******* mode de jeu **********

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
		Deplacements pion = new Deplacements();
		modeFin(pion);
	}

	public static void modePose(Plateau plateau) throws IOException {
		int iDepartRobot = 0; /// d�fini le n� de la case d�part du
					/// robot. Doit �tre � z�ro
		int iDepartJoueur = 0; /// d�fini le n� de la case d�part du
					/// joueur. Doit �tre � z�ro
		plateau.mode = 1;

		for (int i = 0; i < 18; i++) {

			/// buzz pour annoncer au joueur qu'il peut joueur (si
			/// c'est son
			/// tour)
			if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
				Sound.beep();
			}
			/// cr�ation d'un pion avec attribution de la couleur
			Deplacements pion = new Deplacements();
			

			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// case d�part, avance � chaque tour de 1 dans
				/// la ligne de d�part
				pion.setCaseDepart(iDepartRobot + 24, plateau);
				/// choisi une case libre et attribue case
				/// d'arriv�e
				pion.setCaseArrivee(robotPose(pion, plateau), plateau);
				pion.deplacementPionRobot();
				/// incr�mente 1 dans la ligne de d�part des
				/// pions
				iDepartRobot++;

			} else {
				/// case d�part, avance � chaque tour de 1 dans
				/// la ligne de d�part
				pion.setCaseDepart(41 - iDepartJoueur,plateau);
				/// choisi une case libre, et attibue la case
				/// d'arriv�e
				pion.setCaseArrivee(joueurPose(pion, plateau),plateau);

				// pion.deplacementOrigine();
				pion.deplacementPionJoueur();
				/// incr�mente 1 dans la ligne de d�part des
				/// pions
				iDepartJoueur++;
			}

			/// v�rifie si un moulin est effectu�, et joue en
			/// cons�quence
			ModeDeJeuMethodes.verifieMoulin(pion.getCaseArrivee(), pion, plateau);
			/// change la couleur pour donner la main
			Pion.setCouleurActuelle((Pion.getCouleurActuelle() == Pion.blanc ? Pion.noir : Pion.blanc));

		}

	}

	public static void modeGlisse(Plateau plateau) throws IOException {
		/// r�glage du mode
		plateau.mode = 2;

		/// cr�ation d'un pion avec attribution de la couleur
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
		/// r�glage du mode
		plateau.mode = 3;

		/// cr�ation d'un pion avec attribution de la couleur
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
		Reglages outils = new Reglages();
		outils.reglagesFin();
		System.exit(0);
	}

	/// ******* tour des joueurs ************
	public static int joueurPose(Deplacements pion, Plateau plateau) throws IOException {
		boolean ok = true;
		int caseChoisie;
		do {

			caseChoisie = Communication.PCInputStream();

			/// v�rifie que la case choisie est possible
			if (plateau.mode == 1 || plateau.mode == 3) { /// pose ou saut
				if(plateau.tabCases[caseChoisie].pion == Pion.vide){
					ok = false;
				}
			} else if (plateau.mode == 2) { /// glisse
				if (ModeDeJeuMethodes.modeGlisseVerifiePose(pion, caseChoisie, plateau) == true) {
					ok = false;
				}
			}

			/// renvoie un message de d'acceptation ou de rejet
			if (!ok) {
				/// si un moulin est cr��, le joueur est averti
				if (ModeDeJeuMethodes.moulinVerifie(caseChoisie, Pion.getCouleurActuelle(), plateau) == true) {
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
			if (plateau.mode == 1 || plateau.mode == 3) { /// pose ou saut
				if (plateau.tabCases[caseChoisie].pion == Pion.vide) {
					ok = false;
				}
			} else if (plateau.mode == 2) { /// glisse
				if (ModeDeJeuMethodes.modeGlisseVerifiePose(pion, caseChoisie, plateau) == true) {
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

			/// v�rifie que la case choisie est de la bonne couleur
			/// et qu'elle peut �tre d�plac�e
			if (plateau.mode == 2) { /// glisse
				/// si oui
				if (plateau.tabCases[caseChoisie].pion == Pion.getCouleurJoueur()
						&& 	plateau.tabCases[caseChoisie].getBlocage(plateau) == false) {
					ok = true;
				} else {
					ok = false;
				}
			} else if (plateau.mode == 3) { /// saut
				if (plateau.tabCases[caseChoisie].pion == Pion.getCouleurJoueur()) {
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

			/// v�rifie que la case choisie est de la bonne couleur
			/// et qu'elle peut �tre d�plac�e
			if (plateau.mode == 2) { /// glisse
				if (plateau.tabCases[caseChoisie].pion == Pion.getCouleurRobot()
						&& 	plateau.tabCases[caseChoisie].getBlocage(plateau) == false) {
					ok = true;
				} else {
					ok = false;
				}
				/// v�rifie que la case peut �tre choisie
			} else if (plateau.mode == 3) { /// saut
				if (plateau.tabCases[caseChoisie].pion == Pion.getCouleurRobot()) {
					ok = true;
				} else {
					ok = false;
				}
			}
		} while (!ok);

		return caseChoisie;
	}

}
