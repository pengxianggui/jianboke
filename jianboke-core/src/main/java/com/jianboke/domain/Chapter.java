package com.jianboke.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.jianboke.annotation.ColumnComment;

@Entity
@Table(name = "chapters")
public class Chapter extends AbstractAuditingEntity {
//	@Id
//	@Column(name = "id", unique = true, nullable = false)
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
	
	@Column(name = "name", nullable = false)
	@ColumnComment("章节名")
	private String name;
	
	@Column(name = "book_id", nullable = false)
	private Long bookId;
	
	@Column(name = "parent_id")
	@ColumnComment("父章节的id，标示从属于哪个章节。如果为null，则表示改章节就是book(顶结点)")
	private Long parentId;
	
	@Column(name = "description", nullable = true)
	private String description;
	
	@Column(name = "sort_num")
	private Integer sortNum;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
