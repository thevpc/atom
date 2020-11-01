/**
 * 
 */
package org.vpc.mbreakout;

import java.util.Stack;
import java.util.Properties;

import org.vpc.mbreakout.lang.MBLangFr;
import org.vpc.mbreakout.screens.*;


/**
 * @author vpc
 * 
 */
public class MBreakoutMIDlet extends GameApp implements Runnable {
	private int currentScreen = MBScreenIds.UNKNOWN;

	private MBMenuScreen menuScreen;

	private MBInstructionsScreen instructionsScreen;

	private MBSplashScreen splashScreen;

	private MBGameScreen gameScreen;

	private Properties messages;

	private boolean gameActive;

	private Stack screensStack;

	public MBreakoutMIDlet() {
	}

	protected void startApp() throws MIDletStateChangeException {

		messages = new MBLangFr();
		screensStack = new Stack();
		showScreen(MBScreenIds.SPLASH);
	}

	protected void destroyApp(boolean arg0) {
		// TODO Auto-generated method stub

	}

	public void quit() {
		destroyApp(true);
		notifyDestroyed();
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	public void run() {
		// TODO Auto-generated method stub

	}

	public void hideScreen() {
		screensStack.pop();
		showScreen((MBScreen) screensStack.peek());
	}

	public void showScreen(MBScreen screen) {
		currentScreen = screen.getId();
		Display.getDisplay(this).setCurrent(screen.getDisplayable());
	}

	public void showScreen(int screen) {
		MBScreen s = getScreen(screen);
		showScreen(s);
	}

	public MBScreen getScreen(int screen) {
		switch (screen) {
		case MBScreenIds.MENU: {
			if (menuScreen == null) {
				menuScreen = new MBMenuScreen(this);
			}
			return menuScreen;
		}
		case MBScreenIds.INSTRUCTIONS: {
			if (instructionsScreen == null) {
				instructionsScreen = new MBInstructionsScreen(this);
			}
			return instructionsScreen;
		}
		case MBScreenIds.SPLASH: {
			if (splashScreen == null) {
				splashScreen = new MBSplashScreen(this);
			}
			return splashScreen;
		}
		case MBScreenIds.GAME: {
			if (gameScreen == null) {
				gameScreen = new MBGameScreen(this);
			}
			return gameScreen;
		}
		}
		return null;
	}

	public String getMessage(String id) {
		String s = messages.getProperty(id);
		if (s == null) {
			return "<" + id + "!>";
		}
		return s;
	}

	public boolean isGameActive() {
		return gameActive;
	}

	public void setGameActive(boolean gameActive) {
		if (gameActive != this.gameActive) {
			this.gameActive = gameActive;
			if (menuScreen != null) {
				menuScreen.gameActiveChanged(gameActive);
			}
		}
	}
	public int getCurrentScreen() {
		return currentScreen;
	}

}
