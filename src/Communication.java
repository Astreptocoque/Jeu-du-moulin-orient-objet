import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class Communication {

	public static ServerSocket server;
	public static Socket socket;
	
	public static  int PCInputStream() throws IOException {
		int input;
		boolean erreur = false;

		do {
			try {
				server = new ServerSocket(1111);
				erreur = false;
			} catch (BindException e) {
				erreur = true;
				LCD.drawString("erreur", 0, 3);
				Sound.beep();
				System.out.println(e);

			}
		} while (erreur == true);

		socket = server.accept();

		DataInputStream in = new DataInputStream(socket.getInputStream());

		input = in.readInt();

		return input;
	}

	public static  void PCOutputStream(int output) throws IOException {
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());

		out.writeInt(output);

		server.close();

	}
	
}
