package com.jianboke.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.jianboke.model.EmailValidCodeModel;
import com.jianboke.model.ValidationModel;
import com.jianboke.model.ValidationResult;
import com.jianboke.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<Boolean> sendEmailValidCode(@RequestBody EmailValidCodeModel model) {
        log.info("发送邮件验证码 :{}", model.getEmail());
        return null;
    }
}
