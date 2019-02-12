import java.io.*;
import java.net.*;

public class Client implements Runnable {


    private Socket socket = null;
    private ClientThread player = null;
    private DataInputStream dataIn = null;
    private DataOutputStream dataOut = null;
    private String serverAddress;
    private int port;
    private Thread thread;

    public Client(String serverAddress, int port) {
	this.serverAddress = serverAddress;
	this.port = port;
	connect();
    }

    public Socket connect() {	
	try {
	    socket = new Socket(serverAddress, port); 
	    start();
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return socket;
    }

    public void run() {
	while (thread != null) {
	    try {
		dataOut.writeUTF(dataIn.readLine());
		dataOut.flush();
	    } catch (IOException e) {
		e.printStackTrace();
		stop();
	    }
	}
    }
    
    public void sendMessage(String message) {
	if (message.equals("exit"))
	    stop();
	else
	    System.out.println(message);
    }

    public void start() {
	
	try {
	    dataIn = new DataInputStream(System.in);
	    dataOut = new DataOutputStream(socket.getOutputStream());
	} catch (IOException e) {
	    e.printStackTrace();
	}
	if (thread == null) {
	    player = new ClientThread(this, socket);
	    thread = new Thread(this);
	    thread.start();
	}
    }

    public void stop() {
	if (thread != null) {
	    thread.stop();
	    thread = null;
	}
	try {
	    dataIn.close();
	    dataOut.close();
	    socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	player.close();
	player.stop();
    }

    public static void main(String args[]) {
	Thread thread = new Thread(new Client("127.0.0.1", 1027));
    }
}