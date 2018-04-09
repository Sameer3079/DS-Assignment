import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Sensor {

	private int ID = -1;
	private static PrintWriter out;
	private static BufferedReader in;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Sensor sensor = new Sensor();
		sensor.run();
	}
	
	private void run() throws IOException{
		String serverAddress;
		Socket serverSocket = new Socket();
		while(true) {
			try {
				serverAddress = getServerAddress();
				serverSocket= new Socket(serverAddress, 9876);
				break;
			}catch(Exception e) {
				System.out.println("Server address is invalid");
			}
		}
		out = new PrintWriter(serverSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		Authenticate();
		String loginStatus = in.readLine();
		System.out.println("Login Status: " + loginStatus);
		
		ID = Integer.parseInt(in.readLine());
		System.out.println("Sensor Id : " + ID);
		
		serverSocket.close();
	}
	
	private String getServerAddress() {
		System.out.print("Enter Server Address: ");
		Scanner scan = new Scanner(System.in);
		return(scan.nextLine());
	}
	
	private void Authenticate(){
		System.out.print("Enter server password to connect: ");
		Scanner scan = new Scanner(System.in);
		out.println(scan.nextLine());
	}

}
