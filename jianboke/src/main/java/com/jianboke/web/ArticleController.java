package com.jianboke.web;

import java.sql.SQLException;
import java.util.*;

import javax.validation.Valid;

import com.jianboke.domain.criteria.ArticleCriteria;
import com.jianboke.mapper.ArticleMapper;
import com.jianboke.model.ArticleModel;
import com.jianboke.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	@Autowired
	private ArticleMapper articleMapper;

	@RequestMapping(value = "/article", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Map<String, Object> query(@ModelAttribute ArticleCriteria criteria) {
		log.info("Rest request to get Articles by criteria :{}", criteria);
		Map<String, Object> map = new HashMap<>();
		try {
			List<ArticleModel> modelList = new ArrayList<>();
			articleService.queryByCriteria(criteria).forEach(article -> {
				modelList.add(articleMapper.entityToModel(article));
			});
			map.put("result", "success");
			map.put("data", modelList);
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
	public ResponseEntity<List<ArticleModel>> queryByFilter(@PathVariable String filter) {
		 List<ArticleModel> articleModelList = new ArrayList<>();
		 User user = userService.getUserWithAuthorities(); // 获取当前用户
		 if (filter.endsWith("-1") || filter == null) {
			 log.debug("User : {} REST request to query all his articles", user.getUsername());
			 articleRepository.findAllByAuthorId(user.getId()).forEach(article -> {
				 articleModelList.add(articleMapper.entityToModel(article));
			 });
		 } else {
			 log.debug("User : {} REST request to query articles by filter : {}", user.getUsername(), filter);
			 articleRepository.queryByFilterAndAuthorId(filter, user.getId()).forEach(article -> {
				 articleModelList.add(articleMapper.entityToModel(article));
			 });
		 }
		 return ResponseEntity.ok().body(articleModelList);
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
	public ResponseEntity<ArticleModel> get(@PathVariable Long id) {
		log.debug("REST request to get a Article by id:{}", id);
		// TODO 权限校验
		return Optional.ofNullable(articleRepository.findOne(id)).map(articleMapper::entityToModel)
				.map(model -> new ResponseEntity<>(model, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "/article/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Article remove(@PathVariable Long id) {
		log.info("request to delete a article which id is :{}", id);
		// TODO 权限校验
		Article article = articleRepository.findOne(id);
		articleRepository.delete(article);
		return article;
	}
}
