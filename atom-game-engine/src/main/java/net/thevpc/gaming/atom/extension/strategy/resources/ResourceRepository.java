/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.extension.strategy.resources;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface ResourceRepository extends Serializable {

    void setResource(Class<? extends Resource> resourceType, int quantity);

    int addResource(Class<? extends Resource> resourceType, int quantity);

    int getResource(Class<? extends Resource> resourceType);

    int getMaxResource(Class<? extends Resource> resourceType);

    int removeResource(Class<? extends Resource> resourceType, int quantity);

    boolean isAllowedResource(Class<? extends Resource> resourceType);

    public Class<? extends Resource>[] getResources();

    void setMaxResource(int max, Class<? extends Resource>... resourceType);

    void setMaxResource(int max, Collection<Class<? extends Resource>> resourceType);
}
