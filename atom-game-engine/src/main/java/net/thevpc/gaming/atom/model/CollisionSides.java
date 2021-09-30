package net.thevpc.gaming.atom.model;

import java.util.Objects;

public class CollisionSides {
    public static final int SIDE_NONE = 0;
    public static final int SIDE_NORTH = 1;
    public static final int SIDE_EAST = 2;
    public static final int SIDE_SOUTH = 4;
    public static final int SIDE_WEST = 8;
    private int value;

    public static final CollisionSides NONE = CollisionSides.of(CollisionSides.SIDE_NONE);
    public static final CollisionSides NORTH = CollisionSides.of(CollisionSides.SIDE_NORTH);
    public static final CollisionSides SOUTH = CollisionSides.of(CollisionSides.SIDE_SOUTH);
    public static final CollisionSides EAST = CollisionSides.of(CollisionSides.SIDE_EAST);
    public static final CollisionSides WEST = CollisionSides.of(CollisionSides.SIDE_WEST);

    public static CollisionSides of(int value) {
        return new CollisionSides(value);
    }

    private CollisionSides(int value) {
        this.value = value;
    }

    public CollisionSides append(int i) {
        return of(i|value);
    }
    public CollisionSides append(CollisionSides i) {
        return of(i.value|value);
    }

    public String toString() {
        int side=value;
        if (side == 0) {
            return "<none>";
        }
        StringBuilder s = new StringBuilder();
        if ((side & SIDE_NORTH) != 0) {
            s.append('N');
        }
        if ((side & SIDE_EAST) != 0) {
            s.append('E');
        }
        if ((side & SIDE_SOUTH) != 0) {
            s.append('S');
        }
        if ((side & SIDE_WEST) != 0) {
            s.append('W');
        }
        side &= ~(SIDE_EAST | SIDE_WEST | SIDE_NORTH | SIDE_SOUTH);
        if (side != 0) {
            s.append(side);
        }
        return s.toString();
    }


    public boolean isNorth() {
        return (value & SIDE_NORTH) != 0;
    }

    public boolean isSouth() {
        return (value & SIDE_SOUTH) != 0;
    }

    public boolean isWest() {
        return (value & SIDE_WEST) != 0;
    }

    public boolean isEast() {
        return (value & SIDE_EAST) != 0;
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollisionSides that = (CollisionSides) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public boolean isNone() {
        return value==0;
    }
}
