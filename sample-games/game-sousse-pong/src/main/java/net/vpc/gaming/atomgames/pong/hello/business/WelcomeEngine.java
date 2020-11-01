/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atomgames.pong.hello.business;

import net.vpc.gaming.atom.engine.DefaultSceneEngine;
import net.vpc.gaming.atomgames.pong.hello.model.WelcomeModel;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class WelcomeEngine extends DefaultSceneEngine {

    public WelcomeEngine() {
        setModel(new WelcomeModel());
    }
}
