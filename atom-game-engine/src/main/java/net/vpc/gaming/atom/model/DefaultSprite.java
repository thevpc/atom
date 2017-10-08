package net.vpc.gaming.atom.model;

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
    private transient SceneEngineModel sceneModel;


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

    public int getSight() {
        return sight;
    }

    public void setSight(int sight) {
        if (sight < 0) {
            throw new IllegalArgumentException("Sight must be positive");
        }
        int old = this.sight;
        this.sight = sight;
        getPropertyChangeSupport().firePropertyChange("sight", old, sight);
    }

    public boolean isCrossable() {
        return crossable;
    }

    public void setCrossable(boolean crossable) {
        boolean old = this.crossable;
        this.crossable = crossable;
        getPropertyChangeSupport().firePropertyChange("crossable", old, crossable);
    }

    public boolean isAt(ModelBox rect) {
        return getBounds().intersects(rect);
    }

    public boolean isAt(ModelPoint point) {
        return getBounds().contains(point);
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        int old = this.playerId;
        this.playerId = playerId;
        getPropertyChangeSupport().firePropertyChange("playerId", old, playerId);
    }

    public boolean isOwnedByPlayer(int playerId) {
        return playerId == this.playerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        int old = this.id;
        this.id = id;
        getPropertyChangeSupport().firePropertyChange("id", old, id);
    }

    public ModelPoint validatePosition(ModelPoint position) {
        return new ModelPoint(
                revalidatePrecision(position.getX(), locationPrecision.getX()),
                revalidatePrecision(position.getY(), locationPrecision.getY()),
                revalidatePrecision(position.getZ(), locationPrecision.getZ())
        );
    }

    private double revalidatePrecision(double value, double precision) {
        if (Math.abs(value) <= precision) {
            return 0;
        }
        return Math.round(value / precision) * precision;
    }

    public ModelPoint getLocation() {
        return location;
    }

    public void setLocation(int x,int y) {
        setLocation(new ModelPoint(x,y));
    }

    public void setLocation(ModelPoint location) {
        ModelPoint old = this.location;
        this.location = validatePosition(location);
        //System.out.println(old+"  ---> "+this.location);
//        System.out.println(this+" setLocation "+old+" ==> "+location);
        getPropertyChangeSupport().firePropertyChange("location", old, this.location);
        rebuildBounds();
    }

    public ModelBox getBounds() {
        return bounds;
    }

    @Override
    public ModelDimension getSize() {
        ModelBox _bounds = getBounds();
        return new ModelDimension(_bounds.getWidth(), _bounds.getHeight(), _bounds.getAltitude());
    }

    @Override
    public void setSize(int width, int height) {
        setSize(new ModelDimension(width,height));
    }

    public void setSize(ModelDimension size) {
        ModelDimension old = this.size;
        this.size = size;
        getPropertyChangeSupport().firePropertyChange("size", old, size);
        rebuildBounds();
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean value) {
        boolean old = this.selectable;
        this.selectable = value;
        getPropertyChangeSupport().firePropertyChange("selectable", old, selectable);
    }

    public double getWidth() {
        return size.getWidth();
    }

    @Override
    public double getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction value) {
        setDirection(value.getDirectionAngle(getDirection()));
    }

    @Override
    public void setDirection(double value) {
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
    }

    public double getHeight() {
        return size.getHeight();
    }

    public double getAltitude() {
        return size.getAltitude();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        getPropertyChangeSupport().firePropertyChange("name", old, name);
    }

    public int getMovementStyle() {
        return movementStyle;
    }

    public DefaultSprite setMovementStyle(int movementStyle) {
        int old=this.movementStyle;
        this.movementStyle = movementStyle;
        getPropertyChangeSupport().firePropertyChange("movementStyle", old, movementStyle);
        return this;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        String old = this.kind;
        this.kind = kind;
        getPropertyChangeSupport().firePropertyChange("kind", old, kind);
    }

    public SceneEngineModel getSceneEngineModel() {
        return sceneModel;
    }

    public void setSceneEngineModel(SceneEngineModel model) {
        SceneEngineModel old = this.sceneModel;
        this.sceneModel = model;
        getPropertyChangeSupport().firePropertyChange("model", old, model);
    }

    public boolean isDead() {
        return getLife() <= 0;
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(property, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
    }

    private ModelBox rebuildBounds() {
        ModelBox old = bounds;
        ModelBox value = new ModelBox(location, size);
        bounds = value;
        getPropertyChangeSupport().firePropertyChange("bounds", old, bounds);
        return old;
    }

    @Override
    public ModelVector getVelocity() {
        return ModelVector.newAngular(getSpeed(), getDirection());
    }

    @Override
    public void setVelocity(ModelVector velocityVector) {
        setSpeed(velocityVector.getAmplitude());
        setDirection(velocityVector.getDirection());
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        double old = this.speed;
        this.speed = speed;
        getPropertyChangeSupport().firePropertyChange("speed", old, speed);
    }

    @Override
    public double getAngularSpeed() {
        return angularSpeed;
    }

    @Override
    public void setAngularSpeed(double angularSpeed) {
        double old = this.angularSpeed;
        this.angularSpeed = angularSpeed;
        getPropertyChangeSupport().firePropertyChange("angularSpeed", old, angularSpeed);
    }

    public SpriteArmor[] getArmors() {
        return armors;
    }

    public void setArmors(SpriteArmor... armors) {
        SpriteArmor[] old = this.armors;
        this.armors = armors;
        getPropertyChangeSupport().firePropertyChange("armors", old, armors);
    }

    public SpriteWeapon[] getWeapons() {
        return weapons;
    }

    public void setWeapons(SpriteWeapon... weapons) {
        SpriteWeapon[] old = this.weapons;
        this.weapons = weapons;
        getPropertyChangeSupport().firePropertyChange("weapons", old, weapons);
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        if (life <= 0) {
            life = 0;
        }
        int old = this.life;
        this.life = life;
        getPropertyChangeSupport().firePropertyChange("life", old, life);
    }

    public void die() {
        setLife(0);
    }

    public void addLife(int x) {
        setLife(getLife() + x);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "id=" + id + ", name=" + name + ", " + bounds + '}';
    }

    public int getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(int maxLife) {
        int old = this.maxLife;
        this.maxLife = maxLife;
        getPropertyChangeSupport().firePropertyChange("maxLife", old, maxLife);
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        int old = this.style;
        this.style = style;
        getPropertyChangeSupport().firePropertyChange("style", old, style);
    }

    @Override
    public boolean isAttackable() {
        return attackable;
    }

    public void setAttackable(boolean attackable) {
        boolean old = this.attackable;
        this.attackable = attackable;
        getPropertyChangeSupport().firePropertyChange("attackable", old, attackable);
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        int old = this.layer;
        this.layer = layer;
        getPropertyChangeSupport().firePropertyChange("layer", old, layer);
    }

    @Override
    public int getCollisionSides() {
        return collisionSides;
    }

    @Override
    public void setCollisionSides(int collisionSides) {
        int old=this.collisionSides;
        this.collisionSides = collisionSides;
        getPropertyChangeSupport().firePropertyChange("collisionSides", old, collisionSides);
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

    public void setProperty(String name, Object value) {
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
    }

    public double getX() {
        return getLocation().getX();
    }

    public double getY() {
        return getLocation().getY();
    }

    @Override
    public boolean isMoving() {
        return moving;
    }

    @Override
    public void setMoving(boolean moving) {
        boolean old = this.moving;
        this.moving = moving;
        getPropertyChangeSupport().firePropertyChange("moving", old, moving);
    }

    @Override
    public void copyFrom(Sprite sprite) {
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
        setWeapons(sprite.getWeapons());
        for (Map.Entry<String, Object> e : sprite.getProperties().entrySet()) {
            setProperty(e.getKey(), e.getValue());
        }
    }
}
