package com.jianboke.web;

import com.jianboke.domain.Article;
import com.jianboke.domain.Comment;
import com.jianboke.domain.CommentLike;
import com.jianboke.domain.User;
import com.jianboke.enumeration.HttpReturnCode;
import com.jianboke.enumeration.ResourceName;
import com.jianboke.mapper.CommentLikeMapper;
import com.jianboke.mapper.CommentMapper;
import com.jianboke.mapper.UsersMapper;
import com.jianboke.model.CommentLikeModel;
import com.jianboke.model.CommentModel;
import com.jianboke.model.RequestResult;
import com.jianboke.repository.*;
import com.jianboke.service.CommentService;
import com.jianboke.service.UserAuhtorityService;
import com.jianboke.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

/**
 * Created by pengxg on 2017/8/19.
 */
@RestController
@RequestMapping("/api")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UserAuhtorityService userAuhtorityService;

    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private CommentLikeMapper commentLikeMapper;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentService commentService;

    /**
     * 创建一个评论
     * @param model
     * @return
     */
    @RequestMapping(value = "/comment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> create(@Valid @RequestBody CommentModel model) throws URISyntaxException {
        log.info("Save a comment:{}", model);
        // TODO 保存chapter，并以model形式返回该chapter
        if (model.getId() != null || model.getArticleId() == null) {
            return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_PARAM_WRONG)); // 评论无法编辑, articleId不为空
        }
        Article article = articleRepository.findOne(model.getArticleId());
        if (!article.isIfPublic() || !article.isIfAllowComment()) { // 不允许评论
            return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_WITHOUT_AUTHORITY, "操作无权限，作者已禁止评论"));
        }
        User user = userService.getUserWithAuthorities();
        model.setFromUser(usersMapper.entityToModel(user));
        Comment result = commentRepository.saveAndFlush(commentMapper.modelToEntity(model));
        return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, commentMapper.entityToModel(result)));
    }


    @RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentModel remove(@PathVariable Long id) {
        log.info("request to delete a comment which id is :{}", id);
        // TODO 权限校验
        if (userAuhtorityService.ifHasAuthority(ResourceName.COMMENT, id)) {
            Comment comment = commentRepository.findOne(id);
            CommentModel model = commentMapper.entityToModel(comment);
            commentService.deleteComment(comment);
            return model;
        } else {
            return null;
        }
    }

    // 喜欢一个评论/取消喜爱
    @RequestMapping(value = "/comment/like", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> modifyCommentLike(@RequestParam("commentId") Long commentId) {
        log.info("REST request to modify a like to commentId:{}", commentId);
        User user = userService.getUserWithAuthorities();
        return commentLikeRepository.findByCommentIdAndFromUid(commentId, user.getId())
                .map(commentLike -> { // 取消喜欢
                    commentLikeRepository.delete(commentLike);
                    CommentLikeModel model = commentLikeMapper.entityToModel(commentLike);
                    model.setLiked(false);
                    return ResponseEntity.ok().body(
                            RequestResult.create(HttpReturnCode.JBK_SUCCESS, model)
                    );
                }).orElseGet(() -> { // 喜欢
                    CommentLike like = new CommentLike();
                    like.setFromUid(user.getId());
                    like.setCommentId(commentId);
                    CommentLikeModel model = commentLikeMapper.entityToModel(commentLikeRepository.saveAndFlush(like));
                    model.setLiked(true);
                    return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, model));
                });
    }
}
