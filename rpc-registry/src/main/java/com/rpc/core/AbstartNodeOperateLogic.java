package com.rpc.core;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public abstract class AbstartNodeOperateLogic implements ServiceLogic {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstartNodeOperateLogic.class);

    protected String parentNode = "/server";

    private ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    @Override
    public void operateLogic(ZooKeeper zk) {
        Integer count = threadLocal.get() == null?1:threadLocal.get();
        if(count > 3){
            LOGGER.error("zookeeper parentNode server create fail," +
                    "system logout!");
            System.exit(1);
        }
        threadLocal.set(count++);
        try {
            Stat stat = zk.exists(parentNode, false);
            if(stat == null){
                LOGGER.info("parentNode server is not exists!");
                zk.create(parentNode,null
                        , ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                LOGGER.info("create parentNode server success!");
            }
            operateChildNode(zk);
        } catch (Exception e) {
            LOGGER.error("create parentNode server excption! retry:::"+count,e);
            operateLogic(zk);
        }
        threadLocal.remove();
    }

    protected abstract void operateChildNode(ZooKeeper zk);
}
