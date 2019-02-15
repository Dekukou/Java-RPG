import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

public class ServerThread extends Thread {

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
    private int heal;
    private List bag = new ArrayList(2);
    private Weapon weapon;
    private Stats stats;

    public ServerThread(Server server, Socket socket, int color, String[][] map) {
	super();
	this.heal = 100;
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
	while (map[coords[0]][coords[1]] != " ") {
	    coords[1] = 1 + (int)(Math.random() * (map.length - 2));
	    coords[0] = 1 + (int)(Math.random() * (map[0].length - 2));
	}
	map[coords[0]][coords[1]] = color + "X" + end_color;
	
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

    public int getHeal() {
        return heal;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void removeWeapon() {
        if (!bag.contains(weapon))
            bag.add(weapon);
        if (this.getWeapon() != null)
            this.getWeapon().remove(this);
    }

    public void putWeapon(Weapon weapon) {

        if (weapon != null)
            this.removeWeapon();
        if (bag.contains(weapon)) {
            bag.remove(weapon);
            setWeapon(weapon);
        }
        weapon.put(this);
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