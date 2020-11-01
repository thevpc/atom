package tn.rnu.enit.ateliercorba.jcanon.gameengine.model;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 10:54:52
 */
public class AbstractSprite implements Sprite {
    private int x;
    private int y;
    private int mood;
    private int width;
    private int height;
    private boolean dead;
    private GameModel gameModel;

    public AbstractSprite() {
    }


    public boolean isCollideWith(Sprite other) {
        int x0 = other.getX();
        int y0 = other.getY();
        int w0 = other.getWidth();
        int h0 = other.getHeight();
        int w1 = width;
        int h1 = height;
        int x1 = this.x;
        int y1 = this.y;
        return     ((x0 >= x1 && x0 <= (x1 + w1)) && (y0 >= y1 && y0 <= (y1 + h1)))
                || ((x0 + w0 >= x1 && x0 + w0 <= (x1 + w1)) && (y0 >= y1 && y0 <= (y1 + h1)))
                || ((x0 >= x1 && x0 <= (x1 + w1)) && (y0 + h0 >= y1 && y0 + h0 <= (y1 + h1)))
                || ((x0 + w0 >= x1 && x0 + w0 <= (x1 + w1)) && (y0 >= y1 && y0 + h0 <= (y1 + h1)))
                || ((x1 >= x0 && x1 <= (x0 + w0)) && (y1 >= y0 && y1 <= (y0 + h0)))
                || ((x1 + w1 >= x0 && x1 + w1 <= (x0 + w0)) && (y1 >= y0 && y1 <= (y0 + h0)))
                || ((x1 >= x0 && x1 <= (x0 + w0)) && (y1 + h1 >= y0 && y1 + h1 <= (y0 + h0)))
                || ((x1 + w1 >= x0 && x1 + w1 <= (x0 + w0)) && (y1 >= y0 && y1 + h1 <= (y0 + h0)))
        ;
    }

    public void collideWith(Sprite other, Side side) {
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void tic() {

    }

    public GameModel getGame() {
        return gameModel;
    }

    public void setGame(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void revalidatePosition() {
        if (getX() < 0) {
            setX(0);
        }
        if (getX() + getWidth() > getGame().getWidth()) {
            setX(getGame().getWidth() - getWidth());
        }
        if (getY() < 0) {
            setY(0);
        }
        if (getY() + getHeight() > getGame().getHeight()) {
            setY(getGame().getHeight() - getHeight());
        }

    }


    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }
}
