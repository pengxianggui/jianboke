package com.jianboke.web;

import com.jianboke.domain.Comment;
import com.jianboke.domain.Reply;
import com.jianboke.domain.User;
import com.jianboke.enumeration.ResourceName;
import com.jianboke.mapper.ReplyMapper;
import com.jianboke.mapper.UsersMapper;
import com.jianboke.model.CommentModel;
import com.jianboke.model.ReplyModel;
import com.jianboke.repository.ReplyRepository;
import com.jianboke.repository.UserRepository;
import com.jianboke.service.UserAuhtorityService;
import com.jianboke.service.UserService;
import com.jianboke.utils.HeaderUtil;
import com.jianboke.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by pengxg on 2017/8/22.
 */
@RestController
@RequestMapping("/api")
public class ReplyController {

    private static final Logger log = LoggerFactory.getLogger(ReplyController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private UserAuhtorityService userAuhtorityService;
    /**
     * 创建一个评论
     * @param model
     * @return
     */
    @RequestMapping(value = "/reply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReplyModel> create(@Valid @RequestBody ReplyModel model) throws URISyntaxException {
        log.info("Save a reply:{}", model);
        // TODO 保存chapter，并以model形式返回该chapter
        if (model.getId() != null) {
            return ResponseEntity.ok(null); // 回复无法编辑
        }
        User fromUser = userService.getUserWithAuthorities();
        model.setFromUserModel(usersMapper.entityToModel(fromUser));
//        User toUser = userRepository.findOne(model.getToUid());
        // todo 根据content中的 '@username' 来判断回复的对象
        String toUsername = StringUtils.regexUsername(model.getContent());
        return userRepository.findOneByUsername(toUsername).map(toUser -> {
            model.setContent(model.getContent().replace("@" + toUsername, ""));
            model.setToUserModel(usersMapper.entityToModel(toUser));
            Reply result = replyRepository.saveAndFlush(replyMapper.modelToEntity(model));
            return ResponseEntity.ok().body(replyMapper.entityToModel(result));
        }).orElseGet(() -> {
            Reply result = replyRepository.saveAndFlush(replyMapper.modelToEntity(model));
            return ResponseEntity.ok().body(replyMapper.entityToModel(result));
        });
    }

    @javax.transaction.Transactional
    @RequestMapping(value = "/reply/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReplyModel remove(@PathVariable Long id) {
        log.info("request to delete a reply which id is :{}", id);
        // TODO 权限校验
        if (userAuhtorityService.ifHasAuthority(ResourceName.REPLY, id)) {
            Reply reply = replyRepository.findOne(id);
            replyRepository.delete(reply);
            return replyMapper.entityToModel(reply);
        } else {
            return null;
        }
    }
}
