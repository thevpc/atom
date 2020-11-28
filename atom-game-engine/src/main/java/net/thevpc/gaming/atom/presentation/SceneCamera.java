package net.thevpc.gaming.atom.presentation;

import net.thevpc.gaming.atom.model.*;

import java.awt.geom.Path2D;
import java.util.function.Supplier;

public interface SceneCamera {
     void moveTo(ModelPoint modelLocation);

     boolean moveTo(ViewPoint viewLocation);

     void moveTo(RatioPoint ratioPoint);

     void moveTo(Sprite sprite);

     boolean moveBy(ViewPoint delta);


     Path2D getModelPolygon();

     Path2D getViewPolygon();

     RatioBox getRatioViewBounds();

     ModelBox getModelBounds();
     ViewBox getViewBounds();
     ViewBox getViewPort();
     ViewDimension getViewDimension();

     void setDimension(ViewDimension rect);

     void setBounds(ViewBox rect);

     void setBounds(RatioBox ratioBox);


     void setDimension(RatioDimension size);


     void followSprite(Supplier<Sprite> s);

     void followSprite(Sprite s);

     void setFollowStrategy(Supplier<ViewBox> s);

     void unfollowSprite();

}
