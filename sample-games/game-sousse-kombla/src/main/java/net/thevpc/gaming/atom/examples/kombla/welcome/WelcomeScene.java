/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.kombla.welcome;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.annotations.OnInit;
import net.thevpc.gaming.atom.model.Orientation;
import net.thevpc.gaming.atom.model.ViewBox;
import net.thevpc.gaming.atom.model.ViewPoint;
import net.thevpc.gaming.atom.presentation.DefaultScene;
import net.thevpc.gaming.atom.presentation.components.SButton;
import net.thevpc.gaming.atom.presentation.components.SLabel;
import net.thevpc.gaming.atom.presentation.components.SList;
import net.thevpc.gaming.atom.presentation.components.STextField;
import net.thevpc.gaming.atom.presentation.layers.FillScreenGradientLayer;

import java.awt.*;

/**
 * Welcome Scene define the Game Mode Selection : Local, Host or Join
 * and let the Player choosing via simple Widget layout.
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
@AtomScene(
        id = "welcome",
        sceneEngine = "welcome",
        title = "Kombla",
        tileWidth = 600,
        tileHeight = 400
)
public class WelcomeScene extends DefaultScene {

    /**
     * Combobox (List) of the available modes
     */
    private final SList modesList = new SList("modesList");

    /**
     * Start Button
     */
    private final SButton start = new SButton("start", "Press any key to start...");

    /**
     * Title Label
     */
    private SLabel title;

    /**
     * Server Address Text Field used for specifying Remote Server address on Clients
     */
    private final STextField serverAddress = new STextField("serverAddress", "localhost");

    private final STextField playerName = new STextField("playerName", "boss");

    /**
     * Server Address Label
     */
    private final SLabel serverAddressLabel = new SLabel("serverAddressLabel", "Server Address");

    /**
     * Server Port Text Field used for specifying Remote Server port on Clients and listening port for Server
     */
    private final STextField serverPort = new STextField("serverPort", "1234");

    /**
     * Server Port Label
     */
    private final SLabel serverPortLabel = new SLabel("serverPortLabel", "Server Port");
    private final SLabel playerNameLabel = new SLabel("playerNameLabel", "Player Name");

    /**
     * Main constructor
     */
    public WelcomeScene() {

    }

    @OnInit
    public void onInstall() {
        // Fill Background with Gradient colored Layer
        addLayer(new FillScreenGradientLayer(new Color(200, 230, 126), Color.DARK_GRAY, Orientation.NORTH_EAST));

        // register custom controller to process key bindings
//        addSceneController(new WelcomeController());

        // initialize modesList with possible values
        // each value defines the "Object Value" to consider and the "Object Label" to show
        modesList.add(AppRole.LOCAL_GAME, "Local Game");
        modesList.add(AppRole.HOST_GAME, "Host Game");
        modesList.add(AppRole.JOIN_GAME, "Join Game");
        // define component position on screen in pixels
        modesList.setBounds(new ViewBox(200, 120, 200, 30));
        // define selected value of the list to be the virst value aka AppRole.LOCAL_GAME
        modesList.setSelectedIndex(0);
        // force component to gain focus (accept key pressed)
        modesList.setFocused(true);
        // configure style (colors, fonts, ...)
        AppStyles.prepareList1(modesList);


        // configure style (colors, fonts, ...)
        int labelX = 70 + 30;
        int textFieldX = 250 + 20;
        int row1Y = 240;
        int row2Y = 280;
        int row3Y = 320;
        playerNameLabel.setBounds(new ViewBox(labelX, row1Y, 200, 30));
        AppStyles.prepareLabel1(playerNameLabel);

        // define component position on screen in pixels
        // configure Text Field columns to show
        // configure style (colors, fonts, ...)
        playerName.setBounds(new ViewBox(textFieldX, row1Y, 200, 30))
                .setColumns(15);
        AppStyles.prepareTextField1(playerName);


        // configure style (colors, fonts, ...)
        serverAddressLabel.setBounds(new ViewBox(labelX, row2Y, 200, 30));
        AppStyles.prepareLabel1(serverAddressLabel);

        // define component position on screen in pixels
        // configure Text Field columns to show
        // configure style (colors, fonts, ...)
        serverAddress.setBounds(new ViewBox(textFieldX, row2Y, 200, 30))
                .setColumns(15);
        AppStyles.prepareTextField1(serverAddress);

        // define component position on screen in pixels
        // configure style (colors, fonts, ...)
        serverPortLabel.setBounds(new ViewBox(labelX, row3Y, 200, 30));
        AppStyles.prepareLabel1(serverPortLabel);

        // define component position on screen in pixels
        // configure Text Field columns to show
        // configure style (colors, fonts, ...)
        serverPort.setBounds(new ViewBox(textFieldX, row3Y, 200, 30))
                .setColumns(12);
        AppStyles.prepareTextField1(serverPort);

        // To setup start button to be centered in the bottom part of the screen
        // first retrieve Camera ViewPoint
        // than figure the bottom part (r2) and bind it to start button
        ViewBox vp = getCamera().getViewBounds();
        ViewBox r2 = new ViewBox(vp.getX(), vp.getY() + vp.getHeight() * 2 / 3, vp.getWidth(), vp.getHeight() / 3);
        // define component position on screen in pixels
        start.setBounds(r2);
        // configure style (colors, fonts, ...)
        AppStyles.prepareButton1(start);

        title = new SLabel("Kombla")
                .setBounds(new ViewBox(0, 0, getCamera().getViewPort().getWidth(), 100))
                .setLocation(new ViewPoint(vp.getX(), vp.getY()));
        // configure style (colors, fonts, ...)
        AppStyles.prepareTitle1(title);

        // add components for display
        addComponent(modesList);
        addComponent(playerNameLabel);
        addComponent(playerName);
        addComponent(serverAddressLabel);
        addComponent(serverAddress);
        addComponent(serverPortLabel);
        addComponent(serverPort);
        addComponent(start);
        addComponent(title);
    }

    /**
     * override default processing to do each frame to
     * update visibility of components.
     * Actually serverPort should be available if Host or Join Mode
     */
    @Override
    protected void update() {
        AppRole role = (AppRole) modesList.getSelectedValue();
        boolean join = role == AppRole.JOIN_GAME;
        boolean host = role == AppRole.HOST_GAME;
        serverAddress.setVisible(join);
        serverAddressLabel.setVisible(join);
        serverPort.setVisible(join || host);
        serverPortLabel.setVisible(join || host);
    }
}
