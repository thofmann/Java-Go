package Go;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: hofmannt
 * Date: 1/24/14
 * Time: 10:21 PM
 * This class represents one Tile in the game of Go as a JButton.
 */

public class Tile extends JButton {
	/**
	 * state of the Tile, 0-7
	 */
	private int state;
	/**
	 * x-coordinate of the Tile
	 */
	private int xCoord;
	/**
	 * y-coordinate of the Tile
	 */
	private int yCoord;

	/**
	 * Constructor method, assigns values and adds styles to JButton
	 * @param xCoord x-coordinate
	 * @param yCoord y-coordinate
	 */
	public Tile(int xCoord, int yCoord){
		state = 0;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		setBorder(new LineBorder(Color.BLACK, 1));
		setBackground(new Color(219, 177, 92));
		setIcon(Icons.WHITESTONE);
	}

	/**
	 * Sets a new state for the Tile
	 * @param state int in range 0-7
	 */
	public void setState(int state){
		this.state = state;
		if (state == 0){
			setIcon(null);
		}
		else if (state == 1){
			setIcon(Icons.BLACKSTONE);
		}
		else if (state == 2){
			setIcon(Icons.WHITESTONE);
		}
		else if (state == 3){
			setIcon(Icons.BLACKSTONECAPTURED);
		}
		else if (state == 4){
			setIcon(Icons.WHITESTONECAPTURED);
		}
		else if (state == 5){
			setIcon(Icons.BLACKTERRITORY);
		}
		else if (state == 6){
			setIcon(Icons.WHITETERRITORY);
		}
	}

	/**
	 * Returns the state of the Tile
	 * @return int in range 0-7
	 */
	public int getState(){
		return state;
	}

	/**
	 * Returns the color of the stone on the Tile
	 * @return int 0, 1, or 2
	 */
	public int getStone(){
		if (state == 0 || state == 5 || state == 6){
			return 0;
		}
		else if (state == 1 || state == 3){
			return 1;
		}
		else{
			return 2;
		}
	}

	/**
	 * Returns the x-coordinate
	 * @return int x-coordinate
	 */
	public int getXCoord(){
		return xCoord;
	}

	/**
	 * Returns the y-coordinate
	 * @return int y-coordinate
	 */
	public int getYCoord(){
		return yCoord;
	}

}
