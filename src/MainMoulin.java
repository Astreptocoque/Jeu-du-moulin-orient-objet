import java.io.IOException;

public class MainMoulin {

	public static void main(String[] args) throws IOException {
		Outils outils = new Outils();
		outils.initialisation();
		
		Pion.tests();
		
		ModeDeJeu mode = new ModeDeJeu();
		mode.joue();
	}

}