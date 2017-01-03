import java.util.Random;

public class TactiqueSaut {
	public static int hasard() {
		/// prend une case au hasard sur le plateau
		Random random = new Random();
		return random.nextInt(24) + 1;
	}
}
