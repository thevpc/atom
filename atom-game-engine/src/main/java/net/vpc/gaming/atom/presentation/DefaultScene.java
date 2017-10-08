package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.debug.AtomDebug;
import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.SceneEngineChangeAdapter;
import net.vpc.gaming.atom.engine.SceneEngineFrameListener;
import net.vpc.gaming.atom.extension.SceneExtension;
import net.vpc.gaming.atom.extension.board.DefaultBoardTextureExtension;
import net.vpc.gaming.atom.extension.fogofwar.FogOfWarSceneExtension;
import net.vpc.gaming.atom.presentation.components.SceneComponent;
import net.vpc.gaming.atom.presentation.components.SceneComponentLayout;
import net.vpc.gaming.atom.util.AtomUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;

import net.vpc.gaming.atom.model.DefaultSceneModel;
import net.vpc.gaming.atom.model.ModelBox;
import net.vpc.gaming.atom.model.ModelDimension;
import net.vpc.gaming.atom.model.ModelPoint;
import net.vpc.gaming.atom.model.Player;
import net.vpc.gaming.atom.model.RatioViewBox;
import net.vpc.gaming.atom.model.RatioDimension;
import net.vpc.gaming.atom.model.RatioPoint;
import net.vpc.gaming.atom.model.SceneEngineModel;
import net.vpc.gaming.atom.model.SceneModel;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.Tile;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.model.ViewDimension;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.layers.BoardLayer;
import net.vpc.gaming.atom.presentation.layers.BoardLayerDrawingContext;
import net.vpc.gaming.atom.presentation.layers.ComponentsInternalLayer;
import net.vpc.gaming.atom.presentation.layers.EventsDispatcherLayer;
import net.vpc.gaming.atom.presentation.layers.InteractiveLayer;
import net.vpc.gaming.atom.presentation.layers.InternalLayer;
import net.vpc.gaming.atom.presentation.layers.KeyEventExt;
import net.vpc.gaming.atom.presentation.layers.Layer;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;
import net.vpc.gaming.atom.presentation.layers.SpritesInternalLayer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 5 dec. 2006 11:18:25
 */
public class DefaultScene implements Scene {

    private SceneEngine sceneEngine;
    private Game game;
    private Object companionObject;
    private String id;
    private SpriteView fallbackSpriteView = new FallbackSpriteView();
    private SceneModel sceneModel;
    private Map<String, SceneExtension> sceneExtensions = new LinkedHashMap<String, SceneExtension>();
    private List<SceneChangeListener> sceneChangeListeners = new ArrayList<>();
    private IndexLayer[] indexedLayers = new IndexLayer[0];
    private InteractiveLayer[] interactiveLayers = new InteractiveLayer[0];
    private Layer[] backScreenLayers = new Layer[0];
    private BoardLayer[] backgroundLayers = new BoardLayer[0];
    private BoardLayer[] skyLayers = new BoardLayer[0];
    private Layer[] frontScreenLayers = new Layer[0];
    private EventsDispatcherLayer fallbackLayer = new EventsDispatcherLayer();
    private int namedComponentsNameHelper = 0;
    private LinkedHashMap<String, SceneComponentLayout> namedComponents = new LinkedHashMap<String, SceneComponentLayout>();
    private java.util.List<SceneController> listeners = new ArrayList<SceneController>();
    private Map<Class, Object> viewForModelType = new HashMap<Class, Object>();
    private Map<Class<? extends Sprite>, SpriteView> spriteViewForModel = new HashMap<Class<? extends Sprite>, SpriteView>();
    private Map<String, SpriteView> spriteViewForModelKind = new HashMap<String, SpriteView>();
    private Map<Integer, SpriteView> spriteViewForModelId = new HashMap<Integer, SpriteView>();
    private String title = "Game";
    private AffineTransform screenAffineTransform;
    private AffineTransform boardAffineTransform;
    private BufferedImage frameImage;
    private ModelBox cameraModel = null;
    private NativeInputListener nativeInputListener = new NativeInputListener();
    private SceneComponentPainter component = new SceneComponentPainter();
    private FrameBuilder frameBuilder = new FrameBuilder();
    private PropertyChangeSupport propertyChangeSupport;
    private ImageProducer imageProducer;
    private boolean started = false;
    private Sprite cameraLockToSprite;
    private List<SceneListener> sceneListeners = new ArrayList<>();
    private PropertyChangeListener modelListenerBridge = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            propertyChangeSupport.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    };
    SceneEngineFrameListener cameraLockToSpriteListener = new SceneEngineFrameListener() {
        @Override
        public void modelUpdated(SceneEngine sceneEngine, SceneEngineModel model) {
            if (cameraLockToSprite != null) {
                moveCameraTo(cameraLockToSprite);
            }
        }
    };
//    private Comparator<Tile> rowTilesComparator = new Comparator<Tile>() {
//        @Override
//        public int compare(Tile o1, Tile o2) {
//            return o2.getColumn() - o1.getColumn();
//        }
//    };
//    private Comparator<Sprite> rowSpritesComparator = new Comparator<Sprite>() {
//        @Override
//        public int compare(Sprite o1, Sprite o2) {
//            double x1 = o1.getLocation().getX();
//            double w1 = o1.getSize().getWidth();
//            double x2 = o2.getLocation().getX();
//            double w2 = o2.getSize().getWidth();
//            return Double.compare((x2 + w2), (x1 + w1));
//        }
//    };

    public DefaultScene(int tileSize) {
        this("", new ViewDimension(tileSize, tileSize, tileSize));
    }

    public DefaultScene() {
        this("", new ViewDimension(50, 50, 50));
    }

    public DefaultScene(String id, int tileSize) {
        this(id, new ViewDimension(tileSize, tileSize, tileSize));
    }

    public DefaultScene(ViewDimension tileSize) {
        this("", tileSize);
    }

    public String getId() {
        return id;
    }

    public DefaultScene(String id, ViewDimension tileSize) {
        this.id = id;
        if (id == null || id.trim().isEmpty()) {
            id = getClass().getSimpleName();
        }
        this.id = id;
        propertyChangeSupport = new PropertyChangeSupport(this);
        component.setFocusable(true);
        this.fallbackLayer.setLayer(Integer.MIN_VALUE);
        this.fallbackLayer.setScene(this);
        sceneModel = new DefaultSceneModel();
        sceneModel.addPropertyChangeListener(modelListenerBridge);
//        this.gameView = gameView;d

        sceneModel.setTileSize(tileSize);
        sceneModel.setSceneSize(sceneModel.getTileSize());
        PropertyChangeListener resetCameraModel = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                cameraModel = null;
            }
        };
        sceneModel.addPropertyChangeListener("isometricView", resetCameraModel);
        sceneModel.addPropertyChangeListener("tileSize", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateSceneSize();
//                ViewBox oldCamera = getCamera();
//                ViewPoint location = oldCamera.getLocation();
//                ViewDimension oldValue = (ViewDimension) evt.getOldValue();
//                ViewDimension newValue = (ViewDimension) evt.getNewValue();
//                setCamera(new ViewBox(location, new ViewDimension(
//                        (int)(oldCamera.getWidth()*((double)newValue.getWidth()/oldValue.getWidth())),
//                        (int)(oldCamera.getHeight()*((double)newValue.getHeight()/oldValue.getHeight()))
//                )));
            }
        });
        sceneModel.addPropertyChangeListener("screenSize", resetCameraModel);
        sceneModel.addPropertyChangeListener("camera", resetCameraModel);
        packToViewPort();

        component.addMouseMotionListener(nativeInputListener);
        component.addMouseListener(nativeInputListener);
        component.addKeyListener(nativeInputListener);

    }

    public void setTileSize(int size) {
        setTileSize(new ViewDimension(size, size));
    }

    public void setTileSize(int width, int height) {
        setTileSize(new ViewDimension(width, height));
    }

    @Override
    public void setTileSize(int width, int height, int altitude) {
        setTileSize(new ViewDimension(width, height, altitude));
    }

    public ViewDimension getTileSize() {
        return getModel().getTileSize();
    }

    public void setTileSize(ViewDimension tileSize) {
        getModel().setTileSize(tileSize);
    }

    @Override
    public void addSceneChangeListener(SceneChangeListener listener) {
        sceneChangeListeners.add(listener);
    }

    @Override
    public void removeSceneChangeListener(SceneChangeListener listener) {
        sceneChangeListeners.remove(listener);
    }

    @Override
    public SceneChangeListener[] getSceneChangeListeners() {
        return sceneChangeListeners.toArray(new SceneChangeListener[sceneChangeListeners.size()]);
    }

    @Override
    public boolean containsSceneChangeListener(SceneChangeListener listener) {
        return sceneChangeListeners.contains(listener);
    }

    public void removeComponent(SceneComponent component) {
        for (Iterator<SceneComponentLayout> iterator = namedComponents.values().iterator(); iterator.hasNext(); ) {
            SceneComponentLayout li = iterator.next();
            if (li.getComponent().equals(component)) {
                iterator.remove();
            }
        }
    }

    public void removeComponent(String name) {
        namedComponents.remove(name);
    }

    public void addComponent(SceneComponent gameComponent) {
        addComponent(gameComponent, null, Layer.SCREEN_COMPONENT_LAYER);
    }

    public void addComponent(SceneComponent gameComponent, Object constraints) {
        addComponent(gameComponent, constraints, Layer.SCREEN_COMPONENT_LAYER, false);
    }

    public void addComponent(SceneComponent gameComponent, Object constraints, int layer) {
        addComponent(gameComponent, constraints, layer, false);
    }

    public void addComponent(SceneComponent component, Object constraints, int layer, boolean mapAligned) {
        String name = component.getName();
        if (name == null) {
            namedComponentsNameHelper++;
            name = "noname:" + namedComponentsNameHelper;
        } else {
            if (namedComponents.containsKey(name)) {
                throw new IllegalArgumentException("Component with the same name exists : " + name);
            }
            if (name.startsWith("noname:")) {
                throw new IllegalArgumentException("Invalid Component name " + name);
            }
        }
        namedComponents.put(name, new SceneComponentLayout(component, constraints, layer, mapAligned));
        component.setScene(this);
    }

    @Override
    public <T extends GameEngine> T getGameEngine() {
        return getSceneEngine().getGameEngine();
    }

    @Override
    public <T extends SceneEngine> T getSceneEngine() {
        if (sceneEngine == null) {
            throw new IllegalArgumentException("SceneEngine is not defined until scene is registered. Consider moving to method sceneStarted()");
        }
        return (T) sceneEngine;
    }

    @Override
    public boolean isIsometric() {
        return getModel().isIsometric();
    }

    @Override
    public void setIsometric(boolean isometric) {
        getModel().setIsometric(isometric);
    }

    public <T extends SceneModel> T getModel() {
        return (T) sceneModel;
    }

    public void setSceneModel(SceneModel sceneModel) {
        SceneModel old = this.sceneModel;
        this.sceneModel = sceneModel;
        propertyChangeSupport.firePropertyChange("model", old, sceneModel);
    }

    private MouseEvent toIsometricMouseEvent(MouseEvent e) {
        if (isIsometric()) {
            ViewBox vp = getAbsoluteCamera();
            vp = new ViewBox(0, 0, vp.getWidth(), vp.getHeight());
            int x = e.getX();
            int y = e.getY();
            AffineTransform mapTransform = AtomUtils.createIsometricTransformInverse(vp);
            Point dest = new Point();
//            dest = mapTransform.createTransformedShape(new Rectangle(x, y, x + 1, y + 1)).getBounds().getLocation();
            mapTransform.transform(new Point(x, y), dest);
            return new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), dest.x, dest.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        } else {
            return e;
        }
    }

    private void packToViewPort() {
        ViewBox vp = getAbsoluteCamera();
        component.setPreferredSize(new Dimension(vp.getWidth(), vp.getHeight()));
        component.setMinimumSize(new Dimension(vp.getWidth(), vp.getHeight()));
    }

    @Override
    public void init(SceneEngine sceneEngine, Game game) {
        this.sceneEngine = sceneEngine;
        this.game = game;
        sceneEngine.addSceneEngineChangeListener(new SceneEngineChangeAdapter() {
            @Override
            public void modelChanged(SceneEngine sceneEngine, SceneEngineModel oldValue, SceneEngineModel newValue) {
                updateSceneSize();
            }
        });
        sceneEngine.addPropertyChangeListener("size", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateSceneSize();
            }
        });
        sceneEngine.addPropertyChangeListener("camera", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                cameraModel = null;
            }
        });
        updateSceneSize();
        propertyChangeSupport.firePropertyChange("model", null, sceneModel);
        propertyChangeSupport.firePropertyChange("sceneEngine", null, sceneEngine);

        sceneEngine.addSceneFrameListener(frameBuilder);

        //add default extensions
        HashSet<Class> installedExtensions = new HashSet<>();
        for (SceneExtension sceneExtension : getSceneExtensions()) {
            installedExtensions.add(sceneExtension.getClass());
        }
        if (!installedExtensions.contains(DefaultBoardTextureExtension.class)) {
            installExtension(new DefaultBoardTextureExtension());
        }
        if (!installedExtensions.contains(FogOfWarSceneExtension.class)) {
            installExtension(new FogOfWarSceneExtension());
        }
        for (SceneController listener : listeners) {
            listener.sceneInitialized(this);
        }
    }

    @Override
    public void start() {
        if (started) {
            throw new IllegalArgumentException("Scene already started");
        }
        this.started = true;
        for (SceneController sceneEventListener : listeners.toArray(new SceneController[listeners.size()])) {
            sceneEventListener.sceneStarted(this);
        }
        sceneStarted();
    }

    @Override
    public void stop() {
        if (!started) {
            throw new IllegalArgumentException("Scene already stopped");
        }
        this.started = false;
        for (SceneController sceneEventListener : listeners) {
            sceneEventListener.sceneStopped(this);
        }
        sceneStopped();
    }

    protected void updateSceneSize() {
        if(sceneEngine!=null) {
            ModelDimension modeldimension = getSceneEngine().getSize();
            ViewDimension tileDimension = getModel().getTileSize();
            ViewDimension oldScreenSize = getSceneSize();
            ViewDimension screenSize = new ViewDimension(
                    (int) (modeldimension.getWidth() * tileDimension.getWidth()),
                    (int) (modeldimension.getHeight() * tileDimension.getHeight()),
                    (int) (modeldimension.getAltitude() * tileDimension.getAltitude()));
            getModel().setSceneSize(screenSize);
        }
//        ViewBox oldCamera = getCamera();
////        System.out.println("updateSceneSize : oldScreenSize=" + oldScreenSize + " ; oldCamera=" + oldCamera + " : screenSize=" + screenSize);
//        if (oldScreenSize == null) {
//            if (oldCamera == null) {
//                setCamera(new ViewBox(0, 0, screenSize.getWidth(), screenSize.getHeight()));
//            } else {
//                setCamera(new ViewBox(oldCamera.getX(), oldCamera.getY(), screenSize.getWidth(), screenSize.getHeight()));
//            }
//        } else {
//            float oldScreenWidth = (float) oldScreenSize.getWidth();
//            float oldScreenHeight = (float) oldScreenSize.getHeight();
//            if (oldCamera == null) {
//                setCamera(new RatioDimension(1, 1,1));
//            } else {
//                setCamera(new RatioViewBox(
//                        oldCamera.getX() / oldScreenWidth,
//                        oldCamera.getY() / oldScreenHeight,
//                        oldCamera.getWidth() / oldScreenWidth,
//                        oldCamera.getHeight() / oldScreenHeight));
//            }
//        }
    }

    protected void sceneStarted() {
    }

    protected void sceneStopped() {
    }

    protected void frameStep() {
        update();
        for (SceneController sceneController : listeners) {
            sceneController.nexFrame(this);
        }
        buildFrameLayers();
        for (Layer gameLayer : backScreenLayers) {
            gameLayer.nextFrame();
        }
        for (Layer gameLayer : backgroundLayers) {
            gameLayer.nextFrame();
        }
        for (Layer gameLayer : skyLayers) {
            gameLayer.nextFrame();
        }
        for (Layer gameLayer : frontScreenLayers) {
            gameLayer.nextFrame();
        }
        buildFrame();
        component.repaint();
    }

    public void addSceneController(SceneController listener) {
        listeners.add(listener);
    }

    public void removeSceneController(SceneController listener) {
        listeners.remove(listener);
    }

    public void buildFrameLayers() {
        final Collection<Sprite> sprites = findDisplaySprites();
        Sprite[] spritesArr = sprites.toArray(new Sprite[sprites.size()]);
        Arrays.sort(spritesArr, SpriteDrawingComparator.INSTANCE);
        Collection<Sprite> spritesColl = new LinkedList<Sprite>();
        spritesColl.addAll(Arrays.asList(spritesArr));
        Collection<SceneComponentLayout> componentColl = new LinkedList<SceneComponentLayout>(namedComponents.values());
        ArrayList<Layer> frameLayersList = new ArrayList<Layer>();

        if (indexedLayers.length == 0) {
            if (spritesColl.size() > 0) {
                frameLayersList.add(new SpritesInternalLayer(spritesColl.toArray(new Sprite[spritesColl.size()]), this));
            }
            if (componentColl.size() > 0) {
                frameLayersList.add(new ComponentsInternalLayer(componentColl.toArray(new SceneComponentLayout[componentColl.size()]), this));
            }
        } else {
            IndexLayer indexLayer = null;
            int lz = Integer.MAX_VALUE;
            for (int i = 0; i < indexedLayers.length; i++) {
                indexLayer = indexedLayers[i];
                lz = indexLayer.layer.getLayer();
                if (i == 0) {
                    if (lz != Integer.MIN_VALUE) {
                        Sprite[] ss = removeSprites(Integer.MIN_VALUE, lz - 1, spritesColl);
                        if (ss.length > 0) {
                            frameLayersList.add(new SpritesInternalLayer(ss, this));
                        }
                        SceneComponentLayout[] cc = removeComponents(Integer.MIN_VALUE, lz - 1, componentColl);
                        if (cc.length > 0) {
                            frameLayersList.add(new ComponentsInternalLayer(cc, this));
                        }
                    }
                    if (indexLayer.layer.isEnabled()) {
                        frameLayersList.add(indexLayer.layer);
                    }
                } else {
                    int lz0 = indexedLayers[i - 1].layer.getLayer();
                    if (lz != lz0) {
                        Sprite[] ss = removeSprites(lz0, lz - 1, spritesColl);
                        if (ss.length > 0) {
                            frameLayersList.add(new SpritesInternalLayer(ss, this));
                        }
                    }
                    if (lz != lz0) {
                        SceneComponentLayout[] cc = removeComponents(lz0, lz - 1, componentColl);
                        if (cc.length > 0) {
                            frameLayersList.add(new ComponentsInternalLayer(cc, this));
                        }
                    }
                    if (indexLayer.layer.isEnabled()) {
                        frameLayersList.add(indexLayer.layer);
                    }
                }
            }
            Sprite[] ss = removeSprites(lz, Integer.MAX_VALUE, spritesColl);
            if (ss.length > 0) {
                frameLayersList.add(new SpritesInternalLayer(ss, this));
            }
            SceneComponentLayout[] cc = removeComponents(lz, Integer.MAX_VALUE, componentColl);
            if (cc.length > 0) {
                frameLayersList.add(new ComponentsInternalLayer(cc, this));
            }
        }
        ArrayList<Layer> beforeMapLayersList = new ArrayList<Layer>();
        ArrayList<BoardLayer> backgroundLayersList = new ArrayList<BoardLayer>();
        ArrayList<BoardLayer> skyLayersList = new ArrayList<BoardLayer>();
        ArrayList<Layer> afterMapLayersList = new ArrayList<Layer>();
        ArrayList<InteractiveLayer> interactiveLayersList = new ArrayList<>();
        interactiveLayersList.add(fallbackLayer);
        for (Layer layer : frameLayersList) {
            if (layer instanceof BoardLayer) {
                if (layer.getLayer() < Layer.SKY_LAYER) {
                    backgroundLayersList.add((BoardLayer) layer);
                } else {
                    skyLayersList.add((BoardLayer) layer);
                }
            } else if (layer.getLayer() < 0) {
                beforeMapLayersList.add(layer);
            } else {
                afterMapLayersList.add(layer);
            }
            if (layer instanceof InteractiveLayer) {
                interactiveLayersList.add((InteractiveLayer) layer);
            }
        }
        this.backScreenLayers = beforeMapLayersList.toArray(new Layer[beforeMapLayersList.size()]);
        this.backgroundLayers = backgroundLayersList.toArray(new BoardLayer[backgroundLayersList.size()]);
        this.skyLayers = skyLayersList.toArray(new BoardLayer[skyLayersList.size()]);
        this.frontScreenLayers = afterMapLayersList.toArray(new Layer[afterMapLayersList.size()]);
        this.interactiveLayers = interactiveLayersList.toArray(new InteractiveLayer[afterMapLayersList.size()]);
    }

    protected void update() {
        //
    }

    public void buildFrame() {
//        System.out.println("last Frame :DRAW_IMAGE_DRAW_COUNT="+AGEDebug.DRAW_IMAGE_DRAW_COUNT+" ; DRAW_IMAGE_RESCALE_COUNT="+AGEDebug.DRAW_IMAGE_RESCALE_COUNT);
        AtomDebug.DRAW_IMAGE_DRAW_COUNT = 0;
        AtomDebug.DRAW_IMAGE_RESCALE_COUNT = 0;
        ViewBox vp = getCameraScreen();
        BufferedImage _frameImage = new BufferedImage(vp.getWidth(), vp.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = _frameImage.createGraphics();
        AffineTransform sat = getScreenAffineTransform();
        AffineTransform bat = getBoardAffineTransform();

        AffineTransform screenTransform = (AffineTransform) g2d.getTransform().clone();
        AffineTransform boardTransform = (AffineTransform) screenTransform.clone();
        if (bat != null) {
            boardTransform.concatenate(bat);
        }
        if (isIsometric()) {
            boardTransform.concatenate(AtomUtils.createIsometricTransform(vp));
        }
        if (sat != null) {
            boardTransform.concatenate(sat);
            screenTransform.concatenate(sat);
        }

        LayerDrawingContext layerDrawingContext = new LayerDrawingContext(g2d, this, screenTransform, boardTransform);

        //then apply draw on all layers
        g2d.setTransform(screenTransform);
        for (Layer layer : backScreenLayers) {
            layer.draw(layerDrawingContext);
        }

        g2d.setTransform(boardTransform);

        if (isIsometric()) {
            BoardLayerDrawingContext elayerDrawingContext = new BoardLayerDrawingContext(g2d, this, screenTransform, boardTransform);
            for (BoardLayer layer : backgroundLayers) {
                layer.initDrawTiles(elayerDrawingContext);
            }
            List<Tile> screenTilesList = findDisplayTiles();
            Tile[] screenTilesArr = screenTilesList.toArray(new Tile[screenTilesList.size()]);
            Arrays.sort(screenTilesArr, TileDrawingComparator.INSTANCE);
            for (Tile tile : screenTilesArr) {
                elayerDrawingContext.setTile(tile);
                for (BoardLayer layer : backgroundLayers) {
                    layer.drawTile(elayerDrawingContext);
                }
            }
            for (Layer layer : skyLayers) {
                layer.draw(layerDrawingContext);
            }
        } else {
            for (Layer layer : backgroundLayers) {
                layer.draw(layerDrawingContext);
            }
            for (Layer layer : skyLayers) {
                layer.draw(layerDrawingContext);
            }
        }

        g2d.setTransform(screenTransform);
        for (Layer layer : frontScreenLayers) {
            layer.draw(layerDrawingContext);
        }
        g2d.dispose();

        this.frameImage = _frameImage;
    }

    private Sprite[] removeSprites(int z1, int z2, Collection<Sprite> sprites) {
        Collection<Sprite> a = new ArrayList<Sprite>();
        for (Iterator<Sprite> i = sprites.iterator(); i.hasNext(); ) {
            Sprite sprite = i.next();
            if (sprite.getLayer() >= z1 && sprite.getLayer() <= z2) {
                i.remove();
                a.add(sprite);
            }
        }
        return a.toArray(new Sprite[a.size()]);
    }

    private SceneComponentLayout[] removeComponents(int z1, int z2, Collection<SceneComponentLayout> componentsColl) {
        Collection<SceneComponentLayout> a = new ArrayList<SceneComponentLayout>();
        for (Iterator<SceneComponentLayout> i = componentsColl.iterator(); i.hasNext(); ) {
            SceneComponentLayout component = i.next();
            if (component.getLayer() >= z1 && component.getLayer() <= z2) {
                i.remove();
                a.add(component);
            }
        }
        return a.toArray(new SceneComponentLayout[a.size()]);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void installExtension(SceneExtension sceneExtension) {
        if (sceneExtension != null) {
            String k = sceneExtension.getClass().getName();
            if (sceneExtensions.containsKey(k)) {
                throw new IllegalArgumentException("Extension already installed");
            }
            sceneExtensions.put(k, sceneExtension);
            sceneExtension.install(this);
        }
    }

    @Override
    public void uninstallExtension(String sceneViewExtensionId) {
        if (sceneViewExtensionId != null) {
            SceneExtension removed = sceneExtensions.remove(sceneViewExtensionId);
            if (removed != null) {
                removed.uninstall(this);
            }
        }
    }

    @Override
    public void uninstallExtension(Class sceneExtensionType) {
        if (sceneExtensionType != null) {
            uninstallExtension(sceneExtensionType.getName());
        }
    }

    @Override
    public boolean containsExtension(String sceneViewExtensionId) {
        if (sceneViewExtensionId != null) {
            return sceneExtensions.containsKey(sceneViewExtensionId);
        }
        return false;
    }

    @Override
    public boolean containsExtension(Class sceneViewExtensionType) {
        if (sceneViewExtensionType != null) {
            return containsExtension(sceneViewExtensionType.getName());
        }
        return false;
    }

    @Override
    public SceneExtension[] getSceneExtensions() {
        return sceneExtensions.values().toArray(new SceneExtension[sceneExtensions.size()]);
    }

    @Override
    public boolean containsLayer(Layer layer) {
        for (IndexLayer e : indexedLayers) {
            if (e.layer.equals(layer)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsLayer(Class clazz) {
        for (IndexLayer e : indexedLayers) {
            if (clazz.isInstance(e.layer)) {
                return true;
            }
        }
        return false;
    }

    public final void addLayer(Layer layer) {
        if (layer instanceof InternalLayer) {
            throw new IllegalArgumentException("Invalid Layer " + layer);
        }
        IndexLayer[] layers1 = indexedLayers;
        int length = layers1.length;
        IndexLayer[] layers2 = new IndexLayer[layers1.length + 1];
        System.arraycopy(layers1, 0, layers2, 0, length);
        layers2[length] = new IndexLayer(layer);
        for (int i = 0; i < layers2.length; i++) {
            layers2[i].index = i;
        }
        Arrays.sort(layers2);
        indexedLayers = layers2;
        layer.setScene(this);
    }

    public void removeLayer(Layer a) {
        ArrayList<IndexLayer> aa = new ArrayList<IndexLayer>();
        int index = 0;
        Layer removedLayer = null;
        for (IndexLayer e : indexedLayers) {
            if (!e.layer.equals(a)) {
                e.index = index;
                index++;
                aa.add(e);
            } else {
                removedLayer = e.layer;
            }
        }
        indexedLayers = aa.toArray(new IndexLayer[aa.size()]);
        if (removedLayer != null) {
            removedLayer.setScene(null);
        }
    }

    @Override
    public <T extends Layer> T getLayer(Class<T> type) {
        IndexLayer[] layers1 = indexedLayers;
        Layer[] allLayers = new Layer[layers1.length];
        for (int i = 0; i < allLayers.length; i++) {
            Layer layer = layers1[i].layer;
            if (type.isInstance(layer)) {
                return (T) layer;
            }
        }
        return null;
    }

    public Layer[] getLayers() {
        IndexLayer[] layers1 = indexedLayers;
        Layer[] allLayers = new Layer[layers1.length];
        for (int i = 0; i < allLayers.length; i++) {
            allLayers[i] = layers1[i].layer;
        }
        return allLayers;
    }

    public JComponent toComponent() {
        return component;
    }

    @Override
    public Object getViewBinding(Class model) {
        Object v = viewForModelType.get(model);
        if (v == null) {
            throw new IllegalArgumentException("No View for " + model);
        }
        return v;
    }

    @Override
    public void setViewBinding(Class model, Object view) {
        if (view == null) {
            viewForModelType.remove(model);
        } else {
            viewForModelType.put(model, view);
        }
    }

    @Override
    public void setSpriteView(Class<? extends Sprite> sprite, SpriteView view) {
        if (view == null) {
            spriteViewForModel.remove(sprite);
        } else {
            spriteViewForModel.put(sprite, view);
        }
    }

    @Override
    public void setSpriteView(int spriteId, SpriteView view) {
        if (view == null) {
            spriteViewForModelId.remove(spriteId);
        } else {
            spriteViewForModelId.put(spriteId, view);
        }
    }

    @Override
    public void setSpriteView(String spriteKind, SpriteView view) {
        if (view == null) {
            spriteViewForModelKind.remove(spriteKind);
        } else {
            spriteViewForModelKind.put(spriteKind, view);
        }
    }

    public SpriteView getSpriteView(Sprite sprite) {
        SpriteView v = (SpriteView) spriteViewForModelId.get(sprite.getId());
        if (v != null) {
            return v;
        }
        v = (SpriteView) spriteViewForModelKind.get(sprite.getKind());
        if (v != null) {
            return v;
        }
        v = (SpriteView) spriteViewForModel.get(sprite.getClass());
        if (v != null) {
            return v;
        }
        return fallbackSpriteView;
    }

//    public void setCamera(RatioViewBox ratioBox) {
//        ViewDimension md = getSceneSize();
//        setCamera(new ViewBox(
//                (int) (ratioBox.getX() * ratioBox.getWidth()),
//                (int) (ratioBox.getY() * ratioBox.getHeight()),
//                (int) (md.getWidth() * ratioBox.getWidth()),
//                (int) (md.getHeight() * ratioBox.getHeight())));
//    }


    @Override
    public ViewBox getCameraScreen() {
        return getAbsoluteCamera().getDimensionBox();
    }

    @Override
    public ViewBox getSceneScreen() {
        return new ViewBox(new ViewPoint(0, 0), getSceneSize());
    }

    @Override
    public ViewDimension getAbsoluteCameraSize() {
        return getAbsoluteCamera().getSize();
    }

    @Override
    public void setCameraSize(RatioDimension ratioDimension) {
        ViewDimension md = getSceneSize();
        RatioViewBox c = getCamera();
        if (c == null) {
            setCamera(
                    new RatioViewBox(
                            0, 0, 0,
                            ratioDimension.getWidth(), ratioDimension.getHeight(), ratioDimension.getAltitude()
                    )
            );
        } else {
            setCamera(
                    validateRatioViewBox(new RatioViewBox(
                            c.getX(), c.getY(), c.getZ(),
                            ratioDimension.getWidth(), ratioDimension.getHeight(), ratioDimension.getAltitude()
                    )));
        }
    }

    @Override
    public ViewDimension getSceneSize() {
        return getModel().getSceneSize();
    }

    @Override
    public RatioViewBox getCamera() {
        return getModel().getCamera();
    }

    @Override
    public ViewBox getAbsoluteCamera() {
        RatioViewBox camera = getModel().getCamera();
        ViewBox sceneScreen = getSceneScreen();
        return new ViewBox(
                (int) (sceneScreen.getX() + camera.getX() * sceneScreen.getWidth()),
                (int) (sceneScreen.getY() + camera.getY() * sceneScreen.getHeight()),
                (int) (sceneScreen.getZ() + camera.getZ() * sceneScreen.getAltitude()),
                (int) (camera.getWidth() * sceneScreen.getWidth()),
                (int) (camera.getHeight() * sceneScreen.getHeight()),
                (int) (camera.getAltitude() * sceneScreen.getAltitude())
        );
    }

    @Override
    public void setAbsoluteCameraSize(ViewDimension rect) {
        ViewBox c = getAbsoluteCamera();
        setAbsoluteCamera(new ViewBox(
                c.getX(),
                c.getY(),
                c.getZ(),
                rect.getWidth(),
                rect.getHeight(),
                rect.getAltitude()
        ));
    }

    public void setAbsoluteCamera(ViewBox rect) {
        ViewBox s = getSceneScreen();
        RatioViewBox r = new RatioViewBox(
                (float)((double)rect.getX() / s.getWidth()),
                (float)((double)rect.getY() / s.getHeight()),
                (float)((double)rect.getZ() / s.getAltitude()),
                (float)((double)rect.getWidth() / s.getWidth()),
                (float)((double)rect.getHeight() / s.getHeight()),
                (float)((double)rect.getAltitude() / s.getAltitude())
        );
        setCamera(r);
    }

    public void setCamera(RatioViewBox rect) {
        //validateRatioViewBox(rect)
        getModel().setCamera(rect);
    }

    @Override
    public java.util.List<Tile> findDisplayTiles() {
        if (isIsometric()) {
            return getSceneEngine().findTiles(getPolygonAbsoluteCameraModel());
        } else {
            //this is faster
            return getSceneEngine().findTiles(getAbsoluteCameraModelBox());
        }
    }

    public List<Tile> findTiles(ModelBox modelBox) {
        if (isIsometric()) {
            return getSceneEngine().findTiles(toPath2D(modelBox));
        } else {
            //this is faster
            return getSceneEngine().findTiles(modelBox);
        }
    }

    public List<Tile> findTiles(ViewBox viewBox) {
        return getSceneEngine().findTiles(toModelBox(viewBox));
    }

    public Path2D toPath2D(ModelBox modelBox) {
        ModelDimension s = getSceneEngine().getSize();
        if (isIsometric()) {
            ModelBox screen = new ModelBox(-modelBox.getMinX(), -modelBox.getMinY(), s.getWidth(), s.getHeight());
            AffineTransform doIso = AtomUtils.createIsometricTransformInverse(modelBox);
            Path2D p = new Path2D.Double(doIso.createTransformedShape(modelBox.getDimensionBox().toRectangleDouble()));
            Area a = new Area(p);
            a.intersect(new Area(new Path2D.Double(screen.toRectangleDouble())));
            a.transform(AffineTransform.getTranslateInstance(modelBox.getMinX(), modelBox.getMinY()));
            return new Path2D.Double(a);
        } else {
            return new Path2D.Double(modelBox.toRectangleDouble());
        }
    }

    @Override
    public Path2D getPolygonAbsoluteCameraModel() {
        ViewBox v = getAbsoluteCamera();
        SceneModel m = getModel();
        ViewDimension td = m.getTileSize();
        double w = v.getWidth();
        double h = v.getHeight();
        double x = v.getX();
        double y = v.getY();
        double tw = td.getWidth();
        double th = td.getHeight();
        ModelBox cam = new ModelBox(
                x / tw,
                y / th,
                w / tw,
                h / th);
        return toPath2D(cam);
    }

    @Override
    public Path2D getPolygonAbsoluteCamera() {
        ViewBox cam = getAbsoluteCamera();
        ViewDimension s = getSceneSize();
        if (isIsometric()) {
            ViewBox screen = new ViewBox(-cam.getMinX(), -cam.getMinY(), s.getWidth(), s.getHeight());
            AffineTransform doIso = AtomUtils.createIsometricTransformInverse(cam);
            Path2D p = new Path2D.Double(doIso.createTransformedShape(cam.getDimensionBox().toRectangleDouble()));
            Area a = new Area(p);
            a.intersect(new Area(new Path2D.Double(screen.toRectangleDouble())));
            return new Path2D.Double(a);
        } else {
            return new Path2D.Double(cam.getDimensionBox().toRectangleDouble());
        }
    }

    @Override
    public ModelBox getAbsoluteCameraModelBox() {
        Rectangle2D r = getPolygonAbsoluteCameraModel().getBounds2D();
        ModelBox mb = new ModelBox(
                r.getMinX(),
                r.getMinY(),
                r.getWidth(),
                r.getHeight());
        cameraModel = mb;
        return cameraModel;
    }

    @Override
    public void setCameraLocation(RatioPoint ratioPoint) {
        ViewDimension gm = getSceneSize();
        setAbsoluteCameraLocation(new ViewPoint(
                (int) (gm.getWidth() * ratioPoint.getX()),
                (int) (gm.getHeight() * ratioPoint.getY()),
                (int) (gm.getAltitude() * ratioPoint.getZ())));
    }

    @Override
    public void setCameraLocation(ModelPoint modelLocation) {
        setAbsoluteCameraLocation(toViewPoint(modelLocation));
    }

    @Override
    public void setAbsoluteCameraLocation(ViewPoint point) {
        ViewBox v = getAbsoluteCamera();
        moveAbsoluteCameraBy(point.getX() - v.getX(), point.getY() - v.getY());
    }

    @Override
    public void setCameraLocation(Sprite sprite) {
        ModelBox sbounds = sprite.getBounds();
        SceneModel m = getModel();
        ViewDimension td = m.getTileSize();
        ViewBox srect = new ViewBox(
                (int) (sbounds.getX() * td.getWidth()),
                (int) (sbounds.getY() * td.getHeight()),
                (int) (sbounds.getWidth() * td.getWidth()),
                (int) (sbounds.getHeight() * td.getHeight()));
        ViewBox vp = getAbsoluteCamera();
        ViewBox vp2 = validateViewBox(new ViewBox(srect.getX() - vp.getWidth() / 2, srect.getY() - vp.getHeight() / 2, vp.getWidth(), vp.getHeight()));
        setAbsoluteCamera(vp2);
    }

    @Override
    public ViewBox toViewBox(Sprite sprite) {
        return toViewBox(sprite.getBounds());
    }

    @Override
    public ViewBox toViewBox(Tile tile) {
        return toViewBox(tile.getBounds());
    }

    @Override
    public ViewBox toViewBox(ModelBox modelBox) {
        ViewBox viewRectangle = getAbsoluteCamera();
        SceneModel m = getModel();
        ViewDimension tileDimension = m.getTileSize();
        return new ViewBox(
                (int) (modelBox.getX() * tileDimension.getWidth() - viewRectangle.getX()),
                (int) (modelBox.getY() * tileDimension.getHeight() - viewRectangle.getY()),
                (int) (modelBox.getY() * tileDimension.getAltitude() - viewRectangle.getZ()),
                (int) (modelBox.getWidth() * tileDimension.getWidth()),
                (int) (modelBox.getHeight() * tileDimension.getHeight()),
                (int) (modelBox.getAltitude() * tileDimension.getAltitude()));
    }

    public ViewPoint toIsometricViewPoint(ViewPoint point) {
        if (isIsometric()) {
            ViewBox vp = getAbsoluteCamera();
            vp = new ViewBox(0, 0, vp.getWidth(), vp.getHeight());
            int x = point.getX();
            int y = point.getY();
            AffineTransform mapTransform = AtomUtils.createIsometricTransformInverse(vp);
            Point dest = new Point();
//            dest = mapTransform.createTransformedShape(new Rectangle(x, y, x + 1, y + 1)).getBounds().getLocation();
            mapTransform.transform(new Point(x, y), dest);
            return new ViewPoint(dest);
        } else {
            return point;
        }
    }

    public ViewPoint toViewPoint(ModelPoint point) {
        ViewBox viewRectangle = getAbsoluteCamera();
        SceneModel sceneModel1 = getModel();
        ViewDimension tileDimension = sceneModel1.getTileSize();
        return new ViewPoint(
                (int) (point.getX() * tileDimension.getWidth() - viewRectangle.getX()),
                (int) (point.getY() * tileDimension.getHeight() - viewRectangle.getY()),
                (int) (point.getZ() * tileDimension.getAltitude() - viewRectangle.getZ()));
    }

    public ModelBox toModelBox(ViewBox rectangle) {
        ViewBox vp = getAbsoluteCamera();
        int x0 = rectangle.getX();
        int y0 = rectangle.getY();
        int w0 = rectangle.getWidth();
        int h0 = rectangle.getHeight();
        ViewDimension tileDimension = getModel().getTileSize();
        double x = (x0 + vp.getX()) / tileDimension.getWidth();
        double y = (y0 + vp.getY()) / tileDimension.getHeight();
        double w = (w0) / tileDimension.getWidth();
        double h = (h0) / tileDimension.getHeight();
        return new ModelBox(x, y, w, h);
    }

    public ModelPoint toModelPoint(ViewPoint point) {
        ViewBox vp = getAbsoluteCamera();
        int x0 = point.getX();
        int y0 = point.getY();
        ViewDimension tileSize = getModel().getTileSize();
        double x = (x0 + vp.getX()) / tileSize.getWidth();
        double y = (y0 + vp.getY()) / tileSize.getHeight();
        return new ModelPoint(x, y);
    }

    @Override
    public List<Sprite> findDisplaySprites() {
        return getSceneEngine().findSprites(getAbsoluteCameraModelBox());
    }

    @Override
    public Tile findTile(ModelPoint point) {
        return getSceneEngine().findTile(point);
    }

    @Override
    public Tile findTile(ViewPoint point) {
        return getSceneEngine().findTile(toModelPoint(point));
    }

    @Override
    public List<Sprite> findSprites(ViewPoint point) {
//        List<Sprite> sprites = findDisplaySprites();
//        for (Iterator<Sprite> i = sprites.iterator(); i.hasNext(); ) {
//            Sprite s = i.next();
//            Shape sh = getSpriteView(s).getShape(s, this);
//            if (!sh.contains(point.toPoint())) {
//                i.remove();
//            }
//        }
//        return sprites;
        Tile t = findTile(point);
        if (t != null) {
            return getSceneEngine().findSprites(t.getBounds());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public int getEventPlayer(ModelPoint modelPoint, ViewPoint viewPoint, int clickCount, int button, int modifiers) {
        Player p = getControlPlayer();
        return p != null ? p.getId() : -1;
    }

    @Override
    public int getEventPlayer(int keyCode, int keyChar, int keyLocation) {
        Player p = getControlPlayer();
        return p != null ? p.getId() : -1;
    }

    @Override
    public boolean isWithinScreen(Tile tile) {
        return isWithinScreen(toViewBox(tile.getBounds()));
    }

    @Override
    public boolean isWithinScreen(ViewBox bounds) {
        ViewBox vp = getAbsoluteCamera();
        if (isIsometric()) {
            vp = new ViewBox(0, 0, vp.getWidth(), vp.getHeight());
            AffineTransform isometricTransform = AtomUtils.createIsometricTransform(vp);
            int bx = bounds.getX();
            int by = bounds.getY();
            int bz = bounds.getZ();
            int bw = bounds.getWidth();
            int bh = bounds.getHeight();
            ViewPoint[] points = new ViewPoint[]{
                    new ViewPoint(bx, by, bz),
                    new ViewPoint(bx + bw, by, bz),
                    new ViewPoint(bx, by + bh, bz),
                    new ViewPoint(bx + bw, by + bh, bz)
            };
            Point p0 = new Point();
            for (ViewPoint point : points) {
                isometricTransform.transform(point.toPoint(), p0);
                if (p0.x >= 0 && p0.x < vp.getWidth() && p0.x >= 0 && p0.x < vp.getWidth()) {
                    return true;
                }
            }
            return false;
        } else {
            return bounds.toRectangle().intersects(new Rectangle(0, 0, vp.getWidth(), vp.getHeight()));
        }
    }

    @Override
    public boolean isWithinScreen(Sprite sprite) {
        Shape shape = getSpriteView(sprite).getShape(sprite, this);
        ViewBox vp = getAbsoluteCamera();
        if (isIsometric()) {
            vp = new ViewBox(0, 0, vp.getWidth(), vp.getHeight());
            AffineTransform isometricTransform = AtomUtils.createIsometricTransform(vp);
            ViewBox bounds = new ViewBox(shape.getBounds());
            ViewPoint[] points = new ViewPoint[]{
                    new ViewPoint(bounds.getX(), bounds.getY(), bounds.getZ()),
                    new ViewPoint(bounds.getX() + bounds.getWidth(), bounds.getY(), bounds.getZ()),
                    new ViewPoint(bounds.getX(), bounds.getY() + bounds.getHeight(), bounds.getZ()),
                    new ViewPoint(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight(), bounds.getZ())
            };
            Point p0 = new Point();
            for (ViewPoint point : points) {
                isometricTransform.transform(point.toPoint(), p0);
                if (p0.x >= 0 && p0.x < vp.getWidth() && p0.x >= 0 && p0.x < vp.getWidth()) {
                    return true;
                }
            }
            return false;
        } else {
            return shape.intersects(new Rectangle(0, 0, vp.getWidth(), vp.getHeight()));
        }
    }

    //    public boolean scrollViewPort(double modelDx, double modelDy) {
//        int viewDx = (int) (modelDx * getTileWidth());
//        int viewDy = (int) (modelDy * getTileHeight());
//        return scrollViewPort(viewDx, viewDy);
//    }
    public boolean moveCameraTo(Sprite sprite) {
        setCameraLocation(sprite);
        return true;
//        return moveViewPort(vpdx, vpdy);
    }

    protected ViewBox validateViewBox(ViewBox r) {
        int x = r.getX();
        int y = r.getY();
        int width = r.getWidth();
        int height = r.getHeight();
        ViewDimension gameDimension = getSceneSize();
        int gw = gameDimension.getWidth();
        int gh = gameDimension.getHeight();

        if (width > gw) {
            width = gw;
        }
        if (height > gh) {
            height = gh;
        }
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x + width > gw) {
            x = gw - width;
        }
        if (y + height > gh) {
            y = gh - height;
        }
        return new ViewBox(x, y, width, height);
    }

    protected RatioViewBox validateRatioViewBox(RatioViewBox r) {
        float x = r.getX();
        float y = r.getY();
        float z = r.getZ();
        float width = r.getWidth();
        float height = r.getHeight();
        float altitude = r.getAltitude();

        if (x + width > 1) {
            x = 1 - width;
        }
        if (y + height > 1) {
            y = 1 - height;
        }
        if (z + altitude > 1) {
            z = 1 - altitude;
        }
        return new RatioViewBox(x, y, z, width, height, altitude);
    }

    @Override
    public boolean moveAbsoluteCameraBy(int dx, int dy) {
        ViewBox v = getAbsoluteCamera();
        ViewBox rectangle = new ViewBox(v.getX() + dx, v.getY() + dy, v.getWidth(), v.getHeight());
        if (!getSceneScreen().intersects(rectangle)) {
            return false;
        }
        setAbsoluteCamera(rectangle);
        return true;
    }

    /**
     * returns the box needed for drawing a sprite. The drawing takes into
     * consideration sprite height (spriteTilesBox.getAltitude())
     *
     * @param screenTransform
     * @param spriteTilesBox
     * @param tilesAligned
     * @return
     */
    public ViewBox getScreenSpriteBounds(ViewBox spriteTilesBox, boolean tilesAligned, AffineTransform screenTransform) {

        ViewBox vp = getCameraScreen();
        AffineTransform t0 = getScreenAffineTransform();

        AffineTransform mapTransform = null;
        if (isIsometric()) {
            AffineTransform isoTransform = (AffineTransform) (t0 == null ? screenTransform : t0).clone();
            isoTransform.concatenate(AtomUtils.createIsometricTransform(vp));
            mapTransform = isoTransform;
        } else {
            mapTransform = (t0 == null ? screenTransform : t0);
        }
        ViewBox spriteBounds;
//        ViewBox tilesBounds;
        if (!tilesAligned) {
            Shape transformedShape = mapTransform.createTransformedShape(spriteTilesBox.toRectangle());

            ViewPoint isoCenter = AtomUtils.findParallellogrammeCenter(transformedShape);
            spriteBounds = new ViewBox(
                    (int) (isoCenter.getX() - spriteTilesBox.getWidth() / 2),
                    (int) (isoCenter.getY() - spriteTilesBox.getAltitude()),
                    spriteTilesBox.getWidth(),
                    spriteTilesBox.getAltitude());
//            tilesBounds = new ViewBox(transformedShape.getBounds());
        } else {
            spriteBounds = spriteTilesBox;
//            tilesBounds = spriteTilesBox;
        }
        return spriteBounds;
    }

    @Override
    public ViewBox getLayoutBox(ViewBox viewBox, boolean isometricShape, SceneLayoutType boundsType, AffineTransform screenTransform) {

        ViewBox vp = getCameraScreen();
        AffineTransform t0 = getScreenAffineTransform();

        AffineTransform mapTransform = null;
        boolean isometricScene = isIsometric();
        if (isometricScene) {
            AffineTransform isoTransform = (t0 == null ? screenTransform : t0);
            if (isoTransform != null) {
                isoTransform = (AffineTransform) isoTransform.clone();
            }
            if (isoTransform == null) {
                isoTransform = AtomUtils.createIsometricTransform(vp);
            } else {
                isoTransform.concatenate(AtomUtils.createIsometricTransform(vp));
            }
            mapTransform = isoTransform;
        } else {
            mapTransform = (t0 == null ? screenTransform : t0);
        }
        ViewBox spriteBounds;
        ViewBox tilesBounds;
        if (isometricScene && !isometricShape) {
            Rectangle pSrc = viewBox.toRectangle();
            Shape transformedShape = mapTransform == null ? pSrc : mapTransform.createTransformedShape(pSrc);

            ViewPoint isoCenter = AtomUtils.findParallellogrammeCenter(transformedShape);
            tilesBounds = new ViewBox(transformedShape.getBounds());
            int altitude = (int) (1.5 * viewBox.getAltitude() / viewBox.getHeight() * tilesBounds.getHeight());
            spriteBounds = new ViewBox(
                    (int) (isoCenter.getX() - viewBox.getWidth() / 2),
                    (int) (isoCenter.getY() - altitude),
                    viewBox.getWidth(),
                    viewBox.getAltitude());
        } else {
            spriteBounds = viewBox;
            tilesBounds = viewBox;
        }
        switch (boundsType) {
            case FULL_BOUNDS: {
                int maxy = Math.max(tilesBounds.getMaxY(), spriteBounds.getMaxY());
                int miny = Math.min(tilesBounds.getMinY(), spriteBounds.getMinY());
                int maxx = Math.max(tilesBounds.getMaxX(), spriteBounds.getMaxX());
                int minx = Math.min(tilesBounds.getMinX(), spriteBounds.getMinX());
                int h = maxy - miny;
                int w = maxx - minx;
                return new ViewBox(minx, miny, 0, w, h, 1);
            }
            case SPRITE_BASE_LINE: {
                return spriteBounds;
            }
            case SPRITE_DESCENT_LINE: {
                int maxy = Math.max(tilesBounds.getMaxY(), spriteBounds.getMaxY());
                int miny = Math.min(tilesBounds.getMinY(), spriteBounds.getMinY());
                int h = maxy - miny;
                return new ViewBox(spriteBounds.getX(), miny, 0, spriteBounds.getWidth(), h, 1);
            }
            case TILE_BOUNDS: {
                return tilesBounds;
            }
        }
        return tilesBounds;
    }

    @Override
    public ViewBox getLayoutBox(Sprite sprite) {
        SpriteView spriteView = getSpriteView(sprite);
        SpriteViewConstraints c = spriteView.getSpriteViewConstraints(sprite);
        ViewBox b = getLayoutBox(toViewBox(sprite), c.isIsometric(), c.getSceneLayoutType(), getScreenAffineTransform());
        ViewPoint m = c.getMargin();
        if (m != null) {
            return new ViewBox(
                    b.getX() + m.getX(),
                    b.getY() + m.getY(),
                    b.getZ() + m.getZ(),
                    b.getWidth(),
                    b.getHeight(),
                    b.getAltitude()
            );
        }
        return b;
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

    public AffineTransform getScreenAffineTransform() {
        return screenAffineTransform;
    }

    public void setScreenAffineTransform(AffineTransform screenAffineTransform) {
        AffineTransform old = this.screenAffineTransform;
        this.screenAffineTransform = screenAffineTransform;
        propertyChangeSupport.firePropertyChange("screenAffineTransform", old, screenAffineTransform);
    }

    public AffineTransform getBoardAffineTransform() {
        return boardAffineTransform;
    }

    public void setBoardAffineTransform(AffineTransform boardAffineTransform) {
        AffineTransform old = this.boardAffineTransform;
        this.boardAffineTransform = boardAffineTransform;
        propertyChangeSupport.firePropertyChange("boardAffineTransform", old, boardAffineTransform);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String old = this.title;
        this.title = title;
        propertyChangeSupport.firePropertyChange("title", old, title);
    }

    public String getNextFocusComponent(String componentName) {
        java.util.List<String> allFocusable = getFocusableComponents();
        int i = allFocusable.indexOf(componentName);
        if (i < 0) {
            //current is not focusable
            java.util.List<String> all = new ArrayList<String>(namedComponents.keySet());
            i = all.indexOf(componentName);
            if (i < 0) {
                if (allFocusable.size() > 0) {
                    return allFocusable.get(0);
                } else {
                    return null;
                }
            } else {
                boolean found = false;
                for (int j = i + 1; j < all.size(); j++) {
                    String nextCompName = all.get(j);
                    SceneComponent c = namedComponents.get(nextCompName).getComponent();
                    if (c.isVisible() && c.isFocusable()) {
                        if (found) {
                            return nextCompName;
                        }
                        if (nextCompName.equals(componentName)) {
                            found = true;
                        }
                    }
                }
                if (allFocusable.size() > 0) {
                    return allFocusable.get(0);
                }
                return null;
            }
        } else {
            int size = allFocusable.size();
            if (i < size - 1) {
                return allFocusable.get(i + 1);
            } else if (size == 0) {
                return null;
            } else {
                return allFocusable.get(0);
            }
        }
    }

    public String getPreviousFocusComponent(String componentName) {
        java.util.List<String> allFocusable = getFocusableComponents();
        int i = allFocusable.indexOf(componentName);
        if (i < 0) {
            //current is not focusable
            java.util.List<String> all = new ArrayList<String>(namedComponents.keySet());
            i = all.indexOf(componentName);
            if (i < 0) {
                if (allFocusable.size() > 0) {
                    return allFocusable.get(0);
                } else {
                    return null;
                }
            } else {
                boolean found = false;
                for (int j = all.size() - 1; j > 0; j--) {
                    String nextCompName = all.get(j);
                    SceneComponent c = namedComponents.get(nextCompName).getComponent();
                    if (c.isVisible() && c.isFocusable()) {
                        if (found) {
                            return nextCompName;
                        }
                        if (nextCompName.equals(componentName)) {
                            found = true;
                        }
                    }
                }
                if (allFocusable.size() > 0) {
                    return allFocusable.get(allFocusable.size() - 1);
                }
                return null;
            }
        } else {
            int size = allFocusable.size();
            if (i > 0) {
                return allFocusable.get(i - 1);
            } else if (size == 0) {
                return null;
            } else {
                return allFocusable.get(size - 1);
            }
        }
    }

    public java.util.List<String> getFocusedComponents() {
        java.util.List<String> found = new ArrayList<String>();
        for (Map.Entry<String, SceneComponentLayout> sceneComponentLayout : namedComponents.entrySet()) {
            SceneComponent c = sceneComponentLayout.getValue().getComponent();
            if (c.isVisible() && c.isFocusable() && c.hasFocus()) {
                found.add(sceneComponentLayout.getKey());
            }
        }
        return found;
    }

    public java.util.List<String> getFocusableComponents() {
        java.util.List<String> found = new ArrayList<String>();
        for (Map.Entry<String, SceneComponentLayout> sceneComponentLayout : namedComponents.entrySet()) {
            SceneComponent c = sceneComponentLayout.getValue().getComponent();
            if (c.isVisible() && c.isFocusable()) {
                found.add(sceneComponentLayout.getKey());
            }
        }
        return found;
    }

    public <T extends  SceneComponent> T getComponent(String name) {
        SceneComponentLayout t = namedComponents.get(name);
        if (t == null) {
            throw new NoSuchElementException("Componnent not found " + name);
        }
        return (T) t.getComponent();
    }

    @Override
    public long getFrame() {
        return getSceneEngine().getFrame();
    }

    public ImageProducer getImageProducer() {
        return imageProducer;
    }

    public void setImageProducer(ImageProducer imageProducer) {
        ImageProducer old = this.imageProducer;
        if (!Objects.equals(old, imageProducer)) {
            this.imageProducer = imageProducer;
            for (SceneChangeListener listener : sceneChangeListeners) {
                listener.imageProducerManagerChanged(this, old, imageProducer);
            }
            propertyChangeSupport.firePropertyChange("imageProducer", old, imageProducer);
        }
    }

    private static class IndexLayer implements Comparable<IndexLayer> {

        private Layer layer;
        private int index;

        public IndexLayer(Layer layer) {
            this.layer = layer;
        }

        public int compareTo(IndexLayer o) {
            int a = layer.getLayer();
            int b = o.layer.getLayer();
            int x = a < b ? -1 : a > b ? 1 : 0;
            if (x == 0) {
                x = index - o.index;
            }
            return x;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    private static class MouseEventRunnable implements Runnable {

        SceneMouseEvent e;
        MouseEventType t;
        private java.util.List<SceneController> listeners;

        public MouseEventRunnable(SceneMouseEvent e, MouseEventType t, java.util.List<SceneController> listeners) {
            this.e = e;
            this.t = t;
            this.listeners = listeners;
        }

        public void run() {
            switch (t) {
                case MOUSE_CLICKED: {
                    for (SceneController listener : listeners) {
                        if (listener.acceptEvent(SceneController.MOUSE_CLICKED)) {
                            listener.mouseClicked(e);
                            if (e.isDisabled()) {
                                break;
                            }
                        }
                    }
                    break;
                }
                case MOUSE_ENTERED: {
                    for (SceneController listener : listeners) {
                        if (listener.acceptEvent(SceneController.MOUSE_ENTERED)) {
                            listener.mouseEntered(e);
                            if (e.isDisabled()) {
                                break;
                            }
                        }
                    }
                    break;
                }
                case MOUSE_EXITED: {
                    for (SceneController listener : listeners) {
                        if (listener.acceptEvent(SceneController.MOUSE_EXITED)) {
                            listener.mouseExited(e);
                            if (e.isDisabled()) {
                                break;
                            }
                        }
                    }
                    break;
                }
                case MOUSE_PRESSED: {
                    for (SceneController listener : listeners) {
                        if (listener.acceptEvent(SceneController.MOUSE_PRESSED)) {
                            listener.mousePressed(e);
                            if (e.isDisabled()) {
                                break;
                            }
                        }
                    }
                    break;
                }
                case MOUSE_RELEASED: {
                    for (SceneController listener : listeners) {
                        if (listener.acceptEvent(SceneController.MOUSE_RELEASED)) {
                            listener.mouseReleased(e);
                            if (e.isDisabled()) {
                                break;
                            }
                        }
                    }
                    break;
                }
                case MOUSE_DRAGGED: {
                    for (SceneController listener : listeners) {
                        if (listener.acceptEvent(SceneController.MOUSE_DRAGGED)) {
                            listener.mouseDragged(e);
                            if (e.isDisabled()) {
                                break;
                            }
                        }
                    }
                    break;
                }
                case MOUSE_MOVED: {
                    for (SceneController listener : listeners) {
                        if (listener.acceptEvent(SceneController.MOUSE_MOVED)) {
                            listener.mouseMoved(e);
                            if (e.isDisabled()) {
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }

        @Override
        public String toString() {
            return "MouseEventRunnable{" + "e=" + e + ", t=" + t + '}';
        }
    }

    private static class KeyEventRunnable implements Runnable {

        SceneKeyEvent e;
        KeyEventType t;
        private java.util.List<SceneController> listeners;
        private Map<String, SceneComponentLayout> components;

        public KeyEventRunnable(SceneKeyEvent e, KeyEventType t, java.util.List<SceneController> listeners, Map<String, SceneComponentLayout> components) {
            this.e = e;
            this.t = t;
            this.listeners = listeners;
            this.components = components;
        }

        public void run() {
            switch (t) {
                case KEY_PRESSED: {
                    for (SceneComponentLayout l : components.values()) {
                        SceneComponent c = l.getComponent();
                        if (c.isVisible() && c.isFocusable() && c.hasFocus()) {
                            SceneController ctrl = c.getController();
                            if (ctrl != null) {
                                if (ctrl.acceptEvent(SceneController.KEY_PRESSED)) {
                                    ctrl.keyPressed(e);
                                    if (e.isConsumed()) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!e.isDisabled()) {
                        for (SceneController listener : listeners) {
                            if (listener.acceptEvent(SceneController.KEY_PRESSED)) {
                                listener.keyPressed(e);
                                if (e.isDisabled()) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
                case KEY_RELEASED: {
                    for (SceneComponentLayout l : components.values()) {
                        SceneComponent c = l.getComponent();
                        if (c.isVisible() && c.isFocusable() && c.hasFocus()) {
                            SceneController ctrl = c.getController();
                            if (ctrl != null) {
                                if (ctrl.acceptEvent(SceneController.KEY_RELEASED)) {
                                    ctrl.keyReleased(e);
                                    if (e.isConsumed()) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!e.isDisabled()) {
                        for (SceneController listener : listeners) {
                            if (listener.acceptEvent(SceneController.KEY_RELEASED)) {
                                listener.keyReleased(e);
                                if (e.isDisabled()) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
                case KEY_TYPED: {
                    for (SceneComponentLayout l : components.values()) {
                        SceneComponent c = l.getComponent();
                        if (c.isVisible() && c.isFocusable() && c.hasFocus()) {
                            SceneController ctrl = c.getController();
                            if (ctrl != null) {
                                if (ctrl.acceptEvent(SceneController.KEY_TYPED)) {
                                    ctrl.keyTyped(e);
                                    if (e.isConsumed()) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (!e.isDisabled()) {
                        for (SceneController listener : listeners) {
                            listener.keyTyped(e);
                            if (listener.acceptEvent(SceneController.KEY_TYPED)) {
                                if (e.isDisabled()) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }

        @Override
        public String toString() {
            return "KeyEventRunnable{" + "e=" + e + ", t=" + t + '}';
        }
    }

    private class NativeInputListener implements MouseMotionListener, MouseListener, KeyListener {

        Map<Integer, KeyEvent> pressed = new HashMap<Integer, KeyEvent>();

        public void mouseDragged(MouseEvent e) {
            processMouseEvent(e, MouseEventType.MOUSE_DRAGGED);
        }

        public void mouseMoved(MouseEvent e) {
            processMouseEvent(e, MouseEventType.MOUSE_MOVED);
        }

        public void mouseClicked(MouseEvent e) {
            processMouseEvent(e, MouseEventType.MOUSE_CLICKED);
        }

        public void mousePressed(MouseEvent e) {
            processMouseEvent(e, MouseEventType.MOUSE_MOVED);
        }

        public void mouseReleased(MouseEvent e) {
            processMouseEvent(e, MouseEventType.MOUSE_PRESSED);
        }

        public void mouseEntered(MouseEvent e) {
            processMouseEvent(e, MouseEventType.MOUSE_ENTERED);
        }

        public void mouseExited(MouseEvent e) {
            processMouseEvent(e, MouseEventType.MOUSE_EXITED);
        }

        public void keyTyped(KeyEvent e) {
            processKeyEvent(e, KeyEventType.KEY_TYPED);
        }

        public void keyPressed(KeyEvent e) {
            processKeyEvent(e, KeyEventType.KEY_PRESSED);
        }

        public void keyReleased(KeyEvent e) {
            processKeyEvent(e, KeyEventType.KEY_RELEASED);
        }

        public void processKeyEvent(KeyEvent event, KeyEventType eventType) {
            switch (eventType) {
                case KEY_PRESSED: {
                    //System.out.println(eventType);
                    pressed.put(event.getKeyCode(), event);
                    break;
                }
                case KEY_RELEASED: {
                    //System.out.println("\t"+eventType);
                    pressed.remove(event.getKeyCode());
                    break;
                }
            }
            int[] codes = new int[pressed.size()];
            int x = 0;
            for (Integer integer : pressed.keySet()) {
                codes[x] = integer;
                x++;
            }
            KeyEventExt eventExt = new KeyEventExt(event, codes);
            for (int i = interactiveLayers.length - 1; i >= 0; i--) {
                SceneKeyEvent gEvent = evalKeyEvent(interactiveLayers[i], eventExt, eventType);
                if (gEvent != null) {
                    if (listeners.size() > 0) {
                        java.util.List<SceneController> ok = new ArrayList<SceneController>(listeners);
                        sceneEngine.invokeLater(new KeyEventRunnable(gEvent, eventType, ok, namedComponents));
                    }
                    return;
                }
            }
        }

        public void processMouseEvent(MouseEvent event, MouseEventType eventType) {
            for (int i = interactiveLayers.length - 1; i >= 0; i--) {
                SceneMouseEvent gEvent = evalMouseEvent(interactiveLayers[i], event, eventType);
                if (gEvent != null) {
                    if (listeners.size() > 0) {
                        java.util.List<SceneController> ok = new ArrayList<SceneController>(listeners);
                        sceneEngine.invokeLater(new MouseEventRunnable(gEvent, eventType, ok));
                    }
                    return;
                }
            }
        }

        private SceneMouseEvent evalMouseEvent(InteractiveLayer layer, MouseEvent elayer, MouseEventType eventType) {
            switch (eventType) {
                case MOUSE_CLICKED: {
                    return layer.mouseClicked(elayer);
                }
                case MOUSE_DRAGGED: {
                    return layer.mouseDragged(elayer);
                }
                case MOUSE_ENTERED: {
                    return layer.mouseEntered(elayer);
                }
                case MOUSE_EXITED: {
                    return layer.mouseExited(elayer);
                }
                case MOUSE_MOVED: {
                    return layer.mouseMoved(elayer);
                }
                case MOUSE_PRESSED: {
                    return layer.mousePressed(elayer);
                }
                case MOUSE_RELEASED: {
                    return layer.mouseReleased(elayer);
                }
            }
            return null;
        }

        private SceneKeyEvent evalKeyEvent(InteractiveLayer layer, KeyEventExt event, KeyEventType eventType) {
            switch (eventType) {
                case KEY_PRESSED: {
                    return layer.keyPressed(event);
                }
                case KEY_RELEASED: {
                    return layer.keyReleased(event);
                }
                case KEY_TYPED: {
                    return layer.keyTyped(event);
                }
            }
            return null;
        }
//        private void fireKeyEvent(GameViewEventListener listener, GameViewKeyEvent event, KeyEventType eventType) {
//            switch (eventType) {
//                case KEY_PRESSED: {
//                    listener.keyPressed(event);
//                    break;
//                }
//                case KEY_RELEASED: {
//                    listener.keyReleased(event);
//                    break;
//                }
//                case KEY_TYPED: {
//                    listener.keyTyped(event);
//                    break;
//                }
//            }
//        }
    }

    private class SceneComponentPainter extends JComponent {

        public SceneComponentPainter() {
            setFocusTraversalKeysEnabled(false);
        }

        @Override
        public void paintComponent(Graphics graphics) {
            if (frameImage != null) {
                //AGEDebug.DRAW_IMAGE_DRAW_COUNT++;
                graphics.drawImage(frameImage, 0, 0, this);
            }
        }
    }

    private class FrameBuilder implements SceneEngineFrameListener {

        @Override
        public void modelUpdated(SceneEngine sceneEngine, SceneEngineModel model) {
            if (started) {
                frameStep();
            }
        }
    }

    public void lockCamera(Sprite s) {
        this.cameraLockToSprite = validateSprite(s);
        getSceneEngine().removeSceneFrameListener(cameraLockToSpriteListener);
        getSceneEngine().addSceneFrameListener(cameraLockToSpriteListener);
    }

    public void unlockCamera() {
        this.cameraLockToSprite = null;
        getSceneEngine().removeSceneFrameListener(cameraLockToSpriteListener);
    }


    private Sprite validateSprite(Sprite s) {
        if (s == null) {
            throw new IllegalArgumentException("Null Sprite");
        }
        Sprite s2 = getSceneEngine().getSprite(s.getId());
        if (s == null) {
            throw new IllegalArgumentException("Sprite is not yet registered : id =" + s.getId());
        }
        if (s2 != s) {
            throw new IllegalArgumentException("Invalid Sprite. Id clashing with different sprite " + s.getId());
        }
        return s2;
    }


    @Override
    public Player getPlayer(int id) {
        return getSceneEngine().getPlayer(id);
    }

    @Override
    public List<Player> getPlayers() {
        return getSceneEngine().getPlayers();
    }

    @Override
    public Sprite getSprite(int id) {
        return getSceneEngine().getSprite(id);
    }

    @Override
    public void addControlPlayer(int id) {
        getModel().addControlPlayer(id);
    }

    @Override
    public void addControlPlayer(Player player) {
        Player p = getPlayer(player.getId());
        if (p != player) {//reference comparison
            throw new IllegalArgumentException("Invalid Player reference");
        }
        getModel().addControlPlayer(p.getId());
    }

    @Override
    public void resetControlPlayers() {
        getModel().resetControlPlayers();
    }

    @Override
    public boolean removeControlPlayer(int id) {
        return getModel().removeControlPlayer(id);
    }

    @Override
    public Player getControlPlayer() {
        Integer controlPlayer = getModel().getControlPlayer();
        if (controlPlayer != null) {
            return getPlayer(controlPlayer);
        }
        return null;
    }

    @Override
    public List<Player> getControlPlayers() {
        List<Player> p = new ArrayList<>();
        for (Integer cp : getModel().getControlPlayers()) {
            p.add(getPlayer(cp));
        }
        return p;
    }

    @Override
    public boolean isControlPlayer(int id) {
        return getModel().isControlPlayer(id);
    }

    public void addSceneListener(SceneListener sceneListener) {
        if (sceneListener != null) {
            sceneListeners.add(sceneListener);
        }
    }

    public void removeSceneListener(SceneListener sceneListener) {
        sceneListeners.remove(sceneListener);
    }

    @Override
    public Object getCompanionObject() {
        return companionObject;
    }

    @Override
    public void setCompanionObject(Object companionObject) {
        this.companionObject = companionObject;
    }
}
