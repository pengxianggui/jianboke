package com.jianboke.utils;

import com.jianboke.config.MailConfig;
import com.jianboke.domain.Mail;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 邮件工具类，用以强制密码更新后发送邮件通知。继承MailConfig，以便从配置文件中获取公邮信息。
 * @author pengxg
 *
 */
@Service("mailUtil")
public class MailUtil extends MailConfig {
  private final Logger log = LoggerFactory.getLogger(MailUtil.class);

	public boolean send(Mail mail) {
		// 发送email
		HtmlEmail email = new HtmlEmail();
		try {
			// 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
			email.setHostName(mail.getHost());
			// 端口
			email.setSslSmtpPort(String.valueOf(mail.getPort()));
			email.setSSLOnConnect(true);
			// 字符编码集的设置
			email.setCharset(Mail.ENCODEING);
			// 收件人的邮箱
			email.addTo(mail.getReceiver());
			// 发送人的邮箱
			email.setFrom(mail.getSender(), mail.getNickname());
			// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
			email.setAuthentication(mail.getUsername(), mail.getPassword());
			// 要发送的邮件主题
			email.setSubject(mail.getSubject());
			// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
			email.setMsg(mail.getMessage());
			// 发送
			email.send();
			if (log.isDebugEnabled()) {
				log.debug(mail.getSender() + " 发送邮件到 " + mail.getReceiver());
			}
			return true;
		} catch (EmailException e) {
			e.printStackTrace();
			log.info("[{}]",mail.getSender() + " 发送邮件到 " + mail.getReceiver() + " 失败");
			log.info(mail.getSender() + " 发送邮件到 " + mail.getReceiver() + " 失败");
			return false;
		}
	}

	public Mail getMail() {
		Mail mail = new Mail(this.getHost(), this.getPort(), this.getSender(), this.getNickname(), this.getUsername(),
				this.getPassword(), this.getSubject());
		return mail;
	}

}
