package com.rpc.assist.parse.node.data;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class PackageNodeData {

    public static  void handlerData(String data
            , String nodeName, DataHandler dataHandler){
        dataHandler.dataHandler(data,nodeName);
    }
}
