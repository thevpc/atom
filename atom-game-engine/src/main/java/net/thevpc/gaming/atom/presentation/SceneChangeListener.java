package net.thevpc.gaming.atom.presentation;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/25/13
 * Time: 12:20 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SceneChangeListener {
    default void imageProducerManagerChanged(Scene sceneEngine, ImageProducer oldValue, ImageProducer newValue){

    }
}
