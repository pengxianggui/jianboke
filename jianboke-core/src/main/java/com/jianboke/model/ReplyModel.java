package com.jianboke.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jianboke.domain.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by pengxg on 2017/8/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplyModel extends BaseModel {

    private Long commentId;

    private Long fromUid;

    private Long toUid;

    private UsersModel fromUserModel; // 当前回复的发起者

    private UsersModel toUserModel; // 当前回复评论的回复对象

    private String content; // 内容

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public UsersModel getFromUserModel() {
        return fromUserModel;
    }

    public void setFromUserModel(UsersModel fromUserModel) {
        this.fromUserModel = fromUserModel;
    }

    public UsersModel getToUserModel() {
        return toUserModel;
    }

    public void setToUserModel(UsersModel toUserModel) {
        this.toUserModel = toUserModel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFromUid() {
        return fromUid;
    }

    public void setFromUid(Long fromUid) {
        this.fromUid = fromUid;
    }

    public Long getToUid() {
        return toUid;
    }

    public void setToUid(Long toUid) {
        this.toUid = toUid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
