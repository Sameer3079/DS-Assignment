import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Sensor {

	private int ID = -1;
	private static PrintWriter out;
	private static BufferedReader in;
	private static ObjectOutputStream outputObjectStream;
	Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		Sensor sensor = new Sensor();
		sensor.run();
	}
	
	private void run() throws IOException{
		// Connecting to the Server
		String serverAddress;
		Socket serverSocket = new Socket();
		while(true) {
			try {
				System.out.print("Enter Server Address: ");
				serverAddress = scan.nextLine();
				serverSocket= new Socket(serverAddress, 9876);
				break;
			}catch(Exception e) {
				System.out.println("Server address is invalid");
			}
		}
		out = new PrintWriter(serverSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		outputObjectStream = new ObjectOutputStream(serverSocket.getOutputStream());
		
		// Getting Authenticated
		while(true) {
			System.out.print("Enter server password to connect: ");
			out.println(scan.nextLine()); // OUT 1
			String loginStatus = in.readLine(); // IN 2
			System.out.println("Login Status: " + loginStatus);
			if (loginStatus.equals("Password is Correct"))
				break;
		}
		// Getting an ID Assigned
		String sensorId = "";
		while(true) {
			int floor = -1, sensor = 1;
			while(true) {
				// Getting Floor Number
				try {
					System.out.println("Enter floor number: ");
					floor = scan.nextInt();
					if (floor >= 1 && floor <= 23)
						break;
				}
				catch(InputMismatchException e) {
					System.out.println("Invalid Floor Number Entered");
				}
				
			}
			// Getting Sensor Number
			while(true) {
				try {
					System.out.println("Enter sensor number: ");
					sensor = scan.nextInt();
					if (sensor >= 1 && sensor <= 13)
						break;
				}
				catch(InputMismatchException e) {
					System.out.println("Invalid Sensor Number Entered");
				}
			}
			sensorId = floor + "-" + sensor;
			out.println(sensorId); // OUT 3
			if ("VALID".equals(in.readLine())) { // IN 4
				System.out.println("ID is valid");
				System.out.println("ID: " + sensorId);
				break;
			}
			else
				System.out.println("Sensor has already been registered");
		}
		
		// Entering processing loop
		Reading reading = new Reading();
		int count= 0, sleepDuration = 10;
		while(true) {
			try {
				Thread.sleep(sleepDuration);
				count += 1;
				count %= 2;
				reading = GenerateReadings();
				reading.activateAlert();
				reading.setSensorId(sensorId);
				
				if (count == 0) {
					System.out.println("COUNT = 0");
					outputObjectStream.writeObject(reading); // OUT 6
				}

				reading.PrintReading();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				System.out.println("Connection lost with server");
				return;
			}
			
		}
		
		//serverSocket.close();
	}
	
	private Reading GenerateReadings() {
		// TODO : Improve Generation of random numbers
		Random random = new Random();
		Reading data = new Reading();
		data.setTemperature(random.nextInt(50));
		data.setBatteryLevel(random.nextInt(100));
		data.setSmokeLevel(random.nextInt(10));
		data.setCo2Level(random.nextInt(300));
		
		return data;
	}

}
