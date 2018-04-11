

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Scanner;

public class Server{

	private static final int PORT = 9876;
	private static int sensorCount = 0;
	private static HashMap<String, ObjectInputStream> objectsMap = new HashMap<String, ObjectInputStream>();
	private static HashMap<String, PrintWriter> printWritersMap = new HashMap<String, PrintWriter>();
	private static String serverPassword;
	
	// Creates new threads when sensors connect
	// Then starts executing the thread
	public static void main(String[] args) throws Exception{

		System.out.print("Enter a server password: ");
		Scanner in = new Scanner(System.in);
		serverPassword = in.nextLine();
		System.out.println("The Server is running");
		ServerSocket sensorSocket = new ServerSocket(PORT);
		
		try {
			while(true) {
				new Handler(sensorSocket.accept()).start();
			}
		}finally {
			sensorCount -= 1;
			sensorSocket.close();
		}
		
	}
	
	// TO DO : Implement method
	/*private static void sendReadings() {
		// Sends readings to the monitors
	}*/
	
	private static class Handler extends Thread{
		private PrintWriter fileWriter;
		private String sensorId = "";
		private Socket sensorSocket;
		private ObjectInputStream objectInputStream; // Input readings from the sensors
		//private ObjectOutputStream objectOutputStream;
		private BufferedReader in; // Used for authentication
		private PrintWriter out; // Output to the sensors
		private String enteredPassword;
		
		public Handler (Socket sensorSocket) {
			this.sensorSocket = sensorSocket;
		}
		
		public void run() {
			try {
				in =  new BufferedReader(new InputStreamReader(sensorSocket.getInputStream()));
				out = new PrintWriter(sensorSocket.getOutputStream(), true);
				//objectOutputStream = new ObjectOutputStream(sensorSocket.getOutputStream());
				objectInputStream = new ObjectInputStream(sensorSocket.getInputStream());
				
				
				// Authenticating the sensor
				while(true) {
					enteredPassword = in.readLine(); // IN 1
					if (!enteredPassword.equals(serverPassword)) {
						out.println("Incorrect Password"); // OUT 2
						System.out.println(new String("Incorrect Password"));
					}else {
						out.println("Password is Correct"); // OUT 2
						System.out.println("Correct Password");
						break;
					}
				}
				while(true) {
					sensorId = in.readLine(); // IN 3
					//System.out.println("Sensor ID Received: " + sensorId);
					if (printWritersMap.containsKey(sensorId)) {
						System.out.println("Sensor ID is INVALID");
						out.println("INVALID"); // OUT 4
					}
					else {
						System.out.println("Sensor ID is VALID");
						out.println("VALID"); // OUT 4
						break;
					}
							
				}
				printWritersMap.put(sensorId, out);
				//objectsMap.put(sensorId, objectInputStream);
				System.out.println("New sensor connected; Id: " + sensorId);
				
				sensorCount += 1;
				fileWriter = new PrintWriter("data.txt", "UTF-8");
				fileWriter.println("Sensor Count: " + sensorCount);
				fileWriter.close();
				
				// Entering the processing loop
				Reading reading = new Reading();
				while(true) {
					
					try {
						reading = (Reading) objectInputStream.readObject(); // IN 6
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (EOFException e) {
						System.out.println("Sensor has disconnected");
						return;
					}
					
					reading.activateAlert();
					if (reading.isAlert()) {
						reading.PrintAlertReading();
					}
					else {
						reading.PrintReading();
					}
					
					
					
//					try {
//						if(count == 0) {
//							reading = (Reading)objectInputStream.readObject(); // IN 6
//							reading.PrintReading();
//						}
//					} catch(SocketException e) {
//						System.out.println("Sensor has disconnected");
//						return;
//					}catch (IOException e) {
//						e.printStackTrace();
//					}catch (ClassNotFoundException e) {
//						e.printStackTrace();
//					}catch (ClassCastException e) {
//						e.printStackTrace();
//						System.out.println(e.getClass().toString());
//					}
				}
			}
			catch(IOException e) {
				e.printStackTrace();
				System.out.println("OUTER IOException " + e.toString());
			}
			finally{
				// Client has been disconnected
				// TODO : Handle it
			}
		}
		
	}

}
