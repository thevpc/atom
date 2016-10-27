package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.Orientation;
import net.vpc.gaming.atom.model.OrientationType;
import net.vpc.gaming.atom.model.Sprite;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/18/13
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class OrientationImageSelector implements SpriteViewImageSelector {
    private OrientationType orientationType;

    public OrientationImageSelector(OrientationType orientationType) {
        this.orientationType = orientationType;
    }

    @Override
    public int getImageIndex(Sprite sprite, Scene scene, long frame, int imagesCount) {
        int player = sprite.getPlayerId();
        Orientation direction = orientationType.getOrientation(sprite.getDirection(), scene.isIsometric());
        double speed = sprite.getSpeed();
//        SpriteTask t = sprite.getTask();
        boolean moving = scene.getSceneEngine().hasMovedLastFrame(sprite.getId());
        if (!moving) {
            speed = 0;
        }
        return getImageIndex(sprite, scene, frame, direction, player, speed);
    }

    protected abstract int getImageIndex(Sprite sprite, Scene view, long frame, Orientation dir, int player, double speed);
}
