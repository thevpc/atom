package org.vpc.mbreakout.sprites.bars;

import org.vpc.mbreakout.screens.MBGameScreen;

public class BarSupermanSprite extends BarSprite{
	public BarSupermanSprite(MBGameScreen game) {
		super(game,"/org/vpc/mbreakout/images/bar.png", 26, 8);
		setXStep(getXStep()*2);
		setYStep(getYStep()*2);
	}

}
