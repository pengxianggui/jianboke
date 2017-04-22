package com.jianboke.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 验证结果返回model
 * Created by pengxg on 2017/4/21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // springmvc中的标注，是为了控制返回的json字符串显示哪些字段。这里的设置是为null的字段不显示
public class ValidationResult {

    public static final  ValidationResult VALID = new ValidationResult(true); // 验证通过

    public static final ValidationResult INVALID = new ValidationResult(false); // 验证失败

    private boolean valid;
    private Object value;

    public ValidationResult() {}

    public ValidationResult(boolean valid) {
        this.valid = valid;
    }

    public ValidationResult(boolean valid, Object value) {
        this.valid = valid;
        this.value = value;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
