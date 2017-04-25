package com.jianboke.web;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.jianboke.domain.VerificationCode;
import com.jianboke.enumeration.HttpReturnCode;
import com.jianboke.mapper.UsersMapper;
import com.jianboke.model.*;
import com.jianboke.repository.UserRepository;
import com.jianboke.repository.VerificationCodeRepository;
import com.jianboke.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.jianboke.domain.User;
import com.jianboke.service.UserService;

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
    
    @RequestMapping(value = "/account", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getAccount() {
    	log.info("用户权限信息获取");
    	return Optional.ofNullable(userService.getUserWithAuthorities())
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getAccountName(@PathVariable Long id) {
        log.info("查询账户名");
        Map<String, String> map = new HashMap<>();
        String username = userService.findUsernameById(id);
        map.put("data", username);
        return map;
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


        System.out.println("test----------------------------");
        return verificationCodeRepository.findOneByEmail(model.getEmail())
            .filter(verificationCode -> userService.verificationCodeValid(model, verificationCode))
            .map(verificationCode -> ResponseEntity.ok()
                    .body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, userRepository.saveAndFlush(usersMapper.modelToEntity(model)))))
            .orElse(ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_VERIFICATION_CODE_WRONG)));
    }
}
