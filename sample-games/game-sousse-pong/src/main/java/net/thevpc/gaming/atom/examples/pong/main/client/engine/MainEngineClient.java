/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.client.engine;

import net.thevpc.gaming.atom.examples.pong.main.client.dal.DALClient;
import net.thevpc.gaming.atom.examples.pong.main.client.dal.DALClientListener;
import net.thevpc.gaming.atom.examples.pong.main.client.dal.ModelUpdater;
import net.thevpc.gaming.atom.examples.pong.hello.business.WelcomeEngine;
import net.thevpc.gaming.atom.examples.pong.hello.model.WelcomeModel;
import net.thevpc.gaming.atom.examples.pong.main.shared.dal.DALFactory;
import net.thevpc.gaming.atom.examples.pong.main.shared.engine.AbstractMainEngine;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.AppPhase;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.MainModel;
import net.thevpc.gaming.atom.examples.pong.main.shared.model.Paddle;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainEngineClient extends AbstractMainEngine implements DALClientListener {

    private DALClient dal;

    public MainEngineClient() {
        super();
    }

    @Override
    public void updateModel() {
        MainModel m = getModel();
        if (m.getPhase().equals(AppPhase.GAMING)) {
            if (findSprites(Paddle.class).size() < 2) {
                m.setPhase(AppPhase.GAMEOVER);
            }
        }

    }

    @Override
    public void connected() {
        MainModel m = getModel();
        m.setPhase(AppPhase.GAMING);
    }

    @Override
    public void modelChanged(final ModelUpdater modelUpdater) {
        invokeLater(new Runnable() {

            @Override
            public void run() {
                modelUpdater.updateModel(MainEngineClient.this);
            }
        });
    }

    @Override
    public void sceneActivating() {
        WelcomeModel hello = getGameEngine().getScene(WelcomeEngine.class).getModel();
        dal = DALFactory.createClient(hello.getTransport());
        MainModel m = getModel();
        m.setPhase(AppPhase.WAITING);
        //should read from server
        dal.start("localhost", 1050, this);
    }

    public void moveLeft(int playerId) {
        dal.sendLeftKeyPressed();
    }

    public void moveRight(int playerId) {
        dal.sendRightKeyPressed();
    }

    public void releaseBall(int playerId) {
        dal.sendSpacePressed();
    }
}
