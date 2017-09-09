package com.rpc.core;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public interface ServiceLogic {

    int order();

    void operateLogic(ZooKeeper zk);

    void operateLogic(ZooKeeper connect, WatchedEvent event);
}
