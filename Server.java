import java.io.*;
import java.net.*;

public class Server implements Runnable {

    private int port;
    private ServerSocket serverSocket;
    private Thread thread;
    private ServerThread clients[] = new ServerThread[5];
    private int nb_client = 0;

    public Server(int port) {
	try {
	    serverSocket = new ServerSocket(port);
	    System.out.println("Server started: @" + serverSocket.getInetAddress().getHostAddress().toString() + " port:" + serverSocket.getLocalPort());
	    start();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void run() {
	while (thread != null) {
	    try {
		System.out.println("Wait");
		addThread(serverSocket.accept());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
    
    private int getClient(int id) {
	for (int i = 0; i < nb_client; i++)
	    if (clients[i].getID() == id)
		return i;
	return -1;
    }

    public synchronized void sendToClients (int id, String message) {
	if (message.equals("exit"))
	    remove(id);
	else 
	    for (int i = 0 ; i < nb_client; i++)
		if (clients[i].getID() != id)
		    clients[i].send(id + ": "+ message);
    }

    public synchronized void remove(int id) {
	int client_id = getClient(id);

	if (client_id != -1) {
	    ServerThread deletedUser = clients[client_id];
	    if (client_id < nb_client - 1)
		for (int i = client_id; i < nb_client; i++)
		    clients[i -1] = clients[i];
	    nb_client--;
	    
	    try {
		deletedUser.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    deletedUser.stop();
	}
    }

    public void addThread(Socket socket) {
	if (nb_client < clients.length) {
	    clients[nb_client] = new ServerThread(this, socket);
	    try {
		clients[nb_client].open();
		clients[nb_client].start();
		nb_client++;
	    } catch (IOException e) {
		e.printStackTrace();
	    }    
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
	Server server = null;
	server = new Server(1027);
    }
}
