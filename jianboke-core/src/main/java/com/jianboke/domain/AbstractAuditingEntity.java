package com.jianboke.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jianboke.annotation.ColumnComment;

@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity {
	
	  @CreatedDate
	  @Column(name = "created_date", nullable = false, updatable = false)
	  @JsonIgnore
	  @ColumnComment("创建时间")
	  private LocalDateTime createdDate = LocalDateTime.now();
	  
	  @LastModifiedDate
	  @Column(name = "last_modified_date")
	  @JsonIgnore
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
