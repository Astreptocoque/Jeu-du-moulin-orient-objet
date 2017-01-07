import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TactiqueSaut {

	public static Coups tactiqueSaut(Plateau plateau) {
		ArrayList<Integer> pionsRobot = pionSurPlateau(plateau, Pion.getCouleurRobot());
		ArrayList<Integer> pionsJoueur = pionSurPlateau(plateau, Pion.getCouleurJoueur());

		/// on crée le coup
		Coups coup = null;

		coup = deuxPionsAlignesCaseLibre(plateau, coup, pionsRobot);
		if (coup == null) {
			coup = deuxPionsAlignesCaseOccupee(plateau, coup, pionsRobot);
		}

		return coup;
	}

	private static Coups deuxPionsAlignesCaseLibre(Plateau plateau, Coups coup, ArrayList<Integer> pionsRobot) {
		/// si 2 pions robot sont alignés et la 3ème case est libre

		/// on parcourt tous les moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3ème case est vide
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.vide) {

				/// pour l'argument, les pions déjà dans le moulin
				Integer[] pionsMoulin = { moulin[0], moulin[1] };
				coup = chercheRamenePion(plateau, moulin[2], pionsMoulin, pionsRobot);

				/// si la 2ème case est vide
			} else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {

				/// pour l'argument, les pions déjà dans le moulin
				Integer[] pionsMoulin = { moulin[0], moulin[2] };
				coup = chercheRamenePion(plateau, moulin[1], pionsMoulin, pionsRobot);

				/// si la 1ère case est vide
			} else if (plateau.mapCases.get(moulin[0]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {

				/// pour l'argument, les pions déjà dans le moulin
				Integer[] pionsMoulin = { moulin[1], moulin[2] };
				coup = chercheRamenePion(plateau, moulin[0], pionsMoulin, pionsRobot);
			}
		}

		return coup;
	}

	private static Coups deuxPionsAlignesCaseOccupee(Plateau plateau, Coups coup, ArrayList<Integer> pionsRobot) {
		/// si deux pions robot sont alignés et que la 3ème case est occupée

		/// on parcourt tous les moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3ème case est occupée
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {

				/// pour l'argument, les pions déjà dans le moulin
				int[] pionsMoulin = { moulin[0], moulin[1] };
				coup = deuxPionsAlignesCaseOccupeeSuite(plateau, pionsMoulin, pionsRobot, coup);

				/// si la 2ème case est occupée
			} else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {

				/// pour l'argument, les pions déjà dans le moulin
				int[] pionsMoulin = { moulin[0], moulin[2] };
				coup = deuxPionsAlignesCaseOccupeeSuite(plateau, pionsMoulin, pionsRobot, coup);

				/// si la 1ère case est occupée
			} else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {

				/// pour l'argument, les pions déjà dans le moulin
				int[] pionsMoulin = { moulin[1], moulin[2] };
				coup = deuxPionsAlignesCaseOccupeeSuite(plateau, pionsMoulin, pionsRobot, coup);
			}
		}

		return coup;
	}

	//// ------------------------------------------------------------

	private static Coups chercheRamenePion(Plateau plateau, int caseArrivee, Integer[] pionsMoulin,
			ArrayList<Integer> pionsRobot) {
		/// cherche ou est le 3ème pion et le ramène
		/// prend le pion qui n'est pas encore dans le moulin
		int pion3;
		/// on copie pionsRobot pour pas le modifier
		ArrayList<Integer> pionsRobotCopie = new ArrayList<Integer>();
		for (int i = 0; i < pionsRobot.size(); i++) {
			pionsRobotCopie.add(pionsRobot.get(i));
		}
		/// on supprime les 2 pions qu'on connait
		pionsRobotCopie.remove(pionsMoulin[0]);
		pionsRobotCopie.remove(pionsMoulin[1]);
		/// on sait maintenant ou est le troisième pion
		pion3 = pionsRobotCopie.get(0);

		/// on crée le coup
		Coups coup = new Coups(pion3, 0, caseArrivee);

		return coup;
	}

	private static Coups deuxPionsAlignesCaseOccupeeSuite(Plateau plateau, int[] pionsMoulin,
			ArrayList<Integer> pionsRobot, Coups coup) {
		/// prend le pion qui n'est pas encore dans le moulin
		int pion3;
		/// on copie pionsRobot pour pas le modifier
		ArrayList<Integer> pionsRobotCopie = new ArrayList<Integer>();
		for (int i = 0; i < pionsRobot.size(); i++) {
			pionsRobotCopie.add(pionsRobot.get(i));
		}
		/// on supprime les 2 pions qu'on connait
		pionsRobotCopie.remove(pionsMoulin[0]);
		pionsRobotCopie.remove(pionsMoulin[1]);
		/// on sait maintenant ou est le troisième pion
		pion3 = pionsRobotCopie.get(0);

		/// si pion3 est sur une ligne libre
		int[] premiereLigne = plateau.mapCases.get(pion3).casesMoulins[0].clone();
		int[] secondeLigne = plateau.mapCases.get(pion3).casesMoulins[1].clone();
		ArrayList<int[]> lignesLibres = new ArrayList<int[]>();

		/// si premiereligne est libre
		if (plateau.mapCases.get(premiereLigne[0]).pion == Pion.vide
				&& plateau.mapCases.get(premiereLigne[1]).pion == Pion.vide) {
			lignesLibres.add(premiereLigne);
		}
		/// si la deuxième ligne est libre
		if (plateau.mapCases.get(secondeLigne[0]).pion == Pion.vide
				&& plateau.mapCases.get(secondeLigne[1]).pion == Pion.vide) {
			lignesLibres.add(secondeLigne);
		}
		
		Random random = new Random();
		/// s'il y a une ligne libre
		if (!lignesLibres.isEmpty()) {
			/// on bouge un des deux pions du moulin à ses côté
			/// on choisi au hasard une ligne dans lignesLibres
			/// on choisi au hasard un des deux pions
			/// la case depart du coup, un des 2 pions du moulin
			int caseDepart = pionsMoulin[random.nextInt(2)];
			/// la case arrivée, une ligne libre au hasard et un emplacement au hasard
			int caseArrivee = lignesLibres.get(random.nextInt(lignesLibres.size()))[random.nextInt(2)];

			coup = new Coups(caseDepart, 0, caseArrivee);
		}
		/// sinon
		else {
			/// on copie pionsRobot pour pas le modifier
			pionsRobotCopie.clear();
			for (int i = 0; i < pionsRobot.size(); i++) {
				pionsRobotCopie.add(pionsRobot.get(i));
			}

			int caseDepart = 0;
			int caseArrivee = 0;
			/// on bouge n'importe quel pion sur une ligne libre
			/// pour autant qu'il ne bloque pas un moulin adverse
			if(bloqueMoulin(plateau, pionsRobotCopie)){
				/// on prend un pion au hasard dans pionsRobotCopie
				caseDepart = pionsRobotCopie.get(random.nextInt(pionsRobotCopie.size()));
				/// pour la case arrivée on cherche si possible une ligne libre
				/// pour un peu de hasard
				ArrayList<int[]> listeLignes = new ArrayList<int[]>();
				for(int[] ligne : Cases.listeMoulins){
					listeLignes.add(ligne);
				}
				Collections.shuffle(listeLignes);
				
				for(int[] ligne : listeLignes){
					if (plateau.mapCases.get(ligne[0]).pion == Pion.vide
							&& plateau.mapCases.get(ligne[1]).pion == Pion.vide
							&& plateau.mapCases.get(ligne[2]).pion == Pion.vide) {
						caseArrivee = ligne[random.nextInt(ligne.length)];
						break;
					}
				}
				/// si aucune ligne n'est libre
				if(caseArrivee == 0){
					/// on choisit au hasard parmis les cases libres
					/// passe en revue toute les cases libres du plateau
					ArrayList<Integer>casesLibres = new ArrayList<Integer>();
					for(int i = 1; i < 25; i++){
						if(plateau.mapCases.get(i).pion == Pion.vide){
							casesLibres.add(i);
						}
					}
					/// car de toute façon la partie est perdue
					caseArrivee = casesLibres.get(random.nextInt(casesLibres.size()));
				}
			}
			/// sinon, ça signifie que tous les pions bloquent un moulin
			else{
				/// et on bouge un pion au hasard
				caseDepart = pionsRobot.get(random.nextInt(pionsRobot.size()));
				/// pour la case arrivée
				/// passe en revue toute les cases libres du plateau
				ArrayList<Integer>casesLibres = new ArrayList<Integer>();
				for(int i = 1; i < 25; i++){
					if(plateau.mapCases.get(i).pion == Pion.vide){
						casesLibres.add(i);
					}
				}
				/// car de toute façon la partie est perdue
				caseArrivee = casesLibres.get(random.nextInt(casesLibres.size()));
			}
			
			
		}

		return coup;
	}

	private static Boolean bloqueMoulin(Plateau plateau, ArrayList<Integer>pionsRobotCopie) {
		Boolean affirmatif = true;
		/// parcours tous les possibilités de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3ème case
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {
				/// on supprime le pion
				Integer pion = moulin[2];
				pionsRobotCopie.remove(pion);
			}
			/// si la 2ème case 
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// on supprime le pion
				Integer pion = moulin[1];
				pionsRobotCopie.remove(pion);
			}
			/// si la 1ère case
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// on supprime le pion
				Integer pion = moulin[0];
				pionsRobotCopie.remove(pion);
			}
		}
		
		if(pionsRobotCopie.isEmpty()){
			affirmatif = false;
		}

		return affirmatif;
	}

	private static int hasard() {
		/// prend une case au hasard sur le plateau
		Random random = new Random();
		return random.nextInt(24) + 1;
	}

	private static ArrayList<Integer> pionSurPlateau(Plateau plateau, int couleur) {
		/// récupère tous les pions d'une couleur présents sur le
		/// plateau
		/// liste des pions
		ArrayList<Integer> pionPlateau = new ArrayList<Integer>();

		/// parcours toutes les cases
		for (int i = 1; i < 25; i++) {
			if (plateau.mapCases.get(i).pion == couleur)
				pionPlateau.add(i);
		}

		return pionPlateau;
	}
}
