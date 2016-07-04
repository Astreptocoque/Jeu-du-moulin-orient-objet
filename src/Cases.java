import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cases {

	/// variables d'instances
	int numero; /// numero de la case
	ArrayList<Integer> casesAdjacentes; /// les cases adjacentes
	int pion; /// si un pion est sur la case, donc sa couleur
	int coordX; /// Case départ en coordonnées
	int coordY; /// Case arrivée en coordonnées
	int[][] casesMoulins;

	public Cases(int numero) {
		/// son numero lui est attribué dès le début et ne change pas
		this.numero = numero;
		/// lors de l'initialisation, la case est vide
		this.pion = Pion.vide;
		
		if(numero < 25)
			attributionListesCases(numero);

	}

	private void attributionListesCases(int numero) {
		/// contient toutes les informations relatives à chaque cases

		int[][] listeCasesAdjacentes = { { 2, 10 }, { 1, 3, 5 }, { 2, 15 }, { 5, 11 }, { 2, 4, 6, 8 },
				{ 5, 14 }, { 8, 12 }, { 5, 7, 9 }, { 8, 13 }, { 1, 11, 22 }, { 4, 10, 12, 19 },
				{ 7, 11, 16 }, { 9, 14, 18 }, { 6, 13, 15, 21 }, { 3, 14, 24 }, { 12, 17 },
				{ 16, 18, 20 }, { 13, 17 }, { 11, 20 }, { 17, 19, 21, 23 }, { 14, 20 }, { 10, 23 },
				{ 20, 22, 24 }, { 15, 23 } };

		int[][] listeMoulins = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 }, { 13, 14, 15 },
				{ 16, 17, 18 }, { 19, 20, 21 }, { 22, 23, 24 }, { 1, 10, 22 }, { 4, 11, 19 },
				{ 7, 12, 16 }, { 2, 5, 8 }, { 17, 20, 23 }, { 9, 13, 18 }, { 6, 14, 21 },
				{ 3, 15, 24 } };

		/// attribution des cases adjacentes, ne change pas
		this.casesAdjacentes = new ArrayList<Integer>();
		for (int elements : listeCasesAdjacentes[numero - 1]) {
			this.casesAdjacentes.add(elements);
		}

		/// attribution des moulins que peut former chaque case
		int indiceCasesMoulins1 = 0;
		int indiceCasesMoulins2 = 0;
		this.casesMoulins = new int[2][2];
		/// pour chaque moulin possible...
		for (int i = 0; i < listeMoulins.length; i++) {
			indiceCasesMoulins2 = 0;
			/// vérifie si la case est inclue dans ce moulin
			for (int j = 0; j < listeMoulins[i].length; j++) {
				/// si la case est inclue dans le moulin...
				if (listeMoulins[i][j] == numero) {
					/// on ajoute le moulin dans les caract.
					/// de la case, en retirant la case
					/// testée
					for (int elements : listeMoulins[i]) {
						if (elements != numero) {
							this.casesMoulins[indiceCasesMoulins1][indiceCasesMoulins2] = elements;
							indiceCasesMoulins2++;
						}
					}
					indiceCasesMoulins1++;
					break;
				}
			}
		}

	}

	public int[] getCasesAdjacentes() {
		int[] cases = new int[this.casesAdjacentes.size()];
		for (int i = 0; i < this.casesAdjacentes.size(); i++) {
			cases[i] = this.casesAdjacentes.get(i);
		}
		return cases;
	}
	
	
	public boolean getBlocage(Plateau plateau) {
		/// permet d'obtenir si les cases adjacentes sont occupée ou
		/// non, donc si le pion est bloqué
		boolean bloque = false;
		int nbrCasesBloquees = 0;
		int[] casesAdjacentes = this.getCasesAdjacentes();

		/// regarde chaque case adjacente
		for (int i = 0; i < casesAdjacentes.length; i++) {
			/// si la case est occupée...
			if(plateau.mapCases.get(casesAdjacentes[i]).pion != Pion.vide){
//			if (plateau.tabCases[casesAdjacentes[i]].pion != Pion.vide) {
				///... on ajoute 1 à nbrCasesBloquees
				nbrCasesBloquees++;
			}
		}

		/// retourne le nombre de cases bloquée
		/// s'il est égal au nombre de cases adjacentes, alors la case
		/// testée est bloquée
		if(nbrCasesBloquees == this.getCasesAdjacentes().length){
			bloque = true;
		}
		else{
			bloque = false;
		}
		
		return bloque;
	}
}
