package com.ruoyi.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.ruoyi.canal.properties.CanalServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(CanalServerProperties.class)
public class CanalConfiguration extends CachingConfigurerSupport {

    @Autowired
    private CanalServerProperties canalServerProperties;
    @Autowired
    private BinlogConsumer binlogConsumer;


    @Bean
    public CanalConnector canalClientInit() {
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
            new InetSocketAddress(canalServerProperties.getHostname(), canalServerProperties.getPort()), canalServerProperties.getDestination(),
            canalServerProperties.getUsername(),  canalServerProperties.getPassword());
        // 建立连接
        canalConnector.connect();
        // 异步线程开启canal客户端
        Thread thread = new Thread(new BinlogConsumerThread(canalConnector, binlogConsumer));
        thread.setDaemon(true);
        thread.start();
        return canalConnector;
    }



}
