package com.jianboke.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="jianbokeEmail")
public class MailConfig {
	public static final String ENCODEING = "UTF-8";  
	  
    private String host; // 服务器地址  
  
    private String sender; // 发件人的邮箱  
  
    private String receiver; // 收件人的邮箱  
  
    private String nickname; // 发件人昵称  
  
    private String username; // 账号  
  
    private String password; // 密码  
  
    private String subject; // 主题  
  
    private String message; // 信息(支持HTML)  
    
    private String expireTime;//密钥过期时间，暂定30分钟
  
    public MailConfig() {}
    
    /**
     * @param host String 服务器地址
     * @param sender String 发件人邮箱
     * @param name String 发件人昵称
     * @param username String 发件人账号
     * @param password String 发件人密码
     */
//    public Mail(String host, String sender, String nickname, String username, String password, String subject) {
//		super();
//		this.host = host;
//		this.sender = sender;
//		this.nickname = nickname;
//		this.username = username;
//		this.password = password;
//		this.subject = subject;
//	}

	public String getHost() {  
        return host;  
    }  
  
    public void setHost(String host) {  
        this.host = host;  
    }  
  
    public String getSender() {  
        return sender;  
    }  
  
    public void setSender(String sender) {  
        this.sender = sender;  
    }  
  
    public String getReceiver() {  
        return receiver;  
    }  
  
    public void setReceiver(String receiver) {  
        this.receiver = receiver;  
    }  
  
    public String getNickname() {  
        return nickname;  
    }  
  
    public void setNickname(String nickname) {  
        this.nickname = nickname;  
    }  
  
    public String getUsername() {  
        return username;  
    }  
  
    public void setUsername(String username) {  
        this.username = username;  
    }  
  
    public String getPassword() {  
        return password;  
    }  
  
    public void setPassword(String password) {  
        this.password = password;  
    }  
  
    public String getSubject() {  
        return subject;  
    }  
  
    public void setSubject(String subject) {  
        this.subject = subject;  
    }  
  
    public String getMessage() {  
        return message;  
    }  
  
    public void setMessage(String message) {  
        this.message = message;  
    }
    
	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public String toString() {
		return "Mail [host=" + host + ", sender=" + sender + ", receiver=" + receiver + ", nickname=" + nickname
				+ ", username=" + username + ", password=" + password + ", subject=" + subject + ", message=" + message
				+ "]";
	}  
}
