import java.io.*;
import java.net.*;

public class PlayerThread extends Thread {
    
    private Socket socket = null;
    private Player player = null;
    private DataInputStream dataIn = null;
    public boolean isClosed = false;

    public PlayerThread(Player player, Socket socket) {
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
	isClosed = true;
	socket.close();
	} catch (IOException e) {	    
	    e.printStackTrace();
	}
    }
  
    public void run() {
	while (true) {
	    try {
		while (dataIn.available() > 0) {
		    String msg = dataIn.readUTF();
		    player.sendMessage(msg);
		}
	    } catch (IOException e) {
		e.printStackTrace();
		player.stop();		
	    }
	}
    }
}