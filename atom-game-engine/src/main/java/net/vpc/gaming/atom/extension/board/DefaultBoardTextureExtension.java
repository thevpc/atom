/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package net.vpc.gaming.atom.extension.board;
import net.vpc.gaming.atom.debug.AtomDebug;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.SceneEngineChangeAdapter;
import net.vpc.gaming.atom.engine.SceneEngineChangeListener;
import net.vpc.gaming.atom.extension.DefaultSceneExtension;
import net.vpc.gaming.atom.presentation.*;
import net.vpc.gaming.atom.util.AtomUtils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
import net.vpc.gaming.atom.model.CellDef;
import net.vpc.gaming.atom.model.DefaultSceneEngineModel;
import net.vpc.gaming.atom.model.ModelBox;
import net.vpc.gaming.atom.model.SceneEngineModel;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.model.Tile;
import net.vpc.gaming.atom.model.ViewBox;
import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.layers.BoardLayer;
import net.vpc.gaming.atom.presentation.layers.BoardLayerDrawingContext;
import net.vpc.gaming.atom.presentation.layers.DefaultLayer;
import net.vpc.gaming.atom.presentation.layers.FlatBoardLayer;
import net.vpc.gaming.atom.presentation.layers.InteractiveLayer;
import net.vpc.gaming.atom.presentation.layers.LayerDrawingContext;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultBoardTextureExtension extends DefaultSceneExtension {

    SceneEngineChangeListener sceneEngineChangeListener = new SceneEngineChangeAdapter() {
        @Override
        public void modelChanged(SceneEngine sceneEngine, SceneEngineModel oldValue, SceneEngineModel newValue) {
            reconfigure();
        }
    };
    SceneChangeListener sceneChangeListener = new SceneChangeAdapter() {
        @Override
        public void imageProducerManagerChanged(Scene scene, ImageProducer oldValue, ImageProducer newValue) {
            reconfigure();
        }

    };
    PropertyChangeListener updater = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateImages = true;
        }
    };
    SceneLifeCycleListener sceneEngineChangeListenerInstaller = new SceneLifeCycleListener() {
        @Override
        public void sceneInitialized(Scene scene) {
            scene.getSceneEngine().addSceneEngineChangeListener(sceneEngineChangeListener);
        }
    };
    Comparator<BigTile> tilesComparator;
    private ResizableImage[] downImages;
    private ResizableImage[] upImages;
    private BigTile[][] bigTiles;
    //    private int imageTileWidth;
//    private int imageTileHeight;
    private boolean updateImages=true;
    private Scene scene;
    private FlatTextureInternalBoardLayer baseLayer;
    private OverlappingTextureInternalBoardLayer overlapLayer;


    //    public DefaultBoardTextureExtension(InputStream imageStream) {
//        imageMap = AGEUtils.loadBufferedImage(imageStream);
//    }
//
//    public DefaultBoardTextureExtension(Image[][] images) {
//        this.images = new ResizableImage[images.length][];
//        for (int i = 0; i < images.length; i++) {
//            this.images[i] = new ResizableImage[images[i].length];
//            for (int j = 0; j < images[i].length; j++) {
//                this.images[i][j] = new ResizableImage(images[i][j]);
//            }
//        }
//    }

    public void install(Scene scene) {
        this.scene = scene;
        scene.getModel().addPropertyChangeListener(updater);
        scene.addLifeCycleListener(sceneEngineChangeListenerInstaller);
        scene.addSceneChangeListener(sceneChangeListener);
        if (scene.getSceneEngine() != null) {
            scene.getSceneEngine().addSceneEngineChangeListener(sceneEngineChangeListener);
        }
        reconfigure();
    }

    @Override
    public void uninstall(Scene scene) {
        this.scene = null;
        scene.getModel().removePropertyChangeListener(updater);
        scene.removeSceneChangeListener(sceneChangeListener);
        if (scene.getSceneEngine() != null) {
            scene.getSceneEngine().removeSceneEngineChangeListener(sceneEngineChangeListener);
        }
    }

    public void updateImages(LayerDrawingContext context) {
        if (updateImages) {
            updateImages0(context);
        }
    }

    public void updateImages0(LayerDrawingContext context) {
        updateImages=false;
        Scene scene = context.getScene();
        DefaultSceneEngineModel engineModel = context.getSceneEngine().getModel();
//        try {
//            int iw = imageMap.getWidth();
//            int ih = imageMap.getHeight();

        //int imageColumns = imageMapColumns;// iw / imageTileWidth;
        //int imageRows = imageMapRows;//ih / imageTileHeight;

        int cellColumns = engineModel.getCellWidth();
        int cellRows = engineModel.getCellHeight();


        final int[][] biMap = engineModel.getBoardCellsMatrix();
        final int mapRows = biMap.length;
        final int mapColumns = biMap.length == 0 ? 0 : biMap[0].length;


        if (downImages == null) {
            ImageProducer environmentImageProducer = scene.getImageProducer();
            if (environmentImageProducer == null) {
                DefaultSceneEngineModel model = scene.getSceneEngine().getModel();
                environmentImageProducer = model.getImageProducer();
            }
            if (environmentImageProducer != null) {
                //fullImages = AGEUtils.splitResizableImage(imageMap, imageColumns, imageRows);
                downImages = new ResizableImage[environmentImageProducer.getImagesCount(ImageProducer.BACKGROUND)];
                if (scene.isIsometric()) {
                    upImages = new ResizableImage[downImages.length];
                    for (int i = 0; i < downImages.length; i++) {
                        Image baseImage = environmentImageProducer.getImage(ImageProducer.BACKGROUND, i);
                        int w = baseImage.getWidth(null);
                        int h = baseImage.getHeight(null);
                        Polygon up = new Polygon(new int[]{0, w / 2, w, w, 0}, new int[]{3 * h / 4, 2 * h / 4, 3 * h / 4, 0, 0}, 5);
                        Polygon down = new Polygon(new int[]{0, w / 2, w, w, 0}, new int[]{3 * h / 4, 2 * h / 4, 3 * h / 4, h, h}, 5);

                        BufferedImage upImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D gg = upImage.createGraphics();
                        gg.setClip(up);
                        //AGEDebug.DRAW_IMAGE_DRAW_COUNT++;
                        gg.drawImage(baseImage, 0, 0, null);
                        gg.dispose();

                        upImages[i] = AtomUtils.isAlphaImage(upImage) ? null : new ResizableImage(upImage);
                        if (upImages[i] != null) {
                            upImages[i].setName("CellUp[" + i + "]");
                        }
                        BufferedImage downImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                        gg = downImage.createGraphics();
                        gg.setClip(down);
                        //AGEDebug.DRAW_IMAGE_DRAW_COUNT++;
                        gg.drawImage(baseImage, 0, 0, null);
                        gg.dispose();
                        downImages[i] = new ResizableImage(downImage);
                        downImages[i].setName("CellDown[" + i + "]");

//                downImages[i]=new ResizableImage(baseImage);
//                upImages[i]=null;
                    }

                } else {
                    for (int i = 0; i < downImages.length; i++) {
                        downImages[i] = new ResizableImage(environmentImageProducer.getImage(ImageProducer.BACKGROUND, i));
                    }
                }
            }
            CellDef[] cellDefs = engineModel.getBoardCellDefinitions();
            bigTiles = new BigTile[mapRows][mapColumns];
            for (int cr = 0; cr < mapRows; cr++) {
                for (int cc = 0; cc < mapColumns; cc++) {
                    int bigTileIndex = cc * mapRows + cr;
                    Tile bigTile = new Tile();
                    bigTile.setId(bigTileIndex);
                    int cell = biMap[cr][cc];
                    CellDef cellDef = cellDefs[cell % cellDefs.length];
                    bigTile.setKind(cellDef.getType());
                    bigTile.setRow(cr);
                    bigTile.setColumn(cc);
                    bigTile.setBounds(new ModelBox(cc * cellColumns, cr * cellRows, cellColumns, cellRows));
                    bigTiles[cr][cc] = new BigTile(cellDef,bigTile);
                }
            }
        }
        //AGEUtils.resizeImage(, tileDimension.getWidth(), tileDimension.getHeight())
//        } catch (IOException ex) {
//            throw new RuntimeIOException(ex);
//        }
    }

//    public java.util.List<Tile> toBigTiles(Collection<Tile> tiles) {
//        ArrayList<Tile> newBigTiles = new ArrayList<>();
//        HashSet<Integer> visited = new HashSet<>();
//        int cellColumns = mapInfo.getCellWidth();
//        int cellRows = mapInfo.getCellHeight();
//        for (Tile tile : tiles) {
//            int cc = (int) (tile.getX() / cellColumns);
//            int cr = (int) (tile.getY() / cellRows);
//            Tile bt = bigTiles[cc][cr];
//            int t = bt.getId();
//            if (!visited.contains(t)) {
//                visited.add(t);
//                newBigTiles.add(bt);
//            }
//        }
//        Collections.sort(newBigTiles, tilesComparator);
//        return newBigTiles;
//    }

    private java.util.List<BigTile> findBigTiles(Path2D polygon) {
        Rectangle2D rect = polygon.getBounds2D();
        DefaultSceneEngineModel engineModel = scene.getSceneEngine().getModel();
        int _columns = engineModel.getMapColumns();
        int _rows = engineModel.getMapRows();
        BigTile[][] _tileMatrix = bigTiles;
        int tx = engineModel.getCellWidth();
        int ty = engineModel.getCellHeight();

        int gx0 = (int) Math.floor(rect.getX() / tx);
        int gy0 = (int) Math.floor(rect.getY() / ty);

        int gx1 = (int) Math.floor(((rect.getX() + rect.getWidth()) / tx - 0.001));
        int gy1 = (int) Math.floor(((rect.getY() + rect.getHeight()) / ty - 0.001));
        if ((gx0 < 0 && gx1 < 0) || (gx0 >= _columns && gx1 >= _columns)) {
            return Collections.EMPTY_LIST;
        }
        if ((gy0 < 0 && gy1 < 0) || (gy0 >= _rows && gy1 >= _rows)) {
            return Collections.EMPTY_LIST;
        }
        java.util.List<BigTile> all = new ArrayList<BigTile>();
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
                BigTile bigTile = _tileMatrix[r][c];
                Tile m = bigTile.tile;
                if (polygon.intersects(m.getX(), m.getY(), m.getWidth(), m.getHeight())) {
                    //if(clip.intersects(m.getBounds())){
                    all.add(bigTile);
                    //}
                }
            }
        }
        return all;
    }

    protected void reconfigure() {
        updateImages=true;
        if (scene.getSceneEngine() != null) {
            SceneEngineModel model = scene.getSceneEngine().getModel();
            if (model instanceof DefaultSceneEngineModel) {
                if (!scene.containsLayer(getBaseLayer())) {
                    scene.addLayer(getBaseLayer());
                }
                if (!scene.containsLayer(getOverlapLayer())) {
                    scene.addLayer(getOverlapLayer());
                }
                return;
            }
        }
        if (baseLayer != null) {
            scene.removeLayer(baseLayer);
        }
        if (overlapLayer != null) {
            scene.removeLayer(overlapLayer);
        }
    }

    public OverlappingTextureInternalBoardLayer getOverlapLayer() {
        if (overlapLayer == null) {
            overlapLayer = new OverlappingTextureInternalBoardLayer();
        }
        return overlapLayer;
    }

    public FlatTextureInternalBoardLayer getBaseLayer() {
        if (baseLayer == null) {
            baseLayer = new FlatTextureInternalBoardLayer();
        }
        return baseLayer;
    }

    public Comparator<BigTile> getTilesComparator() {
        if (tilesComparator == null) {
            tilesComparator = new Comparator<BigTile>() {
                @Override
                public int compare(BigTile o1, BigTile o2) {
                    int v = Double.compare(o1.tile.getX(), o2.tile.getX());
                    if (v != 0) {
                        return -v;
                    }
                    v = Double.compare(o1.tile.getY(), o2.tile.getY());
                    if (v != 0) {
                        return v;
                    }
                    return 0;
                }
            };
        }
        return tilesComparator;
    }


    private class FlatTextureInternalBoardLayer extends FlatBoardLayer implements BoardLayer, InteractiveLayer {
        public FlatTextureInternalBoardLayer() {
            setLayer(BACKGROUND_LAYER);
        }

        @Override
        public void draw(LayerDrawingContext context) {
            updateImages(context);
            Graphics2D graphics = context.getGraphics();
            Scene scene = context.getScene();
            java.util.List<BigTile> cells = findBigTiles(scene.getCamera().getModelPolygon());
            Collections.sort(cells, getTilesComparator());
            List<TileInfo> upTiles = new ArrayList<>();
            context.setUserObject("DefaultBoardTextureExtension.TileInfos", upTiles);
            if (downImages != null) {
                AffineTransform screenTransform = context.getScreenTransform();
                AffineTransform mapTransform = context.getBoardTransform();
                AffineTransform oldTransform = graphics.getTransform();
                graphics.setTransform(screenTransform);
                for (BigTile c : cells) {
                    int tileType = c.cell.getType() % downImages.length;
                    ResizableImage downImage = downImages[tileType];
                    ViewBox goodBox = scene.toViewBox(c.tile);//getShape(c, scene);
                    //goodBox = ;
                    Point dest = new Point();
                    mapTransform.transform(new Point((int) goodBox.getCenterX(), (int) goodBox.getCenterY()), dest);
                    goodBox = scene.getLayoutBox(new ViewBox(goodBox.getX(), goodBox.getY(), 0, goodBox.getWidth(), goodBox.getHeight(), goodBox.getHeight()), false, SceneLayoutType.FULL_BOUNDS, null);

                    if (downImage != null) {
                        //if (cc == 1 && cr == 2) {
                        int cellX = goodBox.getX();//int) (w * x - xx);
                        int cellY = goodBox.getY();//(int) (h * y - yy);
                        //force all cells to have the very same dim (double precision problem)
                        int h = goodBox.getHeight();
                        int w = goodBox.getWidth();
                        Image cellImage = null;
                        cellImage = downImage.getImage(w, h);
                        AtomDebug.DRAW_IMAGE_DRAW_COUNT++;
                        graphics.drawImage(cellImage, cellX, cellY, null);
                    }
                    if (upImages != null) {
                        ResizableImage upImage = upImages[tileType];
                        if (upImage != null) {
                            TileInfo e = new TileInfo(c, goodBox);
//                    e.smallTiles=scene.findTiles(e.box);
//                    Collections.sort(e.smallTiles,TileDrawingComparator.INSTANCE);
//                    if(e.smallTiles.size()>0){
//                        e.refSmallTile =e.smallTiles.get(0);
//                    }
                            e.refSmallTile = scene.findTile(e.bigTile.tile.getBounds().getModelPoints()[0]);
                            upTiles.add(e);
                        }
                    }
                    //graphics.drawRect(cellX, cellY, w, h);

                    //graphics.drawPolygon(new int[]{cellX,cellX+w/2,cellX+w,cellX+w,cellX}, new int[]{cellY+3*h/4,cellY+2*h/4,cellY+3*h/4,cellY+h,cellY+h}, 5);
                    //}
                }
                //System.out.println("Draw " + goodCellsCount + " cells");
                graphics.setTransform(oldTransform);
            }
        }

//        public ViewBox getShape(Tile sprite, Scene scene) {
//            return scene.getLayoutBox(scene.toViewBox(sprite), false, SceneLayoutType.FULL_BOUNDS, null);
//        }

        @Override
        protected SceneMouseEvent createSceneMouseEvent(MouseEvent e) {
            ViewPoint vp2 = null;
            //if (isIsometric()) {
            vp2 = getScene().toIsometricViewPoint(new ViewPoint(e.getX(), e.getY()));
            e = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), vp2.getX(), vp2.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
//        } else {
//            vp2 = new ViewPoint(e.getPoint());
//        }
            Tile tile = getScene().findTile(vp2);
            if (tile != null) {
                return toSceneMouseEvent(e, tile);
            }
            return null;
        }
    }

    private class OverlappingTextureInternalBoardLayer extends DefaultLayer implements BoardLayer {
        Map<Integer, TileInfo> shouldDraw;

        public OverlappingTextureInternalBoardLayer() {
            setLayer(BACKGROUND_OVERLAPPING_LAYER);
        }

        @Override
        public void initDrawTiles(BoardLayerDrawingContext context) {
            updateImages(context);
            java.util.List<TileInfo> cells = context.getUserObject("DefaultBoardTextureExtension.TileInfos");
            shouldDraw = new HashMap<>();
            for (TileInfo cell : cells) {
                if (cell.refSmallTile != null) {
                    shouldDraw.put(cell.refSmallTile.getId(), cell);
                }
            }
        }

        @Override
        public void drawTile(BoardLayerDrawingContext context) {
            if (upImages != null) {
                Graphics2D graphics = context.getGraphics();
                AffineTransform screenTransform = context.getScreenTransform();
                AffineTransform mapTransform = context.getBoardTransform();

                AffineTransform oldTransform = graphics.getTransform();
                graphics.setTransform(screenTransform);
                TileInfo c = shouldDraw.get(context.getTile().getId());
                //int goodCellsCount=0;
                if (c != null) {
//                System.out.println("drawTile "+context.getTile());
                    ResizableImage currentImage = upImages[c.bigTile.cell.getType() % upImages.length];
                    if (currentImage != null) {
                        ViewBox goodBox = c.box;
                        //if (cc == 1 && cr == 2) {
                        int cellX = goodBox.getX();//int) (w * x - xx);
                        int cellY = goodBox.getY();//(int) (h * y - yy);
                        //force all cells to have the very same dim (double precision problem)
                        int h = goodBox.getHeight();
                        int w = goodBox.getWidth();
                        Image cellImage = null;
                        cellImage = currentImage.getImage(w, h);
                        AtomDebug.DRAW_IMAGE_DRAW_COUNT++;
                        graphics.drawImage(cellImage, cellX, cellY, null);
//                    graphics.setColor(Color.RED);
//                    graphics.drawRect(cellX, cellY, w,h);
//                    graphics.drawString("Small"+context.getTile().toString(),cellX, cellY);
//                    graphics.drawString("Big"+c.bigTile.toString(),cellX, cellY+20);
                        //goodCellsCount++;
                    }
                    //graphics.drawRect(cellX, cellY, w, h);

                    //graphics.drawPolygon(new int[]{cellX,cellX+w/2,cellX+w,cellX+w,cellX}, new int[]{cellY+3*h/4,cellY+2*h/4,cellY+3*h/4,cellY+h,cellY+h}, 5);
                    //}
                }
                //System.out.println("Draw " + goodCellsCount + " cells");
                graphics.setTransform(oldTransform);
            }

        }

        @Override
        public void draw(LayerDrawingContext context) {
            updateImages(context);
            Graphics2D graphics = context.getGraphics();
            Scene scene = context.getScene();
            java.util.List<TileInfo> cells = context.getUserObject("DefaultBoardTextureExtension.TileInfos");
            AffineTransform screenTransform = context.getScreenTransform();
            AffineTransform mapTransform = context.getBoardTransform();

            AffineTransform oldTransform = graphics.getTransform();
            graphics.setTransform(screenTransform);
            //int goodCellsCount=0;
            List<Sprite> screenSpritesList = scene.findDisplaySprites();
            Sprite[] spritesArr = screenSpritesList.toArray(new Sprite[screenSpritesList.size()]);
            Arrays.sort(spritesArr, SpriteDrawingComparator.INSTANCE);
            for (TileInfo c : cells) {
                int tileType = c.bigTile.cell.getType() % upImages.length;
                ResizableImage currentImage = upImages[tileType];
                if (currentImage != null) {
                    ViewBox goodBox = c.box;
                    //if (cc == 1 && cr == 2) {
                    int cellX = goodBox.getX();//int) (w * x - xx);
                    int cellY = goodBox.getY();//(int) (h * y - yy);
                    //force all cells to have the very same dim (double precision problem)
                    int h = goodBox.getHeight();
                    int w = goodBox.getWidth();
                    Image cellImage = null;
                    cellImage = currentImage.getImage(w, h);
                    AtomDebug.DRAW_IMAGE_DRAW_COUNT++;
                    graphics.drawImage(cellImage, cellX, cellY, null);
                    //goodCellsCount++;
                }
                //graphics.drawRect(cellX, cellY, w, h);

                //graphics.drawPolygon(new int[]{cellX,cellX+w/2,cellX+w,cellX+w,cellX}, new int[]{cellY+3*h/4,cellY+2*h/4,cellY+3*h/4,cellY+h,cellY+h}, 5);
                //}
            }
            //System.out.println("Draw " + goodCellsCount + " cells");
            graphics.setTransform(oldTransform);
        }

        public ViewBox getShape(Tile sprite, Scene scene) {
            return scene.getLayoutBox(scene.toViewBox(sprite), false, SceneLayoutType.FULL_BOUNDS, null);
        }
    }

    private static class BigTile{
        CellDef cell;
        Tile tile;

        private BigTile(CellDef cell, Tile tile) {
            this.cell = cell;
            this.tile = tile;
        }
    }

    private static class TileInfo {
        BigTile bigTile;
        ViewBox box;
        List<Tile> smallTiles;
        Tile refSmallTile;

        private TileInfo(BigTile bigTile, ViewBox box) {
            this.bigTile = bigTile;
            this.box = box;
        }
    }
}
