/**
 * This file is part of the Tanks project.
 * 
 * This project is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This project is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * This project. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package net.thevpc.gaming.atom.examples.kombla.welcome;

import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AbstractMainEngine;
import net.thevpc.gaming.atom.annotations.AtomSceneController;
import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.presentation.DefaultSceneController;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.SceneKeyEvent;
import net.thevpc.gaming.atom.presentation.components.SList;
import net.thevpc.gaming.atom.presentation.components.STextField;

/**
 * Default Controller for Welcome Scene.
 * This controller handles SPACE and ENTER key pressed to start the game.
 * Upon pressing SPACE or ENTER, modesList combobox is check for the selected 
 * mode (Local, Host or Join) to create, register and run the appropriate 
 * BattleFieldEngine (any implementation of AbstractBattleFieldEngine).
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomSceneController(
        scene = "welcome"
)
public class WelcomeController extends DefaultSceneController {

    /**
     * default and empty constructor
     */
    public WelcomeController() {
    }

    /**
     * called by AGE framework on key pressed
     * @param e key press event
     */
    @Override
    public void keyPressed(SceneKeyEvent e) {
        Game game = e.getScene().getGame();
        //get the current scene
        WelcomeScene scene = (WelcomeScene) e.getScene();
        //get needed components from scene
        SList modesList = scene.getComponent("modesList");
        STextField serverAddress = scene.getComponent("serverAddress");
        STextField serverPort = scene.getComponent("serverPort");
        //find the selected mode value
        AppRole mode = (AppRole) modesList.getSelectedValue();
        //switch according to pressed key
        switch (e.getKeyCode()) {
            case SPACE:
            case ENTER: {
                GameEngine gameEngine = scene.getSceneEngine().getGameEngine();
                switch (mode) {
                    case LOCAL_GAME: {
                        //activate the created scene
                        gameEngine.setActiveSceneEngine("mainLocal");
                        break;
                    }
                    case HOST_GAME: {
                        //store serverPort in global model to be accessible from engine later
                        AbstractMainEngine.getAppConfig(gameEngine).setServerPort(Integer.parseInt(serverPort.getText()));
                        //activate the created scene
                        gameEngine.setActiveSceneEngine("mainServer");
                        break;
                    }
                    case JOIN_GAME: {
                        //store serverAddress and serverPort in global model to be accessible from engine later
                        AbstractMainEngine.getAppConfig(gameEngine).setServerAddress(serverAddress.getText());
                        AbstractMainEngine.getAppConfig(gameEngine).setServerPort(Integer.parseInt(serverPort.getText()));
                        //activate the created scene
                        gameEngine.setActiveSceneEngine("mainClient");
                        break;
                    }
                }
                //consume the event not to fire another Controller for the same event
                e.setConsumed(true);
                break;
            }
        }
    }
}
