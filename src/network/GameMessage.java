package network;

import java.io.Serializable;

import engine.Game;

public class GameMessage implements Serializable{
	String message;
	Game game;
	boolean wait;
	
	public GameMessage(String message, Game game, boolean wait){
		this.message = message;
		this.game = game;
		this.wait = wait;
	}
	
	public void interpretPlayer(){
		
	}
}
