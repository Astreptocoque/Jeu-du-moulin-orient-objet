import java.io.IOException;

public class MainMoulin {

	public static void main(String[] args) throws IOException {
		Reglages outils = new Reglages();
		outils.initialisation();
		
		
		Plateau plateau = new Plateau();
		ModeDeJeu.joue(plateau);
		
		ModeDeJeu mode = new ModeDeJeu();
		mode.joue();
	}

}