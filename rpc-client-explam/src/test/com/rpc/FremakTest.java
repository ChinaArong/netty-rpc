package com.rpc;

import com.rpc.service.IHolleWordService;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by chengzhengrong on 2017/9/9.
 */
public class FremakTest extends BaseTest {

    @Resource(name="IHolleWordService")
    private IHolleWordService iHolleWordService;

    @Test
    public void test1(){
        iHolleWordService.test1();
        System.out.println(111);
    }
}
