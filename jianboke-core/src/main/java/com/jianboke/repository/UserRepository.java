package com.jianboke.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jianboke.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	Optional<User> findOneByUsername(String username);

	Optional<User> findOneByEmail(String email);

	@Query(value = "SELECT u.* FROM users u LEFT JOIN `fans_relation` fr ON fr.from_user_id = u.`id` WHERE fr.to_user_id =:id", nativeQuery = true)
    List<User> findFansByUserId(@Param("id") Long id);

	@Query(value = "SELECT COUNT(*) FROM `fans_relation` WHERE from_user_id =:id", nativeQuery = true)
	Integer getNumOfAttentions(@Param("id") Long id);

	@Query(value = "SELECT COUNT(*) FROM `fans_relation` WHERE to_user_id =:id", nativeQuery = true)
	Integer getNumOfFans(@Param("id") Long id);

	@Query(value = "SELECT COUNT(*) FROM `articles` WHERE author_id =:id OR second_author_id =:id", nativeQuery = true)
	Integer getNumOfArticles(@Param("id") Long id);
}
