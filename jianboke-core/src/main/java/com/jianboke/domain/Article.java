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
@Table(name = "articles")
public class Article extends AbstractAuditingEntity {
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "content", nullable = false)
	private String content;
	
	@Column(name = "labels", nullable = true)
	@ColumnComment("自定义类别标签，用逗号分隔的字符串")
	private String labels;
	
	@Column(name = "author_id", nullable = false)
	@ColumnComment("文章所属的作者id")
	private Long authorId;
	
//	@Column(name = "book_id", nullable = false)
//	@ColumnComment("文章所属的book的id")
//	private Long bookId;

	@Column(name="second_author_id", nullable = true)
	private Long secondAuthorId;
	
	public Long getSecondAuthorId() {
		return secondAuthorId;
	}

	public void setSecondAuthorId(Long secondAuthorId) {
		this.secondAuthorId = secondAuthorId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

//	public Long getBookId() {
//		return bookId;
//	}
//
//	public void setBookId(Long bookId) {
//		this.bookId = bookId;
//	}

	@Override
	public String toString() {
//		return "Article [id=" + id + ", title=" + title + ", content=" + content + ", labels=" + labels + ", authorId="
//				+ authorId + ", bookId=" + bookId + "]";
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	
}
