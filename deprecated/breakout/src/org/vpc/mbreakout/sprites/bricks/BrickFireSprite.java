package org.vpc.mbreakout.sprites.bricks;

import org.vpc.mbreakout.screens.MBGameScreen;
import org.vpc.mbreakout.sprites.MBSprite;
import org.vpc.mbreakout.sprites.bars.BarSprite;

public class BrickFireSprite extends BrickSprite {
	public BrickFireSprite(MBGameScreen game) {
		super(game, 2, 20);
	}

	public void collide(MBSprite sprite) {
		if (sprite instanceof BarSprite) {
			((BarSprite)sprite).setFireEnabled(true,5000);
			setDead(true);
		} else {
			getGame().addScore(getScore());
			setFalling(true);
		}
	}

}
