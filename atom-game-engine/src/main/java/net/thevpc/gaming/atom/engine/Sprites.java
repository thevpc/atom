//package net.thevpc.gaming.atom.engine;
//
//import net.thevpc.gaming.atom.model.Sprite;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//public interface Sprites {
//    static Sprites byId(int... ids) {
//        return new SpritesById(ids);
//    }
//
//    static Sprites byName(String... ids) {
//        return new SpritesByName(ids);
//    }
//
//    static Sprites byTypeAndName(Class clazz, String name) {
//        return new Sprites() {
//            @Override
//            public List<Sprite> find(SceneEngine engine) {
//                return engine.findSpritesByName(clazz, name);
//            }
//        };
//    }
//
//    static Sprites byType(Class clazz) {
//        return new SpritesByType(clazz);
//    }
//
//    static Sprites byKind(String... kinds) {
//        return new SpritesByKind(kinds);
//    }
//
//    static Sprites byPlayerId(int... players) {
//        return new SpritesByPlayer(players);
//    }
//
//    static Sprites byTypeAndPlayer(Class type, int player) {
//        return new SpritesByTypeAndPlayer(type, player);
//    }
//
//    List<Sprite> find(SceneEngine engine);
//
//    class SpritesById implements Sprites {
//        private final int[] ids;
//
//        public SpritesById(int... ids) {
//            this.ids = ids;
//        }
//
//        public int[] getIds() {
//            return ids;
//        }
//
//        @Override
//        public List<Sprite> find(SceneEngine engine) {
//            return Arrays.stream(ids)
//                    .distinct().mapToObj(
//                            x -> engine.getSprite(x)
//                    ).filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            SpritesById that = (SpritesById) o;
//            return Arrays.equals(ids, that.ids);
//        }
//
//        @Override
//        public int hashCode() {
//            return Arrays.hashCode(ids);
//        }
//    }
//
//    class SpritesByName implements Sprites {
//        private final String[] ids;
//
//        public SpritesByName(String... ids) {
//            this.ids = ids;
//        }
//
//        @Override
//        public List<Sprite> find(SceneEngine engine) {
//            return Arrays.stream(ids)
//                    .distinct()
//                    .flatMap(
//                            x -> engine.findSpritesByName(x).stream()
//                    ).filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            SpritesByName that = (SpritesByName) o;
//            return Arrays.equals(ids, that.ids);
//        }
//
//        @Override
//        public int hashCode() {
//            return Arrays.hashCode(ids);
//        }
//    }
//
//    class SpritesByType implements Sprites {
//        private final Class clazz;
//
//        public SpritesByType(Class clazz) {
//            this.clazz = clazz;
//        }
//
//        @Override
//        public List<Sprite> find(SceneEngine engine) {
//            return engine.findSpritesByName(clazz, null);
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            SpritesByType that = (SpritesByType) o;
//            return Objects.equals(clazz, that.clazz);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(clazz);
//        }
//    }
//
//    class SpritesByKind implements Sprites {
//        private final String[] kinds;
//
//        public SpritesByKind(String... kinds) {
//            this.kinds = kinds;
//        }
//
//        @Override
//        public List<Sprite> find(SceneEngine engine) {
//            return Arrays.stream(kinds)
//                    .distinct()
//                    .flatMap(
//                            x -> engine.findSpritesByKind(x).stream()
//                    ).filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            SpritesByKind that = (SpritesByKind) o;
//            return Arrays.equals(kinds, that.kinds);
//        }
//
//        @Override
//        public int hashCode() {
//            return Arrays.hashCode(kinds);
//        }
//    }
//
//    class SpritesByPlayer implements Sprites {
//        private final int[] players;
//
//        public SpritesByPlayer(int... players) {
//            this.players = players;
//        }
//
//        @Override
//        public List<Sprite> find(SceneEngine engine) {
//            return Arrays.stream(players)
//                    .distinct()
//                    .boxed()
//                    .flatMap(
//                            x -> engine.findSpritesByPlayer(x).stream()
//                    ).filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            SpritesByPlayer that = (SpritesByPlayer) o;
//            return Arrays.equals(players, that.players);
//        }
//
//        @Override
//        public int hashCode() {
//            return Arrays.hashCode(players);
//        }
//    }
//
//    class SpritesByTypeAndPlayer implements Sprites {
//        private final Class type;
//        private final int player;
//
//        public SpritesByTypeAndPlayer(Class type, int player) {
//            this.type = type;
//            this.player = player;
//        }
//
//        @Override
//        public List<Sprite> find(SceneEngine engine) {
//            return engine.findSpritesByPlayer(type, player);
//        }
//    }
//}
