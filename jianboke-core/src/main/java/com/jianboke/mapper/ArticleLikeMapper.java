package com.jianboke.mapper;

import com.jianboke.domain.ArticleLike;
import com.jianboke.model.ArticleLikeModel;
import org.mapstruct.Mapper;

/**
 * Created by pengxg on 2017/8/24.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArticleLikeMapper {

    ArticleLikeModel entityToModel(ArticleLike entity);

    ArticleLike modelToEntity(ArticleLikeModel model);
}
