package com.musicservice.domain.repository.jpa;

import com.musicservice.domain.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("jpaUserRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByName(String name);
    @NotNull Page<User> findAll(@NotNull Pageable pageable);
}
