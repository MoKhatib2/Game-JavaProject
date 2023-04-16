package model.world;
import model.effects.*;

import java.util.ArrayList;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}
	
	public int compareTo(Object c) {
		return super.compareTo((Champion)c);
	}

	
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
		for(int i = 0; i<targets.size();i++){
			Stun s = new Stun(2);
			targets.get(i).getAppliedEffects().add((Effect)s.clone());
			s.apply(targets.get(i));
			
		}
	}		
	
}
