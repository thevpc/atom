package net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.debug.DebugLayer;
import net.thevpc.gaming.atom.engine.SpriteFilter;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.model.armors.InvincibleSpriteArmor;
import net.thevpc.gaming.atom.model.armors.LevelSpriteArmor;
import net.thevpc.gaming.atom.model.weapons.gun.Bullet;
import net.thevpc.gaming.atom.model.weapons.gun.GunWeapon;
import net.thevpc.gaming.atom.model.weapons.intelligun.SmartBullet;
import net.thevpc.gaming.atom.model.weapons.intelligun.SmartGunWeapon;
import net.thevpc.gaming.atom.model.weapons.knife.KnifeWeapon;
import net.thevpc.gaming.atom.presentation.Alignment;
import net.thevpc.gaming.atom.presentation.DefaultScene;
import net.thevpc.gaming.atom.presentation.ImageGrid;
import net.thevpc.gaming.atom.presentation.ImageMatrixProducer;
import net.thevpc.gaming.atom.presentation.armors.InvincibleArmorView;
import net.thevpc.gaming.atom.presentation.armors.LevelArmorView;
import net.thevpc.gaming.atom.presentation.components.MiniMap;
import net.thevpc.gaming.atom.presentation.components.SLabel;
import net.thevpc.gaming.atom.presentation.components.SceneComponentState;
import net.thevpc.gaming.atom.presentation.components.TextStyle;
import net.thevpc.gaming.atom.presentation.dashboard.GunView;
import net.thevpc.gaming.atom.presentation.dashboard.KnifeView;
import net.thevpc.gaming.atom.presentation.dashboard.SmartGunView;
import net.thevpc.gaming.atom.presentation.layers.BordersScrollLayer;
import net.thevpc.gaming.atom.presentation.layers.FillScreenColorLayer;
import net.thevpc.gaming.atom.presentation.layers.Layer;
import net.thevpc.gaming.atom.presentation.layers.SpritesSelectionLayer;
import net.thevpc.gaming.atom.presentation.weapons.BulletView;
import net.thevpc.gaming.atom.examples.soussecraft.hello.business.WelcomeEngine;
import net.thevpc.gaming.atom.examples.soussecraft.hello.model.AppRole;
import net.thevpc.gaming.atom.examples.soussecraft.main.business.MainEngine;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.GamePhase;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.etc.AttackLocationIndicator;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.etc.GatherLocationIndicator;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.etc.GotoLocationIndicator;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.resources.Minerals;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.resources.Woods;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.structures.Atomium;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.structures.CommandCenter;
import net.thevpc.gaming.atom.examples.soussecraft.main.model.units.Worker;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.controller.MainController;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.etc.AttackLocationIndicatorView;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.etc.GatherLocationIndicatorView;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.etc.GotoLocationIndicatorView;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.layers.DashboardLayer;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.layers.DetailsComponent;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.layers.ScoreLayer;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.resources.MineralsView;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.resources.WoodsView;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.structures.AtomiumView;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.structures.CommandCenterView;
import net.thevpc.gaming.atom.examples.soussecraft.main.presentation.view.units.WorkerView;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com) @creationtime 5 dec. 2006
 * 11:30:31
 */
@AtomScene(id = "main")
@AtomSceneEngine(id = "main")
public class MainScene extends DefaultScene {

    private SLabel message = new SLabel("");
    public static final ImageMatrixProducer WALL_IMAGE_PRODUCER = new ImageMatrixProducer(
            "/net/thevpc/gaming/atom/examples/soussecraft/MyBackground.jpg", new ImageGrid(3, 5)
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
        getCamera().setBounds(new RatioBox(0,0,0.5f, 0.5f));

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
        WelcomeEngine scene = getSceneEngine().getGameEngine().getSceneEngine(WelcomeEngine.class);
        return scene.getRole() == AppRole.HOST_GAME;
    }

    /**
     * association entre modeles et vues
     */
    private void addSpriteViews() {
        setSpriteView(SpriteFilter.byType(Bullet.class),new BulletView());
        setSpriteView(SpriteFilter.byType(Worker.class),new WorkerView());

        setSpriteView(SpriteFilter.byType(Minerals.class),new MineralsView());
        setSpriteView(SpriteFilter.byType(Woods.class),new WoodsView());

        setSpriteView(SpriteFilter.byType(CommandCenter.class),new CommandCenterView());
        setSpriteView(SpriteFilter.byType(Atomium.class),new AtomiumView() );

        setSpriteView(SpriteFilter.byType(GotoLocationIndicator.class),new GotoLocationIndicatorView());
        setSpriteView(SpriteFilter.byType(AttackLocationIndicator.class),new AttackLocationIndicatorView());
        setSpriteView(SpriteFilter.byType(GatherLocationIndicator.class),new GatherLocationIndicatorView());

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
        addComponent(new MiniMap(new ViewDimension(80, 80)), new ViewPoint(10, 600 - DashboardLayer.DASHBOARD_SIZE.height / 2 - 80 / 2), Layer.SCREEN_DASHBOARD_LAYER);
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
        addLayer(new DebugLayer(true));

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
