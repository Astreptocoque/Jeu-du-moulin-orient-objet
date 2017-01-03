/// tout ce qui est en lien avec l'intelligence du robot
public class Robot {

	private static int caseChoisie;

	public static int robotJoue(Plateau plateau) {

		if (plateau.mode == 1 && plateau.modeElimination == false) { /// pose
			caseChoisie = TactiquePose.intelligencePose(plateau);

		} else if (plateau.mode == 2 && plateau.modeElimination == false) { /// glisse
			caseChoisie = TactiqueGlisse.hasard();

		} else if (plateau.mode == 3 && plateau.modeElimination == false) {/// saut
			caseChoisie = TactiqueSaut.hasard();

		} else if (plateau.modeElimination == true && plateau.modeElimination == false) { /// mange
			caseChoisie = TactiqueMange.hasard();
		}
		return caseChoisie;
	}


}
