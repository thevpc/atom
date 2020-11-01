package org.vpc.mbreakout.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

public class MBUtils {
    public static final Random random = new Random();

	public static Image createImage(String filename) {
		Image image = null;
		try {
			image = Image.createImage(filename);
		} catch (java.io.IOException e) {
			System.err.println("image not found "+filename);
			// just let return value be null
		}
		return image;
	}
	
    public static void fillRandomTile(TiledLayer background,int NUM_TILES,int WIDTH_IN_TILES,int HEIGHT_IN_TILES)
    {
    	
    	for (int row = 0; row < HEIGHT_IN_TILES + 1; ++row)
        {
            for (int column = 0; column < WIDTH_IN_TILES; ++column)
            {
                background.setCell(column, row, random(NUM_TILES) + 1);
            }
        }
    }


    public static int random(int size)
    {
        return (random.nextInt() & 0x7FFFFFFF) % size;
    }

    public static Player createSoundPlayer(String filename, String format)
    {
        Player p = null;
        try
        {
            InputStream is = MBUtils.class.getResourceAsStream(filename);
            p = Manager.createPlayer(is, format);
            p.prefetch();
        }
        catch (IOException ex)
        {
            // ignore
        }
        catch (MediaException ex)
        {
            // ignore
        }
        return p;
    }
    
    public static void play(Player player)
    {
        if (player != null)
        {
            try
            {
            	player.stop();
            	player.setMediaTime(0L);
            	player.start();
            }
            catch (MediaException ex)
            {
                // ignore
            }
        }
    }


}
