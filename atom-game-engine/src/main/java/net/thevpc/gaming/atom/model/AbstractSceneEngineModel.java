package net.thevpc.gaming.atom.model;

import java.awt.*;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 dec. 2006 22:55:41
 */
public abstract class AbstractSceneEngineModel implements SceneEngineModel {

    private final List<SceneEngineModelListener> listeners = new ArrayList<SceneEngineModelListener>();
    private final Map<Integer, Sprite> sprites = new HashMap<Integer, Sprite>();
    protected PropertyChangeSupport propertyChangeSupport;
    protected Map<String, Object> properties;
    private ModelDimension size;
    private long frameOffset;
    private int spriteIndex;
    private int playerIndex;
    private SceneEngine sceneEngine;
    private CellSprites[] tileSprites;
    private Map<Integer, Player> players = new LinkedHashMap<Integer, Player>();
    private PropertyChangeListener spriteChangedWrapper = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
//            synchronized (listeners) {
            for (SceneEngineModelListener listener : listeners) {
                listener.spriteUpdated(AbstractSceneEngineModel.this, (Sprite) evt.getSource(), evt);
            }
            if (evt.getPropertyName().equals("location")) {
                for (SceneEngineModelListener listener : listeners) {
                    listener.spriteMoved(AbstractSceneEngineModel.this, (Sprite) evt.getSource(), (ModelPoint) evt.getOldValue(), (ModelPoint) evt.getNewValue());
                }
            }
//            }
        }
    };
    private PropertyChangeListener boundsListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            Sprite s = (Sprite) evt.getSource();
            ModelBox r1 = (ModelBox) evt.getOldValue();
            ModelBox r2 = (ModelBox) evt.getNewValue();

            Collection<Tile> oldCells = findTiles(r1);
            Collection<Tile> newCells = findTiles(r2);
            Collection<Tile> toRemove = new ArrayList<Tile>(oldCells);
            toRemove.removeAll(newCells);
            ArrayList<Tile> toAdd = new ArrayList<Tile>(newCells);
            toAdd.removeAll(oldCells);
            oldCells.removeAll(newCells);
            CellSprites[] ts = getTileSprites();
            for (Tile tile : toRemove) {
                ts[tile.getId()].remove(s);
            }
            for (Tile tile : toAdd) {
                ts[tile.getId()].add(s);
            }
        }
    };

    public AbstractSceneEngineModel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public SceneEngine getSceneEngine() {
        return sceneEngine;
    }

    public void setSceneEngine(SceneEngine sceneEngine) {
        this.sceneEngine = sceneEngine;
        for (Sprite sprite : sprites.values()) {
            sprite.setSceneEngine(sceneEngine);
        }
    }
    

    public Sprite getSprite(int id) {
//        synchronized (sprites) {
        return sprites.get(id);
//        }
    }

    public int getPlayersCount() {
        return players.size();
    }

    public List<Player> getPlayers() {
        return new ArrayList<Player>(players.values());
    }

    public Player getPlayer(int id) {
        Player p = players.get(id);
        if (p == null) {
            throw new NoSuchElementException("No such player " + id);
        }
        return p;
    }

    public void removeSprite(int sprite) {
//        synchronized (sprites) {
        if(sprites.containsKey(sprite)) {
            for (SceneEngineModelListener listener : listeners) {
                listener.spriteRemoving(this, sprites.get(sprite));
            }
            Sprite s = (Sprite) sprites.remove(sprite);
            if (s != null) {
                getTileSprites();
                for (Tile cell : findTiles(s.getBounds())) {
                    tileSprites[cell.getId()].remove(s);
                }
                s.setSceneEngine(null);
                s.removePropertyChangeListener(spriteChangedWrapper);
                s.removePropertyChangeListener("bounds", boundsListener);
//                synchronized (listeners) {
                for (SceneEngineModelListener listener : listeners) {
                    listener.spriteRemoved(this, s);
                }
//                }
            }
        }
//        }
    }

    @Override
    public void removePlayer(int playerId) {
        final Player player = players.remove(playerId);
        if (player != null) {
            for (SceneEngineModelListener listener : listeners) {
                listener.playerRemoved(this, player);
            }
        }
        //player.setGame(null);
    }

    @Override
    public void resetSprites() {
//        synchronized (sprites) {
        for (Integer sprite : new ArrayList<Integer>(sprites.keySet())) {
            removeSprite(sprite);
        }
//        int x=0;
//        for (CellSprites cellSprites : tileSprites) {
//            if(cellSprites.sprites.size()!=0){
//                System.out.println("Why?");
//                x++;
//            }
//        }
//        System.out.println("x="+x);
        spriteIndex = 0;
//        }
    }

    @Override
    public void resetPlayers() {
        for (Integer sprite : new ArrayList<Integer>(players.keySet())) {
            removePlayer(sprite);
        }
        playerIndex=0;
    }

    private int nextId() {
        while (sprites.containsKey(spriteIndex)) {
            spriteIndex++;
        }
        return spriteIndex;
    }

    @Override
    public int addSprite(Sprite sprite) {
        int id = sprite.getId();
        if (id <= 0) {
            id = nextId();
        }
        if (sprites.containsKey(id)) {
            throw new IllegalArgumentException("Sprite Already exists : id =" + id + " (old=" + sprites.get(id) + " , new " + sprite + ")");
        }
        if (spriteIndex < id) {
            spriteIndex = id;
        }
        SpriteMainTask oldTask = sprite.getMainTask();
        SpriteCollisionTask oldCollMan = sprite.getCollisionTask();
        sprite.setId(id);
        sprites.put(id, sprite);
        sprite.setSceneEngine(sceneEngine);
        CellSprites[] ts = getTileSprites();
        for (Tile tile : findTiles(sprite.getBounds())) {
            ts[tile.getId()].add(sprite);
        }
        sprite.removePropertyChangeListener(spriteChangedWrapper);
        sprite.addPropertyChangeListener(spriteChangedWrapper);
        sprite.removePropertyChangeListener("bounds", boundsListener);
        sprite.addPropertyChangeListener("bounds", boundsListener);
        sceneEngine.setSpriteMainTask(sprite.getId(),oldTask);
        sceneEngine.setSpriteCollisionTask(sprite.getId(),oldCollMan);
//            synchronized (listeners) {
        for (SceneEngineModelListener listener : listeners) {
            listener.spriteAdded(this, sprite);
        }
        return sprite.getId();
    }

    @Override
    public Player createPlayer() {
        return new DefaultPlayer("Player " + (playerIndex + 1));
    }

    @Override
    public int addPlayer(final Player player) {
        //player.setGame(this);
        if (player.getId() <= 0) {
            player.setId(++playerIndex);
        }
        if (playerIndex < player.getId()) {
            playerIndex++;
        }
        if (players.containsKey(player.getId())) {
            throw new IllegalArgumentException("Player exists");
        }
        player.setSceneModel(this);
        players.put(player.getId(), player);
        for (SceneEngineModelListener listener : listeners) {
            listener.playerAdded(this, player);
        }
        player.addPropertyChangeListener("selectionReset", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                propertyChangeSupport.firePropertyChange("selectionReset", null, player);
            }
        });
        player.addPropertyChangeListener("selectionAdded", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                propertyChangeSupport.firePropertyChange("selectionAdded", null, player);
            }
        });
        player.addPropertyChangeListener("selectionRemoved", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                propertyChangeSupport.firePropertyChange("selectionRemoved", null, player);
            }
        });
        return player.getId();
    }

    public List<Sprite> getSprites() {
//        synchronized (sprites) {
        Collection<Sprite> values = sprites.values();
        return new ArrayList<Sprite>(values);
//        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(property, listener);
    }

    public List<Sprite> findSprites(ModelPoint point, int layer) {
        final Tile cell = findTile(point);
        if (cell != null) {

            return getTileSprites()[cell.getId()].getSprites(point, layer);
        }
        return Collections.EMPTY_LIST;
    }

    public List<Sprite> findSprites(ModelPoint point) {
        final Tile cell = findTile(point);
        if (cell != null) {
            return getTileSprites()[cell.getId()].getSprites(point);
        }
        return Collections.EMPTY_LIST;
    }

    public List<Sprite> findSprites(ModelBox rect) {
        List<Sprite> all = new ArrayList<Sprite>();
        HashSet<Integer> visited = new HashSet<Integer>();
        CellSprites[] ts = getTileSprites();
        for (Tile cell : findTiles(rect)) {
            for (Sprite s : ts[cell.getId()].getSprites(rect)) {
                if (!visited.contains(s.getId())) {
                    visited.add(s.getId());
                    all.add(s);
                }
            }
        }
        return all;
    }

    private CellSprites[] getTileSprites() {
        if (tileSprites == null) {
            Tile[] cells = getTiles();
            tileSprites = new CellSprites[cells.length];
            for (int i = 0; i < cells.length; i++) {
                Tile cell = cells[i];
                tileSprites[i] = new CellSprites(cell);
            }
        }
        return tileSprites;
    }

    @Override
    public List<Tile> findTiles(Path2D polygon) {
        Rectangle rect = polygon.getBounds();
        ModelDimension engineDim = getSize();
        int _columns = (int) engineDim.getWidth();
        int _rows = (int) engineDim.getHeight();
        Tile[][] _tileMatrix = getTileMatrix();

        int gx0 = (int) Math.floor(rect.getX());
        int gy0 = (int) Math.floor(rect.getY());

        int gx1 = (int) Math.floor((rect.getX() + rect.getWidth() - 0.001));
        int gy1 = (int) Math.floor((rect.getY() + rect.getHeight() - 0.001));
        if ((gx0 < 0 && gx1 < 0) || (gx0 >= _columns && gx1 >= _columns)) {
            return Collections.EMPTY_LIST;
        }
        if ((gy0 < 0 && gy1 < 0) || (gy0 >= _rows && gy1 >= _rows)) {
            return Collections.EMPTY_LIST;
        }
        List<Tile> all = new ArrayList<Tile>();
        if (gx0 < 0) {
            gx0 = 0;
        }

        if (gx1 >= _columns) {
            gx1 = _columns - 1;
        }
        if (gy0 < 0) {
            gy0 = 0;
        }
        if (gy1 >= _rows) {
            gy1 = _rows - 1;
        }

        for (int r = gy0; r <= gy1; r++) {
            for (int c = gx0; c <= gx1; c++) {
                Tile m = _tileMatrix[r][c];
                if (polygon.intersects(m.getX(), m.getY(), m.getWidth(), m.getHeight())) {
                    all.add(m);
                }
            }
        }
        return all;
    }

    public List<Tile> findTiles(ModelBox rect) {
        ModelDimension engineDim = getSize();
        int _columns = (int) engineDim.getWidth();
        int _rows = (int) engineDim.getHeight();
        Tile[][] _tileMatrix = getTileMatrix();

        int gx0 = (int) Math.floor(rect.getX());
        int gy0 = (int) Math.floor(rect.getY());

        double x1b = rect.getX() + rect.getWidth();
        double y1b = rect.getY() + rect.getHeight();
        int gx1 = (int) Math.floor(x1b);
        int gy1 = (int) Math.floor(y1b);
        if(gx1==x1b){
            gx1--;
        }
        if(gy1==y1b){
            gy1--;
        }
        if ((gx0 < 0 && gx1 < 0) || (gx0 >= _columns && gx1 >= _columns)) {
            return Collections.EMPTY_LIST;
        }
        if ((gy0 < 0 && gy1 < 0) || (gy0 >= _rows && gy1 >= _rows)) {
            return Collections.EMPTY_LIST;
        }
        List<Tile> all = new ArrayList<Tile>();
        if (gx0 < 0) {
            gx0 = 0;
        }

        if (gx1 >= _columns) {
            gx1 = _columns - 1;
        }
        if (gy0 < 0) {
            gy0 = 0;
        }
        if (gy1 >= _rows) {
            gy1 = _rows - 1;
        }

        for (int r = gy0; r <= gy1; r++) {
            for (int c = gx0; c <= gx1; c++) {
                all.add(_tileMatrix[r][c]);
            }
        }
        return all;
    }

    public Tile findTile(ModelPoint point) {
        ModelDimension engineDim = getSize();
        int _columns = (int) engineDim.getWidth();
        int _rows = (int) engineDim.getHeight();
        Tile[][] _tileMatrix = getTileMatrix();
        int gx = (int) Math.floor(point.getX());
        int gy = (int) Math.floor(point.getY());
        if (gy >= 0 && gy < _rows && gx >= 0 && gx < _columns) {
            return _tileMatrix[gy][gx];
        }
        return null;
    }

    public Tile[] getTiles() {
        ModelDimension engineDim = getSize();
        int _columns = (int) engineDim.getWidth();
        int _rows = (int) engineDim.getHeight();
        Tile[][] _tileMatrix = getTileMatrix();
        Tile[] r = new Tile[_columns * _rows];
        for (int i = 0; i < _rows; i++) {
            System.arraycopy(_tileMatrix[i], 0, r, i * _columns, _columns);
        }
        return r;
    }

    public long getFrame() {
        return frameOffset;
    }

    public void setFrame(long frameOffset) {
        this.frameOffset = frameOffset;
    }

    public void addSceneEngineModelListener(SceneEngineModelListener listener) {
//        synchronized (listeners) {
        listeners.add(listener);
//        }
    }

    public void removeSceneEngineModelListener(SceneEngineModelListener listener) {
//        synchronized (listeners) {
        listeners.remove(listener);
//        }
    }

    public SceneEngineModelListener[] getListeners() {
        return listeners.toArray(new SceneEngineModelListener[listeners.size()]);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void setSizeImpl(ModelDimension dimension) {
        this.size = dimension;
    }

    @Override
    public ModelDimension getSize() {
        return size;
    }

    @Override
    public void setSize(ModelDimension dimension) {
        ModelDimension olModelDimension = this.size;
        if (!Objects.equals(olModelDimension, dimension)) {
            setSizeImpl(dimension);
            getPropertyChangeSupport().firePropertyChange("size", olModelDimension, getSize());
        }
    }

    protected PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    public Map<String, Object> getProperties() {
        //return new LinkedHashMap<String,Object>(properties);
        return properties != null ? new LinkedHashMap<String, Object>(properties) : new HashMap<String, Object>(1);
    }

    public <T> T getProperty(String propertyName) {
        if (properties != null) {
            return (T) properties.get(propertyName);
        }
        return null;
    }

    public void removeProperty(String propertyName) {
        setProperty(propertyName, null);
    }

    public void setProperty(String propertyName, Object value) {
        Object old;
        if (value != null) {
            if (properties == null) {
                properties = new LinkedHashMap<>();
            }
            old = properties.put(propertyName, value);
            getPropertyChangeSupport().firePropertyChange(propertyName, old, value);
        } else {
            if (properties != null) {
                old = properties.remove(propertyName);
                getPropertyChangeSupport().firePropertyChange(propertyName, old, value);
            }
        }
    }

    private static class CellSprites {

        private final Set<Sprite> sprites = new HashSet<Sprite>();
        private Tile cell;

        public CellSprites(Tile cell) {
            this.cell = cell;
        }

        public void add(Sprite sprite) {
//            synchronized (sprites) {
            sprites.add(sprite);
//            }
        }

        public void remove(Sprite sprite) {
//            synchronized (sprites) {
            sprites.remove(sprite);
//            }
        }

        public List<Sprite> getSprites(ModelPoint point, int layer) {
            List<Sprite> all = new ArrayList<Sprite>();
//            synchronized (sprites) {

            for (Sprite sprite : sprites) {
                if (sprite.isAt(point) && layer == sprite.getLayer()) {
                    all.add(sprite);
                }
            }
//            }
            return all;
        }

        public List<Sprite> getSprites(ModelPoint point) {
            List<Sprite> all = new ArrayList<Sprite>();
//            synchronized (sprites) {

            for (Sprite sprite : sprites) {
                if (sprite.isAt(point)) {
                    all.add(sprite);
                }
            }
//            }
            return all;
        }

        public List<Sprite> getSprites() {
            return new ArrayList<Sprite>(sprites);
        }

        public List<Sprite> getSprites(ModelBox rect) {
            List<Sprite> all = new ArrayList<Sprite>();
//            synchronized (sprites) {
            for (Sprite sprite : sprites) {
                if (sprite.isAt(rect)) {
                    all.add(sprite);
                }
            }
//            }
            return all;
        }
    }
}
