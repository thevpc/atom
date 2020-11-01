package org.vpc.mbreakout.sprites.balls;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.vpc.mbreakout.screens.MBGameScreen;
import org.vpc.mbreakout.sprites.MBSprite;
import org.vpc.mbreakout.sprites.bars.BarSprite;
import org.vpc.mbreakout.sprites.bricks.BrickSprite;
import org.vpc.mbreakout.util.MBUtils;

public class BallSprite extends MBSprite {
	private boolean attached;

	private int fastCoeff = 1;

	private int fastCoeffTimeout = 0;

	// private int defaultXStep=1;
	// private int defaultYStep=-1;
	// private int xFactor=1;
	// private int yFactor=1;

	public BallSprite(MBGameScreen game, int index) {
		this(game, "/org/vpc/mbreakout/images/ball" + (index + 1) + ".png", 5,
				5);
	}

	public BallSprite(MBGameScreen game, String image, int width, int height) {
		super(game, MBUtils.createImage(image), width, height);
		setXStep(1);
		setYStep(-1);
	}

	public BallSprite(MBGameScreen game, Image image, int width, int height) {
		super(game, image, width, height);
		setXStep(1);
		setYStep(-1);
	}

	public boolean tick() {
		int keyStates = getGame().getKeyStates();
		if (isAttached()) {
			BarSprite bar = getGame().getBar();
			setPosition(bar.getX() - getWidth() / 2 + bar.getWidth() / 2, bar
					.getY()
					- getHeight() - 1);
			if ((keyStates & GameCanvas.FIRE_PRESSED) != 0) {
				setAttached(false);
			}
			return false;
		} else {
			int xs = getXStep();
			int ys = getYStep();
			int x0 = getX();
			int y0 = getY();
			int gw = getGame().getGameWidth();
			int gh = getGame().getGameHeight();
			int mw = getWidth();
			int mh = getHeight();
			move(xs * fastCoeff, ys * fastCoeff);
			if (fastCoeff > 1) {
				fastCoeffTimeout--;
				if (fastCoeffTimeout <= 0) {
					fastCoeff = 1;
				}
			}
			if (x0 + mw > gw) {
				setPosition(gw - mw, y0);
				setXStep(-getXStep());
			} else if (x0 < 0) {
				setPosition(0, y0);
				setXStep(-xs);
			}
			if (y0 + mh > gh) {
				// game over
				// setPosition(x0, gh-mh);
				// setYStep(-ys);
				setDead(true);
			} else if (getY() < 0) {
				setPosition(x0, 0);
				setYStep(-ys);
			}
			return true;
		}
	}

	public void setAttached(boolean attached) {
		this.attached = attached;
		if (attached) {
			BarSprite bar = getGame().getBar();
			setPosition(bar.getX() - getWidth() / 2 + bar.getWidth() / 2, bar
					.getY()
					- getHeight() - 1);
		}
	}

	public boolean isAttached() {
		return attached;
	}

	public void collide(MBSprite other) {
		if (other instanceof BarSprite) {
			BarSprite bar = (BarSprite) other;
			int x = getX();
			int ox = other.getX();
			int ow = other.getWidth();
			if (x < (ox + (ow / 3))) {
				setYStep(-1);
				setXStep(-2);
			} else if (x < (ox + (2 * ow / 3))) {
				setYStep(-1);
				setXStep(getXStep() < 0 ? 1 : -1);
			} else {
				setYStep(-1);
				setXStep(+2);
			}
			// setYStep(-getYStep());
			setPosition(getX(), bar.getY() - getHeight() - 1);
		} else if (other instanceof BrickSprite) {
			int ox = getOldX();
			int oy = getOldY();
			int nx = getX();
			int ny = getY();
			int bx = other.getX();
			int by = other.getY();
			int w = getWidth();
			int h = getHeight();
			int bw = other.getWidth();
			int bh = other.getHeight();
			if ((ox + w) < bx && bx <= (nx + w)) {
				setXStep(-getXStep());
				setPosition(bx - w, ny);
			} else if (ox > (bx + bw) && nx <= (bx + bw)) {
				setXStep(-getXStep());
				setPosition(bx + bw, ny);
			}
			nx = getX();
			if ((oy + h) <= by && by <= (ny + h)) {
				setYStep(-getYStep());
				setPosition(nx, by - h);
			} else if (oy >= (by + bh) && ny <= (by + bh)) {
				setYStep(-getYStep());
				setPosition(nx, by + bh);
			}
			((BrickSprite) other).collide(this);
		}
	}

	public void setFastCoeff(int fastCoeff, int fastCoeffTimeout) {
		this.fastCoeff = fastCoeff;
		this.fastCoeffTimeout = fastCoeffTimeout;
	}
}
