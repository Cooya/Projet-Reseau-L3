package aquarium.gui;

import java.awt.LayoutManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AquariumWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private final Aquarium aquarium;
	private final static int MARGIN = 10;
	protected static final String BUTTON1 = "1";
	protected static final String BUTTON2 = "2";

	public AquariumWindow(Aquarium aquarium) {
		this.aquarium = aquarium;
	}

	public void displayOnscreen() {
		setTitle("Aquarium réseau");
		setSize(Aquarium.getSizeX() + MARGIN, Aquarium.getSizeY() + MARGIN*5);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(buildContentPane());
		setVisible(true);

		ScheduledExecutorService e = Executors.newScheduledThreadPool(1);
		e.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				synchronized (aquarium.getLockContent()) {
					aquarium.animate();
				}
			}
			
		}, 0, 20, TimeUnit.MILLISECONDS);			
	}
	
	private JPanel buildContentPane(){
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout((LayoutManager) new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // dessus/dessous
		mainPanel.add(aquarium);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(0.5f); // centrer
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS)); // côte à côte
		JButton addFish = new JButton("Ajouter un poisson");
		addFish.setFocusable(false);
		addFish.addActionListener(aquarium);
		addFish.setActionCommand(BUTTON1);
		buttonsPanel.add(addFish);
		JButton addShark = new JButton("Ajouter un requin");
		addShark.setFocusable(false);
		addShark.addActionListener(aquarium);
		addShark.setActionCommand(BUTTON2);
        buttonsPanel.add(addShark);
        
        mainPanel.add(aquarium);
        mainPanel.add(buttonsPanel);
        return mainPanel;
	}
	

}
