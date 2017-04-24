package com.jianboke.mapper;

import com.jianboke.domain.User;
import com.jianboke.model.UsersModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by pengxg on 2017/4/23.
 */

@Mapper(componentModel = "spring", uses = {})
public interface UsersMapper {

    UsersModel entityToModel(User user);

    User modelToEntity(UsersModel model);
}
