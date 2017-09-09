package com.rpc;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class IPTest {

    @Test
    public void test1() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        String hostAddress = addr.getHostAddress();
        System.out.println(hostAddress);
    }
}
