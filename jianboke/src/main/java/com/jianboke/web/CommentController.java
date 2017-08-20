package com.jianboke.web;

import com.jianboke.domain.Comment;
import com.jianboke.domain.User;
import com.jianboke.mapper.CommentMapper;
import com.jianboke.mapper.UsersMapper;
import com.jianboke.model.ChapterModel;
import com.jianboke.model.CommentModel;
import com.jianboke.repository.CommentRepository;
import com.jianboke.service.UserService;
import com.jianboke.utils.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
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
        Comment c = commentMapper.modelToEntity(model);
        Comment result = commentRepository.saveAndFlush(c);
        return ResponseEntity.created(new URI("/api/productGroup/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("chapter", result.getId().toString()))
                .body(commentMapper.entityToModel(result));
    }
}
