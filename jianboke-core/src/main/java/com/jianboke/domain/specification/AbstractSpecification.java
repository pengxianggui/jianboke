package com.jianboke.domain.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.lowerCase;

/**
 * 拼接 like 模糊查询
 * Created by pengxg on 2017/7/3.
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

    public static final String WILDCARD = "%";

    public String wildcards(String str) {
        return join(WILDCARD, str, WILDCARD);
    }

    public String wildcardsAndLower(String str) {
        return wildcards(lowerCase(str));
    }

}
