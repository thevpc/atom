package tn.edu.eniso.soussecraft.main.presentation.view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.vpc.gaming.atom.annotations.AtomScene;
import net.vpc.gaming.atom.debug.DebugLayer;
import net.vpc.gaming.atom.debug.DebugTilesHeatLayer;
import net.vpc.gaming.atom.model.*;
import net.vpc.gaming.atom.model.armors.InvincibleSpriteArmor;
import net.vpc.gaming.atom.model.armors.LevelSpriteArmor;
import net.vpc.gaming.atom.model.weapons.gun.Bullet;
import net.vpc.gaming.atom.model.weapons.gun.GunWeapon;
import net.vpc.gaming.atom.model.weapons.intelligun.SmartBullet;
import net.vpc.gaming.atom.model.weapons.intelligun.SmartGunWeapon;
import net.vpc.gaming.atom.model.weapons.knife.KnifeWeapon;
import net.vpc.gaming.atom.presentation.Alignment;
import net.vpc.gaming.atom.presentation.DefaultScene;
import net.vpc.gaming.atom.presentation.ImageGrid;
import net.vpc.gaming.atom.presentation.ImageMatrixProducer;
import net.vpc.gaming.atom.presentation.armors.InvincibleArmorView;
import net.vpc.gaming.atom.presentation.armors.LevelArmorView;
import net.vpc.gaming.atom.presentation.components.MiniMap;
import net.vpc.gaming.atom.presentation.components.SLabel;
import net.vpc.gaming.atom.presentation.components.SceneComponentState;
import net.vpc.gaming.atom.presentation.components.TextStyle;
import net.vpc.gaming.atom.presentation.dashboard.GunView;
import net.vpc.gaming.atom.presentation.dashboard.KnifeView;
import net.vpc.gaming.atom.presentation.dashboard.SmartGunView;
import net.vpc.gaming.atom.presentation.layers.BordersScrollLayer;
import net.vpc.gaming.atom.presentation.layers.FillScreenColorLayer;
import net.vpc.gaming.atom.presentation.layers.Layer;
import net.vpc.gaming.atom.presentation.layers.SpritesSelectionLayer;
import net.vpc.gaming.atom.presentation.weapons.BulletView;
import tn.edu.eniso.soussecraft.hello.business.WelcomeEngine;
import tn.edu.eniso.soussecraft.hello.model.AppRole;
import tn.edu.eniso.soussecraft.main.business.MainEngine;
import tn.edu.eniso.soussecraft.main.model.GamePhase;
import tn.edu.eniso.soussecraft.main.model.etc.AttackLocationIndicator;
import tn.edu.eniso.soussecraft.main.model.etc.GatherLocationIndicator;
import tn.edu.eniso.soussecraft.main.model.etc.GotoLocationIndicator;
import tn.edu.eniso.soussecraft.main.model.resources.Minerals;
import tn.edu.eniso.soussecraft.main.model.resources.Woods;
import tn.edu.eniso.soussecraft.main.model.structures.Atomium;
import tn.edu.eniso.soussecraft.main.model.structures.CommandCenter;
import tn.edu.eniso.soussecraft.main.model.units.Worker;
import tn.edu.eniso.soussecraft.main.presentation.controller.MainController;
import tn.edu.eniso.soussecraft.main.presentation.view.etc.AttackLocationIndicatorView;
import tn.edu.eniso.soussecraft.main.presentation.view.etc.GatherLocationIndicatorView;
import tn.edu.eniso.soussecraft.main.presentation.view.etc.GotoLocationIndicatorView;
import tn.edu.eniso.soussecraft.main.presentation.view.layers.DashboardLayer;
import tn.edu.eniso.soussecraft.main.presentation.view.layers.DetailsComponent;
import tn.edu.eniso.soussecraft.main.presentation.view.layers.ScoreLayer;
import tn.edu.eniso.soussecraft.main.presentation.view.resources.MineralsView;
import tn.edu.eniso.soussecraft.main.presentation.view.resources.WoodsView;
import tn.edu.eniso.soussecraft.main.presentation.view.structures.AtomiumView;
import tn.edu.eniso.soussecraft.main.presentation.view.structures.CommandCenterView;
import tn.edu.eniso.soussecraft.main.presentation.view.units.WorkerView;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com) @creationtime 5 dec. 2006
 * 11:30:31
 */
@AtomScene(id = "welcomeServer,welcomeClient")
public class MainScene extends DefaultScene {

    private SLabel message = new SLabel("");
    public static final ImageMatrixProducer WALL_IMAGE_PRODUCER = new ImageMatrixProducer("/wall.jpg", new ImageGrid(3, 5)
    ) {
        @Override
        public Image getImage(int type, int index) {
            if (index == 0) {
                index = 3;//
            } else {
                index = 14;//11+index%4;
            }
            return super.getImage(type, index);
        }
    };

    /**
     * Seul Constructeur de la classe l'indexe du joueur sert entre autre a
     * savoir par exemple quelle couleur attribuer au sprite du joueur
     *
     */
    public MainScene() {
        //taille d'un tile (motif) en pixels
        super(15, 15);
        setImageProducer(WALL_IMAGE_PRODUCER);
        //vue 3D simulee
        setIsometric(true);

        //seule une zone de 50% de largeur et de longeur est visible (donc nessite un ascenceur)
        getCamera().setBounds(new RatioViewBox(0,0,0.5f, 0.5f));

        addSpriteViews();

        addDashboard();

        addGameLayers();

//        addDebugLayers();

        addController(new MainController());

        Font verdana24B = new Font("verdana", Font.BOLD, 24);

        message.setAlignment(Alignment.CENTER);
        message.setTextStyle(SceneComponentState.DEFAULT, new TextStyle("default")
                .setForeColor(Color.WHITE)
                .setFont(verdana24B)
                .setBlinkPeriod(10)
        );
        message.setVisible(false);
        addComponent(message, null, Layer.SCREEN_FRONTEND_LAYER);

    }

    @Override
    protected void sceneStarted() {
        ViewBox vp = getCamera().getViewPort();
        message.setBounds(new ViewBox(vp.getX(), vp.getY() + vp.getHeight() * 2 / 3, vp.getWidth(), vp.getHeight() / 3));
        MainEngine e = getSceneEngine();
        Player p = getControlPlayer();
        if (p != null) {
            playerIdentified(p.getId());
        }
        phaseChanged(((MainEngine)getSceneEngine()).getGamePhase());

        getModel().addPropertyChangeListener("controlPlayer", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                playerIdentified((Integer) evt.getNewValue());
            }
        });
        //TODO fix me
        getModel().addPropertyChangeListener("gamePhase", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                phaseChanged((GamePhase) evt.getNewValue());
            }
        });
    }

    private void phaseChanged(GamePhase phase) {
        MainEngine model = getSceneEngine();
        switch (phase) {
            case CONNECTING: {
                //WelcomeModel wmodel = getSceneEngine().getGameEngine().getSceneEngine(WelcomeEngine.class).getModel();
                if (isServer()) {
                    message.setText("Waiting for clients...");
                } else {
                    message.setText("Waiting for server...");
                }
                message.setVisible(true);
                break;
            }
            case STARING: {
                message.setText((model.getPlayers().size() - 1) + " player(s) joined game. press SPACE to start...");
                message.setVisible(true);
                break;
            }
            case STARTED: {
                message.setVisible(false);
                centerToPlayer();
                break;
            }
        }
    }

    private void playerIdentified(int player) {
        resetControlPlayers();
        addControlPlayer(player);
        message.setText("Waiting for players...");
        message.setVisible(true);
    }

    private void centerToPlayer() {
        switch (getControlPlayer().getId()) {
            case 1: {
                getCamera().moveTo(new RatioPoint(0, 0));
                break;
            }
            case 2: {
                getCamera().moveTo(new RatioPoint(0.5f, 0));
                break;
            }
            case 3: {
                getCamera().moveTo(new RatioPoint(0, 0.5f));
                break;
            }
            case 4: {
                getCamera().moveTo(new RatioPoint(0.5f, 0.5f));
                break;
            }
        }
    }

    public boolean isServer() {
        //server is always the first
        WelcomeEngine scene = getSceneEngine().getGameEngine().getScene(WelcomeEngine.class);
        return scene.getRole() == AppRole.HOST_GAME;
    }

    /**
     * association entre modeles et vues
     */
    private void addSpriteViews() {
        setSpriteView(Bullet.class,new BulletView());
        setSpriteView(Worker.class,new WorkerView());

        setSpriteView(Minerals.class,new MineralsView());
        setSpriteView(Woods.class,new WoodsView());

        setSpriteView(CommandCenter.class,new CommandCenterView());
        setSpriteView(Atomium.class,new AtomiumView() );

        setSpriteView(GotoLocationIndicator.class,new GotoLocationIndicatorView());
        setSpriteView(AttackLocationIndicator.class,new AttackLocationIndicatorView());
        setSpriteView(GatherLocationIndicator.class,new GatherLocationIndicatorView());

    }

    private void addDashboard() {
        setViewBinding(KnifeWeapon.class, new KnifeView());
        setViewBinding(GunWeapon.class, new GunView());
        setViewBinding(Bullet.class, new BulletView());
        setViewBinding(SmartBullet.class, new BulletView());
        setViewBinding(SmartGunWeapon.class, new SmartGunView());
        setViewBinding(LevelSpriteArmor.class, new LevelArmorView());
        setViewBinding(InvincibleSpriteArmor.class, new InvincibleArmorView());

        //affichage de la barre de taches
        addLayer(new DashboardLayer());
        addComponent(new MiniMap(new ViewDimension(80, 80)), new Point(10, 600 - DashboardLayer.DASHBOARD_SIZE.height / 2 - 80 / 2), Layer.SCREEN_DASHBOARD_LAYER);
        addComponent(new DetailsComponent(new ViewDimension(400, 200)), new ViewPoint(100, 600 - 200), Layer.SCREEN_DASHBOARD_LAYER);
    }

    /**
     * definition de toutes les couches du jeu (depuis l'arriere plan jusqu'à la
     * couche de score)
     */
    private void addGameLayers() {
        //ecran noir
        addLayer(new FillScreenColorLayer(Color.BLACK));

        //affichage du score
        addLayer(new ScoreLayer());

        // definition de l'arriere plan (les motifs, tiles⁾
//        try {
//
//            addLayer(new CellMapLayer(
//                    //MAP FILE
//                    getClass().getResource("../../model/" + MainModel.BACKGROUND_ID + ".map"),
//                    //IMAGE FILE
//                    getClass().getResource(MainModel.BACKGROUND_ID + ".png")));
//        } catch (IOException ex) {
//            throw new IllegalArgumentException("Should never happen, perhaps bad map file");
//        }



        //affichage des sprites en selection
        addLayer(new SpritesSelectionLayer());

        //definition de la zone de scrolling par les bords
        addLayer(new BordersScrollLayer());


//        addAreaLayer(new BlackBoardLayer());

        //ne plus afficher la background
//        removeAreaLayer(new BackgroundLayer());
    }

    private void addDebugLayers() {
        //*******************************
        // information de debuggage
        //*******************************


        //affichage des chemins
        addLayer(new DebugLayer());

        //affichage de la map de chaleur
        addLayer(new DebugTilesHeatLayer());

        //afficher la zone de detection du scrolling
//        addLayer(new DebugBordersScrollZonesLayer());

        //afficher la grille des tiles
//        addLayer(new DebugTilesGridLayer());

        //afficher la grille des tiles
//        addLayer(new DebugLastClickedTileLayer());

        //afficher les murs
//        addLayer(new DebugWallsLayer());

        //afficher des infos sur les sprites
//        addLayer(new DebugSpritesLocationCellsLayer());
//        addLayer(new DebugSpritesLocationTextLayer());

    }
}
