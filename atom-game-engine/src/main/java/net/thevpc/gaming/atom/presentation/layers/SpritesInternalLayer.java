/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.layers;

import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.model.Tile;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SpriteDrawingComparator;
import net.thevpc.gaming.atom.presentation.SpriteDrawingContext;
import net.thevpc.gaming.atom.presentation.SpriteView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

import net.thevpc.gaming.atom.presentation.SceneMouseEvent;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SpritesInternalLayer extends DefaultLayer implements InteractiveLayer, BoardLayer, InternalLayer {

    private Sprite[] spritesArr;
    private Set<Sprite> spritesSet;
    private Map<Integer, List<Sprite>> spritesByTile;

    public SpritesInternalLayer(Sprite[] spritesArr, Scene scene) {
        setScene(scene);
        this.spritesArr = spritesArr;
        this.spritesSet = new HashSet<Sprite>(Arrays.asList(spritesArr));
    }

    @Override
    public void initDrawTiles(BoardLayerDrawingContext context) {
        spritesByTile = new HashMap<>();
        for (Sprite sprite : spritesArr) {
            //ModelPoint p = sprite.getLocation();
            Tile tile = context.getScene().findTile(sprite.getBounds().getModelPoints()[0]);
            int tileId = tile.getId();
            List<Sprite> sprites = spritesByTile.get(tileId);
            if (sprites == null) {
                sprites = new ArrayList<>();
                spritesByTile.put(tileId, sprites);
            }
            sprites.add(sprite);
        }
    }

    @Override
    public void drawTile(BoardLayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene scene = context.getScene();
        AffineTransform oldTransform = graphics.getTransform();
        List<Sprite> sprites = spritesByTile.get(context.getTile().getId());
        if (sprites != null) {
            for (Sprite sprite : sprites) {
//                System.out.println("drawSprite "+sprite);
//            if (sprite.getBounds().intersects(context.getClipModel())) {
//                System.out.println("[Row"+context.getRow()+"] Draw Sprite "+sprite.getType()+" "+sprite.getId());

                SpriteView spriteView = scene.getSpriteView(sprite);

                if(scene.isIsometric()){
                    if (!spriteView.getSpriteViewConstraints(sprite).isIsometric()) {
                        graphics.setTransform(context.getScreenTransform());
                        spriteView.draw(new SpriteDrawingContext(sprite, scene, graphics,spriteView));
                    } else {
                        graphics.setTransform(context.getBoardTransform());
                        spriteView.draw(new SpriteDrawingContext(sprite, scene, graphics,spriteView));
                    }
                }else{
                    graphics.setTransform(context.getBoardTransform());
                    spriteView.draw(new SpriteDrawingContext(sprite, scene, graphics,spriteView));
                }
                //spriteView.draw(new SpriteDrawingContext(sprite, scene, graphics));
//            }
            }
        }
        graphics.setTransform(oldTransform);
    }

    @Override
    public void draw(LayerDrawingContext context) {
        Graphics2D graphics = context.getGraphics();
        Scene scene = context.getScene();
        AffineTransform oldTransform = graphics.getTransform();
        for (Sprite sprite : spritesArr) {
//            if (sprite.getBounds().intersects(context.getClipModel())) {
//                System.out.println("[Row"+context.getRow()+"] Draw Sprite "+sprite.getType()+" "+sprite.getId());

            SpriteView spriteView = scene.getSpriteView(sprite);
            if(scene.isIsometric()){
                if (!spriteView.getSpriteViewConstraints(sprite).isIsometric()) {
                    graphics.setTransform(context.getScreenTransform());
                    spriteView.draw(new SpriteDrawingContext(sprite, scene, graphics,spriteView));
                } else {
                    graphics.setTransform(context.getBoardTransform());
                    spriteView.draw(new SpriteDrawingContext(sprite, scene, graphics,spriteView));
                }
            }else{
                graphics.setTransform(context.getBoardTransform());
                spriteView.draw(new SpriteDrawingContext(sprite, scene, graphics,spriteView));
            }
            //spriteView.draw(new SpriteDrawingContext(sprite, scene, graphics));
//            }
        }
        graphics.setTransform(oldTransform);
    }

    @Override
    protected SceneMouseEvent createSceneMouseEvent(MouseEvent e) {
        Sprite s = findSprite(e);
        if (s != null) {
            return toSceneMouseEvent(e, s);
        }
        return null;
    }

    private Sprite findSprite(MouseEvent e) {
        //if(is)
//            ModelPoint p = getView().toModel(e.getPoint());
        Collection<Sprite> allPossible = getScene().findSprites(new ViewPoint(e.getPoint()));
        allPossible.retainAll(spritesSet);
        int found = allPossible.size();
        if (found > 0) {
            Sprite[] ok = allPossible.toArray(new Sprite[found]);
            if (ok.length == 1) {
                return ok[0];
            }
            Arrays.sort(ok, SpriteDrawingComparator.INSTANCE);
            return ok[ok.length - 1];
        } else {
            return null;
        }
    }
}