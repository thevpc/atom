/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.palace.main.presentation.view.sprites;

import net.vpc.gaming.atom.annotations.AtomSpriteView;
import net.vpc.gaming.atom.presentation.ImageSpriteView;

/**
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSpriteView(kind = "bomb")
public class BombView extends ImageSpriteView {

    public BombView() {
        super("/net/vpc/games/atom/examples/palace/bomb.png", 1, 10);
        setFramesPerImage(5);
    }
}
