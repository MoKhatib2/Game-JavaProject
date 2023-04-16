package model.world;

import java.util.ArrayList;



public class Villain extends Champion {

	public Villain(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	public int compareTo(Object c) {
		return super.compareTo((Champion)c);
	}

	public void useLeaderAbility(ArrayList<Champion> targets) {
		
		for(int i = 0; i < targets.size();i++)
			targets.get(i).setCurrentHP(0);

	
	}
}