package com.jianboke.service;

import java.util.ArrayList;
import java.util.List;

import com.jianboke.model.ChapterModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jianboke.domain.Chapter;
import com.jianboke.repository.ChapterRepository;
import com.jianboke.utils.ChapterModelComparator;

/**
 * 生成章节树形数据结构的数据，返回前端。
 * @author pengxg
 *
 */
@Service("TreeOfChapterService")
public class TreeOfChapterService {

	private static final Logger log = LoggerFactory.getLogger(TreeOfChapterService.class);
	
	@Autowired
	private ChapterRepository chapterRepository;

	@Autowired
	private ChapterService chapterService;
	
	private ChapterModelComparator chapterModelComparator = new ChapterModelComparator();

	/**
	 * 获取一本书的章节层级结构信息。返回数据为树形结构。用BookChapterArticleModel封装
	 * @param bookId : 传入的书本id
	 * @return ChapterModel : 包含层级结构模型
	 */
	public List<ChapterModel> getTreeWithoutArticle(Long bookId) {
		List<Chapter> chapterList = chapterRepository.findAllByBookId(bookId);
		List<ChapterModel> chapterModelList = changeToModelList(chapterList);
		
		List<ChapterModel> rootNodes = new ArrayList<ChapterModel>(); // 存放当前chapterList中的根节点
		List<ChapterModel> notRootNodes = new ArrayList<ChapterModel>(); //存放非根节点
		
		if (chapterModelList.size() > 0 && chapterModelList != null) { // 存在且有元素
			for (ChapterModel chapterModel : chapterModelList) { // 拆分 根节点和非根节点
				if (chapterModel == null) continue;
				if (!chapterModel.getIsArticle() && 
						(chapterModel.getParentId() == null || chapterModel.getParentId().toString().equals(""))) {
					// 为根节点
					rootNodes.add(chapterModel);
				} else {
					notRootNodes.add(chapterModel);
				}
			}
		}
		// 递归获取子节点
		if (rootNodes.size() > 0) {
			for (ChapterModel chapterModel : rootNodes) {
				chapterModel.setLevel(0);
				chapterModel.setItems(getChildChapter(notRootNodes, chapterModel.getId(), 0));
			}
		}
		return rootNodes;
	}
	
	/**
	 * 迭代方法。为每一个chapter节点设置子节点
	 * @param childList
	 * @param id
	 * @param level 层级结构标示
	 * @return
	 */
	private List<ChapterModel> getChildChapter(List<ChapterModel> childList, Long id, int level) {
		List<ChapterModel> parentNodes = new ArrayList<ChapterModel>();
		List<ChapterModel> childNodes = new ArrayList<ChapterModel>();
		if (childList != null && childList.size() > 0) { //找出当前的根节点和非根节点
			for (ChapterModel chapterModel : childList) {
				// 找出当前childList中的根节点
				if (chapterModel.getParentId().toString().equals(id.toString())) {
					parentNodes.add(chapterModel);
				} else {
					childNodes.add(chapterModel);
				}
			}
		}
		// 给parentNodes赋予子节点
		if (parentNodes.size() > 0) {
			parentNodes.sort(chapterModelComparator);
			int levelTemp = ++level;
			for (ChapterModel chapterModel : parentNodes) {
				List<ChapterModel> nodes;
				chapterModel.setLevel(levelTemp);
				nodes = getChildChapter(childNodes, chapterModel.getId(), levelTemp); //递归
				chapterModel.setItems(nodes);
			}
		}
		return parentNodes;
	}


	/**
	 * 将chapter列表转为BookChapterArticleModel列表，并返回
	 * @param chapterList
	 * @return
	 */
	private List<ChapterModel> changeToModelList(List<Chapter> chapterList) {
		List<ChapterModel> chapterModelList = new ArrayList<ChapterModel>();
		for (Chapter chapter : chapterList) {
			ChapterModel chapterModel = new ChapterModel();
			chapterModel.setId(chapter.getId());
			chapterModel.setParentId(chapter.getParentId());
			chapterModel.setBookId(chapter.getBookId());
			chapterModel.setGroupName(chapter.getName());
			chapterModel.setSortNum(chapter.getSortNum());
			chapterModel.setExpanded(false);
			chapterModel.setIsArticle(false);
			chapterModel.setIfCanClick(true);
			chapterModel.setParentName(chapterService.getNameById(chapter.getParentId()));
			chapterModelList.add(chapterModel);
		}
		return chapterModelList;
	}
}
