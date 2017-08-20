package com.jianboke.mapper;

import com.jianboke.domain.Article;
import com.jianboke.domain.Book;
import com.jianboke.domain.Comment;
import com.jianboke.model.ArticleModel;
import com.jianboke.model.BookModel;
import com.jianboke.model.CommentModel;
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

    @Autowired
    private CommentMapper commentMapper;

    @Mappings({
            @Mapping(source = "article.books", target = "books")
//            @Mapping(source = "article.comments", target = "comments")
    })
    public abstract ArticleModel entityToModel(Article article);

    @Mappings({
            @Mapping(source = "model.books", target = "books")
//            @Mapping(source = "model.comments", target = "comments")
    })
    public abstract Article modelToEntity(ArticleModel model);

//    public List<BookModel> bookListToBookModelList(List<Book> bookList) {
//        List<BookModel> bookModelList = new ArrayList<>();
//        for (Book book : bookList) {
//            bookModelList.add(bookMapper.entityToModel(book));
//        }
//        return bookModelList;
//    }
//
//    public List<Book> bookModelListToBookList(List<BookModel> bookModelList) {
//        List<Book> bookList = new ArrayList<>();
//        for (BookModel bookModel : bookModelList) {
//            bookList.add(bookMapper.modelToEntity(bookModel));
//        }
//        return bookList;
//    }
//
//    public List<CommentModel> commentListToCommentModelList(List<Comment> commentList) {
//        List<CommentModel> commentModelList = new ArrayList<>();
//        for (Comment comment : commentList) {
//            commentModelList.add(commentMapper.entityToModel(comment));
//        }
//        return commentModelList;
//    }
//
//    public List<Comment> commentModelListToCommentList(List<CommentModel> commentModelList) {
//        List<Comment> commentList = new ArrayList<>();
//        for (CommentModel commentModel : commentModelList) {
//            commentList.add(commentMapper.modelToEntity(commentModel));
//        }
//        return commentList;
//    }
}
