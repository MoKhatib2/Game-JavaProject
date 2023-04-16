package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;



public class Hero extends Champion {

	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	
	public int compareTo(Object c) {
		return super.compareTo((Champion)c);
	}


	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
		
		for(Champion t : targets){
			
			int size = t.getAppliedEffects().size();
			for(int i = size - 1; i >= 0; i--){
			  if(t.getAppliedEffects().get(i).getType().equals(EffectType.DEBUFF)){
				  t.getAppliedEffects().get(i).remove(t);
				  //i++;
			  }
			}
			
			Embrace embrace =  new Embrace(2);
			t.getAppliedEffects().add((Effect) embrace.clone());
			embrace.apply(t);
		}
	}
}
