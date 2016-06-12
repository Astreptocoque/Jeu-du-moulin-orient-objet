import java.text.DecimalFormat;
import lejos.hardware.lcd.LCD;
import lejos.internal.ev3.EV3Battery;
import lejos.utility.Delay;

public class AfficheBatterie implements Runnable {
	EV3Battery batt = new EV3Battery();
	DecimalFormat df = new DecimalFormat("#.##");
	public boolean stopBatterie = true;

	public void run() {

		while (stopBatterie) {
			float pourcentage = batt.getVoltage();
			String nombre = df.format((pourcentage - 6) / 2 * 100);
			LCD.refresh();
			LCD.drawString("batterie restante :", 0, 6);
			LCD.drawString(String.valueOf(nombre) + " %", 5, 7);
			Delay.msDelay(5000);
		}
	}
}




