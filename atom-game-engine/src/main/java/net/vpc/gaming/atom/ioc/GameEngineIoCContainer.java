package net.vpc.gaming.atom.ioc;

import net.vpc.common.classpath.AnnotationFilter;
import net.vpc.common.classpath.AnnotationParser;
import net.vpc.common.classpath.ClassPathUtils;
import net.vpc.common.classpath.URLClassIterable;
import net.vpc.gaming.atom.engine.GameEngine;

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
    Map<Class, List<Object>> beansByType = new HashMap<>();
    public GameEngineIoCContainer(GameEngine gameEngine) {
        super(null);
        this.gameEngine=gameEngine;
        register(null,GameEngine.class,gameEngine);
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
        Object o = beansById.get(id);
        if(o==null){
            throw new IllegalArgumentException("Bean Not Found "+id);
        }
        return o;
    }

    @Override
    public void register(String id, Class cls,Object instance){
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
        List<Object> objects = beansByType.get(cls);
        if(objects==null){
            objects=new ArrayList<Object>();
            beansByType.put(cls,objects);
        }
        objects.add(instance);
    }



    @Override
    public List<Object> getBeans(Class type) {
        List<Object> objects = beansByType.get(type);
        if(objects!=null){
            return objects;
        }
        return Collections.EMPTY_LIST;
    }


}
