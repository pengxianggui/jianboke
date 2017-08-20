package com.jianboke.repository;

import com.jianboke.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by pengxg on 2017/8/17.
 */
public interface ReplyRepository extends JpaRepository<Reply, Long>, JpaSpecificationExecutor<Reply> {

}
