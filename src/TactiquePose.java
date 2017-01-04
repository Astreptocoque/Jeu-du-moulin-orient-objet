import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class TactiquePose extends OutilsTactiques {

	private static Random random = new Random();
	private static int caseChoisie = 0;

	public static Coups tactiquePose(Plateau plateau) {
		boolean testSuivant = false;
		int hasard = 0;
		/// regarde si le robot peut faire un moulin testSuivant =
		testSuivant = poseEffectueMoulinDirect(plateau);
		/// si non, v�rifie s'il peut bloquer un moulin
		if (testSuivant) {
			testSuivant = poseBloqueMoulinDirect(plateau);
		}
		
		if (plateau.getNbrPionsSurLePlateau(Pion.getCouleurRobot()) > 0){
			if (testSuivant) {
				testSuivant = poseBloqueSchema(plateau);
			}
			
			/// hasard entre les deux suivants
			hasard = random.nextInt(2);
			
			if(testSuivant && hasard == 0){
				testSuivant = poseEffectueMoulinIndirect(plateau);
			}
			else if(testSuivant && hasard == 1){
				testSuivant = poseEffectueSchema(plateau);
			}
			
			if(testSuivant && hasard == 0){
				testSuivant = poseEffectueSchema(plateau);
			}
			else if(testSuivant && hasard == 1){
				testSuivant = poseEffectueMoulinIndirect(plateau);
			}
		}
		
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

			/// on n'est pas sens� arriver ici, 
			/// mais c'est une s�curit�
			hasard();
		}
		
		/// met le case choisie dans un coup
		Coups coup = new Coups(0, 0, caseChoisie);
		return coup;
	}

	/// ******** m�thode pour l'intelligence pose***************
	public static void hasard() {
		/// prend une case au hasard sur le plateau
		caseChoisie = random.nextInt(24) + 1;
	}

	private static boolean poseEffectueMoulinDirect(Plateau plateau) {

		boolean affirmatif = true;
		/// copie l'original
		int[] ligneMoulin = new int[3];
		/// pour v�rifier si 2 pions sont sur une ligne
		int nbrPionsMoulin = 0;

		/// ************** avant ********************

		/// passe en revue toutes les lignes
		for (int i = 0; i < Cases.listeMoulins.length; i++) {
			/// copie le moulin a tester
			ligneMoulin = Cases.listeMoulins[i].clone();

			/// r�initialise la variable pour chaque tour
			nbrPionsMoulin = 0;
			/// regarde chaque case du possible moulin
			for (int j = 0; j < ligneMoulin.length; j++) {
				/// s'il y a un pion de l'adversaire sur la
				/// case, incr�mentation
				if (plateau.mapCases.get(ligneMoulin[j]).pion == Pion.getCouleurRobot()) {
					nbrPionsMoulin++;
				}
				/// si il y a un pion du joueur, ce n'est pas
				/// possible de faire un moulin
				else if (plateau.mapCases.get(ligneMoulin[j]).pion == Pion.getCouleurJoueur()) {
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

	private static boolean poseBloqueMoulinDirect(Plateau plateau) {

		boolean affirmatif = true;
		/// copie l'original
		int[] ligneMoulin = new int[3];
		/// pour v�rifier si 2 pions sont sur une ligne
		int nbrPionsMoulin = 0;

		/// passe en revue toutes les lignes
		for (int i = 0; i < Cases.listeMoulins.length; i++) {
			/// copie le moulin a tester
			ligneMoulin = Cases.listeMoulins[i].clone();

			/// r�initialise la variable pour chaque tour
			nbrPionsMoulin = 0;

			/// regarde chaque case du possible moulin
			for (int j = 0; j < ligneMoulin.length; j++) {
				/// s'il y a un pion de l'adversaire sur la
				/// case, incr�mentation
				if (plateau.mapCases.get(ligneMoulin[j]).pion == Pion.getCouleurJoueur()) {
					nbrPionsMoulin++;
				}
				/// si il y a un pion du joueur, ce n'est pas
				/// possible de faire un moulin
				else if (plateau.mapCases.get(ligneMoulin[j]).pion == Pion.getCouleurRobot()) {
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

	private static boolean poseBloqueSchema(Plateau plateau) {

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
			if (plateau.mapCases.get(i + 1).pion == Pion.getCouleurJoueur()) {
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
						if (plateau.mapCases.get(tabSchemaValeurs[k]).pion != Pion.vide) {
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

	private static boolean poseEffectueSchema(Plateau plateau) {

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
			if (plateau.mapCases.get(i + 1).pion == Pion.getCouleurRobot()) {
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
					if (plateau.mapCases.get(tabSchemaValeurs[j]).pion != Pion.vide) {
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
					if (plateau.mapCases.get(tabSchemaCles[0]).pion == Pion.getCouleurRobot()) {
						schemaValeursTestee.add(tabSchemaCles[1]);
					} else {
						schemaValeursTestee.add(tabSchemaCles[0]);
					}

					/// v�rifie que toutes les cases soient
					/// libres
					for (int j = 0; j < schemaValeursTestee.size(); j++) {
						/// si une case n'est pas libre,
						/// on abandonne direct
						if (plateau.mapCases
								.get(schemaValeursTestee.get(j)).pion != Pion.vide) {
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

	private static boolean poseStrategique(Plateau plateau) {

		boolean affirmatif = true;
		/// pose un pion sur une case avec le max d'intersection
		/// condition: le pions ne doit pas �tre bloqu� par les pions
		/// adverses

		/// la case potentiellement libre actuellement test�e
		int indexCase;
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
				int casePossible = casesPossibles.get(indexCase);

				/// verifie que la case ne soit pas bloqu�e
				for (int i = 0; i < plateau.mapCases.get(casePossible).casesAdjacentes.size(); i++) {
					if (plateau.mapCases.get(plateau.mapCases.get(casePossible).casesAdjacentes
							.get(i)).pion == Pion.vide) {
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

	private static boolean poseEffectueMoulinIndirect(Plateau plateau) {

		boolean affirmatif = true;

		/// enregiste toutes les pions du robot actuellement sur le
		/// plateau
		ArrayList<Integer> pionsRobot = new ArrayList<Integer>();
		/// r�cup�re tout les cases ou il y a un pion du robot sur le
		/// plateau
		for (int i = 1; i < 25; i++) {
			if (plateau.mapCases.get(i).pion == Pion.getCouleurRobot()) {
				pionsRobot.add(i);
			}
		}

		Collections.shuffle(pionsRobot);

		int[] moulin1;
		int[] moulin2;
		int hasard;

		/// passe les pions m�lang�s en revue
		for (int i = 0; i < pionsRobot.size(); i++) {
			moulin1 = plateau.mapCases.get(pionsRobot.get(i)).casesMoulins[0].clone();
			moulin2 = plateau.mapCases.get(pionsRobot.get(i)).casesMoulins[1].clone();

			hasard = random.nextInt(2);
			/// pour choisir au hasard le moulin
			for (int j = 0; j < 2; j++) {
				if (hasard == 0) {
					if (plateau.mapCases.get(moulin1[0]).pion == Pion.vide
							&& plateau.mapCases.get(moulin1[1]).pion == Pion.vide) {
						affirmatif = false;
						caseChoisie = moulin1[random.nextInt(2)];
						break;
					}
				} else {
					if (plateau.mapCases.get(moulin2[0]).pion == Pion.vide
							&& plateau.mapCases.get(moulin2[1]).pion == Pion.vide) {
						affirmatif = false;
						caseChoisie = moulin2[random.nextInt(2)];
						break;
					}
				}
				hasard = (hasard == 0 ? 1 : 0);
			}
		}
		return affirmatif;
	}

}
