import java.io.*;
import java.net.*;

public class Player implements Runnable {


    private Socket socket = null;
    private PlayerThread player = null;
    private DataInputStream dataIn = null;
    private DataOutputStream dataOut = null;
    private String serverAddress;
    private int port;
    private Thread thread;
    private boolean isClosed = false;

    public Player(String serverAddress, int port) {
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
		dataOut.writeUTF("\033[H\033[2J");
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
	    player = new PlayerThread(this, socket);
	    thread = new Thread(this);
	    thread.start();
	}
    }

    public void stop() {
	isClosed = true;
	if (thread != null) {
	    thread.stop();
	    thread = null;
	}
	try {
	    if (dataIn != null) {
		dataIn.close();
	    }
	    if (dataOut != null) {
		dataOut.close();
	    }
	    if (socket != null) {
		socket.close();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	player.close();
	player.stop();
    }

    public static void main(String args[]) {
	Thread thread = new Thread(new Player("127.0.0.1", 1027));
    }
}