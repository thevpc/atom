/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.shared.model;

import net.thevpc.gaming.atom.model.DefaultSprite;
import net.thevpc.gaming.atom.model.ModelDimension;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class Paddle extends DefaultSprite {

    public Paddle() {
        setSize(new ModelDimension(5, 1));
        setSpeed(0.5);
        setLife(3);
    }

}
