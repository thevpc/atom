/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.hello.model;

import net.vpc.gaming.atom.model.DefaultSceneEngineModel;
import tn.edu.eniso.pong.main.shared.model.AppRole;
import tn.edu.eniso.pong.main.shared.model.AppTransport;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class WelcomeModel extends DefaultSceneEngineModel {

    private AppRole role = AppRole.SERVER;
    private AppTransport transport = AppTransport.TCP;

    public WelcomeModel() {
    }

    public AppRole getRole() {
        return role;
    }

    public void setRole(AppRole role) {
        this.role = role;
    }

    public AppTransport getTransport() {
        return transport;
    }

    public void setTransport(AppTransport transport) {
        this.transport = transport;
    }
}
