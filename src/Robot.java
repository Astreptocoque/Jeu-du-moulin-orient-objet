import java.util.Random;

/// tout ce qui est en lien avec le tour du robot
public class Robot {

	private static Random random = new Random();

	public static int robotJoue() {
		int caseChoisie;

		
		caseChoisie = random.nextInt(24) + 1;
		
		return caseChoisie;
	}
}
