/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong;

import net.thevpc.gaming.atom.Atom;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.examples.pong.hello.business.WelcomeEngine;
import net.thevpc.gaming.atom.examples.pong.hello.presentation.view.WelcomeScene;
import net.thevpc.gaming.atom.examples.pong.main.client.engine.MainEngineClient;
import net.thevpc.gaming.atom.examples.pong.main.server.engine.MainEngineServer;
import net.thevpc.gaming.atom.examples.pong.main.shared.presentation.view.MainScene;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class PongGame {

    public static void main(String[] args) {
        Game game = Atom.createGame();
        game.setIcon("/brick.png");
        game.addScene(new WelcomeScene(), new WelcomeEngine());
        game.addScene(new MainScene(1), new MainEngineServer());
        game.addScene(new MainScene(2), new MainEngineClient());
        game.start();
    }
}
