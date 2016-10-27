/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.shared.dal.sockets;

import net.vpc.gaming.atom.model.ModelPoint;
import tn.edu.eniso.pong.main.shared.dal.model.DALStructModel;
import tn.edu.eniso.pong.main.shared.dal.model.DALStructSprite;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALUtilByteArray {

    public static final int DALDATA_SPRITE_SIZE = 4 + 8 * 4;
    public static final int DALDATA_SIZE = 8 + DALDATA_SPRITE_SIZE * 3;
//    public static void main(String[] args) {
//        char x = 'L';
//        byte[] aa = new byte[50];
//        writeChar(x, aa, new IntHolder(3));
//        System.out.println(readChar(aa, new IntHolder(3)));
//    }

    public static byte[] toByteArray(DALStructModel data) {
        byte[] buffer = new byte[DALDATA_SIZE];
        IntHolder intref = new IntHolder();
        writeLong(data.frame, buffer, intref);
        writeSprite(data.ball, buffer, intref);
        writeSprite(data.paddle1, buffer, intref);
        writeSprite(data.paddle2, buffer, intref);
        return buffer;
    }

    public static DALStructModel toDALData(byte[] buffer) {
        DALStructModel d = new DALStructModel();
        IntHolder intref = new IntHolder();
        d.frame = readLong(buffer, intref);
        d.ball = readSprite(buffer, intref);
        d.paddle1 = readSprite(buffer, intref);
        d.paddle2 = readSprite(buffer, intref);

        return d;
    }

    public static class IntHolder {

        int value;

        public IntHolder() {
        }

        public IntHolder(int value) {
            this.value = value;
        }
    }

    public static ModelPoint readDPoint(byte[] bytes, IntHolder index) {
        return new ModelPoint(readDouble(bytes, index), readDouble(bytes, index));
    }

    public static DALStructSprite readSprite(byte[] bytes, IntHolder index) {
        int life = readInt(bytes, index);
        double x = readDouble(bytes, index);
        double y = readDouble(bytes, index);
        double direction = readDouble(bytes, index);
        double speed = readDouble(bytes, index);
        return life == 0 ? null : new DALStructSprite(life, x, y, direction, speed);
    }

    public static void writeSprite(DALStructSprite p, byte[] bytes, IntHolder index) {
        if (p == null) {
            writeInt(0, bytes, index);
            writeDouble(Double.NaN, bytes, index);
            writeDouble(Double.NaN, bytes, index);
            writeDouble(Double.NaN, bytes, index);
            writeDouble(Double.NaN, bytes, index);
        } else {
            writeInt(p.life, bytes, index);
            writeDouble(p.x, bytes, index);
            writeDouble(p.y, bytes, index);
            writeDouble(p.direction, bytes, index);
            writeDouble(p.speed, bytes, index);
        }
    }

    public static void writeDouble(double value, byte[] bytes, IntHolder index) {
        long longValue = Double.doubleToRawLongBits(value);
        writeLong(longValue, bytes, index);
    }

    public static double readDouble(byte[] bytes, IntHolder index) {
        return Double.longBitsToDouble(readLong(bytes, index));
    }

    public static long readLong(byte[] bytes, IntHolder index) {
        long value = 0;
        int ii = index.value;
        for (int i = 0; i < 8; i++) {
            value = (value << 8) + (bytes[i + ii] & 0xff);
        }
        index.value += 8;
        return value;
    }

    public static int readInt(byte[] bytes, IntHolder index) {
        int value = 0;
        int ii = index.value;

//        for (int i = 0; i < 4; i++) {
//            value += (bytes[i + ii] & 0xff) << (8 * i);
//        }
        for (int i = 0; i < 4; i++) {
            value = (value << 8) + (bytes[i + ii] & 0xff);
        }
        index.value += 4;
        return value;
    }

    public static char readChar(byte[] bytes, IntHolder index) {
        char value = 0;
        int ii = index.value;
        for (int i = 0; i < 2; i++) {
            value = (char) ((value << 8) + (bytes[i + ii] & 0xff));
        }
        index.value += 8;
        return value;
    }

    public static void writeChar(char value, byte[] bytes, IntHolder index) {
        int intValue = (int) value;
        bytes[index.value + 0] = (byte) ((intValue >> 8) & 0xff);
        bytes[index.value + 1] = (byte) ((intValue >> 0) & 0xff);
        index.value += 2;
    }

    public static void writeLong(long value, byte[] bytes, IntHolder index) {
        bytes[index.value + 0] = (byte) ((value >> 56) & 0xff);
        bytes[index.value + 1] = (byte) ((value >> 48) & 0xff);
        bytes[index.value + 2] = (byte) ((value >> 40) & 0xff);
        bytes[index.value + 3] = (byte) ((value >> 32) & 0xff);
        bytes[index.value + 4] = (byte) ((value >> 24) & 0xff);
        bytes[index.value + 5] = (byte) ((value >> 16) & 0xff);
        bytes[index.value + 6] = (byte) ((value >> 8) & 0xff);
        bytes[index.value + 7] = (byte) ((value >> 0) & 0xff);
        index.value += 8;
    }

    public static void writeInt(int value, byte[] bytes, IntHolder index) {
        int ival = index.value;
        bytes[ival + 0] = (byte) ((value >> 24) & 0xff);
        bytes[ival + 1] = (byte) ((value >> 16) & 0xff);
        bytes[ival + 2] = (byte) ((value >> 8) & 0xff);
        bytes[ival + 3] = (byte) ((value >> 0) & 0xff);
        index.value += 4;
    }
}
