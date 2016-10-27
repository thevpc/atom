package net.vpc.gaming.atom.model;

import net.vpc.gaming.atom.presentation.layers.Layer;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Map;

/**
 * A Sprite is a two-dimensional animation that is integrated into a larger
 * scene Sprite class represents the Model Concept of a sprite in MVC design
 * Pattern.
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 5 dec. 2006 10:50:17
 */
public interface Sprite extends Serializable {

    public static final int SPRITES_LAYER = Layer.SPRITES_LAYER;
    public static final int OUT_OF_RANGE = -1;

    int getLayer();

    public void setLayer(int layer) ;

    SpriteArmor[] getArmors();

    void setArmors(SpriteArmor... armors);

    SpriteWeapon[] getWeapons();

    void setWeapons(SpriteWeapon... weapons);

    int getLife();

    void setLife(int life);

    int getMaxLife();

    void setMaxLife(int maxLife);

    public int getStyle();

    public void setStyle(int variation);

    SceneEngineModel getSceneEngineModel();

    void setSceneEngineModel(SceneEngineModel sceneModel);

    public boolean isAt(ModelPoint point);

    public boolean isAt(ModelBox rect);

    public ModelPoint getLocation();

    public void setLocation(int x,int y);

    public void setLocation(ModelPoint point);

    public boolean isMoving();

    public void setMoving(boolean moving);

    public ModelDimension getSize();

    public void setSize(int width,int height);

    public void setSize(ModelDimension dimension);

    public ModelBox getBounds();

    public String getName();

    public void setName(String name);

    public String getKind();

    public void setKind(String type);

    //    public void setSelectable(boolean value);
    public boolean isSelectable();

    public boolean isAttackable();

    public boolean isOwnedByPlayer(int playerId);

    public int getPlayerId();

    public void setPlayerId(int player);

    public int getId();

    public void setId(int id);

    public double getHeight();

    public double getWidth();

    public double getAltitude();

    public double getX();

    public double getY();

    public boolean isDead();

    public boolean isCrossable();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);

    public double getDirection();

    public void setDirection(double value);

    public void setDirection(Direction value);

    public double getSpeed();

    public void setSpeed(double speed);

    public void setCrossable(boolean crossable);

    public double getAngularSpeed();

    public void setAngularSpeed(double speed);

    /**
     * setLife(0)
     */
    public void die();

    /**
     * setLife(getLife()+x)
     *
     * @param x
     */
    public void addLife(int x);

    public ModelPoint validatePosition(ModelPoint position);

    public int getSight();

    /**
     * Sight must be positive
     *
     * @param sight
     * @throws IllegalArgumentException raised when <code>sight<code> is negative
     */
    public void setSight(int sight);

    public double distance(Sprite b);

    public double distance(ModelBox b);

    public double distance(ModelPoint b);

    public Map<String, Object> getProperties();

    public Object getProperty(String name);

    /**
     * Adds or Removes custom properties. If
     * <code>value</code> is not null, the property is added If
     * <code>value</code> is null, the property is removed Names starting with $
     * are not supported. Fires a property change event with key
     * $<code>name</code> (name prefixed with a leading $) and the value setMap.
     *
     * @param name  custom property name
     * @param value custom property value or null
     * @throws NullPointerException     raised when <code>name</name> is null
     * @throws IllegalArgumentException raised when name starts with a leading
     *                                  '$' character. Leading '$' are not supported because property change
     *                                  events will add '$' to custom properties.
     */
    public void setProperty(String name, Object value);

    public ModelVector getVelocity();

    public void setVelocity(ModelVector velocityVector);

    public void copyFrom(Sprite sprite);
}
