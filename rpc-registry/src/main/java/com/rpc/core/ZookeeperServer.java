package com.rpc.core;

import com.rpc.core.registry.DefaultServerRegistry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class ZookeeperServer {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultServerRegistry.class);
    /**
     * serverName
     * connectString
     */
    private static String connectString;
    private static List<ServiceLogic> Logic;
    private static final int ZK_SESSION_TIMEOUT = 5000;//zk超时时间
    private static Integer retryCount = 3;

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    private ZooKeeper zk = null;

    private CountDownLatch latch = new CountDownLatch(1);

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public void setLogic(List<ServiceLogic> logic) {
        if(logic == null){
            Logic = new ArrayList<>();
            return;
        }
        logic.sort(new Comparator<ServiceLogic>() {
            @Override
            public int compare(ServiceLogic o1, ServiceLogic o2) {
                return o1.order() < o2.order()?1:-1;
            }
        });
        Logic = logic;
    }

    public void zkServerLogic() throws IOException, InterruptedException {
        getConnect();
        if(zk == null || !zk.getState().isConnected()){
            latch.await(3000, TimeUnit.MICROSECONDS);
        }
        for(ServiceLogic serviceLogic:Logic){
            serviceLogic.operateLogic(zk);
        }
    }

    public ZooKeeper getConnect() throws IOException {
        try {
            zk = new ZooKeeper(connectString, ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(event.getState() == Event.KeeperState.SyncConnected){
                        latch.countDown();
                    }
                    try {
                        for(ServiceLogic serviceLogic:Logic){
                            serviceLogic.operateLogic(getConnect(),event);
                        }
                    } catch (IOException e) {
                        LOGGER.info("zookeeper update childNode fail!");
                    }
                }
            });
        } catch (IOException e) {
            retry(retryCount);
        }
        threadLocal.remove();
        return zk;
    }

    private void retry(Integer retryCount) throws IOException {
        int count = threadLocal.get() == null ? 1 : threadLocal.get();
        if(count > retryCount){
            LOGGER.info("zookeeper connect fail!");
            throw new IOException();
        }
        threadLocal.set(count++);
        getConnect();
    }
}
