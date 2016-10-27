/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong;

import java.net.URL;
import net.vpc.gaming.atom.Atom;
import net.vpc.gaming.atom.presentation.Game;
import tn.edu.eniso.pong.hello.business.WelcomeEngine;
import tn.edu.eniso.pong.hello.presentation.view.WelcomeScene;
import tn.edu.eniso.pong.main.client.engine.MainEngineClient;
import tn.edu.eniso.pong.main.server.engine.MainEngineServer;
import tn.edu.eniso.pong.main.shared.presentation.view.MainScene;

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
