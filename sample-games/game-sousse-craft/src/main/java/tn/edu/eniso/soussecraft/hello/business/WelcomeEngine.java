/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.soussecraft.hello.business;

import net.vpc.gaming.atom.annotations.AtomSceneEngine;
import net.vpc.gaming.atom.engine.DefaultSceneEngine;
import tn.edu.eniso.soussecraft.hello.model.AppRole;


/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSceneEngine(id = "welcome",welcome = true)
public class WelcomeEngine extends DefaultSceneEngine {

    public WelcomeEngine() {
    }

    public AppRole getRole() {
        return getGameEngine().getProperties().getProperty(AppRole.class.getName());
    }

    public void setRole(AppRole role) {
        getGameEngine().getProperties().setProperty(AppRole.class.getName(),role);
    }
}
