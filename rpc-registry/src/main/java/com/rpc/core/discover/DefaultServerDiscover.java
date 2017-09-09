package com.rpc.core.discover;

import com.rpc.core.AbstartNodeOperateLogic;
import com.rpc.core.CommonVariate;
import com.rpc.core.ServiceLogic;
import com.rpc.assist.parse.node.data.DefaultParseHandler;
import com.rpc.assist.parse.node.data.PackageNodeData;
import com.rpc.domain.IPInfo;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class DefaultServerDiscover extends AbstartNodeOperateLogic implements ServiceLogic {

    @Override
    protected void operateChildNode(ZooKeeper zk) {
        Map<String,List<IPInfo>> serversContainerList = new HashMap<>();
        try {
            List<String> childrenNodes = zk.getChildren(parentNode, true);
            for(String nodes:childrenNodes){
                byte[] data = zk.getData(nodes, false, null);
                String ipInfo = new String(data, Charset.defaultCharset());
                PackageNodeData
                        .handlerData(ipInfo, nodes, new DefaultParseHandler(serversContainerList));
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommonVariate.setServersContainers(serversContainerList);
    }

    @Override
    public int order() {
        return 2;
    }

    @Override
    public void operateLogic(ZooKeeper connect, WatchedEvent event) {
        if(event.getType() == Watcher.Event.EventType.NodeChildrenChanged
                && parentNode.equals(event.getPath())){
            operateLogic(connect);
        }
    }
}
