/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.layers;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public enum LayerEventAction {
    SILENT_BREAK(false, false),
    SILENT_CONTINUE(false, true),
    FIRE_BREAK(true, false),
    FIRE_CONTINUE(true, true);

    private boolean fireEvent;
    private boolean continueEvent;

    private LayerEventAction(boolean fireEvent, boolean continueEvent) {
        this.fireEvent = fireEvent;
        this.continueEvent = continueEvent;
    }

    public boolean isContinueEvent() {
        return continueEvent;
    }

    public boolean isFireEvent() {
        return fireEvent;
    }

}
