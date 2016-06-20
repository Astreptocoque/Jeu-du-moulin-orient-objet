import java.io.IOException;

public class MainMoulin {

	public static void main(String[] args) throws IOException {
		Outils outils = new Outils();
		outils.initialisation();
		
		Pion.sautille();
		for(int i = 0; i < 6; i ++){
			Pion.enlevePionRobot();
		}
		
		ModeDeJeu mode = new ModeDeJeu();
		mode.joue();
	}

}