package network;

import java.io.IOException;

import model.world.Champion;
import engine.Game;
import engine.Player;
import views.MessageBox;

public class OnlineGame{
	
	private GamePlayer player1, player2, cur;
	private Game game;
	
	
	public OnlineGame(GamePlayer p1, GamePlayer p2) {
		System.out.println("Game started");
		player1 = p1;
		player2 = p2;
		cur = player1;
		game = new Game(player1.getPlayer(), player2.getPlayer());
		try {
			Game.loadAbilities("Abilities.csv");
			Game.loadChampions("Champions.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		player1.sendObject(new GameMessage("start", game, false));
		player2.sendObject(new GameMessage("start", game, false));
		player1.sendObject(Game.getAvailableAbilities());
		player2.sendObject(Game.getAvailableAbilities());
		player1.sendObject(Game.getAvailableChampions());
		player2.sendObject(Game.getAvailableChampions());
		System.out.println("sent abilities and champions to both players");
	}
	

	public void play() {
		if (cur == player1){
			GameMessage message = (GameMessage)player1.receiveObject();
			interpret(message);
			if (!message.wait)
				cur = player2;
			
		}
		else if (cur == player2){
			
		}
	}
	public void interpret(GameMessage gm){
		//GameMessage receives no Game Object
		String[] words = gm.message.split(" ");
		switch(words[0]){
		case "addedChampion":
			Champion selected = null;
			for(Champion c : game.getAvailableChampions()){
				if (words[1].equals(c.getName()))
					selected = c;
			}
			if (cur == player1){
				player1.getPlayer().getTeam().add(selected);
				cur = player2;
			}
			else if (cur == player2){
				player2.getPlayer().getTeam().add(selected);
				cur = player1;
			}
			break;
		case "setLeader":
			selected = null;
			
			if (cur == player1){
				for(Champion c : player1.getPlayer().getTeam()){
					if (words[1].equals(c.getName()))
						selected = c;
				}
				player1.getPlayer().setLeader(selected);
			}
			else if (cur == player2){
				for(Champion c : player2.getPlayer().getTeam()){
					if (words[1].equals(c.getName()))
						selected = c;
				}
				player2.getPlayer().setLeader(selected);
			}
			break;
		}
	}
}
