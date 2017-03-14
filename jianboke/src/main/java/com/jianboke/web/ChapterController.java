package com.jianboke.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jianboke.model.BookChapterArticleModel;
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
	private TreeOfChapterService treeOfChapterService;
	
	@RequestMapping(value = "/chapter/listChapterTreeWithoutArticle/{bookId}", method = RequestMethod.GET)
	public BookChapterArticleModel listChapterTreeWithoutArticle(@PathVariable Long bookId) {
		log.info("REST Request to get A tree of Chapters that's bookId is: {}", bookId);
		List<BookChapterArticleModel> chapterList = treeOfChapterService.getTreeWithoutArticle(bookId);
		return chapterList.get(0);
	}

	/**
	 * 创建一个章节节点
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/chapter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookChapterArticleModel> create(@Valid @RequestBody BookChapterArticleModel model) {
		log.info("Save a chapter, chapter's name is:{}, it's bookId is:{}", model.getGroupName(), model.getBookId());
		// TODO 保存chapter，并以model形式返回该chapter

		return null;
	}
}
