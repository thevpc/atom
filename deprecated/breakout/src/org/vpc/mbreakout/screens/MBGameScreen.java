package org.vpc.mbreakout.screens;

import java.util.Vector;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;

import org.vpc.mbreakout.MBLevels;
import org.vpc.mbreakout.MBScreen;
import org.vpc.mbreakout.MBScreenIds;
import org.vpc.mbreakout.MBScreenSupport;
import org.vpc.mbreakout.MBreakoutMIDlet;
import org.vpc.mbreakout.sprites.balls.BallSprite;
import org.vpc.mbreakout.sprites.bars.BarSprite;
import org.vpc.mbreakout.sprites.bricks.BrickFireSprite;
import org.vpc.mbreakout.sprites.bricks.BrickFlashBallSprite;
import org.vpc.mbreakout.sprites.bricks.BrickGoldSprite;
import org.vpc.mbreakout.sprites.bricks.BrickSprite;
import org.vpc.mbreakout.sprites.missiles.MissileSprite;
import org.vpc.mbreakout.util.RandomTiledBackground;

public class MBGameScreen extends GameCanvas implements Runnable, MBScreen {
	public static final int BRICK_1 = 0;

	public static final int BRICK_2 = 0;

	public static final int BRICK_3 = 0;

	public static final int BRICK_4 = 0;

	public static final int BRICK_5 = 0;

	public static final int BAR_DEFAULT = 0;

	public static final int BAR_SUPERMAN = 2;

	public static final int BAR_FIREMAN = 1;

	public static final int BAR_SPEEDMAN = 3;

	public static final int BAR_FATMAN = 4;

	public static final int BALL_DEFAULT = 0;

	public static final int MISSILE_DEFAULT = 0;

	private MBScreenSupport support;

	private static final int MILLIS_PER_TICK = 30;

	private static final int WIDTH = 128;

	private static final int HEIGHT = 128;

	private static final int TILE_WIDTH = 16;

	private static final int TILE_HEIGHT = 16;

	private static final int WIDTH_IN_TILES = WIDTH / TILE_WIDTH;

	private static final int HEIGHT_IN_TILES = HEIGHT / TILE_HEIGHT;

	// private static final int NUM_TILES = 16;

	private final BarSprite bar;

	private final Vector balls = new Vector();

	private final Vector bricks = new Vector();

	private final Vector missiles = new Vector();

	private final LayerManager layerManager;

	private RandomTiledBackground bg;

	private volatile Thread animationThread = null;

	private int ballType = BALL_DEFAULT;

	private int barType = BAR_DEFAULT;

	private int gameLevel = 1;

	private int score = 0;

	private int life = 3;
	private int initTimeout = 0;

	private boolean gameOver = false;

	public MBGameScreen(MBreakoutMIDlet midlet) {
		super(true);
		support = new MBScreenSupport(midlet, MBScreenIds.GAME);
		bg = new RandomTiledBackground("/org/vpc/mbreakout/images/bg1.png",
				WIDTH_IN_TILES, HEIGHT_IN_TILES, TILE_WIDTH, TILE_HEIGHT);
		bar = createBar(barType);
		bar.setPosition(WIDTH / 2 - bar.getWidth() / 2, HEIGHT - 1
				- bar.getHeight());
		// missileImage = HawkMIDlet.createImage("/missile.png");

		layerManager = new LayerManager();
		layerManager.append(bar);
		layerManager.append(bg.getBackground());

	}

	public synchronized void start() {
		init();
		animationThread = new Thread(this);
		animationThread.start();
	}

	void init() {
		try {
			// reinit level
			for (int i = missiles.size() - 1; i >= 0; --i) {
				Sprite missile = (Sprite) (missiles.elementAt(i));
				layerManager.remove(missile);
			}

			for (int i = balls.size() - 1; i >= 0; --i) {
				Sprite missile = (Sprite) (balls.elementAt(i));
				layerManager.remove(missile);
			}

			for (int i = bricks.size() - 1; i >= 0; --i) {
				Sprite missile = (Sprite) (bricks.elementAt(i));
				layerManager.remove(missile);
			}

			missiles.removeAllElements();
			balls.removeAllElements();
			bricks.removeAllElements();

			System.out.println("creating bricks");
			int[] levelDesc = MBLevels.LEVELS[gameLevel - 1];
			for (int i = 0; i < levelDesc.length; i++) {
				int r = i / MBLevels.ROW;
				int c = i % MBLevels.ROW;
				int brickCode = levelDesc[i];
				if (brickCode > 0) {
					int brickId = (brickCode % 10);
					if (brickId > 0) {
						int xtrans = (brickCode % 100) / 10;
						int ytrans = (brickCode / 100);
						BrickSprite brick = createBrick(brickId);
						int h = brick.getHeight();
						int w = brick.getWidth();
						if (xtrans > 0) {
							xtrans = w / xtrans;
						}
						if (ytrans > 0) {
							ytrans = h / ytrans;
						}
						int y = 12 + r * (h + 1) + ytrans;
						int x = 10 + c * (w + 1) + xtrans;
						brick.setPosition(x, y);
						// System.out.println("brick["+r+","+c+"]="+x+","+y);
						bricks.addElement(brick);
						layerManager.insert(brick, 0);
					}
				}
			}

			System.out.println("creating bar");
			bar.setPosition(WIDTH / 2 - bar.getWidth() / 2, HEIGHT - 1
					- bar.getHeight());

			BallSprite ball = createBall(ballType);
			ball.setAttached(true);
			balls.addElement(ball);
			layerManager.insert(ball, 0);
			gameOver = false;
			initTimeout=100;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		// checkAttachedBall();
	}

	// private void checkAttachedBall() {
	// if (attachedBalls) {
	// for (int i = 0; i < balls.size(); ++i) {
	// Sprite ball = (Sprite) (balls.elementAt(i));
	// ball
	// .setPosition(bar.getX() - ball.getWidth() / 2
	// + bar.getWidth() / 2, bar.getY()
	// - ball.getHeight() - 1);
	// }
	// }
	// }

	public synchronized void stop() {
		animationThread = null;
	}

	public Displayable getDisplayable() {
		return this;
	}

	public int getId() {
		return support.getId();
	}

	public MBreakoutMIDlet getMidlet() {
		return support.getMidlet();
	}

	public void run() {
		try {
			Thread currentThread = Thread.currentThread();

			try {
				// This ends when animationThread is set to null, or when
				// it is subsequently set to a new thread; either way, the
				// current thread should terminate
				while (currentThread == animationThread) {
					long startTime = System.currentTimeMillis();
					// Don't advance game or draw if canvas is covered by
					// a system screen.
					if (isShown()) {
						tick();
						draw();
						flushGraphics();
					}
					long timeTaken = System.currentTimeMillis() - startTime;
					if (timeTaken < MILLIS_PER_TICK) {
						synchronized (this) {
							wait(MILLIS_PER_TICK - timeTaken);
						}
					} else {
						Thread.yield();
					}
				}
			} catch (InterruptedException ex) {
				// won't be thrown
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	void tick() {
//		System.out.println("game.tick.start");
		if (gameOver) {
			int keyStates = getKeyStates();
			if (((keyStates & GameCanvas.FIRE_PRESSED) != 0)) {
				getMidlet().showScreen(MBScreenIds.MENU);
			}
		} else {
			if(initTimeout>0){
				initTimeout--;
			}
			// bg.scrollBackground();
			bar.backupPosition();
			bar.tick();

			for (int i = balls.size() - 1; i >= 0; --i) {
				BallSprite ball = (BallSprite) (balls.elementAt(i));
				ball.backupPosition();
				ball.tick();
			}
			for (int i = bricks.size() - 1; i >= 0; --i) {
				BrickSprite brick = (BrickSprite) (bricks.elementAt(i));
				brick.backupPosition();
				brick.tick();
			}
			// handle missiles
			for (int i = missiles.size() - 1; i >= 0; --i) {
				MissileSprite miss = (MissileSprite) (missiles.elementAt(i));
				miss.backupPosition();
				miss.tick();
			}
			// handle collisions
			for (int i = balls.size() - 1; i >= 0; --i) {
				BallSprite ball = (BallSprite) (balls.elementAt(i));
				if (!ball.isDead()) {
					ball.checkCollision(bar);
					for (int j = bricks.size()-1; j >= 0; --j) {
						BrickSprite brick = (BrickSprite) (bricks.elementAt(j));
						if (!brick.isDead() && !brick.isFalling()) {
							ball.checkCollision(brick);
						}
					}
				}
			}

			for (int i = bricks.size() - 1; i >= 0; --i) {
				BrickSprite brick = (BrickSprite) (bricks.elementAt(i));
				if (!brick.isDead() && brick.isFalling()) {
					brick.checkCollision(bar);
				}
			}
			
			for (int i = missiles.size() - 1; i >= 0; --i) {
				MissileSprite missile = (MissileSprite) (missiles.elementAt(i));
				if (!missile.isDead()) {
					for (int j = bricks.size() - 1; j >= 0; --j) {
						BrickSprite brick = (BrickSprite) (bricks.elementAt(j));
						if (!brick.isDead() && !brick.isFalling()) {
							missile.checkCollision(brick);
						}
					}
				}
			}

			// handle death
			for (int i = balls.size() - 1; i >= 0; --i) {
				BallSprite ball = (BallSprite) (balls.elementAt(i));
				if (ball.isDead()) {
					balls.removeElementAt(i);
					layerManager.remove(ball);
				}
			}

			boolean remainingBricks=false;
			for (int i = bricks.size() - 1; i >= 0; --i) {
				BrickSprite brick = (BrickSprite) (bricks.elementAt(i));
				if (brick.isDead()) {
					bricks.removeElementAt(i);
					layerManager.remove(brick);
				}else if(!remainingBricks && !brick.isStone()){
					remainingBricks=true;
				}
			}
			
			if (balls.size() == 0) {
				life--;
				if (life == 0) {
					gameOver = true;
				} else {
					BallSprite ball = createBall(ballType);
					ball.setAttached(true);
					balls.addElement(ball);
					layerManager.insert(ball, 0);
//					for (int i = balls.size() - 1; i >= 0; --i) {
//						BallSprite ball = (BallSprite) (balls.elementAt(i));
//						ball.setAttached(true);
//					}
				}
			}
			if(!remainingBricks){
				gameLevel++;
				if(gameLevel>=MBLevels.LEVELS.length){
					gameOver=true;
				}else{
					init();
				}
			}
		}
//		System.out.println("game.tick.end");
	}

	// draw game
	private void draw() {
		int width = getWidth();
		int height = getHeight();
		Graphics g = getGraphics();

		// clear screen to grey
		g.setColor(0x00888888);
		g.fillRect(0, 0, width, height);

		// clip and translate to centre
		int dx = (width - WIDTH) / 2;
		int dy = (height - HEIGHT) / 2;
		g.setClip(dx, dy, WIDTH, HEIGHT);
		g.translate(dx, dy);

		// draw background and sprites
		layerManager.paint(g, 0, 0);
		g.setColor(0x00888888);
		g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_SMALL));
		g.drawString("Score :" + score + " - Life : " + life, 2, 0, 0);
		g.setColor(0);
		g.drawString("Score :" + score + " - Life : " + life, 3, 1, 0);
		if (gameOver) {
			g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_LARGE));
			g.setColor(0x00FF0000);
			g.drawString("GAME OVER", 25, 50, 0);
			g.setColor(0x00FFFF00);
			g.drawString("GAME OVER", 27, 52, 0);
		}
		if(initTimeout>0){
			g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_LARGE));
			g.setColor(0x00FF0000);
			g.drawString("LEVEL "+gameLevel, 35, 50, 0);
			g.setColor(0x00FFFF00);
			g.drawString("LEVEL "+gameLevel, 37, 52, 0);
		}
	}

	public BarSprite createBar(int id) {
		switch (id) {
		default: {
			return new BarSprite(this);
		}
		}
	}

	public BrickSprite createBrick(int id) {
		switch (id) {
		case 1: {
			return new BrickSprite(this, id, 1);
		}
		case 2: {
			return new BrickFireSprite(this);
		}
		case 3: {
			return new BrickSprite(this, id, 1);
		}
		case 4: {
			return new BrickFlashBallSprite(this);
		}
		case 5: {
			return new BrickGoldSprite(this);
		}
		case 0: {
			return new BrickSprite(this, id, 1);
		}
		default: {
			return new BrickSprite(this, id, 1);
		}
		}
	}

	public BallSprite createBall(int id) {
		switch (id) {
		default: {
			return new BallSprite(this, 0);
		}
		}
	}

	public void addScore(int score) {
		this.score += score;
	}

	public int getGameWidth() {
		return WIDTH;
	}

	public int getGameHeight() {
		return HEIGHT;
	}

	public void fireMissile(MissileSprite missile) {
		missile.setPosition(bar.getX() - missile.getWidth() / 2
				+ bar.getWidth() / 2, bar.getY());
		missiles.addElement(missile);
		layerManager.insert(missile, 1);
		missile.missileLaunched();
	}

	public BarSprite getBar() {
		return bar;
	}

	public Vector getBalls() {
		return balls;
	}

}
