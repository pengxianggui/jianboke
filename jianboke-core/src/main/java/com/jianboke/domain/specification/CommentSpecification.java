package com.jianboke.domain.specification;

import com.jianboke.domain.Comment;
import com.jianboke.domain.Comment_;
import com.jianboke.domain.criteria.ArticleCriteria;
import com.jianboke.domain.criteria.CommentCriteria;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengxg on 2017/8/20.
 */
public class CommentSpecification extends AbstractSpecification {

    private CommentCriteria criteria;

    public CommentSpecification(CommentCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions = predicate.getExpressions();

        if (criteria.getArticleId() != null) {
            expressions.add(cb.equal(root.get(Comment_.articleId), criteria.getArticleId()));
        }

        if (criteria.getFromUid() != null) {
            expressions.add(cb.equal(root.get(Comment_.fromUid), criteria.getFromUid()));
        }

        // 把Predicate应用到CriteriaQuery中去,因为还可以给CriteriaQuery添加其他的功能，比如排序、分组之类的
        query.where(predicate);
        List<Order> orders = new ArrayList<>();
        if (criteria.getOrderBy() == null) { // 默认按照created_date降序
            orders.add(cb.desc(root.get(Comment_.createdDate)));
        }
        query.orderBy(orders);
        return query.getRestriction();
    }
}
