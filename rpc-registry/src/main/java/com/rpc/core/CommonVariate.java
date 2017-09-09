package com.rpc.core;

import com.rpc.domain.IPInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class CommonVariate {

    private static volatile Map<String,List<IPInfo>> serversContainers = new HashMap<>();

    public static Map<String, List<IPInfo>> getServersContainers() {
        return serversContainers;
    }

    public static void setServersContainers(Map<String, List<IPInfo>> serversContainers) {
        CommonVariate.serversContainers = serversContainers;
    }
}
