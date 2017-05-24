package com.jianboke.mapper;

import com.jianboke.domain.Chapter;
import com.jianboke.model.ChapterModel;
import com.jianboke.service.ChapterService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pengxg on 2017/5/20.
 */

@Mapper(componentModel = "spring", uses = {})
public abstract class ChapterMapper {

    @Autowired
    private ChapterService chapterService;

    @Mappings({
            @Mapping(source = "chapter.name", target = "groupName"),
            @Mapping(source = "chapter.parentId", target = "parentName"),
    })
    public abstract ChapterModel entityToModel(Chapter chapter);

    public String changeToParentName(Long parentId) {
        if (parentId != null) {
            return chapterService.getNameById(parentId);
        }
        return null;
    }

    public abstract Chapter modelToEntity(ChapterModel model);
}
