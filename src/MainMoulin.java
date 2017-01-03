import java.io.IOException;

public class MainMoulin {

	public static void main(String[] args) throws IOException {
		Reglages.initialisation();
		
		///**************************
		/// faire attention à l'intelligence la prochaine fois
		///**************************
		
		
		/// création du plateau de jeu et démarrage de la partie
		Plateau plateau = new Plateau();
		/*plateau.mapCases.get(1).pion = Pion.noir;
		plateau.mapCases.get(2).pion = Pion.vide;
		plateau.mapCases.get(3).pion = Pion.blanc;
		plateau.mapCases.get(4).pion = Pion.blanc;
		plateau.mapCases.get(5).pion = Pion.noir;
		plateau.mapCases.get(6).pion = Pion.vide;
		plateau.mapCases.get(7).pion = Pion.noir;
		plateau.mapCases.get(8).pion = Pion.vide;
		plateau.mapCases.get(9).pion = Pion.vide;
		plateau.mapCases.get(10).pion = Pion.vide;
		plateau.mapCases.get(11).pion = Pion.noir;
		plateau.mapCases.get(12).pion = Pion.vide;
		plateau.mapCases.get(13).pion = Pion.blanc;
		plateau.mapCases.get(14).pion = Pion.blanc;
		plateau.mapCases.get(15).pion = Pion.noir;
		plateau.mapCases.get(16).pion = Pion.noir;
		plateau.mapCases.get(17).pion = Pion.noir;
		plateau.mapCases.get(18).pion = Pion.blanc;
		plateau.mapCases.get(19).pion = Pion.vide;
		plateau.mapCases.get(20).pion = Pion.vide;
		plateau.mapCases.get(21).pion = Pion.blanc;
		plateau.mapCases.get(22).pion = Pion.noir;
		plateau.mapCases.get(23).pion = Pion.blanc;
		plateau.mapCases.get(24).pion = Pion.blanc;
*/

		ModeDeJeu.joue(plateau);
		

	}

}