package com.ruoyi.canal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class BinlogConsumerThread implements Runnable {

    private static Logger log = LoggerFactory.getLogger(BinlogConsumerThread.class);

    /**
     * 开关
     */
    private volatile boolean running;

    private CanalConnector canalConnector;

    private BinlogConsumer binlogConsumer;

    public BinlogConsumerThread(CanalConnector canalConnector, BinlogConsumer binlogConsumer) {
        this.canalConnector = canalConnector;
        this.binlogConsumer = binlogConsumer;
        this.running = true;
    }


    @Override
    public void run() {

        // 订阅数据库
        canalConnector.subscribe("sys.document");

        while (running) {

            try {

                // 拉取数据
                Message message = canalConnector.get(100);

                // 获取Entry集合
                List<CanalEntry.Entry> entries = message.getEntries();

                // 判断集合是否为空,如果为空,则等待一会继续拉取数据
                if (entries.size() <= 0) {
//                    log.warn("当次拉取没有数据，睡眠1000毫秒.....");
                    Thread.sleep(1000);
                } else {

                    // 遍历entries，单条解析
                    for (CanalEntry.Entry entry : entries) {

                        //1.获取表名
                        String tableName = entry.getHeader().getTableName();

                        //2.获取类型
                        CanalEntry.EntryType entryType = entry.getEntryType();

                        //3.获取序列化后的数据
                        ByteString storeValue = entry.getStoreValue();

                        //4.判断当前entryType类型是否为ROWDATA
                        if (!CanalEntry.EntryType.ROWDATA.equals(entryType)) {
                            continue;
                        }

                        //5.反序列化数据
                        CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);

                        //6.获取当前事件的操作类型
                        CanalEntry.EventType eventType = rowChange.getEventType();

                        //7.获取数据集
                        List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();

                        //8.遍历rowDataList，并打印数据集
                        for (CanalEntry.RowData rowData : rowDataList) {

                            JSONObject beforeData = new JSONObject();
                            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                            for (CanalEntry.Column column : beforeColumnsList) {
                                beforeData.put(column.getName(), column.getValue());
                            }

                            JSONObject afterData = new JSONObject();
                            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                            for (CanalEntry.Column column : afterColumnsList) {
                                afterData.put(column.getName(), column.getValue());
                            }

                            //数据打印
                            log.info("Table:" + tableName +
                                    ",EventType:" + eventType +
                                    ",Before:" + beforeData +
                                    ",After:" + afterData);

                            // 处理数据
                            binlogConsumer.consumer(tableName, eventType, beforeData, afterData);
                        }

                    }
                }
            } catch (Exception e) {
                log.warn("run error", e);
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
