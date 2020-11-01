package tn.rnu.enit.ateliercorba.jcanon.model;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 13 oct. 2007 19:59:05
 */
public class PlaneBombSprite extends CanonGameSprite {
    public PlaneBombSprite(int x,int y,int mood) {
        setMood(mood);
        setWidth(16);
        setHeight(16);
        setX(x);
        setY(y);
    }


    public void collideWith(Sprite other, Side side) {
        setDead(true);
    }

    int dx=0;
    int dy=-1;
    public void tic() {
        boolean changeOrientation=Math.random()*10<1;
        int xspeed=3;
        int yspeed=20;
        if(dy<0){
            dy=(int)(Math.random()*yspeed)+4;
        }
        if(changeOrientation){
            dx=(int)(Math.random()*2*xspeed)-xspeed;
            if(getX()+dx<0 || getX()+dx>getGame().getWidth()){
                dx=-dx;
            }
            if(getY()+dy<0 || getY()+dy>getGame().getHeight()){
                dy=-dy;
            }
        }
        setX(getX()+dx);
        setY(getY()+dy);
        int lastMood=getMood();
        setMood((lastMood+1)%3);
        if((getY()+getWidth())>=getGame().getHeight()){
            setDead(true);
        }
        revalidatePosition();
    }


}
