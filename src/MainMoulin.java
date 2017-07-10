import java.io.IOException;

public class MainMoulin {

	public static void main(String[] args) throws IOException {
		Reglages.initialisation();
			
		
		/// création du plateau de jeu et démarrage de la partie
		Plateau plateau = new Plateau();

		ModeDeJeu.joue(plateau);
		

	}

}