package com.jianbo.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jianboke.model.BookChapterArticleModel;
import com.jianboke.repository.ChapterRepository;
import com.jianboke.service.TreeOfChapterService;

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
		log.info("REST Request to get A tree of Chapters that's bookId is: {}" + bookId);
		List<BookChapterArticleModel> chapterList = treeOfChapterService.getTreeWithoutArticle(bookId);
		return chapterList.get(0);
	}
}
