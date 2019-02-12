import java.io.*;
import java.net.*;

public class ClientProcessor implements Runnable {

    private Socket socket;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;

    public ClientProcessor(Socket socket) {
	this.socket = socket;
    }

    public void run() {

	System.out.println("Début de la connexion");
	boolean close = false;

	while(!socket.isClosed()) {
	    try {
		String message = null;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		
		while ((message = bufferedReader.readLine()) != null && !message.equals("closeConnection")) {
		    System.out.println(message);
		    dos.writeUTF(message);
		}
		Server.close();
		socket.close();
		break;
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}