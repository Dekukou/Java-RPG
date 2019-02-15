import java.io.*;
import java.net.*;

public class Server extends Stats implements Runnable {

    private int port;
    private ServerSocket serverSocket;
    private Thread thread;
    private ServerThread clients[] = new ServerThread[4];
    private int nb_client = 0;
    private String[][] map = {
	{"H", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "H"},
	{"H", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"3", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"H", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"H", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"H", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"H", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"H", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"H", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "2"},
	{"H", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"1", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"H", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"H", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "H"},
	{"H", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "H"},
    };
    
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
	initMap();
	while (thread != null) {
	    try {
		if (nb_client <= clients.length) {
		    Socket player_id = addThread(serverSocket.accept());
		    System.out.println("Player n°" + player_id.getPort()  + " is connected");
		}
		else {
		    System.out.println("The maximum number of players has already been reached");
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
    
    public void initMap() {
	for (int i = 0; i < map.length; i++) {
	    String tmp = "";
	    for (int j = 0; j < map[i].length; j++)
		tmp += map[i][j];
	    System.out.println(tmp);
	}

    }

    public int getClient(int id) {
	for (int i = 0; i < nb_client; i++)
	    if (clients[i].getID() == id)
		return i;
	return -1;
    }

    public synchronized void sendToClients (int id, String message) {
	for (int i = 0 ; i < nb_client; i++)
	    if (clients[i].getID() == id)
                map = clients[i].playerInput(message, map);
	for (int i = 0 ; i < nb_client; i++) {
	    clients[i].send("\033[H\033[2J");
	    clients[i].send("" + clients[i].getHeal());
	    for (int j = 0; j < map.length; j++) {
		String tmp = "";
		for (int k = 0; k < map[j].length; k++)
		    tmp += map[j][k];
		clients[i].send(tmp);
	    }
	}	
    }

    public synchronized void remove(int id) {
	int client_id = getClient(id);
	
	if (client_id != -1) {
	    ServerThread deletedUser = clients[client_id];
	    for (int i = client_id; i < nb_client - 1; i++)
		clients[i] = clients[i + 1];
	    nb_client--;
	    if (nb_client == 0) {
		try {
		    serverSocket.close();
		    stop();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    try {
		map = deletedUser.close();
	    } catch (IOException e) {
		e.printStackTrace();		
	    }
	    deletedUser.stop();
	} 
    }

    public Socket addThread(Socket socket) {
	if (nb_client < clients.length) {
	    int[] colorArray = {1, 2, 3, 4};
	    
	    if (nb_client > 0) {
		for (int i = 0; i < nb_client; i++)
		    for (int j = 0; j < colorArray.length; j++)
			if (colorArray[j] == clients[i].getColor())
			    for (int k = j; k < colorArray.length - 1; k++)
				colorArray[k] = colorArray[k + 1];
	    }
	    clients[nb_client] = new ServerThread(this, socket, colorArray[0], map);
	    try {
		clients[nb_client].open();
		clients[nb_client].start();
		sendToClients(nb_client, "");
		nb_client++;
	    } catch (IOException e) {
		e.printStackTrace();
	    }    
	}
	return socket;
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
	Server server = new Server(1027);
    }
}
