package com.ruoyi.canal.esmapper;

import cn.easyes.annotation.IndexName;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文档实体
 */
@Data
//@TableName("document")
@IndexName("document")
public class Document {

    /**
     * es中的唯一id
     */
    private Long id;

    /**
     * 文档标题
     */
    private String title;

    /**
     * 文档内容
     */
    private String content;
}
