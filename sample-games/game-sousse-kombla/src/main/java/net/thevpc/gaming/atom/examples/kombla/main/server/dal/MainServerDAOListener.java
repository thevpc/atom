package net.thevpc.gaming.atom.examples.kombla.main.server.dal;

import net.thevpc.gaming.atom.examples.kombla.main.shared.model.StartGameInfo;

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
    StartGameInfo onReceivePlayerJoined(String name);

    void onReceiveMoveLeft(int playerId);

    void onReceiveMoveRight(int playerId);

    void onReceiveMoveUp(int playerId);

    void onReceiveMoveDown(int playerId);

    void onReceiveReleaseBomb(int playerId);
}
