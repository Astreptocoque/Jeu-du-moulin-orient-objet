import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Deplacements extends Pion{

	public Deplacements(int couleur, int numAncienneCase, int numNouvelleCase) {
		super(couleur, numAncienneCase, numNouvelleCase);
	}

	public Deplacements(int couleur) {
		super(couleur);
	}
	
	public Deplacements(){
		
	}

	/// variable public
	public static int couleurRobot = 0;
	public static int couleurJoueur = 0;

	/// moteurs
	EV3LargeRegulatedMotor moteurY1 = Hardware.moteurY1;
	EV3LargeRegulatedMotor moteurY2 = Hardware.moteurY2;
	EV3MediumRegulatedMotor moteurX = Hardware.moteurX;
	EV3LargeRegulatedMotor moteurPince = Hardware.moteurPince;
	RegulatedMotor moteurLevePince = Hardware.moteurLevePince;
	/// capteurs
	SampleProvider tactilePince = Hardware.tactilePince;
	float[] sampleTactilePince = Hardware.sampleTactilePince;
	SampleProvider toucheX = Hardware.toucheX;
	float[] sampleToucheX = Hardware.sampleToucheX;
	SampleProvider toucheY = Hardware.toucheY;
	float[] sampleToucheY = Hardware.sampleToucheY;
	EV3ColorSensor capteurCouleur = Hardware.sensorCouleur;
	float[] sampleCouleur = Hardware.sampleCapteurCouleur;
	
	/// synchronisation des moteurs B et C
	private EV3LargeRegulatedMotor[] synchro = new EV3LargeRegulatedMotor[1];

	/// s'occupe des déplacements des pions
	public void deplacementPion() {
		
		float colonne[] = { 3.5f, 9f, 14f, 18.8f, 23.5f, 28.5f, 33.5f, 38.5f, 43f }; // x
		float ligne[] = { 1.5f, 6.7f, 11.5f, 16.5f, 21.5f, 26.5f, 31.5f, 36.5f, 42.5f }; // y
		Boolean infini = true;

		synchro[0] = moteurY2;
		moteurY1.synchronizeWith(synchro);
		int distanceY = 0;
		int distanceX = 0;
		/// echelle de converstion cm/degré. Pareil pour X et Y
		float echelle = 96;
		/// vitesse donnée au moteur devant etre le plus rapide, l'autre
		/// s'adaptant
		/// il est possible de monter jusqu'à 800 !
		float vitesse = 700;
		int vitessePince = 400;
		int acceleration = 900;
		int degreDescendLevePince = 550;

		/// intilialisation des moteurs
		moteurPince.setSpeed(200);
		moteurY1.resetTachoCount();
		moteurY2.resetTachoCount();
		moteurX.resetTachoCount();
		moteurX.setAcceleration(acceleration);
		moteurY1.setAcceleration(acceleration);
		moteurY2.setAcceleration(acceleration);
		moteurLevePince.setSpeed(vitessePince);

		/// premier trajet, du départ à la première case
		distanceY = (int) (ligne[this.coordCaseDepart[1]] * echelle * -1); /// -1 pour que le moteur tourne à l'envers
		distanceX = (int) (colonne[this.coordCaseDepart[0]] * echelle);
		/// règle la vitesse, pour que chaque axe arrive en même temps
		reglageVitesse(distanceX, distanceY, vitesse);
		/// avance
		moteurY1.startSynchronization();
		moteurY1.rotate(distanceY);
		moteurY2.rotate(distanceY);
		moteurY1.endSynchronization();
		moteurX.rotate(distanceX, true);

		/// la pince s'ouvre
		moteurPince.rotate(175, true);
		moteurY2.waitComplete();
		moteurX.waitComplete();
		/// la pince descend
		moteurLevePince.rotate(degreDescendLevePince);
		/// saisi le pion
		moteurPince.rotate(75);
		/// la pince remonte
		/// manoeuvre pour ne pas défoncer le capteur tactile
		moteurLevePince.rotate(-degreDescendLevePince + 50);
		moteurLevePince.setSpeed(200);
		moteurLevePince.backward();
		while (infini == true) {
			tactilePince.fetchSample(sampleTactilePince, 0);
			if (sampleTactilePince[0] == 1) {
				moteurLevePince.stop();
				infini = false;
			}
		}
		infini = true;
		Delay.msDelay(500);
		/// second trajet, du premier point au second point
		distanceY = (int) ((ligne[this.coordCaseArrivee[1]] - ligne[this.coordCaseDepart[1]]) * echelle * -1);
		distanceX = (int) ((colonne[this.coordCaseArrivee[0]] - colonne[this.coordCaseDepart[0]]) * echelle);

		reglageVitesse(distanceX, distanceY, vitesse);

		moteurY1.startSynchronization();
		moteurY1.rotate(distanceY);
		moteurY2.rotate(distanceY);
		moteurY1.endSynchronization();
		moteurX.rotate(distanceX);
		moteurY2.waitComplete();

		/// la pince descend
		moteurLevePince.setSpeed(vitessePince);
		moteurLevePince.rotate(degreDescendLevePince);
		/// pose le pion
		moteurPince.rotate(-75);
		/// la pince remonte
		moteurLevePince.rotate(-degreDescendLevePince + 50);
		moteurLevePince.setSpeed(200);
		moteurLevePince.backward();
		while (infini == true) {
			tactilePince.fetchSample(sampleTactilePince, 0);
			if (sampleTactilePince[0] == 1) {
				moteurLevePince.stop();
				infini = false;
			}
		}
		/// ouvre totalement la pince
		moteurPince.rotate(-175, true);

		/// récupère la distance de chaque moteur
		distanceX = moteurX.getTachoCount();
		distanceY = moteurY1.getTachoCount();

		reglageVitesse(distanceX, distanceY, vitesse);

		/// retourne en biais au point de départ
		moteurY1.startSynchronization();
		moteurY1.rotate(-distanceY - 70);
		moteurY2.rotate(-distanceY - 70);
		moteurY1.endSynchronization();
		moteurX.rotate(-distanceX + 50);
		moteurY1.waitComplete();

		cadrage();

	}

	/// regle la vitesse de chaque axe pour une diagonale parfaite
	private void reglageVitesse(float distanceX, float distanceY, float vitesse) {
		
		
		float distancex = Math.abs(distanceX);
		float distancey = Math.abs(distanceY);
		float vitessex = 0;
		float vitessey = 0;
		/// calcule le rapport de vitesse entre les distances, et met la
		/// plus grande vitesse à la plus grande distance
		float rapport = distancey / distancex;
		if (rapport >= 1) {
			vitessey = vitesse;
			vitessex = vitessey / rapport;
		} else {
			vitessex = vitesse;
			vitessey = vitessex * rapport;
		}
		moteurX.setSpeed(vitessex);
		moteurY1.setSpeed(vitessey);
		moteurY2.setSpeed(vitessey);
	}

	public void cadrage() {
		
		
	
		
		/// variable de méthode
		Boolean infini = true; // défini si le robot roule à l'inifini
					// ou s'arrête

		/// synchronise les moteurs
		synchro[0] = moteurY1;
		moteurY2.synchronizeWith(synchro);

		moteurX.setSpeed(400);
		moteurY1.setSpeed(400);
		moteurY2.setSpeed(400);
		moteurX.setAcceleration(5500);
		moteurY1.setAcceleration(5500);
		moteurY2.setAcceleration(5500);

		/// cadre le remonte pince
		moteurLevePince.setSpeed(200);
		moteurLevePince.backward();
		while (infini == true) {
			tactilePince.fetchSample(sampleTactilePince, 0);
			if (sampleTactilePince[0] == 1) {
				moteurLevePince.stop();
				infini = false;
			}
		}

		infini = true;

		/// cadre le robot sur l'axe X, fait donc avancer le robot
		/// jusqu'au capteur tactile
		moteurX.backward();
		while (infini) {
			toucheX.fetchSample(sampleToucheX, 0);
			if (sampleToucheX[0] == 1) {
				moteurX.stop();
				infini = false;
			}
		}
		infini = true; /// infini est remit à vrai pour cadrer l'axe y

		/// cadre le robot sur l'axe Y
		moteurY2.startSynchronization();
		moteurY1.forward();
		moteurY2.forward();
		moteurY2.endSynchronization();
		while (infini) {
			toucheY.fetchSample(sampleToucheY, 0);
			if (sampleToucheY[0] == 1) {
				infini = false;
			}
		}
		moteurY2.startSynchronization();
		moteurY2.stop();
		moteurY1.stop();
		moteurY2.endSynchronization();

	}

	public void detecteCouleur() {
		
		
		cadrage();
		
		int acceleration = 900;
		Boolean infini = true;
		synchro[0] = moteurY2;
		moteurY1.synchronizeWith(synchro);
		
		
		moteurPince.setSpeed(200);
		moteurY1.resetTachoCount();
		moteurY2.resetTachoCount();
		moteurX.resetTachoCount();
		moteurX.setAcceleration(acceleration);
		moteurY1.setAcceleration(acceleration);
		moteurY2.setAcceleration(acceleration);
		moteurLevePince.setSpeed(200);

		moteurY1.startSynchronization();
		moteurY1.rotate(-160);
		moteurY2.rotate(-160);
		moteurY1.endSynchronization();
		moteurX.rotate(350, true);
		moteurY2.waitComplete();
		moteurLevePince.rotate(450);

		/// regarde la couleur du pion
		capteurCouleur.fetchSample(sampleCouleur, 0);
		capteurCouleur.close();
		/// règle les couleurs au joueurs
		if (sampleCouleur[0] == 6){
			couleurRobot = 6;
			couleurJoueur = 1;
		}
		else{
			couleurRobot = 1;
			couleurJoueur = 6;
		}
		
		/// la pince remonte
		moteurLevePince.backward();
		while (infini == true) {
			tactilePince.fetchSample(sampleTactilePince, 0);
			if (sampleTactilePince[0] == 1) {
				moteurLevePince.stop();
				infini = false;
			}
		}
		
		moteurY1.startSynchronization();
		moteurY1.rotate(160);
		moteurY2.rotate(160);
		moteurY1.endSynchronization();
		moteurX.rotate(-350, true);
		moteurY2.waitComplete();
		
		LCD.clear();
		LCD.drawInt(couleurRobot, 0, 3);
		LCD.drawInt(couleurJoueur,2,3);
		
//		moteurY1.close();
//		moteurY2.close();
//		moteurX.close();
//		moteurLevePince.close();
		
	}


}
