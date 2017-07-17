package com.jianboke.service;

import com.jianboke.domain.Book;
import com.jianboke.domain.BookChapterArticle;
import com.jianboke.repository.BookChapterArticleRepository;
import com.jianboke.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengxg on 2017/5/21.
 */
@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookChapterArticleRepository bookChapterArticleRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ChapterService chapterService;

    /**
     * 根据articleId查找书本。一篇文章可能归类到多本书下
     * @param articleId
     * @return
     */
    public List<Book> getBooksByArticleId(Long articleId) {
        List<BookChapterArticle> bcaList = bookChapterArticleRepository.findAllByArticleId(articleId);
        List<Book> bookList = new ArrayList<>();
        bcaList.forEach(bca -> {
            Book b = bookRepository.findOne(bca.getBookId());
            if (b != null) {
                bookList.add(b);
            }
        });
        return bookList;
    }

    /**
     * 保存一本书,并初始化章节内容
     * @param book
     */
    @Transactional
    public Book saveBook(Book book) {
        Book b = bookRepository.saveAndFlush(book);
        chapterService.initChapterForBook(book);
        return b;
    }
}
