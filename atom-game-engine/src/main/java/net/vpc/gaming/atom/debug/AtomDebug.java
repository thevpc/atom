package net.vpc.gaming.atom.debug;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: vpc
 * Date: 8/18/13
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class AtomDebug {
    public static int DRAW_IMAGE_DRAW_COUNT = 0;
    public static int DRAW_IMAGE_RESCALE_COUNT = 0;

    public static class CheckThreadViolationRepaintManager extends RepaintManager {
        // it is recommended to pass the complete check

        private boolean completeCheck = true;

        public boolean isCompleteCheck() {
            return completeCheck;
        }

        public void setCompleteCheck(boolean completeCheck) {
            this.completeCheck = completeCheck;
        }

        public synchronized void addInvalidComponent(JComponent component) {
            checkThreadViolations(component);
            super.addInvalidComponent(component);
        }

        public void addDirtyRegion(JComponent component, int x, int y, int w, int h) {
            checkThreadViolations(component);
            super.addDirtyRegion(component, x, y, w, h);
        }

        private void checkThreadViolations(JComponent c) {
            if (!SwingUtilities.isEventDispatchThread() && (completeCheck
                    || c.isShowing())) {
                Exception exception = new Exception();
                boolean repaint = false;
                boolean fromSwing = false;
                StackTraceElement[] stackTrace = exception.getStackTrace();
                for (StackTraceElement st : stackTrace) {
                    if (repaint && st.getClassName().startsWith("javax.swing.")) {
                        fromSwing = true;
                    }
                    if ("repaint".equals(st.getMethodName())) {
                        repaint = true;
                    }
                }
                if (repaint && !fromSwing) {
                    //no problems here, since repaint() is thread safe
                    return;
                }
                exception.printStackTrace();
            }
        }
    }

    public static void install() {
        RepaintManager.setCurrentManager(new CheckThreadViolationRepaintManager());
    }

    public static void showImagesFrame(Image[] images) {

        JPanel p = new JPanel(new GridLayout(0, 1));
        for (int i = 0; i < images.length; i++) {
            Image image = images[i];
            p.add(new JLabel("" + (i + 1), new ImageIcon(image), SwingConstants.CENTER));
        }
        JScrollPane b = new JScrollPane(p);
        JFrame f = new JFrame("Debug");
        f.add(b);
        f.setVisible(true);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
