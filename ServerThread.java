import java.io.*;
import java.net.*;

public class ServerThread extends Thread{

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

    public int getID() {
	return this.id;
    }
    
    public void run() {
	while (true) {
	    try	{
		server.sendToClients(id, dataIn.readUTF());
	    } catch(IOException e) {
		server.remove(id);
		stop();
	    }
	}
    }
    
    public void open() throws IOException {
	dataIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dataOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }
    
    public void close() throws IOException {
	if (socket != null) {
	    socket.close();
	    System.out.println("test");
	}
	if (dataIn != null) {
	    dataIn.close();
	    System.out.println("test2");
	}
	if (dataOut != null) {
	    dataOut.close();
	    System.out.println("test3");
	}
    }
}