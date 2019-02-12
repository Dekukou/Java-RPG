import java.io.*;
import java.net.*;

public class Client implements Runnable {


    private PrintWriter printWriter = null;
    private BufferedReader bufferedReader = null;
    private PrintStream printStream = null;
    private DataInputStream dataInputStream = null;
    private String serverAddress;
    private Socket socket = null;
    private String name;
    private int port;

    public Client(String serverAddress, int port) {
	this.serverAddress = serverAddress;
	this.port = port;
    }

    public Socket connect() {	
	try {
	    socket = new Socket(serverAddress, port); 
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return socket;
    }

    public void run() {
	connect();
	try {
            Thread.currentThread().sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	sendMessage();
	try {
	socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void sendMessage() {
	try {
	    Thread.currentThread().sleep(1);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	
	try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	    printStream = new PrintStream(socket.getOutputStream());
	    dataInputStream = new DataInputStream(socket.getInputStream());
	    String readerInput = "";
	    while (readerInput != null && !readerInput.equals("closeConnection")) {
		readerInput = bufferedReader.readLine();
		printWriter.println(readerInput);
	    }
        } catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String args[]) {
	Thread thread = new Thread(new Client("127.0.0.1", 1027));
	thread.start();
    }
}