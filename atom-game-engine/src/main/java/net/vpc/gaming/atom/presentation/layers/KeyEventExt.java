/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.presentation.layers;

import java.awt.event.KeyEvent;

/**
 * @author Taha Ben Salah
 */
public class KeyEventExt {
    public KeyEvent keyEvent;
    public int[] pressedKeys;

    public KeyEventExt(KeyEvent keyEvent, int[] pressedKeys) {
        this.keyEvent = keyEvent;
        this.pressedKeys = pressedKeys;
    }

}
