/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation.layers;

import net.thevpc.gaming.atom.presentation.KeyCode;

import java.awt.event.KeyEvent;

/**
 * @author Taha Ben Salah
 */
public class KeyEventExt {
    public KeyEvent keyEvent;
    public KeyCode[] pressedKeys;

    public KeyEventExt(KeyEvent keyEvent, KeyCode[] pressedKeys) {
        this.keyEvent = keyEvent;
        this.pressedKeys = pressedKeys;
    }

}
