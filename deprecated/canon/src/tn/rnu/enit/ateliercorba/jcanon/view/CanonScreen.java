package tn.rnu.enit.ateliercorba.jcanon.view;

import tn.rnu.enit.ateliercorba.jcanon.gameengine.view.SpriteContainer;
import tn.rnu.enit.ateliercorba.jcanon.model.PlayerSprite;
import tn.rnu.enit.ateliercorba.jcanon.model.CanonGameModel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 11 dec. 2006 02:44:40
 */
public class CanonScreen extends SpriteContainer {
    CanonGameView view;
    public CanonScreen(CanonGameView view) {
        super(view.getViewMapper(),view.getGame().getWidth(),view.getGame().getHeight());
        this.view=view;
        setFont(new JLabel().getFont().deriveFont(Font.BOLD,16));
    }


    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2=(Graphics2D) g;
        CanonGameModel model = view.getGame();
        PlayerSprite player1 = model.getPlayer(1);
        PlayerSprite player2 = model.getPlayer(2);
        if(player1!=null){
            g2.setColor(Color.RED);
            if(player1.isDead()){
                g2.drawString("Player 1 DEAD : Score "+player1.getScore(),5,15);
            }else{
                g2.drawString("Player 1 : Life "+ player1.getLife()+" Score "+player1.getScore(),5,15);
            }
        }
        if(player2!=null){
            g2.setColor(Color.GRAY);
            if(player2.isDead()){
                g2.drawString("Player 2 DEAD : Score "+player2.getScore(),5,35);
            }else{
                g2.drawString("Player 2 : Life "+ player2.getLife()+" Score "+player2.getScore(),5,35);
            }
        }
        switch(model.getStatus()){
            case GAME_INIT:{
                String message="WAITING FOR SECOND PLAYER";
                g2.setFont(getFont().deriveFont(Font.BOLD, 42f));
                g2.setColor(Color.RED);
                g2.drawString(message,200,200);
                g2.setColor(Color.YELLOW);
                g2.drawString(message,202,202);
                break;
            }
            case GAME_STARTED:{

                break;
            }
            case GAME_OVER:{
                String message="GAME OVER";
                g2.setFont(getFont().deriveFont(Font.BOLD, 56f));
                g2.setColor(Color.RED);
                g2.drawString(message,200,200);
                g2.setColor(Color.YELLOW);
                g2.drawString(message,202,202);
                break;
            }
        }
    }
}
