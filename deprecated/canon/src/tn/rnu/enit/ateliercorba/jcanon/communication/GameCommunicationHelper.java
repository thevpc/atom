package tn.rnu.enit.ateliercorba.jcanon.communication;

import jcanon.*;
import tn.rnu.enit.ateliercorba.jcanon.model.CanonGameModel;
import tn.rnu.enit.ateliercorba.jcanon.model.*;

/**
 * Classe utilitaire qui permet de convertir entre CanonGameModel et CorbaGame et vice versa.
 *
 * Cette classe permet de separer la logique du jeu de la technique d'implementation de la
 * distribution. En Effet la classe CorbaGame est specifique a l'implementation Corba alors
 * que CanonGameModel ne l'est pas. Donc ainsi si l'on veut utiliser RMI, RPC, SOAP ou
 * toute autre technologie de  * distribution, les packages gameengine, model et view
 * ne changerait pas du tout
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 10 dec. 2006 23:34:49
 */
public class GameCommunicationHelper {

    /**
     * convertit un objet CorbaGame en un objet CanonGameModel
     * @param g une instance CorbaGame
     * @return l'instance convertie en CanonGameModel
     */
    public static CanonGameModel fromCorba(CorbaGame g) {
        CanonGameModel model = new CanonGameModel();
        switch(g.gameStatus.value()){
            case CorbaGameStatus._GAME_INIT:{
                model.setStatus(CanonGameModel.GameStatus.GAME_INIT);
                break;
            }
            case CorbaGameStatus._GAME_STARTED:{
                model.setStatus(CanonGameModel.GameStatus.GAME_STARTED);
                break;
            }
            case CorbaGameStatus._GAME_OVER:{
                model.setStatus(CanonGameModel.GameStatus.GAME_OVER);
                break;
            }
        }
        for (int i = 0; i < g.canons.length; i++) {
            model.addSprite(
                    new PlayerSprite(
                            g.canons[i].player,
                            g.canons[i].life,
                            g.canons[i].score,
                            g.canons[i].x,
                            g.canons[i].y,
                            g.canons[i].mood
                    ));
        }

        for (int i = 0; i < g.fires.length; i++) {
            model.addSprite(new FireSprite(
                    g.fires[i].player,
                    g.fires[i].x,
                    g.fires[i].y,
                    g.fires[i].mood
            ));
        }

        for (int i = 0; i < g.planes.length; i++) {
            model.addSprite(
                    g.planes[i].type.equals(CorbaPlaneType.ONE) ?

                            new PlaneSpriteOne(
                                    g.planes[i].status.equals(CorbaPlaneStatus.ALIVE) ? PlaneSprite.Status.ALIVE : PlaneSprite.Status.FALLING,
                                    g.planes[i].x,
                                    g.planes[i].y,
                                    g.planes[i].mood
                            ) :
                            new PlaneSpriteTwo(
                                    g.planes[i].status.equals(CorbaPlaneStatus.ALIVE) ? PlaneSprite.Status.ALIVE : PlaneSprite.Status.FALLING,
                                    g.planes[i].x,
                                    g.planes[i].y,
                                    g.planes[i].mood
                            )
            );
        }

        for (int i = 0; i < g.bombs.length; i++) {
            model.addSprite(
                    g.bombs[i].type.equals(CorbaPlaneBombType.SIMPLE) ?
                            new PlaneBombSprite(
                                    g.bombs[i].x,
                                    g.bombs[i].y,
                                    g.bombs[i].mood
                            ) :
                            null
            );
        }

        return model;
    }

    /**
     * convertit un objet CanonGameModel en un objet CorbaGame
     * @param canonGame une instance CanonGameModel
     * @return l'instance convertie en CorbaGame
     */
    public static CorbaGame toCorba(CanonGameModel canonGame) {
        synchronized (canonGame) {
            CorbaGame g = new CorbaGame();
            switch(canonGame.getStatus()){
                case GAME_INIT:{
                    g.gameStatus= CorbaGameStatus.GAME_INIT;
                    break;
                }
                case GAME_STARTED:{
                    g.gameStatus= CorbaGameStatus.GAME_STARTED;
                    break;
                }
                case GAME_OVER:{
                    g.gameStatus= CorbaGameStatus.GAME_OVER;
                    break;
                }
            }

            PlayerSprite[] playerSprites = canonGame.getCanons();
            g.canons = new CorbaCanon[playerSprites.length];
            for (int i = 0; i < g.canons.length; i++) {
                g.canons[i] = new CorbaCanon(
                        playerSprites[i].getPlayer(),
                        playerSprites[i].getScore(),
                        playerSprites[i].getX(),
                        playerSprites[i].getY(),
                        playerSprites[i].getLife(),
                        playerSprites[i].getMood()
                );
            }

            FireSprite[] fireSprites = canonGame.getFires();
            g.fires = new CorbaFire[fireSprites.length];
            for (int i = 0; i < g.fires.length; i++) {
                g.fires[i] = new CorbaFire(
                        fireSprites[i].getPlayer(),
                        fireSprites[i].getX(),
                        fireSprites[i].getY(),
                        fireSprites[i].getMood()
                );
            }

            PlaneSprite[] planeSprites = canonGame.getPlanes();
            g.planes = new CorbaPlane[planeSprites.length];
            for (int i = 0; i < g.planes.length; i++) {
                g.planes[i] = new CorbaPlane(
                        planeSprites[i].getX(),
                        planeSprites[i].getY(),
                        planeSprites[i] instanceof PlaneSpriteOne ? CorbaPlaneType.ONE : CorbaPlaneType.TWO,
                        planeSprites[i].getStatus() == PlaneSprite.Status.ALIVE ? CorbaPlaneStatus.ALIVE : CorbaPlaneStatus.FALLING,
                        planeSprites[i].getMood()
                );
            }
            PlaneBombSprite[] planeBombSprites = canonGame.getBombs();
            g.bombs = new CorbaPlaneBomb[planeBombSprites.length];
            for (int i = 0; i < g.bombs.length; i++) {
                g.bombs[i] = new CorbaPlaneBomb(
                        planeBombSprites[i].getX(),
                        planeBombSprites[i].getY(),
                        planeBombSprites[i] instanceof PlaneBombSprite ? CorbaPlaneBombType.SIMPLE : CorbaPlaneBombType.SIMPLE,
                        planeBombSprites[i].getMood()
                );
            }
            return g;
        }
    }
}
