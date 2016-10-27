package net.vpc.gaming.atom.presentation;

import net.vpc.gaming.atom.model.Orientation;
import net.vpc.gaming.atom.model.Sprite;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/15/13
 * Time: 12:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class SpriteController extends DefaultSceneController {
    private Sprite sprite;
    private String spriteName;

    public SpriteController(Sprite sprite) {
        this.sprite = sprite;
    }

    public SpriteController(String sprite) {
        this.spriteName = sprite;
    }

    @Override
    public void keyPressed(SceneKeyEvent e) {
        if(spriteName!=null){
            for (Sprite sp : e.getScene().getSceneEngine().findSpritesByName(spriteName)) {
                keyPressed(sp,e.getKeyCode());
            }
        }else{
            keyPressed(sprite,e.getKeyCode());
        }
    }

    public void keyPressed(Sprite sprite,int keyCode) {
        switch (keyCode) {
            case SceneKeyEvent.VK_LEFT: {
                sprite.setDirection(Orientation.WEST);
                break;
            }
            case SceneKeyEvent.VK_RIGHT: {
                sprite.setDirection(Orientation.EAST);
                break;
            }
            case SceneKeyEvent.VK_UP: {
                sprite.setDirection(Orientation.NORTH);
                break;
            }
            case SceneKeyEvent.VK_DOWN: {
                sprite.setDirection(Orientation.SOUTH);
                break;
            }

        }
    }
}
