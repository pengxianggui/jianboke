package com.jianboke.mapper;

import com.jianboke.domain.Article;
import com.jianboke.domain.Book;
import com.jianboke.model.ArticleModel;
import com.jianboke.model.BookModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengxg on 2017/4/23.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class ArticleMapper {

    @Autowired
    private BookMapper bookMapper;

    @Mappings(
            @Mapping(source = "article.books", target = "books")
    )
    public abstract ArticleModel entityToModel(Article article);

    @Mappings(
            @Mapping(source = "model.books", target = "books")
    )
    public abstract Article modelToEntity(ArticleModel model);

    public List<BookModel> bookListToBookModelList(List<Book> bookList) {
        List<BookModel> bookModelList = new ArrayList<>();
        for (Book book : bookList) {
            bookModelList.add(bookMapper.entityToModel(book));
        }
        return bookModelList;
    }

    public List<Book> bookModelListToBookList(List<BookModel> bookModelList) {
        List<Book> bookList = new ArrayList<>();
        for (BookModel bookModel : bookModelList) {
            bookList.add(bookMapper.modelToEntity(bookModel));
        }
        return bookList;
    }
}
