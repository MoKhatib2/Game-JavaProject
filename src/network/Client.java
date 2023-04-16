package network;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import model.abilities.*;
import model.world.Champion;
import engine.Game;
import engine.Player;

public class Client{
	
	private int player = 0;
	private Game game;
	public ArrayList<Champion> champions;
	public ArrayList<Ability> abilities;
	private SocketAction s;
	private boolean gameStarted;
	private GameMessage gm;
	
	public Client(String name, String ip) throws UnknownHostException, IOException {
		game = new Game(new Player("dasd"), new Player("fsdf"));
		
		Socket socket = new Socket(ip, 7378);

		s = new SocketAction(socket);
		s.send(name);
		
		String in = "";
		try {
			in = s.receive();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (in.equals("1")){
			player = 1;		
		}
		else if (in.equals("2")){
			player = 2;
		}
		
		waitForGame();
		run();
	}
	
	public void waitForGame(){
		gm = null;
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				while(gm != null){

					gm = (GameMessage) s.receiveObject();
					System.out.println("Client received game started");
					game = gm.game;
					gameStarted = true;
					
					abilities = (ArrayList<Ability>) ((ArrayList<Ability>) s.receiveObject()).clone();
					champions = (ArrayList<Champion>) ((ArrayList<Champion>) s.receiveObject()).clone();
					if (player == 2){
						interpret((GameMessage) s.receiveObject());
						break;
					}
				}
				
			}
			
		}).start();
		
		
	}
	
	
	private void interpret(GameMessage message) {
		
		
	}

	public void run(){
		if (gameStarted){
			//try {
			//	sleep(1000);
			//} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
			System.out.println("Client running");
		}
	}
	
	public Game getGame(){
		return game;
	}
	
	public int getPlayerNumber(){
		return player;
	}
	public boolean isGameStarted(){
		return gameStarted;
	}
}
