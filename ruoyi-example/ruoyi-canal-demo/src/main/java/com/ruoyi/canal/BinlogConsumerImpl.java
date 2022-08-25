package com.ruoyi.canal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
public class BinlogConsumerImpl implements BinlogConsumer {

    @Override
    public void consumer(String tableName, CanalEntry.EventType eventType, JSONObject beforeData, JSONObject afterData) {
        System.out.println("tableName:"+tableName +",beforeData:"+beforeData+"afterData:"+afterData);
//        if("after_sale_info".equalsIgnoreCase(tableName)) {
//            targetAfterSaleInfoService.consumer(eventType, beforeData, afterData);
//            return;
//        }
//
//        if("after_sale_item".equalsIgnoreCase(tableName)) {
//            targetAfterSaleItemService.consumer(eventType, beforeData, afterData);
//            return;
//        }


    }
}
