import java.util.Random;
/// tout ce qui est en lien avec le tour du robot
public class Robot {
	
	private static Random random = new Random();
	public static int robotJoue(){
		
		 return random.nextInt(24) + 1;
	}
}
