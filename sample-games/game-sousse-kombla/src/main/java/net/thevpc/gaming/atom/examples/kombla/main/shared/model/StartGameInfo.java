package net.thevpc.gaming.atom.examples.kombla.main.shared.model;

/**
 * Created by vpc on 10/7/16.
 */
public class StartGameInfo {
    private int playerId;
    private int[][] maze;

    public StartGameInfo(int playerId, int[][] maze) {
        this.playerId = playerId;
        this.maze = maze;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int[][] getMaze() {
        return maze;
    }
}
