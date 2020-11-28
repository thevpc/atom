/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.shared.model;

import net.thevpc.gaming.atom.model.DefaultSceneEngineModel;
import net.thevpc.gaming.atom.model.ModelPoint;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainModel extends DefaultSceneEngineModel {

    private AppRole role = AppRole.SERVER;
    private AppPhase phase = AppPhase.WAITING;

    public MainModel() {
        super(30, 20);

    }


    public AppPhase getPhase() {
        return phase;
    }

    public void setPhase(AppPhase phase) {
        this.phase = phase;
    }

    public AppRole getRole() {
        return role;
    }

    public void setRole(AppRole role) {
        this.role = role;
    }
}
