package com.jianboke.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jianboke.domain.User;
import com.jianboke.repository.UserRepository;
import com.jianboke.security.SecurityUtils;

@Service
@Transactional
public class UserService {
	  private final Logger log = LoggerFactory.getLogger(UserService.class);
	  
	  @Autowired
	  private UserRepository userRepository;
	  
	  private PasswordEncoder passwordEncoder;
	  /**
	   * 获取当前登录的用户的User对象。
	   * @return
	   */
	  @Transactional(readOnly = true)
	  public User getUserWithAuthorities() {
		  User user = userRepository.findOneByUsername(SecurityUtils.getCurrentUsername()).get();
		  return user;
	  }
	  
	  /**
	   * 注册功能:创建用户
	   * @param user
	   * @return
	   */
	  public User createUser(User user) {
		  user.setPassword(passwordEncoder.encode(user.getPassword()));
		  log.debug("Create a User : {}" , user);
		  return userRepository.save(user);
	  }
	  
	  /**
	   * 更改密码
	   * @param password
	   */
	  public User changePassword(String password) {
		  User user = this.getUserWithAuthorities();
		  user.setPassword(passwordEncoder.encode(password));
		  userRepository.save(user);
	      log.debug("Changed password for User: {}", user);
	      return user;
	  }
	  
	  /**
	   * 更新电话号码
	   * @param phonenum
	   * @return
	   */
	  public User updatePhoneNum(String phonenum) {
		  User user = this.getUserWithAuthorities();
		  user.setPhonenum(phonenum);
		  userRepository.save(user);
		  log.debug("Changed phonenum for User: {}", user);
		  return user;
	  }
	  
	  /**
	   * 更新邮箱
	   * @param email
	   * @return
	   */
	  public User updateEmail(String email) {
		  User user = this.getUserWithAuthorities();
		  user.setEmail(email);
		  userRepository.save(user);
	      log.debug("Changed email for User: {}", user);
		  return user;
	  }
	  
	  /**
	   * 更新头像
	   */
	  public User updateAvatarPath(String avatarPath) {
		  User user = this.getUserWithAuthorities();
		  user.setAvatarPath(avatarPath);
		  userRepository.save(user);
	      log.debug("Changed avatarPath for User: {}", user);
		  return user;
	  }
}
