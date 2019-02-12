import java.io.*;
import java.net.*;

public class Server {

    private int port;
    public static boolean isRunning = true;
    private ServerSocket serverSocket;
    private Socket socket;
    private Thread thread;
    private ServerThread serverThread;

    public Server(int port) {
	try {
	    serverSocket = new ServerSocket(port);
	    System.out.println("Server started: @" + serverSocket.getInetAddress().getHostAddress().toString() + " port:" + serverSocket.getLocalPort());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void run() {
	while (thread != null) {
	    try
		{
		    System.out.println("Wait");
		    addThread(server.accept());
		} catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
    public void addThread(Socket socket) {
	
	serverThread = new ServerThread(this, socket);
	try {
	    client.open();
	    client.start();
	} catch (IOException e) {
	    e.printStackTrace();
	}    
    }

    public void start() {
	if (thread == null) {
	    thread = new Thread(this); 
	    thread.start();
	}
    }
	
    public void stop() {
	if (thread != null) {
	    thread.stop(); 
	    thread = null;
	}
    }

    public static void main(String args[]) {
	ChatServer server = null;
	if (args.length != 1)
	    System.out.println("Usage: java ChatServer port");
	else
	    server = new ChatServer(Integer.parseInt(args[0]));
    }
}
