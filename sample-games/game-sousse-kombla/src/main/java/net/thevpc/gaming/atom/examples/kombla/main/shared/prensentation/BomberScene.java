/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.main.shared.prensentation;

//import net.thevpc.gaming.atom.debug.layers.DebugLayer;

import net.thevpc.gaming.atom.presentation.DefaultScene;
import net.thevpc.gaming.atom.presentation.ImageGrid;
import net.thevpc.gaming.atom.presentation.ImageMatrixProducer;
import net.thevpc.gaming.atom.presentation.*;

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
