package org.vpc.mbreakout.sprites.bars;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.vpc.mbreakout.screens.MBGameScreen;
import org.vpc.mbreakout.sprites.MBSprite;
import org.vpc.mbreakout.sprites.missiles.MissileSprite;
import org.vpc.mbreakout.util.MBUtils;

public class BarSprite extends MBSprite {
	private boolean fireEnabled;

	private int fireTimeout;

	private boolean verticalMoveEnabled;

	private boolean firing;

	public BarSprite(MBGameScreen game) {
		this(game, "/org/vpc/mbreakout/images/bar.png", 26, 8);
	}

	public BarSprite(MBGameScreen game, String image, int width, int height) {
		super(game, MBUtils.createImage(image), width, height);
		setXStep(3);
	}

	public BarSprite(MBGameScreen game, Image image, int width, int height) {
		super(game, image, width, height);
		setXStep(3);
	}

	public void setFireEnabled(boolean fireEnabled,int fireTimeout) {
		this.fireEnabled = fireEnabled;
		this.fireTimeout = fireTimeout;
	}

	public boolean isFireEnabled() {
		return fireEnabled;
	}

	public void setVerticalMoveEnabled(boolean verticalMoveEnabled) {
		this.verticalMoveEnabled = verticalMoveEnabled;
	}

	public boolean isVerticalMoveEnabled() {
		return verticalMoveEnabled;
	}

	public boolean tick() {
		boolean ticked = false;
		int keyStates = getGame().getKeyStates();
		int x0 = getX();
		int xs = getXStep();
		int mw = getWidth();
		int gw = getGame().getGameWidth();
		if (((keyStates & GameCanvas.LEFT_PRESSED) != 0)
				&& ((keyStates & GameCanvas.RIGHT_PRESSED) == 0)) {
			setFrame(1);
			if (x0 > 0) {
				move(-xs, 0);
				ticked = true;
			}
		} else if (((keyStates & GameCanvas.RIGHT_PRESSED) != 0)
				&& ((keyStates & GameCanvas.LEFT_PRESSED) == 0)) {
			setFrame(2);
			if (x0 < gw - mw) {
				move(xs, 0);
				if (getX() + mw > gw) {
					setPosition(gw - mw, 0);
				}
				ticked = true;
			}
		} else {
			setFrame(0);
		}

		if (verticalMoveEnabled) {
			int y0 = getY();
			int mh = getHeight();
			if (((keyStates & GameCanvas.UP_PRESSED) != 0)
					&& ((keyStates & GameCanvas.DOWN_PRESSED) == 0)) {
				if (y0 > 0) {
					move(0, -getYStep());
					ticked = true;
				}
			} else if (((keyStates & GameCanvas.DOWN_PRESSED) != 0)
					&& ((keyStates & GameCanvas.UP_PRESSED) == 0)) {
				if (y0 < getGame().getGameHeight() - mh - 1) {
					move(0, getYStep());
					ticked = true;
				}
			}
		}
		if (fireEnabled) {
			if (fireEnabled && (keyStates & GameCanvas.FIRE_PRESSED) != 0) {
				if (!firing) {
					firing = true;
					getGame().fireMissile(createMissile());
				}
			} else {
				firing = false;
			}
			fireTimeout--;
			if(fireTimeout<=0){
				fireEnabled=false;
			}
		}

		return ticked;
	}

	private MissileSprite createMissile() {
		return new MissileSprite(getGame());
	}

}
