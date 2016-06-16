import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class Hardware {
	
	/// moteurs
	public static EV3LargeRegulatedMotor moteurY1 = new EV3LargeRegulatedMotor(MotorPort.B);
	public static EV3LargeRegulatedMotor moteurY2 = new EV3LargeRegulatedMotor(MotorPort.C);
	public static EV3MediumRegulatedMotor moteurX = new EV3MediumRegulatedMotor(MotorPort.D);
	public static EV3LargeRegulatedMotor moteurPince = new EV3LargeRegulatedMotor(MotorPort.A);
	public static RegulatedMotor moteurLevePince = Outils.brique[1].createRegulatedMotor("B", 'L');

	/// capteurs
	public static SampleProvider toucheX = new NXTTouchSensor(SensorPort.S2).getTouchMode();
	public static float[] sampleToucheX = new float[toucheX.sampleSize()];
	
	public static SampleProvider toucheY = new NXTTouchSensor(SensorPort.S1).getTouchMode();
	public static float[] sampleToucheY = new float[toucheY.sampleSize()];

	public static SampleProvider tactilePince = new EV3TouchSensor(SensorPort.S4).getTouchMode();
	public static float[] sampleTactilePince = new float[tactilePince.sampleSize()];
	
	public static EV3ColorSensor sensorCouleur = new EV3ColorSensor(SensorPort.S3);
	public static SensorMode capteurCouleur = sensorCouleur.getRedMode();
	public static float[] sampleCapteurCouleur = new float[capteurCouleur.sampleSize()];

}
