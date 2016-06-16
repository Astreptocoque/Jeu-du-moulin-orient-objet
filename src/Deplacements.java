import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class Deplacements extends Pion{

	/// constructeurs pour la création des pions
	public Deplacements(int couleur, int numAncienneCase, int numNouvelleCase) {
		super(couleur, numAncienneCase, numNouvelleCase);
	}

	public Deplacements(int couleur) {
		super(couleur);
	}
	
	public Deplacements(){
		
	}
	
	/// Degrés a faire en moins sur la distance de retour
	/// Sécurité pour ne pas "défoncer" le capteur tactile
	private final static int degreMoins = 80;
	/// vitesse de déplacements
	private final static int vitesseMax = 700;
	/// vitesse du cadrage
	private final static int vitesseCadrage = 400;
	
	/// vitesse de la pince
	private final static int vitessePince = 200;
	/// vitesse pour le moteur "descend pince" pour les endroits ralentits
	private final static int vitesseLentePince = 200;
	private final static int vitesseRapidePince = 400;
	/// acceleration de base
	private final static int acceleration = 900;
	
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

		synchro[0] = moteurY2;
		moteurY1.synchronizeWith(synchro);
		int distanceY;
		int distanceX;
		/// echelle de converstion cm/degré. Pareil pour X et Y
		float echelle = 96;
		int degreDescendLevePince = 550;
		Boolean infini = true;

		/// intilialisation des moteurs
		moteurPince.setSpeed(vitessePince);
		moteurY1.resetTachoCount();
		moteurY2.resetTachoCount();
		moteurX.resetTachoCount();
		moteurX.setAcceleration(acceleration);
		moteurY1.setAcceleration(acceleration);
		moteurY2.setAcceleration(acceleration);
		moteurLevePince.setSpeed(vitesseRapidePince);

		/// premier trajet, du départ à la première case
		distanceY = (int) (ligne[this.coordCaseDepart[1]] * echelle * -1); /// -1 pour que le moteur tourne à l'envers
		distanceX = (int) (colonne[this.coordCaseDepart[0]] * echelle);
		/// regle la vitesse, pour que chaque axe arrive en même temps à l'emplacement choisi
		reglageVitesse(distanceX, distanceY);
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
		/// Monte tout d'abord avec une vitesse élevée et s'arrête juste avant le tactile
		moteurLevePince.rotate(-degreDescendLevePince + degreMoins);
		/// ralentit et termine la montée, et se cadre sur le tactile
		moteurLevePince.setSpeed(vitesseLentePince);
		moteurLevePince.backward();
		while (infini == true) {
			tactilePince.fetchSample(sampleTactilePince, 0);
			if (sampleTactilePince[0] == 1) {
				moteurLevePince.stop();
				infini = false;
			}
		}
		infini = true;
//		Delay.msDelay(500);
		/// second trajet, du premier point au second point
		distanceY = (int) ((ligne[this.coordCaseArrivee[1]] - ligne[this.coordCaseDepart[1]]) * echelle * -1);
		distanceX = (int) ((colonne[this.coordCaseArrivee[0]] - colonne[this.coordCaseDepart[0]]) * echelle);

		reglageVitesse(distanceX, distanceY);

		moteurY1.startSynchronization();
		moteurY1.rotate(distanceY);
		moteurY2.rotate(distanceY);
		moteurY1.endSynchronization();
		moteurX.rotate(distanceX);
		moteurY2.waitComplete();

		/// la pince descend
		moteurLevePince.setSpeed(vitesseRapidePince);
		moteurLevePince.rotate(degreDescendLevePince);
		/// pose le pion
		moteurPince.rotate(-75);
		/// la pince remonte
		moteurLevePince.rotate(-degreDescendLevePince + degreMoins);
		moteurLevePince.setSpeed(vitesseLentePince);
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

		reglageVitesse(distanceX, distanceY);

		/// retourne en biais au point de départ
		moteurY1.startSynchronization();
		moteurY1.rotate(-distanceY - degreMoins);
		moteurY2.rotate(-distanceY - degreMoins);
		moteurY1.endSynchronization();
		moteurX.rotate(-distanceX + degreMoins);
		moteurY1.waitComplete();

		/// le robot se cadre parfaitement avec les capteurs tactiles.
		cadrage();

	}

	/// regle la vitesse de chaque axe pour une diagonale parfaite
	private void reglageVitesse(float distanceX, float distanceY) {
		
		float distancex = Math.abs(distanceX); /// distance (vecteur) que parcours l'axe x
		float distancey = Math.abs(distanceY); /// distance (vecteur) que parcours l'axe y
		float vitessex;
		float vitessey;
		/// calcule le rapport de vitesse entre les distances, et donne la
		/// vitesse de base (la plus rapide) à la plus grande distance
		/// l'autre distance reçoit la vitesse ralentie pour arriver en même temps
		float rapport = distancey / distancex;
		if (rapport >= 1) {
			vitessey = vitesseMax;
			vitessex = vitessey / rapport;
		} else {
			vitessex = vitesseMax;
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

		moteurX.setSpeed(vitesseCadrage);
		moteurY1.setSpeed(vitesseCadrage);
		moteurY2.setSpeed(vitesseCadrage);
		moteurX.setAcceleration(5500);
		moteurY1.setAcceleration(5500);
		moteurY2.setAcceleration(5500);

		/// cadre le remonte pince
		moteurLevePince.setSpeed(vitesseLentePince);
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
		
		Boolean infini = true;
		synchro[0] = moteurY2;
		moteurY1.synchronizeWith(synchro);
		
		
		moteurPince.setSpeed(vitessePince);
		moteurY1.resetTachoCount();
		moteurY2.resetTachoCount();
		moteurX.resetTachoCount();
		moteurX.setAcceleration(acceleration);
		moteurY1.setAcceleration(acceleration);
		moteurY2.setAcceleration(acceleration);
		moteurLevePince.setSpeed(vitesseLentePince);

		/// effectue les mouvements nécessaire afin de se placer sur le 1er pion
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
		if (sampleCouleur[0] == Pion.couleurDominante){
			Pion.setCouleurRobot(blanc);
			Pion.setCouleurJoueur(noir);
		}
		else{
			Pion.setCouleurRobot(noir);
			Pion.setCouleurJoueur(blanc);
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
		LCD.drawInt(Pion.getCouleurRobot(), 0, 3);
		LCD.drawInt(Pion.getCouleurJoueur(),2,3);
		
	}


}
