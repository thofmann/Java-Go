package Go;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: hofmannt
 * Date: 1/25/14
 * Time: 1:48 PM
 * This class contains all of the ImageIcons needed for Go.
 */

public class Icons {
	/**
	 * ImageIcon of a Black Stone
	 */
	public static final ImageIcon BLACKSTONE = createIcon("blackstone.png");
	/**
	 * ImageIcon of a White Stone
	 */
	public static final ImageIcon WHITESTONE = createIcon("whitestone.png");
	/**
	 * ImageIcon of a capture Black Stone
	 */
	public static final ImageIcon BLACKSTONECAPTURED = createIcon("blackstonecaptured.png");
	/**
	 * ImageIcon of a captured White Stone
	 */
	public static final ImageIcon WHITESTONECAPTURED = createIcon("whitestonecaptured.png");
	/**
	 * ImageIcon of Black territory
	 */
	public static final ImageIcon BLACKTERRITORY = createIcon("blackterritory.png");
	/**
	 * ImageIcon of White territory
	 */
	public static final ImageIcon WHITETERRITORY = createIcon("whiteterritory.png");

	/**
	 * Generates the resized ImageIcons
	 * @param file name of the image file
	 * @return ImageIcon
	 */
	private static ImageIcon createIcon(String file){
		try{
			URL url = Icon.class.getResource("/resources/"+file);
			Image img = ImageIO.read(url);
			img = img.getScaledInstance(20, 20, 5);
			return new ImageIcon(img);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
}
