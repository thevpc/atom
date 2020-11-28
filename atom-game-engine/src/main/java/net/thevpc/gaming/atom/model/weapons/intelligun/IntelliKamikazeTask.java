package net.thevpc.gaming.atom.model.weapons.intelligun;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.thevpc.gaming.boardgame.model.weapons.intelligun;
//
//import net.thevpc.gaming.boardgame.business.SpriteMainTask;
//import net.thevpc.gaming.boardgame.model.Sprite;
//import net.thevpc.gaming.util.ModelPoint;
//import net.thevpc.gaming.util.Geom2DUtils;
//
///**
// *
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// */
//public class IntelliKamikazeTask implements SpriteMainTask {
//
//    private Sprite target;
//
//    public IntelliKamikazeTask(Sprite point) {
//        this.target = point;
//    }
//
//    public void taskStep(Sprite sprite) {
//        ModelPoint stepPoint = Geom2DUtils.nextLinePoint(sprite.getPosition(), target.getPosition(), sprite.getMovementSpeed());
//        sprite.setPosition(stepPoint);
//        for (Sprite otherSprite : sprite.getModel().findSprites(stepPoint)) {
//            if (otherSprite.getPlayerId() != sprite.getPlayerId()) {
//                if (sprite.attack((Unit)otherSprite)>=0) {
//                    sprite.setLife(0);
//                }
//            }
//        }
//    }
//}
