package org.vpc.mbreakout.sprites.bricks;

import javax.microedition.lcdui.Image;

import org.vpc.mbreakout.screens.MBGameScreen;
import org.vpc.mbreakout.sprites.MBSprite;
import org.vpc.mbreakout.sprites.bars.BarSprite;
import org.vpc.mbreakout.util.MBUtils;

public class BrickSprite extends MBSprite {
	private boolean falling = false;

	private int score = 1;
	private boolean stone = false;

	public BrickSprite(MBGameScreen game, int index, int score) {
		this(game, "/org/vpc/mbreakout/images/brick" + index + ".png", 12, 6);
		this.score = score;
	}

	public BrickSprite(MBGameScreen game, String image, int width, int height) {
		super(game, MBUtils.createImage(image), width, height);
	}

	public BrickSprite(MBGameScreen game, Image image, int width, int height) {
		super(game, image, width, height);
	}

	public void collide(MBSprite sprite) {
		if (!(sprite instanceof BarSprite)) {
			getGame().addScore(score);
			if(!isStone()){
				setDead(true);
			}
			//setFalling(true);
		}
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public boolean tick() {
		if (isFalling() && !isDead()) {
			int y = getY() + getYStep();
			setPosition(getX(), y);
			if (y > 128) {
				setDead(true);
				setFalling(false);
			}
			return true;
		}
		return false;
	}

	public int getScore() {
		return score;
	}
	
	public boolean isStone() {
		return stone;
	}
	
	public void setStone(boolean stone) {
		this.stone = stone;
	}
	
}
