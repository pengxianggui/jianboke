package com.jianboke.mapper;

import com.jianboke.domain.CommentLike;
import com.jianboke.model.CommentLikeModel;
import org.mapstruct.Mapper;

/**
 * Created by pengxg on 2017/8/24.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommentLikeMapper {

    CommentLikeModel entityToModel(CommentLike entity);

    CommentLike modelToEntity(CommentLikeModel model);
}
