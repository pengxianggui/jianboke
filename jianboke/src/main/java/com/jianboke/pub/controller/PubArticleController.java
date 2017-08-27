package com.jianboke.pub.controller;

import com.jianboke.domain.Article;
import com.jianboke.domain.ArticleLike;
import com.jianboke.domain.criteria.ArticleCriteria;
import com.jianboke.domain.specification.ArticleSpecification;
import com.jianboke.enumeration.HttpReturnCode;
import com.jianboke.enumeration.ResourceName;
import com.jianboke.mapper.ArticleLikeMapper;
import com.jianboke.mapper.ArticleMapper;
import com.jianboke.model.ArticleLikeModel;
import com.jianboke.model.ArticleModel;
import com.jianboke.model.RequestResult;
import com.jianboke.repository.ArticleLikeRepository;
import com.jianboke.repository.ArticleRepository;
import com.jianboke.repository.UserRepository;
import com.jianboke.service.UserAuhtorityService;
import com.jianboke.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by pengxg on 2017/8/6.
 */
@RestController
@RequestMapping(value = "/pub")
public class PubArticleController {

    private final Logger log = LoggerFactory.getLogger(PubArticleController.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuhtorityService userAuhtorityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> get(@PathVariable Long id) {
        log.debug("Public REST request to get a Article by id:{}", id);
        // TODO 权限校验
        return Optional.ofNullable(articleRepository.findOne(id)).map(articleMapper::entityToModel)
                // 只显示已公开的文章，未公开需要有权限
                .filter(model -> model.isIfPublic() || userAuhtorityService.ifHasAuthority(ResourceName.ARTICLE, model.getId()))
                .map(model -> ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, model)))
                .orElse(ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_WITHOUT_AUTHORITY)));
    }

    /**
     * 分页、关键字搜索库中所有相关博客。用于全局搜索
     * @param criteria 只有filter有值
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/article/queryAll", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public Page<ArticleModel> queryAll(@ModelAttribute ArticleCriteria criteria, @PageableDefault Pageable pageable) {
        log.info("The vistor search the articles under the global with criteria:{}", criteria);
        Page<Article> page = articleRepository.findAll(new ArticleSpecification(criteria), pageable);
        List<ArticleModel> list = new ArrayList<>();
        page.getContent().forEach(t -> list.add(articleMapper.entityToModel(t)));
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    // 个人主页显示article
    @RequestMapping(value = "/article/queryAllByUsername/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ArticleModel> queryAllByUsername(@PathVariable String username, @PageableDefault Pageable pageable) {
        log.info("REST request to query all books by username:{}", username);
        ArticleCriteria criteria = new ArticleCriteria();
        return userRepository.findOneByUsername(username)
                .map(user -> {
                    criteria.setAuthorId(user.getId());
                    Page<Article> page = articleRepository.findAll(new ArticleSpecification(criteria), pageable); // 分页查询
                    List<ArticleModel> list = new ArrayList<>();
                    page.getContent().forEach(t -> {
                        // 公开的文章才显示在个人主页，否则判断权限
                        if(t.isIfPublic()) {
                            list.add(articleMapper.entityToModel(t));
                        }
                    });
                    return new PageImpl<>(list, pageable, page.getTotalElements());
                })
                .orElseGet(null);
    }

    @RequestMapping(value = "/article/queryAllLikesByArticleId/{articleId}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public List<ArticleLikeModel> queryAllLikesByArticleId(@PathVariable Long articleId) {
        log.info("Rest request to query all likes by articleId:{}", articleId);
        List<ArticleLikeModel> models = new ArrayList<>();
        return articleLikeRepository.findAllByArticleId(articleId)
                .map(articleLikes -> {
                    articleLikes.forEach(articleLike -> models.add(articleLikeMapper.entityToModel(articleLike)));
                    return models;
                }).orElseGet(() -> models);
    }
}
