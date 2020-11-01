package org.vpc.mbreakout.util;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

public class RandomTiledBackground {
    private int WIDTH = 128;
    private int HEIGHT = 128;
    private int TILE_WIDTH = 16;
    private int TILE_HEIGHT = 16;
    private int WIDTH_IN_TILES = WIDTH / TILE_WIDTH;
    private int HEIGHT_IN_TILES = HEIGHT / TILE_HEIGHT;
    private int NUM_TILES = 16;
    private final TiledLayer background;
    private int backgroundScroll;
	public RandomTiledBackground(String file,int WIDTH_IN_TILES,int HEIGHT_IN_TILES,int TILE_WIDTH,int TILE_HEIGHT) {
		this.WIDTH_IN_TILES=WIDTH_IN_TILES;
		this.HEIGHT_IN_TILES=HEIGHT_IN_TILES;
		this.TILE_WIDTH=TILE_WIDTH;
		this.TILE_HEIGHT=TILE_HEIGHT;
        Image backgroundTiles = MBUtils.createImage("/org/vpc/mbreakout/images/bg1.png");
        background = new TiledLayer(WIDTH_IN_TILES,
                                    HEIGHT_IN_TILES + 1,
                                    backgroundTiles,
                                    TILE_WIDTH,
                                    TILE_HEIGHT);
        fillRandomTile();
        backgroundScroll = 1 - TILE_HEIGHT;
        background.setPosition(0, backgroundScroll);
	}
	
	public TiledLayer getBackground() {
		return background;
	}
	
    private void scrollTileRows()
    {
        for (int row = HEIGHT_IN_TILES; row >= 1; row--)
        {
            for (int column = 0; column < WIDTH_IN_TILES; column++)
            {
                int cell = background.getCell(column, row - 1);
                background.setCell(column, row, cell);
            }
        }
        fillTileRow(0);
    }
    
    private void fillTileRow(int row)
    {
        for (int column = 0; column < WIDTH_IN_TILES; ++column)
        {
            background.setCell(column, row, MBUtils.random(NUM_TILES) + 1);
        }
    }
    
    public void fillRandomTile()
    {
    	
    	for (int row = 0; row < HEIGHT_IN_TILES + 1; ++row)
        {
            for (int column = 0; column < WIDTH_IN_TILES; ++column)
            {
                background.setCell(column, row, MBUtils.random(NUM_TILES) + 1);
            }
        }
    }
    
    public void scrollBackground()
    {
        backgroundScroll += 2;       // scrolling by 1 was too slow
        if (backgroundScroll > 0)
        {
            backgroundScroll = 2 - TILE_HEIGHT;
            scrollTileRows();
        }
        background.setPosition(0, backgroundScroll);
    }

}
