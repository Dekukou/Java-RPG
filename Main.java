import java.io.*;
import java.net.*;


public class Main {

    public static void main(String[] args) {
	String host = "127.0.0.1";
	int port = 1027;
	
	Server server = new Server(port);
	server.start();
	
	for (int i = 0; i < 5; i++) {
	    Client client = new Client(host, port);
	    Thread thread = new Thread(client);
	    thread.start();
	}
    }
}