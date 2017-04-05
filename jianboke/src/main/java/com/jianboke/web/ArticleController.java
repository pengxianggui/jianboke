package com.jianboke.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.jianboke.domain.criteria.ArticleCriteria;
import com.jianboke.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.jianboke.domain.Article;
import com.jianboke.domain.User;
import com.jianboke.repository.ArticleRepository;
import com.jianboke.service.UserService;

@RestController
@RequestMapping("/api")
public class ArticleController {
	private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ArticleService articleService;

	@RequestMapping(value = "/article", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Map<String, Object> query(@ModelAttribute ArticleCriteria criteria) {
		log.info("Rest request to get Articles by criteria :{}", criteria);
		Map<String, Object> map = new HashMap<>();
		try {
			List<Article> articlesList = articleService.queryByCriteria(criteria);
			map.put("result", "success");
			map.put("data", articlesList);
		} catch (SQLException e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			map.put("result", "fail");
			map.put("data", null);
		}
		return map;
	}
	
	/**
	 * 根据关键字过滤.关键字为-1时，搜索所有的Article
	 * @param filter
	 * @return
	 */
	@RequestMapping(value = "/articles/{filter}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Article> queryByFilter(@PathVariable String filter) {
		 List<Article> articleList = new ArrayList<Article>();
		 User user = userService.getUserWithAuthorities(); // 获取当前用户
		 if (filter.endsWith("-1") || filter == null) {
			 log.debug("User : {} REST request to query all his articles", user.getUsername());
			 articleList = articleRepository.findAllByAuthorId(user.getId());
		 } else {
			 log.debug("User : {} REST request to query articles by filter : {}", user.getUsername(), filter);
			 articleList = articleRepository.queryByFilterAndAuthorId(filter, user.getId());
		 }
		 return articleList;
	}
	
	@RequestMapping(value = "/article", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Article saveArticle(@Valid @RequestBody Article article) {
		log.debug("REST request to save article : {}", article);
		article.setAuthorId(userService.getUserWithAuthorities().getId());
		Article a = articleRepository.save(article);
		System.out.println(a.toString());
		return a;
	}

	@RequestMapping(value = "/article/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Article get(@PathVariable Long id) {
		log.debug("REST request to get a Article by id:{}", id);
		return articleRepository.findOne(id);
	}

	@RequestMapping(value = "/article/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Article remove(@PathVariable Long id) {
		log.info("request to delete a article which id is :{}", id);
		Article article = articleRepository.findOne(id);
		articleRepository.delete(article);
		return article;
	}
}
