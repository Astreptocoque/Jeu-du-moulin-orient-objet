import java.io.IOException;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.remote.ev3.RemoteRequestEV3;

public class Reglages {

	private static AfficheBatterie batterie = new AfficheBatterie();

	/// permet la connection � la seconde brique
	public static String[] names = { "EV1", "EV2" };
	public static RemoteRequestEV3[] brique = new RemoteRequestEV3[names.length];

	
	public static void initialisation() throws IOException {

		connectionEV2();

		/// cr�ation du thread pour afficher la batterie � l'�cran
		Thread threadBatterie = new Thread(batterie);
		threadBatterie.start();
		
		/// regarde quelle est la couleur du robot
		Deplacements d = new Deplacements();
		d.detecteCouleur();

	}

	public static void reglagesFin() throws IOException {
		/// stop le thread de la batterie;
		batterie.stopBatterie = false;
		/// arr�te le moteur de la brique esclave
		Hardware.moteurLevePince.close();
		/// d�connecte la brique esclave
		brique[1].disConnect();
	}

	private static void connectionEV2() {
		/// connection � la brique EV2
		LCD.drawString("Connection a EV2...", 0, 0);

		try {
			brique[1] = new RemoteRequestEV3(BrickFinder.find(names[1])[0].getIPAddress());
		} catch (Exception e) {
			LCD.clear();
			Sound.beepSequenceUp();
			LCD.drawString("La connection", 1, 0);
			LCD.drawString("a echouee", 3, 1);
			LCD.drawString("!!!!!!!!", 5, 4);
			LCD.drawString("pressez un bouton", 0, 6);
			Button.waitForAnyPress();
			System.exit(0);
		}

		LCD.clear();
		LCD.drawString("Connecte a EV2", 0, 0);
	}

}
