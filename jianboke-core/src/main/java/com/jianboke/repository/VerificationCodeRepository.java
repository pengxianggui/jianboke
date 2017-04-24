package com.jianboke.repository;

import com.jianboke.domain.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Created by pengxg on 2017/4/22.
 */
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>, JpaSpecificationExecutor<VerificationCode> {

    Optional<VerificationCode> findOneByEmail(String email);
}
