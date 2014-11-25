package com.massivecraft.factions.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

/**
 * 
 * A simple wrapper for guava cache
 * It has Weak Keys and Weak Values
 * 
 * @author Techcable
 *
 * @param <K> keys in this cache
 * @param <V> values in this cache
 */
public class EasyCache<K, V> {
    private final Cache<K, V> backing;
    public EasyCache(Loader<K, V> loader) {
        backing = CacheBuilder.newBuilder().weakKeys().weakValues().build(new LoaderCacheLoader<K, V>(loader));
    }
    
    public V get(K key) {
        return getBacking().getUnchecked(key);
    }
    
    public Cache<K, V> getBacking() {
    	return backing;
    }
    
    public static interface Loader<K, V> {
        public V load(K key);
    }
    
    private static class LoaderCacheLoader<K, V> extends CacheLoader<K, V> {
        
        private LoaderCacheLoader(Loader<K, V> backing) {
            this.backing = backing;
        }
        private final Loader<K, V> backing;
        
        @Override
        public V load(K key) {
            return getBacking().load(key);
        }
        
        public Loader<K, V> getBacking() {
        	return backing;
        }
    }
}