/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.soussecraft.hello.business;

import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.engine.DefaultSceneEngine;
import net.thevpc.gaming.atom.examples.soussecraft.hello.model.AppRole;


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
