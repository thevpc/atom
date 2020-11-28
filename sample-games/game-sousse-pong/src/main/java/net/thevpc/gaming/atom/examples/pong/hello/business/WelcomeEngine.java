/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.hello.business;

import net.thevpc.gaming.atom.engine.DefaultSceneEngine;
import net.thevpc.gaming.atom.examples.pong.hello.model.WelcomeModel;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class WelcomeEngine extends DefaultSceneEngine {

    public WelcomeEngine() {
        setModel(new WelcomeModel());
    }
}
