package com.jianboke.domain.specification;

import com.jianboke.domain.Article;
import com.jianboke.domain.Article_;
import com.jianboke.domain.criteria.ArticleCriteria;
import com.jianboke.repository.BookRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于高级查询
 * Created by pengxg on 2017/7/3.
 */
public class ArticleSpecification extends AbstractSpecification<Article>{

    private final ArticleCriteria criteria;

    @Autowired
    private BookRepository bookRepository;

    public ArticleSpecification(ArticleCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();
        List<Expression<Boolean>> expressions =  predicate.getExpressions();

        if (StringUtils.isNotBlank(criteria.getFilter())) {
            expressions.add(cb.or(
                cb.like(cb.lower(root.get(Article_.title)), wildcardsAndLower(criteria.getFilter())),
                cb.like(cb.lower(root.get(Article_.labels)),wildcardsAndLower(criteria.getFilter())))
            );
        }

        if (criteria.getBookId() != null) {
            if (criteria.getBookId().equals(0l)) { // 所有书籍中的文章

            } else if (criteria.getBookId().equals(-1l)) { // 未归类的文章
                expressions.add(cb.isEmpty(root.get(Article_.books)));
            } else {
                if (criteria.getBook() != null)
                    expressions.add(cb.isMember(criteria.getBook(), root.get(Article_.books)));
            }
        }

        if (criteria.getTitle() != null) {
            expressions.add(cb.like(cb.lower(root.get(Article_.title)), wildcardsAndLower(criteria.getTitle())));
        }

        if (criteria.getLabels() != null) {
            expressions.add(cb.like(cb.lower(root.get(Article_.labels)), wildcardsAndLower(criteria.getLabels())));
        }

        if (criteria.isIfAllowComment() != null) {
            expressions.add(cb.equal(root.get(Article_.ifAllowComment), criteria.isIfAllowComment()));
        }

        if (criteria.isIfAllowReprint() != null) {
            expressions.add(cb.equal(root.get(Article_.ifAllowReprint), criteria.isIfAllowReprint()));
        }

        if (criteria.isIfAllowSecondAuthor() != null) {
            expressions.add(cb.equal(root.get(Article_.ifAllowSecondAuthor), criteria.isIfAllowSecondAuthor()));
        }

        if (criteria.isIfOriginal() != null) {
            expressions.add(cb.equal(root.get(Article_.ifOriginal), criteria.isIfOriginal()));
        }

        if (criteria.isIfPublic() != null) {
            expressions.add(cb.equal(root.get(Article_.ifPublic), criteria.isIfPublic()));
        }

        // 把Predicate应用到CriteriaQuery中去,因为还可以给CriteriaQuery添加其他的功能，比如排序、分组之类的
        query.where(predicate);
        List<Order> orders = new ArrayList<>();
        orders.add(cb.desc(root.get(Article_.ifSetTop))); // 置顶
        orders.add(cb.desc(root.get(Article_.id))); // id倒序
        query.orderBy(orders);
        return query.getRestriction();
    }
}
