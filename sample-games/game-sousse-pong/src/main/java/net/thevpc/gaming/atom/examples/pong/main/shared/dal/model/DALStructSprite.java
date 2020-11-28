/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.shared.dal.model;

import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALStructSprite implements Serializable {
    public int life;
    public double x;
    public double y;
    public double direction;
    public double speed;

    public DALStructSprite() {
    }


    public DALStructSprite(int life, double x, double y, double direction, double speed) {
        this.life = life;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
    }

}
