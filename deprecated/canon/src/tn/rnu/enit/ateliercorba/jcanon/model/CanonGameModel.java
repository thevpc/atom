package tn.rnu.enit.ateliercorba.jcanon.model;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.GameModel;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 11:29:56
 */
public class CanonGameModel extends GameModel {
    private ArrayList<PlayerSprite> canons = new ArrayList<PlayerSprite>();
    private ArrayList<FireSprite> fires = new ArrayList<FireSprite>();
    private ArrayList<PlaneSprite> planes = new ArrayList<PlaneSprite>();
    private ArrayList<PlaneBombSprite> bombs = new ArrayList<PlaneBombSprite>();
    private ArrayList<Sprite> all = new ArrayList<Sprite>();
    public enum GameStatus{
        GAME_INIT, GAME_STARTED,GAME_OVER}
    private GameStatus status=GameStatus.GAME_INIT;
    public CanonGameModel() {
        super(1024, 800);
    }

    public void addDefaultSprites() {
        reset();
        addSprite(new PlayerSprite(1, 3, 0, 300, getHeight() - 200, 0));
        addSprite(new PlayerSprite(2, 3, 0, getWidth() - 300, getHeight() - 200, 0));
        int xcount = 10;
        int xstep = getWidth() / (xcount + 1);
        int ycount = 2;
        int ystep = 200;
        for (int i = 0; i < xcount; i++) {
            for (int j = 0; j < ycount; j++) {
                if (Math.random() < 0.5) {
                    addSprite(new PlaneSpriteTwo(PlaneSprite.Status.ALIVE, 50 + xstep * i, 50 + 50 * j, 0));
                } else {
                    addSprite(new PlaneSpriteOne(PlaneSprite.Status.ALIVE, 50 + xstep * i, 50 + ystep * j, 0));
                }
            }
        }
    }

    public void reset() {
        canons.clear();
        fires.clear();
        planes.clear();
        bombs.clear();
    }

    public void removeSprite(Sprite sprite) {
        all.remove(sprite);
        canons.remove(sprite);
        fires.remove(sprite);
        planes.remove(sprite);
        bombs.remove(sprite);
    }

    public void addSprite(Sprite sprite) {
        sprite.setGame(this);
        all.add(sprite);
        if (sprite instanceof PlayerSprite) {
            canons.add((PlayerSprite) sprite);
        }
        if (sprite instanceof FireSprite) {
            fires.add((FireSprite) sprite);
        }
        if (sprite instanceof PlaneSprite) {
            planes.add((PlaneSprite) sprite);
        }
        if (sprite instanceof PlaneBombSprite) {
            bombs.add((PlaneBombSprite) sprite);
        }
    }

    public PlayerSprite[] getCanons() {
        return canons.toArray(new PlayerSprite[canons.size()]);
    }

    public FireSprite[] getFires() {
        return fires.toArray(new FireSprite[fires.size()]);
    }

    public PlaneSprite[] getPlanes() {
        return planes.toArray(new PlaneSprite[planes.size()]);
    }

    public PlaneBombSprite[] getBombs() {
        return bombs.toArray(new PlaneBombSprite[bombs.size()]);
    }

    public synchronized void checkCollision() {
        checkCollision(fires, planes);
        checkCollision(fires, bombs);
        checkCollision(canons, planes);
        checkCollision(canons, bombs);
    }

    protected synchronized void checkCollision(Collection<? extends Sprite> first, Collection<? extends Sprite> second) {
        first=new ArrayList<Sprite>(first);   // pour eviter ConcurrentModificationException
        second=new ArrayList<Sprite>(second); // pour eviter ConcurrentModificationException
        for (Iterator<? extends Sprite> i = first.iterator(); i.hasNext();) {
            Sprite a = i.next();
            for (Iterator<? extends Sprite> j = second.iterator(); j.hasNext();) {
                Sprite b = j.next();
                if (b.isCollideWith(a)) {
                    System.out.println("collision " + a + "/" + b);
                    a.collideWith(b, Sprite.Side.NORTH);
                    b.collideWith(a, Sprite.Side.SOUTH);
                }
                if (b.isDead() && !(b instanceof PlayerSprite)) {
                    j.remove();
                }
                if (a.isDead() && !(b instanceof PlayerSprite)) {
                    i.remove();
                    break;
                }
            }
        }
    }

    public PlayerSprite getPlayer(int player) {
        for (PlayerSprite canon : getCanons()) {
            if (canon.getPlayer() == player) {
                return canon;
            }
        }
        return null;
    }


    public void tic() {
        if (status.equals(GameStatus.GAME_STARTED)) {
            for (Sprite s : new ArrayList<Sprite>(all)) {
                s.tic();
                if(s.isDead()){
                    removeSprite(s);
                }
            }
            checkCollision();
            if(canons.size()==1){
                setStatus(GameStatus.GAME_OVER);
            }
            for (PlayerSprite canon : canons) {
                if(canon.isDead()){
                    setStatus(GameStatus.GAME_OVER);
                }
            }

        }
    }

    public void playerHitFire(int player) {
        PlayerSprite p = getPlayer(player);
        if (p != null && !p.isDead()) {
            p.hitFire();
        }
    }

    public void playerMoveLeft(int player) {
        PlayerSprite playerSprite = getPlayer(player);
        if (playerSprite != null && !playerSprite.isDead()) {
            playerSprite.moveLeft();
        }
    }

    public void playerMoveRight(int player) {
        PlayerSprite playerSprite = getPlayer(player);
        if (playerSprite != null && !playerSprite.isDead()) {
            playerSprite.moveRight();
        }
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
}
