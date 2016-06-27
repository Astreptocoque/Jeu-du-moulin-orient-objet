import java.io.IOException;

public class MainMoulin {

	public static void main(String[] args) throws IOException {
		Reglages outils = new Reglages();
		outils.initialisation();
		
//		Pion.partieGlisse();
//		for(int i = 0; i < 6; i ++){
//			Pion.enlevePionRobot();
//		}
//		Pion.caseID.remove(20);
		
//		Pion.caseID.put(20,6);
		
		ModeDeJeu mode = new ModeDeJeu();
		mode.joue();
	}

}