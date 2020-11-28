package net.thevpc.gaming.atom.engine;

/**
 * The listener that's notified when game state changes
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface GameEngineStateListener {

    /**
     * called when game starts
     *
     * @param game current game
     */
    public void gameStarting(GameEngine game);

    /**
     * called when game starts
     *
     * @param game current game
     */
    public void gameStarted(GameEngine game);

    /**
     * called when game stops
     *
     * @param game current game
     */
    public void gameStopping(GameEngine game);

    /**
     * called when game stops
     *
     * @param game current game
     */
    public void gameStopped(GameEngine game);

    /**
     * called when game stops
     *
     * @param game current game
     */
    public void gamePausing(GameEngine game);

    /**
     * called when game stops
     *
     * @param game current game
     */
    public void gamePaused(GameEngine game);

    /**
     * called when game stops
     *
     * @param game current game
     */
    public void gameResuming(GameEngine game);

    /**
     * called when game stops
     *
     * @param game current game
     */
    public void gameResumed(GameEngine game);

    /**
     * called when game is disposing
     *
     * @param game current game
     */
    public void gameDisposing(GameEngine game);

    /**
     * called when game stops
     *
     * @param game current game
     */
    public void gameDisposed(GameEngine game);
}
