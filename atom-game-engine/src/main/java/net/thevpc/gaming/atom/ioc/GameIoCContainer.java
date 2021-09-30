package net.thevpc.gaming.atom.ioc;

import net.thevpc.common.classpath.AnnotationFilter;
import net.thevpc.common.classpath.AnnotationParser;
import net.thevpc.common.classpath.ClassPathUtils;
import net.thevpc.common.classpath.URLClassIterable;
import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.presentation.Game;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Created by vpc on 9/25/16.
 */
public class GameIoCContainer extends AbstractAtomIoCContainer {
    private Game game;
    java.util.Map<String, Object> beansById = new HashMap<>();
    java.util.Map<Class, List<Object>> beansByType = new HashMap<>();

    public GameIoCContainer(Game game) {
        super(null);
        this.game = game;
        register(null,Game.class, null, game);
    }

    public Game getGame() {
        return game;
    }

    public GameEngine getGameEngine() {
        return game.getGameEngine();
    }

    @Override
    public void start() {
        AnnotationFilter annotationFilter = new AnnotationFilter() {
            @Override
            public boolean isSupportedTypeAnnotation() {
                return true;
            }

            @Override
            public boolean isSupportedMethodAnnotation() {
                return true;
            }

            @Override
            public boolean isSupportedFieldAnnotation() {
                return true;
            }

            @Override
            public boolean acceptTypeAnnotation(String name, String targetType, Class value) {
                return true;
            }

            @Override
            public boolean acceptMethodAnnotation(String name, String targetMethod, String targetType, Method value) {
                return true;
            }

            @Override
            public boolean acceptFieldAnnotation(String name, String targetField, String targetType, Field value) {
                return true;
            }
        };
        AtomAnnotationsProcessor annotationVisitor = new AtomAnnotationsProcessor(this);
        URL[] urls = ClassPathUtils.resolveContextLibraries();
        URLClassIterable it = new URLClassIterable(
                urls,
                null, null
        );
        AnnotationParser p = new AnnotationParser(
                it,
                annotationFilter, annotationVisitor
        );
        try {
            p.parse();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        annotationVisitor.build();
    }

    @Override
    public Object getBeanOrNull(String id) {
        return beansById.get(id);
    }



    @Override
    public void register(String id, Class cls, String name, Object instance) {
        if(cls==null){
            cls=instance.getClass();
        }
        if (id == null) {
            id = cls.getName();
        }
        if (beansById.containsKey(id)) {
            throw new RuntimeException("Bean Already registered " + id);
        }
        beansById.put(id, instance);
        List<Object> objects = beansByType.get(cls);
        if (objects == null) {
            objects = new ArrayList<Object>();
            beansByType.put(cls, objects);
        }
        objects.add(instance);
    }

    @Override
    public List<Object> getBeans(Class type) {
        List<Object> objects = beansByType.get(type);
        if (objects != null) {
            return objects;
        }
        return Collections.EMPTY_LIST;
    }


}
