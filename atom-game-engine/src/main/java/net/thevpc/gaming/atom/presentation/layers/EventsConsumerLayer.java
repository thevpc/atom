/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.layers;

import net.thevpc.gaming.atom.presentation.SceneKeyEvent;
import net.thevpc.gaming.atom.presentation.SceneMouseEvent;

import java.awt.event.MouseEvent;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class EventsConsumerLayer extends DefaultLayer implements InteractiveLayer {
    private boolean consumeMouseEvents;
    private boolean consumeKeyEvents;

    public EventsConsumerLayer(boolean consumeMouseEvents, boolean consumeKeyEvents) {
        this.consumeMouseEvents = consumeMouseEvents;
        this.consumeKeyEvents = consumeKeyEvents;
    }

    @Override
    public void draw(LayerDrawingContext context) {
        //draw nothing
    }

    @Override
    protected SceneKeyEvent createSceneKeyEvent(KeyEventExt e) {
        if (consumeKeyEvents) {
            SceneKeyEvent evt = new SceneKeyEvent(getScene(), this, -1, 0, 0, -1, '\0', -1);
            evt.setConsumed(true);
            return evt;
        }
        return null;
    }

    @Override
    protected SceneMouseEvent createSceneMouseEvent(MouseEvent e) {
        if (consumeMouseEvents) {
            SceneMouseEvent evt = new SceneMouseEvent(getScene(), -1, 0, 0, Double.NaN, Double.NaN, Integer.MIN_VALUE, Integer.MIN_VALUE, -1, false, -1, this, null);
            evt.setConsumed(true);
            return evt;
        }
        return null;
    }


}
