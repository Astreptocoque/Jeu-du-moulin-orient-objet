import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cases extends Plateau {
	/// contient toutes les informations relatives � chaque cases

	private final static int[][] listeCasesAdjacentes = { { 2, 10 }, { 1, 3, 5 }, { 2, 15 }, { 5, 11 },
			{ 2, 4, 6, 8 }, { 5, 14 }, { 8, 12 }, { 5, 7, 9 }, { 8, 13 }, { 1, 11, 22 }, { 4, 10, 12, 19 },
			{ 7, 11, 16 }, { 9, 14, 18 }, { 6, 13, 15, 21 }, { 3, 14, 24 }, { 12, 17 }, { 16, 18, 20 },
			{ 13, 17 }, { 11, 20 }, { 17, 19, 21, 23 }, { 14, 20 }, { 10, 23 }, { 20, 22, 24 },
			{ 15, 23 } };

	int numero; /// numero de la case
	ArrayList<Integer> casesAdjacentes; /// les cases adjacentes
	int pion; /// si un pion est sur la case, donc sa couleur
	int coordX; /// Case d�part en coordonn�es
	int coordY; /// Case arriv�e en coordonn�es

	public Cases(int numero) {
		/// son numero lui est attribu� d�s le d�but et ne change pas
		this.numero = numero;
		/// lors de l'initialisation, la case est vide
		this.pion = Pion.vide;
		/// attribution des cases adjacentes, ne change pas
		for (int elements : listeCasesAdjacentes[numero -1]) {
			this.casesAdjacentes.add(listeCasesAdjacentes[numero -1][elements]);
		}
	}

	public int getSituation() {
		/// permet d'obtenir si les cases adjacentes sont occup�e ou
		/// non, donc si le pion est bloqu�

		int nbrCasesBloquees = 0;

		/// regarde chaque case adjacente
		for (int i = 0; i < this.casesAdjacentes.size(); i++) {
			/// si la case est occup�e...
			if (super.tabCases[this.casesAdjacentes.get(i)].pion != Pion.vide) {
				///... on ajoute 1 � nbrCasesBloquees
				nbrCasesBloquees++;
			}
		}

		/// retourne le nombre de cases bloqu�e
		/// s'il est �gal au nombre de cases adjacentes, alors la case
		/// test�e est bloqu�e
		return nbrCasesBloquees;
	}

	public int getNbrCasesAdjacentes() {
		return this.casesAdjacentes.size();
	}
}
