package com.ruoyi.canal;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试mq
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/test-canal")
public class TestCanalController {


    /**
     * 发送消息Rabbitmq
     *
     * @param msg 消息内容
     * @param delay 延时时间
     */
  /*  @GetMapping("/sendRabbitmq")
    public R<Void> sendRabbitmq(String msg, Long delay) {
        delayProducer.sendMsg(msg, delay);
        return R.ok();
    }*/

}
