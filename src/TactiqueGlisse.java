import java.util.Random;

public class TactiqueGlisse {

	public static Coups tactiqueGlisse(Plateau plateau){
		
		Random random = new Random();
		
		int caseDepart = hasard();
		int[] casesAdjacentesCaseDepart = plateau.mapCases.get(caseDepart).getCasesAdjacentes();
		int caseArrivee = casesAdjacentesCaseDepart[random.nextInt(casesAdjacentesCaseDepart.length)];
		
		Coups coup = new Coups(caseDepart, 0, caseArrivee);
		
		
		return coup;
	}
	
	
	public static int hasard() {
		/// prend une case au hasard sur le plateau
		Random random = new Random();
		return random.nextInt(24) + 1;
	}
}
