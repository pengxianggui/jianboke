package com.jianboke.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;

/**
 * Created by pengxg on 2017/8/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentModel extends BaseModel {

    private Long articleId;

    private UsersModel fromUser;

    private String content;

    private Set<ReplyModel> replys;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public UsersModel getFromUser() {
        return fromUser;
    }

    public void setFromUser(UsersModel fromUser) {
        this.fromUser = fromUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<ReplyModel> getReplys() {
        return replys;
    }

    public void setReplys(Set<ReplyModel> replys) {
        this.replys = replys;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
