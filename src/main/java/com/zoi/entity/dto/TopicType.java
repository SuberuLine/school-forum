package com.zoi.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zoi.entity.BaseData;
import lombok.Data;

@Data
@TableName("db_topic_type")
public class TopicType implements BaseData {
    Integer id;
    String name;
    @TableField("`desc`")
    String desc;
    String color;
}
