package com.jianboke.web;

import com.jianboke.domain.Comment;
import com.jianboke.domain.User;
import com.jianboke.enumeration.ResourceName;
import com.jianboke.mapper.CommentMapper;
import com.jianboke.mapper.UsersMapper;
import com.jianboke.model.CommentModel;
import com.jianboke.repository.CommentRepository;
import com.jianboke.repository.ReplyRepository;
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
    private ReplyRepository replyRepository;

    /**
     * 创建一个评论
     * @param model
     * @return
     */
    @RequestMapping(value = "/comment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentModel> create(@Valid @RequestBody CommentModel model) throws URISyntaxException {
        log.info("Save a comment:{}", model);
        // TODO 保存chapter，并以model形式返回该chapter
        if (model.getId() != null) {
            return ResponseEntity.ok(null); // 评论无法编辑
        }
        User user = userService.getUserWithAuthorities();
        model.setFromUser(usersMapper.entityToModel(user));
        Comment result = commentRepository.saveAndFlush(commentMapper.modelToEntity(model));
        return ResponseEntity.ok().body(commentMapper.entityToModel(result));
    }

    @javax.transaction.Transactional
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommentModel remove(@PathVariable Long id) {
        log.info("request to delete a comment which id is :{}", id);
        // TODO 权限校验
        if (userAuhtorityService.ifHasAuthority(ResourceName.COMMENT, id)) {
            Comment comment = commentRepository.findOne(id);
            replyRepository.delete(comment.getReplys());
            commentRepository.delete(comment);
            return commentMapper.entityToModel(comment);
        } else {
            return null;
        }
    }
}
