import java.io.IOException;

public class MainMoulin {

	public static void main(String[] args) throws IOException {
		Reglages.initialisation();
			
		
		/// cr�ation du plateau de jeu et d�marrage de la partie
		Plateau plateau = new Plateau();

		ModeDeJeu.joue(plateau);
		

	}

}