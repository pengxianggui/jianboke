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
@Table(name = "books")
public class Book extends AbstractAuditingEntity {
//	@Id
//	@Column(name = "id", unique = true, nullable = false)
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
	
	@Column(name = "book_name", nullable = false)
	private String bookName;
	
	@Column(name = "book_intro")
	@ColumnComment("书籍简介")
	private String bookIntro;
	
	@Column(name = "author_id", nullable = false)
	@ColumnComment("作者id")
	private Long authorId;
	
	@Column(name = "book_cover_path")
	@ColumnComment("书籍的封面图片路径")
	private String bookCoverPath;

	@Column(name="second_author_id", nullable = true)
	private Long secondAuthorId;

	public Long getSecondAuthorId() {
		return secondAuthorId;
	}

	public void setSecondAuthorId(Long secondAuthorId) {
		this.secondAuthorId = secondAuthorId;
	}

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookIntro() {
		return bookIntro;
	}

	public void setBookIntro(String bookIntro) {
		this.bookIntro = bookIntro;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getBookCoverPath() {
		return bookCoverPath;
	}

	public void setBookCoverPath(String bookCoverPath) {
		this.bookCoverPath = bookCoverPath;
	}

	@Override
	public String toString() {
//		return "Book [id=" + id + ", bookName=" + bookName + ", bookIntro=" + bookIntro + ", authorId=" + authorId
//				+ ", bookCoverPath=" + bookCoverPath + "]";
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	
}
