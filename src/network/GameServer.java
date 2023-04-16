package network;

public class GameServer {
	public static boolean running = false;
	public static void main(String args[ ]) {
		System.out.println("Game server up and running...");
		if (!running) new GameDaemon().start();
	}
}
