package net.vpc.gaming.atom.engine;

import net.vpc.gaming.atom.model.DefaultPlayer;
import net.vpc.gaming.atom.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/31/13
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultPlayerFactory implements PlayerFactory {
    @Override
    public Player createPlayer() {
        return new DefaultPlayer("");
    }
}
