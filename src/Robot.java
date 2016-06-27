/// tout ce qui est en lien avec l'intelligence du robot
public class Robot {

	private static int caseChoisie;

	public static int robotJoue() {

		if (Pion.mode == 1) { /// pose

			IntelligencePose go = new IntelligencePose();
			caseChoisie = go.intelligencePose();

		} else if (Pion.mode == 2) { /// glisse

			IntelligenceGlisse go = new IntelligenceGlisse();
			caseChoisie = go.hasard();

		} else if (Pion.mode == 3) {/// saut

			IntelligenceSaut go = new IntelligenceSaut();
			caseChoisie = go.hasard();

		} else { /// mange
			robotMange();
		}
		return caseChoisie;
	}

	public static void robotMange() {
		IntelligenceMange go = new IntelligenceMange();
		caseChoisie = go.hasard();
	}

}
