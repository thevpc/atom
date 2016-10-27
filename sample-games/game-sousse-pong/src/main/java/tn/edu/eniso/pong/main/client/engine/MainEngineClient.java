/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.client.engine;

import tn.edu.eniso.pong.hello.business.WelcomeEngine;
import tn.edu.eniso.pong.hello.model.WelcomeModel;
import tn.edu.eniso.pong.main.client.dal.DALClient;
import tn.edu.eniso.pong.main.client.dal.DALClientListener;
import tn.edu.eniso.pong.main.client.dal.ModelUpdater;
import tn.edu.eniso.pong.main.shared.dal.DALFactory;
import tn.edu.eniso.pong.main.shared.engine.AbstractMainEngine;
import tn.edu.eniso.pong.main.shared.model.AppPhase;
import tn.edu.eniso.pong.main.shared.model.MainModel;
import tn.edu.eniso.pong.main.shared.model.Paddle;

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
        WelcomeModel hello = getGameEngine().getSceneEngine(WelcomeEngine.class).getModel();
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

    public void relaseBall(int playerId) {
        dal.sendSpacePressed();
    }
}
