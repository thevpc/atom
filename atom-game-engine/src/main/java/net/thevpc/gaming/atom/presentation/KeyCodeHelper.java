package net.thevpc.gaming.atom.presentation;

import java.awt.event.KeyEvent;

public class KeyCodeHelper {
    public static int toAwtKeyEvent(KeyCode c){
        if(c==null){
            return 0;
        }
        switch (c){
            case A:return KeyEvent.VK_A;
            case B:return KeyEvent.VK_B;
            case C:return KeyEvent.VK_C;
            case D:return KeyEvent.VK_D;
            case E:return KeyEvent.VK_E;
            case F:return KeyEvent.VK_F;
            case G:return KeyEvent.VK_G;
            case H:return KeyEvent.VK_H;
            case I:return KeyEvent.VK_I;
            case J:return KeyEvent.VK_J;
            case K:return KeyEvent.VK_K;
            case L:return KeyEvent.VK_L;
            case M:return KeyEvent.VK_M;
            case N:return KeyEvent.VK_N;
            case O:return KeyEvent.VK_O;
            case P:return KeyEvent.VK_P;
            case Q:return KeyEvent.VK_Q;
            case R:return KeyEvent.VK_R;
            case S:return KeyEvent.VK_S;
            case T:return KeyEvent.VK_T;
            case U:return KeyEvent.VK_U;
            case V:return KeyEvent.VK_V;
            case W:return KeyEvent.VK_W;
            case X:return KeyEvent.VK_X;
            case Y:return KeyEvent.VK_Y;
            case Z:return KeyEvent.VK_Z;
            case NUMPAD0:return KeyEvent.VK_0;
            case NUMPAD1:return KeyEvent.VK_1;
            case NUMPAD2:return KeyEvent.VK_2;
            case NUMPAD3:return KeyEvent.VK_3;
            case NUMPAD4:return KeyEvent.VK_4;
            case NUMPAD5:return KeyEvent.VK_5;
            case NUMPAD6:return KeyEvent.VK_6;
            case NUMPAD7:return KeyEvent.VK_7;
            case NUMPAD8:return KeyEvent.VK_8;
            case NUMPAD9:return KeyEvent.VK_9;
        }
        throw new IllegalArgumentException("please fix me");
    }
}
