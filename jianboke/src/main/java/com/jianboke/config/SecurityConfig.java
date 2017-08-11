package com.jianboke.config;

import com.jianboke.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

	@Autowired
	private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;  
	
	@Autowired
	private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;
	
	@Autowired
	private Http401UnauthorizedEntryPoint authenticationEntryPoint;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private RememberMeServices rememberMeServices;

	@Autowired
	private AppProperties appProperties;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Override
//	protected UserDetailsService userDetailsService() {
//		// TODO Auto-generated method stub
////		return super.userDetailsService();
//		return new CustomUserDetailsService();
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
				.disable()
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint)
		.and()
			.rememberMe()
			.rememberMeServices(rememberMeServices)
			.rememberMeParameter("remember-me")
			.key(appProperties.getSecurity().getKey())
		.and()
			.formLogin()
		    .loginProcessingUrl("/api/authentication")
		    .successHandler(ajaxAuthenticationSuccessHandler)
		    .failureHandler(ajaxAuthenticationFailureHandler)
		    .usernameParameter("jbk_username")
		    .passwordParameter("jbk_password")
		    .permitAll()
		.and().logout()	// 登出方法也要被放行
		    .logoutUrl("/api/logout")
		    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
			.deleteCookies("JSESSIONID")
			.permitAll()
		.and()
			.authorizeRequests()
			.antMatchers("/").permitAll() // "/"这个路径允许所有行为
			.antMatchers("/api/account/usernameUniqueValid").permitAll()
			.antMatchers("/api/account/emailUniqueValid").permitAll()
			.antMatchers("/api/account/sendEmailValidCode").permitAll()
			.antMatchers("/api/account/register").permitAll()
			.antMatchers("/pub/**").permitAll() // 公开资源：无需登录
			.anyRequest().authenticated(); // 其他路径都会被加上权限拦截;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring()
	    .antMatchers("/bower_components/**")
	    .antMatchers("/fonts/**")
	    .antMatchers("/images/**")
	    .antMatchers("/img/**")
	    .antMatchers("/scripts/**")
	    .antMatchers("/style/**")
	    .antMatchers("/views/**")
	    .antMatchers("/resources/**")
		.antMatchers("/favicon.ico");
	}
	
//	@Override
//    protected void configure(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.userDetailsService(userDetailsService);
////				.passwordEncoder(passwordEncoder()); // 暂时先屏蔽密码加密
//    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//			.withUser("user")
//			.password("password")
//			.roles("USER")
//		.and()
//			.withUser("admin")
//			.password("password")
//			.roles("ADMIN", "USER");
		auth.userDetailsService(userDetailsService);//.passwordEncoder(passwordEncoder());
	}
}
