package model.effects;

import model.world.Champion;

public class Shield extends Effect {

	public Shield( int duration) {
		super("Shield", duration, EffectType.BUFF);
	}
	
	public void apply(Champion c){
		c.setSpeed((int)(1.02*c.getSpeed()));
	}
	
	public void remove(Champion c){
		c.getAppliedEffects().remove(this);
		c.setSpeed((int)(c.getSpeed()/1.02));
	}

}
