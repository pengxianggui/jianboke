package com.jianboke.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BookChapterArticleModel {
	
	private Long id;
	
	private Long bookId;
	
	private Long parentId;
	
	private String groupName;
	
	private Integer sortNum;
	
	private String description;
	
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	private List<BookChapterArticleModel> items; // 树结构的体现
	
	private int level;
	
	private boolean expanded;

	private boolean isArticle;
	
	private boolean ifCanClick;

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getBookId() {
		return bookId;
	}


	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}


	public Long getParentId() {
		return parentId;
	}


	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	public Integer getSortNum() {
		return sortNum;
	}


	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}


	public List<BookChapterArticleModel> getItems() {
		return items;
	}


	public void setItems(List<BookChapterArticleModel> items) {
		this.items = items;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public boolean isExpanded() {
		return expanded;
	}


	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}


	public void setIsArticle(boolean isArticle) {
		this.isArticle = isArticle;
	}

	public boolean getIsArticle() {
		return this.isArticle;
	}

	public boolean getIfCanClick() {
		return ifCanClick;
	}


	public void setIfCanClick(boolean ifCanClick) {
		this.ifCanClick = ifCanClick;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
