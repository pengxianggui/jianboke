package com.jianboke.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 前后端交互model:数据校验model
 * Created by pengxg on 2017/4/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true) // 转json时忽略不知道的属性，防止转失败
public class ValidationModel {

    private Long id;

    private String value;

    public ValidationModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
