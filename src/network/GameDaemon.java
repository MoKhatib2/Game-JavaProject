package network;

import java.io.IOException;
import java.net.*;

import views.MessageBox;
import engine.Game;

public class GameDaemon {
	
	public ServerSocket port;
	public GamePlayer playerWaiting;
	public OnlineGame thisGame;
	private int playerCount = 0;
	
	public void run() {
		Socket clientSocket;
		try {
			port = new ServerSocket(7378);
		} catch (IOException e1) {
			MessageBox.showError(e1.getMessage());
		}
		while (playerCount < 2) {
			if (port == null) {
				System.out.println("Sorry, the port disappeared.");
				System.exit(1);
			}
			try {
				clientSocket = port.accept();
				GamePlayer p = new GamePlayer(this, clientSocket);
				String name = p.receive();
				playerCount++;
				p.createPlayer(name);
				p.start();
				
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void start() {
		try {
			port = new ServerSocket();
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void waitForGame(GamePlayer p) {
		
		if (playerWaiting == null){
			playerWaiting = p;
			p.send("1");
			System.out.println(p.getPlayer().getName() + " is waiting for another player");
			while (playerWaiting != null) {
				try {
					wait();
				}
				catch (InterruptedException e) {
					System.out.println("Error:" + e);
				}
			}
		}
		else {
			p.send("2");
			try {
				new OnlineGame((GamePlayer) playerWaiting.clone(), p);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			playerWaiting = null;
			notify();
		}
	}
}
