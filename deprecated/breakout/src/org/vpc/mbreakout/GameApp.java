package org.vpc.mbreakout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 10:35:33
 */
public abstract class GameApp{
    int width;
    int heigth;
    GameScreen currentScreen;
    String title;
    JFrame frame;


    protected GameApp(int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
    }

    public void runApplication(){
        frame=new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startApp();
        frame.setPreferredSize(new Dimension(width,heigth));
        frame.setVisible(true);
    }

    protected abstract void startApp() ;

    protected abstract void destroyApp(boolean arg0) ;

    public abstract void quit();

    protected abstract void pauseApp();


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public void showScreen(GameScreen screen){
        screen.showScreen();
    }

    public void hideScreen(GameScreen screen){
        
        screen.hideScreen();
    }


    public GameScreen getCurrentScreen() {
        return currentScreen;
    }
}
