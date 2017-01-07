import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class Deplacements {

	/// variables d'instances
	int coordDepartX;
	int coordDepartY;
	int coordArriveeX;
	int coordArriveeY;
	int numeroCaseDepart; /// N° de la case départ du pion
	int numeroCaseArrivee; /// N° de la case arrivée du pion

	/// constructeurs pour la création d'un déplacement
	public Deplacements() {

	}

	public void setCaseDepart(int numeroCase, Plateau plateau) {
		this.numeroCaseDepart = numeroCase;
		/// modifie l'emplacement du nouveau pion en tant
		/// que vide
		plateau.mapCases.get(numeroCase).pion = Pion.vide;

		/// définit les coordonnées de départ
		coordDepartX = plateau.mapCases.get(numeroCase).coordX;
		coordDepartY = plateau.mapCases.get(numeroCase).coordY;

		/// change l'état (dans un moulin ou non) de la case et des
		/// cases formant avec elle un moulin
		if (plateau.mapCases.get(numeroCase).etatMoulin == true){
			int[] moulin1 = plateau.mapCases.get(numeroCase).casesMoulins[0].clone();
			int[] moulin2 = plateau.mapCases.get(numeroCase).casesMoulins[1].clone();
			for(int i = 0; i < moulin1.length; i++){
				if(plateau.mapCases.get(moulin1[i]).etatMoulin == true){
					plateau.mapCases.get(moulin1[i]).etatMoulin = false;
				}
			}
			for(int i = 0; i < moulin2.length; i++){
				if(plateau.mapCases.get(moulin2[i]).etatMoulin == true){
					plateau.mapCases.get(moulin2[i]).etatMoulin = false;
				}
			}
		}
	}

	public void setCaseArrivee(int numeroCase, Plateau plateau) {
		this.numeroCaseArrivee = numeroCase;
		/// modifie l'emplacement du nouveau pion
		/// initiale des pions
		plateau.mapCases.get(numeroCase).pion = Pion.getCouleurActuelle();

		/// enregistre la dernière case. La condition est là pour
		/// empêcher de changer la case si le joueur mange un pion
		if (plateau.mode != 4) {
			Pion.derniereCaseJoueur = numeroCase;
		}

		/// définit les coordonnées d'arrivée
		coordArriveeX = plateau.mapCases.get(numeroCase).coordX;
		coordArriveeY = plateau.mapCases.get(numeroCase).coordY;

	}

	public int getCaseDepart() {
		return this.numeroCaseDepart;
	}

	public int getCaseArrivee() {
		return this.numeroCaseArrivee;
	}

	/// Degrés a faire en moins sur la distance de retour
	/// Sécurité pour ne pas "défoncer" le capteur tactile
	private final static int degreEnmoinsDeplacement = 80;
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
	/// échelle de conversion cm/degrés
	private final static float echelle = 96;
	/// degrés pour faire descendre la pince
	private final static int degreDescendLevePince = 550;
	/// degrés pour fermer la pince, ne pas changer (sauf micro-ajustements)
	private final static int degreFermePince = 260;
	/// degrés de pré-fermeture de la pince, valeur ajustable
	private final static int degrePreFermePince = 180;
	/// distance en cm des cases par rapport à l'origine, valeurs ajustables
	private final float colonne[] = { 3.5f, 8.7f, 13.8f, 18.8f, 23.5f, 28.5f, 33.5f, 38.7f, 42.5f }; // x
	private final float ligne[] = { 1.5f, 6.7f, 11.5f, 16.5f, 21.5f, 26.5f, 31.5f, 36.5f, 42.5f }; // y

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
	public void deplacementPionJoueur() {

		synchro[0] = moteurY2;
		moteurY1.synchronizeWith(synchro);
		int distanceY;
		int distanceX;
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
		/// -1 pour que le moteur tourne à l'envers

		distanceY = (int) ((ligne[this.coordDepartY]) * echelle * -1);
		distanceX = (int) ((colonne[this.coordDepartX]) * echelle);

		/// regle la vitesse, pour que chaque axe arrive en même
		/// temps à
		/// l'emplacement choisi
		reglageVitesse(distanceX, distanceY);
		/// avance
		moteurY1.startSynchronization();
		moteurY1.rotate(distanceY);
		moteurY2.rotate(distanceY);
		moteurY1.endSynchronization();
		moteurX.rotate(distanceX, true);

		moteurPince.rotate(degrePreFermePince, true);

		moteurY2.waitComplete();
		moteurX.waitComplete();
		moteurPince.waitComplete();
		/// la pince descend
		moteurLevePince.rotate(degreDescendLevePince);
		/// saisi le pion, fait la différence entre le total et ce qui
		/// est déjà baissé
		moteurPince.rotate(degreFermePince - degrePreFermePince);
		/// la pince remonte
		/// Monte tout d'abord avec une vitesse élevée et s'arrête juste
		/// avant le tactile
		moteurLevePince.rotate(-degreDescendLevePince + degreEnmoinsDeplacement);
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

		/// second trajet, du premier point au second point
		distanceY = (int) ((ligne[this.coordArriveeY] - ligne[this.coordDepartY]) * echelle * -1);
		distanceX = (int) ((colonne[this.coordArriveeX] - colonne[this.coordDepartX]) * echelle);

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
		moteurPince.rotate(-(degreFermePince - degrePreFermePince));
		/// la pince remonte
		moteurLevePince.rotate(-degreDescendLevePince + degreEnmoinsDeplacement);
		moteurLevePince.setSpeed(vitesseLentePince);
		moteurLevePince.backward();
		while (infini == true) {
			tactilePince.fetchSample(sampleTactilePince, 0);
			if (sampleTactilePince[0] == 1) {
				moteurLevePince.stop();
				infini = false;
			}
		}

		/// récupère la distance de chaque moteur
		distanceX = moteurX.getTachoCount();
		distanceY = moteurY1.getTachoCount();
		/// ajuste les coordonnées
		Pion.coordDernierPion[0] = distanceX;
		Pion.coordDernierPion[1] = distanceY;

	}

	public void deplacementPionRobot() {
		synchro[0] = moteurY2;
		moteurY1.synchronizeWith(synchro);
		int distanceY;
		int distanceX;
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

		/// baisse la pince si c'est le 1er tour et le robot commence
		if (Pion.coordDernierPion[0] == 0 && Pion.coordDernierPion[1] == 0) {
			moteurPince.rotate(degrePreFermePince, true);
		}

		/// pour se rendre au premier pion
		distanceY = (int) ((ligne[this.coordDepartY]) * echelle * -1 - Pion.coordDernierPion[1]);
		distanceX = (int) ((colonne[this.coordDepartX]) * echelle - Pion.coordDernierPion[0]);
		/// ajuste les coordonnées
		Pion.coordDernierPion[0] = Pion.coordDernierPion[0] + distanceX;
		Pion.coordDernierPion[1] = Pion.coordDernierPion[1] + distanceY;

		/// regle la vitesse, pour que chaque axe arrive en même temps à
		/// l'emplacement choisi
		reglageVitesse(distanceX, distanceY);

		/// avance
		moteurY1.startSynchronization();
		moteurY1.rotate(distanceY);
		moteurY2.rotate(distanceY);
		moteurY1.endSynchronization();
		moteurX.rotate(distanceX, true);
		moteurY1.waitComplete();
		moteurX.waitComplete();

		/// la pince descend
		moteurLevePince.rotate(degreDescendLevePince);
		/// saisi le pion
		moteurPince.rotate(degreFermePince - degrePreFermePince);
		/// la pince remonte
		/// Monte tout d'abord avec une vitesse élevée et s'arrête juste
		/// avant le tactile
		moteurLevePince.rotate(-degreDescendLevePince + degreEnmoinsDeplacement);
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

		/// deplacement au second pion
		distanceY = (int) ((ligne[this.coordArriveeY] - ligne[this.coordDepartY]) * echelle * -1);
		distanceX = (int) ((colonne[this.coordArriveeX] - colonne[this.coordDepartX]) * echelle);
		/// ajuste les coordonnées
		Pion.coordDernierPion[0] = (int) (colonne[this.coordArriveeX] * echelle);
		Pion.coordDernierPion[1] = (int) (ligne[this.coordArriveeY] * echelle * -1);
		/// regle la vitesse, pour que chaque axe arrive en même temps à
		/// l'emplacement choisi
		reglageVitesse(distanceX, distanceY);

		/// avance
		moteurY1.startSynchronization();
		moteurY1.rotate(distanceY);
		moteurY2.rotate(distanceY);
		moteurY1.endSynchronization();
		moteurX.rotate(distanceX, true);
		moteurY1.waitComplete();
		moteurX.waitComplete();

		/// la pince descend
		moteurLevePince.setSpeed(vitesseRapidePince);
		moteurLevePince.rotate(degreDescendLevePince);
		/// pose le pion
		moteurPince.rotate(-(degreFermePince - degrePreFermePince));
		/// la pince remonte
		moteurLevePince.rotate(-degreDescendLevePince + degreEnmoinsDeplacement);
		moteurLevePince.setSpeed(vitesseLentePince);
		moteurLevePince.backward();
		while (infini == true) {
			tactilePince.fetchSample(sampleTactilePince, 0);
			if (sampleTactilePince[0] == 1) {
				moteurLevePince.stop();
				infini = false;
			}
		}

	}

	public void deplacementOrigine() {
		synchro[0] = moteurY2;
		moteurY1.synchronizeWith(synchro);
		int distanceY;
		int distanceX;
		/// intilialisation des moteurs
		moteurY1.resetTachoCount();
		moteurY2.resetTachoCount();
		moteurX.resetTachoCount();
		moteurX.setAcceleration(acceleration);
		moteurY1.setAcceleration(acceleration);
		moteurY2.setAcceleration(acceleration);

		/// empêche des mouvements inutiles
		if (Pion.coordDernierPion[0] != 0 && Pion.coordDernierPion[1] != 0) {
			/// retour à l'origine
			distanceY = (int) (Pion.coordDernierPion[1]);
			distanceX = (int) (Pion.coordDernierPion[0]);

			/// regle la vitesse, pour que chaque axe arrive en même
			/// temps à
			/// l'emplacement choisi
			reglageVitesse(distanceX, distanceY);
			/// reouvre totalement la pince
			moteurPince.rotate(-degrePreFermePince, true);
			/// avance
			moteurY1.startSynchronization();
			moteurY1.rotate(-distanceY - degreEnmoinsDeplacement);
			moteurY2.rotate(-distanceY - degreEnmoinsDeplacement);
			moteurY1.endSynchronization();
			moteurX.rotate(-distanceX + degreEnmoinsDeplacement);

			/// attend que la pince s'ouvre completement (si pas)
			moteurPince.waitComplete();
			moteurY1.waitComplete();
			moteurX.waitComplete();
		}
		cadrage();
	}

	private void reglageVitesse(float distanceX, float distanceY) {
		/// regle la vitesse de chaque axe pour une diagonale parfaite

		float distancex = Math.abs(distanceX); /// distance (vecteur)
							/// que parcours l'axe x
		float distancey = Math.abs(distanceY); /// distance (vecteur)
							/// que parcours l'axe y
		float vitessex;
		float vitessey;
		/// calcule le rapport de vitesse entre les distances, et donne
		/// la
		/// vitesse de base (la plus rapide) à la plus grande distance
		/// l'autre distance reçoit la vitesse ralentie pour arriver en
		/// même temps
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

		/// réinitialise les coordonnées du robot
		/// pour être sur qu'elles soient justes
		Pion.coordDernierPion[0] = 0;
		Pion.coordDernierPion[1] = 0;

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

		/// effectue les mouvements nécessaire afin de se placer sur le
		/// 1er pion
		moteurY1.startSynchronization();
		moteurY1.rotate(-160);
		moteurY2.rotate(-160);
		moteurY1.endSynchronization();
		moteurX.rotate(350, true);
		moteurY2.waitComplete();
		moteurLevePince.rotate(450);

		/// regarde la couleur du pion
		capteurCouleur.fetchSample(sampleCouleur, 0);

		if (sampleCouleur[0] == Pion.couleurDominante) {
			Pion.setCouleurRobot(Pion.blanc);
			Pion.setCouleurJoueur(Pion.noir);
		} else {
			Pion.setCouleurRobot(Pion.noir);
			Pion.setCouleurJoueur(Pion.blanc);
		}

		/// éteint le capteur
		capteurCouleur.close();

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

	}

}
