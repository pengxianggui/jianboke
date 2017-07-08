package com.jianboke.service;

import com.jianboke.domain.AccountDefaultSetting;
import com.jianboke.domain.Article;
import com.jianboke.domain.BookChapterArticle;
import com.jianboke.domain.criteria.ArticleCriteriaJDBC;
import com.jianboke.model.ArticleModel;
import com.jianboke.repository.AccountDefaultSettingRepository;
import com.jianboke.repository.ArticleRepository;
import com.jianboke.repository.BookChapterArticleRepository;
import com.jianboke.security.SecurityUtils;
import com.jianboke.utils.DBConfigUtils;
import com.jianboke.utils.DateTimeUtils;
import org.hibernate.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * jdbc查询
 * Created by pengxg on 2017/3/25.
 */
@Component("ArticleService")
public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BookChapterArticleService bookChapterArticleService;

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountDefaultSettingRepository accountDefaultSettingRepository;

    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    public List<Article> queryByCriteria(ArticleCriteriaJDBC criteria) throws SQLException {
        List<Article> resultList;
        String findBy = criteria.getFindBy(),
               filter = criteria.getFilter();
        Integer page = criteria.getPage(),
                size = criteria.getSize(),
                begin = (page - 1) * size;
        DataSource ds = jdbcTemplate.getDataSource();
        Long authorId = userService.getUserWithAuthorities().getId();
        boolean isGroupByBook = false;
        try {
            conn = ds.getConnection();
            log.debug("Connection open");
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT DISTINCT a.* FROM articles a ");
            if (findBy.equalsIgnoreCase("all")) {
                // 无需根据bookId查询
                sb.append("WHERE ");
            } else if (findBy.equalsIgnoreCase("alone")) {
                // 查询未归类的博客
                sb.append("WHERE ");
                sb.append("a.id NOT IN (SELECT DISTINCT article_id FROM book_chapter_articles) AND ");
            } else { // findBy的值是bookId
                isGroupByBook = true;
                sb.append(", book_chapter_articles bca WHERE ");
                sb.append("(bca.article_id = a.id) AND ");
                sb.append("bca.book_id = ? AND ");
            }
            sb.append("(a.author_id = ? OR a.second_author_id = ?) AND");
            sb.append("(a.title LIKE ? OR a.labels LIKE ?) ");
            sb.append("ORDER BY a.last_modified_date desc ");
            sb.append("LIMIT ?, ?");
            log.info("sql is :{}" , sb.toString());
            stmt = conn.prepareStatement(sb.toString());
            if (isGroupByBook) {
                stmt.setLong(1, Long.valueOf(findBy));
                stmt.setLong(2, authorId);
                stmt.setLong(3, authorId);
                stmt.setString(4, "%" + filter + "%");
                stmt.setString(5, "%" + filter + "%");
                stmt.setInt(6, begin);
                stmt.setInt(7, size);
            } else {
                stmt.setLong(1, authorId);
                stmt.setLong(2, authorId);
                stmt.setString(3, "%" + filter + "%");
                stmt.setString(4, "%" + filter + "%");
                stmt.setInt(5, begin);
                stmt.setInt(6, size);
            }
            rs = stmt.executeQuery();
            resultList = getResultFromRs(rs);
            return resultList;
        } catch (SQLException e) {
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
            throw e;
        } finally {
            log.debug("closeConn");
            DBConfigUtils.closeConn(conn, stmt, rs);
        }
    }

    private List<Article> getResultFromRs(ResultSet rs) throws SQLException {
        List<Article> temp = new ArrayList<>();
        while (rs.next()) {
            Article a = new Article();
            a.setId(rs.getLong("id"));
            a.setTitle(rs.getString("title"));
            a.setContent(rs.getString("content"));
            a.setLabels(rs.getString("labels"));
            a.setAuthorId(rs.getLong("author_id"));
            a.setSecondAuthorId(rs.getLong("second_author_id"));
            a.setIfOriginal(rs.getBoolean("if_original"));
            a.setOriginalAuthorName(rs.getString("original_author_name"));
            a.setIfPublic(rs.getBoolean("if_public"));
            a.setIfAllowReprint(rs.getBoolean("if_allow_reprint"));
            a.setIfAllowComment(rs.getBoolean("if_allow_comment"));
            a.setIfAllowSecondAuthor(rs.getBoolean("if_allow_second_author"));
            a.setIfSetTop(rs.getBoolean("if_set_top"));
            a.setCreatedDate(DateTimeUtils.toLdt(rs.getDate("created_date")));
            a.setLastModifiedDate(DateTimeUtils.toLdt(rs.getDate("last_modified_date")));
            temp.add(a);
        }
        return temp;
    }

    /**
     * 查找一本书下所有的article, 在该书中不同章节下重复归档的文章也会被重复返回
     * @param bookId
     * @return
     */
    public List<Article> findAllByBookId(Long bookId) {
        List<BookChapterArticle> bcaList = bookChapterArticleService.getAllByBookId(bookId);
        List<Article> returnArticleList = new ArrayList<>();
        bcaList.forEach(bca -> {
            Article article = articleRepository.findOne(bca.getArticleId());
            if (article != null) {
                returnArticleList.add(article);
            }
        });
        return returnArticleList;
    }

    /**
     * 根据bookId查找书下所有的Article对应的模型
     * @param bookId
     * @return
     */
    public List findArticleModelByBookId(Long bookId) {
        return articleRepository.findArticleModelByBookId(bookId);
    }

    /**
     * 为刚保存的article，设置用户自定义默认的权限。若用户没有自定义的用户设置，则采用系统默认的设置
     * @param model
     * @param userId
     */
    public ArticleModel setDefaultAuthority(ArticleModel model, Long userId) {
        return accountDefaultSettingRepository.findOneByUserId(userId).map(ads -> {
            model.setIfPublic(ads.isDefaultIfPublic());
            model.setIfAllowComment(ads.isDefaultIfAllowComment());
            model.setIfAllowReprint(ads.isDefaultIfAllowReprint());
            model.setIfAllowSecondAuthor(ads.isDefaultIfAllowSecondAuthor());
            return model;
        }).orElseGet(() -> { // 该用户不存在默认值。则采用系统默认值
            AccountDefaultSetting ads = AccountDefaultSetting.create(userId);
            model.setIfPublic(ads.isDefaultIfPublic());
            model.setIfAllowComment(ads.isDefaultIfAllowComment());
            model.setIfAllowReprint(ads.isDefaultIfAllowReprint());
            model.setIfAllowSecondAuthor(ads.isDefaultIfAllowSecondAuthor());
            accountDefaultSettingRepository.saveAndFlush(ads); // 并保存，以系统默认的文章权限设置作为用户的默认文章权限设置
            return model;
        });

    }
}
