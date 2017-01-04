import java.util.Random;

public class TactiqueSaut {
	
	public static Coups tactiqueSaut(Plateau plateau){
		
		Coups coup = new Coups(hasard(), 0, hasard());
		
		return coup;
	}
	
	public static int hasard() {
		/// prend une case au hasard sur le plateau
		Random random = new Random();
		return random.nextInt(24) + 1;
	}
}
