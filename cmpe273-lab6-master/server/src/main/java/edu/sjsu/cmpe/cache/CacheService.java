package edu.sjsu.cmpe.cache;

import java.io.File;
import java.util.concurrent.ConcurrentMap;


import net.openhft.chronicle.map.ChronicleMapBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import edu.sjsu.cmpe.cache.api.resources.CacheResource;
import edu.sjsu.cmpe.cache.config.CacheServiceConfiguration;
import edu.sjsu.cmpe.cache.domain.Entry;
import edu.sjsu.cmpe.cache.repository.CacheInterface;
import edu.sjsu.cmpe.cache.repository.ChronicleMapCache;


public class CacheService extends Service<CacheServiceConfiguration> {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static String [] copy;
   
    public static void main(String[] args) throws Exception {
    	copy=args;
        new CacheService().run(args);
    }

    @Override
    public void initialize(Bootstrap<CacheServiceConfiguration> bootstrap) {
        bootstrap.setName("cache-server");
    }

    @Override
    public void run(CacheServiceConfiguration configuration,
            Environment environment) throws Exception {
        /** Cache APIs */
        /*
        ConcurrentHashMap<Long, Entry> map = new ConcurrentHashMap<Long, Entry>();
        CacheInterface cache = new InMemoryCache(map);
        environment.addResource(new CacheResource(cache));
        log.info("Loaded resources");*/
        
    	
    	String server_name=copy[1].substring(14, 15);
    	//String pathname = "/Users/shubhiyede/Desktop/CMPE273-SithuAung/lab3/trial2/cmpe273-lab6-master/server/myfile"+server_name+".txt";
    	String pathname = System.getProperty("user.dir")+"/myfile"+server_name+".txt";
    	File file = new File(pathname);
	  
	    ChronicleMapBuilder<Long, Entry> builder =ChronicleMapBuilder.of(Long.class, Entry.class);
	    ConcurrentMap<Long, Entry> map = builder.createPersistedTo(file);
	    CacheInterface cache = new ChronicleMapCache(map);
        environment.addResource(new CacheResource(cache));
        log.info("Loaded resources");
    }
}
