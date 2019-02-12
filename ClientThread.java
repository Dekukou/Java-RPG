import java.io.*;
import java.net.*;

public class ClientThread extends Thread {
    
    private Socket socket = null;
    private Client player = null;
    private DataInputStream dataIn = null;
    
    public ClientThread(Client player, Socket socket) {
	this.player = player;
	this.socket = socket;
	open();
	start();
    }
    
    public void open() {
	try {
	    dataIn = new DataInputStream(socket.getInputStream());
	} catch (IOException e) {
	    e.printStackTrace();
	    player.stop();
	}
    }
    
    public void close() {
	try {
	    dataIn.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void run() {
	while (true) {
	    try {
		if (socket.isConnected())
		    player.sendMessage(dataIn.readUTF());
	    } catch (IOException e) {
		e.printStackTrace();
		player.stop();
	    }
	}
    }
}