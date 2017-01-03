import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TactiqueMange {

	public void tactiqueMange(Plateau plateau) {
		ArrayList<Integer> pionsRobot = pionSurPlateau(plateau, Pion.getCouleurRobot());
		ArrayList<Integer> pionsJoueur = pionSurPlateau(plateau, Pion.getCouleurJoueur());
		ArrayList<Coups> listeCoups = new ArrayList<Coups>();

		/// si le jeu est en mode pose
		if (plateau.mode == 1) {
			detecteSchema(plateau, pionsJoueur, listeCoups);
			detectePlacementStrategique(plateau, pionsJoueur, listeCoups);
			pourChaquePionMode1(plateau, pionsJoueur, listeCoups);
			deuxPionsAlignesMode1(plateau, pionsJoueur, listeCoups);
			moulinBloque(plateau, pionsRobot, listeCoups);
			pionBloqueOuvertureMoulin(plateau, pionsRobot, pionsJoueur, listeCoups);
		}
		/// si c'est un autre mode
		else {
			deuxPionsAlignes(plateau, pionsJoueur, listeCoups);
			moulinBloque(plateau, pionsRobot,listeCoups);
			pourChaquePionAutresModes(plateau, pionsJoueur, listeCoups);

		}

	}

	public void pionBloqueOuvertureMoulin(Plateau plateau, ArrayList<Integer>pionsRobot, ArrayList<Integer>pionsJoueur, ArrayList<Coups> listeCoups){
		/// si un pion bloque l'ouverture d'un moulin
		
		/// parcours tous les possibilit�s de moulins
		for(int[] moulin : Cases.listeMoulins){
			
		}
	}
	
	public void moulinBloque(Plateau plateau, ArrayList<Integer> pionsRobot, ArrayList<Coups> listeCoups) {
		// si deux pions robot sont align�s avec un troisi�me adversaire
		// qui bloque

		/// pour �viter de tester 2 fois le m�me moulin
		ArrayList<Integer> pionsDejaAnalyses = new ArrayList<Integer>();

		/// Pour chaque pions du robot
		for (int i = 0; i < pionsRobot.size(); i++) {
			/// on r�cup�re les potentiels moulins
			int[][] casesMoulins = plateau.mapCases.get(pionsRobot.get(i)).casesMoulins.clone();
			/// car 2 moulins diff�rents
			for (int j = 0; j < 2; j++) {
				/// pour �viter de retester un coup d�j� test�
				if (!pionsDejaAnalyses.contains(casesMoulins[j][0])
						&& !pionsDejaAnalyses.contains(casesMoulins[j][1])) {
					/// si la 2�me case est occup�e par un
					/// m�me pion
					/// et la 3�me est vide
					if (casesMoulins[j][0] == Pion.getCouleurJoueur()
							&& casesMoulins[j][1] == Pion.getCouleurRobot()) {
						/// cr�e le coup
						Coups coup = new Coups();
						coup.setValeur(1);
						///
						coup.setCoupCaseArrivee(casesMoulins[j][1]);
						/// ajoute le coup � listecoup
						listeCoups.add(coup);
					}
				}

				/// si la 2�me est vide et la 3�me est
				/// occup�e
				/// par un m�me pion
				if (casesMoulins[j][1] == Pion.getCouleurJoueur()
						&& casesMoulins[j][0] == Pion.getCouleurRobot()) {
					/// cr�e le coup
					Coups coup = new Coups();
					coup.setValeur(1);
					///
					coup.setCoupCaseArrivee(casesMoulins[j][0]);
					/// ajoute le coup � listecoup
					listeCoups.add(coup);
				}

			}
		}

	}

	public void deuxPionsAlignes(Plateau plateau, ArrayList<Integer> pionsJoueur, ArrayList<Coups> listeCoups) {
		/// si deux pions sont align�s

		/// une fois qu'un pion a �t� test�, on l'ajout lui et les pions
		/// formant les moulins dans cette liste pour pas faire 2 fois
		/// les moulins.
		ArrayList<Integer> pionsDejaAnalyses = new ArrayList<Integer>();

		/// pour chacun des pions du joueur
		for (int i = 0; i < pionsJoueur.size(); i++) {
			/// si deux pions sont align�s
			int[][] casesMoulins = plateau.mapCases.get(pionsJoueur.get(i)).casesMoulins.clone();
			/// car 2 moulins pour chaque case --> int[][]
			for (int j = 0; j < 2; j++) {

				/// pour �viter de retester un coup d�j� test�
				if (!pionsDejaAnalyses.contains(casesMoulins[j][0])
						&& !pionsDejaAnalyses.contains(casesMoulins[j][1])) {
					/// si la 2�me case est occup�e par un
					/// m�me pion
					/// et la 3�me est vide
					if (casesMoulins[j][0] == Pion.getCouleurJoueur()
							&& casesMoulins[j][1] == Pion.vide) {
						/// cr�e le coup
						Coups coup = new Coups();
						/// remonte les cases jusqu'�
						/// trouver un
						/// pion qui pourrai compl�ter
						/// le moulin
						ArrayList<Integer> valeursDifferentsChemins = new ArrayList<Integer>();
						int retour = remonteCase(plateau,
								plateau.mapCases.get(casesMoulins[j][1]).numero,
								Pion.getCouleurJoueur(), 0, -1,
								valeursDifferentsChemins);
						
						/// set les valeur de coup
						coup.setValeur(retour -1);
						coup.setCoupCaseArrivee(casesMoulins[j][0]);
						/// ajoute le coup � listecoup
						listeCoups.add(coup);
						Coups coup1 = new Coups();
						coup1.setValeur(retour -1);
						coup1.setCoupCaseArrivee(i);
						listeCoups.add(coup1);
					}
					/// si la 2�me est vide et la 3�me est
					/// occup�e
					/// par un m�me pion
					if (casesMoulins[j][1] == Pion.getCouleurJoueur()
							&& casesMoulins[j][0] == Pion.vide) {
						/// cr�e le coup
						Coups coup = new Coups();
						/// remonte les cases jusqu'�
						/// trouver un
						/// pion qui pourrai compl�ter
						/// le moulin
						ArrayList<Integer> valeursDifferentsChemins = new ArrayList<Integer>();
						int retour = remonteCase(plateau,
								plateau.mapCases.get(casesMoulins[j][1]).numero,
								Pion.getCouleurJoueur(), 0, -1,
								valeursDifferentsChemins);
						/// set les valeurs de coup
						coup.setValeur(retour - 1);
						coup.setCoupCaseArrivee(casesMoulins[j][1]);
						/// ajoute le coup � listecoup
						listeCoups.add(coup);
						Coups coup1 = new Coups();
						coup1.setValeur(retour - 1);
						coup1.setCoupCaseArrivee(i);
						listeCoups.add(coup1);
					}
					pionsDejaAnalyses.add(casesMoulins[j][0]);
					pionsDejaAnalyses.add(casesMoulins[j][0]);
				}
				pionsDejaAnalyses.add(i);
			}
		}
	}

	public void detecteSchema(Plateau plateau, ArrayList<Integer> pionsJoueur, ArrayList<Coups> listeCoups) {
		/// pour savoir si le coup est valid� ou non
		Boolean affirmatif = false;
		/// passe en revue tout les couples possibles de duo de cases
		/// pour un sch�ma
		/// parcours le tableau --> faire un dessin si jamais
		for (int pion1 = 0; pion1 < pionsJoueur.size() - 1; pion1++) {
			for (int pion2 = pion1 + 1; pion2 < pionsJoueur.size() - pion1; pion2++) {
				/// couple � tester
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
						/// si oui, on v�rifie que
						/// toutes les cases n�cessaires
						/// au schema soient libres
						int[] casesNecessairesSchema = OutilsTactiques.casesNecessairesSchemas[i]
								.clone();
						for (int j = 0; j < casesNecessairesSchema.length; j++) {
							/// si elle n'est pas
							/// vide, on
							/// abandonne tout
							if (plateau.mapCases.get(
									casesNecessairesSchema[j]).pion != Pion.vide) {
								affirmatif = false;
								break;
							} else {
								/// 1ere case
								/// bloque le
								/// schema
								affirmatif = true;
							}
						}
						/// si affirmatif est vrai,
						/// alors un sch�ma a �t�
						/// reconnu
						if (affirmatif) {
							/// les deux cases
							/// formant le sch�ma
							/// sont ajout�s dans
							/// listeCoups
							for (int pionSchema = 0; pionSchema < schema.length; pionSchema++) {
								Coups coup = new Coups();
								coup.setValeur(1);
								coup.setCoupCaseArrivee(schema[pionSchema]);
								listeCoups.add(coup);
							}
						}
					}
				}

			}
		}
	}

	public void detectePlacementStrategique(Plateau plateau, ArrayList<Integer> pionsJoueur,
			ArrayList<Coups> listeCoups) {
		/// pour savoir si le coup est valid� ou non
		Boolean affirmatif = false;
		/// passe en revue tout les couples possibles de duo de cases
		/// pour un sch�ma
		/// parcours le tableau --> faire un dessin si jamais
		for (int pion1 = 0; pion1 < pionsJoueur.size() - 1; pion1++) {
			for (int pion2 = pion1 + 1; pion2 < pionsJoueur.size() - pion1; pion2++) {
				/// couple � tester
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
					/// si les deux couples sont �gaux
					if (schema[0] == OutilsTactiques.placementsStrategiques[i][0]
							&& schema[1] == OutilsTactiques.placementsStrategiques[i][1]) {
						/// on copie les schema 1 et 2
						/// de niveau 1
						int[] casesPS11 = OutilsTactiques.casesNecessairesPS[2 * i].clone();
						int[] casesPS12 = OutilsTactiques.casesNecessairesPS[2 * i + 1].clone();
						/// on v�rifie que toutes les
						/// cases n�ces. au PS 1 du 1er
						/// niveau soient libres
						for (int casePS1 = 0; casePS1 < casesPS11.length; casePS1++) {
							if (plateau.mapCases
									.get(casesPS11[casePS1]).pion != Pion.vide) {
								/// pour tester
								/// le second
								/// niveau
								affirmatif = false;
								break;
							} else {
								affirmatif = true;
								/// les deux
								/// cases
								/// formant le
								/// sch�ma
								/// sont ajout�s
								/// dans
								/// listeCoups
								for (int pionSchema = 0; pionSchema < schema.length; pionSchema++) {
									Coups coup = new Coups();
									coup.setValeur(1);
									coup.setCoupCaseArrivee(schema[pionSchema]);
									listeCoups.add(coup);
								}
							}
						}
						if (affirmatif == false) {
							/// le 1er niveau n'est
							/// pas libre
							/// on teste le second
							/// niveau
							/// si la case centrale
							/// est occup�e et les
							/// deux autres libres
							if (plateau.mapCases.get(casesPS11[0]).pion == Pion
									.getCouleurJoueur()
									&& plateau.mapCases
											.get(casesPS11[1]).pion == Pion.vide
									&& plateau.mapCases.get(
											casesPS11[2]).pion == Pion.vide) {
								/// alors on
								/// ajoute le
								/// coup
								Coups coup = new Coups();
								coup.setValeur(1);
								coup.setCoupCaseArrivee(casesPS11[0]);
								listeCoups.add(coup);
							}

						}

						/// cases n�ces. au PS 2 du 1er
						/// niveau soient libres
						for (int casePS2 = 0; casePS2 < casesPS12.length; casePS2++) {
							if (plateau.mapCases
									.get(casesPS12[casePS2]).pion != Pion.vide) {
								/// pour tester
								/// le second
								/// niveau
								affirmatif = false;
								break;
							} else {
								affirmatif = true;
								/// les deux
								/// cases
								/// formant le
								/// sch�ma
								/// sont ajout�s
								/// dans
								/// listeCoups
								for (int pionSchema = 0; pionSchema < schema.length; pionSchema++) {
									Coups coup = new Coups();
									coup.setValeur(1);
									coup.setCoupCaseArrivee(schema[pionSchema]);
									listeCoups.add(coup);
								}
							}
						}
						if (affirmatif == false) {
							/// le 1er niveau n'est
							/// pas libre
							/// on teste le second
							/// niveau
							/// si la case centrale
							/// est occup�e et les
							/// deux autres libres
							if (plateau.mapCases.get(casesPS12[0]).pion == Pion
									.getCouleurJoueur()
									&& plateau.mapCases
											.get(casesPS12[1]).pion == Pion.vide
									&& plateau.mapCases.get(
											casesPS12[2]).pion == Pion.vide) {
								/// alors on
								/// ajoute le
								/// coup
								Coups coup = new Coups();
								coup.setValeur(1);
								coup.setCoupCaseArrivee(casesPS12[0]);
								listeCoups.add(coup);
							}

						}
					}
				}
			}
		}
	}

	public void pourChaquePionMode1(Plateau plateau, ArrayList<Integer> pionsJoueur, ArrayList<Coups> listeCoups) {
		/// pour chaque pion...
		for (int elements : pionsJoueur) {
			Coups coup = new Coups();
			coup.setValeur(2);
			coup.setCoupCaseArrivee(elements);
			listeCoups.add(coup);
		}
	}

	public void deuxPionsAlignesMode1(Plateau plateau, ArrayList<Integer> pionsJoueur,
			ArrayList<Coups> listeCoups) {
		/// si deux pions sont align�s

		/// une fois qu'un pion a �t� test�, on l'ajout lui et les pions
		/// formant les moulins dans cette liste pour pas faire 2 fois
		/// les moulins.
		ArrayList<Integer> pionsDejaAnalyses = new ArrayList<Integer>();

		/// pour chacun des pions du joueur
		for (int i = 0; i < pionsJoueur.size(); i++) {
			/// si deux pions sont align�s
			int[][] casesMoulins = plateau.mapCases.get(pionsJoueur.get(i)).casesMoulins.clone();
			/// car 2 moulins pour chaque case --> int[][]
			for (int j = 0; j < 2; j++) {

				/// pour �viter de retester un coup d�j� test�
				if (!pionsDejaAnalyses.contains(casesMoulins[j][0])
						&& !pionsDejaAnalyses.contains(casesMoulins[j][1])) {
					/// si la 2�me case est occup�e par un
					/// m�me pion
					/// et la 3�me est vide
					if (casesMoulins[j][0] == Pion.getCouleurJoueur()
							&& casesMoulins[j][1] == Pion.vide) {
						/// ajoute les pions du moulin
						/// comme
						/// coup
						Coups coup = new Coups();
						coup.setValeur(0);
						coup.setCoupCaseArrivee(casesMoulins[j][0]);
						listeCoups.add(coup);
						Coups coup2 = new Coups();
						coup2.setValeur(0);
						coup2.setCoupCaseArrivee(i);
						listeCoups.add(coup2);

					}
					/// si la 2�me est vide et la 3�me est
					/// occup�e
					/// par un m�me pion
					if (casesMoulins[j][1] == Pion.getCouleurJoueur()
							&& casesMoulins[j][0] == Pion.vide) {
						/// ajoute les pions du moulin
						/// comme
						/// coup
						for (int pionSchema = 0; pionSchema < casesMoulins[j].length; pionSchema++) {
							Coups coup = new Coups();
							coup.setValeur(0);
							coup.setCoupCaseArrivee(casesMoulins[j][1]);
							listeCoups.add(coup);
							Coups coup2 = new Coups();
							coup2.setValeur(0);
							coup2.setCoupCaseArrivee(i);
							listeCoups.add(coup2);
						}
					}
					pionsDejaAnalyses.add(casesMoulins[j][0]);
					pionsDejaAnalyses.add(casesMoulins[j][0]);
				}
				pionsDejaAnalyses.add(i);
			}
		}
	}

	public void pourChaquePionAutresModes(Plateau plateau, ArrayList<Integer>pionsJoueur, ArrayList<Coups>listeCoups){
		/// pour chaque pion
		int retour = 0;
		for(int element : pionsJoueur){
			ArrayList<Integer> valeursDifferentsChemins = new ArrayList<Integer>();
			retour = remonteCase(plateau, element, Pion.getCouleurJoueur(), 0, -1, valeursDifferentsChemins);
			Coups coup = new Coups();
			coup.setValeur(retour);
			coup.setCoupCaseArrivee(element);
		}
	}
	
	public static int hasard() {
		/// prend une case au hasard sur le plateau
		Random random = new Random();
		return random.nextInt(24) + 1;
	}

	/// ------------------------------- outils
	/// ------------------------------------

	public int remonteCase(Plateau plateau, int caseDepart, int couleurJoueur, int valeur, int casePrecedente,
			ArrayList<Integer> valeursDifferentsChemins) {
		/// !!! r�cursivit�

		casePrecedente = caseDepart;
		/// r�cup�re les cases adjacente � caseDepart
		ArrayList<Integer> casesAdjacentes = new ArrayList<Integer>();
		for (int element : plateau.mapCases.get(caseDepart).casesAdjacentes)
			casesAdjacentes.add(element);
		/// retire la case pr�c�demment test�e
		if (casePrecedente != -1) {
			for (int i = 0; i < casesAdjacentes.size(); i++) {
				if (casesAdjacentes.get(i) == casePrecedente)
					casesAdjacentes.remove(i);
			}
		}

		/// parcours toutes les cases adjacente � la case
		for (int caseAdjacente : casesAdjacentes) {
			/// si la case est vide, recommence l'op�ration
			if (plateau.mapCases.get(caseAdjacente).pion == Pion.vide) {
				valeur += 1;
				/// caseDepart devient caseAdjacente, casePrecedente a d�j� �t� chang�e
				remonteCase(plateau, caseAdjacente, couleurJoueur, valeur, casePrecedente, valeursDifferentsChemins);
			}
			/// si la case est occup�e par un pion de l'adversaire
			else if (plateau.mapCases.get(caseAdjacente).pion == couleurJoueur) {
				valeur += 1;

			}
			/// si la case est occup�e par un pion du robot, rien
			/// on ajoute la valeur du chemin effectu�
			valeursDifferentsChemins.add(valeur);
		}

		/// tri les diff�rentes valeur (pas tr�s propre comme code)
		int[] valeursChemins = new int[valeursDifferentsChemins.size()];
		for (int i = 0; i < valeursDifferentsChemins.size(); i++) {
			valeursChemins[i] = valeursDifferentsChemins.get(i);
		}
		Arrays.sort(valeursChemins);

		/// la valeur est la 1�re valeur de valeursChemins, la plus
		/// petite
		/// si valeur vaut -1, alors il n'y pas de pion pouvant
		/// atteindre la case
		return valeursChemins[0];
	}

	public ArrayList<Integer> pionSurPlateau(Plateau plateau, int couleur) {
		/// r�cup�re tous les pions d'une couleur pr�sents sur le
		/// plateau
		/// liste des pions
		ArrayList<Integer> pionPlateau = new ArrayList<Integer>();

		/// parcours toutes les cases
		for (int i = 1; i < 25; i++) {
			if (plateau.mapCases.get(i).pion == couleur)
				;
			pionPlateau.add(i);
		}

		return pionPlateau;
	}
}
