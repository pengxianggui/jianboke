package com.jianboke.domain.specification;

import com.jianboke.domain.Article_;
import com.jianboke.domain.User;
import com.jianboke.domain.User_;
import com.jianboke.domain.criteria.AccountCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by pengxg on 2017/7/23.
 */
public class UserSpecification extends AbstractSpecification<User> {

    private AccountCriteria criteria;

    public UserSpecification(AccountCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions =  predicate.getExpressions();

        if (criteria.getKeyWord() != null) { // 只根据用户名查询
            expressions.add(cb.like(cb.lower(root.get(User_.username)), wildcardsAndLower(criteria.getKeyWord())));
        }
        return predicate;
    }
}
