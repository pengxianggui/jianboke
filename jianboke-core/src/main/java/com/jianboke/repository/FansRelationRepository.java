package com.jianboke.repository;

import com.jianboke.domain.FansRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by pengxg on 2017/7/30.
 */
public interface FansRelationRepository extends JpaRepository<FansRelation, Long>, JpaSpecificationExecutor<FansRelation> {

    /**
     * 查所有粉丝
     * @param toUserId
     * @return
     */
    List<FansRelation> findAllByToUserId(Long toUserId);

    /**
     * 查所有关注
     * @param toUserId
     * @return
     */
    List<FansRelation> findAllByFromUserId(Long toUserId);
}
