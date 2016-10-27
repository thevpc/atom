/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.shared.dal.corba;

import tn.edu.eniso.pong.main.shared.dal.corba.generated.ModelCorba;
import tn.edu.eniso.pong.main.shared.dal.corba.generated.SpriteCorba;
import tn.edu.eniso.pong.main.shared.dal.model.DALStructModel;
import tn.edu.eniso.pong.main.shared.dal.model.DALStructSprite;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALUtilCorba {

    public static SpriteCorba toSpriteCorba(DALStructSprite s) {
        return s == null ? new SpriteCorba(0, 0, 0, 0, 0) : new SpriteCorba(s.life, s.x, s.y, s.direction, s.speed);
    }

    public static DALStructSprite toDALStructSprite(SpriteCorba s) {
        return new DALStructSprite(s.life, s.x, s.y, s.direction, s.speed);
    }

    public static DALStructModel toDALStructModel(ModelCorba data) {
        DALStructModel d = new DALStructModel();
        d.frame = data.frame;
        d.ball = toDALStructSprite(data.ball);
        d.paddle1 = toDALStructSprite(data.paddle1);
        d.paddle2 = toDALStructSprite(data.paddle2);
        return d;
    }

    public static ModelCorba toModelCorba(DALStructModel data) {
        ModelCorba d = new ModelCorba();
        d.frame = data.frame;
        d.ball = toSpriteCorba(data.ball);
        d.paddle1 = toSpriteCorba(data.paddle1);
        d.paddle2 = toSpriteCorba(data.paddle2);
        return d;
    }

}
