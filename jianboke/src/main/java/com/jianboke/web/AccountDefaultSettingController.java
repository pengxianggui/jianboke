package com.jianboke.web;

import com.jianboke.domain.AccountDefaultSetting;
import com.jianboke.domain.User;
import com.jianboke.enumeration.HttpReturnCode;
import com.jianboke.model.RequestResult;
import com.jianboke.repository.AccountDefaultSettingRepository;
import com.jianboke.repository.UserRepository;
import com.jianboke.service.UserService;
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

/**
 * Created by pengxg on 2017/6/3.
 */
@RestController
@RequestMapping("/api")
public class AccountDefaultSettingController {

    private static final Logger log = LoggerFactory.getLogger(AccountDefaultSettingController.class);
//
//    private AccountDefaultSetting accountDefaultSetting =
//            new AccountDefaultSetting();

    @Autowired
    private UserService userService;

    @Autowired
    private AccountDefaultSettingRepository accountDefaultSettingRepository;


    @RequestMapping(value = "/accountDefaultSetting", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountDefaultSetting get() {
        User user = userService.getUserWithAuthorities();
        log.info("Rest request to get default setting of user:{}", user);
        return accountDefaultSettingRepository.findOneByUserId(user.getId())
                .map(u -> u).orElseGet(() -> {
                    AccountDefaultSetting defaultSetting =
                            accountDefaultSettingRepository.saveAndFlush(AccountDefaultSetting.create(user.getId()));
                    return defaultSetting;
                });
    }

    @RequestMapping(value = "/accountDefaultSetting", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RequestResult> save(@Valid @RequestBody AccountDefaultSetting entity) {
        log.info("REST request to save AccountDefaultSetting entity:{}", entity);
        if (accountDefaultSettingRepository.saveAndFlush(entity) != null) {
            return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS));
        }
        return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_ERROR));
    }
}
