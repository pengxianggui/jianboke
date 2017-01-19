package com.jianboke.security;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.jianboke.domain.User;
import com.jianboke.repository.UserRepository;

/**
 * 自定义的UserDetailService。为了使得security能从数据库中验证用户。
 * @author pengxg
 *
 */
@Component("CustomUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String lowerUsername = username.toLowerCase(); // 忽略用户名的大小写
		System.out.println("用户名：--------------" + lowerUsername);
		Optional<User> userFromDatabase = userRepository.findOneByUsername(lowerUsername);
		log.debug("user get from database: {}", userFromDatabase);
		// 暂时不加授权，后期引入Role对象
//		List<Role> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
//        System.err.println("username is " + username + ", " + user.getRole().name());
//        return new org.springframework.security.core.userdetails.User(userFromDatabase.get().getUsername(),
//        		userFromDatabase.get().getPassword(), null);
//		return userFromDatabase.get();
		
		return userFromDatabase.map(user -> {
			System.out.println("用户" + user.toString());
			return user;
		}).orElseThrow(() -> new UsernameNotFoundException(
				"User " + lowerUsername + "was not found in the database"));
	}

}
