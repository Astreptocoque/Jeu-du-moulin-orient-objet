import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cases extends Plateau {
	/// contient toutes les informations relatives à chaque cases

	private final static int[][] listeCasesAdjacentes = { { 2, 10 }, { 1, 3, 5 }, { 2, 15 }, { 5, 11 },
			{ 2, 4, 6, 8 }, { 5, 14 }, { 8, 12 }, { 5, 7, 9 }, { 8, 13 }, { 1, 11, 22 }, { 4, 10, 12, 19 },
			{ 7, 11, 16 }, { 9, 14, 18 }, { 6, 13, 15, 21 }, { 3, 14, 24 }, { 12, 17 }, { 16, 18, 20 },
			{ 13, 17 }, { 11, 20 }, { 17, 19, 21, 23 }, { 14, 20 }, { 10, 23 }, { 20, 22, 24 },
			{ 15, 23 } };

	int numero; /// numero de la case
	ArrayList<Integer> casesAdjacentes; /// les cases adjacentes
	int pion; /// si un pion est sur la case, donc sa couleur
	int coordX; /// Case départ en coordonnées
	int coordY; /// Case arrivée en coordonnées

	public Cases(int numero) {
		/// son numero lui est attribué dès le début et ne change pas
		this.numero = numero;
		/// lors de l'initialisation, la case est vide
		this.pion = Pion.vide;
		/// attribution des cases adjacentes, ne change pas
		for (int elements : listeCasesAdjacentes[numero -1]) {
			this.casesAdjacentes.add(listeCasesAdjacentes[numero -1][elements]);
		}
	}

	public int getSituation() {
		/// permet d'obtenir si les cases adjacentes sont occupée ou
		/// non, donc si le pion est bloqué

		int nbrCasesBloquees = 0;

		/// regarde chaque case adjacente
		for (int i = 0; i < this.casesAdjacentes.size(); i++) {
			/// si la case est occupée...
			if (super.tabCases[this.casesAdjacentes.get(i)].pion != Pion.vide) {
				///... on ajoute 1 à nbrCasesBloquees
				nbrCasesBloquees++;
			}
		}

		/// retourne le nombre de cases bloquée
		/// s'il est égal au nombre de cases adjacentes, alors la case
		/// testée est bloquée
		return nbrCasesBloquees;
	}

	public int getNbrCasesAdjacentes() {
		return this.casesAdjacentes.size();
	}
}
