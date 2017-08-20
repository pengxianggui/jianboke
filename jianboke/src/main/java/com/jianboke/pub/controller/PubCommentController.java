package com.jianboke.pub.controller;

import com.jianboke.domain.Comment;
import com.jianboke.domain.criteria.CommentCriteria;
import com.jianboke.domain.specification.CommentSpecification;
import com.jianboke.mapper.CommentMapper;
import com.jianboke.model.CommentModel;
import com.jianboke.repository.CommentRepository;
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
import java.util.stream.Collectors;

/**
 * Created by pengxg on 2017/8/19.
 */
@RestController
@RequestMapping(value = "/pub")
public class PubCommentController {
    private static final Logger log = LoggerFactory.getLogger(PubCommentController.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 获取文章下所有的评论（不包含回复）
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/comment/query/{articleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<List<CommentModel>> query(@PathVariable("articleId") Long articleId) {
        log.info("REST request to query comments under the article(which id is :{})", articleId);
        List<CommentModel> models = commentRepository.findAllByArticleId(articleId).stream()
                .map(comment -> commentMapper.entityToModel(comment))
                .collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @RequestMapping(value = "/comment/pageQuery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public Page<CommentModel> query(@ModelAttribute CommentCriteria criteria, @PageableDefault Pageable pageable) {
        log.info("REST request to query comment pageablly, by criteria:{}", criteria);
        Page<Comment> comments = commentRepository.findAll(new CommentSpecification(criteria), pageable);
        List<CommentModel> models = new ArrayList<>();
        comments.getContent().forEach(comment -> models.add(commentMapper.entityToModel(comment)));
        return new PageImpl<>(models, pageable, comments.getTotalElements());
    }
}
