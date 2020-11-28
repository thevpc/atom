/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model.animations;

import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.ModelDimension;
import net.thevpc.gaming.atom.presentation.layers.Layer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class LocationIndicator extends Animation {

    public LocationIndicator(int playerId, ModelPoint position, int lifeTime) {
        setSize(new ModelDimension(1, 1));
//        double x0 = Math.floor(x);
//        double y0 = Math.floor(y);
        setLocation(position);
        setLife(lifeTime);
        setMaxLife(lifeTime);
        setPlayerId(playerId);
//        setTask(new SuicideSpriteMainTask());
        setLayer(Layer.BACKGROUND_DECORATION_LAYER);
    }
}
