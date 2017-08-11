package com.jianboke.pub.controller;

import com.jianboke.domain.User;
import com.jianboke.domain.criteria.AccountCriteria;
import com.jianboke.domain.specification.UserSpecification;
import com.jianboke.enumeration.HttpReturnCode;
import com.jianboke.mapper.ArticleMapper;
import com.jianboke.mapper.UsersMapper;
import com.jianboke.model.RequestResult;
import com.jianboke.model.UsersModel;
import com.jianboke.repository.ArticleRepository;
import com.jianboke.repository.UserRepository;
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

import java.util.*;

/**
 * Created by pengxg on 2017/8/6.
 */
@RestController
@RequestMapping(value = "/pub")
public class PubAccountController {

    private final Logger log = LoggerFactory.getLogger(PubAccountController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ArticleRepository articleRepository;


    @RequestMapping(value = "/account/getAuthorNameByArticleId/{articleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<List<String>> getAuthorNamesByArticleId(@PathVariable Long articleId) {
        log.info("Pub Rest request to find author names of a article which id:{}", articleId);
        return articleRepository.findAuthorNameByArticleId(articleId)
                .map(list -> ResponseEntity.ok().body(list))
                .orElse(ResponseEntity.ok().body(new ArrayList<String>()));
    }

    /**
     * 分页、关键字搜索库中所有相关博客。用于全局搜索
     * @param criteria 只有filter有值
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/account/queryAll", method = RequestMethod.GET)
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<UsersModel> queryAll(@ModelAttribute AccountCriteria criteria, @PageableDefault Pageable pageable) {
        User u = userService.getUserWithAuthorities();
        log.info("The vistor search the users under the global with criteria:{}, user:{}", criteria, u);
        Page<User> page = userRepository.findAll(new UserSpecification(criteria), pageable);
        List<UsersModel> list = new ArrayList<>();
        page.getContent().forEach(t -> {
            UsersModel model = usersMapper.entityToModel(t);
            if (u != null) {
                if (u.getAttentions().contains(t)) { // 已关注的
                    model.setAttention(true);
                } else {
                    model.setAttention(false);
                }
            } else {
                model.setAttention(false);
            }
            list.add(model);
        });
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    /**
     * 获取某个用户下所有的关注用户
     * @param username
     * @return
     */
    @RequestMapping(value = "/account/attentions/{username}", method = RequestMethod.GET)
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<RequestResult> queryAttentions(@PathVariable String username) {
        User currentUser = userService.getUserWithAuthorities(); // 查询当前用户。null为游客
        List<UsersModel> models = new ArrayList<>();
        return userRepository.findOneByUsername(username).map(user -> {
            log.info("Pub get all attentions under the User:{}, username:{}", user, username);
            Set<User> hasAttentionsSet = userService.getAttentionsByUser(user);
            hasAttentionsSet.forEach(attentionUser -> {
                UsersModel model = usersMapper.entityToModel(attentionUser);
                if (currentUser != null && currentUser.getAttentions().contains(attentionUser)) { // 如果存在当前用户，则判断
                    model.setAttention(true);
                } else {
                    model.setAttention(false);
                }
                models.add(model);
            });
            log.info("the attentions of user:{} are:{}", user, models);
            return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, models));
        }).orElse(ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_RESOURCE_NOT_FOUND)));
    }

    /**
     * 获取某个用户下所有的粉丝用户
     * @param username
     * @return
     */
    @RequestMapping(value = "/account/fans/{username}", method = RequestMethod.GET)
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<RequestResult> queryFans(@PathVariable String username) {
        User currentUser = userService.getUserWithAuthorities(); // 查询当前用户。null为游客
        List<UsersModel> models = new ArrayList<>();
        return userRepository.findOneByUsername(username).map(user -> {
            log.info("Pub get all fans under the User:{}, username:{}", user, username);
            Set<User> hasFansSet = userService.getFansByUser(user);
            hasFansSet.forEach(fansUser -> {
                UsersModel model = usersMapper.entityToModel(fansUser);
                if (currentUser != null && currentUser.getAttentions().contains(fansUser)) { // 如果存在当前用户，则判断
                    model.setAttention(true);
                } else {
                    model.setAttention(false);
                }
                models.add(model);
            });
            log.info("the fans of user:{} are:{}", user, models);
            return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, models));
        }).orElse(ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_RESOURCE_NOT_FOUND)));
    }


    @RequestMapping(value = "/account/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> getByUsername(@PathVariable String username) {
        log.info("REST request to get a user by username:{}", username);
        return userRepository.findOneByUsername(username)
                .map(user -> {
                    UsersModel model = usersMapper.entityToModel(user);
                    return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, model));
                })
                .orElse(ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_RESOURCE_NOT_FOUND)));
    }

    // 获取关注数
    @RequestMapping(value = "/account/getNumOfAttentions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> getNumOfAttentions(@PathVariable Long id) {
        log.info("Pub Rest request to get num of attentions by user which id:{}", id);
        Integer sum = userRepository.getNumOfAttentions(id);
        log.info("sum:{}", sum);
        return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, sum));
    }
    // 获取关注数
    @RequestMapping(value = "/account/getNumOfFans/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> getNumOfFans(@PathVariable Long id) {
        log.info("Pub Rest request to get num of fans by user which id:{}", id);
        Integer sum = userRepository.getNumOfFans(id);
        log.info("sum:{}", sum);
        return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, sum));
    }
    // 获取关注数
    @RequestMapping(value = "/account/getNumOfArticles/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> getNumOfArticles(@PathVariable Long id) {
        log.info("Pub Rest request to get num of articles by user which id:{}", id);
        Integer sum = userRepository.getNumOfArticles(id);
        log.info("sum:{}", sum);
        return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, sum));
    }
}
