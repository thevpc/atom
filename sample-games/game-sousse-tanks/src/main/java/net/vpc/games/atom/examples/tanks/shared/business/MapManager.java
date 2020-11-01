/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.games.atom.examples.tanks.shared.business;

import net.vpc.gaming.atom.engine.maze.Maze;
import net.vpc.games.atom.examples.tanks.shared.dal.MapDAO;
import net.vpc.gaming.atom.engine.maze.MazeTypeDecorator;
import net.vpc.gaming.atom.model.DefaultSceneEngineModel;
import net.vpc.gaming.atom.model.SceneEngineModel;

/**
 *
 * @author vpc
 */
public class MapManager {

    public static SceneEngineModel getBattleFieldModel(String name) {
        MapDAO d = new MapDAO();
        int[][] t = null;
        try {
            t = d.load(name);
        } catch (Exception e) {
            t = Maze.generateBoxedMaze(
                    16, 16, null, new MazeTypeDecorator() {
                @Override
                public int decorate(int tileX, int tileY, boolean obstacle) {
                    if (!obstacle) {
                        return 0;
                    } else {
                        return (((int) (Math.random() * 4)) * 8 + 7);
                    }
                }
            });
            d.save(t, name);
        }
//        BattleFieldModel m = new BattleFieldModel(t);
//        Tank tank = m.findSprite(Tank.class);
//        tank.setTask(new SmartMoveTask(false));
        return new DefaultSceneEngineModel(t);
    }
}
