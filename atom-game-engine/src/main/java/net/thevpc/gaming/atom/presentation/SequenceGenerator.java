/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.presentation;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public abstract class SequenceGenerator {

//    public static void main(String[] args) {
//        SequenceGenerator a1 = createUnsignedSequence(3,2);
//        SequenceGenerator a2 = createSignedSequence(3,2);
//        for (int frame = 0; frame < 100; frame++) {
//            System.out.println("frame="+frame);
//            System.out.println("\tA1="+a1.next(frame));
//            System.out.println("\tA2="+a2.next(frame));
//        }
//    }

    /**
     * return pattern index for a given frame
     * createPatternIndexSequence(3,2)
     * will return sequence [1,1,1,2,2] periodically
     *
     * @param pattern pattern
     * @return SequenceGenerator
     */
    public static SequenceGenerator createUnsignedSequence(int... pattern) {
        return new UnsignedSequence(pattern);
    }

    /**
     * return signed pattern index for a given frame
     * sign is positive at the very start of the pattern
     * and negative for all following frames
     * <p/>
     * createSignedPatternIndexSequence(3,2)
     * will return sequence [1,-1,-1,2,-2] periodically
     *
     * @param pattern pattern
     * @return SequenceGenerator
     */
    public static SequenceGenerator createSignedSequence(int... pattern) {
        return new SignedSequence(pattern);
    }

    public abstract int next(long frame);


    private static class UnsignedSequence extends SequenceGenerator {

        private int[] cycle;
        private int[] cumulativeCycles;
        private int length;

        public UnsignedSequence(int... cycle) {
            if (cycle.length <= 0) {
                throw new IllegalArgumentException("empty cycle");
            }
            this.cycle = cycle;
            this.cumulativeCycles = new int[cycle.length];
            for (int i = 0; i < cycle.length; i++) {
                int j = cycle[i];
                length += j;
                if (i > 0) {
                    cumulativeCycles[i] = cycle[i] + cumulativeCycles[i - 1];
                } else {
                    cumulativeCycles[i] = cycle[i];
                }
            }

        }

        /**
         * return one based cycle index
         *
         * @param frame
         * @return
         */
        public int next(long frame) {
            long cycleIdex = frame % length;
            for (int i = 0; i < cumulativeCycles.length; i++) {
                int j = cumulativeCycles[i];
                if (cycleIdex < j) {
                    return i + 1;
                }
            }
            throw new IllegalArgumentException("Never");
        }
    }

    private static class SignedSequence extends SequenceGenerator {

        private int[] cycle;
        private int[] cumulativeCycles;
        private int length;

        public SignedSequence(int... cycle) {
            if (cycle.length <= 0) {
                throw new IllegalArgumentException("empty cycle");
            }
            this.cycle = cycle;
            this.cumulativeCycles = new int[cycle.length];
            for (int i = 0; i < cycle.length; i++) {
                int j = cycle[i];
                length += j;
                if (i > 0) {
                    cumulativeCycles[i] = cycle[i] + cumulativeCycles[i - 1];
                } else {
                    cumulativeCycles[i] = cycle[i];
                }
            }

        }

        public int next(long frame) {
            long cycleIdex = frame % length;
            for (int i = 0; i < cumulativeCycles.length; i++) {
                int j = cumulativeCycles[i];
                if (cycleIdex < j) {
                    if (i == 0) {
                        return cycleIdex == 0 ? 1 : -1;
                    } else {
                        return (cycleIdex == cumulativeCycles[i - 1]) ? (i + 1) : -(i + 1);
                    }
                }
            }
            throw new IllegalArgumentException("Never");
        }
    }
}
