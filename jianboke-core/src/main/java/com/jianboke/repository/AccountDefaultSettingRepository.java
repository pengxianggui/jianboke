package com.jianboke.repository;

import com.jianboke.domain.AccountDefaultSetting;
import com.jianboke.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Created by pengxg on 2017/6/3.
 */
public interface AccountDefaultSettingRepository  extends JpaRepository<AccountDefaultSetting, Long>, JpaSpecificationExecutor<AccountDefaultSetting> {

    Optional<AccountDefaultSetting> findOneByUserId(Long id);
}
