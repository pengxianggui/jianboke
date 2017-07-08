package com.jianboke.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.jianboke.annotation.ColumnComment;

import java.util.Collection;

@Entity
@Table(name = "articles")
public class Article extends AbstractAuditingEntity {
//	@Id
//	@Column(name = "id", unique = true, nullable = false)
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
	
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

	@Column(name="second_author_id")
	private Long secondAuthorId;

	@Column(name = "if_original")
	@ColumnComment("是否原创（若否，表明是转载的）")
	private boolean ifOriginal = true; //默认是原创

	@Column(name = "original_author_name")
	@ColumnComment("原创作者名")
	private String originalAuthorName;

	@Column(name = "if_public")
	@ColumnComment("是否公开（是否允许其他用户搜索到）")
	private boolean ifPublic = true;

	@Column(name = "if_allow_reprint")
	@ColumnComment("是否允许拷贝（即：转载，转载意味着拷贝了一个副本）")
	private boolean ifAllowReprint = true;

	@Column(name = "if_allow_comment")
	@ColumnComment("是否允许评论")
	private boolean ifAllowComment = true;

	@Column(name = "if_allow_second_author")
	@ColumnComment("是否允许接收第二作者申请：设置为false，其他用户无法发出协同创作请求")
	private boolean ifAllowSecondAuthor = true;

//	@Column(name = "if_accept_second_author")
//	@ColumnComment("是否接受第二作者申请：设置为false，相当于拒绝其他用户协同创作的请求")
//	private boolean ifAcceptSecondAuthor;

	@Column(name = "if_set_top")
	@ColumnComment("是否在个人主页置顶")
	private boolean ifSetTop = false; //默认false

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "book_chapter_articles",
			joinColumns = { @JoinColumn(name = "article_id", referencedColumnName = "id")},
			inverseJoinColumns = { @JoinColumn(name = "book_id", referencedColumnName = "id")}
	)
	private Collection<Book> books;
	
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

	public boolean isIfOriginal() {
		return ifOriginal;
	}

	public void setIfOriginal(boolean ifOriginal) {
		this.ifOriginal = ifOriginal;
	}

	public String getOriginalAuthorName() {
		return originalAuthorName;
	}

	public void setOriginalAuthorName(String originalAuthorName) {
		this.originalAuthorName = originalAuthorName;
	}

	public boolean isIfPublic() {
		return ifPublic;
	}

	public void setIfPublic(boolean ifPublic) {
		this.ifPublic = ifPublic;
	}

	public boolean isIfAllowReprint() {
		return ifAllowReprint;
	}

	public void setIfAllowReprint(boolean ifAllowReprint) {
		this.ifAllowReprint = ifAllowReprint;
	}

	public boolean isIfAllowComment() {
		return ifAllowComment;
	}

	public void setIfAllowComment(boolean ifAllowComment) {
		this.ifAllowComment = ifAllowComment;
	}

	public boolean isIfAllowSecondAuthor() {
		return ifAllowSecondAuthor;
	}

	public void setIfAllowSecondAuthor(boolean ifAllowSecondAuthor) {
		this.ifAllowSecondAuthor = ifAllowSecondAuthor;
	}

	public boolean isIfSetTop() {
		return ifSetTop;
	}

	public void setIfSetTop(boolean ifSetTop) {
		this.ifSetTop = ifSetTop;
	}

	public Collection<Book> getBooks() {
		return books;
	}

	public void setBooks(Collection<Book> books) {
		this.books = books;
	}

	@Override
	public String toString() {
//		return "Article [id=" + id + ", title=" + title + ", content=" + content + ", labels=" + labels + ", authorId="
//				+ authorId + ", bookId=" + bookId + "]";
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	
}
