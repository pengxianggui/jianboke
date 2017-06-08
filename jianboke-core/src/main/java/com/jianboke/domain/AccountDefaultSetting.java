package com.jianboke.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户默认设置
 * Created by pengxg on 2017/6/3.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "account_default_setting")
public class AccountDefaultSetting extends AbstractAuditingEntity {

    public static AccountDefaultSetting create(Long userId) {
        return new AccountDefaultSetting(userId, false, true, "",
                true, true, true, true);
    }

    public AccountDefaultSetting() {
    }

    public AccountDefaultSetting(Long userId, boolean isDarkTheme, boolean ifAcceptPush, String hobbyLabels, boolean defaultIfPublic, boolean defaultIfAllowReprint, boolean defaultIfAllowComment, boolean defaultIfAllowSecondAuthor) {
        this.userId = userId;
        this.isDarkTheme = isDarkTheme;
        this.ifAcceptPush = ifAcceptPush;
        this.hobbyLabels = hobbyLabels;
        this.defaultIfPublic = defaultIfPublic;
        this.defaultIfAllowReprint = defaultIfAllowReprint;
        this.defaultIfAllowComment = defaultIfAllowComment;
        this.defaultIfAllowSecondAuthor = defaultIfAllowSecondAuthor;
    }

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_dark_theme", nullable = false)
    private boolean isDarkTheme;

    @Column(name = "if_accept_push")
    private boolean ifAcceptPush;

    @Column(name = "hobby_labels")
    private String hobbyLabels;

    @Column(name = "default_if_public")
    private boolean defaultIfPublic;

    @Column(name = "default_if_allow_reprint")
    private boolean defaultIfAllowReprint;

    @Column(name = "default_if_allow_comment")
    private boolean defaultIfAllowComment;

    @Column(name = "default_if_allow_second_author")
    private boolean defaultIfAllowSecondAuthor;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    public boolean isDefaultIfPublic() {
        return defaultIfPublic;
    }

    public void setDefaultIfPublic(boolean defaultIfPublic) {
        this.defaultIfPublic = defaultIfPublic;
    }

    public boolean isDefaultIfAllowReprint() {
        return defaultIfAllowReprint;
    }

    public void setDefaultIfAllowReprint(boolean defaultIfAllowReprint) {
        this.defaultIfAllowReprint = defaultIfAllowReprint;
    }

    public boolean isDefaultIfAllowComment() {
        return defaultIfAllowComment;
    }

    public void setDefaultIfAllowComment(boolean defaultIfAllowComment) {
        this.defaultIfAllowComment = defaultIfAllowComment;
    }

    public boolean isDefaultIfAllowSecondAuthor() {
        return defaultIfAllowSecondAuthor;
    }

    public void setDefaultIfAllowSecondAuthor(boolean defaultIfAllowSecondAuthor) {
        this.defaultIfAllowSecondAuthor = defaultIfAllowSecondAuthor;
    }

    public boolean isIfAcceptPush() {
        return ifAcceptPush;
    }

    public void setIfAcceptPush(boolean ifAcceptPush) {
        this.ifAcceptPush = ifAcceptPush;
    }

    public String getHobbyLabels() {
        return hobbyLabels;
    }

    public void setHobbyLabels(String hobbyLabels) {
        this.hobbyLabels = hobbyLabels;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
