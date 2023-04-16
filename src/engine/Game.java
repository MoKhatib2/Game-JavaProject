package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import views.MessageBox;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class Game implements Serializable{
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private Player firstPlayer;
	private Player secondPlayer;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player first, Player second) {
		firstPlayer = first;
		secondPlayer = second;
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		turnOrder = new PriorityQueue(6);
		startGame();
	}
	public void startGame(){
		prepareChampionTurns();
		placeChampions();
		placeCovers();
	}

	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}

	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
			case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}

	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
			int y = (int) (Math.random() * BOARDHEIGHT);

			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}

	}

	public void placeChampions() {
		int i = 1;
		for (Champion c : firstPlayer.getTeam()) {
			board[0][i] = c;
			c.setLocation(new Point(0, i));
			i++;
		}
		i = 1;
		for (Champion c : secondPlayer.getTeam()) {
			board[BOARDHEIGHT - 1][i] = c;
			c.setLocation(new Point(BOARDHEIGHT - 1, i));
			i++;
		}
	
	}
	
	public Champion getCurrentChampion(){
		return (Champion)turnOrder.peekMin();
	}
	
	public Player checkGameOver(){
		/*int l1 = 0;
		int l2 = 0;
		for(int i= 0;i< firstPlayer.getTeam().size();i++){
			if(firstPlayer.getTeam().get(i).getCondition()!=Condition.KNOCKEDOUT)
				l1++;
		}
		for(int i= 0;i< secondPlayer.getTeam().size();i++){
			if(secondPlayer.getTeam().get(i).getCondition()!=Condition.KNOCKEDOUT)
				l2++;
		}
		if(l1>0 && l2 == 0)
			return firstPlayer;
		if(l2>0 && l1 == 0)
			return secondPlayer;*/
		
		if (firstPlayer.getTeam().size() == 0)
			return secondPlayer;
		if (secondPlayer.getTeam().size() == 0)
			return firstPlayer;
		
		return null;
		
	}
	
	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException{
		
		if (this.getCurrentChampion().getCondition() == Condition.ROOTED)
			throw new UnallowedMovementException("Champion rooted");
		if (this.getCurrentChampion().getCurrentActionPoints() < 1)
			throw new NotEnoughResourcesException("Not enough action points");
		
		int x = this.getCurrentChampion().getLocation().x;
		int newx = this.getCurrentChampion().getLocation().x;
		int y = this.getCurrentChampion().getLocation().y;
		int newy = this.getCurrentChampion().getLocation().y;
		
		if(d == Direction.DOWN && x - 1 >= 0 && this.board[x - 1][y] == null)
			newx = x - 1;
		else if(d == Direction.UP && x + 1 < getBoardheight() && this.board[x + 1][y] == null)
			newx = x + 1;
		else if(d == Direction.RIGHT && y + 1 < getBoardwidth() && this.board[x][y + 1] == null)
			newy = y + 1;
		else if(d == Direction.LEFT && y - 1 >= 0 && this.board[x][y - 1] == null)
			newy = y - 1;
		else 
			throw new UnallowedMovementException("Cannot move in that direction");
		
		if(newx != x || newy != y){
			this.getCurrentChampion().setLocation(new Point(newx, newy));
			this.board[newx][newy] = this.board[x][y];
			this.board[x][y] = null;
			this.getCurrentChampion().setCurrentActionPoints(this.getCurrentChampion().getCurrentActionPoints() - 1);
		}
			
	}
	public static void main(String[] args){
		
		Player ana = new Player("omar");
		Player mo = new Player("mo");
		
		
		Game g = new Game(ana, mo);
		try {
			loadChampions("Champions.csv");
			loadAbilities("Abilities.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ana.getTeam().add(g.getAvailableChampions().get(0));
		ana.getTeam().add(g.getAvailableChampions().get(1));
		ana.getTeam().add(g.getAvailableChampions().get(2));
		mo.getTeam().add(g.getAvailableChampions().get(3));
		mo.getTeam().add(g.getAvailableChampions().get(4));
		mo.getTeam().add(g.getAvailableChampions().get(5));
		g.placeChampions();
		
		for(int i = 0; i < 3; i++)
			g.getTurnOrder().insert(ana.getTeam().get(i));
		for(int i = 0; i < 3; i++)
			g.getTurnOrder().insert(mo.getTeam().get(i));
		g.printBoard();
		System.out.println();
		try {
			g.getCurrentChampion().setCurrentActionPoints(0);
			g.attack(Direction.LEFT);
		} catch (NotEnoughResourcesException | ChampionDisarmedException | InvalidTargetException e) {
			MessageBox.showError(e.getMessage());
		}		
		g.endTurn();
	}
	
	private void printBoard() {
		for(int i = 4; i >=0; i--) {
			for(int j = 0; j < 5; j++) {
				
				if (board[i][j] instanceof Cover) System.out.print(" [=] ");
				else if (board[i][j] instanceof Champion && !this.getTurnOrder().isEmpty() && (Champion)board[i][j] == this.getCurrentChampion()) System.out.print(" [@] ");
				else if (board[i][j] instanceof Champion) System.out.print(" [C] ");
				else System.out.print("  0  ");
				
			}
			System.out.println();
		}
		
	}
	
	public void attack(Direction d) throws NotEnoughResourcesException, ChampionDisarmedException, InvalidTargetException{
		Champion c = this.getCurrentChampion();
		if (c.getCurrentActionPoints() < 2)
			throw new NotEnoughResourcesException("Not enough action points");
		for(Effect effect : c.getAppliedEffects())
			if (effect instanceof Disarm)
				throw new ChampionDisarmedException("M3aksh sela7 ya abooz");
		
		c.setCurrentActionPoints(c.getCurrentActionPoints() - 2);
		ArrayList<Damageable> possibletargets = getDirectionalTargets(d, c.getLocation(), c.getAttackRange());
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		if (possibletargets.size() != 0) targets.add(possibletargets.get(0));
		
		for(Damageable target : targets)
			if (target == null)
				return;
			else if (target instanceof Cover){
				target.setCurrentHP(target.getCurrentHP() - c.getAttackDamage());
				removeIf0(target);
			}
			else if (target instanceof Champion){
				
				//if (sameTeam((Champion)target, c))
					//throw new InvalidTargetException("Enta btedrab sa7bak yabn el");	
				
				Effect dodge = null, shield = null;
				for (Effect e : ((Champion)target).getAppliedEffects()){
					if (e instanceof Dodge)
						dodge = e;
					else if (e instanceof Shield)
						shield = e;
				}
				
				if (shield != null){
					shield.remove((Champion)target);
					return;
				}
				
				if (dodge != null){
					int chance = (int)(Math.random() * 2);
					if (chance == 0)
						return;
				}
				
				target.setCurrentHP(((Champion)target).getCurrentHP() - getFinalAttackDamage(c, (Champion)target));
				removeIf0(target);
			}
		//}
	}
	
	public boolean sameTeam(Champion c1, Champion c2){
		boolean first1 = true, first2 = true, second1 = false, second2 = false;
		for(Champion c : this.getSecondPlayer().getTeam())
			if (c == c1){
				first1 = false;
				second1 = true;
			}
		for(Champion c : this.getSecondPlayer().getTeam())
			if (c == c2){
				first2 = false;
				second2 = true;
			}
		if (second1 && second2)
			return true;
		if (first1 && first2)
			return true;
		return false;
	}

	public void castAbility(Ability a) throws NotEnoughResourcesException, AbilityUseException, CloneNotSupportedException{
		Champion c = this.getCurrentChampion();
		
		if (c.getMana() < a.getManaCost() || c.getCurrentActionPoints() < a.getRequiredActionPoints())
			throw new NotEnoughResourcesException("M3aksh resources yahbal");
		
		if(a.getCurrentCooldown() != 0)
            throw new AbilityUseException("Ability is on cooldown");
		
        
        for (Effect e : c.getAppliedEffects())
        	if (e instanceof Silence)
        		throw new AbilityUseException("Cannot use abilities while silenced");
		
		AreaOfEffect area = a.getCastArea();
		ArrayList<Damageable> possibletargets = new ArrayList<Damageable>();
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		
		if (a instanceof DamagingAbility && (area == AreaOfEffect.SURROUND || area == AreaOfEffect.TEAMTARGET))
			targets = getTargets(area, a.getCastRange(), true, true);
		
		else if (a instanceof HealingAbility && (area == AreaOfEffect.SURROUND || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SELFTARGET))
			targets = getTargets(area, a.getCastRange(), false, false);
		
		else if (a instanceof CrowdControlAbility && ((CrowdControlAbility)a).getEffect().getType() == EffectType.DEBUFF && (area == AreaOfEffect.SURROUND || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SELFTARGET))
			targets = getTargets(area, a.getCastRange(), true, false);
		
		else if (a instanceof CrowdControlAbility && ((CrowdControlAbility)a).getEffect().getType() == EffectType.BUFF && (area == AreaOfEffect.SURROUND || area == AreaOfEffect.TEAMTARGET || area == AreaOfEffect.SELFTARGET))
			targets = getTargets(area, a.getCastRange(), false, false);
		
		//else
			//throw new AbilityUseException("No valid targets found");
		
		a.execute(targets);
		for(Damageable target : targets)
			removeIf0(target);
		
		c.setMana(c.getMana() - a.getManaCost());
		c.setCurrentActionPoints(c.getCurrentActionPoints() - a.getRequiredActionPoints());
        a.setCurrentCooldown(a.getBaseCooldown());
	}

	public void castAbility(Ability a, Direction d) throws NotEnoughResourcesException, InvalidTargetException, AbilityUseException, CloneNotSupportedException{
		Champion c = this.getCurrentChampion();
        if(c.getCurrentActionPoints()<a.getRequiredActionPoints() || c.getMana()<a.getManaCost()){
            throw new NotEnoughResourcesException();
        }
        for (Effect e : c.getAppliedEffects())
        	if (e instanceof Silence)
        		throw new AbilityUseException("Cannot use abilities while silenced");
		
        if(a.getCurrentCooldown() != 0)
            throw new AbilityUseException("Ability is on cooldown");
        
        
        
        ArrayList<Damageable> possibletargets =  getDirectionalTargets(d, c.getLocation(), a.getCastRange());
        ArrayList<Damageable> targets = new ArrayList<Damageable>();
        
        if (a instanceof HealingAbility){
        	for(Damageable target : possibletargets)
        		if (target instanceof Champion && sameTeam((Champion)target, c))
        			targets.add(target);
        }
        else if (a instanceof DamagingAbility)
        	for(Damageable target : possibletargets){
        		if ((target instanceof Champion && !sameTeam((Champion)target, c)) || target instanceof Cover)
        			targets.add(target);
        	}
		else if (a instanceof CrowdControlAbility){
			if (((CrowdControlAbility) a).getEffect().getType() == EffectType.DEBUFF){
				for(Damageable target : possibletargets)
	        		if ((target instanceof Champion && !sameTeam((Champion)target, c)) || target instanceof Cover)
	        			targets.add(target);
			}
    		else if (((CrowdControlAbility) a).getEffect().getType() == EffectType.BUFF){
    			for(Damageable target : possibletargets)
            		if (target instanceof Champion && sameTeam((Champion)target, c))
            			targets.add(target);
    		}
		}
        //else
            //throw new InvalidTargetException();
            //}
        a.execute(targets);
        for (Damageable target : targets)
        	removeIf0(target);
        c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
        c.setMana(c.getMana()-a.getManaCost());
        a.setCurrentCooldown(a.getBaseCooldown());
    }
	
	public void castAbility(Ability a, int x, int y) throws NotEnoughResourcesException, AbilityUseException, InvalidTargetException, CloneNotSupportedException{
		
		Champion c = this.getCurrentChampion();
		Damageable target = (Damageable)this.getBoard()[x][y];
        
		if(c.getCurrentActionPoints()<a.getRequiredActionPoints() || c.getMana()<a.getManaCost())
            throw new NotEnoughResourcesException("M3aksh resources yahbal");
        
		for (Effect e : c.getAppliedEffects())
        	if (e instanceof Silence)
        		throw new AbilityUseException("Cannot use abilities while silenced");

        if(a.getCurrentCooldown() != 0)
            throw new AbilityUseException("Ability is on cooldown");
        
        if (this.getBoard()[x][y] == null)
        	throw new InvalidTargetException("Invalid target");
        
        if (ManhattanDistance(c.getLocation(), new Point(x, y)) > a.getCastRange())
        	throw new AbilityUseException("Target out of cast range");
        
        
    	
        ArrayList<Damageable> targets = new ArrayList<Damageable>();
        
        Boolean Ally = false;
        if (target instanceof Champion)
        	Ally = sameTeam((Champion)target, c);
        
        if(target != null){
        	
        	if((target instanceof Champion && a instanceof HealingAbility && Ally) || (a instanceof DamagingAbility && !Ally))
            	targets.add(target);
            else if(a instanceof CrowdControlAbility && (target instanceof Champion)){
                CrowdControlAbility cca = (CrowdControlAbility)a;
                if((cca.getEffect().getType()==EffectType.BUFF && Ally) || (cca.getEffect().getType()==EffectType.DEBUFF && !Ally && !(target instanceof Cover))){
                	targets.add(target);
                }
                else{
                    if (Ally) throw new InvalidTargetException("Target cannot be an ally");
                    else throw new InvalidTargetException("Target must be an ally");
                }
            }
        	
            else
            	throw new InvalidTargetException("Cannot target covers with crowd control abilities");
        	a.execute(targets);
        	removeIf0(target);
        }
        
        c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
        c.setMana(c.getMana()-a.getManaCost());
        a.setCurrentCooldown(a.getBaseCooldown());
	}
	public Shield hasShield(Champion c){
		for(Effect e : c.getAppliedEffects())
			if (e instanceof Shield)
				return (Shield) e;
		return null;
	}
	public void useLeaderAbility() throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException, CloneNotSupportedException{
		Champion c = this.getCurrentChampion();
		if(!(c.equals(this.getFirstPlayer().getLeader())) && !(c.equals(this.getSecondPlayer().getLeader()))){
			throw new LeaderNotCurrentException("Current Champion is not Leader");
		}
		ArrayList<Champion> targets = new ArrayList<Champion>(); 
		
		boolean First = false;
		for(int i = 0; i < this.getFirstPlayer().getTeam().size();i++){
			if(c == this.getFirstPlayer().getTeam().get(i)){
				First = true;
			}
		}
		
		if((this.firstLeaderAbilityUsed && First)||(this.secondLeaderAbilityUsed && !First))
			throw new LeaderAbilityAlreadyUsedException();
	
		if(c instanceof Hero){
			if(First)
				for(Champion t : this.getFirstPlayer().getTeam())
					targets.add(t);
			else
				for(Champion t : this.getSecondPlayer().getTeam())
					targets.add(t);
			
		}
		else if(c instanceof Villain){
			if(First)
				for(Champion t : this.getSecondPlayer().getTeam()){
					if(t.getCurrentHP() < t.getMaxHP()*0.3) 
						targets.add(t);
				}
			else
				for(Champion t : this.getFirstPlayer().getTeam()){
					if(t.getCurrentHP() < t.getMaxHP()*0.3) 
						targets.add(t);
				}
			
		}
		else if(c instanceof AntiHero){
				for(Champion t : this.getFirstPlayer().getTeam()){
					if(this.getFirstPlayer().getLeader()!=t && t.getCondition() != Condition.KNOCKEDOUT)
						targets.add(t);
				}
				for(Champion t : this.getSecondPlayer().getTeam()){
					if(this.getSecondPlayer().getLeader()!=t && t.getCondition() != Condition.KNOCKEDOUT)
						targets.add(t);
				}
		}
		c.useLeaderAbility(targets);
		
		for(Damageable target : targets)
			removeIf0(target);
		
		if(First){
			firstLeaderAbilityUsed = true;
		}
		else{
			secondLeaderAbilityUsed = true;
		}
		
	}
	
	public void endTurn(){
		this.turnOrder.remove();
		if (this.turnOrder.isEmpty())
			prepareChampionTurns();
		
		Champion c1  = (Champion)this.turnOrder.peekMin();
        while(c1.getCondition()==Condition.INACTIVE){
        	for(int i = 0; i < c1.getAppliedEffects().size(); i++)
				if (c1.getAppliedEffects().get(i).getDuration() == 1){
					c1.getAppliedEffects().get(i).remove(c1);
					i++;
				}
				else
					c1.getAppliedEffects().get(i).setDuration(c1.getAppliedEffects().get(i).getDuration() - 1);
		
			for(Ability a : c1.getAbilities())
				if (a.getCurrentCooldown() != 0)
					a.setCurrentCooldown(a.getCurrentCooldown() - 1);
			c1.setCurrentActionPoints(c1.getMaxActionPointsPerTurn());
            this.getTurnOrder().remove();
            
            if (this.turnOrder.isEmpty())
    			prepareChampionTurns();
            
            c1 = (Champion)this.turnOrder.peekMin();
        }

        
		for(int i = 0; i < this.getCurrentChampion().getAppliedEffects().size(); i++)
			if (this.getCurrentChampion().getAppliedEffects().get(i).getDuration() == 1){
				this.getCurrentChampion().getAppliedEffects().get(i).remove(this.getCurrentChampion());
				i++;
			}
			else
				this.getCurrentChampion().getAppliedEffects().get(i).setDuration(this.getCurrentChampion().getAppliedEffects().get(i).getDuration() - 1);
	
		for(Ability a : this.getCurrentChampion().getAbilities()){
			if (a != null && a.getCurrentCooldown() != 0)
				a.setCurrentCooldown(a.getCurrentCooldown() - 1);
		}
		this.getCurrentChampion().setCurrentActionPoints(c1.getMaxActionPointsPerTurn());
			
	}
	
	public void prepareChampionTurns(){
		for(Champion c : this.getFirstPlayer().getTeam())
			this.getTurnOrder().insert(c);
		for(Champion c : this.getSecondPlayer().getTeam())
			this.getTurnOrder().insert(c);
	}
	
	public ArrayList<Damageable> getTargets(AreaOfEffect area, int castRange, boolean enemies, boolean covers){
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		Champion c = this.getCurrentChampion();
		int x = c.getLocation().x;
		int y = c.getLocation().y;
		
		boolean first = false, second = false;
		
		for(Champion t : this.getFirstPlayer().getTeam())
			if (t == c) 
				first = true;
		for(Champion t : this.getSecondPlayer().getTeam())
			if (t == c) 
				second = true;
		
		if (area == AreaOfEffect.SURROUND){
			boolean down = false, up = false, right = false, left = false;
			if (x + 1 < this.BOARDHEIGHT) up = true;
			if (x - 1 >= 0) down = true;
			if (y + 1 < this.BOARDWIDTH) right = true;
			if (y - 1 >= 0) left = true;
			
			
			if (up && board[x + 1][y] instanceof Damageable)
				if (covers && board[x + 1][y] instanceof Cover)
					targets.add((Damageable)board[x + 1][y]);
				else if (!enemies && board[x + 1][y] instanceof Champion && sameTeam(c, (Champion)board[x + 1][y]))
					targets.add((Damageable)board[x + 1][y]);
				else if (enemies && board[x + 1][y] instanceof Champion && !sameTeam(c, (Champion)board[x + 1][y]))
					targets.add((Damageable)board[x + 1][y]);
				
			if (down && board[x - 1][y] instanceof Damageable)
				if (covers && board[x - 1][y] instanceof Cover)
					targets.add((Damageable)board[x - 1][y]);
				else if (!enemies && board[x - 1][y] instanceof Champion && sameTeam(c, (Champion)board[x - 1][y]))
					targets.add((Damageable)board[x - 1][y]);
				else if (enemies && board[x - 1][y] instanceof Champion && !sameTeam(c, (Champion)board[x - 1][y]))
					targets.add((Damageable)board[x - 1][y]);
			
			if (right && board[x][y + 1] instanceof Damageable)
				if (covers && board[x][y + 1] instanceof Cover)
					targets.add((Damageable)board[x][y + 1]);
				else if (!enemies && board[x][y + 1] instanceof Champion && sameTeam(c, (Champion)board[x][y + 1]))
					targets.add((Damageable)board[x][y + 1]);
				else if (enemies && board[x][y + 1] instanceof Champion && !sameTeam(c, (Champion)board[x][y + 1]))
					targets.add((Damageable)board[x][y + 1]);
			
			if (left && board[x][y - 1] instanceof Damageable)
				if (covers && board[x][y - 1] instanceof Cover)
					targets.add((Damageable)board[x][y - 1]);
				else if (!enemies && board[x][y - 1] instanceof Champion && sameTeam(c, (Champion)board[x][y - 1]))
					targets.add((Damageable)board[x][y - 1]);
				else if (enemies && board[x][y - 1] instanceof Champion && !sameTeam(c, (Champion)board[x][y - 1]))
					targets.add((Damageable)board[x][y - 1]);
			
			if (down && right && board[x - 1][y + 1] instanceof Damageable)
				if (covers && board[x - 1][y + 1] instanceof Cover)
					targets.add((Damageable)board[x - 1][y + 1]);
				else if (!enemies && board[x - 1][y + 1] instanceof Champion && sameTeam(c, (Champion)board[x - 1][y + 1]))
					targets.add((Damageable)board[x - 1][y + 1]);
				else if (enemies && board[x - 1][y + 1] instanceof Champion && !sameTeam(c, (Champion)board[x - 1][y + 1]))
					targets.add((Damageable)board[x - 1][y + 1]);
			
			if (down && left && board[x - 1][y - 1] instanceof Damageable)
				if (covers && board[x - 1][y - 1] instanceof Cover)
					targets.add((Damageable)board[x - 1][y - 1]);
				else if (!enemies && board[x - 1][y - 1] instanceof Champion && sameTeam(c, (Champion)board[x - 1][y - 1]))
					targets.add((Damageable)board[x - 1][y - 1]);
				else if (enemies && board[x - 1][y - 1] instanceof Champion && !sameTeam(c, (Champion)board[x - 1][y - 1]))
					targets.add((Damageable)board[x - 1][y - 1]);
			
			if (up && right && board[x + 1][y + 1] instanceof Damageable)
				if (covers && board[x + 1][y + 1] instanceof Cover)
					targets.add((Damageable)board[x + 1][y + 1]);
				else if (!enemies && board[x + 1][y + 1] instanceof Champion && sameTeam(c, (Champion)board[x + 1][y + 1]))
					targets.add((Damageable)board[x + 1][y + 1]);
				else if (enemies && board[x + 1][y + 1] instanceof Champion && !sameTeam(c, (Champion)board[x + 1][y + 1]))
					targets.add((Damageable)board[x + 1][y + 1]);
				
			if (up && left && board[x + 1][y - 1] instanceof Damageable)
				if (covers && board[x + 1][y - 1] instanceof Cover)
					targets.add((Damageable)board[x + 1][y - 1]);
				else if (!enemies && board[x + 1][y - 1] instanceof Champion && sameTeam(c, (Champion)board[x + 1][y - 1]))
					targets.add((Damageable)board[x + 1][y - 1]);
				else if (enemies && board[x + 1][y - 1] instanceof Champion && !sameTeam(c, (Champion)board[x + 1][y - 1]))
					targets.add((Damageable)board[x + 1][y - 1]);
		
		}
		else if (area == AreaOfEffect.TEAMTARGET){
			if (!enemies){
				if (first)
					for(Champion t : this.getFirstPlayer().getTeam())
						if (t.getCondition() != Condition.KNOCKEDOUT && ManhattanDistance(t.getLocation(), c.getLocation()) <= castRange)
							targets.add(t);
				if (second)
					for(Champion t : this.getSecondPlayer().getTeam())
						if (t.getCondition() != Condition.KNOCKEDOUT && ManhattanDistance(t.getLocation(), c.getLocation()) <= castRange)
							targets.add(t);
			}
			else{
				if (second)
					for(Champion t : this.getFirstPlayer().getTeam())
						if (t.getCondition() != Condition.KNOCKEDOUT && ManhattanDistance(t.getLocation(), c.getLocation()) <= castRange)
							targets.add(t);
				if (first)
					for(Champion t : this.getSecondPlayer().getTeam())
						if (t.getCondition() != Condition.KNOCKEDOUT && ManhattanDistance(t.getLocation(), c.getLocation()) <= castRange)
							targets.add(t);
			}
		}
		else if (area == AreaOfEffect.SELFTARGET)
			targets.add(c);
		return targets;
	}
	
	public static int ManhattanDistance(Point p1, Point p2){
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
	
	
	public void removeIf0(Damageable c){
		if (c.getCurrentHP() == 0){
			this.getBoard()[c.getLocation().x][c.getLocation().y] = null;
			if (c instanceof Champion){
				((Champion) c).setLocation(null);
				PriorityQueue temp = new PriorityQueue(6);
				int size = this.getTurnOrder().size();
				for(int i = 0; i < size; i++){
					if (this.getTurnOrder().peekMin() == c)
						this.getTurnOrder().remove();
					else 
						temp.insert(this.getTurnOrder().remove());
				}
				size = temp.size();
				for(int i = 0 ; i < size; i++){
					this.getTurnOrder().insert(temp.remove());
				}
				
				for (int i = 0; i < this.getFirstPlayer().getTeam().size(); i++)
					if (c == getFirstPlayer().getTeam().get(i))
						this.getFirstPlayer().getTeam().remove(i);
				
				for (int i = 0; i < this.getSecondPlayer().getTeam().size(); i++)
					if (c == getSecondPlayer().getTeam().get(i))
						this.getSecondPlayer().getTeam().remove(i);
				
			}
		}
	}
	
	public static int getFinalAttackDamage(Champion c, Champion t){
		if ((c instanceof Hero && t instanceof Hero) || (c instanceof Villain && t instanceof Villain) || (c instanceof AntiHero && t instanceof AntiHero))
			return c.getAttackDamage();
		else
			return (int)(c.getAttackDamage() * 1.5);
	}
	
	public ArrayList<Damageable> getDirectionalTargets(Direction d, Point p, int range){
		
		ArrayList<Damageable> targets = new ArrayList<Damageable>();
		
		if (d == Direction.DOWN){
			for (int i = p.x - 1; i >= 0 && p.x - i <= range; i--){
				if (this.board[i][p.y] instanceof Damageable){
					targets.add((Damageable)this.board[i][p.y]);
				}
			}
		}
		else if (d == Direction.UP){
			for (int i = p.x + 1; i < this.getBoardheight() && i - p.x <= range; i++){
				if (this.board[i][p.y] instanceof Damageable)
					targets.add((Damageable)this.board[i][p.y]);
			}
		}
		else if (d == Direction.RIGHT){
			for (int i = p.y + 1; i < this.getBoardwidth() && i - p.y <= range; i++){
				if (this.board[p.x][i] instanceof Damageable)
					targets.add((Damageable)this.board[p.x][i]);
			}
		}
		else if (d == Direction.LEFT){
			for (int i = p.y - 1; i >= 0 && p.y - i <= range; i--){
				if (this.board[p.x][i] instanceof Damageable)
					targets.add((Damageable)this.board[p.x][i]);
			}
		}
		
		return targets;
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}
}
