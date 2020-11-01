//package tn.edu.eniso.soussecraft.main.model;
//
//import java.io.IOException;
//import org.vpc.gaming.model.strategy.StrategySceneModel;
//import tn.edu.eniso.soussecraft.main.model.resources.Minerals;
//import tn.edu.eniso.soussecraft.main.model.resources.Woods;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com) @creationtime 5 dec. 2006
// * 11:29:56
// */
//public class MainModel extends StrategySceneModel {
//
//    public static final String BACKGROUND_ID = "MyBackground";
//    private GamePhase gamePhase = GamePhase.CONNECTING;
//    private Integer controlPlayer;
//
//    public MainModel() throws IOException {
//        super(BACKGROUND_ID + ".map");
//    }
//
//    public Integer getControlPlayer() {
//        return controlPlayer;
//    }
//
//    public void setControlPlayer(Integer controlPlayer) {
//        Integer old = this.controlPlayer;
//
//        this.controlPlayer = controlPlayer;
//        firePropertyChange("controlPlayer", old, controlPlayer);
//
//    }
//
//    public GamePhase getGamePhase() {
//        return gamePhase;
//    }
//
//    public void setGamePhase(GamePhase gamePhase) {
//        GamePhase old=this.gamePhase;
//        this.gamePhase = gamePhase;
//        firePropertyChange("gamePhase", old, gamePhase);
//    }
//}
