package org.example.DataLayer;

import jakarta.validation.constraints.NotNull;
import org.example.EntityLayer.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByUserId(Long userId);
    boolean existsByName(String name);

    UserEntity save(@NotNull UserEntity userEntity);

    UserEntity findByUserId(Long userId);
}
