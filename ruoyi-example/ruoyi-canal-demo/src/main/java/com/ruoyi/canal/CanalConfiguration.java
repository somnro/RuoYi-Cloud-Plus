package com.ruoyi.canal;

import cn.easyes.common.enums.FieldType;
import cn.easyes.core.conditions.LambdaEsIndexWrapper;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.ruoyi.canal.esmapper.Document;
import com.ruoyi.canal.esmapper.DocumentMapper;
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

    @Autowired
    private DocumentMapper documentMapper;

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
        // 初始化-> 创建索引,相当于MySQL建表 | 此接口须首先调用,只调用一次即可
//        LambdaEsIndexWrapper<Document> indexWrapper = new LambdaEsIndexWrapper<>();
//        indexWrapper.indexName(Document.class.getSimpleName().toLowerCase());
//        indexWrapper.mapping(Document::getTitle, FieldType.KEYWORD)
//            .mapping(Document::getContent, FieldType.TEXT);
//        documentMapper.createIndex(indexWrapper);
        return canalConnector;
    }



}
