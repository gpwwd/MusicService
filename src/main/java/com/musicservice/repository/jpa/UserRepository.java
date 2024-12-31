package com.musicservice.repository.jpa;

import com.musicservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("jpaUserRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
}
