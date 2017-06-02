package com.jianboke.web;

import java.sql.SQLException;
import java.util.*;

import javax.validation.Valid;

import com.jianboke.domain.criteria.ArticleCriteria;
import com.jianboke.enumeration.HttpReturnCode;
import com.jianboke.enumeration.ResourceName;
import com.jianboke.mapper.ArticleMapper;
import com.jianboke.model.ArticleModel;
import com.jianboke.model.RequestResult;
import com.jianboke.service.ArticleService;
import com.jianboke.service.BookChapterArticleService;
import com.jianboke.service.UserAuhtorityService;
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

	@Autowired
	private UserAuhtorityService userAuhtorityService;

	@Autowired
	private BookChapterArticleService bookChapterArticleService;

	@RequestMapping(value = "/article", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Map<String, Object> query(@ModelAttribute ArticleCriteria criteria) {
		log.info("Rest request to get Articles by criteria :{}", criteria);
		Map<String, Object> map = new HashMap<>();
		try {
			List<ArticleModel> modelList = new ArrayList<>();
			articleService.queryByCriteria(criteria).forEach(article ->
					modelList.add(articleMapper.entityToModel(article)));
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

	/**
	 * 保存
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/article", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RequestResult> saveArticle(@Valid @RequestBody ArticleModel model) {
		if (model.getId() != null) { // 更新
			return updateArticle(model);
		}
		log.debug("REST request to save article : {}", model);
		model.setAuthorId(userService.getUserWithAuthorities().getId());
		Article a = articleRepository.save(articleMapper.modelToEntity(model));
		if (a != null) {
			return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, model));
		}
		return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_ERROR, "保存失败"));
	}

	/**
	 * 更新
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/article", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RequestResult> updateArticle(@Valid @RequestBody ArticleModel model) {
		log.info("REST request to update a article, the model is :{}", model);
		Article a = articleRepository.saveAndFlush(articleMapper.modelToEntity(model));
		if (a != null) {
			return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_SUCCESS, model));
		}
		return ResponseEntity.ok().body(RequestResult.create(HttpReturnCode.JBK_ERROR, "保存失败"));
	}

	@RequestMapping(value = "/article/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArticleModel> get(@PathVariable Long id) {
		log.debug("REST request to get a Article by id:{}", id);
		// TODO 权限校验
		if(userAuhtorityService.ifHasAuthority(ResourceName.ARTICLE, id)) {
			return Optional.ofNullable(articleRepository.findOne(id)).map(articleMapper::entityToModel)
					.map(model -> new ResponseEntity<>(model, HttpStatus.OK))
					.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
		return ResponseEntity.ok().body(null); // 不能把没有权限的资源返回
	}
	@javax.transaction.Transactional
	@RequestMapping(value = "/article/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Article remove(@PathVariable Long id) {
		log.info("request to delete a article which id is :{}", id);
		// TODO 权限校验
		if (userAuhtorityService.ifHasAuthority(ResourceName.ARTICLE, id)) {
			Article article = articleRepository.findOne(id);
			bookChapterArticleService.deleteByArticleId(id);
			articleRepository.delete(article);
			return article;
		} else {
			return null;
		}
	}

//	@RequestMapping(value = "/article/ifPublic", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<HttpReturnCode> ifPublicSave(@RequestParam("id") Long id,
//													   @RequestParam("ifPublic") boolean ifPublic) {
//		log.info("rest request to update ifPublic:{} of article:{}", ifPublic, id);
//		System.out.println(id);
//		System.out.println(ifPublic);
//		return null;
//	}
}
