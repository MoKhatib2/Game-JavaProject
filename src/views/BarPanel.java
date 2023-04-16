package views;

import javax.swing.*;
import java.awt.*;

public class BarPanel extends JProgressBar{
	private Color c;
	
	public BarPanel (Color color, int cur, int max){
		
		super();
		this.setForeground(color);
		this.setValue(cur * 100 / max);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		
	}
	public void update(int cur, int max){
		this.setValue(cur * 100 / max);
	}
	public static void main(String[] args){
		JFrame f = new JFrame();
		f.add(new BarPanel(Color.BLUE, 1, 2));
		f.setSize(500,400);
		f.setVisible(true);
	}
}
