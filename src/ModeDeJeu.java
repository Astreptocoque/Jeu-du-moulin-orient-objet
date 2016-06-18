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
				/// si le nombre de poins est supérieur à 3, glissements
				if (Pion.getNbrPionsRobot() > 3)
					modeGlisse();
				/// s'il est égal à 3, sauts
				else if (Pion.getNbrPionsRobot() == 3)
					modeSaut();
				/// sinon, c'est la fin du jeu
				else
					fin = false;

			} else {
				if (Pion.getNbrPionsJoueur() > 3)
					modeGlisse();
				else if (Pion.getNbrPionsJoueur() == 3)
					modeSaut();
				else
					fin = false;
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
		modeFin();
	}

	public void modePose() throws IOException {
		int iDepartRobot = 0; /// défini le n° de la case départ du
					/// robot. Doit être à zéro
		int iDepartJoueur = 0; /// défini le n° de la case départ du
					/// joueur. Doit être à zéro

		/// buzz pour annoncer au joueur qu'il peut joueur (si c'est son
		/// tour)
		if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
			Sound.beep();
		}

		for (int i = 0; i < 18; i++) {

			/// création d'un pion avec attribution de la couleur
			Deplacements pion = new Deplacements(Pion.getCouleurActuelle());

			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// case départ, avance à chaque tour de 1 dans la ligne de départ
				pion.setCaseDepart(iDepartRobot + 25);
				/// choisi une case libre et attribue case d'arrivée
				pion.setCaseArrivee(robotPose());
				/// incrémente 1 dans la ligne de départ des pions
				iDepartRobot++;
				
			} else {
				/// case départ, avance à chaque tour de 1 dans la ligne de départ
				pion.setCaseDepart(42 - iDepartJoueur);
				/// choisi une case libre, et attibue la case d'arrivée
				pion.setCaseArrivee(joueurPose());
				/// incrémente 1 dans la ligne de départ des pions
				iDepartJoueur++;
			}
			
			/// déplacement du pion
			pion.deplacementPion();
			/// vérifie si un moulin est effectué, et joue en
			/// conséquence
			ModeDeJeuMethodes.verifieMoulin(pion.getCaseArrivee());
			/// change la couleur pour donner la main
			Pion.setCouleurActuelle((Pion.getCouleurActuelle() == Pion.blanc ? Pion.noir : Pion.blanc));
	}

	}

	public void modeGlisse() throws IOException {
		/// création d'un pion avec attribution de la couleur
		Deplacements pion = new Deplacements(Pion.getCouleurActuelle());

		if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
			pion.setCaseDepart(robotPrend());
			pion.setCaseArrivee(robotPose());
		} else {
			pion.setCaseDepart(joueurPrend());
			pion.setCaseArrivee(joueurPose());
		}
		
		/// déplace le pion
		pion.deplacementPion();
		/// vérifie si un moulin est créé
		ModeDeJeuMethodes.verifieMoulin(pion.getCaseArrivee());
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

	
	/// ******* outils pour les modes********

	
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
