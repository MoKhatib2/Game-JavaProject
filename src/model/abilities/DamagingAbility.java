package model.abilities;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.Shield;
import model.world.Champion;
import model.world.Damageable;



public class DamagingAbility extends Ability {
	
	private int damageAmount;
	public DamagingAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required,int damageAmount) {
		super(name, cost, baseCoolDown, castRadius, area,required);
		this.damageAmount=damageAmount;
	}
	public int getDamageAmount() {
		return damageAmount;
	}
	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}
	
	public void execute(ArrayList<Damageable> targets){
		for(Damageable target : targets){
			if (target instanceof Champion){
				
				Effect shield = null;
				for (Effect e : ((Champion) target).getAppliedEffects())
					if (e instanceof Shield) shield = e;
				
				if (shield != null) shield.remove((Champion)target);
				else target.setCurrentHP(target.getCurrentHP() - this.getDamageAmount());
			}
			else target.setCurrentHP(target.getCurrentHP() - this.getDamageAmount());
		}
	}

	

}
