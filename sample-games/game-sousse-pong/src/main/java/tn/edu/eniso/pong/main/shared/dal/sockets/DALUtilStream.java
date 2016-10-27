/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.shared.dal.sockets;

import tn.edu.eniso.pong.main.shared.dal.model.DALStructModel;
import tn.edu.eniso.pong.main.shared.dal.model.DALStructSprite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALUtilStream {

    public static void writeDALData(DALStructModel data, DataOutputStream out) throws IOException {
        out.writeLong(data.frame);
        writeSprite(data.ball, out);
        writeSprite(data.paddle1, out);
        writeSprite(data.paddle2, out);

    }

    public static DALStructModel readDALData(DataInputStream in) throws IOException {
        DALStructModel d = new DALStructModel();
        d.frame = in.readLong();
        d.ball = readSprite(in);
        d.paddle1 = readSprite(in);
        d.paddle2 = readSprite(in);
        return d;
    }

    private static DALStructSprite readSprite(DataInputStream in) throws IOException {
        int life = in.readInt();
        double x = in.readDouble();
        double y = in.readDouble();
        double direction = in.readDouble();
        double speed = in.readDouble();
        if (life == 0) {
            //null value
            return null;
        }
        return new DALStructSprite(life, x, y, direction, speed);
    }

    private static void writeSprite(DALStructSprite p, DataOutputStream out) throws IOException {
        if (p != null) {
            out.writeInt(p.life);
            out.writeDouble(p.x);
            out.writeDouble(p.y);
            out.writeDouble(p.direction);
            out.writeDouble(p.speed);
        } else {
            out.writeInt(0);
            out.writeDouble(Double.NaN);
            out.writeDouble(Double.NaN);
            out.writeDouble(Double.NaN);
            out.writeDouble(Double.NaN);
        }
    }

}
