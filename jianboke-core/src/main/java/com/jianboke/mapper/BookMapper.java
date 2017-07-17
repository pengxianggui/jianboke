package com.jianboke.mapper;

import com.jianboke.domain.Book;
import com.jianboke.model.BookModel;
import org.mapstruct.Mapper;

/**
 * Created by pengxg on 2017/7/16.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BookMapper {

    BookModel entityToModel(Book book);

    Book modelToEntity(BookModel model);
}
