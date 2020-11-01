package tn.rnu.enit.ateliercorba.jcanon.model;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.AbstractSprite;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 10 dec. 2006 23:22:10
 */
public abstract class PlaneSprite extends CanonGameSprite {
    public static enum Status{
        ALIVE,FALLING
    }
    private Status status;
    public PlaneSprite(Status status,int x,int y,int mood) {
        this.status=status;
        setMood(mood);
        setWidth(128);
        setHeight(64);
        setX(x);
        setY(y);
    }


    public void collideWith(Sprite other, Side side) {
        if(other instanceof FireSprite){
            if(status==Status.ALIVE){
                status=Status.FALLING;
            }
        }else{
            setDead(true);
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    int dx=0;
    int dy=0;
    public void tic() {
        boolean changeOrientation=Math.random()*10<1;
        int speed=(status==Status.ALIVE)?5:20;
        if(changeOrientation){
            dx=(int)(Math.random()*2*speed)-speed;
            if(status==Status.ALIVE){
                dy=(int)(Math.random()*2*speed)-speed;
            }else{
                dy=(int)(Math.random()*speed)+1;
            }
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
        setMood((lastMood+1)%4);
        if((getY()+getWidth())>=getGame().getHeight()){
            setDead(true);
        }
        revalidatePosition();
    }


}
