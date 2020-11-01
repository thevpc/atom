package net.vpc.gaming.atom.presentation.layers;

import net.vpc.gaming.atom.model.Tile;
import net.vpc.gaming.atom.model.ViewBox;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/22/13
 * Time: 11:37 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FlatBoardLayer extends DefaultLayer implements BoardLayer {
    private BufferedImage _frameImage;

    @Override
    public void initDrawTiles(BoardLayerDrawingContext context) {
        ViewBox vp = context.getScene().getCamera().getViewPort();
        _frameImage = new BufferedImage(vp.getWidth(), vp.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = _frameImage.createGraphics();
        draw(context);
        g2d.dispose();

    }

    @Override
    public void drawTile(BoardLayerDrawingContext context) {
        Tile tile = context.getTile();
        ViewBox viewBox = context.getScene().toViewBox(tile);
        //BufferedImage i = _frameImage.getSubimage(viewBox.getX(), viewBox.getY(), viewBox.getWidth(), viewBox.getHeight());
        Graphics2D graphics = context.getGraphics();
        Shape oldClip = graphics.getClip();
        graphics.setClip(viewBox.getX(), viewBox.getY(), viewBox.getWidth(), viewBox.getHeight());

        graphics.drawImage(_frameImage, viewBox.getX(), viewBox.getY(), null);
        graphics.setClip(oldClip);
    }

//    private long lastFrame =-1;
//    private BufferedImage _frameImage;
//    @Override
//    public void drawRow(LayerDrawingContext context) {
//        long frame = context.getScene().getFrame();
//        if(lastFrame!= frame){
//            lastFrame=frame;
//            ViewBox vp = context.getScene().getScreen();
//            _frameImage = new BufferedImage(vp.getWidth(), vp.getHeight(), BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g2d = _frameImage.createGraphics();
//            LayerDrawingContext context2=new LayerDrawingContext(g2d,context.getScene(),context.getScreenTransform(),context.getBoardTransform());
//            draw(context2);g2d.dispose();
//        }
//        Graphics2D graphics = context.getGraphics();
//        Shape oldClip = graphics.getClip();
//
//        graphics.setClip(context.getClip());
//        graphics.drawImage(_frameImage,0,0,null);
//        graphics.setClip(oldClip);
//    }

    public abstract void draw(LayerDrawingContext context);
}
