/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.examples.pong.main.shared.dal.sockets;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DALUtilMulticast {

    public static final String DEFAULT_ADDRESS = "230.230.230.230";
    private static final byte[] BURST = "*Never203#".getBytes();
    public static final int BURST_SIZE = BURST.length;

    public static byte[] createBurst() {
        byte[] copy = new byte[BURST_SIZE];
        System.arraycopy(BURST, 0, copy, 0, BURST_SIZE);
        return copy;
    }

    public static boolean isBurst(byte[] bytes) {
        if (bytes.length < BURST_SIZE) {
            return false;
        }
        for (int i = 0; i < BURST_SIZE; i++) {
            if (bytes[i] != BURST[i]) {
                return false;
            }
        }
        return true;
    }
}
