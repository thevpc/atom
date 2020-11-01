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
@AtomSpriteView(kind = "Explosion")
public class ExplosionView extends ImageSpriteView {

    public ExplosionView() {
        super("/net/vpc/games/atom/examples/palace/explosion.png", 5, 5);
    }

//    public int getImageIndex(Sprite sprite, Scene view, long frame) {
//        ExplosionTask task = (ExplosionTask) sprite.getTask();
//        return task.getTime();
//    }
}
