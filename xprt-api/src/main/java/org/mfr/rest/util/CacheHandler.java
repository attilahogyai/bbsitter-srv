package org.mfr.rest.util;

import java.io.InputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class CacheHandler {
	static {
		InputStream config=CacheHandler.class.getResourceAsStream("ehcache-config.xml");
		CacheManager.create(config);
	}
	public static Cache getCache(String name){
		return CacheManager.getInstance().getCache(name);
	}
}
