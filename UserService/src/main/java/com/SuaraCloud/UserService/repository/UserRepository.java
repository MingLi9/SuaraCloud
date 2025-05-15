package com.SuaraCloud.UserService.repository;

import com.SuaraCloud.UserService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByTokenId(String tokenId);

    User findUserByTokenId(String tokenId);
}