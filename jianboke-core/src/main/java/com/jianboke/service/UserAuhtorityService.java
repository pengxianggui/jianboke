package com.jianboke.service;

import com.jianboke.domain.Article;
import com.jianboke.domain.Book;
import com.jianboke.domain.User;
import com.jianboke.enumeration.ResourceName;
import com.jianboke.repository.ArticleRepository;
import com.jianboke.repository.BookRepository;
import com.jianboke.repository.ChapterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 资源权限校验,用于判断当前用户是否具有某些资源的权限
 * Created by pengxg on 2017/4/25.
 */
@Service
public class UserAuhtorityService {


    private final Logger log = LoggerFactory.getLogger(UserAuhtorityService.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private UserService userService;

    /**
     * 传入资源名，如："ARTICLE", 资源id, 如: 1，判断当前登录的用户是否具有id为1的文章表的权限(权限依据为是否属于该用户)
     * @param resourceName
     * @param resourceId
     * @return
     */
    public boolean ifHasAuthority(ResourceName resourceName, Long resourceId) {
        switch (resourceName.name()) {
            case "ARTICLE": {
                return ifHasArticleAuthority(resourceId);
            }
            case "BOOK": {
                return ifHasBookAuthority(resourceId);
            }
            case "CHAPTER": {
                return ifHasChapterAuthority(resourceId);
            }
            default: {
                return false;
            }
        }
    }

    private boolean ifHasChapterAuthority(Long resourceId) {
        User currentUser = userService.getUserWithAuthorities();
        chapterRepository.findOne(resourceId);
        return false;
    }

    private boolean ifHasBookAuthority(Long resourceId) {
        User currentUser = userService.getUserWithAuthorities();
        Book book = bookRepository.findOne(resourceId);
        if (book == null) return true;
        String authorId = "", secondAuthorId = "";
        if (book.getAuthorId() != null) authorId = book.getAuthorId().toString();
        if (book.getSecondAuthorId() != null) secondAuthorId = book.getSecondAuthorId().toString();
        if (authorId.equals(currentUser.getId().toString())
                || secondAuthorId.equals(currentUser.getId().toString())) {
            return true;
        }
        return false;
    }

    private boolean ifHasArticleAuthority(Long resourceId) {
        User currentUser = userService.getUserWithAuthorities();
        log.info("currentUser:{}", currentUser);
        Article article = articleRepository.findOne(resourceId);
        if (article == null) return true;
        String authorId = "", secondAuthorId = "";
        if (article.getAuthorId() != null) authorId = article.getAuthorId().toString();
        if (article.getSecondAuthorId() != null) secondAuthorId = article.getSecondAuthorId().toString();
        log.info("secondAuthorId:{}", secondAuthorId);
        if (currentUser != null && (authorId.equals(currentUser.getId().toString()) || secondAuthorId.equals(currentUser.getId().toString()))) {
            return true;
        }
        return false;
    }
}
