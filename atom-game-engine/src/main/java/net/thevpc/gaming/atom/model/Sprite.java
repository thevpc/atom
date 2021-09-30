package net.thevpc.gaming.atom.model;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;
import net.thevpc.gaming.atom.presentation.layers.Layer;

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

    SpriteMainTask getMainTask();

    /**
     * collisiontasks sides are updated by atom whenever collisiontasks is detected for
     * this sprite
     *
     * @return
     */
    CollisionSides getCollisionSides();

    Sprite setCollisionSides(CollisionSides sides);

    int getLayer();

    public Sprite setLayer(int layer);

    SpriteArmor[] getArmors();

    Sprite setArmors(SpriteArmor... armors);

    SpriteWeapon[] getWeapons();

    Sprite setWeapons(SpriteWeapon... weapons);

    int getLife();

    Sprite setLife(int life);

    int getMaxLife();

    Sprite setMaxLife(int maxLife);

    public int getStyle();

    public Sprite setStyle(int variation);

    SceneEngine getSceneEngine();

    Sprite setSceneEngine(SceneEngine sceneEngine);

    Sprite setMainTask(SpriteMainTask task);

    SpriteCollisionTask getCollisionTask();

    Sprite setCollisionTask(SpriteCollisionTask collisionManager);

    public boolean isAt(ModelPoint point);

    public boolean isAt(ModelBox rect);

    ModelPoint getLocation();

    ModelPoint getPreviousLocation();

    Sprite setPreviousLocation(ModelPoint point);

    public Sprite setLocation(ModelPoint point);

    public Sprite setLocation(double x, double y);

    public boolean isMoving();

    public Sprite setMoving(boolean moving);

    public ModelDimension getSize();

    public Sprite setSize(ModelDimension dimension);

    public Sprite setSize(double width, double height);

    public ModelBox getBounds();

    public ModelBox getPreviousBounds();

    public String getName();

    public Sprite setName(String name);

    public String getKind();

    public Sprite setKind(String type);

    public boolean isSelectable();

    public boolean isAttackable();

    public boolean isOwnedByPlayer(int playerId);

    public int getPlayerId();

    public Sprite setPlayerId(int player);

    public int getId();

    public Sprite setId(int id);

    public double getHeight();

    public double getWidth();

    public double getAltitude();

    public double getX();

    public double getY();

    public boolean isDead();

    public boolean isCrossable();

    public Sprite setCrossable(boolean crossable);

    Sprite addPropertyChangeListener(PropertyChangeListener listener);

    Sprite addPropertyChangeListener(String property, PropertyChangeListener listener);

    Sprite removePropertyChangeListener(PropertyChangeListener listener);

    Sprite removePropertyChangeListener(String property, PropertyChangeListener listener);

    public double getDirection();

    public Sprite setDirection(double value);

    public Sprite setDirection(Direction value);

    public double getSpeed();

    public Sprite setSpeed(double speed);

    public double getAngularSpeed();

    public Sprite setAngularSpeed(double speed);

    /**
     * setLife(0)
     */
    public Sprite die();

    /**
     * setLife(getLife()+x)
     *
     * @param x
     */
    public Sprite addLife(int x);

    public ModelPoint validatePosition(ModelPoint position);

    public int getSight();

    /**
     * Sight must be positive
     *
     * @param sight
     * @throws IllegalArgumentException raised when <code>sight<code> is negative
     */
    public Sprite setSight(int sight);

    public double distance(Sprite b);

    public double distance(ModelBox b);

    public double distance(ModelPoint b);

    public Map<String, Object> getProperties();

    public Object getProperty(String name);

    /**
     * Adds or Removes custom properties. If <code>value</code> is not null, the
     * property is added If <code>value</code> is null, the property is removed
     * Names starting with $ are not supported. Fires a property change event
     * with key $<code>name</code> (name prefixed with a leading $) and the
     * value setMap.
     *
     * @param name custom property name
     * @param value custom property value or null
     * @throws NullPointerException raised when <code>name</name> is null
     * @throws IllegalArgumentException raised when name starts with a leading
     * '$' character. Leading '$' are not supported because property change
     * events will add '$' to custom properties.
     */
    public Sprite setProperty(String name, Object value);

    public ModelVector getVelocity();

    public Sprite setVelocity(ModelVector velocityVector);

    Sprite copy();

    public Sprite copyFrom(Sprite sprite);

    public int getMovementStyle();

    public Sprite setMovementStyle(int movementStyle);

    ModelDimension getPreviousSize();

    Sprite setPreviousSize(ModelDimension size);
}
