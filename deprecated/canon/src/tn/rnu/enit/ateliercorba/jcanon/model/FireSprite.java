package tn.rnu.enit.ateliercorba.jcanon.model;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.AbstractSprite;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 10 dec. 2006 23:20:44
 */
public class FireSprite extends CanonGameSprite {
    private int player;
    public FireSprite(int player,int x,int y,int mood) {
        this.player=player;
//        setWidth(2);
//        setHeight(6);
        setWidth(16);
        setHeight(16);
        setX(x);
        setY(y);
        setMood(mood);
    }


    public void collideWith(Sprite other, Side side) {
        if(other instanceof PlaneSprite){
            getGame().getPlayer(player).addScore(2);
        }
        setDead(true);
    }

    public int getPlayer() {
        return player;
    }


    public void tic() {
        setY(getY()-20);
        if(getY()<0){
            setDead(true);
        }
        revalidatePosition();
    }


}
