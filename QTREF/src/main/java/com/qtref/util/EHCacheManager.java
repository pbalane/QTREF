package com.qtref.util;

import com.qtref.model.TransactionData;
import com.qtref.model.TransactionMetaData;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * This class is used as in memory data store
 * use EhCache APIs
 */
public class EHCacheManager {

    private CacheManager cacheManager;

    private static EHCacheManager EH_CACHE_INSTANCE;

    private Cache<TransactionMetaData, TransactionData> transactionsCache;

    private EHCacheManager() {

        if(EH_CACHE_INSTANCE == null) {

            cacheManager = CacheManagerBuilder
                    .newCacheManagerBuilder().build();
            cacheManager.init();

        }
    }

    /**
     * This is single ton pattern instance
     * one instance is used in this application
     *
     * @return EHCacheManager instance
     */
    public static EHCacheManager getInstance() {

        if(EH_CACHE_INSTANCE == null) {
            synchronized(EHCacheManager.class) {
                if(EH_CACHE_INSTANCE == null) {
                    EH_CACHE_INSTANCE = new EHCacheManager();
                }
            }
        }

        return EH_CACHE_INSTANCE;
    }

    /**
     * The cache is created during only at the Start up,
     * The consumers can use this cache for entry insert and fetch.
     * Cache is created using API and no xml is preferred here.
     *
     * @return Cache entry Cache<TransactionMetaData, TransactionData>
     */
    public Cache<TransactionMetaData, TransactionData> getTransactionsCache() {

        if(cacheManager != null ) {
            transactionsCache = cacheManager.getCache("transactionsCache",
                                                         TransactionMetaData.class, TransactionData.class);

            if(transactionsCache == null) {
                //Cache created using APIs and not with xml files
                transactionsCache = cacheManager
                        .createCache("transactionsCache", CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(TransactionMetaData.class, TransactionData.class,
                                ResourcePoolsBuilder.heap(10000)));
            }
        }

        return transactionsCache;
    }

}
