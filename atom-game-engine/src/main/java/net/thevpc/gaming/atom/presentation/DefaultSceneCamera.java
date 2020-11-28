package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SceneEngineFrameListener;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.util.AtomUtils;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Supplier;

public class DefaultSceneCamera implements SceneCamera{
    private DefaultScene scene;
    private ModelBox cameraModel = null;
    private Supplier<ViewBox> cameraViewBoxSupplier;
//    private Sprite cameraLockToSprite;
//    SceneEngineFrameListener cameraLockToSpriteListener = new SceneEngineFrameListener() {
//        @Override
//        public void modelUpdated(SceneEngine sceneEngine, SceneEngineModel model) {
//            if (cameraLockToSprite != null) {
//                moveTo(cameraLockToSprite);
//            }
//        }
//    };
    PropertyChangeListener resetCameraModel = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            cameraModel = null;
        }
    };
    public DefaultSceneCamera(DefaultScene scene) {
        this.scene = scene;

        scene.getModel().addPropertyChangeListener("isometricView", resetCameraModel);
        scene.getModel().addPropertyChangeListener("screenSize", resetCameraModel);
        scene.getModel().addPropertyChangeListener("camera", resetCameraModel);
//        scene.getSceneEngine().addPropertyChangeListener("camera", new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                cameraModel = null;
//            }
//        });
        scene.addLifeCycleListener(new SceneLifeCycleListener() {
            @Override
            public void sceneStarted(Scene scene) {
                scene.getSceneEngine().addSceneFrameListener(new SceneEngineFrameListener() {
                    @Override
                    public void modelUpdated(SceneEngine sceneEngine, SceneEngineModel model) {
                        ViewBox bounds= cameraViewBoxSupplier ==null?null: cameraViewBoxSupplier.get();
                        if(bounds!=null) {
                            setBounds(bounds);
                        }
                    }
                });

            }
        });
    }

    @Override
    public boolean moveBy(ViewPoint delta) {
        ViewBox v = getViewBounds();
        ViewBox rectangle = new ViewBox(v.getX() + delta.getX(), v.getY() + delta.getY(), v.getWidth(), v.getHeight());
        if (!scene.getSceneScreen().intersects(rectangle)) {
            return false;
        }
        setBounds(rectangle);
        return true;
    }


    @Override
    public ViewBox getViewBounds() {
        RatioBox camera = scene.getModel().getCamera();
        ViewBox sceneScreen = scene.getSceneScreen();
        return new ViewBox(
                (int) (sceneScreen.getX() + camera.getX() * sceneScreen.getWidth()),
                (int) (sceneScreen.getY() + camera.getY() * sceneScreen.getHeight()),
                (int) (sceneScreen.getZ() + camera.getZ() * sceneScreen.getAltitude()),
                (int) (camera.getWidth() * sceneScreen.getWidth()),
                (int) (camera.getHeight() * sceneScreen.getHeight()),
                (int) (camera.getAltitude() * sceneScreen.getAltitude())
        );
    }


    public void setBounds(ViewBox rect) {
        ViewBox s = scene.getSceneScreen();
        RatioBox r = new RatioBox(
                (float)((double)rect.getX() / s.getWidth()),
                (float)((double)rect.getY() / s.getHeight()),
                (float)((double)rect.getZ() / s.getAltitude()),
                (float)((double)rect.getWidth() / s.getWidth()),
                (float)((double)rect.getHeight() / s.getHeight()),
                (float)((double)rect.getAltitude() / s.getAltitude())
        );
        setBounds(r);
    }

    public void setBounds(RatioBox rect) {
        //validateRatioViewBox(rect)
        scene.getModel().setCamera(rect);
    }


    @Override
    public Path2D getModelPolygon() {
        ViewBox v = getViewBounds();
        SceneModel m = scene.getModel();
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
        return scene.toPath2D(cam);
    }


    @Override
    public Path2D getViewPolygon() {
        ViewBox cam = getViewBounds();
        ViewDimension s = scene.getSceneSize();
        if (scene.isIsometric()) {
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
    public ModelBox getModelBounds() {
        Rectangle2D r = getModelPolygon().getBounds2D();
        ModelBox mb = new ModelBox(
                r.getMinX(),
                r.getMinY(),
                r.getWidth(),
                r.getHeight());
        cameraModel = mb;
        return cameraModel;
    }



    @Override
    public void moveTo(RatioPoint ratioPoint) {
        ViewDimension gm = scene.getSceneSize();
        moveTo(new ViewPoint(
                (int) (gm.getWidth() * ratioPoint.getX()),
                (int) (gm.getHeight() * ratioPoint.getY()),
                (int) (gm.getAltitude() * ratioPoint.getZ())));
    }

    @Override
    public void moveTo(ModelPoint modelLocation) {
        moveTo(scene.toViewPoint(modelLocation));
    }

    @Override
    public boolean moveTo(ViewPoint point) {
        ViewBox v = getViewBounds();
        ViewBox rectangle = new ViewBox(point.getX(), point.getY(), v.getWidth(), v.getHeight());
        if (!scene.getSceneScreen().intersects(rectangle)) {
            return false;
        }
        setBounds(rectangle);
        return true;
    }

    protected ViewBox getCenteredSpriteViewBound(Sprite sprite){
        if(sprite==null){
            return null;
        }
        ModelBox sbounds = sprite.getBounds();
        SceneModel m = scene.getModel();
        ViewDimension td = m.getTileSize();
        ViewBox srect = new ViewBox(
                (int) (sbounds.getX() * td.getWidth()),
                (int) (sbounds.getY() * td.getHeight()),
                (int) (sbounds.getWidth() * td.getWidth()),
                (int) (sbounds.getHeight() * td.getHeight()));
        ViewBox vp = getViewBounds();
        return scene.validateViewBox(new ViewBox(srect.getX() - vp.getWidth() / 2, srect.getY() - vp.getHeight() / 2, vp.getWidth(), vp.getHeight()));
    }

    @Override
    public void moveTo(Sprite sprite) {
        ViewBox v = getCenteredSpriteViewBound(sprite);
        if(v!=null) {
            setBounds(v);
        }
    }

    @Override
    public void setDimension(ViewDimension rect) {
        ViewBox c = getViewBounds();
        setBounds(new ViewBox(
                c.getX(),
                c.getY(),
                c.getZ(),
                rect.getWidth(),
                rect.getHeight(),
                rect.getAltitude()
        ));
    }

    @Override
    public RatioBox getRatioViewBounds() {
        return scene.getModel().getCamera();
    }



    @Override
    public ViewDimension getViewDimension() {
        return getViewBounds().getSize();
    }

    @Override
    public void setDimension(RatioDimension ratioDimension) {
        ViewDimension md = scene.getSceneSize();
        RatioBox c = getRatioViewBounds();
        if (c == null) {
            setBounds(
                    new RatioBox(
                            0, 0, 0,
                            ratioDimension.getWidth(), ratioDimension.getHeight(), ratioDimension.getAltitude()
                    )
            );
        } else {
            setBounds(
                    scene.validateRatioViewBox(new RatioBox(
                            c.getX(), c.getY(), c.getZ(),
                            ratioDimension.getWidth(), ratioDimension.getHeight(), ratioDimension.getAltitude()
                    )));
        }
    }


    public void followSprite(Sprite s) {
        followSprite(s==null?null:()->s);
    }

    @Override
    public void followSprite(Supplier<Sprite> s) {
        this.cameraViewBoxSupplier = s==null?null:()->getCenteredSpriteViewBound(s.get());
    }

    @Override
    public void setFollowStrategy(Supplier<ViewBox> s) {
        this.cameraViewBoxSupplier = s;
    }

    public void unfollowSprite() {
        setFollowStrategy(null);
    }


    @Override
    public ViewBox getViewPort() {
        return getViewBounds().getDimensionBox();
    }
}
