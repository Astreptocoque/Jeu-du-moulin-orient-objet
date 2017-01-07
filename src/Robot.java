import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

/// tout ce qui est en lien avec l'intelligence du robot
public class Robot {

	

	public static Coups robotJoue(Plateau plateau) {

		Coups coupChoisi = new Coups();
		
		LCD.clear();
		LCD.drawString("robotJoue", 0, 0);

		
		if (plateau.mode == 1 && plateau.modeElimination == false) { /// pose
			LCD.drawString("pose", 0, 1);
			coupChoisi = TactiquePose.tactiquePose(plateau);

		} else if (plateau.mode == 2 && plateau.modeElimination == false) { /// glisse
			LCD.drawString("glisse", 0, 1);
			coupChoisi = TactiqueGlisse.tactiqueGlisse(plateau);

		} else if (plateau.mode == 3 && plateau.modeElimination == false) {/// saut
			LCD.drawString("saut", 0, 1);
			coupChoisi = TactiqueSaut.tactiqueSaut(plateau);

		} else if (plateau.modeElimination == true) { /// mange
			LCD.drawString("mange", 0, 1);
			coupChoisi = TactiqueMange.tactiqueMange(plateau);
		}
				
		return coupChoisi;
	}


}
