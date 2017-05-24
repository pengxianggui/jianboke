package com.jianboke.repository;

import com.jianboke.domain.PersistentLogin;
import com.jianboke.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, String> {

  List<PersistentLogin> findByUser(User user);

  List<PersistentLogin> findByLastUsedBefore(LocalDateTime dateTime);
  
  @Transactional
  @Modifying
  @Query(value = "DELETE FROM persistent_logins WHERE user_id IN (SELECT id FROM users  WHERE username=:username)", nativeQuery = true)
  void deleteByUsername(@Param("username") String username);

}
