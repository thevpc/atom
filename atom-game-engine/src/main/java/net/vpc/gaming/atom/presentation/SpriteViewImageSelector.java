package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.Sprite;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/18/13
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SpriteViewImageSelector {
    int getImageIndex(Sprite sprite, Scene scene, long frame, int imagesCount);
}
