package model.world;
import java.awt.Point;
import java.io.Serializable;

public interface Damageable extends Serializable{

	public Point getLocation();
	public int getCurrentHP();
	public void setCurrentHP(int hp);
	
}
