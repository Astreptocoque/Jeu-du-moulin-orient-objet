import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class TactiqueMange {

	public static Coups tactiqueMange(Plateau plateau) {
		// ArrayList<Integer> pionsRobot = pionSurPlateau(plateau, Pion.getCouleurRobot());
		ArrayList<Integer> pionsJoueur = pionSurPlateau(plateau, Pion.getCouleurJoueur());
		ArrayList<Coups> listeCoups = new ArrayList<Coups>();

		LCD.clear();
		LCD.drawString("tactiqueMange", 0, 0);

		/// si le jeu est en mode pose
		if (plateau.mode == 1) {
			detecteSchema(plateau, pionsJoueur, listeCoups);
			detectePlacementStrategique(plateau, pionsJoueur, listeCoups);
			deuxPionsAdversesAlignesMode1(plateau, listeCoups);
			fermetureMoulinBloque(plateau, listeCoups);
			pourChaquePionMode1(plateau, pionsJoueur, listeCoups);
		}
		/// si c'est un autre mode
		else {
			deuxPionsAdversesAlignesAutresModes(plateau, listeCoups);
			pourChaquePionAutresModes(plateau, pionsJoueur, listeCoups);
			pionBloqueOuvertureMoulinAutresModes(plateau, listeCoups);
			fermetureMoulinBloque(plateau, listeCoups);
		}

		/// on mélange la liste
		Collections.shuffle(listeCoups);
		/// et on prend le premier coup le plus bas
		Boolean affirmatif = true;
		int valeur = 0;
		Coups coupChoisi = null;
		while (affirmatif) {
			for (int i = 0; i < listeCoups.size(); i++) {
				/// s'il y a un coup de la valeur voulue
				if (listeCoups.get(i).getValeur() == valeur) {
					/// pour sortir de la boucle
					affirmatif = false;
					/// on prend le coup
					coupChoisi = listeCoups.get(i);
				}
			}
			/// si affirmatif = true, c'est qu'aucun coup n'a été
			/// trouvé
			if (affirmatif == true) {
				/// on essaye avec la valeur du dessus
				valeur += 1;
			}
		}

		return coupChoisi;

	}

	public static void pionBloqueOuvertureMoulinAutresModes(Plateau plateau, ArrayList<Coups> listeCoups) {
		/// si un pion bloque l'ouverture d'un moulin

		/// parcours tous les possibilités de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si les 3 cases sont occupées par un pion du robot
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {
				/// on regarde pour chaque pion si ces cases
				/// adjacentes sont libres
				/// ou occupés par le robot. Il faut au moins
				/// une case libre
				for (int pion : moulin) {
					/// récupère les cases adjacentes
					int[] casesAdjacentes = plateau.mapCases.get(pion).getCasesAdjacentes();
					for (int caseAdjacente : casesAdjacentes) {
						/// si la case a un pion
						/// adverse, on l'ajoute en coup
						if (plateau.mapCases.get(caseAdjacente).pion == Pion
								.getCouleurJoueur()) {
							Coups coup = new Coups(3, caseAdjacente);
							listeCoups.add(coup);
						}
					}

				}

			}
		}
	}

	public static void fermetureMoulinBloque(Plateau plateau, ArrayList<Coups> listeCoups) {
		// si deux pions robot sont alignés avec un troisième adversaire
		// qui bloque

		/// parcours tous les possibilités de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3ème case est occupée par l'adversaire
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// on ajoute le pion adverse
				Coups coup = new Coups(1, moulin[2]);
				listeCoups.add(coup);
			}
			/// si la 2ème case est occupée par l'adversaire
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {
				/// on ajoute le pion adverse
				Coups coup = new Coups(1, moulin[1]);
				listeCoups.add(coup);
			}
			/// si la 1ère case est occupée par l'adversaire
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {
				/// on ajoute le pion adverse
				Coups coup = new Coups(1, moulin[0]);
				listeCoups.add(coup);
			}
		}

	}

	public static void deuxPionsAdversesAlignesAutresModes(Plateau plateau, ArrayList<Coups> listeCoups) {
		/// si deux pions sont alignés

		// si deux pions adverses sont alignés et la 3ème case est vide

		/// parcours tous les possibilités de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3ème case est vide
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.vide) {

				/// remonte les cases jusqu'a trouver un pion
				/// qui pourrai compléter le moulin
				ArrayList<Integer> valeursDifferentsChemins = new ArrayList<Integer>();
				ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();
				valeursDifferentsChemins = remonteCase(plateau, moulin[2], Pion.getCouleurJoueur(), 0,
						-1, valeursDifferentsChemins, casesDejaVisitees);

				if (!valeursDifferentsChemins.isEmpty()) {
					/// tri les différentes valeur (pas très propre comme code)
					/// pour obtenir la pièce la plus proche
					int[] valeursChemins = new int[valeursDifferentsChemins.size()];
					for (int i = 0; i < valeursDifferentsChemins.size(); i++) {
						valeursChemins[i] = valeursDifferentsChemins.get(i);
					}
					Arrays.sort(valeursChemins);
					/// la valeur est la 1ère valeur de valeursChemins, la plus
					/// petite

					/// on ajoute les deux pions
					Coups coup1 = new Coups(valeursChemins[0] - 1, moulin[0]);
					Coups coup2 = new Coups(valeursChemins[0] - 1, moulin[1]);
					listeCoups.add(coup1);
					listeCoups.add(coup2);
				}

			}
			/// si la 2ème case est vide
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {

				/// remonte les cases jusqu'a trouver un pion
				/// qui pourrai compléter le moulin
				ArrayList<Integer> valeursDifferentsChemins = new ArrayList<Integer>();
				ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();
				valeursDifferentsChemins = remonteCase(plateau, moulin[1], Pion.getCouleurJoueur(), 0,
						-1, valeursDifferentsChemins, casesDejaVisitees);

				if (!valeursDifferentsChemins.isEmpty()) {
					/// tri les différentes valeur (pas très propre comme code)
					/// pour obtenir la pièce la plus proche
					int[] valeursChemins = new int[valeursDifferentsChemins.size()];
					for (int i = 0; i < valeursDifferentsChemins.size(); i++) {
						valeursChemins[i] = valeursDifferentsChemins.get(i);
					}
					Arrays.sort(valeursChemins);
					/// la valeur est la 1ère valeur de valeursChemins, la plus
					/// petite

					/// on ajoute les deux pions
					Coups coup1 = new Coups(valeursChemins[0] - 1, moulin[0]);
					Coups coup2 = new Coups(valeursChemins[0] - 1, moulin[2]);
					listeCoups.add(coup1);
					listeCoups.add(coup2);
				}
			}
			/// si la 1ère case est vide
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {

				/// remonte les cases jusqu'a trouver un pion
				/// qui pourrai compléter le moulin
				ArrayList<Integer> valeursDifferentsChemins = new ArrayList<Integer>();
				ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();
				valeursDifferentsChemins = remonteCase(plateau, moulin[0], Pion.getCouleurJoueur(), 0,
						-1, valeursDifferentsChemins, casesDejaVisitees);

				if (!valeursDifferentsChemins.isEmpty()) {
					/// tri les différentes valeur (pas très propre comme code)
					/// pour obtenir la pièce la plus proche
					int[] valeursChemins = new int[valeursDifferentsChemins.size()];
					for (int i = 0; i < valeursDifferentsChemins.size(); i++) {
						valeursChemins[i] = valeursDifferentsChemins.get(i);
					}
					Arrays.sort(valeursChemins);
					/// la valeur est la 1ère valeur de valeursChemins, la plus
					/// petite

					/// on ajoute les deux pions
					Coups coup1 = new Coups(valeursChemins[0] - 1, moulin[1]);
					Coups coup2 = new Coups(valeursChemins[0] - 1, moulin[2]);
					listeCoups.add(coup1);
					listeCoups.add(coup2);
				}

			}
		}

	}

	public static void detecteSchema(Plateau plateau, ArrayList<Integer> pionsJoueur, ArrayList<Coups> listeCoups) {
		/// pour savoir si le coup est validé ou non
		Boolean affirmatif = false;
		/// passe en revue tout les couples possibles de duo de cases
		/// pour un schéma. parcours le tableau --> faire un dessin si jamais
		for (int pion1 = 0; pion1 < pionsJoueur.size() - 1; pion1++) {
			for (int pion2 = pion1 + 1; pion2 < pionsJoueur.size() - pion1; pion2++) {
				/// couple à tester
				int[] schema = { pionsJoueur.get(pion1), pionsJoueur.get(pion2) };
				/// trie le tableau
				if (schema[0] > schema[1]) {
					int echange = schema[0];
					schema[0] = schema[1];
					schema[1] = echange;
				}

				/// on passe en revue tout les couples de schema
				for (int i = 0; i < OutilsTactiques.casesSchemas.length; i++) {
					/// si les couples sont pareils
					if (schema[0] == OutilsTactiques.casesSchemas[i][0]
							&& schema[1] == OutilsTactiques.casesSchemas[i][1]) {
						/// si oui, on vérifie que toutes les cases
						/// nécessaires au schema soient libres
						int[] casesNecessairesSchema = OutilsTactiques.casesNecessairesSchemas[i]
								.clone();
						for (int j = 0; j < casesNecessairesSchema.length; j++) {
							/// si elle n'est pas vide, on abandonne
							/// tout
							if (plateau.mapCases.get(
									casesNecessairesSchema[j]).pion != Pion.vide) {
								affirmatif = false;
								break;
							} else {
								/// 1ere case bloque le schema
								affirmatif = true;
							}
						}
						/// si affirmatif est vrai, alors un schéma a été
						/// reconnu
						if (affirmatif) {
							/// les deux cases formant le schéma
							/// sont ajoutés dans listeCoups
							for (int pionSchema = 0; pionSchema < schema.length; pionSchema++) {
								Coups coup = new Coups(1, schema[pionSchema]);
								listeCoups.add(coup);
							}
						}
					}
				}

			}
		}
	}

	public static void detectePlacementStrategique(Plateau plateau, ArrayList<Integer> pionsJoueur,
			ArrayList<Coups> listeCoups) {
		/// pour savoir si le coup est validé ou non
		Boolean affirmatif = false;
		/// passe en revue tout les couples possibles de duo de cases
		/// pour un schéma
		/// parcours le tableau --> faire un dessin si jamais
		for (int pion1 = 0; pion1 < pionsJoueur.size() - 1; pion1++) {
			for (int pion2 = pion1 + 1; pion2 < pionsJoueur.size() - pion1; pion2++) {
				/// couple à tester
				int[] schema = { pionsJoueur.get(pion1), pionsJoueur.get(pion2) };
				/// trie le tableau
				if (schema[0] > schema[1]) {
					int echange = schema[0];
					schema[0] = schema[1];
					schema[1] = echange;
				}

				/// on passe en revue tout les couples de
				/// placements strat. possibles
				for (int i = 0; i < OutilsTactiques.placementsStrategiques.length; i++) {
					/// si les deux couples sont égaux
					if (schema[0] == OutilsTactiques.placementsStrategiques[i][0]
							&& schema[1] == OutilsTactiques.placementsStrategiques[i][1]) {
						/// on copie les schema 1 et 2 de niveau 1
						int[] casesPS11 = OutilsTactiques.casesNecessairesPS[2 * i].clone();
						int[] casesPS12 = OutilsTactiques.casesNecessairesPS[2 * i + 1].clone();
						/// on vérifie que toutes les cases néces. au PS 1
						/// du 1er niveau soient libres
						for (int casePS1 = 0; casePS1 < casesPS11.length; casePS1++) {
							if (plateau.mapCases
									.get(casesPS11[casePS1]).pion != Pion.vide) {
								/// pour tester le second niveau
								affirmatif = false;
								break;
							} else {
								affirmatif = true;
								/// les deux cases formant le schéma
								/// sont ajoutés dans listeCoups
								for (int pionSchema = 0; pionSchema < schema.length; pionSchema++) {
									Coups coup = new Coups(1, schema[pionSchema]);
									listeCoups.add(coup);
								}
							}
						}
						if (affirmatif == false) {
							/// le 1er niveau n'est pas libre on teste
							/// le second niveau si la case centrale est
							/// occupée et les deux autres libres
							if (plateau.mapCases.get(casesPS11[0]).pion == Pion
									.getCouleurJoueur()
									&& plateau.mapCases
											.get(casesPS11[1]).pion == Pion.vide
									&& plateau.mapCases.get(
											casesPS11[2]).pion == Pion.vide) {
								/// alors on ajoute le coup
								Coups coup = new Coups(1, casesPS11[0]);
								listeCoups.add(coup);
							}

						}

						/// cases néces. au PS 2 du 1er
						/// niveau soient libres
						for (int casePS2 = 0; casePS2 < casesPS12.length; casePS2++) {
							if (plateau.mapCases
									.get(casesPS12[casePS2]).pion != Pion.vide) {
								/// pour tester le second niveau
								affirmatif = false;
								break;
							} else {
								affirmatif = true;
								/// les deux cases formant le schéma
								/// sont ajoutés dans listeCoups
								for (int pionSchema = 0; pionSchema < schema.length; pionSchema++) {
									Coups coup = new Coups(1, schema[pionSchema]);
									listeCoups.add(coup);
								}
							}
						}
						if (affirmatif == false) {
							/// le 1er niveau n'est pas libre on teste
							/// le second niveau si la case centrale est
							/// occupée et les deux autres libres
							if (plateau.mapCases.get(casesPS12[0]).pion == Pion
									.getCouleurJoueur()
									&& plateau.mapCases
											.get(casesPS12[1]).pion == Pion.vide
									&& plateau.mapCases.get(
											casesPS12[2]).pion == Pion.vide) {
								/// alors on ajoute le coup
								Coups coup = new Coups(1, casesPS12[0]);
								listeCoups.add(coup);
							}

						}
					}
				}
			}
		}
	}

	public static void pourChaquePionMode1(Plateau plateau, ArrayList<Integer> pionsJoueur,
			ArrayList<Coups> listeCoups) {
		/// pour chaque pion...
		for (int elements : pionsJoueur) {
			Coups coup = new Coups(2, elements);
			listeCoups.add(coup);
		}
	}

	public static void deuxPionsAdversesAlignesMode1(Plateau plateau, ArrayList<Coups> listeCoups) {

		/// si 2 pions du joueur sont alignés avec la troisième case
		/// libre

		/// parcours tous les possibilités de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3ème case est vide
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.vide) {
				/// on ajoute les deux pions
				Coups coup1 = new Coups(0, moulin[0]);
				Coups coup2 = new Coups(0, moulin[1]);
				listeCoups.add(coup1);
				listeCoups.add(coup2);
			}
			/// si la 2ème case est vide
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// on ajoute les deux pions
				Coups coup1 = new Coups(0, moulin[0]);
				Coups coup2 = new Coups(0, moulin[2]);
				listeCoups.add(coup1);
				listeCoups.add(coup2);
			}
			/// si la 1ère case est vide
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// on ajoute les deux pions
				Coups coup1 = new Coups(0, moulin[1]);
				Coups coup2 = new Coups(0, moulin[2]);
				listeCoups.add(coup1);
				listeCoups.add(coup2);
			}
		}
	}

	public static void pourChaquePionAutresModes(Plateau plateau, ArrayList<Integer> pionsJoueur,
			ArrayList<Coups> listeCoups) {
		/// pour chaque pion
		for (int element : pionsJoueur) {
			ArrayList<Integer> valeursDifferentsChemins = new ArrayList<Integer>();
			ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();
			valeursDifferentsChemins = remonteCase(plateau, element, Pion.getCouleurJoueur(), 0, -1,
					valeursDifferentsChemins, casesDejaVisitees);

			/// uniquement si la liste n'est pas vide. Si elle est vide, il n'y pas
			/// d'autre pion joignable
			if (!valeursDifferentsChemins.isEmpty()) {
				/// tri les différentes valeur (pas très propre comme code)
				/// pour obtenir la pièce la plus proche
				int[] valeursChemins = new int[valeursDifferentsChemins.size()];
				for (int i = 0; i < valeursDifferentsChemins.size(); i++) {
					valeursChemins[i] = valeursDifferentsChemins.get(i);
				}
				Arrays.sort(valeursChemins);
				/// la valeur est la 1ère valeur de valeursChemins, la plus petite
				/// si valeur
				Coups coup = new Coups(valeursChemins[0] - 1, element);
				listeCoups.add(coup);
			}
		}

	}

	public static int hasard() {
		/// prend une case au hasard sur le plateau
		Random random = new Random();
		return random.nextInt(24) + 1;
	}

	/// ------------------------------- outils
	/// ------------------------------------

	public static ArrayList<Integer> remonteCase(Plateau plateau, int caseDepart, int couleurJoueur, int valeur,
			int casePrecedente, ArrayList<Integer> valeursDifferentsChemins, ArrayList<Integer> casesDejaVisitees) {
		/// !!! récursivité

		/// récupère les cases adjacente à caseDepart
		ArrayList<Integer> casesAdjacentes = new ArrayList<Integer>();
		for (int element : plateau.mapCases.get(caseDepart).casesAdjacentes)
			casesAdjacentes.add(element);
		/// retire la case précédemment testée
		if (casePrecedente != -1) {
			for (int i = 0; i < casesAdjacentes.size(); i++) {
				if (casesAdjacentes.get(i) == casePrecedente)
					casesAdjacentes.remove(i);
			}
		}

		casePrecedente = caseDepart;

		/// parcours toutes les cases adjacente à la case
		for (int caseAdjacente : casesAdjacentes) {
			/// si la case est vide, recommence l'opération
			
			/// on verifie que la case n'a pas déjà été visitée
			if (!casesDejaVisitees.contains(caseAdjacente)) {
			if (plateau.mapCases.get(caseAdjacente).pion == Pion.vide) {
				valeur += 1;
				/// pour pas tourner en rond dans le tablier
				casesDejaVisitees.add(caseAdjacente);
				/// caseDepart devient caseAdjacente,
				/// casePrecedente a déjà été changée
				valeursDifferentsChemins = remonteCase(plateau, caseAdjacente, couleurJoueur, valeur,
						casePrecedente, valeursDifferentsChemins, casesDejaVisitees);
			}
			/// si la case est occupée par un pion de l'adversaire
			else if (plateau.mapCases.get(caseAdjacente).pion == couleurJoueur) {
				valeur += 1;
				/// si la case est occupée par un pion du robot, rien
				/// on ajoute la valeur du chemin effectué
				valeursDifferentsChemins.add(valeur);

			}
			/// si la case est occupée par un pion "ami"
			else {
				valeur = 0;
			}
			}
		}

		/// envoi les différentes valeurs
		return valeursDifferentsChemins;
	}

	public static ArrayList<Integer> pionSurPlateau(Plateau plateau, int couleur) {
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
