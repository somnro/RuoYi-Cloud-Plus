package com.ruoyi.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.ruoyi.canal.properties.CanalServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

import java.net.InetSocketAddress;

/**
 * 演示模块
 *
 * @author Lion Li
 */
@SpringBootApplication
public class RuoYiDemoApplication {

    @Autowired
    private CanalServerProperties canalServerProperties;
    @Autowired
    private BinlogConsumer binlogConsumer;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RuoYiDemoApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
       // initCanalClient();
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  演示模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }

    public  void initCanalClient(){
        String hostname = canalServerProperties.getHostname();
        int port = canalServerProperties.getPort();
        String destination = canalServerProperties.getDestination();
        String username = canalServerProperties.getUsername();
        String password = canalServerProperties.getPassword();
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
            new InetSocketAddress(hostname, port), destination, username, password);
        // 建立连接
        canalConnector.connect();
        // 异步线程开启canal客户端
        Thread thread = new Thread(new BinlogConsumerThread(canalConnector, binlogConsumer));
        thread.setDaemon(true);
        thread.start();
    }


}
