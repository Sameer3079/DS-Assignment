

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Server{

	private static final int PORT = 9876;
	
	private static HashMap<Integer, ObjectOutputStream> objectsMap = new HashMap<Integer, ObjectOutputStream>();
	private static HashMap<Integer, PrintWriter> printWritersMap = new HashMap<Integer, PrintWriter>();
	private static String serverPassword;
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

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
			sensorSocket.close();
		}
		
	}
	
	// TO DO : Implement method
	/*private static void sendReadings() {
		// Sends readings to the monitors
	}*/
	
	private static class Handler extends Thread{
		private Integer sensorId = -1;
		private Socket sensorSocket;
		//private ObjectInputStream inReading; // Input readings from the sensors
		private ObjectOutputStream objectStream;
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
				objectStream = new ObjectOutputStream(sensorSocket.getOutputStream());
				
				// Authenticating the sensor
				while(true) {
					//out.println("ENTER_SERVER_PASSWORD");
					enteredPassword = in.readLine();					
					if (!enteredPassword.equals(serverPassword)) {
						out.println("Incorrect Password");
						System.out.println(new String("Incorrect Password"));
						return;
					}
					else {
						out.println("Password is Correct");
						System.out.println("Correct Password");
						break;
					}
				}
				// Assigning an id to the sensor
				Integer maxId = -1;
				for (Integer x : printWritersMap.keySet()) {
					if (x > maxId)
						maxId = x;
				}
				sensorId = maxId + 1;
				
				out.println(sensorId);
				
				printWritersMap.put(sensorId, out);
				objectsMap.put(sensorId, objectStream);
				
				System.out.println("New sensor connected; Id: " + sensorId);
			}
			catch(IOException e) {
				System.out.println("Error in run() : " + e.toString());
			}
			finally{
				// Client has been disconnected
				// TODO : Handle it
			}
		}
	}

}
