/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.tanks.shared.presentation.view;

import net.vpc.games.atom.examples.tanks.shared.presentation.controller.BattleFieldController;
import net.vpc.gaming.atom.annotations.AtomScene;
import net.vpc.gaming.atom.presentation.DefaultScene;
import net.vpc.gaming.atom.presentation.ImageGrid;
import net.vpc.gaming.atom.presentation.ImageMatrixProducer;

import java.awt.*;

/**
 * @author Taha Ben Salah
 */
@AtomScene(
        id = "battle",
        tileWidth = 80,
        tileHeight = 80,
        cameraWidth = 0.5f,
        cameraHeight = 0.5f
)
public class BattleFieldScene extends DefaultScene {
    public static final ImageMatrixProducer WALL_IMAGE_PRODUCER = new ImageMatrixProducer(
            "/net/vpc/games/atom/examples/tanks/images/tiles.png",
            new ImageGrid(8, 4)
    ) {
        int[] EMPTY = {0, 3, 8, 11};

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

    public BattleFieldScene() {
        setImageProducer(WALL_IMAGE_PRODUCER);
////        addLayer(new FillBoardColorLayer(Color.BLACK));
////        addLayer(new ImageBoardLayer(1, 4, 8, "/net/vpc/games/atom/examples/tanks/images/tiles.png"));
        addController(new BattleFieldController());
        addControlPlayer(1);
    }

    @Override
    protected void sceneStarted() {
        getCamera().followSprite(() ->
                getSceneEngine().findSpriteByKind("Tank", getControlPlayer() == null ? null : getControlPlayer().getId(), null)
        );
    }
}
