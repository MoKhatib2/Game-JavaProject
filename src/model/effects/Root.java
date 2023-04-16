package model.effects;
import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root( int duration) {
		super("Root", duration, EffectType.DEBUFF);	
	}
	
	public void apply(Champion c){
		
		if(c.getCondition()==Condition.ACTIVE){
			c.setCondition(Condition.ROOTED);
		}
				
	}
	
	public void remove(Champion c){
		c.getAppliedEffects().remove(this);
		
		
		if (c.getCondition() == Condition.INACTIVE)
				return;
			
		
		//for(Effect e : c.getAppliedEffects()){
		//	if(e instanceof Root){
				c.setCondition(Condition.ROOTED);
		//		return;
		//	}
		//}
		//c.setCondition(Condition.ACTIVE);
	}
}
