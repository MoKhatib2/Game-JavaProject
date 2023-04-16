package views;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import network.Client;
import network.GameServer;

public class OnlinePlayerPanel extends JPanel{
	String name;
	String ip;
	JFrame frame;
	JPanel thisPanel;
	JButton join;
	JButton create;
	Client client;

	public OnlinePlayerPanel(){
		super();
		
		thisPanel = this;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.insets = new Insets(20,20,20,20);
		Font dFont = new Font("Dialog", Font.PLAIN, 24);


		final JTextField nameField = new JTextField();
		nameField.setFont(dFont);
		nameField.setPreferredSize(new Dimension(300, 40));

		cons.gridx = 0;
		cons.gridy = 0;
		this.add(new LabeledPanel("Name", nameField), cons);

		final JTextField ipField = new JTextField();
		ipField.setFont(dFont);
		ipField.setPreferredSize(new Dimension(300, 40));

		cons.gridx = 1;
		cons.gridy = 0;
		this.add(new LabeledPanel("IP Address", ipField), cons);
		
		create = new JButton("Create Game");
		create.setFont(dFont);
		create.setPreferredSize(new Dimension(200, 70));
		create.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent event) {
				//((JButton)event.getSource()).getParent().repaint();
				//((JButton)event.getSource()).getParent().revalidate();
				
				new Thread(new Runnable(){
					public void run() {
						new GameServer().main(null);						
					}
				}).start();
				
				
				try {
					client = new Client(nameField.getText(), ipField.getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
				
				create.setPreferredSize(new Dimension(400, 70));
				join.setEnabled(false);
				create.setEnabled(false);
				create.setText("Waiting for other player...");
				JFrame frame = getFrame((Container)event.getSource());
				while (true){
					System.out.println("checking if game started");
					if (client.isGameStarted()){
						frame.remove(getThisPanel((Container)event.getSource()));
						frame.add(new OnlineChampionPanel(client), 0);
						frame.getParent().repaint();
						frame.getParent().revalidate();
						break;
					}
				}

				
				
			}
		});

		cons.gridx = 0;
		cons.gridy = 1;
		cons.gridwidth = 2;
		this.add(create, cons);
		
		join = new JButton("Join Game");
		join.setFont(dFont);
		join.setPreferredSize(new Dimension(200, 70));
		join.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent event) {
				
				new Thread(new Runnable(){
					public void run() {
						try {
							Client client = new Client(nameField.getText(), ipField.getText());
							while(true){
								if (client.isGameStarted() && thisPanel.getParent() != null){
									
									thisPanel.getParent().add(new OnlineChampionPanel(client));
									thisPanel.getParent().getParent().getParent().getParent().repaint();
									thisPanel.getParent().getParent().getParent().getParent().remove(thisPanel);
									
								}
							}
							
						} catch (IOException e) {
							MessageBox.showError("Couldn't find a server with that IP Address");
						}
					}
				}).start();
				join.setPreferredSize(new Dimension(400, 70));
				join.setEnabled(false);
				create.setEnabled(false);
				join.setText("Waiting for other player...");
			}
		});

		cons.gridx = 0;
		cons.gridy = 2;
		cons.gridwidth = 2;
		this.add(join, cons);
		
		

	}
	public JFrame getFrame(Container comp){
		if (comp instanceof JFrame)
			return (JFrame) comp;
		return getFrame(comp.getParent());
	}
	public OnlinePlayerPanel getThisPanel(Container comp){
		if (comp instanceof OnlinePlayerPanel)
			return (OnlinePlayerPanel) comp;
		return getThisPanel(comp.getParent());
	}

	public static void main (String[] args){

		JFrame f = new JFrame();
		f.add(new OnlinePlayerPanel());
		//f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setSize(800,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		//f.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
		//f.setUndecorated(true);
		f.setVisible(true);

	}		
}
