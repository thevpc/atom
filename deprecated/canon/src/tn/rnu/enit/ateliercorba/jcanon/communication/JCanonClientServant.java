package tn.rnu.enit.ateliercorba.jcanon.communication;

import jcanon.CorbaGame;
import jcanon._JCanonClientImplBase;
import tn.rnu.enit.ateliercorba.jcanon.view.CanonGameView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Classe Servant pour l'interface Corba JCanonClient
 *
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 11 dec. 2006 00:49:20
 */
public class JCanonClientServant extends _JCanonClientImplBase {
    private jcanon.JCanonServer server;
    private CanonGameView view = null;

    /**
     * constructeur par defaut
     */
    public JCanonClientServant() {
    }

    /**
     * appelee par le serveur a chaque fois que le modele partage est modifie
     * @param model le modele du jeu (les donnees du jeu)
     */
    public void modelChanged(CorbaGame model) {
        if (view != null) {
//            System.out.println("modelChanged...");
            view.setGame(GameCommunicationHelper.fromCorba(model));
        }
    }

    public void startGame(jcanon.JCanonServer serv) {
        this.server = serv;
        System.out.println("connection to server...");

        // le modificateur final ici c'est pour pouvoir l'utiliser dans la classe anonyme KeyAdapter
        // techniquement final veut dire constant
        final int player = server.connect(this);

        System.out.println("I'm player " + player);

        //creer la vue (presentation, fenetre du jeu)
        view = new CanonGameView(player);

        //associer un traitement au touches appuyees par l'utilisateur
        view.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT: {
                        // si le joueur appuie sur 'gauche' on notifie le serveur avec server.playerMoveLeft
                        server.playerMoveLeft(player);
                        break;
                    }
                    case KeyEvent.VK_RIGHT: {
                        // si le joueur appuie sur 'droite' on notifie le serveur avec server.playerMoveRight
                        server.playerMoveRight(player);
                        break;
                    }
                    case KeyEvent.VK_SPACE: {
                        // si le joueur appuie sur 'espace' on notifie le serveur avec server.playerHitFire
                        server.playerHitFire(player);
                        break;
                    }
                }
            }
        });
        System.out.println("starting view");

        //afficher la fenetre
        view.show();
    }
}
