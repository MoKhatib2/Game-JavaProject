package views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

import model.abilities.*;
import model.effects.Effect;
import model.world.*;
import engine.*;
import exceptions.ChampionDisarmedException;


public class CurrentChampPanel extends JPanel{

	Game g;
	Champion cur;
	JLabel img;
	JLabel name;
	JTextArea att;
	JTextArea eff;
	BarPanel health;
	
	public CurrentChampPanel(Game game){
		super();
		cur = game.getCurrentChampion();
		g = game;
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JPanel right = new JPanel();
		right.setLayout(new GridBagLayout());
		
		JPanel left = new JPanel();
		left.setLayout(new GridBagLayout());
		Font dfont = new Font("Dialog", Font.PLAIN, 20);
		
		name = new JLabel(cur.getName());
		setChampName();
		name.setFont(new Font("Dialog", Font.BOLD, 24));
		
		c.gridx = 0;
		c.gridy = 0;
		right.add(name, c);
		
		img = new JLabel(new ImageIcon("assets/" + cur.getName().toLowerCase() + ".png"));
		img.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 4), BorderFactory.createEmptyBorder(5,5,5,5)));
		
		c.gridy = 1;
		c.insets = new Insets(7,20,7,7);
		right.add(img, c);
		
		health = new BarPanel(Color.green, cur.getCurrentHP(), cur.getMaxHP());
		c.gridy = 2;
		right.add(health, c);
		
		att = new JTextArea(
				"Type: " + cur.getClass().getSimpleName() 
				+ "\nHealth: " + cur.getCurrentHP() + " / " + cur.getMaxHP()
				+ "\nCurrent Action Points: " + cur.getCurrentActionPoints() + " / " + cur.getMaxActionPointsPerTurn()
				+ "\nMana: " + cur.getMana()
				+ "\nAttack Damage: " + cur.getAttackDamage()
				+ "\nAttack Range: " + cur.getAttackRange()
				+ "\nSpeed: " + cur.getSpeed());
		att.setFont(dfont);
		att.setBackground(this.getBackground());
		att.setHighlighter(null);
		att.setEditable(false);
		c.gridx = 1;
		c.gridy = 1;
		//c.gridheight = 3;
		c.anchor = GridBagConstraints.PAGE_START;
		left.add(new LabeledPanel("Champion Attributes", att), c);
		
		eff = new JTextArea();
		for(int i = 0; i < cur.getAppliedEffects().size(); i++){
			eff.setText(eff.getText() + cur.getAppliedEffects().get(i).getDuration() + " " + cur.getAppliedEffects().get(i).getName());
			if (i != cur.getAppliedEffects().size() - 1)
				eff.setText(eff.getText() + "\n");	
		}
		eff.setFont(dfont);
		eff.setBackground(this.getBackground());
		eff.setHighlighter(null);
		eff.setEditable(false);
		c.gridx = 1;
		c.gridy = 2;
		//c.gridheight = 3;
		c.anchor = GridBagConstraints.PAGE_START;
		left.add(new LabeledPanel("Applied Effects", eff) , c);
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		this.add(right, c);
		c.gridx = 1;
		this.add(left, c);
		
	}
	
	public void update(){
		cur = g.getCurrentChampion();
		setChampName();
		img.setIcon(new ImageIcon("assets/" + cur.getName().toLowerCase() + ".png"));
		health.update(cur.getCurrentHP(), cur.getMaxHP());
		att.setText(
				"Type: " + cur.getClass().getSimpleName() 
				+ "\nHealth: " + cur.getCurrentHP() + " / " + cur.getMaxHP()
				+ "\nCurrent Action Points: " + cur.getCurrentActionPoints() + " / " + cur.getMaxActionPointsPerTurn()
				+ "\nMana: " + cur.getMana()
				+ "\nAttack Damage: " + cur.getAttackDamage()
				+ "\nAttack Range: " + cur.getAttackRange()
				+ "\nSpeed: " + cur.getSpeed());
		
		eff.setText("");
		for(int i = 0; i < cur.getAppliedEffects().size(); i++){
			eff.setText(eff.getText() + cur.getAppliedEffects().get(i).getDuration() + " " + cur.getAppliedEffects().get(i).getName());
			if (i != cur.getAppliedEffects().size() - 1)
				eff.setText(eff.getText() + "\n");	
		}
		
	}
	
	private void setChampName(){
		if (cur == g.getFirstPlayer().getLeader() || cur == g.getSecondPlayer().getLeader())
			name.setText(cur.getName() + " (Leader)");
		else
			name.setText(cur.getName());
	}
	
	public static void main(String[] args) throws ChampionDisarmedException{
		Player ana = new Player("omar");
		Player mo = new Player("mo");
		
		
		Game g = new Game(ana, mo);
		try {
			g.loadAbilities("Abilities.csv");
			g.loadChampions("Champions.csv");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ana.getTeam().add(g.getAvailableChampions().get(8));
		ana.getTeam().add(g.getAvailableChampions().get(1));
		ana.getTeam().add(g.getAvailableChampions().get(2));
		mo.getTeam().add(g.getAvailableChampions().get(3));
		mo.getTeam().add(g.getAvailableChampions().get(4));
		mo.getTeam().add(g.getAvailableChampions().get(5));
		g.placeChampions();
		
		g.prepareChampionTurns();
		
		final JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new CurrentChampPanel(g));
		f.setSize(600, 600);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
}
