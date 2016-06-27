import java.util.Random;

public class IntelligenceMange {
	public int hasard() {
		/// prend une case au hasard sur le plateau
		Random random = new Random();
		return random.nextInt(24) + 1;
	}
}
