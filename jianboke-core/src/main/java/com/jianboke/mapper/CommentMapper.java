package com.jianboke.mapper;

import com.jianboke.domain.Article;
import com.jianboke.domain.Comment;
import com.jianboke.domain.Reply;
import com.jianboke.domain.User;
import com.jianboke.model.CommentModel;
import com.jianboke.model.ReplyModel;
import com.jianboke.model.UsersModel;
import com.jianboke.repository.ArticleRepository;
import com.jianboke.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

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

    @Autowired
    private ReplyMapper replyMapper;

    @Mappings({
            @Mapping(source = "comment.replys", target = "replys"),
            @Mapping(source = "comment.fromUid", target = "fromUser")
    })
    public abstract CommentModel entityToModel(Comment comment);

    @Mappings({
            @Mapping(source = "model.replys", target = "replys"),
            @Mapping(source = "model.fromUser", target = "fromUid")
    })
    public abstract Comment modelToEntity(CommentModel model);

    public Set<ReplyModel> replySetToReplyModelSet(Set<Reply> set) {
        if ( set == null ) {
            return null;
        }
        Set<ReplyModel> set1 = new HashSet<ReplyModel>();
        for ( Reply reply : set ) {
            set1.add(replyMapper.entityToModel(reply));
        }
        return set1;
    }

    public Set<Reply> replyModelSetToReplySet(Set<ReplyModel> set) {
        if ( set == null ) {
            return null;
        }
        Set<Reply> set1 = new HashSet<Reply>();
        for ( ReplyModel model : set ) {
            set1.add(replyMapper.modelToEntity(model));
        }
        return set1;
    }

    public Long fromUerToFromUid(UsersModel fromUser) {
        return fromUser.getId();
    }

    public UsersModel fromUidToFromUser(Long fromUid) {
        return usersMapper.entityToModel(userRepository.findOne(fromUid));
    }
}
