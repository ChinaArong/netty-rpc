package com.rpc;

import com.rpc.service.IHolleWordService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class FremakTest extends BaseTest {

//    @Resource(name="IHolleWordService")
    @Autowired
    private IHolleWordService iHolleWordService;

    @Test
    public void test1() throws InterruptedException {
        iHolleWordService.test1();
        System.out.println(111);
    }
}
