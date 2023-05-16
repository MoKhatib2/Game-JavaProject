package views;

import engine.*;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughResourcesException;
import model.world.*;
import model.abilities.*;
import model.effects.Effect;
import model.effects.EffectType;


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

public class GamePanel extends JPanel{
	Game g;
	GameBoard b;
	JPanel p;
	CurrentChampPanel cur;
	ControlsPanel cp;
	TabPanel t;
	
	public GamePanel(Game game){
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(Color.BLACK);
		final JPanel butPanel = new JPanel();
		
		g = game;
		p = new JPanel();
		t = new TabPanel(g);
		cur = new CurrentChampPanel(g);
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridheight = 3;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(20,20,20,20);
		b = new GameBoard(g, cur);
		b.setPreferredSize(new Dimension(900,900));
		p.add(b, c);
		this.setFocusTraversalKeysEnabled(false);
		this.setFocusable(true);
		p.setFocusable(false);
		t.setFocusable(false);
		c.gridheight = 1;
		this.addKeyListener(new KeyListener(){
			
			boolean pressed = false;
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB && !pressed){
					pressed = true;
					System.out.println("pressed");
					t = new TabPanel(g);
					((GamePanel)e.getComponent()).remove(p);
					((GamePanel)e.getComponent()).add(t);
					((GamePanel)e.getComponent()).repaint();
					((GamePanel)e.getComponent()).revalidate();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB){
					pressed = false;
					System.out.println("released");
					((GamePanel)e.getComponent()).remove(t);
					((GamePanel)e.getComponent()).repaint();
					((GamePanel)e.getComponent()).add(p);
					((GamePanel)e.getComponent()).repaint();
					((GamePanel)e.getComponent()).revalidate();
				}
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
		});
		
		
		
		c.gridy = 0;
		c.gridx = 1;
		p.add(cur, c);
		
		c.gridy = 1;
		cp = new ControlsPanel(g, b, cur);
		p.add(cp, c);
		
		JLabel tab = new JLabel("Press TAB to view all champions");
		tab.setHorizontalAlignment(JLabel.CENTER);
		c.insets = new Insets(0,0,12,0);
		c.gridwidth = 2;
		c.gridy = 2;			
		p.add(tab, c);
		
		this.add(p);
		
		
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
		mo.getTeam().add(g.getAvailableChampions().get(3));
		mo.getTeam().add(g.getAvailableChampions().get(4));
		mo.getTeam().add(g.getAvailableChampions().get(5));
		ana.setLeader(g.getAvailableChampions().get(8));
		mo.setLeader(g.getAvailableChampions().get(4));
		g.placeChampions();
		
		g.prepareChampionTurns();
		
		final JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new GamePanel(g));
		f.setSize(1600, 1000);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		ana.getTeam().get(0).setCurrentHP(0);
		g.removeIf0(ana.getTeam().get(0));
	}
									
	
}
