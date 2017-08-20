package com.jianboke.mapper;

import com.jianboke.domain.Article;
import com.jianboke.domain.Comment;
import com.jianboke.domain.User;
import com.jianboke.model.CommentModel;
import com.jianboke.model.UsersModel;
import com.jianboke.repository.ArticleRepository;
import com.jianboke.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pengxg on 2017/8/17.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class CommentMapper {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Mappings({
//            @Mapping(source = "comment.article", target = "articleId"),
            @Mapping(source = "comment.fromUid", target = "fromUser")
    })
    public abstract CommentModel entityToModel(Comment comment);

    @Mappings({
//            @Mapping(source = "model.articleId", target = "article"),
            @Mapping(source = "model.fromUser", target = "fromUid")
    })
    public abstract Comment modelToEntity(CommentModel model);
//
//    public Long articleToId(Article article) {
//        return article.getId();
//    }
//
//    public Article idToArticle(Long id) {
//        return articleRepository.findOne(id);
//    }

    public Long fromUerToFromUid(UsersModel fromUser) {
        return fromUser.getId();
    }

    public UsersModel fromUidToFromUser(Long fromUid) {
        return usersMapper.entityToModel(userRepository.findOne(fromUid));
    }
}
