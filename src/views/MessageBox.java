package views;
import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public abstract class MessageBox{
	
	public static void showError(String text){
		JOptionPane p = new JOptionPane();
		p.showMessageDialog(new JFrame(), text, "Error", JOptionPane.ERROR_MESSAGE);		
	}
	public static void showInfo(String text){
		JOptionPane p = new JOptionPane();
		p.showMessageDialog(new JFrame(), text, "Error", JOptionPane.PLAIN_MESSAGE);		
	}
	
	public static void main(String[] args){
		showInfo("Test");
	}
	
}
