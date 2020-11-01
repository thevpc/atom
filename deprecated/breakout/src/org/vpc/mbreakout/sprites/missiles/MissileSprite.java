package org.vpc.mbreakout.sprites.missiles;

import javax.microedition.lcdui.Image;
import javax.microedition.media.Player;

import org.vpc.mbreakout.screens.MBGameScreen;
import org.vpc.mbreakout.sprites.MBSprite;
import org.vpc.mbreakout.sprites.bricks.BrickSprite;
import org.vpc.mbreakout.util.MBUtils;


public class MissileSprite extends MBSprite{
	private static final Player shotSoundPlayer=MBUtils.createSoundPlayer(
			"/org/vpc/mbreakout/sounds/shot.wav", "audio/x-wav");
	public MissileSprite(MBGameScreen game) {
		this(game,"/org/vpc/mbreakout/images/missile.png", 4, 13);
	}
	
	public MissileSprite(MBGameScreen game,String image, int width, int height) {
		super(game,MBUtils.createImage(image),width,height);
		setXStep(0);
		setYStep(-3);
	}
	
	public MissileSprite(MBGameScreen game,Image image, int width, int height) {
		super(game,image,width,height);
		setXStep(0);
		setYStep(-3);
	}
	
	public boolean tick() {
		move(0, getYStep());
		if (getY() < 0) {
			setDead(true);
		}
		return true;
	}
	
	public void missileLaunched(){
		MBUtils.play(shotSoundPlayer);
	}
	
	public void collide(MBSprite other) {
		((BrickSprite) other).collide(this);
		setDead(true);
	}
}
