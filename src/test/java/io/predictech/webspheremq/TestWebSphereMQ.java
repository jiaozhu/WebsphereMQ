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
        WebSphereMQ webSphereMQ = WebSphereMQ.getInstance("10.10.10.201",1414,"WMQ1","CNN_IDEA","a_remote");
        System.out.println(webSphereMQ.sendMessage("Hello World"));
    }

    @Test
    public void testGetMessage() {
        WebSphereMQ webSphereMQ = WebSphereMQ.getInstance("10.10.10.202",1414,"WMQ2","CNN_IDEA","b_local");
        System.out.println(webSphereMQ.getMessage());
    }
}
