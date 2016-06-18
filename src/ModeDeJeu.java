import java.io.IOException;
import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class ModeDeJeu {

	public static int robotElimine = 24; /// variable pour sortir les pions
						/// que le
						/// robot mange
	public static int joueurElimine = 43; /// variable pour sortir les pions
						/// que
						/// le joueur mange

	///
	Deplacements deplacementsOutils = new Deplacements();
	///

	/// ******* mode de jeu **********

	public void joue() throws IOException {
		/// variable pour sortir du jeu s'il un joueur fini
		boolean fin = false;
		/// blanc commence
		Pion.setCouleurActuelle(Pion.couleurDominante);
		/// première partie, les joueurs posent leurs pions.
		modePose();

		while (fin) {

			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				if (Pion.getNbrPionsRobot() > 3)
					modeDeplacement();
				else if (Pion.getNbrPionsRobot() == 3)
					modeSaut();
				else
					fin = true;

			} else {
				if (Pion.getNbrPionsJoueur() > 3)
					modeDeplacement();
				else if (Pion.getNbrPionsJoueur() == 3)
					modeSaut();
				else
					fin = true;
			}

			/// effectue le changement de joueur
			Pion.setCouleurActuelle((Pion.getCouleurActuelle() == Pion.blanc ? Pion.noir : Pion.blanc));

		}

		modeFin();
	}

	public void modePose() throws IOException {

		int IDCaseDepartJoueur = 0; /// case depart joueur
		int IDCaseDepartRobot = 0; /// case depart robot
		int IDCaseArrivee = 0;/// case arrivee
		int iDepartRobot = 0; /// défini le n° de la case départ du
					/// robot
		int iDepartJoueur = 0; /// défini le n° de la case départ du
					/// joueur

		LCD.drawString("coucou", 0, 0);
		deplacementsOutils.cadrage();

		/// buzz pour annoncer au joueur qu'il peut joueur (si c'est son
		/// tour)
		if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
			Sound.beep();
		}

		for (int i = 0; i < 18; i++) {

			/// création d'un pion avec attribution de la couleur
			Deplacements pion = new Deplacements(Pion.getCouleurActuelle());

			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				IDCaseDepartRobot = iDepartRobot + 25;
				/// choisi une case libre
				IDCaseArrivee = robotPose();
				/// attribution au pion de la case de depart
				pion.setCaseDepart(IDCaseDepartRobot);
				iDepartRobot++;
				LCD.clear(4);
				LCD.drawString("Robot", 0, 4);
			} else {
				IDCaseDepartJoueur = 42 - iDepartJoueur;
				/// choisi une case libre

				IDCaseArrivee = joueurPose();
				/// attribution au pion de la case de départ
				pion.setCaseDepart(IDCaseDepartJoueur);
				iDepartJoueur++;
				LCD.clear(4);
				LCD.drawString("Joueur", 0, 4);

			}
			/// attribue la case d'arrivée au pion
			pion.setCaseArrivee(IDCaseArrivee);

			/// déplacement du pion
			pion.deplacementPion();

			/// vérifie si un moulin est effectué, et joue en
			/// conséquence
			ModeDeJeuMethodes.verifieMoulin(IDCaseArrivee);

			/// change la couleur pour donner la main
			Pion.setCouleurActuelle((Pion.getCouleurActuelle() == Pion.blanc ? Pion.noir : Pion.blanc));

			/// buzz pour annoncer au joueur qu'il peut joueur (si
			/// c'est son tour)
			if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
				Sound.beep();
			}

		}

	}

	public void modeDeplacement() throws IOException {
		int IDCaseDepart;
		int IDCaseArrivee;

		/// création d'un pion avec attribution de la couleur
		Deplacements pion = new Deplacements(Pion.getCouleurActuelle());

		if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
			IDCaseDepart = robotPrend();
			IDCaseArrivee = robotPose();
		} else {
			IDCaseDepart = joueurPrend();
			IDCaseArrivee = joueurPose();
		}
		pion.setCaseDepart(IDCaseDepart);
		pion.setCaseArrivee(IDCaseArrivee);

		pion.deplacementPion();

		ModeDeJeuMethodes.verifieMoulin(IDCaseArrivee);

		/// buzz pour annoncer au joueur qu'il peut joueur (si c'est son
		/// tour)
		if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
			Sound.beep();
		}
	}

	public void modeSaut() {
		Button.waitForAnyPress();
	}

	public void modeFin() throws IOException {
		Outils outils = new Outils();
		outils.reglagesFin();
	}

	/// ******* tour des joueurs ************
	public int joueurPose() throws IOException {
		boolean ok = true;
		int caseChoisie;
		do {

			caseChoisie = Communication.PCInputStream();

			/// vérifie que la case choisie est libre
			ok = caseLibre(caseChoisie);
			/// renvoie un message de d'acceptation ou de rejet
			if (ok == false) {
				Communication.PCOutputStream(2);
			} else {
				Communication.PCOutputStream(1);
			}

		} while (ok);
		return caseChoisie;
	}

	public int robotPose() {
		boolean ok = true;
		int caseChoisie;

		do {
			caseChoisie = Robot.robotJoue();
			/// vérifie que la case choisie est libre
			ok = caseLibre(caseChoisie);
		} while (ok);

		return caseChoisie;
	}

	public int joueurPrend() {
		int caseChoisie;
		LCD.clear(6);
		LCD.drawString("joueur", 0, 6);
		Button.waitForAnyPress();
		caseChoisie = 0;
		return caseChoisie;
	}

	public int robotPrend() {
		int caseChoisie;
		LCD.clear(6);
		LCD.drawString("robot", 0, 6);
		Button.waitForAnyPress();
		caseChoisie = 0;
		return caseChoisie;
	}

	public static int mange() throws IOException {
		boolean ok = false;
		int caseChoisie;

		/// répète jusqu'à que la case soit correctement choisie
		do {
			/// regarde qui joue est fait en conséquence
			if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
				caseChoisie = Communication.PCInputStream();
			} else {
				caseChoisie = Robot.robotJoue();
			}
			/// ok doit être true pour continuer. Veut dire que la case est occupée
			ok = caseLibre(caseChoisie);
			/// buzz pour le joueur s'il faut rejouer
			if (ok == false && Pion.getCouleurActuelle() == Pion.getCouleurJoueur()){
				Communication.PCOutputStream(4);
				Sound.beep();
			}
			
		} while (ok == false);
		return caseChoisie;
	}

	/// ******* outils pour les modes********

	// à tester si je le met dans outils
	public void detecteCouleur() {
		deplacementsOutils.detecteCouleur();
	}

	public static boolean caseLibre(int caseTestee) {
		boolean ok = true;

		/// regarde si la case est occupée par un pion
		/// inoccupee = 0 , occupee = 1 ou 6 (noir ou blanc)
		if (Pion.caseID.get(caseTestee) == Pion.noir || Pion.caseID.get(caseTestee) == Pion.blanc)
			ok = true;
		else
			ok = false;

		return ok;
	}

	public boolean caseOccupee(int caseTestee, int couleur) {

		return false;

	}

}
