package network;

import java.net.Socket;

import model.effects.Effect;
import engine.Player;

public class GamePlayer extends SocketAction implements Cloneable{
	
	private GameDaemon daemon = null;
	private Player player = null;
	
	
	public GamePlayer(GameDaemon server, Socket sock) {
		super(sock);
		daemon = server;
	}
	
	public void run() {
		daemon.waitForGame(this);
	}
	
	public void closeConnections() {
		super.closeConnections();
		if (outStream != null) {
			send("Connection closed");
		}
	}
	public void createPlayer(String name){
		player = new Player(name);
		System.out.println("Created new player with name: " + name);
	}
	public void setPlayer(Player p){
		player = p;
	}
	public Player getPlayer(){
		return player;
	}
	public Object clone() throws CloneNotSupportedException{
		GamePlayer r = new GamePlayer(this.daemon, this.getSocket());
		r.setPlayer(this.player);
		return r;
	}
}
