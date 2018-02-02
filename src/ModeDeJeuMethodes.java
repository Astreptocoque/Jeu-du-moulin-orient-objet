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

		/// vérifie si un moulin est créé
		/// vérifie si min. une pièce adverse est disponible
		/// si aucune pièce adverse est diponible
		/// le programme saute l'étape
		if (moulinVerifie(IDCaseTestee, Pion.getCouleurActuelle(), plateau)
				&& plateau.getNbrPionsSurLePlateau(couleurAdversaire) > 0
				&& plateau.caseLibre(couleurAdversaire)) {
			
			
			/// si oui, le joueur choisi un pion à manger
			/// création du pion à manger
			Deplacements pionAManger = new Deplacements();

			/// set la case départ, avec mangePion
			pionAManger.setCaseDepart(mangePion(couleurAdversaire, pionAManger, plateau), plateau);

			/// attribue la case d'arrivée sur la case de la
			/// ligne de départ suivante
			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				pionAManger.setCaseArrivee(robotElimine += 1, plateau);
				/// sort le pion
				pionAManger.deplacementPionRobot();
				/// se replace à l'origine pour le
				/// joueur
				pionAManger.deplacementOrigine();
			} else {
				pionAManger.setCaseArrivee(joueurElimine -= 1, plateau);
				/// sort le pion
				pionAManger.deplacementPionJoueur();
			}
			/// si aucun moulin n'est créé...
		} else {
			/// ... et si c'est le robot qui a joué...
			if (Pion.getCouleurActuelle() == Pion.getCouleurRobot()) {
				/// ...il se replace à l'origine pour le
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
		

		/// la boucle est effectuée tant qu'un pion valide n'est pas
		/// choisi
		do {
			/// choisi la case à manger
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

			/// on vérifie ensuite que le pion puisse être mangé

			/// condition 1 : vérifie que le pion choisi ne
			/// soit pas dans un moulin
			if (moulinVerifie(caseChoisie, couleurAManger, plateau) == false
					/// c.2 : si la case est occupée
					/// c'est ce que l'on souhaite
					&& plateau.mapCases.get(caseChoisie).pion != Pion.vide
					/// c.3 : vérifie la couleur du pion
					/// on veut la couleur à manger
					&& plateau.mapCases.get(caseChoisie).pion == couleurAManger) {
				/// si toutes les conditions sont réunis, alors
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
			/// soustrait le pion mangé au nbr total de pion du
			/// joueur
			plateau.enlevePionRobot();
		} else {
			/// soustrait le pion au nbr total de pion du robot
			plateau.enlevePionJoueur();
		}
		return caseChoisie;
	}

	public static boolean moulinVerifie(int caseTestee, int couleur, Plateau plateau) {
		/// vérifie si un moulin est créé
		/// la variable couleur désigne la couleur du joueur
		boolean oui_non = false;

		/// on récupère les moulins possibles avec la case testée
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
		/// vérifie si le joueur a le droit de poser son pion à
		/// l'endroit choisi

		boolean ok = false;
		/// prend la série de cases possibles pour le pion choisi
		int casesPossibles[] = plateau.mapCases.get(pion.getCaseDepart()).getCasesAdjacentes();
		/// regarde si la case choisie par le joueur est comprise dans
		/// le tableau
		for (int casePossible : casesPossibles) {
			/// si la case d'arrivée est dans les cases disponibles
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
		/// vérifie si le joueur peut déplacer un pion, où s'il est
		/// coincé
		boolean ok = false;

		/// test chaque case, les unes après les autres
		for (int i = 1; i < 25; i++) {
			ok = plateau.mapCases.get(i).getBlocage(plateau);
			if (ok) {
				break;
			}
		}

		return ok;
	}
}
