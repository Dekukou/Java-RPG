import java.io.*;
import java.net.*;

public class ServerThread extends Thread{

    private Server server;
    private Socket socket;
    private int id = -1;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private int[] coords = {0, 0};
    private int health = 100;
    private int score = 0;
    private int nb_color;
    private boolean displayScore = false;
    private String[][] map;
    private String color;
    private String end_color = "\u001B[0m";
    //    private List<Item> bag = new ArrayList<>();


    public ServerThread(Server server, Socket socket, int color, String[][] map) {
	super();
	this.nb_color = color;
	this.map = map;
	this.color = "\u001B[3" + color + "m";
	this.server = server;
	this.socket = socket;		   
	this.id = socket.getPort();
	map = getPos();
	server.sendToClients(id, "");
    }

    public int getColor() {
	return nb_color;
    }

    public String[][] getPos() {
	
	for (int j = 1; j < map[1].length; j++) {
	    if (map[1][j] == " ") {
		coords[0] = 1;
		coords[1] = j;
		map[coords[0]][coords[1]] = color + "X" + end_color;
		break;
	    }
	}
	
	return map;
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

    public String[][] playerInput(String message, String[][] map) {
	map = map;
	switch (message) {
	case "w":
	    if (map[coords[0] - 1][coords[1]] != "-" && !map[coords[0] - 1][coords[1]].equals("\u001B[31mX\u001B[0m") && !map[coords[0] - 1][coords[1]].equals("\u001B[32mX\u001B[0m") && !map[coords[0] - 1][coords[1]].equals("\u001B[33mX\u001B[0m") && !map[coords[0] - 1][coords[1]].equals("\u001B[34mX\u001B[0m")) {
		System.out.println(map[coords[0] - 1][coords[1]]);
		map[coords[0]][coords[1]] = " ";
		coords[0] = coords[0] - 1;
	    }
	    break;
	case "a":
	    if (map[coords[0]][coords[1] - 1] != "H" && !map[coords[0]][coords[1] - 1].equals("\u001B[31mX\u001B[0m") && !map[coords[0]][coords[1] - 1].equals("\u001B[32mX\u001B[0m") && !map[coords[0]][coords[1] - 1].equals("\u001B[33mX\u001B[0m") && !map[coords[0]][coords[1] - 1].equals("\u001B[34mX\u001B[0m")) {
		map[coords[0]][coords[1]] = " ";
		coords[1] = coords[1] - 1;
	    }
            break;
	case "s":
	    if (map[coords[0] + 1][coords[1]] != "-" && !map[coords[0] + 1][coords[1]].equals("\u001B[31mX\u001B[0m") && !map[coords[0] + 1][coords[1]].equals("\u001B[32mX\u001B[0m") && !map[coords[0] + 1][coords[1]].equals("\u001B[33mX\u001B[0m") && !map[coords[0] + 1][coords[1]].equals("\u001B[34mX\u001B[0m")) {
		map[coords[0]][coords[1]] = " ";
		coords[0] = coords[0] + 1;
	    }
            break;
	case "d":
	    if (map[coords[0]][coords[1] + 1] != "H" && !map[coords[0]][coords[1] + 1].equals("\u001B[31mX\u001B[0m") && !map[coords[0]][coords[1] + 1].equals("\u001B[32mX\u001B[0m") && !map[coords[0]][coords[1] + 1].equals("\u001B[33mX\u001B[0m") && !map[coords[0]][coords[1] + 1].equals("\u001B[34mX\u001B[0m")) {
		map[coords[0]][coords[1]] = " ";
		coords[1] = coords[1] + 1;
	    }
            break;
	case "o":
            displayScore = (displayScore ? false : true);
            break;
	default: ;
	    break;
	}
	map[coords[0]][coords[1]] = color + "X" + end_color;
	return map;
    }

    public int getID() {
	return this.id;
    }
    
    public void run() {
	while (true) {
	    try	{
		server.sendToClients(id, dataIn.readUTF());
	    } catch(IOException e) {
		System.out.println("Player n°"+ id + " is deconnected");
		server.remove(id);
		stop();
	    }
	}
    }
    
    public void open() throws IOException {
	dataIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dataOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public boolean getDisplayScore() {
	return displayScore;
    }
    
    public String[][] close() throws IOException {
	map[coords[0]][coords[1]] = " ";
	
	if (socket != null) {
	    socket.close();
	}
	if (dataIn != null) {
	    dataIn.close();
	}
	if (dataOut != null) {
	    dataOut.close();
	}
	return map;
    }
}