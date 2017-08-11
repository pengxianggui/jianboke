package com.jianboke.web;

import java.time.LocalDateTime;
import java.util.*;

import com.jianboke.domain.AccountDefaultSetting;
import com.jianboke.domain.Article;
import com.jianboke.domain.VerificationCode;
import com.jianboke.domain.criteria.AccountCriteria;
import com.jianboke.domain.criteria.ArticleCriteria;
import com.jianboke.domain.specification.ArticleSpecification;
import com.jianboke.domain.specification.UserSpecification;
import com.jianboke.enumeration.HttpReturnCode;
import com.jianboke.mapper.UsersMapper;
import com.jianboke.model.*;
import com.jianboke.repository.AccountDefaultSettingRepository;
import com.jianboke.repository.UserRepository;
import com.jianboke.repository.VerificationCodeRepository;
import com.jianboke.security.SecurityUtils;
import com.jianboke.utils.FileUploadUtils;
import com.jianboke.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.jianboke.domain.User;
import com.jianboke.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api")
public class AccountController {
	
    private final Logger log = LoggerFactory.getLogger(AccountController.class);
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileUploadUtils fileUploadUtils;

    @Autowired
    private AccountDefaultSettingRepository accountDefaultSettingRepository;
    
    @RequestMapping(value = "/account", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getAccount() {
    	log.info("用户权限信息获取");
    	return Optional.ofNullable(userService.getUserWithAuthorities())
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @RequestMapping(value = "/account/usernameUniqueValid", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidationResult> usernameUniqueValid(@RequestBody ValidationModel validation) {
        log.info("校验用户名的唯一性:{}" , validation.toString());
        return userRepository.findOneByUsername(validation.getValue())
                .map(user -> ResponseEntity.ok().body(ValidationResult.INVALID)) // user不为空表示唯一性验证失败
                .orElse(ResponseEntity.ok().body(ValidationResult.VALID)); // 否则唯一性验证成功
    }

    @RequestMapping(value = "/account/emailUniqueValid", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidationResult> emailUniqueValid(@RequestBody ValidationModel validation) {
        log.info("校验注册邮箱的唯一性:{}", validation.toString());
        return userRepository.findOneByEmail(validation.getValue())
                .map(user -> ResponseEntity.ok().body(ValidationResult.INVALID)) // user不为空表示唯一性验证失败
                .orElse(ResponseEntity.ok().body(ValidationResult.VALID)); // 否则唯一性验证成功
    }

    @RequestMapping(value = "/account/sendEmailValidCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidationResult> sendEmailValidCode(@RequestBody EmailValidCodeModel model) {
        log.info("发送邮件验证码 :{}", model.getEmail());
        String code = StringUtils.randomNumberStr(6);
        if (userService.sendEmailCodeForVaild(model.getEmail(), code)) {
            // TODO 将code存储起来
            return verificationCodeRepository.findOneByEmail(model.getEmail())
            .map(vc -> {
                vc.setCode(code);
                verificationCodeRepository.saveAndFlush(vc);
                return ResponseEntity.ok().body(ValidationResult.VALID);
            }).orElseGet(() -> {
                verificationCodeRepository.saveAndFlush(new VerificationCode(model.getEmail(), code));
                return ResponseEntity.ok().body(ValidationResult.VALID);
            });
        }
        return ResponseEntity.ok().body(ValidationResult.INVALID);
    }

    @RequestMapping(value = "/account/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> register(@RequestBody UsersModel model) {
        log.info("Rest Request to register a new user :{}", model);
        // 校验username和email都在数据库中不存在
        if (userRepository.findOneByEmail(model.getEmail()).isPresent()
                || userRepository.findOneByUsername(model.getUsername()).isPresent()) {
            return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_ACCOUNT_IS_EXIST));
        }
        return verificationCodeRepository.findOneByEmail(model.getEmail())
            .filter(verificationCode -> userService.verificationCodeValid(model, verificationCode))
            .map(verificationCode -> {
                User user = usersMapper.modelToEntity(model);
                user.setPassword(passwordEncoder.encode(model.getPassword())); // 加密
                userRepository.saveAndFlush(user);
                User u = user;
                u.setPassword(model.getPassword());
                return ResponseEntity.ok()
                    .body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, u));
            })
            .orElse(ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_VERIFICATION_CODE_WRONG)));
    }

    /**
     * 上传用户头像
     * @param file
     * @return
     */
    @Transactional
    @RequestMapping(value = "/account/avator", method = RequestMethod.POST)
    public ResponseEntity<RequestResult> uploadAvator(@RequestParam("file") MultipartFile file) {
        log.info("rest request to upload avator for the user:{}", SecurityUtils.getCurrentUsername());
        ResponseEntity<RequestResult> result =  fileUploadUtils.uploadAvator(file);
        if (result.getBody().getCode().equals("0000")) { // 保存路径
            userService.updateAvatarPath((String) result.getBody().getData());
        }
        return result;
    }

    @RequestMapping(value = "/account/updateUsername/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> updateUsername(@PathVariable String username) {
        log.info("rest request to update the username for :{}", SecurityUtils.getCurrentUsername());
        if(userService.updateUsername(username) != null) {
            return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS));
        }
        return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_ERROR));
    }

    @RequestMapping(value = "/account/getShowDarkTheme", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> getShowDarkTheme() {
        log.info("REST resquest to get showDarkTheme or not...");
        User user = userService.getUserWithAuthorities();
        return accountDefaultSettingRepository.findOneByUserId(user.getId())
                .map(setting -> {
                    System.out.println(setting.toString());
                    return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, setting.isDarkTheme()));
                })
                .orElseGet(null);
    }

    @RequestMapping(value = "/account/saveShowDarkTheme/{showDarkTheme}", method = RequestMethod.GET)
    public void saveShowDarkTheme(@PathVariable Boolean showDarkTheme) {
        log.info("REST request to save showDarkTheme:{}", showDarkTheme);
        User user = userService.getUserWithAuthorities();
        accountDefaultSettingRepository.findOneByUserId(user.getId())
            .map(setting -> {
            setting.setDarkTheme(showDarkTheme);
            accountDefaultSettingRepository.saveAndFlush(setting);
            return setting;
        });
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
        log.info("The User:{} search the users under the global with criteria:{}", u, criteria);
        Page<User> page = userRepository.findAll(new UserSpecification(criteria), pageable);
        List<UsersModel> list = new ArrayList<>();
        page.getContent().forEach(t -> {
            UsersModel model = usersMapper.entityToModel(t);
            if (u.getAttentions().contains(t)) { // 已关注的
                model.setAttention(true);
            } else {
                model.setAttention(false);
            }
            list.add(model);
        });
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    /**
     * 获取当前用户下所有的关注用户or粉丝
     * @param findBy
     * @return
     */
    @RequestMapping(value = "/account/query/{findBy}", method = RequestMethod.GET)
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<RequestResult> query(@PathVariable String findBy) {
        User user = userService.getUserWithAuthorities();
        List<UsersModel> models = new ArrayList<>();
        log.info("get all attentions or fans under the User:{}, findBy:{}", user, findBy);
        if (findBy == null) return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_PARAM_WRONG));
        Set<User> hasAttentionsSet = userService.getAttentionsByUser(user);
        if (findBy.equals("attentions")) { // 查找关注者
            hasAttentionsSet.forEach(u -> {
                UsersModel model = usersMapper.entityToModel(u);
                model.setAttention(true);
                models.add(model);
            });
        } else { // 查找粉丝
            userService.getFansByUser(user).forEach(u -> {
                UsersModel model = usersMapper.entityToModel(u);
                if (hasAttentionsSet.contains(u)) { // 已关注的
                    model.setAttention(true);
                } else {
                    model.setAttention(false);
                }
                models.add(model);
            });
        }
        return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, models));
    }

    /**
     * 关注 or 取消关注
     * @param userId 操作对象
     * @return
     */
    @RequestMapping(value = "/account/follow/{userId}", method = RequestMethod.GET)
    public ResponseEntity<RequestResult> follow(@PathVariable Long userId) {
        User fromU = userService.getUserWithAuthorities();
        User toU = userRepository.findOne(userId);
        log.info("the User:{} rest request to follow the User:{}", fromU, toU);
        if (fromU.getAttentions().contains(toU)) {
            log.info("attention already! cancel attention...");
            fromU.getAttentions().remove(toU);
        } else {
            log.info("haven't attention. attention...");
            fromU.getAttentions().add(toU);
        }
        userRepository.saveAndFlush(fromU);
        return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS));
    }
}
