package model.effects;
import java.io.Serializable;

import model.world.*;


public abstract class Effect implements Cloneable, Serializable{
	private String name;
	private EffectType type;
	private int duration;

	public Effect(String name, int duration, EffectType type) {
		this.name = name;
		this.type = type;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public EffectType getType() {
		return type;
	}
	public Object clone() throws CloneNotSupportedException{
		return (Effect) super.clone();
	}
	
	public abstract void apply(Champion c) throws CloneNotSupportedException;
	public abstract void remove(Champion c);

	

}
