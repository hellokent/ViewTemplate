package com.example.viewtemplate;

import java.util.HashMap;

/**
 * Created by chenyang.coder@gmail.com on 14-1-14 上午10:47.
 */
public class TwowayMap<K, V> {
    HashMap<K, V> kKeyMap = new HashMap<K, V>();
    HashMap<V, K> kValueMap = new HashMap<V, K>();

    public V getValue(K k){
        return kKeyMap.get(k);
    }

    public K getKey(V v){
        return kValueMap.get(v);
    }

    public void put(final K k, final V v){
        kKeyMap.put(k, v);
        kValueMap.put(v, k);
    }

    public void removeKey(final K k){
        V v = kKeyMap.get(k);
        if (v == null){
            return;
        }
        remove(k, v);
    }

    public void removeValue(final V v){
        K k = kValueMap.get(v);
        if (k == null){
            return;
        }
        remove(k, v);
    }

    protected void remove(K k, V v){
        kKeyMap.remove(k);
        kValueMap.remove(v);
    }

    public boolean containsKey(K k){
        return kKeyMap.containsKey(k);
    }

    public boolean containsValue(V v){
        return kKeyMap.containsValue(v);
    }
}
