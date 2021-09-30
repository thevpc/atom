package net.thevpc.gaming.atom.presentation;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeyCodeSet implements Iterable<KeyCode> {
    private final Set<KeyCode> all;


    private KeyCodeSet(Collection<KeyCode> others) {
        if (others != null) {
            all = others.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        } else {
            all = Collections.emptySet();
        }
    }

    public static KeyCodeSet of(KeyCode... others) {
        return of(others == null ? null : Arrays.asList(others));
    }

    public static KeyCodeSet of(Collection<KeyCode> others) {
        return (others == null || others.isEmpty())
                ? new KeyCodeSet(Collections.emptySet())
                : new KeyCodeSet(others);
    }

    public boolean isEmpty() {
        return all.isEmpty();
    }

    public int size() {
        return all.size();
    }

    public boolean contains(KeyCode a) {
        return all.contains(a);
    }

    public boolean contains(KeyCode... others) {
        return all.containsAll(
                Arrays.stream(others).filter(Objects::nonNull).collect(Collectors.toSet())
        );
    }

    public boolean contains(Collection<KeyCode> others) {
        return all.containsAll(
                others == null ? Collections.emptySet()
                        : others.stream().filter(Objects::nonNull).collect(Collectors.toSet())
        );
    }

    public boolean is(KeyCode other) {
        return other != null && all.size() == 1 && all.contains(other);
    }

    public boolean contains(KeyCodeSet others) {
        return contains(others == null ? null : others.all);
    }

    public boolean is(KeyCodeSet others) {
        return is(others == null ? null : others.all);
    }

    public boolean is(Collection<KeyCode> others) {
        Set<KeyCode> o = others == null ? Collections.emptySet() :
                others.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        return all.size() == o.size() && all.containsAll(o);
    }

    public boolean is(KeyCode... others) {
        Set<KeyCode> o = Arrays.stream(others).filter(Objects::nonNull).collect(Collectors.toSet());
        return all.size() == o.size() && all.containsAll(o);
    }

    public boolean is(KeyCodeSet... others) {
        Set<KeyCode> o = Arrays.stream(others).filter(Objects::nonNull)
                .flatMap(x->x.toSet().stream())
                .collect(Collectors.toSet());
        return all.size() == o.size() && all.containsAll(o);
    }

    public Stream<KeyCode> stream() {
        return all.stream();
    }

    public Set<KeyCode> toSet() {
        return all.isEmpty() ? Collections.emptySet() :
                new HashSet<>(all);
    }

    public KeyCode[] toArray() {
        return all.toArray(new KeyCode[0]);
    }

    @Override
    public int hashCode() {
        return Objects.hash(all);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyCodeSet that = (KeyCodeSet) o;
        return Objects.equals(all, that.all);
    }

    @Override
    public String toString() {
        return all.toString();
    }

    @Override
    public Iterator<KeyCode> iterator() {
        return all.iterator();
    }

    @Override
    public void forEach(Consumer<? super KeyCode> action) {
        all.forEach(action);
    }

    @Override
    public Spliterator<KeyCode> spliterator() {
        return all.spliterator();
    }

    public KeyCodeSet plus(KeyCodeSet other) {
        Set<KeyCode> result = new HashSet<>(all);
        if (other != null) {
            result.addAll(other.all);
        }
        return of(result);
    }

    public KeyCodeSet minus(KeyCodeSet other) {
        Set<KeyCode> result = new HashSet<>(all);
        if (other != null) {
            result.removeAll(other.all);
        }
        return of(result);
    }

    public KeyCodeSet retain(KeyCodeSet other) {
        Set<KeyCode> result = new HashSet<>(all);
        if (other != null) {
            result.retainAll(other.all);
        }
        return of(result);
    }

    public KeyCodeSet plus(KeyCode... others) {
        Set<KeyCode> result = new HashSet<>(all);
        if (others != null) {
            result.addAll(Arrays.asList(others));
        }
        return of(result);
    }

    public KeyCodeSet minus(KeyCodeSet... others) {
        Set<KeyCode> result = new HashSet<>(all);
        if (others != null) {
            result.removeAll(Arrays.asList(others));
        }
        return of(result);
    }

    public KeyCodeSet retain(KeyCodeSet... others) {
        Set<KeyCode> result = new HashSet<>(all);
        if (others != null) {
            result.retainAll(Arrays.asList(others));
        }
        return of(result);
    }

    public KeyCodeSet plus(Collection<KeyCode> others) {
        Set<KeyCode> result = new HashSet<>(all);
        if (others != null) {
            result.addAll(others);
        }
        return of(result);
    }

    public KeyCodeSet minus(Collection<KeyCodeSet> others) {
        Set<KeyCode> result = new HashSet<>(all);
        if (others != null) {
            result.removeAll(others);
        }
        return of(result);
    }

    public KeyCodeSet retain(Collection<KeyCodeSet> others) {
        Set<KeyCode> result = new HashSet<>(all);
        if (others != null) {
            result.retainAll(others);
        }
        return of(result);
    }

}
