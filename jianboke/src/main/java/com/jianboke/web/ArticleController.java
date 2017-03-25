package com.jianboke.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	public void remove(@PathVariable Long id) {
		log.info("request to delete a article which id is :{}", id);
		articleRepository.delete(id);
	}
}
