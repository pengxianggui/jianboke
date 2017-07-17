package com.jianboke.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jianboke.mapper.ChapterMapper;
import com.jianboke.model.ChapterModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jianboke.domain.Chapter;
import com.jianboke.repository.ChapterRepository;
import com.jianboke.comparator.ChapterModelComparator;

/**
 * 生成章节树形数据结构的数据，返回前端。
 * @author pengxg
 *
 */
@Service("TreeOfChapterService")
public class TreeOfChapterService {

	private static final Logger log = LoggerFactory.getLogger(TreeOfChapterService.class);
	private Long expandedChapterId; //标识要定位的文章的上级id(迭代赋值)

	@Autowired
	private ChapterRepository chapterRepository;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ChapterMapper chapterMapper;

	private ChapterModelComparator chapterModelComparator = new ChapterModelComparator();

	/**
	 * 获取一本书的章节层级结构信息。返回数据为树形结构。用BookChapterArticleModel封装
	 * @param bookId : 传入的书本id
	 * @param fixChapter : 需要定为的叶子章节
	 * @return ChapterModel : 包含层级结构模型
	 */
	public List<ChapterModel> getTreeWithoutArticle(Long bookId, Chapter fixChapter) {
		if (fixChapter != null && fixChapter.getId() != null && !fixChapter.getId().toString().equals("")) {
			expandedChapterId = fixChapter.getId();
		}
		List<Chapter> chapterList = chapterRepository.findAllByBookId(bookId);
		List<ChapterModel> chapterModelList = new ArrayList<>();
		chapterList.forEach(chapter -> chapterModelList.add(chapterMapper.entityToModel(chapter)));
		
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
				chapterModel.setIsArticle(false);
				chapterModel.setItems(getChildChapter(notRootNodes, chapterModel.getId(), 0, -1l));
				chapterModel.setExpanded(true);
			}
		}
		return rootNodes;
	}

	/**
	 *
	 * @param bookId
	 * @return
	 */
	public List<ChapterModel> getTreeWithArticle(Long bookId, Long tableId) {
		List<Chapter> chapterList = chapterRepository.findAllByBookId(bookId); // 获取所有组
		List<ChapterModel> chapterModelList = new ArrayList<>();
		// 转换为Model
		chapterList.forEach(chapter -> {
			chapterModelList.add(chapterMapper.entityToModel(chapter));
		});
		List listArticle = articleService.findArticleModelByBookId(bookId); // 获取所有表
		List<ChapterModel> articleChapterModelList = changeToModelList(listArticle);
		chapterModelList.addAll(articleChapterModelList); // 将表模型也放到组模型去
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
				chapterModel.setIsArticle(false);
				chapterModel.setExpanded(true);
				chapterModel.setItems(getChildChapter(notRootNodes, chapterModel.getId(), 0, tableId));
				expandedChapterId = null; // 复位
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
	private List<ChapterModel> getChildChapter(List<ChapterModel> childList, Long id, int level, Long articleId) {
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
				if (chapterModel.getIsArticle()) { // 如果是表
					nodes = null;
					chapterModel.setExpanded(false);
					if (articleId.toString().equals(chapterModel.getId().toString())) {
						expandedChapterId = chapterModel.getParentId();
					}
				} else {
					nodes = getChildChapter(childNodes, chapterModel.getId(), levelTemp, articleId); //递归
					if (expandedChapterId != null
							&& chapterModel.getId().toString().equals(expandedChapterId.toString())) {
						chapterModel.setExpanded(true);
						expandedChapterId = chapterModel.getParentId(); // 继续定位上一级展开的目录
					} else {
						chapterModel.setExpanded(false);
					}
					chapterModel.setIsArticle(false);
				}
				chapterModel.setItems(nodes);
			}
		}
		return parentNodes;
	}


	/**
	 * 将article列表转为BookChapterArticleModel(ChapterModel)列表，并返回
	 * @param articleList
	 * @return
	 */
	private List<ChapterModel> changeToModelList(List articleList) {
		List<ChapterModel> chapterModelList = new ArrayList<ChapterModel>();
		Iterator iterator = articleList.iterator();
		ChapterModel chapterModel;
		while (iterator.hasNext()) {
			chapterModel = new ChapterModel();
			Object temp = iterator.next();
			Object[] objT = (Object[]) temp;
			chapterModel.setId(Long.valueOf(objT[0].toString()));
			chapterModel.setGroupName(objT[1].toString());
			chapterModel.setParentId(Long.valueOf(objT[2].toString()));
			chapterModel.setParentName(objT[3].toString());
			chapterModel.setSortNum(objT[4] == null ? 0 : Integer.valueOf(objT[4].toString()));
			chapterModel.setBookId(Long.valueOf(objT[5].toString()));
			chapterModel.setExpanded(false);
			chapterModel.setIsArticle(true);
			chapterModel.setItems(null);
			chapterModelList.add(chapterModel);
		}
		return chapterModelList;
	}
}
