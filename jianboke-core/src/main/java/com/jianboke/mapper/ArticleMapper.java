package com.jianboke.mapper;

import com.jianboke.domain.Article;
import com.jianboke.model.ArticleModel;
import org.mapstruct.Mapper;

/**
 * Created by pengxg on 2017/4/23.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArticleMapper {

    ArticleModel entityToModel(Article article);

    Article modelToEntity(ArticleModel model);
}
