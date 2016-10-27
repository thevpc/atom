/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.extension.strategy.resources;

import java.io.Serializable;
import java.util.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class DefaultResourceRepository implements ResourceRepository {

    private Map<Class<? extends Resource>, ResourceInfo> resources = new HashMap<Class<? extends Resource>, ResourceInfo>(3);
    private Set<Class<? extends Resource>> allowedTypes = new HashSet<Class<? extends Resource>>(3);

    private static class ResourceInfo implements Serializable {

        private Class<? extends Resource> type;
        private int value = 0;
        private int max = -1;

        public ResourceInfo(Class<? extends Resource> type) {
            this.type = type;
        }
    }

    public DefaultResourceRepository(Class<? extends Resource>... types) {
        allowedTypes.addAll(Arrays.asList(types));
    }

    private ResourceInfo getResourceInfo(Class<? extends Resource> resourceType) {
        ResourceInfo d = resources.get(resourceType);
        if (d == null) {
            d = new ResourceInfo(resourceType);
            resources.put(resourceType, d);
        }
        return d;
    }

    public int getMaxResource(Class<? extends Resource> resourceType) {
        return getResourceInfo(resourceType).max;
    }

    public void setMaxResource(int max, Class<? extends Resource>... resourceTypes) {
        for (Class<? extends Resource> resourceType : resourceTypes) {
            getResourceInfo(resourceType).max = max;
        }
    }

    public void setMaxResource(int max, Collection<Class<? extends Resource>> resourceTypes) {
        for (Class<? extends Resource> resourceType : resourceTypes) {
            getResourceInfo(resourceType).max = max;
        }
    }

    public int getResource(Class<? extends Resource> resourceType) {
        return getResourceInfo(resourceType).value;
    }

    public boolean isAllowedResource(Class<? extends Resource> resourceType) {
        return allowedTypes.isEmpty() || allowedTypes.contains(resourceType);
    }

    public void setResource(Class<? extends Resource> resourceType, int quantity) {
        ResourceInfo ri = getResourceInfo(resourceType);
        ri.value = quantity;
    }


    public int addResource(Class<? extends Resource> resourceType, int quantity) {
        ResourceInfo ri = getResourceInfo(resourceType);
        if (ri.max >= 0 && ri.value >= ri.max) {
            throw new RepositoryFullException();
        }
        int before = ri.value;
        if (quantity < 0) {
            quantity = 0;
        }
        int q = quantity + before;
        if (ri.max >= 0 && q > ri.max) {
            quantity = ri.max - before;
            ri.value = ri.max;
            return quantity;
        } else {
            ri.value = q;
            return quantity;
        }
    }

    public int removeResource(Class<? extends Resource> resourceType, int quantity) {
        ResourceInfo ri = getResourceInfo(resourceType);
        if (ri.value == 0 && quantity > 0) {
            throw new RepositoryEmptyException();
        }
        int current = ri.value;
        if (quantity > 0) {
            if (quantity > current) {
                ri.value = 0;
                return current;
            } else {
                ri.value = current - quantity;
                return quantity;
            }
        }
        return 0;
    }

    public Class<? extends Resource>[] getResources() {
        Set<Class<? extends Resource>> keySet = resources.keySet();
        Class<? extends Resource>[] rr = keySet.toArray(new Class[resources.size()]);
//        Arrays.sort(rr);
        return rr;
    }
}
