/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.Sprite;

/**
 * A interface contract for classed that are both <code>SpriteFilter<code> and  <code>java.lang.Iterable&lt;Sprite><code>
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface IterableSpritesFilter extends SpriteFilter, Iterable<Sprite> {

}
