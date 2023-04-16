package model.effects;
import model.world.*;


public class Embrace extends Effect {
	

	public Embrace(int duration) {
		super("Embrace", duration, EffectType.BUFF);
	}
	
	public void apply(Champion c){
		c.setCurrentHP((int)(0.2*c.getMaxHP())+c.getCurrentHP());
		c.setMana((int)(1.2*c.getMana()));
		c.setSpeed((int)(1.2*c.getSpeed()));
		c.setAttackDamage((int)(1.2*c.getAttackDamage()));
	}
	
	public void remove(Champion c){
		c.getAppliedEffects().remove(this);
		c.setSpeed((int)(c.getSpeed()/1.2));
		c.setAttackDamage((int)(c.getAttackDamage() / 1.2));
	}
	

}
