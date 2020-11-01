package org.vpc.mbreakout.sprites.bricks;

import java.util.Vector;

import org.vpc.mbreakout.screens.MBGameScreen;
import org.vpc.mbreakout.sprites.MBSprite;
import org.vpc.mbreakout.sprites.balls.BallSprite;
import org.vpc.mbreakout.sprites.bars.BarSprite;

public class BrickFlashBallSprite extends BrickSprite {
	public BrickFlashBallSprite(MBGameScreen game) {
		super(game, 4, 50);
	}

	public void collide(MBSprite sprite) {
		if (sprite instanceof BarSprite) {
			Vector b=getGame().getBalls();
			for(int i=b.size()-1;i>=0;--i ){
				BallSprite ba=(BallSprite)b.elementAt(i);
				ba.setFastCoeff(2, 5000);
			}
			setDead(true);
		} else {
			getGame().addScore(getScore());
			setFalling(true);
		}
	}

}
