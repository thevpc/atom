package tn.rnu.enit.ateliercorba.jcanon.communication;

import jcanon.JCanonClient;
import jcanon._JCanonServerImplBase;
import tn.rnu.enit.ateliercorba.jcanon.model.CanonGameModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Classe Servant pour l'interface Corba JCanonServer
 *
 * Elle ecapsule une instance du Model (CanonGameModel) qui est partage
 * par tous les joueurs.
 *
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 11 dec. 2006 00:49:20
 */
public class JCanonServerServant extends _JCanonServerImplBase {
    /**
     * instance du model du jeu
     */
    private CanonGameModel game;

    /**
     * liste des joueurs
     */
    private ArrayList<JCanonClient> players = new ArrayList<JCanonClient>();

    /**
     * Constructeur principal du servant.
     * Une instance du model (CanonGameModel) est cree puis
     * un Timer est active pour appeler toutes les 100ms la fonction tic()
     */
    public JCanonServerServant() {
        game = new CanonGameModel();
        game.addDefaultSprites();
        Timer timer=new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                tic();
            }
        }, 0,100);
    }


    /**
     * appelee chaque delta-t (temps fixe) pour implementer la dynamique du jeu
     * Elle appelle CanonGameModel.tic() puis fireModelChanged()
     */
    public void tic() {
        game.tic();
        fireModelChanged();
    }

    /**
     * ici on implemente le callback ,c'est a dire l'appel du/des client a partir du serveur.
     * en effet, le serveur notifie tous les joueurs (identifies par un ionstance de linterface
     * JCanonClient recuperee lors de l'appel a connect
     */
    public void fireModelChanged() {
        for (JCanonClient player : players) {
            player.modelChanged(GameCommunicationHelper.toCorba(game));
        }
    }

    /**
     * appelee a distance par les client en passant une instance JCanonClient qui sera utilisee dans le callback
     * le premier connecte sera le joueur 1, le second, le joueur 2
     * @param client le joueur
     * @return l'index du joueur 1 ou 2
     */
    public int connect(JCanonClient client) {
        if (players.size() < 2) {
            System.out.println("user connected");
            players.add(client);
            if(players.size()==2){
                game.setStatus(CanonGameModel.GameStatus.GAME_STARTED);
            }
            return players.size();
        }
        System.out.println("user rejected");
        throw new RuntimeException("Already Connected");
    }

    /**
     * appelee par le client pour notifier le serveur que le joueur a appuye sur 'espace'
     * @param player l'index du joueur 1 ou 2
     */
    public void playerHitFire(int player) {
        System.out.println("playerHitFire "+player);
        game.playerHitFire(player);
        fireModelChanged();
    }

    /**
     * appelee par le client pour notifier le serveur que le joueur a appuye sur 'gauche'
     * @param player l'index du joueur 1 ou 2
     */
    public void playerMoveLeft(int player) {
        System.out.println("playerMoveLeft "+player);
        game.playerMoveLeft(player);
        fireModelChanged();
    }

    /**
     * appelee par le client pour notifier le serveur que le joueur a appuye sur 'droite'
     * @param player l'index du joueur 1 ou 2
     */
    public void playerMoveRight(int player) {
        System.out.println("playerMoveRight "+player);
        game.playerMoveRight(player);
        fireModelChanged();
    }
}
