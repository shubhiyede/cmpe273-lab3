package edu.sjsu.cmpe.cache.client;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;



//import com.csforge.ConsistentHash;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;


public class Client {
	
	private static final HashFunction hfunc = Hashing.murmur3_128();
	private static final Funnel<CharSequence> strFunnel = Funnels.stringFunnel();
	private static final int numberOfServers=3;
	private static final int failedServers=1;
	private static final String dataString[]={"a","b","c","d","e","f","g","h","i","j"};
	private static final int dataKey=dataString.length;
	private static final Map<String, AtomicInteger> nodeMap = Maps.newHashMap(); 	
	private static final List<String> nodesList = getNodes(nodeMap);
	
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        RendezvousHashTest();
        System.out.println("Existing Cache Client...");
    }

	private static List<String> getNodes(Map<String, AtomicInteger> nodeMap) {
		List<String> nodes = Lists.newArrayList();
		for(int i = 0 ; i < numberOfServers; i ++) {
			nodes.add("http://localhost:300"+i);
			nodeMap.put("http://localhost:300"+i, new AtomicInteger());
		}
		return nodes;
	}
	
	private static void RendezvousHashTest(){
		//======: RendezvousHash :========
				//System.out.println("Performing RendezvousHash on list of servers....");
				RendezvousHash<String, String> rendezvousHash = new RendezvousHash(hfunc, strFunnel, strFunnel, nodesList);

				//System.out.println("Distribute data using Consistent Hashing");
				for(int i = 0 ; i < dataKey; i++) {
					String server=rendezvousHash.get(""+i);
					int serverLoad=nodeMap.get(server).incrementAndGet();
					rendezvousHash.insertData(server,i+1,dataString[i]);
				}
				//System.out.println("Distribute data using Consistent Hashing");
				for(int i = 0 ; i < dataKey; i++) {
					String server=rendezvousHash.get(""+i);
					rendezvousHash.getData(server,i+1);
				}
				
	}

}
