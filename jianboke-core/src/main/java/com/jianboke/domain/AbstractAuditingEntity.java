package com.jianboke.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jianboke.annotation.ColumnComment;

@SuppressWarnings("serial")
@JsonInclude(Include.NON_NULL)
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity implements Serializable{
	
	  @CreatedDate
	  @Column(name = "created_date", nullable = false, updatable = false)
//	  @JsonIgnore
	  @ColumnComment("创建时间")
	  private LocalDateTime createdDate = LocalDateTime.now();
	  
	  @LastModifiedDate
	  @Column(name = "last_modified_date")
//	  @JsonIgnore
	  @ColumnComment("最后修改时间")
	  private LocalDateTime lastModifiedDate = LocalDateTime.now();

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	  
	  
}
