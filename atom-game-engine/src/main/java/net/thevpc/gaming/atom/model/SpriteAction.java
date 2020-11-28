/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.model;

import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SpriteAction extends Serializable {

    public String getName();

    public Sprite getSprite();

    public boolean isEnabled();

    public boolean isRunning();

    public void run();
}
