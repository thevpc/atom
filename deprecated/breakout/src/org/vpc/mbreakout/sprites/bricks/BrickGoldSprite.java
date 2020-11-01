package org.vpc.mbreakout.sprites.bricks;

import org.vpc.mbreakout.screens.MBGameScreen;
import org.vpc.mbreakout.sprites.MBSprite;
import org.vpc.mbreakout.sprites.bars.BarSprite;

public class BrickGoldSprite extends BrickSprite {
	private int life = 3;

	public BrickGoldSprite(MBGameScreen game) {
		super(game, 6, 50);
	}

	public void collide(MBSprite sprite) {
		if (sprite instanceof BarSprite) {
			getGame().addScore(50);
			setDead(true);
		} else {
			getGame().addScore((4-life)*2);
			life--;
			setFrame(3 - life);
			if (life == 0) {
				setFalling(true);
			}
		}
	}

}
