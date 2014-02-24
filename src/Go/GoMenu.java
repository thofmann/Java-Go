package Go;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: hofmannt
 * Date: 1/22/14
 * Time: 1:55 PM
 * This class is the menu where the user can select the game options.
 */
public class GoMenu extends JFrame {

	/**
	 * Title for the menu
	 */
	private JLabel title = new JLabel("Go Menu");
	/**
	 * Label for the board size options
	 */
	private JLabel sizeLabel = new JLabel("Board Size:");
	/**
	 * Label for the human players options
	 */
	private JLabel humansLabel = new JLabel("Human Players:");
	/**
	 * JButtons for selecting number of humans
	 */
	private JButton[] humansButtons = new JButton[3];
	/**
	 * JButtons for selecting the board size
	 */
	private JButton[] sizeButtons = new JButton[3];
	/**
	 * JButton to start the game
	 */
	private JButton startButton = new JButton("Start!");
	/**
	 * Default number of humans
	 */
	private int humans = 1;
	/**
	 * Default board size
	 */
	private int size = 19;

	/**
	 * Constructor class, creates the menu
	 */
	public GoMenu(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 420);
		setLayout(null);
		setResizable(false);
		setTitle("Go");
		getContentPane().setBackground(Color.BLACK);
		addLabels();
		addButtons();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Adds labels to the menu
	 */
	private void addLabels(){
		addLabel(title, 150, 25);
		addLabel(sizeLabel, 140, 75);
		addLabel(humansLabel, 120, 200);
	}

	/**
	 * Adds a single label to the menu
	 * @param label to be added
	 * @param locX x-location for label
	 * @param locY y-location for label
	 */
	private void addLabel(JLabel label, int locX, int locY){
		label.setLocation(locX, locY);
		label.setSize(200, 24);
		label.setFont(new Font("SansSerif", Font.PLAIN, 24));
		label.setForeground(Color.WHITE);
		label.setVisible(true);
		add(label);
	}

	/**
	 * Adds buttons to the menu
	 */
	private void addButtons(){
		sizeButtons[0] = new JButton("9");
		sizeButtons[1] = new JButton("13");
		sizeButtons[2] = new JButton("19");
		for (int i = 0; i <= 2; i++){
			sizeButtons[i].setSize(50, 50);
			sizeButtons[i].setLocation(115 + 60 * i, 110);
			sizeButtons[i].setFont(new Font("SansSerif", Font.BOLD, 15));
			sizeButtons[i].addActionListener(new SizeButtonsListener());
			add(sizeButtons[i]);
		}
		for (int i = 0; i <= 2; i++){
			humansButtons[i] = new JButton(i+"");
			humansButtons[i].setSize(50, 50);
			humansButtons[i].setLocation(115 + 60 * i, 235);
			humansButtons[i].setFont(new Font("SansSerif", Font.BOLD, 15));
			humansButtons[i].addActionListener(new HumansButtonsListener());
			add(humansButtons[i]);
		}
		startButton.setSize(100, 50);
		startButton.setLocation(150, 310);
		startButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						new GoGame(size, humans);
						dispose();
					}
				}
		);
		add(startButton);
		paintSizeButtons();
		paintHumansButtons();
	}

	/**
	 * Colors the size buttons
	 */
	private void paintSizeButtons(){
		for (int i = 0; i <= 2; i++){
			sizeButtons[i].setBackground(Color.WHITE);
		}
		if (size == 9){
			sizeButtons[0].setBackground(Color.CYAN);
		}
		else if (size == 13){
			sizeButtons[1].setBackground(Color.CYAN);
		}
		else{
			sizeButtons[2].setBackground(Color.CYAN);
		}
	}

	/**
	 * Colors the humans buttons
	 */
	private void paintHumansButtons(){
		for (int i = 0; i <= 2; i++){
			humansButtons[i].setBackground(Color.WHITE);
		}
		humansButtons[humans].setBackground(Color.CYAN);
	}

	/**
	 * ActionListener for clicking size buttons
	 */
	private class SizeButtonsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			JButton clickedButton = (JButton) e.getSource();
			if (clickedButton == sizeButtons[0]){
				size = 9;
			}
			else if (clickedButton == sizeButtons[1]){
				size = 13;
			}
			else{
				size = 19;
			}
			paintSizeButtons();
		}
	}

	/**
	 * ActionListener for clicking humans buttons
	 */
	private class HumansButtonsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			JButton clickedButton = (JButton) e.getSource();
			if (clickedButton == humansButtons[0]){
				humans = 0;
			}
			else if (clickedButton == humansButtons[1]){
				humans = 1;
			}
			else{
				humans = 2;
			}
			paintHumansButtons();
		}
	}

}