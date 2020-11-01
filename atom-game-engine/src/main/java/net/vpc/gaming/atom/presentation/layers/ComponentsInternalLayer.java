/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.layers;

import net.vpc.gaming.atom.model.ViewPoint;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SceneMouseEvent;
import net.vpc.gaming.atom.presentation.components.SceneComponentLayout;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class ComponentsInternalLayer extends DefaultLayer implements InteractiveLayer, InternalLayer {

    private SceneComponentLayout[] componentsArr;
//    private Set<Sprite> spritesSet;

    public ComponentsInternalLayer(SceneComponentLayout[] componentsArr, Scene scene) {
        this(componentsArr, Layer.SCREEN_COMPONENT_LAYER, scene);
    }

    public ComponentsInternalLayer(SceneComponentLayout[] componentsArr, int layer, Scene scene) {
        setScene(scene);
        setLayer(layer);
        this.componentsArr = componentsArr;
//        this.spritesSet = new HashSet<Sprite>(Arrays.asList(spritesArr));
    }

    protected void drawComponent(SceneComponentLayout component, LayerDrawingContext context) {
        if (component != null) {
            Object c = component.getConstraints();
            ViewPoint position = null;
            if (c instanceof ViewPoint) {
                position = (ViewPoint) c;
            }
            if (position != null) {
                component.getComponent().setLocation(position);
            }
            component.getComponent().draw(context);
        }
    }

    @Override
    public void draw(LayerDrawingContext context) {
        AffineTransform screenTransform = context.getScreenTransform();
        AffineTransform mapTransform = context.getBoardTransform();
        Graphics2D graphics = context.getGraphics();
        AffineTransform oldTransform = graphics.getTransform();
        for (SceneComponentLayout component : componentsArr) {
            if (!component.isIsometric()) {
                graphics.setTransform(screenTransform);
                drawComponent(component, context);
            } else {
                graphics.setTransform(mapTransform);
                drawComponent(component, context);
            }
        }
        graphics.setTransform(oldTransform);
    }

    @Override
    protected SceneMouseEvent createSceneMouseEvent(MouseEvent e) {
        SceneComponentLayout s = findComponent(e);
        if (s != null) {
            return toSceneMouseEvent(e, s);
        }
        return null;
    }

    private SceneComponentLayout findComponent(MouseEvent e) {
        return null;
//        //if(is)
////            ModelPoint p = getView().toModel(e.getPoint());
//        Collection<Sprite> allPossible = getScene().findSprites(e.getPoint());
//        allPossible.retainAll(spritesSet);
//        int found = allPossible.size();
//        if (found > 0) {
//            Sprite[] ok = allPossible.toArray(new Sprite[found]);
//            if (ok.length == 1) {
//                return ok[0];
//            }
//            Arrays.sort(ok, SpriteDrawingComparator.INSTANCE);
//            return ok[ok.length - 1];
//        } else {
//            return null;
//        }
    }
}