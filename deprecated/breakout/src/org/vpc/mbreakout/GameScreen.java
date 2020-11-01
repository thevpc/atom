package org.vpc.mbreakout;

import java.awt.event.KeyEvent;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 10:41:06
 */
public abstract class GameScreen {
    private GameApp app;
    public void setAppGame(GameApp app){
        this.app=app;
    }
    abstract void getAppGame(GameApp app);
    abstract void keyPressed(KeyEvent event);
    abstract void gamePaused();
    abstract void gameResumed();
    abstract void gameQuit();
    abstract void close();
    abstract void hideScreen();
    abstract void showScreen();
}
