package edu.sjsu.cmpe.cache.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.openhft.chronicle.map.ChronicleMap;
import edu.sjsu.cmpe.cache.domain.Entry;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ChronicleMapCache implements CacheInterface{

	ConcurrentMap<Long, Entry> inMemoryMap;

	public ChronicleMapCache(ConcurrentMap<Long, Entry> entries) {
        inMemoryMap = entries;
    }

    @Override
    public Entry save(Entry newEntry) {
        checkNotNull(newEntry, "newEntry instance must not be null");
        inMemoryMap.putIfAbsent(newEntry.getKey(), newEntry);

        return newEntry;
    }

    @Override
    public Entry get(Long key) {
        checkArgument(key > 0,
                "Key was %s but expected greater than zero value", key);
        return inMemoryMap.get(key);
    }

    @Override
    public List<Entry> getAll() {
        return new ArrayList<Entry>(inMemoryMap.values());
    }

	
}
