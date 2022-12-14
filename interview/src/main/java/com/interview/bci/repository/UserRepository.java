package com.interview.bci.repository;

import com.interview.bci.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>
{
    boolean existsByEmail(String email);
}
