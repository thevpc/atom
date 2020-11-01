package tn.rnu.enit.ateliercorba.jcanon.view;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.model.Sprite;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.view.FrameGameView;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.view.GameRendererManagerMap;
import tn.rnu.enit.ateliercorba.jcanon.gameengine.view.SpriteContainer;
import tn.rnu.enit.ateliercorba.jcanon.model.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 5 dec. 2006 11:30:31
 */
public class CanonGameView extends FrameGameView {
    /**
     * modele de jeu
     */
    private CanonGameModel game;
    private GameRendererManagerMap viewMapper = new GameRendererManagerMap();
    private SpriteContainer screen;
    private int player;

    /**
     * Seul Constructeur de la classe
     * l'indexe du joueur sert entre autre a savoir par exemple quelle couleur attribue au sprite du joueur
     *
     * @param player l'indexe du joueur (1 ou 2)
     */
    public CanonGameView(int player) {
        game = new CanonGameModel();
        this.player = player;
        setComponent(screen = new CanonScreen(this));

        // association entre Modele et Vues
        // en fait c'est pour dire a la presentation comment representer chaque objet (sprite) du modele
        // ainsi tout les objets modele de type PlayerSprite seront representes (dessines) grace a la
        // l'instance new PlayerView().
        // cette correspondance est nessessaire car le modele ne connat pas la presentation donc on
        // ne peut pas par exemple rajouter une fonction polymorphe dans le modele de type 'getView()' pour
        //retourner la vue selon le model courant.
        viewMapper.register(PlayerSprite.class, new PlayerView());
        viewMapper.register(PlaneSpriteOne.class, new PlaneViewOne());
        viewMapper.register(PlaneSpriteTwo.class, new PlaneViewTwo());
        viewMapper.register(FireSprite.class, new FireView2());
        viewMapper.register(PlaneBombSprite.class, new BombView());

        //revalider les sprites a afficher
        revalidateModel();
    }


    public int getPlayer() {
        return player;
    }

    /**
     * revalider les sprites a afficher et les passer au screen(ecran en cours)
     */
    public void revalidateModel() {
        ArrayList<Sprite> s = new ArrayList<Sprite>();
        s.addAll(Arrays.asList(game.getCanons()));
        s.addAll(Arrays.asList(game.getFires()));
        s.addAll(Arrays.asList(game.getPlanes()));
        s.addAll(Arrays.asList(game.getBombs()));
        screen.setSprites(s);
    }

    /**
     * recupere le modele du jeu : les donnees du jeu en cours
     *
     * @return le modele du jeu
     */
    public CanonGameModel getGame() {
        return game;
    }

    /**
     * mettre a jour le jeu en cours
     * appelee par JCanonClientServant.modelChanged lorsque le serveur notifie
     * le client par les nouvelles donnees
     *
     * @param game le modele du jeu
     */
    public void setGame(CanonGameModel game) {
        this.game = game;
        revalidateModel();
    }

    /**
     * recupere l'Opjet responsable dela correspondance entre Modeles et Vues
     *
     * @return l'instance locale viewMapper
     */
    public GameRendererManagerMap getViewMapper() {
        return viewMapper;
    }
}
