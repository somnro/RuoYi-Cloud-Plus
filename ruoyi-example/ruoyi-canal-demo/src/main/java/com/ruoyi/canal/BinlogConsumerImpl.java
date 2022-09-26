package com.ruoyi.canal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.ruoyi.canal.esmapper.Document;
import com.ruoyi.canal.esmapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class BinlogConsumerImpl implements BinlogConsumer {

    @Autowired
     private DocumentMapper documentMapper;


    @Override
    public void consumer(String tableName, CanalEntry.EventType eventType, JSONObject beforeData, JSONObject afterData) {
        System.out.println("tableName:"+tableName +",beforeData:"+beforeData+"afterData:"+afterData);
        if("document".equalsIgnoreCase(tableName)) {
            Document document =  afterData.toJavaObject(Document.class);
            if(CanalEntry.EventType.INSERT.equals(eventType) ) {
                documentMapper.insert(document);
            }else if(CanalEntry.EventType.UPDATE.equals(eventType)){
                documentMapper.updateById(document);
            }
        }
             //




    }
}
