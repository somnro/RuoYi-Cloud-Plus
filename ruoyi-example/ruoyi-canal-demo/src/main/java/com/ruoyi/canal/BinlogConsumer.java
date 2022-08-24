package com.ruoyi.canal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;


public interface BinlogConsumer {

    /**
     * 消费binlog数据
     * @param tableName
     * @param eventType
     * @param beforeData
     * @param afterData
     */
    void consumer(String tableName, CanalEntry.EventType eventType, JSONObject beforeData, JSONObject afterData);

}
