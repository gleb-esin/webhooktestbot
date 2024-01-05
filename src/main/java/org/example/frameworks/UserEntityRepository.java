package org.example.frameworks;

import org.example.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByUserId(Long userId);
    boolean existsByName(String name);

    @Query("SELECT u.name FROM UserEntity u WHERE u.userId = :userId")
    String findNameByUserId(@Param("userId") Long userId);

    UserEntity save(UserEntity userEntity);

    UserEntity saveAndFlush(UserEntity userEntity);

    UserEntity findByUserId(Long userId);
}
