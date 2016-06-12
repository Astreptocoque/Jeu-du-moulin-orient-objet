import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class ModeDeJeu {

	static ServerSocket server;
	static Socket socket;

	private int robotElimine = 24; /// variable pour sortir les pions que le
					/// robot mange
	private int joueurElimine = 43; /// variable pour sortir les pions que
					/// le joueur mange
	private int nbrPionsJoueur = 9;
	private int nbrPionsRobot = 9;

	private int couleur;
	///
	Random random = new Random();
	Deplacements deplacementsOutils = new Deplacements();
	///

	/// ******* mode de jeu **********

	public void joue() throws IOException {
		/// variable pour sortir du jeu s'il un joueur fini
		boolean fin = false;
		/// couleur de départ. Blanc commence, donc 6
		couleur = 6;
		
		/// première partie, les joueurs posent leurs pions.
		modePose();

		while (fin) {
			
			if (couleur == Deplacements.couleurRobot) {
				if (nbrPionsRobot > 3)
					modeDeplacement();
				else if (nbrPionsRobot == 3)
					modeSaut();
				else
					fin = true;

			} else {
				if (nbrPionsJoueur > 3)
					modeDeplacement();
				else if (nbrPionsJoueur == 3)
					modeSaut();
				else
					fin = true;
			}

			if (couleur == 6)
				couleur = 1;
			else
				couleur = 6;
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

		for (int i = 0; i < 18; i++) {
			
			/// création d'un pion avec attribution de la couleur
			Deplacements pion = new Deplacements(couleur);
			
			
			if (couleur == Deplacements.couleurRobot) {
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
				///attribution au pion de la case de départ
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
			verifieMoulin(IDCaseArrivee);

			/// change la couleur pour donner la main
			if (couleur == 6)
				couleur = 1;
			else
				couleur = 6;

			/// buzz pour annoncer au joueur qu'il peut joueur (si c'est son tour)
			if (couleur == Deplacements.couleurJoueur) {
				Sound.beep();
			}

		}

	}

	public void modeDeplacement() throws IOException {
		int IDCaseDepart;
		int IDCaseArrivee;
		
		/// création d'un pion avec attribution de la couleur
		Deplacements pion = new Deplacements(couleur);
		
		if (couleur == Deplacements.couleurRobot) {
			IDCaseDepart = robotPrend();
			IDCaseArrivee = robotPose();
		} else {
			IDCaseDepart = joueurPrend();
			IDCaseArrivee = joueurPose();
		}
		pion.setCaseDepart(IDCaseDepart);
		pion.setCaseArrivee(IDCaseArrivee);


		pion.deplacementPion();

		verifieMoulin(IDCaseArrivee);

		/// buzz pour annoncer au joueur qu'il peut joueur (si c'est son tour)
		if (couleur == Deplacements.couleurRobot) {
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

			caseChoisie = PCInputStream();

			/// vérifie que la case choisie est libre
			ok = caseLibre(caseChoisie);

			if (ok == false) {
				PCOutputStream(2);
			} else {
				PCOutputStream(1);
			}

		} while (ok);
		return caseChoisie;
	}

	private int robotPose() {
		boolean ok = true;
		int caseChoisie;

		do {
			caseChoisie = random.nextInt(24) + 1;
			/// vérifie que la case choisie est libre
			ok = caseLibre(caseChoisie);
		} while (ok);

		return caseChoisie;
	}

	private int joueurPrend() {
		int caseChoisie;
		LCD.clear(6);
		LCD.drawString("joueur", 0, 6);
		Button.waitForAnyPress();
		caseChoisie = 0;
		return caseChoisie;
	}

	private int robotPrend() {
		int caseChoisie;
		LCD.clear(6);
		LCD.drawString("robot", 0, 6);
		Button.waitForAnyPress();
		caseChoisie = 0;
		return caseChoisie;
	}

	/// pour manger des pions
	private int joueurChoisit() throws IOException {
		boolean ok = true;
		int caseChoisie;
		do {
			if(server.isClosed() == false){
				server.close();
			}
			if(socket.isClosed() == false){
				socket.close();
			}
			caseChoisie = PCInputStream();

			/// vérifie que la case choisie est libre
			ok = caseLibre(caseChoisie);

		} while (ok == false);
		return caseChoisie;
	}

	private int robotChoisit() {
		boolean ok = true;
		int caseChoisie;

		do {
			caseChoisie = random.nextInt(24) + 1;
			/// vérifie que la case choisie est libre
			ok = caseLibre(caseChoisie);
		} while (ok == false);

		return caseChoisie;

	}

	/// ******* outils pour les modes********

	public void detecteCouleur() {
		deplacementsOutils.detecteCouleur();
	}

	public boolean caseLibre(int caseTestee) {
		boolean ok = true;

		/// regarde si la case est occupée par un pion
		/// inoccupee = 0 , occupee = 1 ou 6 (noir ou blanc)
		if (Pion.caseID.get(caseTestee) == 1 || Pion.caseID.get(caseTestee) == 6)
			ok = true;
		else
			ok = false;

		return ok;
	}

	public boolean caseOccupee(int caseTestee, int couleur) {

		return false;

	}

	private void verifieMoulin(int IDCaseTestee) throws IOException {
		int couleurJoueurActuel = 0; /// couleur de celui qui fait le
						/// moulin
		int couleurAdversaire = 0; /// couleur de l'adversaire
		boolean peutManger = false;
		/// si tout les pions de l'adversaire forment des moulins,
		/// stop empêche le robot de cherche infiniment
		/// une pièce libre pour l'enlever
		/// joue sur la probalité qu'au bout de 100 essai,
		/// le hasard a essayé toutes les pièces (au max 9 pièces)

		if (couleur == 6) {
			couleurJoueurActuel = 6;
			couleurAdversaire = 1;
		} else {
			couleurJoueurActuel = 1;
			couleurAdversaire = 6;
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
			if (moulinCompareCase(IDCaseTestee, couleurJoueurActuel) == true) {

				/// si oui, le joueur choisi un pion à manger
				int caseChoisie = 0;

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
				if (couleurJoueurActuel == Deplacements.couleurRobot)
					pion.setCaseArrivee(robotElimine += 1);
				else
					pion.setCaseArrivee(joueurElimine -= 1);

				pion.deplacementPion();
				
			} else {
				LCD.clear(6);
				LCD.drawString("aucune pieces", 0, 6);
			}
		}

	}

	private boolean moulinCompareCase(int caseDeposee, int couleur) {
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

	private int mangePion(int couleurAManger) throws IOException {
		int caseChoisie = 0;
		boolean ok = false;
		/// défini si c'est le robot ou le joueur qui joue
		if (couleurAManger == Deplacements.couleurJoueur) {
			do {
				LCD.drawString("robot mange", 0, 5);
				caseChoisie = robotChoisit();
				/// vérifie que la case choisie est occupée par
				/// un pion adverse
				if (moulinCompareCase(caseChoisie, couleurAManger) == false && caseLibre(caseChoisie) == true && Pion.caseID.get(caseChoisie) == couleurAManger)
					ok = true;
			} while (ok == false);
			nbrPionsJoueur--;

		} else {
			/// en attendant, c'est au hasard
			do {
				Sound.beep();

				LCD.drawString("joueur mange", 0, 5);
				caseChoisie = joueurChoisit();
				/// vérifie que la case choisie est occupée par
				/// un pion adverse
				if (moulinCompareCase(caseChoisie, couleurAManger) == false
						&& caseLibre(caseChoisie) == true
						&& Pion.caseID.get(caseChoisie) == couleurAManger) {
					ok = true;
					PCOutputStream(3);
				} else {
					ok = false;
					PCOutputStream(4);
				}

			} while (ok == false);
			nbrPionsRobot--;
		}

		return caseChoisie;
	}

	public int PCInputStream() throws IOException {

		LCD.drawString("attente", 0, 5);
		int input;
		boolean erreur = false;

		do {
			LCD.clear();
			try {
				server = new ServerSocket(1111);
				erreur = false;
			} catch (BindException e) {
				// TODO Auto-generated catch block
				LCD.clear();
				e.printStackTrace();
				erreur = true;
			}
		} while (erreur == true);

		socket = server.accept();

		DataInputStream in = new DataInputStream(socket.getInputStream());

		input = in.readInt();

		return input;
	}

	public void PCOutputStream(int output) throws IOException {
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());

		out.writeInt(output);
		LCD.clear(5);

		server.close();

	}

	public void tests() {
		Pion.caseID.remove(22);
		Pion.caseID.put(22, 6);
		Pion.caseID.remove(23);
		Pion.caseID.put(23, 6);
		Pion.caseID.remove(1);
		Pion.caseID.put(1, 1);
		Pion.caseID.remove(2);
		Pion.caseID.put(2, 1);
//		Outils.caseID.remove(3);
//		Outils.caseID.put(3, 1);
		// Outils.caseID.remove(8);
		// Outils.caseID.put(8, 1);
	}

}
