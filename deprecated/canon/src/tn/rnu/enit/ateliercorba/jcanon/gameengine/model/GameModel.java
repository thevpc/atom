package tn.rnu.enit.ateliercorba.jcanon.gameengine.model;

import java.util.Hashtable;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 10 dec. 2006 22:55:41
 */
public class GameModel {
    private int heigth;
    private int width;

    public GameModel(int width,int heigth) {
        this.heigth = heigth;
        this.width = width;
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return heigth;
    }
}
