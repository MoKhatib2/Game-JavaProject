package views;

import engine.*;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import model.world.*;
import model.abilities.*;
import model.effects.Effect;
import model.effects.EffectType;
import javafx.scene.input.KeyCode;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

public class ControlsPanel extends JPanel{
	
	Game g;
	Champion c;
	GameBoard b;
	JList ab;
	JButton cast;
	JTextArea att;
	JButton leader;
	CurrentChampPanel ccp;
	
	public ControlsPanel(Game game, GameBoard board, CurrentChampPanel currentChampPanel){
		c = game.getCurrentChampion();
		g = game;
		b = board;
		ccp = currentChampPanel;
		cast = new JButton("Select An Ability");
		att = new JTextArea(
				"Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
		JPanel right = new JPanel();
		right.setLayout(new GridBagLayout());
		JPanel left = new JPanel();
		left.setLayout(new GridBagLayout());
		
		Font dFont = new Font("Dialog", Font.PLAIN, 20);
		
		ab = new JList(c.getAbilities().toArray());
		ab.setFont(dFont);
		ab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ab.setLayoutOrientation(JList.VERTICAL);
		
		ab.addListSelectionListener(new ListSelectionListener(){

			
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && ab.getSelectedValue() != null){
					Ability a = (Ability)ab.getSelectedValue();
					ControlsPanel cp = (ControlsPanel)ab.getParent().getParent().getParent().getParent().getParent();
					if (a instanceof DamagingAbility){
						att.setText("Type: Damaging"
								+ "\nMana Cost: " + a.getManaCost()
								+ "\nCurrent Cooldown: " + a.getCurrentCooldown()
								+ "\nBase Cooldown: " + a.getBaseCooldown()
								+ "\nCast Range: " + a.getCastRange()
								+ "\nCast Area: " + a.getCastArea().name()
								+ "\nAction Points: " + a.getRequiredActionPoints()
								+ "\nDamage Amount: " + ((DamagingAbility)a).getDamageAmount());
					}
					else if (a instanceof HealingAbility){
						att.setText("Type: Healing"
								+ "\nMana Cost: " + a.getManaCost() 
								+ "\nCurrent Cooldown: " + a.getCurrentCooldown()
								+ "\nBase Cooldown: " + a.getBaseCooldown()
								+ "\nCast Range: " + a.getCastRange()
								+ "\nCast Area: " + a.getCastArea().name()
								+ "\nAction Points: " + a.getRequiredActionPoints()
								+ "\nHeal Amount: " + ((HealingAbility)a).getHealAmount());
					}
					else if (a instanceof CrowdControlAbility){
						att.setText("Type: Crowd Control"
								+ "\nMana Cost: " + a.getManaCost() 
								+ "\nCurrent Cooldown: " + a.getCurrentCooldown()
								+ "\nBase Cooldown: " + a.getBaseCooldown()
								+ "\nCast Range: " + a.getCastRange()
								+ "\nCast Area: " + a.getCastArea().name()
								+ "\nAction Points: " + a.getRequiredActionPoints()
								+ "\nEffect: " + ((CrowdControlAbility)a).getEffect().getName()
								+ "\nDuration: " + ((CrowdControlAbility)a).getEffect().getDuration());
					}
					
					
					if (a.getCastArea() == AreaOfEffect.SINGLETARGET){
						cast.setText("Select Location On Board");
						cast.setEnabled(false);
					}
					else if (a.getCastArea() == AreaOfEffect.DIRECTIONAL){
						cast.setText("Select Direction On Board");
						cast.setEnabled(false);
					}
					else{
						cast.setText("Cast Ability");
						cast.setEnabled(true);
					}
					
					b.cast(a, cp);
					
				}
				
			}
			
		});
		
		JScrollPane aScroller = new JScrollPane(ab);
		aScroller.setHorizontalScrollBar(null);
		aScroller.setPreferredSize(new Dimension(250,100));
		aScroller.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		
		GridBagConstraints con = new GridBagConstraints();
		
		con.insets = new Insets(10,25,10,25);
		con.gridx = 0;
		con.gridy = 0;
		right.add(new LabeledPanel("Available Abilities", aScroller), con);
		
		att.setFont(dFont);
		att.setHighlighter(null);
		att.setEditable(false);
		att.setPreferredSize(new Dimension(280,240));
		
		con.gridy = 1;
		right.add(new LabeledPanel("Ability Attributes", att), con);
		
		cast.setFont(dFont);
		cast.setEnabled(false);
		cast.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				try {
					g.castAbility((Ability)ab.getSelectedValue());
				} catch (NotEnoughResourcesException | AbilityUseException
						| CloneNotSupportedException e) {
					MessageBox.showError(e.getMessage());
				}
				b.updateBoard();
				resetList();
				ccp.update();
			}
			
		});
		con.gridx = 0;
		con.gridy = 2;
		right.add(cast, con);
		
		
		
		JButton move = new JButton("Move");
		
		move.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				b.move();
			}
		});
		
		move.setFont(dFont);
		move.setPreferredSize(new Dimension(100, 50));
		con.gridy = 0;
		left.add(move, con);
		JButton attack = new JButton("Attack");
		
		attack.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				b.attack();
			}
		});
		
		attack.setFont(dFont);
		attack.setPreferredSize(new Dimension(100, 50));
		con.gridy = 1;
		left.add(attack, con);
		
		leader = new JButton("Use Leader Ability");
		leader.setFont(dFont);
		leader.setPreferredSize(new Dimension(200, 50));
		leader.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				try {
					g.useLeaderAbility();
				} catch (LeaderNotCurrentException
						| LeaderAbilityAlreadyUsedException
						| CloneNotSupportedException e) {
					MessageBox.showError(e.getMessage());
				}
				b.updateBoard();
				ccp.update();
			}
			
		});
		
		con.gridx = 0;
		con.gridy = 2;
		
		left.add(leader, con);
		
		JButton end = new JButton("End Turn");
		
		end.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				g.endTurn();
				ccp.update();
				b.updateBoard();
				update();
			}
			
		});
		
		end.setFont(dFont);
		end.setPreferredSize(new Dimension(150, 50));
		con.gridy = 3;
		left.add(end, con);
		
		
		con.gridx = 0;
		con.gridy = 0;
		this.add(right, con);
		con.gridx = 1;
		con.gridy = 0;
		
		this.add(left, con);
		
	}
	
	public void update(){
		resetList();
		c = g.getCurrentChampion();
		ab.setListData(c.getAbilities().toArray());
		att.setText(
				"Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
		
	}
	
	
	public void resetList(){
		cast.setText("Select An Ability");
		cast.setEnabled(false);
		ab.clearSelection();
		att.setText(
				"Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
	}
	
	private void checkWin(){
		
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
		//f.add(new ControlsPanel(g, new GameBoard(g)));
		f.setSize(600, 600);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
