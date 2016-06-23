import java.io.IOException;

public class MainMoulin {

	public static void main(String[] args) throws IOException {
		Reglages outils = new Reglages();
		outils.initialisation();
		
//		Pion.partieGlisse();
//		for(int i = 0; i < 6; i ++){
//			Pion.enlevePionRobot();
//		}
		
		ModeDeJeu mode = new ModeDeJeu();
		mode.joue();
	}

}