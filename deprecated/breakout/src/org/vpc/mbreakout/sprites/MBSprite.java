package org.vpc.mbreakout.sprites;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import org.vpc.mbreakout.screens.MBGameScreen;
import org.vpc.mbreakout.util.MBUtils;


public class MBSprite extends Sprite{
	public static final int SIDE_NORTH=1;
	public static final int SIDE_WEST=2;
	public static final int SIDE_EAST=4;
	public static final int SIDE_SOUTH=8;

	private boolean dead=false;
	private int xStep=1;
	private int yStep=1;
	private MBGameScreen game;
	private int oldX;
	private int oldY;
	public MBSprite(MBGameScreen game,String image, int width, int height) {
		this(game, MBUtils.createImage(image),width,height);
	}
	
	public MBSprite(MBGameScreen game,Image image, int width, int height) {
		super(image,width,height);
		//System.out.println("sprite[w="+this.getWidth()+";h="+this.getHeight()+";s="+this.getFrameSequenceLength()+"] : "+"image["+image.getWidth()+"/"+image.getHeight()+"]")
		this.game=game;
	}
	
	public void setXStep(int step) {
		xStep = step;
	}
	
	public void setYStep(int step) {
		yStep = step;
	}
	
	public int getXStep() {
		return xStep;
	}
	
	public int getYStep() {
		return yStep;
	}
	
	public boolean tick(){
		return false;
	}
	
	public MBGameScreen getGame() {
		return game;
	}
	
	public void checkCollision(MBSprite other) {
		if(collidesWith(other, true)){
			collide(other);
		}
	}
	
	public void collide(MBSprite other){
		
	}
	
	public void backupPosition(){
		oldX=getX();
		oldY=getY();
	}
	
	public void restorePosition(){
		setPosition(oldX,oldY);
	}
	
	public int getOldX() {
		return oldX;
	}
	
	public int getOldY() {
		return oldY;
	}
	
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}

}
