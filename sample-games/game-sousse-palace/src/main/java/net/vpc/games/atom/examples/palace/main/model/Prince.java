/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.palace.main.model;

import net.vpc.gaming.atom.model.DefaultSprite;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class Prince extends DefaultSprite {
    public static final double JUMP_SPEED = 0.2;
    public static final double MOVE_SPEED = 0.1;

    public Prince() {
        setKind("Prince");
        setSize(1,2);
        setSpeed(0.1);
    }

}
