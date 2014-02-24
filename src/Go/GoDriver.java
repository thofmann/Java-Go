package Go;

/**
 * Created with IntelliJ IDEA.
 * User: hofmannt
 * Date: 1/22/14
 * Time: 1:50 PM
 * This was definitely the most difficult lab so far, because I decided to challenge myself with the open-ended
 * project. I chose to create the board game Go with the options for Human-vs-Human, Human-vs-AI, and AI-vs-AI
 * games on boards of dimensions 9x9, 13x13, or 19x19. It was definitely worth doing, because I now feel much more
 * comfortable working with Java having created an entire game with it from start to finish. One of the challenges
 * were writing the scoring algorithm, because the "territory" concept in Go is very hard to put into an algorithm.
 * A similar challenge was writing a decent AI, as Go is known for being very difficult to write an AI for. The one
 * I created is good enough to be fun to play against, but definitely has many drawbacks. All required features of
 * the lab were included. The GoGame class is a JFrame-based main window using an embedded layout with three
 * different layout managers (FlowLayout, GridLayout, and BoxLayout). The Start, New Game, and Pass buttons all
 * use anonymous inner-class ActionListeners. The menu options and the Tiles in the game use ActionListeners that
 * respond to multiple sources. The basic information of the round number and current score are displayed throughout
 * the game, and a JOptionPane is used at the end to notify the user which player wins.
 */
public class GoDriver {

	/**
	 * Main driver for the entire game
	 * @param args ignored
	 */
	public static void main(String[] args){
		new GoMenu();
	}

}