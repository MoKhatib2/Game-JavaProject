package views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainPanel extends JPanel{
	String player1, player2;
	Image Background;
	
	public MainPanel (){
		super();
		this.setLayout(new GridBagLayout());
		try {
			Background = ImageIO.read(getClass().getResource("/4824751.jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	
		
		GridBagConstraints c = new GridBagConstraints();
		
		final JTextField f1 = new JTextField(10);
		final JTextField f2 = new JTextField(10);
		
		JLabel title = new JLabel();
		title.setFont(new Font("Dialog", Font.BOLD, 36));
		title.setForeground(new Color(255,0,0));
		
		ImageIcon TitleIcon = new ImageIcon("Images/MarvelTitle.png");
	     Image image = TitleIcon.getImage();
		Image newimg = image.getScaledInstance(700, 700,  java.awt.Image.SCALE_SMOOTH);
		TitleIcon  = new ImageIcon(newimg);
	    title.setIcon(TitleIcon);
		
		c.insets = new Insets(0,0,0,0);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		this.add(title, c);
		c.insets = new Insets(0,0,0,0);
		
		JLabel l1 = new JLabel("Player 1");
		JLabel l2 = new JLabel("Player 2");
		
		
		f1.setFont(new Font("Dialog", Font.PLAIN, 24));
		f2.setFont(new Font("Dialog", Font.PLAIN, 24));
		l1.setFont(new Font("Dialog", Font.PLAIN, 24));
		l2.setFont(new Font("Dialog", Font.PLAIN, 24));
		
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		
		p1.setLayout(new BoxLayout(p1, BoxLayout.PAGE_AXIS));
		p2.setLayout(new BoxLayout(p2, BoxLayout.PAGE_AXIS));
		
		
		l1.setAlignmentX(Component.CENTER_ALIGNMENT);
		l2.setAlignmentX(Component.CENTER_ALIGNMENT);
		p1.add(l1);
		p1.add(f1);
		p2.add(l2);
		p2.add(f2);
		
		
		//c.ipadx = 20;
		//c.ipady = 20;
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		
		this.add(p1, c);
		
		c.gridx = 1;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		this.add(p2, c);
		
		JButton start = new JButton("Start Game");
		start.setSize(100,50);
		start.setFont(new Font("Dialog", Font.PLAIN, 24));
		start.addActionListener(new ActionListener(){

			
			public void actionPerformed(ActionEvent e) {
				if (f1.getText().equals("") || f2.getText().equals("")){	
					MessageBox.showError("Both players should enter their names");
					return;
				}
				
				player1 = f1.getText();
				player2 = f2.getText();
				//System.out.println("player1 = " + player1 + "\nplayer2 = " + player2);
				((JButton)e.getSource()).getParent().setVisible(false);
				((JButton)e.getSource()).getParent().getParent().add(new ChampionPanel(player1, player2));
				((JButton)e.getSource()).getParent().getParent().remove(((JButton)e.getSource()).getParent());
			}
			
		});
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.PAGE_END;
		
		this.add(start, c);
		
		
		
		
		
	}
	
	protected void paintComponent(Graphics g) {

	    super.paintComponent(g);
	    	g.drawImage(Background, 0, 0, getWidth(), getHeight(), null);
	}
	
	public Dimension getPreferredSize()
    {
        return new Dimension(Background.getWidth(this), Background.getHeight(this));
    }
	
	
	public static void main (String[] args){
		
		JFrame f = new JFrame();
		f.add(new MainPanel());
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
		//f.setUndecorated(true);
		f.setVisible(true);
		
	}
	public String getPlayer1(){
		return player1;
	}
	public String getPlayer2(){
		return player2;
	}
}
