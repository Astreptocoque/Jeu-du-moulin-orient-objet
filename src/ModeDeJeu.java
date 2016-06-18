import java.io.IOException;
import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class ModeDeJeu {
	/// variable pour sortir les pions que le robot mange
	public static int robotElimine = 24; 
	 /// variable pour sortir les pions que le joueur mange
	public static int joueurElimine = 43;
	/// variable pour d�finir le mode de jeu. 1 = pose, 2 = glisse, 3 = saut
	private int mode;
	
	///
	Deplacements deplacementsOutils = new Deplacements();
	///

	/// ******* mode de jeu **********

	public void joue() throws IOException {
		/// variable pour sortir du jeu s'il un joueur fini
		boolean fin = false;
		/// blanc commence
		Pion.setCouleurActuelle(Pion.couleurDominante);
		/// premi�re partie, les joueurs posent leurs pions.
		modePose();

		while (fin) {

			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// si le nombre de poins est sup�rieur � 3, glissements
				if (Pion.getNbrPionsRobot() > 3)
					modeGlisse();
				/// s'il est �gal � 3, sauts
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
		int iDepartRobot = 0; /// d�fini le n� de la case d�part du
					/// robot. Doit �tre � z�ro
		int iDepartJoueur = 0; /// d�fini le n� de la case d�part du
					/// joueur. Doit �tre � z�ro

		/// buzz pour annoncer au joueur qu'il peut joueur (si c'est son
		/// tour)
		if (Pion.getCouleurActuelle() == Pion.getCouleurJoueur()) {
			Sound.beep();
		}

		for (int i = 0; i < 18; i++) {

			/// cr�ation d'un pion avec attribution de la couleur
			Deplacements pion = new Deplacements(Pion.getCouleurActuelle());

			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// case d�part, avance � chaque tour de 1 dans la ligne de d�part
				pion.setCaseDepart(iDepartRobot + 25);
				/// choisi une case libre et attribue case d'arriv�e
				pion.setCaseArrivee(robotPose(pion));
				/// incr�mente 1 dans la ligne de d�part des pions
				iDepartRobot++;
				
			} else {
				/// case d�part, avance � chaque tour de 1 dans la ligne de d�part
				pion.setCaseDepart(42 - iDepartJoueur);
				/// choisi une case libre, et attibue la case d'arriv�e
				pion.setCaseArrivee(joueurPose(pion));
				/// incr�mente 1 dans la ligne de d�part des pions
				iDepartJoueur++;
			}
			
			/// d�placement du pion
			pion.deplacementPion();
			/// v�rifie si un moulin est effectu�, et joue en
			/// cons�quence
			ModeDeJeuMethodes.verifieMoulin(pion.getCaseArrivee());
			/// change la couleur pour donner la main
			Pion.setCouleurActuelle((Pion.getCouleurActuelle() == Pion.blanc ? Pion.noir : Pion.blanc));
	}

	}

	public void modeGlisse() throws IOException {
		/// cr�ation d'un pion avec attribution de la couleur
		Deplacements pion = new Deplacements(Pion.getCouleurActuelle());
		
		/// s�l�ctionne le pion � bouger et la case d'arriv�e
		if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
			/// v�rifie que le robot puisse d�placer un pion
			if(ModeDeJeuMethodes.modeGlisseVerifiePosePossible() == true){
				/// si oui, fait le n�cessaire pour en d�placer un
				pion.setCaseDepart(robotPrend());
				pion.setCaseArrivee(robotPose(pion));
			}
			else{
				/// si non, la partie est finie
				modeFin();
			}
			
		} else {
			/// v�rifie que le robot puisse d�placer un pion
			if(ModeDeJeuMethodes.modeGlisseVerifiePosePossible() == true){
				/// si oui, fait le n�cessaire pour en d�placer un
				pion.setCaseDepart(joueurPrend());
				pion.setCaseArrivee(joueurPose(pion));
			}
			else{
				/// si non, la partie est finie
				modeFin();
			}
		}
		
		/// d�place le pion
		pion.deplacementPion();
		/// v�rifie si un moulin est cr��
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
	public int joueurPose(Pion pion) throws IOException {
		boolean ok = true;
		int caseChoisie;
		do {

			caseChoisie = Communication.PCInputStream();

			/// v�rifie que la case choisie est libre
			if(mode == 1){ /// pose
				ok = ModeDeJeuMethodes.caseLibre(caseChoisie);
			}
			else if (mode == 2){ /// glisse
				ok = ModeDeJeuMethodes.modeGlisseVerifie(pion, caseChoisie);
			}
			
			/// renvoie un message de d'acceptation ou de rejet
			if (ok == false) {
				Communication.PCOutputStream(2);
			} else {
				Communication.PCOutputStream(1);
			}

		} while (ok);
		return caseChoisie;
	}

	public int robotPose(Pion pion) {
		boolean ok = true;
		int caseChoisie;

		do {
			caseChoisie = Robot.robotJoue();
			/// v�rifie que la case choisie est libre
			if(mode == 1){ /// pose
				ok = ModeDeJeuMethodes.caseLibre(caseChoisie);
			}
			else if (mode == 2){ /// glisse
				ok = ModeDeJeuMethodes.modeGlisseVerifie(pion, caseChoisie);
			}
			
		} while (ok);

		return caseChoisie;
	}

	public int joueurPrend() {
		int caseChoisie;
		/// doit v�rifier que la case est occup�e par un pion de la bonne couleur
		
		
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

	
	


}
