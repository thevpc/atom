/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.main.shared.prensentation.sprites;

import net.vpc.gaming.atom.annotations.AtomSpriteView;
import net.vpc.gaming.atom.presentation.ImageSpriteView;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSpriteView(
        scene = "mainLocal,mainServer,mainClient",
        kind = "Bomb"
)
public class BombView extends ImageSpriteView {

    public BombView() {
//        super("/bomb.png", 10, 1);
        super("/bomb.png", 3, 1);
        //setResizeImages(true);
        setFramesPerImage(5);
    }
}
