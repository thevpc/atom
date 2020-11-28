package net.thevpc.gaming.atom.model;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 5 dec. 2006 10:54:52
 */
public class DefaultSprite implements Sprite {

    protected transient PropertyChangeSupport propertyChangeSupport;
    private int id;
    private int layer = SPRITES_LAYER;
    private ModelBox bounds = new ModelBox(0, 0, 0, 0, 0, 0);
    private ModelPoint location = ModelPoint.ORIGIN;
    private boolean moving = false;
    private ModelPoint locationPrecision = new ModelPoint(0.0001, 0.0001, 0.0001);
    private int z;
    private boolean selectable = true;
    private int playerId;
    private int collisionSides;
    private double direction = 0;
    private ModelDimension size = new ModelDimension(0, 0, 0);
    private boolean crossable = false;
    private boolean attackable = true;
    private String name = "Unknown";
    private String kind = "Sprite";
    private double speed = 0.125 / 4;
    private double angularSpeed = 0;
    private int life = 1;
    private int maxLife = 1;
    private SpriteArmor[] armors = new SpriteArmor[0];
    private SpriteWeapon[] weapons = new SpriteWeapon[0];
    private int sight = 1;
    private int style = 0;
    private int movementStyle = MovementStyles.STOPPED;
    private HashMap<String, Object> properties;
    private transient SceneEngine sceneEngine;
    private transient SpriteMainTask task;
    private transient SpriteCollisionTask collisionManager;

    public DefaultSprite() {
        this(null);
    }

    public DefaultSprite(String kind) {
        this(kind, 1.0, 1.0, 1.0);
    }

    public DefaultSprite(double width, double height, double altitude) {
        this(null, width, height, altitude);
    }

    public DefaultSprite(String kind, double width, double height, double altitude) {
        this.kind = (kind == null) ? getClass().getSimpleName() : kind;
        this.size = new ModelDimension(width, height, altitude);
    }

    private PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    private double revalidatePrecision(double value, double precision) {
        if (Math.abs(value) <= precision) {
            return 0;
        }
        return Math.round(value / precision) * precision;
    }

    public Sprite setSelectable(boolean value) {
        boolean old = this.selectable;
        this.selectable = value;
        getPropertyChangeSupport().firePropertyChange("selectable", old, selectable);
        return this;
    }

    @Override
    public SpriteMainTask getMainTask() {
        if (sceneEngine == null) {
            return task;
        } else {
            return getSceneEngine().getSpriteMainTask(this);
        }
    }

    private ModelBox rebuildBounds() {
        ModelBox old = bounds;
        ModelBox value = new ModelBox(location, size);
        bounds = value;
        getPropertyChangeSupport().firePropertyChange("bounds", old, bounds);
        return old;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "id=" + id + ", name=" + name + ", " + bounds + '}';
    }

    public Sprite setAttackable(boolean attackable) {
        boolean old = this.attackable;
        this.attackable = attackable;
        getPropertyChangeSupport().firePropertyChange("attackable", old, attackable);
        return this;
    }

    @Override
    public int getCollisionSides() {
        return collisionSides;
    }

    @Override
    public Sprite setCollisionSides(int collisionSides) {
        int old = this.collisionSides;
        this.collisionSides = collisionSides;
        getPropertyChangeSupport().firePropertyChange("collisionSides", old, collisionSides);
        return this;
    }

    public int getLayer() {
        return layer;
    }

    public Sprite setLayer(int layer) {
        int old = this.layer;
        this.layer = layer;
        getPropertyChangeSupport().firePropertyChange("layer", old, layer);
        return this;
    }

    public SpriteArmor[] getArmors() {
        return armors;
    }

    public Sprite setArmors(SpriteArmor... armors) {
        SpriteArmor[] old = this.armors;
        this.armors = armors;
        getPropertyChangeSupport().firePropertyChange("armors", old, armors);
        return this;
    }

    public SpriteWeapon[] getWeapons() {
        return weapons;
    }

    public Sprite setWeapons(SpriteWeapon... weapons) {
        SpriteWeapon[] old = this.weapons;
        this.weapons = weapons;
        getPropertyChangeSupport().firePropertyChange("weapons", old, weapons);
        return this;
    }

    public int getLife() {
        return life;
    }

    public Sprite setLife(int life) {
        if (life <= 0) {
            life = 0;
        }
        int old = this.life;
        this.life = life;
        getPropertyChangeSupport().firePropertyChange("life", old, life);
        return this;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public Sprite setMaxLife(int maxLife) {
        int old = this.maxLife;
        this.maxLife = maxLife;
        getPropertyChangeSupport().firePropertyChange("maxLife", old, maxLife);
        return this;
    }

    public int getStyle() {
        return style;
    }

    public Sprite setStyle(int style) {
        int old = this.style;
        this.style = style;
        getPropertyChangeSupport().firePropertyChange("style", old, style);
        return this;
    }

    public SceneEngine getSceneEngine() {
        return sceneEngine;
    }

    public Sprite setSceneEngine(SceneEngine model) {
        SceneEngine old = this.sceneEngine;
        this.sceneEngine = model;
        if (this.sceneEngine != null) {
            this.task = null;
            this.collisionManager = null;
        }
        getPropertyChangeSupport().firePropertyChange("model", old == null ? null : old.getModel(), model == null ? null : model.getModel());
        return this;
    }

    public Sprite setMainTask(SpriteMainTask task) {
        if (sceneEngine == null) {
            this.task = task;
        } else {
            getSceneEngine().setSpriteMainTask(this, task);
        }
        return this;
    }

    @Override
    public SpriteCollisionTask getCollisionTask() {
        if (sceneEngine == null) {
            return collisionManager;
        } else {
            return getSceneEngine().getSpriteCollisionTask(this);
        }
    }

    public Sprite setCollisionTask(SpriteCollisionTask collisionManager) {
        if (sceneEngine == null) {
            this.collisionManager = collisionManager;
        } else {
            getSceneEngine().setSpriteCollisionTask(this, collisionManager);
        }
        return this;
    }

    public boolean isAt(ModelPoint point) {
        return getBounds().contains(point);
    }

    public boolean isAt(ModelBox rect) {
        return getBounds().intersects(rect);
    }

    public ModelPoint getLocation() {
        return location;
    }

    public Sprite setLocation(ModelPoint location) {
        ModelPoint old = this.location;
        this.location = validatePosition(location);
        //System.out.println(old+"  ---> "+this.location);
//        System.out.println(this+" setLocation "+old+" ==> "+location);
        getPropertyChangeSupport().firePropertyChange("location", old, this.location);
        rebuildBounds();
        return this;
    }

    public Sprite setLocation(double x, double y) {
        return setLocation(new ModelPoint(x, y));
    }

    @Override
    public boolean isMoving() {
        return moving;
    }

    @Override
    public Sprite setMoving(boolean moving) {
        boolean old = this.moving;
        this.moving = moving;
        getPropertyChangeSupport().firePropertyChange("moving", old, moving);
        return this;
    }

    @Override
    public ModelDimension getSize() {
        ModelBox _bounds = getBounds();
        return new ModelDimension(_bounds.getWidth(), _bounds.getHeight(), _bounds.getAltitude());
    }

    public Sprite setSize(ModelDimension size) {
        ModelDimension old = this.size;
        this.size = size;
        getPropertyChangeSupport().firePropertyChange("size", old, size);
        rebuildBounds();
        return this;
    }

    @Override
    public Sprite setSize(double width, double height) {
        return setSize(new ModelDimension(width, height));
    }

    public ModelBox getBounds() {
        return bounds;
    }

    public String getName() {
        return name;
    }

    public Sprite setName(String name) {
        String old = this.name;
        this.name = name;
        getPropertyChangeSupport().firePropertyChange("name", old, name);
        return this;
    }

    public String getKind() {
        return kind;
    }

    public Sprite setKind(String kind) {
        String old = this.kind;
        this.kind = kind;
        getPropertyChangeSupport().firePropertyChange("kind", old, kind);
        return this;
    }

    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public boolean isAttackable() {
        return attackable;
    }

    public boolean isOwnedByPlayer(int playerId) {
        return playerId == this.playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Sprite setPlayerId(int playerId) {
        int old = this.playerId;
        this.playerId = playerId;
        getPropertyChangeSupport().firePropertyChange("playerId", old, playerId);
        return this;
    }

    public int getId() {
        return id;
    }

    public Sprite setId(int id) {
        int old = this.id;
        this.id = id;
        getPropertyChangeSupport().firePropertyChange("id", old, id);
        return this;
    }

    public double getHeight() {
        return size.getHeight();
    }

    public double getWidth() {
        return size.getWidth();
    }

    public double getAltitude() {
        return size.getAltitude();
    }

    public double getX() {
        return getLocation().getX();
    }

    public double getY() {
        return getLocation().getY();
    }

    public boolean isDead() {
        return getLife() <= 0;
    }

    public boolean isCrossable() {
        return crossable;
    }

    public Sprite setCrossable(boolean crossable) {
        boolean old = this.crossable;
        this.crossable = crossable;
        getPropertyChangeSupport().firePropertyChange("crossable", old, crossable);
        return this;
    }

    public Sprite addPropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(listener);
        return this;
    }

    public Sprite addPropertyChangeListener(String property, PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(property, listener);
        return this;
    }

    public Sprite removePropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
        return this;
    }

    public Sprite removePropertyChangeListener(String property, PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(property, listener);
        return this;
    }

    @Override
    public double getDirection() {
        return direction;
    }

    @Override
    public Sprite setDirection(double value) {
        double p2 = 2 * Math.PI;
        while (value > 0 && value > p2) {
            value = value - p2;
        }
        while (value < 0 && value < p2) {
            value = value + p2;
        }

        double old = this.direction;
        this.direction = value;
        getPropertyChangeSupport().firePropertyChange("direction", old, direction);
        return this;
    }

    @Override
    public Sprite setDirection(Direction value) {
        setDirection(value.getDirectionAngle(getDirection()));
        return this;
    }

    public double getSpeed() {
        return speed;
    }

    public Sprite setSpeed(double speed) {
        double old = this.speed;
        this.speed = speed;
        getPropertyChangeSupport().firePropertyChange("speed", old, speed);
        return this;
    }

    @Override
    public double getAngularSpeed() {
        return angularSpeed;
    }

    @Override
    public Sprite setAngularSpeed(double angularSpeed) {
        double old = this.angularSpeed;
        this.angularSpeed = angularSpeed;
        getPropertyChangeSupport().firePropertyChange("angularSpeed", old, angularSpeed);
        return this;
    }

    public Sprite die() {
        setLife(0);
        return this;
    }

    public Sprite addLife(int x) {
        setLife(getLife() + x);
        return this;
    }

    public ModelPoint validatePosition(ModelPoint position) {
        return new ModelPoint(
                revalidatePrecision(position.getX(), locationPrecision.getX()),
                revalidatePrecision(position.getY(), locationPrecision.getY()),
                revalidatePrecision(position.getZ(), locationPrecision.getZ())
        );
    }

    public int getSight() {
        return sight;
    }

    public Sprite setSight(int sight) {
        if (sight < 0) {
            throw new IllegalArgumentException("Sight must be positive");
        }
        int old = this.sight;
        this.sight = sight;
        getPropertyChangeSupport().firePropertyChange("sight", old, sight);
        return this;
    }

    @Override
    public double distance(Sprite b) {
        return getBounds().distance(b.getBounds());
    }

    @Override
    public double distance(ModelBox b) {
        return getBounds().distance(b);
    }

    public double distance(ModelPoint b) {
        return getBounds().distance(b);
    }

    public Map<String, Object> getProperties() {
        return properties == null ? new HashMap<String, Object>() : new HashMap<String, Object>(properties);
    }

    public Object getProperty(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        return properties == null ? null : properties.get(name);
    }

    public Sprite setProperty(String name, Object value) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        if (name.startsWith("$")) {
            throw new IllegalArgumentException("Names starting with $ are not supported");
        }
        if (properties == null) {
            if (value != null) {
                properties = new HashMap<String, Object>();
                properties.put(name, value);
                getPropertyChangeSupport().firePropertyChange("$" + name, null, value);
            }
        } else {
            if (value != null) {
                Object oldValue = properties.put(name, value);
                getPropertyChangeSupport().firePropertyChange("$" + name, oldValue, value);
            } else {
                Object oldValue = properties.remove(name);
                getPropertyChangeSupport().firePropertyChange("$" + name, oldValue, value);
            }
        }
        return this;
    }

    @Override
    public ModelVector getVelocity() {
        return ModelVector.newAngular(getSpeed(), getDirection());
    }

    @Override
    public Sprite setVelocity(ModelVector velocityVector) {
        setSpeed(velocityVector.getAmplitude());
        setDirection(velocityVector.getDirection());
        return this;
    }

    @Override
    public Sprite copy() {
        return new DefaultSprite().copyFrom(this);
    }

    public Sprite copyFrom(Sprite sprite) {
        setArmors(sprite.getArmors());
        setAttackable(sprite.isAttackable());
        setCrossable(sprite.isCrossable());
        setLayer(sprite.getLayer());
        setLocation(sprite.getLocation());
        setSize(sprite.getSize());
        setMaxLife(sprite.getMaxLife());
        setMoving(sprite.isMoving());
        setName(sprite.getName());
        setPlayerId(sprite.getPlayerId());
        setSpeed(sprite.getSpeed());
        setSight(sprite.getSight());
        setSelectable(sprite.isSelectable());
        setVelocity(sprite.getVelocity());
        setAngularSpeed(sprite.getAngularSpeed());
        setDirection(sprite.getDirection());
        setLife(sprite.getLife());
        setId(sprite.getId());
        setKind(sprite.getKind());
        setCollisionSides(sprite.getCollisionSides());
        setStyle(sprite.getStyle());
        setWeapons(sprite.getWeapons());
        for (Map.Entry<String, Object> e : sprite.getProperties().entrySet()) {
            setProperty(e.getKey(), e.getValue());
        }
        return this;
    }

    public int getMovementStyle() {
        return movementStyle;
    }

    public Sprite setMovementStyle(int movementStyle) {
        int old = this.movementStyle;
        this.movementStyle = movementStyle;
        getPropertyChangeSupport().firePropertyChange("movementStyle", old, movementStyle);
        return this;
    }
}
