package aquarium;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import network.Tracker;
import aquarium.gui.Aquarium;
import aquarium.gui.AquariumWindow;

public class Launcher extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Aquarium aquarium = null;
	private Tracker tracker = null;
	private JButton B_server = new JButton("Lancer un serveur");
	private JButton B_client = new JButton("Lancer un client");
	private JTextField T_IP = new JTextField("localhost", 15);
	private JButton B_tracker = new JButton("Lancer un tracker");
	private JTextField T_IP2 = new JTextField("localhost", 15);
	private JButton B_app = new JButton("Lancer une application");
	
	public Launcher() {
		JPanel panel = new JPanel();
		
		setTitle("Launcher");
		setSize(300,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		B_server.setBackground(new Color(220,20,60));
		B_server.setFocusable(false);
		B_server.addActionListener(this);
		B_server.setActionCommand("server");
		
		B_client.setBackground(new Color(60,179,113));
		B_client.setFocusable(false);
		B_client.addActionListener(this);
		B_client.setActionCommand("client");
		
		B_tracker.setBackground(new Color(220,20,60));
		B_tracker.setFocusable(false);
		B_tracker.addActionListener(this);
		B_tracker.setActionCommand("tracker");
		
		B_app.setBackground(new Color(60,179,113));
		B_app.setFocusable(false);
		B_app.addActionListener(this);
		B_app.setActionCommand("app");
		
		panel.add(B_server);
		panel.add(B_client);
		panel.add(T_IP);
		panel.add(B_tracker);
		panel.add(B_app);
		panel.add(T_IP2);
		
		setContentPane(panel);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("server"))
			aquarium = new Aquarium("server");
		else if(e.getActionCommand().equals("client"))
			aquarium = new Aquarium("client", T_IP.getText());
		else if(e.getActionCommand().equals("tracker")) {
			if (tracker == null) {
				tracker = new Tracker();
				System.out.println("Tracker started.");
			}	
		}
		else if(e.getActionCommand().equals("app"))
			aquarium = new Aquarium("app", T_IP2.getText());
		else
			return;
		if(aquarium != null) {
			AquariumWindow animation = new AquariumWindow(aquarium);
			animation.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					aquarium.delete();
				}
			});
			animation.displayOnscreen();
		}
	}
}
