package views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

import network.Client;
import network.GameServer;
import model.abilities.*;
import model.world.*;
import engine.*;

public class OnlineChampionPanel extends JPanel{
	Image Background;
	Game g;
	int playerTurn;
	
	public OnlineChampionPanel(Client client){
		super();
		
		final String player1 = client.getGame().getFirstPlayer().getName();
		final String player2 = client.getGame().getSecondPlayer().getName();
		
		final Player p1 = client.getGame().getFirstPlayer();
		final Player p2 = client.getGame().getSecondPlayer();
		
		
		g = client.getGame();
		
		//for(Ability a : g.getAvailableAbilities())
		//	g.getAvailableAbilities().add(a);
		
		//for(Champion c : client.getGame().getAvailableChampions())
		//	g.getAvailableChampions().add(c);
		
		
		playerTurn = 1;
		
		JPanel selectionPanel = new JPanel();
		JPanel p1Panel = new JPanel();
		JPanel p2Panel = new JPanel();
		
		this.setLayout(new GridBagLayout());
	
		
		selectionPanel.setLayout(new GridBagLayout());
		p1Panel.setLayout(new GridBagLayout());
		p2Panel.setLayout(new GridBagLayout());
		//this.setBackground(Color.pink);
		
		Font dFont = new Font("Dialog", Font.PLAIN, 24);
		
		g = client.getGame();
		
		final JList p1List = new JList();
		final JList p2List = new JList();
		final JList p1abList = new JList();
		final JList p2abList = new JList();
		final JScrollPane s1 = new JScrollPane(p1List);
		final JScrollPane s2 = new JScrollPane(p2List);
		final JScrollPane s1ab = new JScrollPane(p1abList);
		final JScrollPane s2ab = new JScrollPane(p2abList);
		final JTextArea p1text = new JTextArea("Type: "
				+ "\nHealth: "
				+ "\nMana: "
				+ "\nAttack Damage: "
				+ "\nAttack Range: "
				+ "\nAction Points: "
				+ "\nSpeed: ");
		final JTextArea p2text = new JTextArea("Type: "
				+ "\nHealth: "
				+ "\nMana: "
				+ "\nAttack Damage: "
				+ "\nAttack Range: "
				+ "\nAction Points: "
				+ "\nSpeed: ");
		final JTextArea p1abtext = new JTextArea("Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
		final JTextArea p2abtext = new JTextArea("Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
		final JButton leader1 = new JButton("<html><center>Set As Leader<br>Current Leader: None</center></html>");
		final JButton leader2 = new JButton("<html><center>Set As Leader<br>Current Leader: None</center></html>");
		
		
		final DefaultListModel allChampionsModel = new DefaultListModel();
		for(Champion c : g.getAvailableChampions())
			allChampionsModel.addElement(c);
		final JList allChampions = new JList(allChampionsModel);
		final JList abilities = new JList();
		final JButton add = new JButton("Add to " + player1 + "'s Team");
		final JTextArea att = new JTextArea("Type: "
				+ "\nHealth: "
				+ "\nMana: "
				+ "\nAttack Damage: "
				+ "\nAttack Range: "
				+ "\nAction Points: "
				+ "\nSpeed: ");
		final JTextArea abilityAtt = new JTextArea("Type: "
				+ "\nMana Cost: " 
				+ "\nCooldown: "
				+ "\nCast Range: "
				+ "\nCast Area: "
				+ "\nAction Points: ");
		
		abilities.setFont(dFont);
		abilities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		abilities.setLayoutOrientation(JList.VERTICAL);
		
		
		JScrollPane aScroller = new JScrollPane(abilities);
		aScroller.setHorizontalScrollBar(null);
		aScroller.setPreferredSize(new Dimension(300,115));
		aScroller.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		allChampions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		allChampions.setFont(dFont);
		allChampions.setLayoutOrientation(JList.VERTICAL);
		JScrollPane cScroller = new JScrollPane(allChampions);
		//cScroller.setMinimumSize(new Dimension(200, 400));
		
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10,10,10,10);
		LabeledPanel avChampPanel = new LabeledPanel("Available Champions", cScroller);
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.fill = GridBagConstraints.VERTICAL;
		//c.weighty = 1;
		//c.weightx = 1;
		c.anchor = GridBagConstraints.CENTER;
		selectionPanel.add(avChampPanel, c);
		
		final JLabel img = new JLabel();
		img.setPreferredSize(new Dimension(250,250));
		img.setMaximumSize(new Dimension(250,250));
		img.setHorizontalAlignment(JLabel.CENTER);
		img.setBorder(BorderFactory.createLineBorder(Color.black, 4));
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridy = 2;
		selectionPanel.add(img, c);
		
		att.setFont(dFont);
		att.setEditable(false);
		att.setHighlighter(null);
		att.setBackground(this.getBackground());
		att.setPreferredSize(new Dimension(300,240));
		att.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		LabeledPanel attPanel = new LabeledPanel("Attributes", att);
		Border black = BorderFactory.createLineBorder(Color.BLACK,3);
		attPanel.setBorder(black);
		//attPanel.setBackground(new Color(255,255,255));
		//c.weighty = 0.5;
		//c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		//c.fill = GridBagConstraints.BOTH;
		selectionPanel.add(attPanel, c);
		
		//c.fill = GridBagConstraints.NONE;
		c.gridy = 1;
		c.weighty = 0;
		LabeledPanel avAbPanel = new LabeledPanel("Available Abilities", aScroller);
		avAbPanel.setBorder(black);
		//avAbPanel.setMaximumSize(new Dimension(300, 150));
		selectionPanel.add(avAbPanel, c);
		//this.setBackground(new Color(0,0,0));
		c.weighty = 1;
		allChampions.addListSelectionListener(new ListSelectionListener(){
		
			
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && allChampions.getSelectedIndex() != -1){
					Champion c = (Champion)allChampions.getSelectedValue();
					//System.out.println(allChampions.getSelectedValue());
					abilities.setListData(c.getAbilities().toArray());
					att.setText("Type: " + c.getClass().getSimpleName() + 
							"\nHealth: " + c.getMaxHP()
							+ "\nMana: " + c.getMana()
							+ "\nAttack Damage: " + c.getAttackDamage()
							+ "\nAttack Range: " + c.getAttackRange()
							+ "\nAction Points: " + c.getMaxActionPointsPerTurn()
							+ "\nSpeed: " + c.getSpeed());
					abilityAtt.setText("Type: "
							+ "\nMana Cost: " 
							+ "\nCooldown: "
							+ "\nCast Range: "
							+ "\nCast Area: "
							+ "\nAction Points: ");
					//img.removeAll();
					img.setIcon(new ImageIcon("assets/" + c.getName().toLowerCase() + ".png"));
				}
				
			}
			
		});
		
		 
		abilityAtt.setFont(dFont);
		abilityAtt.setHighlighter(null);
		abilityAtt.setEditable(false);
		abilityAtt.setPreferredSize(new Dimension(300,265));
		abilityAtt.setBackground(this.getBackground());
		abilityAtt.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		c.gridy = 2;
		LabeledPanel abAtPanel = new LabeledPanel("Ability Attributes", abilityAtt);
		abAtPanel.setBorder(black);
		selectionPanel.add(abAtPanel, c);
		
		abilities.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				Ability a = (Ability)abilities.getSelectedValue();
				if (a instanceof DamagingAbility){
					abilityAtt.setText("Type: Damaging"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nDamage Amount: " + ((DamagingAbility)a).getDamageAmount());
				}
				else if (a instanceof HealingAbility){
					abilityAtt.setText("Type: Healing"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nHeal Amount: " + ((HealingAbility)a).getHealAmount());
				}
				else if (a instanceof CrowdControlAbility){
					abilityAtt.setText("Type: Crowd Control"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nEffect: " + ((CrowdControlAbility)a).getEffect().getName()
							+ "\nDuration: " + ((CrowdControlAbility)a).getEffect().getDuration());
				}
				
			}
			
		});
		
		add.setPreferredSize(new Dimension(200, 70));
		add.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				
				if (add.getText().equals("Start Game")){
					if (g.getFirstPlayer().getLeader() == null || g.getSecondPlayer().getLeader() == null){
						MessageBox.showError("Both players should set their leaders");
						return;
					}
					OnlineChampionPanel cp = (OnlineChampionPanel)((JButton)e.getSource()).getParent().getParent().getParent();
					JFrame f = (JFrame)((JButton)e.getSource()).getParent().getParent().getParent().getParent().getParent().getParent().getParent();
					f.remove(cp);
					f.add(new GamePanel(new Game(p1, p2)));
					f.revalidate();
					f.repaint();
					
					return;
				}
				
				if (allChampions.getSelectedValue() == null){
					MessageBox.showError("Please pick a champion to add");
					return;
				}
				
				
				
				if (playerTurn == 1){
					
					g.getFirstPlayer().getTeam().add((Champion)allChampions.getSelectedValue());
					System.out.println("player 1 team size: " + g.getFirstPlayer().getTeam().size());
					g.getAvailableChampions().remove((Champion)allChampions.getSelectedValue());
					allChampions.setListData(g.getAvailableChampions().toArray());
					
					p1List.setListData(g.getFirstPlayer().getTeam().toArray());
					
					add.setText("Add to " + player2 + "'s Team");
					playerTurn = 2;
				}
				
				else if (playerTurn == 2){
					g.getSecondPlayer().getTeam().add((Champion)allChampions.getSelectedValue());
					System.out.println("player 2 team size: " + g.getSecondPlayer().getTeam().size());
					g.getAvailableChampions().remove((Champion)allChampions.getSelectedValue());
					allChampions.setListData(g.getAvailableChampions().toArray());
					
					p2List.setListData(g.getSecondPlayer().getTeam().toArray());
					
					add.setText("Add to " + player1 + "'s Team");
					playerTurn = 1;
				}
				
				att.setText("Type: "
						+ "\nHealth: "
						+ "\nMana: "
						+ "\nAttack Damage: "
						+ "\nAttack Range: "
						+ "\nAction Points: "
						+ "\nSpeed: ");
				abilityAtt.setText("Type: "
						+ "\nMana Cost: " 
						+ "\nCooldown: "
						+ "\nCast Range: "
						+ "\nCast Area: "
						+ "\nAction Points: ");
				abilities.setListData(new Object[0]);
				int turns = 0;
				turns += g.getFirstPlayer().getTeam().size();
				turns += g.getSecondPlayer().getTeam().size();
				
				if (turns == 6){
					add.setText("Start Game");
					return;
				}
				img.setIcon(null);
			}
			
		});
		
		
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.NONE;
		c.ipadx = 0;
		c.ipady = 0;
		c.weighty = 0;
		c.insets = new Insets(0,0,20,0);
		c.gridx = 0;
		c.gridwidth = 2;
		butPanel.add(add, c);
		
		c.gridy = 4;
		selectionPanel.add(butPanel, c);
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
		con.insets = new Insets(0,0,0,0);
		con.gridx = 1;
		con.ipady = 0;
		con.weightx = 1;
		con.weighty = 1;
		this.add(selectionPanel, con);
		
		
		
		GridBagConstraints co = new GridBagConstraints();
		co.insets = new Insets(0,0,0,0);
		co.weighty = 1;
		co.gridy = 0;
		c.ipadx = 0;
		c.ipady = 0;
		p1List.setFont(dFont);
		p2List.setFont(dFont);
		s1.setPreferredSize(new Dimension(300,105));
		s2.setPreferredSize(new Dimension(300,105));
		p1Panel.add(new LabeledPanel(player1 + "'s Team", s1), co);
		p2Panel.add(new LabeledPanel(player2 + "'s Team", s2), co);
		
		co.gridy = 1;
		p1text.setPreferredSize(new Dimension(300,225));
		p2text.setPreferredSize(new Dimension(300,225));
		p1text.setFont(dFont);
		p2text.setFont(dFont);
		p1text.setHighlighter(null);
		p1text.setEditable(false);
		p2text.setHighlighter(null);
		p2text.setEditable(false);
		p1Panel.add(new LabeledPanel("Champion Attributes", p1text), co);
		p2Panel.add(new LabeledPanel("Champion Attributes", p2text), co);
		
		co.gridy = 2;
		leader1.setPreferredSize(new Dimension(250,50));
		leader2.setPreferredSize(new Dimension(250,50));
		leader1.setHorizontalAlignment(SwingConstants.CENTER);
		leader2.setHorizontalAlignment(SwingConstants.CENTER);
		p1Panel.add(leader1, co);
		p2Panel.add(leader2, co);
		
		co.gridy = 3;
		//co.fill = GridBagConstraints.NONE;
		p1abList.setFont(dFont);
		p2abList.setFont(dFont);
		s1ab.setPreferredSize(new Dimension(300,105));
		s2ab.setPreferredSize(new Dimension(300,105));
		p1Panel.add(new LabeledPanel("Champion Abilities", s1ab), co);
		p2Panel.add(new LabeledPanel("Champion Abilities", s2ab), co);
		
		co.gridy = 4;
		p1abtext.setPreferredSize(new Dimension(300,260));
		p2abtext.setPreferredSize(new Dimension(300,260));
		p1abtext.setFont(dFont);
		p2abtext.setFont(dFont);
		p1abtext.setHighlighter(null);
		p1abtext.setEditable(false);
		p2abtext.setHighlighter(null);
		p2abtext.setEditable(false);
		p1Panel.add(new LabeledPanel("Ability Attributes", p1abtext), co);
		p2Panel.add(new LabeledPanel("Ability Attributes", p2abtext), co);
		
		leader1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if ((Champion)p1List.getSelectedValue() != null){
					g.getFirstPlayer().setLeader((Champion)p1List.getSelectedValue());
					leader1.setText("<html><center>Set As Leader<br>Current Leader: " + (Champion)p1List.getSelectedValue() + "</center></html>");
			
				}
			}
			
		});
		leader2.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if ((Champion)p2List.getSelectedValue() != null){
					g.getSecondPlayer().setLeader((Champion)p2List.getSelectedValue());
					leader2.setText("<html><center>Set As Leader<br>Current Leader: " + (Champion)p2List.getSelectedValue() + "</center></html>");
				}
			}
			
		});
		p1List.addListSelectionListener(new ListSelectionListener(){
		
			
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && p1List.getSelectedIndex() != -1){
					Champion c = (Champion)p1List.getSelectedValue();
					//System.out.println(allChampions.getSelectedValue());
					p1abList.setListData(c.getAbilities().toArray());
					p1text.setText("Type: " + c.getClass().getSimpleName() + 
							"\nHealth: " + c.getMaxHP()
							+ "\nMana: " + c.getMana()
							+ "\nAttack Damage: " + c.getAttackDamage()
							+ "\nAttack Range: " + c.getAttackRange()
							+ "\nAction Points: " + c.getMaxActionPointsPerTurn()
							+ "\nSpeed: " + c.getSpeed());
					p1abtext.setText("Type: "
							+ "\nMana Cost: " 
							+ "\nCooldown: "
							+ "\nCast Range: "
							+ "\nCast Area: "
							+ "\nAction Points: ");
				}
				
			}
			
		});
		p2List.addListSelectionListener(new ListSelectionListener(){
		
			
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && p2List.getSelectedIndex() != -1){
					Champion c = (Champion)p2List.getSelectedValue();
					//System.out.println(allChampions.getSelectedValue());
					p2abList.setListData(c.getAbilities().toArray());
					p2text.setText("Type: " + c.getClass().getSimpleName() + 
							"\nHealth: " + c.getMaxHP()
							+ "\nMana: " + c.getMana()
							+ "\nAttack Damage: " + c.getAttackDamage()
							+ "\nAttack Range: " + c.getAttackRange()
							+ "\nAction Points: " + c.getMaxActionPointsPerTurn()
							+ "\nSpeed: " + c.getSpeed());
					p2abtext.setText("Type: "
							+ "\nMana Cost: " 
							+ "\nCooldown: "
							+ "\nCast Range: "
							+ "\nCast Area: "
							+ "\nAction Points: ");
				}
				
			}
			
		});
		p1abList.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				Ability a = (Ability)p1abList.getSelectedValue();
				if (a instanceof DamagingAbility){
					p1abtext.setText("Type: Damaging"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nDamage Amount: " + ((DamagingAbility)a).getDamageAmount());
				}
				else if (a instanceof HealingAbility){
					p1abtext.setText("Type: Healing"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nHeal Amount: " + ((HealingAbility)a).getHealAmount());
				}
				else if (a instanceof CrowdControlAbility){
					p1abtext.setText("Type: Crowd Control"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nEffect: " + ((CrowdControlAbility)a).getEffect().getName()
							+ "\nDuration: " + ((CrowdControlAbility)a).getEffect().getDuration());
				}
				
			}
			
		});
		p2abList.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				Ability a = (Ability)p2abList.getSelectedValue();
				if (a instanceof DamagingAbility){
					p2abtext.setText("Type: Damaging"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nDamage Amount: " + ((DamagingAbility)a).getDamageAmount());
				}
				else if (a instanceof HealingAbility){
					p2abtext.setText("Type: Healing"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nHeal Amount: " + ((HealingAbility)a).getHealAmount());
				}
				else if (a instanceof CrowdControlAbility){
					p2abtext.setText("Type: Crowd Control"
							+ "\nMana Cost: " + a.getManaCost() 
							+ "\nCooldown: " + a.getBaseCooldown()
							+ "\nCast Range: " + a.getCastRange()
							+ "\nCast Area: " + a.getCastArea().name()
							+ "\nAction Points: " + a.getRequiredActionPoints()
							+ "\nEffect: " + ((CrowdControlAbility)a).getEffect().getName()
							+ "\nDuration: " + ((CrowdControlAbility)a).getEffect().getDuration());
				}
				
			}
			
		});
		
		con.gridx = 0;
		
		this.add(p1Panel, con);
		con.gridx = 2;
		
		this.add(p2Panel, con);
		
	}
	

	protected void paintComponent(Graphics g) {

	    super.paintComponent(g);
	    	g.drawImage(Background, 0, 0, getWidth(), getHeight(), null);
	}
	
	
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		
		//new Thread(new Runnable() {
		//     public void run() {
		//          new GameServer().main(null);
		//     }
		//}).start();
		//try {
		//	Thread.sleep(1000);
		//} catch (InterruptedException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
		/*final Client client = null;
		new Thread(new Runnable() {
		     public void run() {
		          try {
					client = new Client("omar", "");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		}).start();*/
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Client client = new Client("omar", "");
		f.add(new OnlineChampionPanel(client));
		f.setSize(1600, 1000);
		f.setLocationRelativeTo(null);
		//f.setMinimumSize(new Dimension(1000, 600));
		//f.pack();
		//f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setVisible(true);
		
	}
}
