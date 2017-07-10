import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class TactiqueGlisse {

	public static Coups tactiqueGlisse(Plateau plateau) {
		ArrayList<Integer> pionsRobot = pionSurPlateau(plateau, Pion.getCouleurRobot());
		ArrayList<Integer> pionsJoueur = pionSurPlateau(plateau, Pion.getCouleurJoueur());
		/// listeCoupsAttenteValeurX
		ArrayList<Coups> listeCoupsAV2 = verifiePionsRobotBloques(plateau, pionsRobot);
		ArrayList<Coups> listeCoupsAV1 = verifiePionsAdversaireBloques(plateau, pionsJoueur);
		ArrayList<Coups> listeCoupsAV3 = verifiePionBloqueMoulinAdversaire(plateau);
		ArrayList<Coups> listeCoups = new ArrayList<Coups>();

		LCD.clear();
		LCD.drawString("tactiqueGlisse", 0, 0);

		/// analyse des coups
		deuxPionsRobotCaseLibre(plateau, listeCoups, listeCoupsAV1, listeCoupsAV2, listeCoupsAV3);
		deuxPionsAdversaireCaseLibre(plateau, listeCoups, listeCoupsAV1, listeCoupsAV2, listeCoupsAV3);
		robotMoulin(plateau, listeCoups, listeCoupsAV1, listeCoupsAV2, listeCoupsAV3);
		chaquePion(plateau, listeCoups, pionsRobot, listeCoupsAV1, listeCoupsAV2, listeCoupsAV3);

		/// on m�lange la liste
		Collections.shuffle(listeCoups);
		/// et on prend le premier coup le plus bas
		Coups coupChoisi;

		coupChoisi = choisiMeilleurCoup(listeCoups);

		if (coupChoisi == null) {
			/// on vide listeCoup pour recommence avec les listeCoupsAV
			listeCoups.clear();
			/// on passe en revue listeCoupAV1
			for (Coups coup : listeCoupsAV1) {
				/// si le coup � d�j� une case d'arriv�e
				if (coup.getCoupCaseArrivee() != 0) {
					listeCoups.add(coup);
				}
				/// s'il n'en a pas
				else {
					/// on lui attribue toute les cases adjacente comme
					/// case d'arriv�e
					int[] casesAdjacentes = plateau.mapCases.get(coup.getCoupCaseDepart())
							.getCasesAdjacentes();
					for (int caseAdjacente : casesAdjacentes) {
						/// si la case est libre
						if (plateau.mapCases.get(caseAdjacente).pion == Pion.vide) {
							coup.setCoupCaseArrivee(caseAdjacente);
							listeCoups.add(coup);
						}
					}

				}
			}
			/// on passe en revue listeCoupAV2
			for (Coups coup : listeCoupsAV2) {
				/// si le coup � d�j� une case d'arriv�e
				if (coup.getCoupCaseArrivee() != 0) {
					listeCoups.add(coup);
				}
				/// s'il n'en a pas
				else {
					/// on lui attribue toute les cases adjacente comme
					/// case d'arriv�e
					int[] casesAdjacentes = plateau.mapCases.get(coup.getCoupCaseDepart())
							.getCasesAdjacentes();
					for (int caseAdjacente : casesAdjacentes) {
						/// si la case est libre
						if (plateau.mapCases.get(caseAdjacente).pion == Pion.vide) {
							coup.setCoupCaseArrivee(caseAdjacente);
							listeCoups.add(coup);
						}
					}

				}
			}
			/// on passe en revue listeCoupAV3
			for (Coups coup : listeCoupsAV3) {
				/// si le coup � d�j� une case d'arriv�e
				if (coup.getCoupCaseArrivee() != 0) {
					listeCoups.add(coup);
				}
				/// s'il n'en a pas
				else {
					/// on lui attribue toute les cases adjacente comme
					/// case d'arriv�e
					int[] casesAdjacentes = plateau.mapCases.get(coup.getCoupCaseDepart())
							.getCasesAdjacentes();
					for (int caseAdjacente : casesAdjacentes) {
						/// si la case est libre
						if (plateau.mapCases.get(caseAdjacente).pion == Pion.vide) {
							coup.setCoupCaseArrivee(caseAdjacente);
							listeCoups.add(coup);
						}
					}

				}
			}
		}
		/// on rer�p�te avec listecoup qui a les listeAV
		coupChoisi = choisiMeilleurCoup(listeCoups);

		LCD.clear();
		LCD.drawString("glissade", 0, 0);
		LCD.drawString("coup" + coupChoisi.getCoupCaseDepart(), 0, 1);
		return coupChoisi;
	}

	public static Coups choisiMeilleurCoup(ArrayList<Coups> listeCoups) {
		Boolean affirmatif = true;
		int valeur = 0;
		int nombreTests = 0;
		Coups coupChoisi = null;

		Collections.shuffle(listeCoups);

		/// on rep�te le d�filement de liste coup avec chaque fois une valeur plus �lev�e
		while (affirmatif) {
			for (int i = 0; i < listeCoups.size(); i++) {
				/// s'il y a un coup de la valeur voulue
				if (listeCoups.get(i).getValeur() == valeur) {
					/// pour sortir de la boucle
					affirmatif = false;
					/// on prend le coup
					coupChoisi = listeCoups.get(i);
					break;
				} else {
					/// pour sortir de la boucle une fois
					nombreTests += 1;
				}

			}
			/// si affirmatif = true, c'est qu'aucun coup n'a �t�
			/// trouv�
			if (affirmatif == true) {
				/// on essaye avec la valeur du dessus
				valeur += 1;
				nombreTests = 0;
			}
			/// si le nbr Test = listeCoup, c'est qu'on a tout essay�.
			/// on reprend les listeCoupAttente.
			if (nombreTests == listeCoups.size()) {
				///
				affirmatif = false;

			}
		}

		return coupChoisi;
	}

	public static void deuxPionsRobotCaseLibre(Plateau plateau, ArrayList<Coups> listeCoups,
			ArrayList<Coups> listeCoupsAV1, ArrayList<Coups> listeCoupsAV2,
			ArrayList<Coups> listeCoupsAV3) {
		// si deux pions du robot sont align�s et la 3�me case est vide
		int modeGlisse = 1;

		/// parcours tous les possibilit�s de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3�me case est vide
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.vide) {

				/// remonte les cases jusqu'a trouver un pion
				/// qui pourrai compl�ter le moulin
				ArrayList<Coups> valeursDifferentsChemins = new ArrayList<Coups>();
				ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();

				valeursDifferentsChemins = remonteCase(plateau, moulin[2], Pion.getCouleurRobot(), 0,
						-1, valeursDifferentsChemins, casesDejaVisitees);
				/// en argument de deuxPionCaseLibreOptimiser
				/// pour pas compter les pion du moulin dans un coup
				int[] casesMoulin = { moulin[0], moulin[1] };
				/// on s'occupe de valeursDifferentsChemins
				deuxPionsCaseLibreOptimiser(plateau, valeursDifferentsChemins, listeCoups,
						listeCoupsAV1, listeCoupsAV2, listeCoupsAV3, casesMoulin, modeGlisse);

			}
			/// si la 2�me case est vide
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {

				/// remonte les cases jusqu'a trouver un pion
				/// qui pourrai compl�ter le moulin
				ArrayList<Coups> valeursDifferentsChemins = new ArrayList<Coups>();
				ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();

				valeursDifferentsChemins = remonteCase(plateau, moulin[1], Pion.getCouleurRobot(), 0,
						-1, valeursDifferentsChemins, casesDejaVisitees);
				/// en argument de deuxPionCaseLibreOptimiser
				/// pour pas compter les pion du moulin dans un coup
				int[] casesMoulin = { moulin[0], moulin[2] };
				/// on s'occupe de valeursDifferentsChemins
				deuxPionsCaseLibreOptimiser(plateau, valeursDifferentsChemins, listeCoups,
						listeCoupsAV1, listeCoupsAV2, listeCoupsAV3, casesMoulin, modeGlisse);

			}
			/// si la 1�re case est vide
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {

				/// remonte les cases jusqu'a trouver un pion
				/// qui pourrai compl�ter le moulin
				ArrayList<Coups> valeursDifferentsChemins = new ArrayList<Coups>();
				ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();

				valeursDifferentsChemins = remonteCase(plateau, moulin[0], Pion.getCouleurRobot(), 0,
						-1, valeursDifferentsChemins, casesDejaVisitees);
				/// en argument de deuxPionCaseLibreOptimiser
				/// pour pas compter les pion du moulin dans un coup
				int[] casesMoulin = { moulin[1], moulin[2] };
				/// on s'occupe de valeursDifferentsChemins
				deuxPionsCaseLibreOptimiser(plateau, valeursDifferentsChemins, listeCoups,
						listeCoupsAV1, listeCoupsAV2, listeCoupsAV3, casesMoulin, modeGlisse);
			}
		}
	}

	public static void deuxPionsAdversaireCaseLibre(Plateau plateau, ArrayList<Coups> listeCoups,
			ArrayList<Coups> listeCoupsAV1, ArrayList<Coups> listeCoupsAV2,
			ArrayList<Coups> listeCoupsAV3) {
		// si deux pions du joueur sont align�s et la 3�me case est vide
		int modeGlisse = 2;

		/// parcours tous les possibilit�s de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3�me case est vide
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.vide) {

				/// remonte les cases jusqu'a trouver un pion
				/// qui pourrai compl�ter le moulin
				ArrayList<Coups> valeursDifferentsChemins = new ArrayList<Coups>();
				ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();

				valeursDifferentsChemins = remonteCase(plateau, moulin[2], Pion.getCouleurRobot(), 0,
						-1, valeursDifferentsChemins, casesDejaVisitees);
				/// en argument de deuxPionCaseLibreOptimiser
				/// pour pas compter les pion du moulin dans un coup
				int[] casesMoulin = { moulin[0], moulin[1] };
				/// on s'occupe de valeursDifferentsChemins
				deuxPionsCaseLibreOptimiser(plateau, valeursDifferentsChemins, listeCoups,
						listeCoupsAV1, listeCoupsAV2, listeCoupsAV3, casesMoulin, modeGlisse);

			}
			/// si la 2�me case est vide
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {

				/// remonte les cases jusqu'a trouver un pion
				/// qui pourrai compl�ter le moulin
				ArrayList<Coups> valeursDifferentsChemins = new ArrayList<Coups>();
				ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();

				valeursDifferentsChemins = remonteCase(plateau, moulin[1], Pion.getCouleurRobot(), 0,
						-1, valeursDifferentsChemins, casesDejaVisitees);
				/// en argument de deuxPionCaseLibreOptimiser
				/// pour pas compter les pion du moulin dans un coup
				int[] casesMoulin = { moulin[0], moulin[2] };
				/// on s'occupe de valeursDifferentsChemins
				deuxPionsCaseLibreOptimiser(plateau, valeursDifferentsChemins, listeCoups,
						listeCoupsAV1, listeCoupsAV2, listeCoupsAV3, casesMoulin, modeGlisse);

			}
			/// si la 1�re case est vide
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.vide
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {

				/// remonte les cases jusqu'a trouver un pion
				/// qui pourrai compl�ter le moulin
				ArrayList<Coups> valeursDifferentsChemins = new ArrayList<Coups>();
				ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();

				valeursDifferentsChemins = remonteCase(plateau, moulin[0], Pion.getCouleurRobot(), 0,
						-1, valeursDifferentsChemins, casesDejaVisitees);
				/// en argument de deuxPionCaseLibreOptimiser
				/// pour pas compter les pion du moulin dans un coup
				int[] casesMoulin = { moulin[1], moulin[2] };
				/// on s'occupe de valeursDifferentsChemins
				deuxPionsCaseLibreOptimiser(plateau, valeursDifferentsChemins, listeCoups,
						listeCoupsAV1, listeCoupsAV2, listeCoupsAV3, casesMoulin, modeGlisse);
			}
		}
	}

	public static void robotMoulin(Plateau plateau, ArrayList<Coups> listeCoups, ArrayList<Coups> listeCoupsAV1,
			ArrayList<Coups> listeCoupsAV2, ArrayList<Coups> listeCoupsAV3) {
		/// si le robot a des moulins
		int modeGlisse = 3;

		/// parcours tous les possibilit�s de moulins
		for (int[] moulin : Cases.listeMoulins) {
			/// s'il y a un moulin
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {
				/// on passe en revue chacune des cases
				for (int caseMoulin : moulin) {
					/// on regarde si les cases adjacentes sont soit libres soit
					/// occup� par un pion ami
					int[] casesAdjacentes = plateau.mapCases.get(caseMoulin).getCasesAdjacentes();
					ArrayList<Integer> pionsRobot = new ArrayList<Integer>();
					ArrayList<Integer> pionsJoueur = new ArrayList<Integer>();
					ArrayList<Integer> casesLibres = new ArrayList<Integer>();
					for (int caseAdjacente : casesAdjacentes) {
						if (plateau.mapCases.get(caseAdjacente).pion == Pion.getCouleurRobot())
							pionsRobot.add(caseAdjacente);
						else if (plateau.mapCases.get(caseAdjacente).pion == Pion
								.getCouleurJoueur())
							pionsJoueur.add(caseAdjacente);
						else
							casesLibres.add(caseAdjacente);
					}
					/// on compare les r�sultats
					/// s'il n'y a aucun pion adverse
					if (pionsJoueur.isEmpty()) {
						/// et s'il y a des cases libres
						if (!casesLibres.isEmpty()) {
							/// on ajoute les coups
							for (int caseLibre : casesLibres) {
								/// seulement s'il n'est pas dans
								/// les listeCoupAV
								Coups coup = new Coups(caseMoulin, 1, caseLibre);
								listeCoups.add(coup);
								
							}
						}
					}
					/// si il y a des pions adverses
					else {
						/// mais aussi des cases libres
						if (!casesLibres.isEmpty()) {
							/// on ajoute les coups
							for (int caseLibre : casesLibres) {
								/// seulement s'il n'est pas dans
								/// les listeCoupAV
								Coups coup = new Coups(caseMoulin, 1, caseLibre);
								if (verifieExclusion(plateau, coup, listeCoupsAV1,
										listeCoupsAV2, listeCoupsAV3))
									listeCoupsAV2.add(coup);
							}
						}
					}

				}

			}
		}
	}

	public static void chaquePion(Plateau plateau, ArrayList<Coups> listeCoups, ArrayList<Integer> pionsRobot,
			ArrayList<Coups> listeCoupsAV1, ArrayList<Coups> listeCoupsAV2,
			ArrayList<Coups> listeCoupsAV3) {
		int modeGlisse = 4;

		/// prend les pions supprimer de pionsRobotCopie pour des comparaisons
		ArrayList<Integer> pionsSupprimes = new ArrayList<Integer>();
		/// pour ne pas modifier pionsRobot lors des suppressions.
		ArrayList<Integer> pionsRobotCopie = new ArrayList<Integer>();
		/// copie les pions
		for (int element : pionsRobot) {
			pionsRobotCopie.add(element);
		}

		/// nbr de d�placement pour qu'un pion du robot rejoigne un autre
		/// pour chaque pion :
		for (int pion : pionsRobot) {
			/// on v�rifie si il y a un pion ami adjacent
			/// dans ce cas le pion ne sera pas analys�
			/// il sera mis dans listeCoupAV1
			int[] casesAdjacentes = plateau.mapCases.get(pion).getCasesAdjacentes();
			for (int caseAdjacente : casesAdjacentes) {
				/// si le pion adjacent est aussi un pion robot
				if (plateau.mapCases.get(caseAdjacente).pion == Pion.getCouleurRobot()) {
					/// on ajoute le pionDepart � listeCoupAttente
					Coups coup = new Coups();
					/// la case d'arriv�e est attribu�e plus tard
					coup.setCoupCaseDepart(pion);
					coup.setValeur(1);
					if (verifieExclusion(plateau, coup, listeCoupsAV1, listeCoupsAV2,
							listeCoupsAV3))
						listeCoupsAV1.add(coup);
					pionsSupprimes.add(pion);
					break;
				}

			}
		}
		/// on supprime les pions qui ne doivent pas �tre analys�s
		for (Integer pion : pionsSupprimes) {
			pionsRobotCopie.remove(pion);
		}
		/// et maintenant on remonte dans les cases
		ArrayList<Coups> differentsChemins = new ArrayList<Coups>();
		ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();

		for (int pion : pionsRobotCopie) {
			differentsChemins.clear();
			casesDejaVisitees.clear();
			differentsChemins = remonteCase(plateau, pion, Pion.getCouleurRobot(), 0, -1, differentsChemins,
					casesDejaVisitees);
			/// � cause des m�thodes pr�c�dente, on met 0 pour ne pas d�ranger
			int[] casesMoulin = { 0, 0 };
			/// on met de l'ordre dans les diff�rents chemins
			deuxPionsCaseLibreOptimiser(plateau, differentsChemins, listeCoups, listeCoupsAV1,
					listeCoupsAV2, listeCoupsAV3, casesMoulin, modeGlisse);
		}

	}

	public static void creationCoupValeurAttente(Plateau plateau) {
		/// non en fait il faut cr�er les coup directement dans la cr�atin des listes. On
		/// regarde toute les directions que peut prendre chaque pion et on l'ajoute comme
		/// coup

	}

	/// --------------------------------------------------------------------------

	public static void deuxPionsCaseLibreOptimiser(Plateau plateau, ArrayList<Coups> valeursDifferentsChemins,
			ArrayList<Coups> listeCoups, ArrayList<Coups> listeCoupsAV1, ArrayList<Coups> listeCoupsAV2,
			ArrayList<Coups> listeCoupsAV3, int[] casesMoulin, int modeGlisse) {
		/// pour les 3 r�p�titions dans deuxPionsRobotCaseLibre et dans
		/// deuxPionJoueurCaseLibre et pour chaqueCoup

		if (!valeursDifferentsChemins.isEmpty()) {
			/// on supprime les coups comprenant les cases du moulin
			ArrayList<Coups> differentsCoups = new ArrayList<Coups>();
			for (Coups coup : valeursDifferentsChemins) {
				if (coup.getCoupCaseDepart() != casesMoulin[0]
						&& coup.getCoupCaseDepart() != casesMoulin[1]) {
					differentsCoups.add(coup);
				}
			}

			/// on m�lange la liste (� la place d'un tri)
			Collections.shuffle(differentsCoups);

			/// et on prend le premier coup le plus bas
			Boolean affirmatif = true;
			int valeur = 0;
			ArrayList<Coups> coupsPotentielsRobot = new ArrayList<Coups>();
			while (affirmatif) {
				for (int i = 0; i < differentsCoups.size(); i++) {
					/// s'il y a un coup de la valeur voulue
					if (differentsCoups.get(i).getValeur() == valeur) {
						/// pour sortir de la boucle
						affirmatif = false;
						/// si c'est un pion du robot
						if (plateau.mapCases.get(
								differentsCoups.get(i).getCoupCaseDepart()).pion == Pion
										.getCouleurRobot()) {
							/// on ajoute le coup potentiel
							coupsPotentielsRobot.add(differentsCoups.get(i));
						}
					}
				}
				/// si affirmatif = true, c'est qu'aucun coup n'a
				/// �t�
				/// trouv�
				if (affirmatif == true) {
					/// on essaye avec la valeur du dessus
					valeur += 1;
				}
			}

			/// si coupsPR contient n'est pas vide, alors le robot
			/// est soit le plus proche, soit � �gale distance avec
			/// le joueur
			if (!coupsPotentielsRobot.isEmpty()) {
				for (Coups coup : coupsPotentielsRobot) {
					/// on v�rifie les exclusions uniquement en fonction du
					/// modeGlisse
					if (modeGlisse == 1 || modeGlisse == 2) {
						/// exclusion 3
						Boolean ok = true;
						for (Coups test : listeCoupsAV3) {
							if (test.getCoupCaseDepart() == coup.getCoupCaseDepart())
								ok = false;
						}
						if (ok)
							/// si c'est modeGlisse 1, on regarde ceci :
							/// si la valeur = 1, c'est � dire que le robot
							/// peut faire un moulin, on la met � 0
							if(modeGlisse == 1){
								if(coup.getValeur() == 1)
									coup.setValeur(0);
							}
							listeCoups.add(coup);
					}
					/// pour les autre mode
					else {
						/// seulement si pas dans listeCoupsAV
						if (verifieExclusion(plateau, coup, listeCoupsAV1, listeCoupsAV2,
								listeCoupsAV3)) {
							listeCoups.add(coup);
						}
					}

				}
			}

		}
	}

	public static ArrayList<Coups> verifiePionsRobotBloques(Plateau plateau, ArrayList<Integer> pionRobot) {
		/// si le pion est bloqu�, alors ValeurDifferentsChemins doit �tre vide
		ArrayList<Coups> differentsChemins = new ArrayList<Coups>();
		/// la liste des coups en attente
		ArrayList<Coups> listeCoupsAV2 = new ArrayList<Coups>();
		/// lors du remonteCase, pour ne pas tourner en rond
		ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();

		/// on passe en revue tout les pions du robot
		for (int pion : pionRobot) {
			/// on remet � z�ro les compteurs
			casesDejaVisitees.clear();
			differentsChemins.clear();

			differentsChemins = remonteCaseVerifie(plateau, pion, Pion.getCouleurRobot(), -1,
					differentsChemins, casesDejaVisitees);

			/// si c'est vide, alors le pion est "bloqu�"
			if (differentsChemins.isEmpty()) {
				/// on ajoute le coup
				Coups coup = new Coups();
				coup.setCoupCaseDepart(pion);
				coup.setValeur(2);
				listeCoupsAV2.add(coup);

			}
		}

		return listeCoupsAV2;

	}

	public static ArrayList<Coups> verifiePionsAdversaireBloques(Plateau plateau, ArrayList<Integer> pionsJoueur) {
		/// si le pion est bloqu�, alors ValeurDifferentsChemins doit �tre vide
		ArrayList<Coups> differentsChemins = new ArrayList<Coups>();
		/// la liste des coups en attente
		ArrayList<Coups> listeCoupsAV1 = new ArrayList<Coups>();
		ArrayList<Integer> casesDejaVisitees = new ArrayList<Integer>();
		/// on passe en revue tout les pions de l'adversaire
		for (int pion : pionsJoueur) {
			/// on remet � z�ro les variables
			casesDejaVisitees.clear();
			differentsChemins.clear();
			differentsChemins = remonteCaseVerifie(plateau, pion, Pion.getCouleurJoueur(), -1,
					differentsChemins, casesDejaVisitees);

			/// si c'est vide, alors le pion N'EST PAS bloqu�
			if (differentsChemins.isEmpty()) {
				/// on ajoute tous les coups, donc les pions robots qui bloque les
				/// pions joueurs
				/// on remet � z�ro les variables
				casesDejaVisitees.clear();
				differentsChemins.clear();
				differentsChemins = remonteCaseVerifie(plateau, pion, Pion.getCouleurRobot(), -1,
						differentsChemins, casesDejaVisitees);
				for (Coups coup : differentsChemins) {
					coup.setCoupCaseArrivee(0);
					coup.setValeur(1);
					listeCoupsAV1.add(coup);
				}
			}
		}

		return listeCoupsAV1;

	}

	public static ArrayList<Coups> verifiePionBloqueMoulinAdversaire(Plateau plateau) {
		// si deux pions adverses sont align�s avec un troisi�me robot qui bloque
		/// la liste des coups en attente
		ArrayList<Coups> listeCoupsAttenteValeur3 = new ArrayList<Coups>();

		/// parcours tous les possibilit�s de moulins
		///il faut un pion adverse � cot� des cases bloquant le moulin
		/// pour que le moulin soit vraiment une menace
		for (int[] moulin : Cases.listeMoulins) {
			/// si la 3�me case est occup�e par l'adversaire
			if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurRobot()) {
				/// si il y a un pion a cot� de la case du joueur
				Boolean bloque = false;
				for (int caseAdjacente : plateau.mapCases.get(moulin[2]).getCasesAdjacentes()) {
					if (plateau.mapCases.get(caseAdjacente).pion == Pion
							.getCouleurJoueur()) {
						bloque = true;
					}
				}
				/// si bloque = true, alors le pion bloque un moulin
				if (bloque) {
					/// on ajoute le pion adverse
					Coups coup = new Coups();
					coup.setCoupCaseDepart(moulin[2]);
					coup.setValeur(3);
					listeCoupsAttenteValeur3.add(coup);
				}
				
			}
			/// si la 2�me case est occup�e par l'adversaire
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// si il y a un pion a cot� de la case du joueur
				Boolean bloque = false;
				for (int caseAdjacente : plateau.mapCases.get(moulin[1]).getCasesAdjacentes()) {
					if (plateau.mapCases.get(caseAdjacente).pion == Pion
							.getCouleurJoueur()) {
						bloque = true;
					}
				}
				/// si bloque = true, alors le pion bloque un moulin
				if (bloque) {
					/// on ajoute le pion adverse
					Coups coup = new Coups();
					coup.setCoupCaseDepart(moulin[1]);
					coup.setValeur(3);
					listeCoupsAttenteValeur3.add(coup);
				}
			}
			/// si la 1�re case est occup�e par l'adversaire
			else if (plateau.mapCases.get(moulin[0]).pion == Pion.getCouleurRobot()
					&& plateau.mapCases.get(moulin[1]).pion == Pion.getCouleurJoueur()
					&& plateau.mapCases.get(moulin[2]).pion == Pion.getCouleurJoueur()) {
				/// si il y a un pion a cot� de la case du joueur
				Boolean bloque = false;
				for (int caseAdjacente : plateau.mapCases.get(moulin[0]).getCasesAdjacentes()) {
					if (plateau.mapCases.get(caseAdjacente).pion == Pion
							.getCouleurJoueur()) {
						bloque = true;
					}
				}
				/// si bloque = true, alors le pion bloque un moulin
				if (bloque) {
					/// on ajoute le pion adverse
					Coups coup = new Coups();
					coup.setCoupCaseDepart(moulin[0]);
					coup.setValeur(3);
					listeCoupsAttenteValeur3.add(coup);
				}
			}
		}

		return listeCoupsAttenteValeur3;

	}

	public static ArrayList<Coups> remonteCase(Plateau plateau, int caseDepart, int couleur, int valeur,
			int casePrecedente, ArrayList<Coups> differentsChemins, ArrayList<Integer> casesDejaVisitees) {
		/// !!! r�cursivit�
		/// la couleur est celle que l'on veut faire venir sur caseDepart

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

		casePrecedente = caseDepart;

		/// parcours toutes les cases adjacente � la case
		for (int caseAdjacente : casesAdjacentes) {
			int valeurNouvelle = 0;
			/// si la case est vide, recommence l'op�ration

			/// on verifie que la case n'a pas d�j� �t� visit�e
			if (!casesDejaVisitees.contains(caseAdjacente)) {
				if (plateau.mapCases.get(caseAdjacente).pion == Pion.vide) {
					valeurNouvelle += 1;
					/// pour pas tourner en rond dans le tablier
					casesDejaVisitees.add(caseAdjacente);
					/// caseDepart devient caseAdjacente,
					/// casePrecedente a d�j� �t� chang�e
					differentsChemins = remonteCase(plateau, caseAdjacente, couleur,
							valeur + valeurNouvelle, casePrecedente, differentsChemins,
							casesDejaVisitees);
				}
				/// si la case est occup�e par un pion de la couleur choisie
				else if (plateau.mapCases.get(caseAdjacente).pion == couleur) {
					valeurNouvelle += 1;
					/// si la case est occup�e par un pion du robot
					/// on cr�e un coup : pion qui bougera + valeur + sur quelle
					/// case il
					/// bouge
					Coups coup = new Coups(caseAdjacente, valeurNouvelle + valeur, casePrecedente);
					/// on ajoute le coup � valeurDifferentsChemins
					differentsChemins.add(coup);
//					valeur = 0;
				}
				/// si la case est occup�e par un pion ennemi
				else {
					valeurNouvelle += 1;
					/// on cr�e un coup, pour comparer plus tard et savoir si le
					/// pion le
					/// plus proche de case d�part est du robot ou de
					/// l'adversaire
					Coups coup = new Coups(caseAdjacente, valeurNouvelle + valeur, casePrecedente);
					differentsChemins.add(coup);
//					valeur = 0;
				}
			}

		}

		/// envoi les diff�rentes valeurs
		return differentsChemins;

	}

	public static ArrayList<Coups> remonteCaseVerifie(Plateau plateau, int caseDepart, int couleur,
			int casePrecedente, ArrayList<Coups> differentsChemins, ArrayList<Integer> casesDejaVisitees) {
		/// !!! r�cursivit�
		/// la couleur est celle que l'on veut faire venir sur caseDepart

		/// r�cup�re les cases adjacente � caseDepart
		int[] casesAdjacentes = plateau.mapCases.get(caseDepart).getCasesAdjacentes();

		/// retire la case pr�c�demment test�e
		if (casePrecedente != -1) {
			for (int i = 0; i < casesAdjacentes.length; i++) {
				if (casesAdjacentes[i] == casePrecedente)
					casesAdjacentes[i] = 0;
			}
		}

		casePrecedente = caseDepart;

		/// parcours toutes les cases adjacente � la case
		for (int caseAdjacente : casesAdjacentes) {
			/// on v�rifie que la case n'est pas �t� visit�e et qu'elle n'est pas = � 0
			if (!casesDejaVisitees.contains(caseAdjacente) && caseAdjacente != 0) {

				/// si la case est vide, recommence l'op�ration
				if (plateau.mapCases.get(caseAdjacente).pion == Pion.vide) {
					/// pour pas tourner en rond
					casesDejaVisitees.add(caseAdjacente);
					/// caseDepart devient caseAdjacente,
					/// casePrecedente a d�j� �t� chang�e
					differentsChemins = remonteCaseVerifie(plateau, caseAdjacente, couleur,
							casePrecedente, differentsChemins, casesDejaVisitees);
				}
				/// si la case est occup�e par un pion de la couleur choisie
				else if (plateau.mapCases.get(caseAdjacente).pion == couleur) {
					/// si la case est occup�e par un pion du robot
					/// on cr�e un coup : pion qui bougera + valeur + sur quelle
					/// case il bouge
					Coups coup = new Coups();
					coup.setCoupCaseDepart(caseAdjacente);
					coup.setCoupCaseArrivee(casePrecedente);
					/// on ajoute le coup � valeurDifferentsChemins
					differentsChemins.add(coup);
				}
				/// si la case est occup�e par un pion ennemi, rien
			}
		}
		/// envoi les diff�rentes valeurs
		return differentsChemins;
	}

	public static Boolean verifieExclusion(Plateau plateau, Coups coup, ArrayList<Coups> listeCoupsAV1,
			ArrayList<Coups> listeCoupsAV2, ArrayList<Coups> listeCoupsAV3) {
		Boolean ok = true;

		for (Coups test : listeCoupsAV1) {
			if (test.getCoupCaseDepart() == coup.getCoupCaseDepart()) {
				ok = false;
			}
		}
		for (Coups test : listeCoupsAV2) {
			if (test.getCoupCaseDepart() == coup.getCoupCaseDepart()) {
				ok = false;
			}
		}
		for (Coups test : listeCoupsAV3) {
			if (test.getCoupCaseDepart() == coup.getCoupCaseDepart()) {
				ok = false;
			}
		}

		return ok;
	}

	public static int hasard() {
		/// prend une case au hasard sur le plateau
		Random random = new Random();
		return random.nextInt(24) + 1;
	}

	public static ArrayList<Integer> pionSurPlateau(Plateau plateau, int couleur) {
		/// r�cup�re tous les pions d'une couleur pr�sents sur le
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
