package com.jianboke.mapper;

import com.jianboke.domain.Reply;
import com.jianboke.domain.User;
import com.jianboke.model.ReplyModel;
import com.jianboke.model.UsersModel;
import com.jianboke.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pengxg on 2017/8/22.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class ReplyMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Mappings({
            @Mapping(source = "reply.fromUid", target = "fromUserModel"),
            @Mapping(source = "reply.toUid", target = "toUserModel")
    })
    public abstract ReplyModel entityToModel(Reply reply);

    @Mappings({
            @Mapping(source = "model.fromUserModel", target = "fromUid"),
            @Mapping(source = "model.toUserModel", target = "toUid")
    })
    public abstract Reply modelToEntity(ReplyModel model);

    public UsersModel uidToUserModel(Long uid) {
        if (uid == null) return null;
        return usersMapper.entityToModel(userRepository.findOne(uid));
    }

    public Long userModelToUid(UsersModel model) {
        if (model == null) return null;
        return model.getId();
    }


}
