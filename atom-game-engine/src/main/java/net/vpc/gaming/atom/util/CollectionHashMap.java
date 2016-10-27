package net.vpc.gaming.atom.util;

import java.util.*;

public class CollectionHashMap<K,V>{
    private HashMap<K,Collection<V>> data=new HashMap<>();

    public void add(K k,V v){
        Collection<V> lst = getValues(k);
        lst.add(v);
    }

    public void remove(K k,V v){
        Collection<V> lst = getValues(k);
        lst.remove(v);
        if(lst.isEmpty()){
            data.remove(k);
        }
    }

    public Collection<V> getValues(K k){
        Collection<V> lst = data.get(k);
        if(lst==null){
            lst=createCollection();
            data.put(k,lst);
        }
        return lst;
    }

    public Collection<V> createCollection(){
        return new ArrayList<>();
    }

    public int size(){
        return data.size();
    }

    public Set<Map.Entry<K, Collection<V>>> entrySet() {
        return data.entrySet();
    }

    public Set<K> keySet() {
        return data.keySet();
    }

    public void clear(){
        data.clear();
    }
}
