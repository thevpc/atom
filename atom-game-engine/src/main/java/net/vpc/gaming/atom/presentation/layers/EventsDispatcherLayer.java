/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.layers;

import net.vpc.gaming.atom.presentation.SceneKeyEvent;
import net.vpc.gaming.atom.presentation.SceneMouseEvent;

import java.awt.event.MouseEvent;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class EventsDispatcherLayer extends DefaultLayer implements InteractiveLayer {
    @Override
    public void draw(LayerDrawingContext context) {
        //
    }

    @Override
    protected SceneKeyEvent createSceneKeyEvent(KeyEventExt e) {
        return toSceneKeyEvent(e, this);
    }

    @Override
    protected SceneMouseEvent createSceneMouseEvent(MouseEvent e) {
        return toSceneMouseEvent(e, null);
    }

}
