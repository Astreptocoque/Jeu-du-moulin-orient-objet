/// tout ce qui est en lien avec l'intelligence du robot
public class Robot {

	private static int caseChoisie;

	public static int robotJoue(Plateau plateau) {

		if (plateau.mode == 1) { /// pose
			caseChoisie = IntelligencePose.intelligencePose(plateau);

		} else if (plateau.mode == 2) { /// glisse
			caseChoisie = IntelligenceGlisse.hasard();

		} else if (plateau.mode == 3) {/// saut
			caseChoisie = IntelligenceSaut.hasard();

		} else { /// mange
			caseChoisie = IntelligenceMange.hasard();
		}
		return caseChoisie;
	}


}
