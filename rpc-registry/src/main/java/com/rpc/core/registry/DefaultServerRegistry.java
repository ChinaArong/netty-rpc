package com.rpc.core.registry;

import com.rpc.core.AbstartNodeOperateLogic;
import com.rpc.constant.Constant;
import com.rpc.core.ServiceLogic;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class DefaultServerRegistry extends AbstartNodeOperateLogic implements ServiceLogic {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultServerRegistry.class);

    private ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    private String nodeName;
    private String ip;
    private String weight;
    private String port;

    public DefaultServerRegistry(String prefix, String weight, String port) {
        this.nodeName = "/"+prefix;
        this.weight = weight;
        this.port = port;
    }

    public DefaultServerRegistry(String prefix, String port) {
        this.nodeName = "/"+prefix;
        this.port = port;
    }

    @Override
    protected void operateChildNode(ZooKeeper zk) {
        Integer count = threadLocal.get() == null?1:threadLocal.get();
        if(count > 3){
            LOGGER.error("zookeeper parentNode server create fail," +
                    "system logout!");
            System.exit(1);
        }
        threadLocal.set(count++);
        StringBuffer ipPrefix = new StringBuffer();
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
            if(StringUtils.isEmpty(ip)){
                throw new UnknownHostException();
            }
            ipPrefix.append(Constant.POINT).
                append(ip.substring(ip.lastIndexOf(Constant.POINT)));
        } catch (UnknownHostException e) {
            LOGGER.info("UnknownHostException system logout!");
            System.exit(1);
        }
        StringBuffer nodeDate = new StringBuffer();
        nodeDate.append(ip).append(Constant.COLON).append(port);
        if(!StringUtils.isEmpty(weight)){
            nodeDate.append(Constant.HASH_KEY).append(weight);
        }
        try {
            zk.create(parentNode + nodeName + ipPrefix.toString()
                    , nodeDate.toString().getBytes()
                    , ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            LOGGER.error(ip + ":" + port
                    + " create server node excption! retry:::"+count,e);
            operateChildNode(zk);
        }
        threadLocal.remove();
    }

    @Override
    public int order() {
        return 2;
    }

    @Override
    public void operateLogic(ZooKeeper connect, WatchedEvent event) {

    }
}
