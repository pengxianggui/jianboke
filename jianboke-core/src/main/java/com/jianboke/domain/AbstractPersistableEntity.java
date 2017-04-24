package com.jianboke.domain;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;

/**
 * Created by pengxg on 2017/4/23.
 */
@MappedSuperclass
public abstract class AbstractPersistableEntity implements Persistable<Long>{

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
