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
    private boolean isClosed = false;

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
		String msg = dataIn.readLine();
		    System.out.println(msg);
		    dataOut.writeUTF(msg);
		    dataOut.flush();
	    } catch (IOException e) {
		e.printStackTrace();
		stop();
	    }
	}
    }
    
    public void sendMessage(String message) {
	if (message.equals("exit")) {
	    stop();
	}
	else {
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	    System.out.println(message);
	}
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
	System.out.println("I am GROOT");
	isClosed = true;
	if (thread != null) {
	    thread.stop();
	    thread = null;
	}
	try {
	    if (dataIn != null) {
		System.out.println("TSET");
		dataIn.close();
	    }
	    else 
		System.out.println("TEST");
	    if (dataOut != null) {
		dataOut.close();
		System.out.println("test2");
	    }
	    if (socket != null) {
		socket.close();
		System.out.println("test3");
	    }
	} catch (IOException e) {
	    System.out.println("GROOT");
	    e.printStackTrace();
	}
	player.close();
	player.stop();
    }

    public static void main(String args[]) {
	Thread thread = new Thread(new Client("127.0.0.1", 1027));
    }
}