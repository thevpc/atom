package tn.rnu.enit.ateliercorba.jcanon.model;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 10 dec. 2006 23:23:35
 */
public class PlaneSpriteTwo extends PlaneSprite {

    public PlaneSpriteTwo(Status status,int x,int y,int mood) {
        super(status,x,y,mood);
        setX(x);
        setY(y);
    }
    int doFireRatio=10000;
    public void tic() {
        super.tic();
        boolean doFire=Math.random()*(doFireRatio/100)<1;
        doFireRatio--;
        if(doFire){
            PlaneBombSprite f=new PlaneBombSprite(getX()+getWidth()/2,getY()+getHeight()+2,0);
            getGame().addSprite(f);
        }
    }
}
