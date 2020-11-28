/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.palace.main.presentation.view.sprites;

import net.thevpc.gaming.atom.annotations.AtomSpriteView;
import net.thevpc.gaming.atom.presentation.ImageSpriteView;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSpriteView(kind = "bomb")
public class BombView extends ImageSpriteView {

    public BombView() {
        super("/net/thevpc/gaming/atom/examples/palace/bomb.png", 1, 10);
        setFramesPerImage(5);
    }
}
