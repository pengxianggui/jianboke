package com.jianboke.service;

import com.jianboke.domain.Mail;
import com.jianboke.utils.MailUtil;
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
	  private PasswordEncoder passwordEncoder;

	  @Autowired
	  private UserRepository userRepository;

	  @Autowired
	  private MailUtil mailUtil;


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

	/**
	 * 根据用户id查找一个用户的用户名
	 * @param id
	 * @return
	 */
	public String findUsernameById(Long id) {
		User user = userRepository.findOne(id);
		if (user == null) return null;
		return user.getUsername();
	}

	/**
	 * 验证邮箱
	 * @param receiver
	 * @param code 校验码
	 * @return
	 */
	public boolean sendEmailCodeForVaild(String receiver, String code) {
		Mail mail = mailUtil.getMail();
		String subject = "验证码";
		StringBuffer message = new StringBuffer();
		message.append("^_^, 您终于来了！<br/>");
		message.append("感谢您注册<b>简博客</b>, 你的验证码为<code>" + code + "</code>。");
		message.append("请在30分钟完成注册，验证码过期失效。<br/>");
		message.append("(ps: 如果这不是您本人操作，请忽略此邮件。)");
		mail.setSubject(subject);
		mail.setMessage(message.toString());
		mail.setReceiver(receiver);
		return mailUtil.send(mail);
	}
}
