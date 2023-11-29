package org.example.network;

import jakarta.transaction.Transactional;
import org.example.model.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
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

//    @Modifying
//    @Transactional
//    @Query("INSERT INTO UserEntity (userId, name) VALUES (:userId, :name)")
//    void insertUserEntity(@Param("userId") Long userId, @Param("name") String name);

    UserEntity save(UserEntity userEntity);

    UserEntity saveAndFlush(UserEntity userEntity);
}
