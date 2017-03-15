package com.jianboke.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import com.jianboke.domain.Article;
import com.jianboke.domain.BookChapterArticle;
import com.jianboke.model.ChapterModel;
import com.jianboke.repository.BookChapterArticleRepository;
import com.jianboke.service.BookChapterArticleService;
import com.jianboke.service.ChapterService;
import com.jianboke.utils.HeaderUtil;
import org.json.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jianboke.repository.ChapterRepository;
import com.jianboke.service.TreeOfChapterService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ChapterController {

	private static final Logger log = LoggerFactory.getLogger(ChapterController.class);
	
	@Autowired
	private ChapterRepository chapterRepository;

	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private TreeOfChapterService treeOfChapterService;

	@Autowired
	private BookChapterArticleService bookChapterArticleService;
	
	@RequestMapping(value = "/chapter/listChapterTreeWithoutArticle/{bookId}", method = RequestMethod.GET)
	public ChapterModel listChapterTreeWithoutArticle(@PathVariable Long bookId) {
		log.info("REST Request to get A tree of Chapters that's bookId is: {}", bookId);
		List<ChapterModel> chapterList = treeOfChapterService.getTreeWithoutArticle(bookId);
		return chapterList.get(0);
	}

	/**
	 * 创建一个章节节点
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/chapter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ChapterModel> create(@Valid @RequestBody ChapterModel model) throws URISyntaxException {
		log.info("Save a chapter, chapter's name is:{}, it's bookId is:{}", model.getGroupName(), model.getBookId());
		// TODO 保存chapter，并以model形式返回该chapter
		if (model.getId() != null) {
			return update(model);
		}
		ChapterModel result = chapterService.saveChapterModel(model);
		return ResponseEntity.created(new URI("/api/productGroup/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("chapter", result.getId().toString())).body(result);
	}

	/**
	 * 更新一个章节节点，更新chapter
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/chapter", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<ChapterModel> update(@Valid @RequestBody ChapterModel model) throws URISyntaxException {
		log.info("Update a chapter, the name is :{}, bookId is :{}", model.getGroupName(), model.getBookId());
		if (model.getId() == null) {
			return create(model);
		}
		// TODO 更新一个chapter，并返回
		return Optional.ofNullable(chapterRepository.findOne(model.getId())).map(chapter -> {
			ChapterModel result = chapterService.saveChapterModel(model);
			return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("chapter", result.getId().toString()))
					.body(result);
		}).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * 删除一个章节。将删除该章节下所有的章节，如果该章节下存在归档的文章，则这些文章将全部被撤销归档。返回操作的成功与否信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/chapter/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean removeChapter(@PathVariable("id") Long id) {
		log.info("Request to remove a chapter, id is:{}, the sons of the chapter will be romove as well.", id);
		System.out.println("Request to remove a chapter, id is:" + id + ", the sons of the chapter will be romove as well.");
		boolean result = chapterService.removeChapterByIdStrong(id);
		return result;
	}

	/**
	 * 根据id查询该章节下(直接从属的)所有的BookChapterArticle对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/chapter/listArticlesById/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BookChapterArticle> getArticlesById(@PathVariable Long id) {
		log.info("Request to get direct articles of chapter which id is :{}", id);
		if (id == null) return null;
		return bookChapterArticleService.getAllByParentId(id);
	}
}
