package net.thevpc.gaming.atom.ioc;

import net.thevpc.common.classpath.AnnotationFilter;
import net.thevpc.common.classpath.AnnotationParser;
import net.thevpc.common.classpath.ClassPathUtils;
import net.thevpc.common.classpath.URLClassIterable;
import net.thevpc.gaming.atom.engine.GameEngine;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Created by vpc on 9/25/16.
 */
public class GameEngineIoCContainer extends AbstractAtomIoCContainer {
    private GameEngine gameEngine;
    Map<String, Object> beansById = new HashMap<>();
    ClassNamedObjectMap beansByType = new ClassNamedObjectMap();
    public GameEngineIoCContainer(GameEngine gameEngine) {
        super(null);
        this.gameEngine=gameEngine;
        register(null,GameEngine.class, null, gameEngine);
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    @Override
    public void start(){
        AnnotationFilter annotationFilter = new AnnotationFilter() {

            public boolean isSupportedTypeAnnotation() {
                return true;
            }

            public boolean isSupportedMethodAnnotation() {
                return true;
            }

            public boolean isSupportedFieldAnnotation() {
                return true;
            }

            public boolean acceptTypeAnnotation(String name, String targetType, Class value) {
                return true;
            }

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
    public Object getBeanOrNull(String id){
        return beansById.get(id);
    }

    @Override
    public void register(String id, Class cls, String name, Object instance){
        if(cls==null){
            cls=instance.getClass();
        }
        if(id==null){
            id=cls.getName();
        }
        if(beansById.containsKey(id)){
            throw new RuntimeException("Bean Already registered "+id);
        }
        beansById.put(id,instance);
        beansByType.add(cls, name, instance);
    }



    @Override
    public List<Object> getBeans(Class type) {
        List<Object> objects = beansByType.findAll(type);
        if(objects!=null){
            return objects;
        }
        return Collections.EMPTY_LIST;
    }


}
