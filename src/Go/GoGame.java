package Go;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: hofmannt
 * Date: 1/24/14
 * Time: 9:59 PM
 * This is the heavy class that acts as the main Go game.
 */

public class GoGame extends JFrame {
	/**
	 * The tiles of the board as a two-dimensional array
	 */
	private Tile[][] boardTiles;
	/**
	 * Size of the board
	 */
	private int boardSize;
	/**
	 * Whose turn it is, 1 or 2
	 */
	private int playerToGo;
	/**
	 * Whose turn it is NOT, 1 or 2
	 */
	private int playerWaiting;
	/**
	 * Number of humans playing
	 */
	private int humans;
	/**
	 * Label for Black's score
	 */
	private JLabel blackScore = new JLabel("Black: -6.5");
	/**
	 * Label for White's score
	 */
	private JLabel whiteScore = new JLabel("White: 6.5");
	/**
	 * Round of the game
	 */
	private int round = 1;
	/**
	 * Label for the round
	 */
	private JLabel roundLabel = new JLabel("Round: 1");
	/**
	 * In 1-human games, is Player 1 the human?
	 */
	private boolean humanIsPlayerOne = false;
	/**
	 * Is the AI testing moves?
	 */
	private boolean testing = false;
	/**
	 * Is a tile currently unplayable because of the Ko rule?
	 */
	private Tile forbiddenKo;
	/**
	 * Has one of the players passed?
	 */
	private boolean pass = false;

	/**
	 * Constructor class. Assigns all of the attributes, creates the board, and begins the game.
	 * @param size dimensions of the board
	 * @param humans how many humans are playing, 1 or 2
	 */
	public GoGame(int size, int humans){
		boardSize = size;
		this.humans = humans;
		playerToGo = 1;
		playerWaiting = 2;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 800);
		setLayout(new BorderLayout());
		setResizable(false);
		setTitle("Go");
		getContentPane().setBackground(Color.BLACK);
		setLocationRelativeTo(null);
		setVisible(true);
		boardTiles = new Tile[size][size];
		JPanel board = new JPanel();
		board.setLayout(new GridLayout(size, size));
		add(board, BorderLayout.CENTER);
		for(int x = 0; x < size; x ++){
			for(int y = 0; y < size; y ++){
				boardTiles[x][y] = new Tile(x, y);
				boardTiles[x][y].addActionListener(new StonePressed());
				board.add(boardTiles[x][y]);
			}
		}
		evaluateBoard();
		if (humans == 0){
			computerMove();
		}
		else if (humans == 1){
			Random myRand = new Random();
			if (myRand.nextInt(2) == 1){
				humanIsPlayerOne = true;
			}
			else{
				computerMove();
			}
		}
		JPanel score = new JPanel();
		score.setLayout(new FlowLayout());
		score.add(whiteScore);
		score.add(blackScore);
		score.add(roundLabel);
		add(score, BorderLayout.NORTH);
		JPanel options = new JPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));
		add(options, BorderLayout.SOUTH);
		JButton newGame = new JButton("New Game");
		options.add(newGame);
		newGame.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new GoMenu();
						dispose();
					}
				}
		);
		JButton passTurn = new JButton("Pass");
		options.add(passTurn);
		passTurn.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (isHumanTurn()) {
							pass();
						}
					}
				}
		);
	}

	/**
	 * Get the stone from given coordinates
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return int Stone at coordinate, 0 for none, 1 for black, 2 for white
	 */
	private int getStone(int x, int y){
		if (x < 0 || x >= boardSize || y < 0 || y >= boardSize){
			return -1;
		}
		else{
			return boardTiles[x][y].getStone();
		}
	}

	/**
	 * Get the stone state from given coordinates
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return int Stone state at coordinate, value can be 0-7
	 */
	private int getStoneState(int x, int y){
		if (x < 0 || x >= boardSize || y < 1 || y >= boardSize){
			return -1;
		}
		else{
			return boardTiles[x][y].getState();
		}
	}

	/**
	 * Evaluate the board for scoring and update score/round labels
	 */
	private void evaluateBoard(){
		for (int x = 0; x < boardSize; x++){
			for (int y = 0; y < boardSize; y++){
				int influence = 0;
				for (int xx = -3; xx <= 3; xx++){
					for (int yy = -3; yy <= 3; yy++){
						if (getStone(x+xx, y+yy) == 1){
							influence += 7 - Math.abs(xx) - Math.abs(yy);
						}
						else if (getStone(x+xx, y+yy) == 2){
							influence -= 7 - Math.abs(xx) - Math.abs(yy);
						}
					}
				}
				if (boardTiles[x][y].getStone() == 0){
					if (influence > 2){
						boardTiles[x][y].setState(5);
					}
					else if (influence < -2){
						boardTiles[x][y].setState(6);
					}
					else{
						boardTiles[x][y].setState(0);
					}
				}
				else if (boardTiles[x][y].getStone() == 1){
					if (influence < -2){
						boardTiles[x][y].setState(3);
					}
					else{
						boardTiles[x][y].setState(1);
					}
				}
				else{
					if (influence > 2){
						boardTiles[x][y].setState(4);
					}
					else{
						boardTiles[x][y].setState(2);
					}
				}
			}
		}
		evaluateGroups();
		whiteScore.setText("White: " + getWhiteScore());
		blackScore.setText("Black: "+getBlackScore());
		roundLabel.setText("Round: "+round);
	}

	/**
	 * Evaluates alive groups for scoring
	 */
	private void evaluateGroups(){
		boolean success;
		do{
			success = false;
			for (int x = 0; x < boardSize; x++){
				for (int y = 0; y < boardSize; y++){
					if (getStoneState(x,y) == 3){
						if (getStoneState(x+1,y) == 1 ||
								getStoneState(x-1,y) == 1 ||
								getStoneState(x,y+1) == 1 ||
								getStoneState(x,y-1) == 1){
							boardTiles[x][y].setState(1);
							success = true;
						}
					}
					else if (getStoneState(x,y) == 4){
						if (getStoneState(x+1,y) == 2 ||
								getStoneState(x-1,y) == 2 ||
								getStoneState(x,y+1) == 2 ||
								getStoneState(x,y-1) == 2){
							boardTiles[x][y].setState(2);
							success = true;
						}
					}
				}
			}
		} while (success);
	}

	/**
	 * Determines if it is a human's turn to play
	 * @return boolean result
	 */
	private boolean isHumanTurn(){
		if (humans == 2){
			return true;
		}
		else if (humans == 1 && ((humanIsPlayerOne && playerToGo == 1) || (!humanIsPlayerOne && playerToGo == 2))){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Calculate computer's move and complete it
	 */
	private void computerMove(){
		testing = true;
		Random myRandom = new Random();
		ArrayList<Tile> moves = getPossibleMoves();
		if (moves.size() > 0){
			Tile selection = null;
			float highestPoints = 0;
			for (Tile move : moves){
				float points = 50*testPoints(move);
				if (points > 0){
					points += myRandom.nextInt(5);
					if (points > highestPoints){
						selection = move;
						highestPoints = points;
					}
				}
			}
			testing = false;
			if (selection == null){
				pass();
			}
			else{
				nextMove(selection);
			}
		}
	}

	/**
	 * Pass instead of playing
	 */
	private void pass(){
		if (pass){
			endGame();
		}
		else{
			pass = true;
			int temp = playerToGo;
			playerToGo = playerWaiting;
			playerWaiting = temp;
			if (!isHumanTurn()){
				javax.swing.Timer myTimer = new Timer(10, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						computerMove();
					}
				});
				myTimer.setRepeats(false);
				myTimer.start();
			}
		}
	}

	/**
	 * Used by the computerMove() AI to score each possible move
	 * @param selection Tile to be evaluated
	 * @return float change in points as a result of the move
	 */
	private float testPoints(Tile selection){
		Tile[][] temp = new Tile[boardSize][boardSize];
		for(int x = 0; x < boardSize; x ++){
			for(int y = 0; y < boardSize; y ++){
				temp[x][y] = new Tile(x, y);
				temp[x][y].setState(boardTiles[x][y].getState());
			}
		}
		float startingScore;
		if (playerToGo == 1){
			startingScore = getBlackScore();
		}
		else{
			startingScore = getWhiteScore();
		}
		nextMove(selection);
		float endingScore;
		if (playerToGo == 1){
			endingScore = getBlackScore();
		}
		else{
			endingScore = getWhiteScore();
		}
		for(int x = 0; x < boardSize; x ++){
			for(int y = 0; y < boardSize; y ++){
				boardTiles[x][y].setState(temp[x][y].getState());
			}
		}
		return endingScore - startingScore;
	}

	/**
	 * Complete the given move
	 * @param selection Tile that was selected
	 */
	private void nextMove(Tile selection){
		int x = selection.getXCoord();
		int y = selection.getYCoord();
		boardTiles[x][y].setState(playerToGo);
		forbiddenKo = null;
		if (getStone(x+1, y) == playerWaiting && isDead(getGroup(x+1, y))){
			kill(getGroup(x+1, y));
		}
		if (getStone(x-1, y) == playerWaiting && isDead(getGroup(x-1, y))){
			kill(getGroup(x-1, y));
		}
		if (getStone(x, y+1) == playerWaiting && isDead(getGroup(x, y+1))){
			kill(getGroup(x, y+1));
		}
		if (getStone(x, y-1) == playerWaiting && isDead(getGroup(x, y-1))){
			kill(getGroup(x, y-1));
		}
		if (!testing){
			round ++;
			pass = false;
			int temp = playerToGo;
			playerToGo = playerWaiting;
			playerWaiting = temp;
		}
		evaluateBoard();
		if (!isHumanTurn() && !testing){
			javax.swing.Timer myTimer = new Timer(10, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					computerMove();
				}
			});
			myTimer.setRepeats(false);
			myTimer.start();
		}
	}

	/**
	 * Determines if the given group is dead or alive
	 * @param group ArrayList of tiles in a group to be evaluated
	 * @return boolean Is group alive?
	 */
	private boolean isDead(ArrayList<Tile> group){
		boolean dead = true;
		for (Tile tile : group){
			int x = tile.getXCoord();
			int y = tile.getYCoord();
			if(getStone(x+1, y) == 0){
				dead = false;
			}
			else if(getStone(x-1, y) == 0){
				dead = false;
			}
			else if(getStone(x, y+1) == 0){
				dead = false;
			}
			else if(getStone(x, y-1) == 0){
				dead = false;
			}
		}
		return dead;
	}

	/**
	 * Finds the rest of the Tiles in the group of a given Tile
	 * @param startX x-coordinate of first Tile
	 * @param startY y-coordinate of first Tile
	 * @return ArrayList of Tiles in the group
	 */
	private ArrayList<Tile> getGroup(int startX, int startY){
		int stone = getStone(startX, startY);
		ArrayList<Tile> group = new ArrayList<Tile>();
		group.add(boardTiles[startX][startY]);
		boolean doneLooping = false;
		while (!doneLooping){
			doneLooping = true;
			int len = group.size();
			for(int i=0; i<len; i++){
				Tile tile = group.get(i);
				int x = tile.getXCoord();
				int y = tile.getYCoord();
				if(getStone(x+1, y) == stone && !group.contains(boardTiles[x+1][y])){
					group.add(boardTiles[x+1][y]);
					doneLooping = false;
				}
				if(getStone(x-1, y) == stone && !group.contains(boardTiles[x-1][y])){
					group.add(boardTiles[x-1][y]);
					doneLooping = false;
				}
				if(getStone(x, y+1) == stone && !group.contains(boardTiles[x][y+1])){
					group.add(boardTiles[x][y+1]);
					doneLooping = false;
				}
				if(getStone(x, y-1) == stone && !group.contains(boardTiles[x][y-1])){
					group.add(boardTiles[x][y-1]);
					doneLooping = false;
				}
			}
		}
		return group;
	}

	/**
	 * Removes a given dead group
	 * @param group ArrayList group of Tiles
	 */
	private void kill(ArrayList<Tile> group){
		if (!testing && group.size() == 1){
			forbiddenKo = group.get(0);
		}
		for (Tile tile : group){
			int x = tile.getXCoord();
			int y = tile.getYCoord();
			boardTiles[x][y].setState(0);
		}
	}

	/**
	 * Calculates black's net score
	 * @return float black's score
	 */
	private float getBlackScore(){
		return (float) getBlackRawScore() - (float) getWhiteRawScore()- (float) 6.5;
	}

	/**
	 * Calculate's white's net score
	 * @return float white's score
	 */
	private float getWhiteScore(){
		return (float) getWhiteRawScore() - (float) getBlackRawScore() + (float) 6.5;
	}

	/**
	 * Calculate's black's raw score
	 * @return int black's raw score
	 */
	private int getBlackRawScore(){
		int score = 0;
		for (Tile[] row : boardTiles){
			for (Tile tile : row){
				if (tile.getState() == 1 || tile.getState() == 4 || tile.getState() == 5){
					score ++;
				}
			}
		}
		return score;
	}

	/**
	 * Calculates white's raw score
	 * @return int white's raw score
	 */
	private int getWhiteRawScore(){
		int score = 0;
		for (Tile[] row : boardTiles){
			for (Tile tile : row){
				if (tile.getState() == 2 || tile.getState() == 3 || tile.getState() == 6){
					score ++;
				}
			}
		}
		return score;
	}

	/**
	 * Determine all possible moves this turn
	 * @return ArrayList of possible Tiles
	 */
	private ArrayList<Tile> getPossibleMoves(){
		ArrayList<Tile> moves = new ArrayList<Tile>();
		for (Tile[] row : boardTiles){
			for (Tile tile : row){
				int x = tile.getXCoord();
				int y = tile.getYCoord();
				if (tile.getStone() == 0 && !isSuicide(x, y) && tile != forbiddenKo){
					moves.add(tile);
				}
			}
		}
		return moves;
	}

	/**
	 * Determines if the move is ineligible due to the Suicide rule
	 * @param x x-coordinate of Tile
	 * @param y y-coordinate of Tile
	 * @return boolean Is the move suicide?
	 */
	private boolean isSuicide(int x, int y){
		int tempState = boardTiles[x][y].getState();
		boardTiles[x][y].setState(playerToGo);
		boolean suicide = false;
		if (isDead(getGroup(x, y))){
			suicide = true;
		}
		if (getStone(x+1, y) == playerWaiting && isDead(getGroup(x+1, y))){
			suicide = false;
		}
		if (getStone(x-1, y) == playerWaiting && isDead(getGroup(x-1, y))){
			suicide = false;
		}
		if (getStone(x, y+1) == playerWaiting && isDead(getGroup(x, y+1))){
			suicide = false;
		}
		if (getStone(x, y-1) == playerWaiting && isDead(getGroup(x, y-1))){
			suicide = false;
		}
		boardTiles[x][y].setState(tempState);
		return suicide;
	}

	/**
	 * Ends the game, informs the user of the winner, and returns to the menu window
	 */
	private void endGame(){
		if (getBlackScore() > getWhiteScore()){
			JOptionPane.showMessageDialog(null, "Black Wins!");
		}
		else{
			JOptionPane.showMessageDialog(null, "White Wins!");
		}
		new GoMenu();
		dispose();
	}

	/**
	 * ActionListener for pressing a Tile
	 */
	public class StonePressed implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			Tile selection = (Tile) e.getSource();
			if (isHumanTurn() && getPossibleMoves().contains(selection)){
				nextMove(selection);
			}
		}
	}
}
