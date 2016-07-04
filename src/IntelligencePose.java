import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class IntelligencePose extends Outils {

	private Random random = new Random();
	private int caseChoisie;

	public int intelligencePose(Plateau plateau) {
		boolean testSuivant = false;
		int hasard;
		/// regarde si le robot peut faire un moulin testSuivant =
		testSuivant = poseEffectueMoulinDirect(plateau);
		/// si non, v�rifie s'il peut bloquer un moulin
		if (testSuivant) {
			LCD.refresh();
			LCD.drawString("Pose : 2", 0, 0);
			LCD.drawInt(caseChoisie, 0, 1);
			testSuivant = poseBloqueMoulinDirect(plateau);
		}
		if (yatilUnPion(plateau)) {
			hasard = random.nextInt(5);
			if (testSuivant && hasard > 3) {
				LCD.refresh();
				LCD.drawString("Pose : 5", 6, 0);
				LCD.drawInt(caseChoisie, 0, 1);
				testSuivant = poseEffectueMoulinIndirect(plateau);
			}
		}

		/// si non, il regarde sinle joueur fait un sch�ma

		/// si non, fait un schema
		if (yatilUnPion(plateau)) {
			if (testSuivant) {
				hasard = random.nextInt(2);
				if (hasard == 0) {
					LCD.refresh();
					LCD.drawString("Pose : 4", 6, 0);
					LCD.drawInt(caseChoisie, 0, 1);
					testSuivant = poseEffectueSchema(plateau);
				} else {
					LCD.refresh();
					LCD.drawString("Pose : 3", 0, 0);
					LCD.drawInt(caseChoisie, 0, 1);
					testSuivant = poseBloqueSchema(plateau);

				}
			}
		} /// si non, pose strat�giquement if (testSuivant) {

		if (testSuivant) {
			LCD.refresh();
			LCD.drawString("Pose : 6", 0, 0);
			testSuivant = poseStrategique(plateau);
		}
		/// en dernier recours, au hasard
		if (testSuivant) {
			LCD.refresh();
			LCD.drawString("Pose : 7", 0, 0);
			LCD.drawInt(caseChoisie, 0, 1);

			/// on n'est pas sens� arriver ici, /// mais c'est une
			/// s�curit�
			hasard();
		}
		return caseChoisie;
	}

	/// ******** m�thode pour l'intelligence pose***************
	public void hasard() {
		/// prend une case au hasard sur le plateau
		caseChoisie = random.nextInt(24) + 1;
	}

	private boolean poseEffectueMoulinDirect(Plateau plateau) {

		boolean affirmatif = true;
		/// copie l'original
		int[] ligneMoulin = new int[3];
		/// pour v�rifier si 2 pions sont sur une ligne
		int nbrPionsMoulin = 0;

		/// passe en revue toutes les lignes
		for (int i = 0; i < listeMoulins.length; i++) {
			/// copie le moulin a tester
			ligneMoulin = listeMoulins[i].clone();

			/// r�initialise la variable pour chaque tour
			nbrPionsMoulin = 0;
			/// regarde chaque case du possible moulin
			for (int j = 0; j < ligneMoulin.length; j++) {
				/// s'il y a un pion de l'adversaire sur la
				/// case, incr�mentation
				if (plateau.tabCases[ligneMoulin[j]].pion == Pion.getCouleurRobot()) {
					nbrPionsMoulin++;
				}
				/// si il y a un pion du joueur, ce n'est pas
				/// possible de faire un moulin
				else if (plateau.tabCases[ligneMoulin[j]].pion == Pion.getCouleurJoueur()) {
					/// pour emp�cher la comparaison
					/// suivante
					nbrPionsMoulin = -1;
					break;
				} else {
					/// si l'on arrive � ce cas, c'est que
					/// c'est oblig� que la caseChoisie soit
					/// celle-ci
					caseChoisie = ligneMoulin[j];
					affirmatif = true;
				}
			}
			/// si le total de pion du robot est �gal � 2
			/// c'est qu'il peut faire un moulin
			if (nbrPionsMoulin == 2) {
				affirmatif = false;
				break;
			}
		}
		return affirmatif;

	}

	private boolean poseBloqueMoulinDirect(Plateau plateau) {

		boolean affirmatif = true;
		/// copie l'original
		int[] ligneMoulin = new int[3];
		/// pour v�rifier si 2 pions sont sur une ligne
		int nbrPionsMoulin = 0;

		/// passe en revue toutes les lignes
		for (int i = 0; i < listeMoulins.length; i++) {
			/// copie le moulin a tester
			ligneMoulin = listeMoulins[i].clone();

			/// r�initialise la variable pour chaque tour
			nbrPionsMoulin = 0;

			/// regarde chaque case du possible moulin
			for (int j = 0; j < ligneMoulin.length; j++) {
				/// s'il y a un pion de l'adversaire sur la
				/// case, incr�mentation
				if (plateau.tabCases[ligneMoulin[j]].pion == Pion.getCouleurJoueur()) {
					nbrPionsMoulin++;
				}
				/// si il y a un pion du joueur, ce n'est pas
				/// possible de faire un moulin
				else if (plateau.tabCases[ligneMoulin[j]].pion == Pion.getCouleurRobot()) {
					/// pour emp�cher la comparaison
					/// suivante
					nbrPionsMoulin = -1;
					break;
				} else {
					/// si l'on arrive � ce cas, c'est que
					/// c'est oblig� que la caseChoisie soit
					/// celle-ci
					caseChoisie = ligneMoulin[j];
					affirmatif = true;
				}
			}
			/// si le total de pion du robot est �gal � 2
			/// c'est qu'il peut faire un moulin
			if (nbrPionsMoulin == 2) {
				affirmatif = false;
				break;
			}
		}

		return affirmatif;
	}

	private boolean poseBloqueSchema(Plateau plateau) {

		boolean affirmatif = true;

		/// enregiste toutes les cases adveres
		ArrayList<Integer> pionsJoueur = new ArrayList<Integer>();
		/// tab qui sera compar� aux tableaux de casesSchemas
		int[] tabVerifie = new int[2];
		tabVerifie[0] = Pion.getDerniereCaseJoueur();
		/// tab qui prend les cl�s de sch�ma, pour tester
		int[] tabSchemaCles = new int[2];
		/// tab qui prend les valeurs de sch�ma
		int[] tabSchemaValeurs = new int[3];

		/// r�cup�re tout les pions adverse sur le plateau
		for (int i = 0; i < 24; i++) {
			if (plateau.tabCases[i + 1].pion == Pion.getCouleurJoueur()) {
				pionsJoueur.add(i + 1);
			}
		}

		/// v�rifie maintenant si chacune de ces cases forme un sch�ma
		for (int i = 0; i < pionsJoueur.size(); i++) {
			/// ajoute la case testee au tableau verifie
			tabVerifie[1] = pionsJoueur.get(i);
			/// trie du plus petit au plus grand, pour la recherche
			/// dans casesSchemas
			Arrays.sort(tabVerifie);
			/// cherche la correspondance dans casesSchemas
			for (int j = 0; j < casesSchemas.length; j++) {
				tabSchemaCles = casesSchemas[j].clone();
				/// regarde si les deux tab sont pareils
				if (tabSchemaCles[0] == tabVerifie[0] && tabSchemaCles[1] == tabVerifie[1]) {
					/// si oui, on verifie que le schema
					/// puisse �tre fait donc on v�rifie que
					/// toutes les casesmvaleurs soient
					/// libres
					tabSchemaValeurs = casesNecessairesSchemas[j].clone();

					for (int k = 0; k < tabSchemaValeurs.length; k++) {
						/// si elle n'est pas vide, on
						/// abandonne tout
						if (plateau.tabCases[tabSchemaValeurs[k]].pion != Pion.vide) {
							affirmatif = true;
							break;
						} else {
							/// 1ere case bloque le
							/// schema
							caseChoisie = tabSchemaValeurs[0];
							affirmatif = false;
						}
					}
				}
			}
		}

		return affirmatif;
	}

	private boolean poseEffectueSchema(Plateau plateau) {

		boolean affirmatif = true;
		/// V�rifie d'abord si un sch�ma peut �tre termin�
		/// c�d qu'il n'y a qu'� poser la case "intersection"

		/// enregiste toutes les cases du robot
		ArrayList<Integer> pionsRobot = new ArrayList<Integer>();
		/// copie les tableau
		int[] tabSchemaCles = new int[2];
		/// copie les valeurs
		int[] tabSchemaValeurs = new int[3];

		/// r�cup�re tout les pions du robot sur le plateau
		for (int i = 0; i < 24; i++) {
			if (plateau.tabCases[i + 1].pion == Pion.getCouleurRobot()) {
				pionsRobot.add(i + 1);
			}
		}

		/// regarde tous les sch�mas possible et v�rifie s'il est
		/// possible d'en completer un (c�d de poser le pion central)
		for (int i = 0; i < casesSchemas.length; i++) {
			tabSchemaCles = casesSchemas[i].clone();
			/// si les cl�s du sch�mas sont sur le plateau
			if (pionsRobot.contains(tabSchemaCles[0]) && pionsRobot.contains(tabSchemaCles[1])) {
				/// si oui, v�rifie que les cases du sch�mas
				/// soient libres
				tabSchemaValeurs = casesNecessairesSchemas[i].clone();

				for (int j = 0; j < tabSchemaValeurs.length; j++) {
					/// si une case n'est pas libre, on
					/// abandonne direct
					if (plateau.tabCases[tabSchemaValeurs[j]].pion != Pion.vide) {
						affirmatif = true;
						break;
					} else {
						affirmatif = false;
						caseChoisie = tabSchemaValeurs[0];
					}
				}
				if (affirmatif == false) {
					break;
				}
			}
		}

		/// si affirmatif = true, c'est qu'il n'y avait pas de sch�ma a
		/// terminer. Ici, on commence alors un schema, � partir d'une
		/// case d�j� sur le plateau

		if (affirmatif == true) {
			LCD.clear(0);
			LCD.drawString("Pose : 4,5", 6, 0);

			ArrayList<Integer> schemaValeursTestee = new ArrayList<Integer>();

			/// on prend le premier schema de la liste qui est ok
			for (int i = 0; i < casesSchemas.length; i++) {
				tabSchemaCles = casesSchemas[i].clone();
				/// si le sch�ma contient une case sur le
				/// plateau
				if (pionsRobot.contains(tabSchemaCles[0]) || pionsRobot.contains(tabSchemaCles[1])) {

					/// ajoute les valeurs dans la liste
					for (int elements : casesNecessairesSchemas[i]) {
						schemaValeursTestee.add(elements);
					}

					/// ajoute la case cl� du sch�ma qui
					/// n'est pas l� dans les cases
					/// tabSchemaValeurs, qui sont � test�
					/// si elles sont vides
					if (plateau.tabCases[tabSchemaCles[0]].pion == Pion.getCouleurRobot()) {
						schemaValeursTestee.add(tabSchemaCles[1]);
					} else {
						schemaValeursTestee.add(tabSchemaCles[0]);
					}

					/// v�rifie que toutes les cases soient
					/// libres
					for (int j = 0; j < schemaValeursTestee.size(); j++) {
						/// si une case n'est pas libre,
						/// on abandonne direct
						if (plateau.tabCases[schemaValeursTestee.get(j)].pion != Pion.vide) {
							affirmatif = true;
							break;
						} else {
							affirmatif = false;
							/// prend la case
							/// completant le schema
							if (pionsRobot.contains(tabSchemaCles[0])) {
								caseChoisie = tabSchemaCles[1];
							} else {
								caseChoisie = tabSchemaCles[0];
							}

						}
					}

					if (affirmatif == false) {

						break;
					}
				}
			}
		}

		return affirmatif;
	}

	private boolean poseStrategique(Plateau plateau) {

		boolean affirmatif = true;
		/// pose un pion sur une case avec le max d'intersection
		/// condition: le pions ne doit pas �tre bloqu� par les pions
		/// adverses

		/// la case potentiellement libre actuellement test�e
		int indexCase;
		/// cases adjacentes
		ArrayList<Integer> casesAdjacentes = new ArrayList<Integer>();
		///
		/// les cases potentiellement libres
		ArrayList<Integer> casesPossibles = new ArrayList<Integer>();
		int tour = random.nextInt(2);
		do {

			if (tour == 0) {
				for (int i = 0; i < cases4intersections.length; i++) {
					casesPossibles.add(cases4intersections[i]);
				}
			} else {
				for (int i = 0; i < cases3intersections.length; i++) {
					casesPossibles.add(cases3intersections[i]);
				}
			}

			do {
				/// prend au hasard dans les cases
				indexCase = random.nextInt(casesPossibles.size());

				for (int e : listeCasesAdjacentes[casesPossibles.get(indexCase)]) {
					casesAdjacentes.add(e);
				}
				/// verifie que la case ne soit pas bloqu�e
				for (int i = 0; i < casesAdjacentes.size(); i++) {
					if (plateau.tabCases[casesAdjacentes.get(i)].pion == Pion.vide) {
						caseChoisie = casesPossibles.get(indexCase);
						affirmatif = false;
					} else {
						affirmatif = true;
						break;
					}
				}
				/// enl�ve la case test�e
				casesPossibles.remove(indexCase);
			} while (casesPossibles.size() > 0 && affirmatif == true);
			tour++;
		} while (tour < 3 && affirmatif == true);
		return affirmatif;
	}

	private boolean poseEffectueMoulinIndirect(Plateau plateau) {

		boolean affirmatif = true;

		int indicePionsRobot = 0;

		int[] tabMoulin = new int[3];

		int nbrPionsOk = 0;

		int[] casesMoulins = new int[3];

		/// enregiste toutes les pions du robot actuellement sur le
		/// plateau
		ArrayList<Integer> pionsRobot = new ArrayList<Integer>();
		/// r�cup�re tout les cases ou il y a un pion du robot sur le
		/// plateau
		for (int i = 0; i < 24; i++) {
			if (plateau.tabCases[i + 1].pion == Pion.getCouleurRobot()) {
				pionsRobot.add(i + 1);
			}
		}

		Collections.shuffle(pionsRobot);

		do {

			/// prend au hasard une case dans les pions d�j� sur le
			/// plateau
			/// ajoute du hasard dans le jeu, pour ne pas prendre le
			/// premier
			/// moulin disponible dans la liste.
			// indicePionsRobot = random.nextInt(pionsRobot.size());

			/// cherche une possibilit� de moulin
			for (int i = 0; i < listeMoulins.length; i++) {
				/// copie le moulin � tester
				tabMoulin = listeMoulins[i].clone();
				/// indice pour choisir plus tard une case
				nbrPionsOk = 0;
				/// verifie que la ligne test�e soit libre pour
				/// faire un
				/// moulin

				for (int j = 0; j < tabMoulin.length; j++) {
					/// si la case est vide
					if (plateau.tabCases[tabMoulin[j]].pion == Pion.vide) {
						nbrPionsOk += 2;
						if (casesMoulins[0] == 0)
							casesMoulins[0] = tabMoulin[j];
						else
							casesMoulins[1] = tabMoulin[j];

						/// si la case est la m�me que
						/// celle tir�e au hasard
					} else if (tabMoulin[j] == pionsRobot.get(indicePionsRobot)) {
						nbrPionsOk += 1;
					} else {
						affirmatif = true;
						break;
					}

				}

				if (nbrPionsOk == 5) {
					affirmatif = false;
					caseChoisie = casesMoulins[random.nextInt(2)];

					break;
				}
			}
			pionsRobot.remove(indicePionsRobot);
			indicePionsRobot++;
		} while (pionsRobot.size() > 0 && affirmatif == true);
		return affirmatif;
	}

	private boolean yatilUnPion(Plateau plateau) {
		boolean ok;
		/// prend tous les pions du robot qui sont sur le plateau
		ArrayList<Integer> nbrPionsRobot = new ArrayList<Integer>();

		for (int i = 0; i <24; i++) {
			if (plateau.tabCases[i + 1].pion == Pion.getCouleurRobot()) {
				nbrPionsRobot.add(plateau.tabCases[i + 1].pion);
			}
		}

		/// regarde maintenant si il y a des pions
		if (nbrPionsRobot.size() == 0) {
			ok = false;
		} else {
			ok = true;
		}
		return ok;
	}
}
