package com.rpc.assist.parse.node.data;

import com.rpc.constant.Constant;
import com.rpc.domain.IPInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class DefaultParseHandler implements DataHandler{

    private Map<String,List<IPInfo>> serversContainerList;

    public DefaultParseHandler() {

    }

    public DefaultParseHandler(Map<String, List<IPInfo>> serversContainerList) {
        this.serversContainerList = serversContainerList;
    }

    @Override
    public void dataHandler(String data, String nodeName) {
        if(data == null
                || nodeName == null){
            return;
        }
        String childNode = nodeName.substring(nodeName.lastIndexOf("/"));
        String childNodeName = childNode.substring(0, childNode.lastIndexOf(Constant.POINT));
        List<IPInfo> ipInfos = serversContainerList.get(childNodeName);
        if(ipInfos == null){
            ipInfos = new ArrayList<>();
        }
        IPInfo ipInfo = new IPInfo();
        ipInfos.add(ipInfo);
        ipInfo.setWeigth(data.substring(data.lastIndexOf(Constant.HASH_KEY)));
        String ipAndPort = data.substring(0, data.lastIndexOf(Constant.COLON));
        ipInfo.setIpAddr(ipAndPort.substring(0, ipAndPort.lastIndexOf(Constant.COLON)));
        ipInfo.setPort(ipAndPort.substring(ipAndPort.lastIndexOf(Constant.COLON)));
    }
}
