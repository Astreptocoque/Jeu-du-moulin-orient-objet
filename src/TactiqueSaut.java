import java.awt.LinearGradientPaint;
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
		if (coup == null) {
			coup = troisPionsSepares(plateau, coup, pionsRobot);
		}
		if (coup == null) {
			coup = troisPionsMoulin(plateau, coup, pionsRobot);
		}
		if (coup == null) {
			coup = hasard(plateau, coup, pionsRobot);
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
				Integer[] pionsMoulin = { moulin[0], moulin[1] };
				coup = deuxPionsAlignesCaseOccupeeSuite(plateau, pionsMoulin, pionsRobot, coup);

				/// si la 2ème case est occupée
			} else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {

				/// pour l'argument, les pions déjà dans le moulin
				Integer[] pionsMoulin = {( moulin[0]),moulin[2] };
				coup = deuxPionsAlignesCaseOccupeeSuite(plateau, pionsMoulin, pionsRobot, coup);

				/// si la 1ère case est occupée
			} else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {

				/// pour l'argument, les pions déjà dans le moulin
				Integer[] pionsMoulin = { moulin[1], moulin[2] };
				coup = deuxPionsAlignesCaseOccupeeSuite(plateau, pionsMoulin, pionsRobot, coup);
			}
		}

		return coup;
	}

	private static Coups troisPionsSepares(Plateau plateau, Coups coup, ArrayList<Integer> pionsRobot) {
		/// si les 3 pions sont sur des lignes différentes
		Random random = new Random();
		ArrayList<Integer> casesLignes = new ArrayList<Integer>();
		/// si les pions sont séparés
		Boolean pionsSepares = false;
		/// on regarde les lignes de chaque pion
		for (int pion : pionsRobot) {
			casesLignes.clear();
			/// les pions sur les lignes de pion
			casesLignes.add(plateau.mapCases.get(pion).casesMoulins[0][0]);
			casesLignes.add(plateau.mapCases.get(pion).casesMoulins[0][1]);
			casesLignes.add(plateau.mapCases.get(pion).casesMoulins[1][0]);
			casesLignes.add(plateau.mapCases.get(pion).casesMoulins[1][1]);

			/// on vérifie que chaque pions soit vide
			for (int caseLigne : casesLignes) {
				if (plateau.mapCases.get(caseLigne).pion != Pion.getCouleurRobot()) {
					pionsSepares = true;
				} else {
					pionsSepares = false;
					break;
				}
			}
			/// pour quitter la boucle
			if (!pionsSepares)
				break;
		}
		/// si pionsSepares = true, alors les pions sont séparés
		if (pionsSepares == true) {
			/// on regarde s'ils bloquent des moulins
			ArrayList<Integer> nombrePionsBloques = new ArrayList<Integer>();

			/// on parcourt tous les moulins pour savoir le nombre de pions bloqués
			for (int[] moulin : Cases.listeMoulins) {
				/// si la 3ème case est vide
				if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
						&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
						&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {
					/// si il y a un pion a coté de la case du joueur
					Boolean bloque = false;
					for (int caseAdjacente : plateau.mapCases.get(moulin[2]).getCasesAdjacentes()) {
						if (plateau.mapCases.get(caseAdjacente).pion == Pion
								.getCouleurJoueur()) {
							bloque = true;
						}
					}
					/// si bloque = true, alors le pion bloque un moulin
					if (bloque) {
						nombrePionsBloques.add(moulin[2]);
					}
				} else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
						&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
						&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
					/// si il y a un pion a coté de la case du joueur
					Boolean bloque = false;
					for (int caseAdjacente : plateau.mapCases.get(moulin[1]).getCasesAdjacentes()) {
						if (plateau.mapCases.get(caseAdjacente).pion == Pion
								.getCouleurJoueur()) {
							bloque = true;
						}
					}
					/// si bloque = true, alors le pion bloque un moulin
					if (bloque) {
						nombrePionsBloques.add(moulin[1]);
					}
				} else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
						&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
						&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
					/// si il y a un pion a coté de la case du joueur
					Boolean bloque = false;
					for (int caseAdjacente : plateau.mapCases.get(moulin[0]).getCasesAdjacentes()) {
						if (plateau.mapCases.get(caseAdjacente).pion == Pion
								.getCouleurJoueur()) {
							bloque = true;
						}
					}
					/// si bloque = true, alors le pion bloque un moulin
					if (bloque) {
						nombrePionsBloques.add(moulin[0]);
					}
				}
			}

			/// maintenant on regarde le nbr de pion bloqués
			/// si aucun
			if (nombrePionsBloques.isEmpty()) {

			}
			/// si 1
			else if (nombrePionsBloques.size() == 1) {
				/// à corriger
				/// on bouge le troisième pion à côté d'un autre pion sur une ligne
				/// libre
				/// d'abord on identifie le pion
				int pionSeulBloque = nombrePionsBloques.get(0);
				int[] pionsSeuls = new int[2];
				for (int pion = 0; pion < pionsRobot.size(); pion++) {
					if (!pionsRobot.contains(pion)) {
						if (pion == 0) {
							pionsSeuls[0] = pionsRobot.get(1);
							pionsSeuls[1] = pionsRobot.get(2);
						} else if (pion == 1) {
							pionsSeuls[0] = pionsRobot.get(0);
							pionsSeuls[1] = pionsRobot.get(2);
						} else if (pion == 2) {
							pionsSeuls[0] = pionsRobot.get(0);
							pionsSeuls[1] = pionsRobot.get(1);
						}
					}
				}
				coup  = new Coups();
				coup.setCoupCaseDepart(pionsSeuls[random.nextInt(pionsSeuls.length)]);
				coup.setValeur(0);
				int caseArrivee = 0;
				/// on regarde si l'adversaire peut faire un moulin
				for (int[] moulin : Cases.listeMoulins) {
					if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
							&& plateau.mapCases.get(moulin[1]).pion == Pion
									.getCouleurJoueur()
							&& plateau.mapCases.get(moulin[2]).pion == Pion.vide) {
						/// si il y a un pion a coté de la case du joueur
						Boolean bloque = false;
						for (int caseAdjacente : plateau.mapCases.get(moulin[2])
								.getCasesAdjacentes()) {
							if (plateau.mapCases.get(caseAdjacente).pion == Pion
									.getCouleurJoueur()) {
								bloque = true;
							}
						}
						/// si bloque = true, alors le pion bloque un moulin
						if (bloque) {
							caseArrivee = moulin[2];
							break;
						}

					} else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
							&& plateau.mapCases.get(moulin[1]).pion == Pion.vide
							&& plateau.mapCases.get(moulin[2]).pion == Pion
									.getCouleurJoueur()) {
						/// si il y a un pion a coté de la case du joueur
						Boolean bloque = false;
						for (int caseAdjacente : plateau.mapCases.get(moulin[1])
								.getCasesAdjacentes()) {
							if (plateau.mapCases.get(caseAdjacente).pion == Pion
									.getCouleurJoueur()) {
								bloque = true;
							}
						}
						/// si bloque = true, alors le pion bloque un moulin
						if (bloque) {
							caseArrivee = moulin[1];
							break;
						}

					} else if (plateau.mapCases.get(moulin[0]).pion == Pion.vide
							&& plateau.mapCases.get(moulin[1]).pion == Pion
									.getCouleurJoueur()
							&& plateau.mapCases.get(moulin[2]).pion == Pion
									.getCouleurJoueur()) {
						/// si il y a un pion a coté de la case du joueur
						Boolean bloque = false;
						for (int caseAdjacente : plateau.mapCases.get(moulin[0])
								.getCasesAdjacentes()) {
							if (plateau.mapCases.get(caseAdjacente).pion == Pion
									.getCouleurJoueur()) {
								bloque = true;
							}
						}
						/// si bloque = true, alors le pion bloque un moulin
						if (bloque) {
							caseArrivee = moulin[0];
							break;
						}
					}
				}

				/// si on peut bloquer un moulin
				if (caseArrivee != 0) {
					coup.setCoupCaseArrivee(caseArrivee);
				}
				/// sinon on met un des deux pions à côté de l'autre
				else {
					ArrayList<Integer> casesArriveePossibles = new ArrayList<Integer>();

					/// pour pionsSeuls
					for (int pion = 0; pion < pionsSeuls.length; pion++) {
						casesLignes.clear();
						int[] ligne = new int[2];
						/// première ligne
						ligne = plateau.mapCases.get(pion).casesMoulins[0].clone();
						/// si elle est vide
						if (plateau.mapCases.get(ligne[0]).pion == Pion.vide
								&& plateau.mapCases.get(ligne[1]).pion == Pion.vide) {
							casesArriveePossibles.add(ligne[0]);
							casesArriveePossibles.add(ligne[1]);
						}
						/// seconde ligne
						ligne = plateau.mapCases.get(pion).casesMoulins[1].clone();
						/// si elle est vide
						if (plateau.mapCases.get(ligne[0]).pion == Pion.vide
								&& plateau.mapCases.get(ligne[1]).pion == Pion.vide) {
							casesArriveePossibles.add(ligne[0]);
							casesArriveePossibles.add(ligne[1]);
						}

						/// si casesArriveePossibles n'est pas vide
						if (!casesArriveePossibles.isEmpty()) {
							/// on prend une case au hasard, les pions
							/// seront 2 à coté
							caseArrivee = casesArriveePossibles.get(
									random.nextInt(casesArriveePossibles.size()));
							break;
						}
					}

					/// si caseArrivee = 0, aucune ligne n'est libre vers un des
					/// deux pions
					if (caseArrivee == 0) {
						/// on cherche une ligne libre n'importe ou
						for (int[] moulin : Cases.listeMoulins) {
							if (plateau.mapCases.get(moulin[0]).pion == Pion.vide
									&& plateau.mapCases
											.get(moulin[1]).pion == Pion.vide
									&& plateau.mapCases.get(
											moulin[2]).pion == Pion.vide) {
								casesArriveePossibles.add(moulin[0]);
								casesArriveePossibles.add(moulin[1]);
								casesArriveePossibles.add(moulin[2]);
							}
						}
						/// si il y a des lignes libres
						if (!casesArriveePossibles.isEmpty()) {
							caseArrivee = casesArriveePossibles.get(
									random.nextInt(casesArriveePossibles.size()));
						}
						/// sinon au hasard dans une case libre
						else {
							for (int i = 1; i < 25; i++) {
								if (plateau.mapCases.get(i).pion == Pion.vide) {
									casesArriveePossibles.add(i);
								}
							}
							caseArrivee = casesArriveePossibles.get(
									random.nextInt(casesArriveePossibles.size()));
						}
					}

					coup.setCoupCaseArrivee(caseArrivee);
					coup.setCoupCaseArrivee(caseArrivee);
				}
				/// si on a un premier coup, alors on fait pas le second
			}

			/// si 2
			else if (nombrePionsBloques.size() == 2)

			{
				ArrayList<Integer> casesArriveePossibles = new ArrayList<Integer>();
				/// on bouge le troisième pion à côté d'un autre pion sur une ligne
				/// libre
				/// d'abord on identifie le pion
				int pionSeul = 0;
				for (Integer pion : pionsRobot) {
					if (!nombrePionsBloques.contains(pion)) {
						pionSeul = pion;
					}
				}
				coup = new Coups();
				coup.setCoupCaseDepart(pionSeul);
				coup.setValeur(0);

				/// ensuite on regarde si on peut le bouger à côté d'un autre pion
				/// sur une ligne libre
				int[] ligne = new int[2];
				/// on regarde les lignes des deux pions
				for (int pion : nombrePionsBloques) {
					/// première ligne
					ligne = plateau.mapCases.get(pion).casesMoulins[0].clone();
					/// si elle est vide
					if (plateau.mapCases.get(ligne[0]).pion == Pion.vide
							&& plateau.mapCases.get(ligne[1]).pion == Pion.vide) {
						casesArriveePossibles.add(ligne[0]);
						casesArriveePossibles.add(ligne[1]);
					}
					/// seconde ligne
					ligne = plateau.mapCases.get(pion).casesMoulins[1].clone();
					/// si elle est vide
					if (plateau.mapCases.get(ligne[0]).pion == Pion.vide
							&& plateau.mapCases.get(ligne[1]).pion == Pion.vide) {
						casesArriveePossibles.add(ligne[0]);
						casesArriveePossibles.add(ligne[1]);
					}
				}
				int caseArrivee = 0;
				/// si casesArriveePossibles n'est pas vide
				if (!casesArriveePossibles.isEmpty()) {
					/// on prend une case au hasard, les pions seront 2 à coté
					caseArrivee = casesArriveePossibles
							.get(random.nextInt(casesArriveePossibles.size()));
				}
				/// sinon
				else {
					/// on cherche une ligne libre n'importe ou
					for (int[] moulin : Cases.listeMoulins) {
						if (plateau.mapCases.get(moulin[0]).pion == Pion.vide
								&& plateau.mapCases.get(moulin[1]).pion == Pion.vide
								&& plateau.mapCases.get(moulin[2]).pion == Pion.vide) {
							casesArriveePossibles.add(moulin[0]);
							casesArriveePossibles.add(moulin[1]);
							casesArriveePossibles.add(moulin[2]);
						}
					}
					/// si il y a des lignes libres
					if (!casesArriveePossibles.isEmpty()) {
						caseArrivee = casesArriveePossibles
								.get(random.nextInt(casesArriveePossibles.size()));
					}
					/// sinon au hasard dans une case libre
					else {
						for (int i = 1; i < 25; i++) {
							if (plateau.mapCases.get(i).pion == Pion.vide) {
								casesArriveePossibles.add(i);
							}
						}
						caseArrivee = casesArriveePossibles
								.get(random.nextInt(casesArriveePossibles.size()));
					}
				}
				coup.setCoupCaseArrivee(caseArrivee);
			}
			/// si 3
			else if (nombrePionsBloques.size() == 3) {
				/// la partie est perdue
				/// on prend n'importe lequel qu'on bouge n'importe ou
				int caseDepart = nombrePionsBloques.get(random.nextInt(nombrePionsBloques.size()));
				/// pour l'arrivee on prend une case libre
				ArrayList<Integer> casesLibres = new ArrayList<Integer>();
				for (int i = 1; i < 25; i++) {
					if (plateau.mapCases.get(i).pion == Pion.vide) {
						casesLibres.add(i);
					}
				}
				int caseArrivee = casesLibres.get(random.nextInt(casesLibres.size()));
				/// set le coup
				coup = new Coups();
				coup.setCoupCaseArrivee(caseArrivee);
				coup.setCoupCaseDepart(caseDepart);
				coup.setValeur(0);
			}

		}

		return coup;

	}

	private static Coups troisPionsMoulin(Plateau plateau, Coups coup, ArrayList<Integer> pionsRobot) {
		/// si les trois pions forment un moulin
		Random random = new Random();

		/// parcours tous les possibilités de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// s'ils sont en moulin
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {
				/// on vérifie qu'en bougeant un pion, aucun adverse ne peut aller
				/// sur la case
				int caseDepart = 0;
				ArrayList<Integer> pionsPossibles = new ArrayList<Integer>();
				/// donc pour chaque pion du moulin
				for (int pion : moulin) {
					/// pour chacune des cases adjacentes au pion
					int[] casesAdjacentes = plateau.mapCases.get(pion).getCasesAdjacentes();
					for (int caseAdjacente : casesAdjacentes) {
						/// si il y a un pion du joueur
						if (plateau.mapCases.get(caseAdjacente).pion == Pion
								.getCouleurJoueur()) {
							caseDepart = 0;
							break;
						} else {
							caseDepart = pion;
						}
					}
					/// s'il n'y a pas de pions adverse
					if (caseDepart != 0) {
						pionsPossibles.add(pion);
					}
				}
				/// s'il n'y a aucun pion, on en prend un au hasard
				if (pionsPossibles.isEmpty()) {
					/// on prend un pion au hasard
					caseDepart = pionsRobot.get(random.nextInt(pionsRobot.size()));
					coup = new Coups();
					coup.setCoupCaseDepart(caseDepart);
					/// on choisit la case arrivée
					coup = troisPionsMoulinCaseArrivee(plateau, coup);

					/// sinon on prend un pion au hasard dans les possibles
				} else {
					caseDepart = pionsPossibles.get(random.nextInt(pionsPossibles.size()));
					coup = new Coups();
					coup.setCoupCaseDepart(caseDepart);
					/// on choisit la case d'arrivée
					coup = troisPionsMoulinCaseArrivee(plateau, coup);
				}

			}
		}

		return coup;
	}

	private static Coups hasard(Plateau plateau, Coups coup, ArrayList<Integer> pionsRobot) {
		/// prend une case au hasard sur le plateau
		Random random = new Random();
		int caseDepart = pionsRobot.get(random.nextInt(pionsRobot.size()));
		ArrayList<Integer> casesLibres = new ArrayList<Integer>();
		for (int i = 1; i < 25; i++) {
			if (plateau.mapCases.get(i).pion == Pion.vide) {
				casesLibres.add(i);
			}
		}
		Collections.shuffle(casesLibres);
		int caseArrivee = casesLibres.get(random.nextInt(casesLibres.size()));

		coup.setCoupCaseArrivee(caseArrivee);
		coup.setCoupCaseDepart(caseDepart);
		coup.setValeur(0);

		return coup;
	}

	//// ------------------------------------------------------------

	private static Coups troisPionsMoulinCaseArrivee(Plateau plateau, Coups coup) {
		/// d'abord on vérifie si le pion peut bloquer un moulin adverse
		int caseArrivee = 0;
		Random random = new Random();
		/// pour un peu de hasard
		ArrayList<int[]> listeMoulins = new ArrayList<int[]>();
		for (int[] moulin : Cases.listeMoulins) {
			listeMoulins.add(moulin);
		}
		Collections.shuffle(listeMoulins);

		/// on parcourt les moulins
		for (int[] moulin : listeMoulins) {
			/// si y'a effectivement des moulins
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.vide) {
				/// si il y a un pion a coté de la case du joueur
				Boolean bloque = false;
				for (int caseAdjacente : plateau.mapCases.get(moulin[2]).getCasesAdjacentes()) {
					if (plateau.mapCases.get(caseAdjacente).pion == Pion.getCouleurJoueur()) {
						bloque = true;
					}
				}
				/// si bloque = true, alors le pion bloque un moulin
				if (bloque) {
					caseArrivee = moulin[2];
					break;
				}
			} else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// si il y a un pion a coté de la case du joueur
				Boolean bloque = false;
				for (int caseAdjacente : plateau.mapCases.get(moulin[1]).getCasesAdjacentes()) {
					if (plateau.mapCases.get(caseAdjacente).pion == Pion.getCouleurJoueur()) {
						bloque = true;
					}
				}
				/// si bloque = true, alors le pion bloque un moulin
				if (bloque) {
					caseArrivee = moulin[1];
					break;
				}
			} else if (plateau.mapCases.get(moulin[0]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// si il y a un pion a coté de la case du joueur
				Boolean bloque = false;
				for (int caseAdjacente : plateau.mapCases.get(moulin[0]).getCasesAdjacentes()) {
					if (plateau.mapCases.get(caseAdjacente).pion == Pion.getCouleurJoueur()) {
						bloque = true;
					}
				}
				/// si bloque = true, alors le pion bloque un moulin
				if (bloque) {
					caseArrivee = moulin[0];
					break;
				}
			}
		}
		/// si caseArrive vaut 0, alors il n'y a pas de pré-moulin
		if (caseArrivee == 0) {
			/// on choisit alors une case, d'abord sur une ligne libre
			Collections.shuffle(listeMoulins);
			ArrayList<Integer> casesPossibles = new ArrayList<Integer>();
			/// on cherche la ligne libre
			/// on parcourt les moulins
			for (int[] moulin : listeMoulins) {
				/// s'il y en a une
				if (plateau.mapCases.get(moulin[0]).pion == Pion.vide
						&& plateau.mapCases.get(moulin[1]).pion == Pion.vide
						&& plateau.mapCases.get(moulin[2]).pion == Pion.vide) {
					/// on prend au hasard une case de cette ligne
					casesPossibles.add(moulin[0]);
					casesPossibles.add(moulin[1]);
					casesPossibles.add(moulin[2]);
				}
			}
			/// si casesPossibles est vide, alors il n'y a pas de lignes libre
			if (casesPossibles.isEmpty()) {
				/// on choisit une case au hasard
				ArrayList<Integer> casesLibres = new ArrayList<Integer>();
				/// on parcourt les cases
				for (int i = 1; i < 25; i++) {
					if (plateau.mapCases.get(i).pion == Pion.vide) {
						casesLibres.add(i);
					}
				}
				Collections.shuffle(casesLibres);
				caseArrivee = casesLibres.get(random.nextInt(casesLibres.size()));
			}
			/// sinon
			else {
				Collections.shuffle(casesPossibles);
				caseArrivee = casesPossibles.get(random.nextInt(casesPossibles.size()));
			}

			/// set le coup
			coup.setCoupCaseArrivee(caseArrivee);
			coup.setValeur(0);
		}

		return coup;
	}

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

	private static Coups deuxPionsAlignesCaseOccupeeSuite(Plateau plateau, Integer[] pionsMoulin,
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
			if (bloqueMoulin(plateau, pionsRobotCopie)) {
				/// on prend un pion au hasard dans pionsRobotCopie
				caseDepart = pionsRobotCopie.get(random.nextInt(pionsRobotCopie.size()));
				/// pour la case arrivée on cherche si possible une ligne libre
				/// pour un peu de hasard
				ArrayList<int[]> listeLignes = new ArrayList<int[]>();
				for (int[] ligne : Cases.listeMoulins) {
					listeLignes.add(ligne);
				}
				Collections.shuffle(listeLignes);

				for (int[] ligne : listeLignes) {
					if (plateau.mapCases.get(ligne[0]).pion == Pion.vide
							&& plateau.mapCases.get(ligne[1]).pion == Pion.vide
							&& plateau.mapCases.get(ligne[2]).pion == Pion.vide) {
						caseArrivee = ligne[random.nextInt(ligne.length)];
						break;
					}
				}
				/// si aucune ligne n'est libre
				if (caseArrivee == 0) {
					/// on choisit au hasard parmis les cases libres
					/// passe en revue toute les cases libres du plateau
					ArrayList<Integer> casesLibres = new ArrayList<Integer>();
					for (int i = 1; i < 25; i++) {
						if (plateau.mapCases.get(i).pion == Pion.vide) {
							casesLibres.add(i);
						}
					}
					/// car de toute façon la partie est perdue
					caseArrivee = casesLibres.get(random.nextInt(casesLibres.size()));
				}
			}
			/// sinon, ça signifie que tous les pions bloquent un moulin
			else {
				/// et on bouge un pion au hasard
				caseDepart = pionsRobot.get(random.nextInt(pionsRobot.size()));
				/// pour la case arrivée
				/// passe en revue toute les cases libres du plateau
				ArrayList<Integer> casesLibres = new ArrayList<Integer>();
				for (int i = 1; i < 25; i++) {
					if (plateau.mapCases.get(i).pion == Pion.vide) {
						casesLibres.add(i);
					}
				}
				/// car de toute façon la partie est perdue
				caseArrivee = casesLibres.get(random.nextInt(casesLibres.size()));
			}

			coup = new Coups(caseDepart, 0, caseArrivee);
		}

		return coup;
	}

	private static Boolean bloqueMoulin(Plateau plateau, ArrayList<Integer> pionsRobotCopie) {
		Boolean affirmatif = true;
		/// parcours tous les possibilités de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3ème case
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {
				/// si il y a un pion a coté de la case du joueur
				Boolean bloque = false;
				for (int caseAdjacente : plateau.mapCases.get(moulin[2]).getCasesAdjacentes()) {
					if (plateau.mapCases.get(caseAdjacente).pion == Pion.getCouleurJoueur()) {
						bloque = true;
					}
				}
				/// si bloque = true, alors le pion bloque un moulin
				if (bloque) {
					/// on supprime le pion
					Integer pion = moulin[2];
					pionsRobotCopie.remove(pion);
				}

			}
			/// si la 2ème case
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// si il y a un pion a coté de la case du joueur
				Boolean bloque = false;
				for (int caseAdjacente : plateau.mapCases.get(moulin[1]).getCasesAdjacentes()) {
					if (plateau.mapCases.get(caseAdjacente).pion == Pion.getCouleurJoueur()) {
						bloque = true;
					}
				}
				/// si bloque = true, alors le pion bloque un moulin
				if (bloque) {
					/// on supprime le pion
					Integer pion = moulin[1];
					pionsRobotCopie.remove(pion);
				}
			}
			/// si la 1ère case
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// si il y a un pion a coté de la case du joueur
				Boolean bloque = false;
				for (int caseAdjacente : plateau.mapCases.get(moulin[0]).getCasesAdjacentes()) {
					if (plateau.mapCases.get(caseAdjacente).pion == Pion.getCouleurJoueur()) {
						bloque = true;
					}
				}
				/// si bloque = true, alors le pion bloque un moulin
				if (bloque) {
					/// on supprime le pion
					Integer pion = moulin[0];
					pionsRobotCopie.remove(pion);
				}
			}
		}

		if (pionsRobotCopie.isEmpty()) {
			affirmatif = false;
		}

		return affirmatif;
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
