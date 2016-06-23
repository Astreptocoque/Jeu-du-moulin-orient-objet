import java.util.Random;

/// tout ce qui est en lien avec l'intelligence du robot
public class Robot {

	private static Random random = new Random();

	public static int robotJoue() {
		int caseChoisie;

		caseChoisie = cretin();
		
		return caseChoisie;
	}
	
	private static int cretin(){
		/// prend une case au hasard sur le plateau
		return random.nextInt(24) + 1;
	}

	private static int casesVides(){
		return 0;
		
	}
}
