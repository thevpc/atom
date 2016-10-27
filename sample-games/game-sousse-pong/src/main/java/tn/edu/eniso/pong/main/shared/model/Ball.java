/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.shared.model;

import net.vpc.gaming.atom.model.DefaultSprite;
import net.vpc.gaming.atom.model.ModelDimension;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class Ball extends DefaultSprite {

    public Ball() {
        setSize(new ModelDimension(1, 1));
        setSpeed(0.5);
    }
}
