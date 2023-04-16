package views;

import engine.*;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.world.*;
import model.abilities.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class TabPanel extends JPanel{
	Game g;
	
	public TabPanel(Game game){
		super();
		g = game;
		this.setLayout(new GridBagLayout());
		//this.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		//c.weightx = 1;
		//c.weighty = 1;
		c.insets = new Insets(0,20,0,20);
		JPanel top = new JPanel(new GridBagLayout());
		JPanel bot = new JPanel(new GridBagLayout());
		for(int i = 0; i < g.getFirstPlayer().getTeam().size(); i++){
			c.gridx = i;
			
			GridBagConstraints cons = new GridBagConstraints();
			
			JPanel p = new  JPanel();
			p.setLayout(new GridBagLayout());
			cons.gridx = 0;
			cons.gridy = 0;
			cons.weightx = 1;
			cons.weighty = 1;
			ImageIcon img = new ImageIcon("assets/" + g.getFirstPlayer().getTeam().get(i).getName().toLowerCase() + ".png");
			//ImageIcon img = new ImageIcon("assets/ironman.png");
			JLabel pic = new JLabel(img);
			p.add(pic, cons);
			JTextArea att = new JTextArea("Type: " + g.getFirstPlayer().getTeam().get(i).getClass().getSimpleName() + 
					"\nCurrent Health: " + g.getFirstPlayer().getTeam().get(i).getCurrentHP()
					+ "\nMax Health: " + g.getFirstPlayer().getTeam().get(i).getMaxHP()
					+ "\nMana: " + g.getFirstPlayer().getTeam().get(i).getMana()
					+ "\nAttack Damage: " + g.getFirstPlayer().getTeam().get(i).getAttackDamage()
					+ "\nAttack Range: " + g.getFirstPlayer().getTeam().get(i).getAttackRange()
					+ "\nMax Action Points: " + g.getFirstPlayer().getTeam().get(i).getMaxActionPointsPerTurn()
					+ "\nSpeed: " + g.getFirstPlayer().getTeam().get(i).getSpeed());
			att.setFont(new Font("Dialog", Font.PLAIN, 20));
			att.setBackground(this.getBackground());
			att.setPreferredSize(new Dimension(200,300));
			att.setAlignmentX(CENTER_ALIGNMENT);
			att.setHighlighter(null);
			att.setEditable(false);
			att.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
			p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3,3,3,3), BorderFactory.createLineBorder(Color.black,  4)));
			p.setAlignmentX(CENTER_ALIGNMENT);
			//p.setPreferredSize(new Dimension(300, 380));
			cons.gridy = 1;
			p.add(att, cons);
			LabeledPanel l;
			if (g.getFirstPlayer().getTeam().get(i) == g.getFirstPlayer().getLeader())
				l = new LabeledPanel(g.getFirstPlayer().getTeam().get(i).getName() + " (Leader)", p);
			else 
				l = new LabeledPanel(g.getFirstPlayer().getTeam().get(i).getName(), p);
			
			if (g.getFirstPlayer().getTeam().get(i) == g.getCurrentChampion()) {
				p.setBorder(BorderFactory.createLineBorder(Color.red, 4));
				l = new LabeledPanel("Current Champion", l, Color.red);
			}
			else
				p.setBorder(BorderFactory.createLineBorder(Color.black, 4));
			top.add(l, c);
		}
		c.gridy = 1;
		
		for(int i = 0; i < g.getSecondPlayer().getTeam().size(); i++){
			c.gridx = i;
			
			GridBagConstraints cons = new GridBagConstraints();
			
			JPanel p = new  JPanel();
			p.setLayout(new GridBagLayout());
			cons.gridx = 0;
			cons.gridy = 0;
			ImageIcon img = new ImageIcon("assets/" + g.getSecondPlayer().getTeam().get(i).getName().toLowerCase() + ".png");
			//ImageIcon img = new ImageIcon("assets/ironman.png");
			JLabel pic = new JLabel(img);
			p.add(pic, cons);
			JTextArea att = new JTextArea("Type: " + g.getSecondPlayer().getTeam().get(i).getClass().getSimpleName() + 
					"\nCurrent Health: " + g.getSecondPlayer().getTeam().get(i).getCurrentHP()
					+ "\nMax Health: " + g.getSecondPlayer().getTeam().get(i).getMaxHP()
					+ "\nMana: " + g.getSecondPlayer().getTeam().get(i).getMana()
					+ "\nAttack Damage: " + g.getSecondPlayer().getTeam().get(i).getAttackDamage()
					+ "\nAttack Range: " + g.getSecondPlayer().getTeam().get(i).getAttackRange()
					+ "\nMax Action Points: " + g.getSecondPlayer().getTeam().get(i).getMaxActionPointsPerTurn()
					+ "\nSpeed: " + g.getSecondPlayer().getTeam().get(i).getSpeed());
			att.setFont(new Font("Dialog", Font.PLAIN, 20));
			att.setBackground(this.getBackground());
			att.setPreferredSize(new Dimension(200,300));
			att.setAlignmentX(CENTER_ALIGNMENT);
			att.setHighlighter(null);
			att.setEditable(false);
			att.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
			p.setAlignmentX(CENTER_ALIGNMENT);
			cons.gridy = 1;
			p.add(att, cons);
			p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3,3,3,3), BorderFactory.createLineBorder(Color.black,  4)));
			LabeledPanel l;
			if (g.getSecondPlayer().getTeam().get(i) == g.getSecondPlayer().getLeader())
				l = new LabeledPanel(g.getSecondPlayer().getTeam().get(i).getName() + " (Leader)", p);
			else 
				l = new LabeledPanel(g.getSecondPlayer().getTeam().get(i).getName(), p);
			
			if (g.getSecondPlayer().getTeam().get(i) == g.getCurrentChampion()) {
				p.setBorder(BorderFactory.createLineBorder(Color.red, 4));
				l = new LabeledPanel("Current Champion", l, Color.red);
			}
			else
				p.setBorder(BorderFactory.createLineBorder(Color.black, 4));
			bot.add(l, c);
		}
		
		c.insets = new Insets(0,0,0,0);
		c.gridx = 0;
		c.gridy = 0;
		this.add(new LabeledPanel(g.getFirstPlayer().getName() + "'s Team", top), c);
		c.gridx = 0;
		c.gridy = 1;
		this.add(new LabeledPanel(g.getSecondPlayer().getName() + "'s Team", bot), c);
		
		JTextPane turns = new JTextPane();
		//turns.setFont(new Font("Dialog", Font.PLAIN, 28));
		turns.setAlignmentX(CENTER_ALIGNMENT);
		StyledDocument doc = turns.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		turns.setHighlighter(null);
		turns.setEditable(false);
		turns.setFont(new Font ("Dialog", Font.PLAIN, 24));
		turns.setMinimumSize(new Dimension(200,205));
		turns.setBackground(this.getBackground());
		turns.setBorder(BorderFactory.createLineBorder(Color.black, 4));
		ArrayList<String> names = getTurns();
		Style style = turns.addStyle("", null);
		StyleConstants.setForeground(style, Color.black);
		for(int i = 0; i < names.size(); i++){
			System.out.println(names.get(i));
			
			try{
				
			if (g.getCurrentChampion().getName() == names.get(i)){
				StyleConstants.setForeground(style, Color.red);
				doc.insertString(doc.getLength(), names.get(i), style);
				StyleConstants.setForeground(style, Color.black);
			}
			else
				doc.insertString(doc.getLength(), names.get(i), style);
			if (i != names.size() - 1)
				doc.insertString(doc.getLength(), "\n", style);
			}catch(Exception e){
				
			}
		}
		
		LabeledPanel turnsPanel = new LabeledPanel("Turn Order", turns);
		turnsPanel.setPreferredSize(new Dimension(250,500));
		c.gridx = 1;
		c.gridy = 0;
		//c.insets = new Insets(10,10,10,10);
		//c.gridheight = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		this.add(turnsPanel, c);
		
		JTextPane leaderText = new JTextPane();
		
		doc = leaderText.getStyledDocument();
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		leaderText.setHighlighter(null);
		leaderText.setEditable(false);
		leaderText.setFont(new Font ("Dialog", Font.PLAIN, 24));
		leaderText.setMinimumSize(new Dimension(350,205));
		leaderText.setBackground(this.getBackground());
		//leaderText.setBorder(BorderFactory.createLineBorder(Color.black, 4));
		style = leaderText.addStyle("", null);
		StyleConstants.setForeground(style, Color.black);
		try {
			doc.insertString(doc.getLength(), g.getFirstPlayer().getName() + " has their leader ability:\n", style);
			
			if (g.isFirstLeaderAbilityUsed()){
				StyleConstants.setForeground(style, Color.red);
				doc.insertString(doc.getLength(), "false", style);
			}
			else{
				StyleConstants.setForeground(style, Color.green);
				doc.insertString(doc.getLength(), "true", style);
			}
			StyleConstants.setForeground(style, Color.black);
			doc.insertString(doc.getLength(), "\n" + g.getSecondPlayer().getName() + " has their leader ability: \n", style);
			
			if (g.isSecondLeaderAbilityUsed()){
				StyleConstants.setForeground(style, Color.red);
				doc.insertString(doc.getLength(), "false", style);
			}
			else{
				StyleConstants.setForeground(style, Color.green);
				doc.insertString(doc.getLength(), "true", style);
			}
		
		} catch (BadLocationException e) {
		}
		
		LabeledPanel leaderPanel = new LabeledPanel("Leader Ability Use", leaderText);
		leaderPanel.setPreferredSize(new Dimension(600,200));
		c.gridx = 1;
		c.gridy = 1;
		//c.insets = new Insets(10,10,10,10);
		c.gridheight = 1;
		c.anchor = GridBagConstraints.PAGE_START;
		this.add(leaderPanel, c);
		
	}
	
	private ArrayList<String> getTurns(){
		ArrayList<String> names = new ArrayList<String>();
		PriorityQueue temp = new PriorityQueue(6);
		
		while(!g.getTurnOrder().isEmpty()){
			Champion champ = (Champion)g.getTurnOrder().remove();
			temp.insert(champ);
		}
		
		g.prepareChampionTurns();
		
		while(!g.getTurnOrder().isEmpty()){
			Champion champ = (Champion)g.getTurnOrder().remove();
			names.add(champ.getName());
		}
		while(!temp.isEmpty())
			g.getTurnOrder().insert(temp.remove());
		
		return names;
	}
	
	
	public static void main(String[] args) throws ChampionDisarmedException{
		Player ana = new Player("omar");
		Player mo = new Player("mo");
		
		
		Game g = new Game(ana, mo);
		try {
			g.loadChampions("Champions.csv");
			g.loadAbilities("Abilities.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ana.getTeam().add(g.getAvailableChampions().get(8));
		ana.getTeam().add(g.getAvailableChampions().get(1));
		ana.getTeam().add(g.getAvailableChampions().get(2));
		mo.getTeam().add(g.getAvailableChampions().get(3));
		mo.getTeam().add(g.getAvailableChampions().get(4));
		mo.getTeam().add(g.getAvailableChampions().get(5));
		ana.setLeader(ana.getTeam().get(0));
		mo.setLeader(mo.getTeam().get(0));
		g.placeChampions();
		
		g.prepareChampionTurns();
		
		JFrame f = new JFrame();
		
		f.setBackground(Color.black);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new TabPanel(g));
		f.setSize(1000, 1000);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
