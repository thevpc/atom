package tn.rnu.enit.ateliercorba.jcanon.model;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.AbstractSprite;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 10 dec. 2006 23:19:21
 */
public class PlayerSprite extends CanonGameSprite {
    private int player;
    private int life;
    private int score;
    private int direction=0;
    private int vitesse=5;
    public PlayerSprite(int player,int life,int score,int x,int y,int mood) {
        this.player=player;
        this.life=life;
        this.score=score;
        setWidth(128);
        setHeight(96);
        setX(x);
        setY(y);
        setMood(mood);
    }


    public void collideWith(Sprite other, Side side) {
        if(other instanceof PlaneSprite){
            if(((PlaneSprite)other).getStatus()== PlaneSprite.Status.FALLING){
                score+=10;
            }else{
                setLife(getLife()-1);
            }
        }else if(other instanceof PlaneBombSprite){
            setLife(getLife()-1);
        }
        super.collideWith(other, side);
    }

    public int getPlayer() {
        return player;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
        if(this.life<0){
            setDead(true);
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int pts){
        this.score+=pts;
    }

    public void tic() {
        super.tic();
        if(direction>0){
            setX(getX()+10);
        }else if(direction<0){
            setX(getX()-10);
        }
        revalidatePosition();
    }

    public void moveLeft(){
        setX(getX()-vitesse);
        setDirection(-1);
        revalidatePosition();
    }

    public void hitFire(){
        FireSprite f=new FireSprite(player,0,getY()-2,0);
        f.setX(getX()+(getWidth()-f.getWidth())/2);
        getGame().addSprite(f);
    }

    public void moveRight(){
        setX(getX()+vitesse);
        setDirection(1);
        revalidatePosition();
    }

    private void setDirection(int dir){
        direction=dir;
        setMood(direction==0?0:direction>0?1:2);
    }
}
