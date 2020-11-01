/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.kombla.main.shared.prensentation;

//import net.vpc.gaming.atom.debug.layers.DebugLayer;

import net.vpc.gaming.atom.presentation.*;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */

public class BomberScene extends DefaultScene {

    public static final ImageMatrixProducer WALL_IMAGE_PRODUCER = new ImageMatrixProducer("/wall.jpg", new ImageGrid(3, 5)
    ) {
        @Override
        public Image getImage(int type, int index) {
            if (index == 0) {
                index = 3;//
            } else {
                index = 14;//11+index%4;
            }
            return super.getImage(type, index);
        }
    };

    public BomberScene() {
        setImageProducer(WALL_IMAGE_PRODUCER);
        getCamera().followSprite(
                ()-> getSceneEngine().findSpriteByKind("Person")
        );
    }
}
