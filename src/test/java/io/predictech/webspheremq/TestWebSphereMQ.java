package io.predictech.webspheremq;

import org.junit.Test;

/**
 * @author Weijie Zhao
 * @email tttx(at)me.com
 * @date 2018/5/7
 * @description
 */
public class TestWebSphereMQ {
    @Test
    public void testSendMessage() {
        System.out.println(WebSphereMQ.sendMessage("TestWebSphereMQ hello word!"));
    }

    @Test
    public void testGetMessage() {
        System.out.println(WebSphereMQ.getMessage());
    }
}
