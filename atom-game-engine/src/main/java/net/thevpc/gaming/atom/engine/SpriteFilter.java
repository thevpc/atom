/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.Sprite;

/**
 * Sprite Filter
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SpriteFilter {
    public boolean accept(Sprite sprite);
}
