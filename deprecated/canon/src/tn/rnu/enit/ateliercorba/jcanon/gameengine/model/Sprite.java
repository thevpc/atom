package tn.rnu.enit.ateliercorba.jcanon.gameengine.model;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 10:50:17
 */
public interface Sprite {
    public enum Side{
        NORTH,SOUTH,WEST,EAST
    }

    GameModel getGame();

    void setGame(GameModel gameModel);

    public void setX(int x);

    public void setY(int y);

    public int getX();

    public int getY();

    public int getWidth();

    public void setWidth(int width);

    public int getHeight();

    public void setHeight(int height);

    public boolean isCollideWith(Sprite other);

    public void collideWith(Sprite other,Side side);

    public boolean isDead();

    public void tic();

    int getMood();

    void setMood(int mood);
}
