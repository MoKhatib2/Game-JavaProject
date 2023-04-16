package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

public class LabeledPanel extends JPanel{
	JLabel label;
	JComponent comp;
	
	public LabeledPanel(String text, JComponent c){
		super();
		label = new JLabel(text);
		comp = c;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		label.setFont(new Font("Dialog", Font.BOLD, 24));
		label.setAlignmentX(CENTER_ALIGNMENT);
		this.add(label);
		this.add(comp);
	}
	public LabeledPanel(String text, JComponent c, Color color){
		super();
		label = new JLabel(text);
		comp = c;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		label.setFont(new Font("Dialog", Font.BOLD, 24));
		label.setForeground(color);
		label.setAlignmentX(CENTER_ALIGNMENT);
		this.add(label);
		this.add(comp);
	}
}
