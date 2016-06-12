import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.remote.ev3.RemoteRequestEV3;

public class Outils {

	AfficheBatterie batterie = new AfficheBatterie();
	
	/// permet la connection à la seconde brique
	public static  String[] names = { "EV1", "EV2" };
	public static  RemoteRequestEV3[] brique = new RemoteRequestEV3[names.length];

	public void initialisation() throws IOException {

		connectionEV2();

		/// création du thread pour afficher la batterie à l'écran
		Thread threadBatterie = new Thread(batterie);
		threadBatterie.start();

		/// création des map pour les cases
		Pion.creationMapCaseIDCoord();
		Pion.creationMapCaseID();
		

	}

	public void reglagesFin() throws IOException {
		/// stop le thread de la batterie;
		batterie.stopBatterie = false;
		/// arrête le moteur de la brique esclave
		Hardware.moteurLevePince.close();
		/// déconnecte la brique esclave
		brique[1].disConnect();
		/// stop la conection avec le pc
	}

	private void connectionEV2() {
		/// connection à la brique EV2
		LCD.drawString("Connection a EV2...", 0, 0);

		try {
			brique[1] = new RemoteRequestEV3(BrickFinder.find(names[1])[0].getIPAddress());
		} catch (Exception e) {
			LCD.clear();
			Sound.beep();
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
