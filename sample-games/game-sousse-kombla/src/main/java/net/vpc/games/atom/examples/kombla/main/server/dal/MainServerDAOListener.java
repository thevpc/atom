package net.vpc.games.atom.examples.kombla.main.server.dal;

import net.vpc.games.atom.examples.kombla.main.shared.model.StartGameInfo;

/**
 * Created by vpc on 10/7/16.
 */
public interface MainServerDAOListener {
    /**
     * called by dal when a player joins the game
     *
     * @param name player name
     * @return player id and maze tiles
     */
    public StartGameInfo onReceivePlayerJoined(String name);

    public void onReceiveMoveLeft(int playerId);

    public void onReceiveMoveRight(int playerId);

    public void onReceiveMoveUp(int playerId);

    public void onReceiveMoveDown(int playerId);

    public void onReceiveReleaseBomb(int playerId);
}
