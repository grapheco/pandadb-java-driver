package org.grapheco.pandadb.driver;

import org.grapheco.lynx.types.LynxValue;

import java.util.HashMap;
import java.util.Iterator;

public class JPandaDBDriver {
    public String host;
    public int port;
    public PandaDBDriver driver;
    public JPandaDBDriver(String host, int port) {
        driver = new PandaDBDriver(host,port);
    }

    public Iterator<HashMap<String, PandaDBValue>> query(String stat, HashMap<String, Object> params){
        scala.collection.Iterator<scala.collection.immutable.Map<String, LynxValue>> iter = driver.query(stat, DataConversion.hashMapToScala(params));
        return DataConversion.iterMapToIterHashMap(iter);
    }

    public void shutdown() throws InterruptedException {
        driver.shutdown();
    }
}
