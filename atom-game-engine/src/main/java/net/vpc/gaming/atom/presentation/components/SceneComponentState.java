package net.vpc.gaming.atom.presentation.components;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SceneComponentState implements Comparable<SceneComponentState>{
    private enum Flag {
        DEFAULT,
        DISABLED,
        FOCUSED,
        SELECTED,
        HOVER;
    }
    public static final SceneComponentState DEFAULT = new SceneComponentState(Flag.DEFAULT);
    public static final SceneComponentState DISABLED = new SceneComponentState(Flag.DISABLED);
    public static final SceneComponentState FOCUSED = new SceneComponentState(Flag.FOCUSED);
    public static final SceneComponentState SELECTED = new SceneComponentState(Flag.SELECTED);
    public static final SceneComponentState HOVER = new SceneComponentState(Flag.HOVER);

    private EnumSet<Flag> values = EnumSet.noneOf(Flag.class);

    private SceneComponentState(Set<Flag> s) {
        for (Flag s0 : s) {
            if (s0 != Flag.DEFAULT) {
                this.values.add(s0);
            }
        }
        if (this.values.isEmpty()) {
            this.values.add(Flag.DEFAULT);
        }
    }

    private SceneComponentState(Flag... s) {
        for (Flag s0 : s) {
            if (s0 != Flag.DEFAULT) {
                this.values.add(s0);
            }
        }
        if (this.values.isEmpty()) {
            this.values.add(Flag.DEFAULT);
        }
    }

    public SceneComponentState add(SceneComponentState other) {
        EnumSet<Flag> n = EnumSet.noneOf(Flag.class);
        n.addAll(values);
        n.addAll(other.values);
        return new SceneComponentState(n);
    }

    public boolean is(SceneComponentState other) {
        if (other.equals(DEFAULT)) {
            return true;
        }
        if (this.equals(DEFAULT)) {
            return false;
        }
        return values.containsAll(other.values);
    }

    public SceneComponentState remove(SceneComponentState other) {
        EnumSet<Flag> n = EnumSet.noneOf(Flag.class);
        n.addAll(values);
        n.removeAll(other.values);
        return new SceneComponentState(n);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SceneComponentState that = (SceneComponentState) o;
        return values.equals(that.values);
    }


    @Override
    public String toString() {
        return values.stream().map(Enum::name).collect(Collectors.joining(","));
    }

    @Override
    public int compareTo(SceneComponentState o) {
        Flag[] a1 = this.values.toArray(new Flag[0]);
        Flag[] a2 = o.values.toArray(new Flag[0]);
        Arrays.sort(a1);
        Arrays.sort(a2);
        int max= Math.max(a1.length, a2.length);
        for (int i = 0; i < max; i++) {
            if(i>=a1.length){
                return -1;
            }
            if(i>=a2.length){
                return 1;
            }
            int j=a1[i].compareTo(a2[i]);
            if(j!=0){
                return j;
            }
        }
        return 0;
    }
}
