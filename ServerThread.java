import java.io.*;
import java.net.*;

public class ServerThread {

    private Server server;
    private Socket socket;
    private int id = -1;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    public ServerThread(Server server, Socket socket) {
	super();
	this.server = server;
	this.socket = socket;		   
	this.id = socket.getPort();
    }

    
    public void send(String message) {
	try {
	    dataOut.writeUTF(message);
	    dataOut.flush();
	} catch (IOException e) {
	    server.remove(id);
	    stop();
	}
    }
    
}